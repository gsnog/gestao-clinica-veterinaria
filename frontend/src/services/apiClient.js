const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/clinica';
const MUTATING_METHODS = new Set(['POST', 'PUT', 'DELETE']);

let csrfToken = null;
let csrfTokenRequest = null;

export function clearCsrfToken() {
  csrfToken = null;
  csrfTokenRequest = null;
}

async function fetchCsrfToken() {
  if (csrfToken) return csrfToken;
  if (csrfTokenRequest) return csrfTokenRequest;

  csrfTokenRequest = fetch(`${API_BASE_URL}/api/csrf`, {
    credentials: 'include',
    headers: { Accept: 'application/json' },
  })
    .then(async (response) => {
      if (!response.ok) {
        throw new Error('Não foi possível obter o token CSRF.');
      }
      const corpo = await response.json();
      if (!corpo?.csrfToken) {
        throw new Error('Resposta CSRF inválida.');
      }
      csrfToken = corpo.csrfToken;
      return csrfToken;
    })
    .finally(() => {
      csrfTokenRequest = null;
    });

  return csrfTokenRequest;
}

async function apiFetch(path, options = {}) {
  const method = (options.method || 'GET').toUpperCase();
  const headers = new Headers({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  new Headers(options.headers || {}).forEach((value, key) => {
    headers.set(key, value);
  });

  if (MUTATING_METHODS.has(method)) {
    headers.set('X-CSRF-Token', await fetchCsrfToken());
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    credentials: 'include',
    ...options,
    method,
    headers,
  });

  if (!response.ok) {
    let mensagem = 'Falha ao comunicar com a API.';
    let fieldErrors;
    try {
      const corpo = await response.json();
      if (corpo?.message) mensagem = corpo.message;
      fieldErrors = corpo?.fieldErrors;
    } catch {
      // resposta de erro sem corpo JSON: mantém a mensagem genérica
    }
    const erro = new Error(mensagem);
    if (fieldErrors) erro.fieldErrors = fieldErrors;
    throw erro;
  }

  const contentType = response.headers.get('content-type') || '';
  if (!contentType.includes('application/json')) {
    throw new Error('O endpoint não retornou JSON. Verifique o backend/API.');
  }

  const corpo = await response.json();

  if (path === '/api/login' && method === 'POST') {
    clearCsrfToken();
  }

  return corpo;
}

export function toFormBody(payload) {
  const form = new URLSearchParams();
  Object.entries(payload).forEach(([key, value]) => {
    if (value !== undefined && value !== null && `${value}` !== '') {
      form.append(key, `${value}`);
    }
  });
  return form;
}

export default apiFetch;
