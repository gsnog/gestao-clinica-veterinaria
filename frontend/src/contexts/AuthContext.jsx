import { useEffect, useMemo, useState } from 'react';
import domainService from '../services/domainService';
import { clearCsrfToken } from '../services/apiClient';
import AuthContext from './authContext';
import { getDefaultRoute, readStoredAuth, writeStoredAuth } from './authStorage';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => readStoredAuth());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;

    domainService.getPerfil()
      .then((result) => {
        if (!active) return;
        const usuario = result.usuario || result;
        const normalizedUser = {
          id: usuario.id,
          nome: usuario.nome,
          email: usuario.email,
          role: usuario.role,
        };
        setUser(normalizedUser);
        writeStoredAuth(normalizedUser);
      })
      .catch(() => {
        if (!active) return;
        setUser(null);
        writeStoredAuth(null);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const value = useMemo(() => ({
    user,
    role: user?.role || null,
    loading,
    defaultRoute: getDefaultRoute(user?.role),
    setUser(authUser) {
      setUser(authUser);
      writeStoredAuth(authUser);
    },
    async logout() {
      try {
        await domainService.logout();
      } catch {
        // Mesmo se a API falhar, a UI local deve sair da sessão.
      } finally {
        clearCsrfToken();
        setUser(null);
        writeStoredAuth(null);
      }
    },
  }), [loading, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
