import { Navigate, Route, Routes } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import ProtectedRoute from './ProtectedRoute';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import DashboardPage from '../pages/DashboardPage';
import PetsPage from '../pages/PetsPage';
import PetFormPage from '../pages/PetFormPage';
import ConsultasPage from '../pages/ConsultasPage';
import ConsultaFormPage from '../pages/ConsultaFormPage';
import TutoresPage from '../pages/TutoresPage';
import TutorFormPage from '../pages/TutorFormPage';
import VeterinariosPage from '../pages/VeterinariosPage';
import VeterinarioFormPage from '../pages/VeterinarioFormPage';
import PerfilPage from '../pages/PerfilPage';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/registro" element={<RegisterPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route element={<ProtectedRoute allowedRoles={['TUTOR', 'VETERINARIO']} />}>
            <Route path="/consultas" element={<ConsultasPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={['VETERINARIO']} />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/consultas/nova" element={<ConsultaFormPage />} />
            <Route path="/consultas/:id/editar" element={<ConsultaFormPage />} />
            <Route path="/veterinarios" element={<VeterinariosPage />} />
            <Route path="/veterinarios/:id/editar" element={<VeterinarioFormPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={['TUTOR']} />}>
            <Route path="/pets" element={<PetsPage />} />
            <Route path="/pets/novo" element={<PetFormPage />} />
            <Route path="/pets/:id/editar" element={<PetFormPage />} />
            <Route path="/tutores" element={<TutoresPage />} />
            <Route path="/tutores/:id/editar" element={<TutorFormPage />} />
          </Route>

          <Route path="/perfil" element={<PerfilPage />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

export default AppRoutes;
