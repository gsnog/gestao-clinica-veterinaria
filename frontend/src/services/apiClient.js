const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/gestao-clinica-veterinaria';

async function apiFetch(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    credentials: 'include',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    throw new Error('Falha ao comunicar com a API.');
  }

  const contentType = response.headers.get('content-type') || '';
  if (!contentType.includes('application/json')) {
    throw new Error('O endpoint não retornou JSON. Verifique o backend/API.');
  }

  return response.json();
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
