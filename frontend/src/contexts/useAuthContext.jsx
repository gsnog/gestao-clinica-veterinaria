import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import domainService from '../services/domainService';
import { getDefaultRoute, readStoredAuth, writeStoredAuth } from './authStorage';

/**
 * Authentication context for managing user login state and role-based access.
 * Use the AuthProvider component and useAuth hook for accessing auth state.
 */
const AuthContext = createContext(null);

/**
 * Hook to access authentication context.
 * Must be used within an AuthProvider.
 * @returns {Object} Auth context with user, role, loading, and auth methods
 */
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

/**
 * Provider component for authentication context.
 * Wraps your app to provide auth state to all child components.
 * @param {React.ReactNode} children - Child components
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => readStoredAuth());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;

    const fetchProfile = async () => {
      try {
        const result = await domainService.getPerfil();
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
        setError(null);
      } catch (err) {
        if (!active) return;
        setUser(null);
        writeStoredAuth(null);
        setError(err.message);
      } finally {
        if (active) setLoading(false);
      }
    };

    fetchProfile();

    return () => {
      active = false;
    };
  }, []);

  const value = useMemo(() => ({
    user,
    role: user?.role || null,
    loading,
    error,
    defaultRoute: getDefaultRoute(user?.role),
    setUser(authUser) {
      setUser(authUser);
      writeStoredAuth(authUser);
    },
    logout() {
      setUser(null);
      writeStoredAuth(null);
      setError(null);
    },
  }), [loading, user, error]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthContext;
