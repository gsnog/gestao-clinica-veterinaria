import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/authContext';
import { getDefaultRoute } from '../contexts/authStorage';

function ProtectedRoute({ allowedRoles }) {
  const { role, loading } = useAuth();

  if (loading) {
    return <main className="main"><p className="text-muted">Carregando permissões...</p></main>;
  }

  if (!role) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(role)) {
    return <Navigate to={getDefaultRoute(role)} replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;
