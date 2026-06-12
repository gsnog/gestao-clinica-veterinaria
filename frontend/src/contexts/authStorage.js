const STORAGE_KEY = 'vetcare-auth';

export function readStoredAuth() {
  try {
    const raw = window.sessionStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function writeStoredAuth(authUser) {
  if (authUser) {
    window.sessionStorage.setItem(STORAGE_KEY, JSON.stringify(authUser));
    return;
  }

  window.sessionStorage.removeItem(STORAGE_KEY);
}

export function getDefaultRoute(role) {
  if (role === 'TUTOR') return '/pets';
  if (role === 'ADMIN') return '/admin';
  return '/dashboard';
}
