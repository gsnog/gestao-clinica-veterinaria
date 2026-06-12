import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";

function AdminDashboardPage() {
  const [stats, setStats] = useState({
    totalPets: 0,
    totalTutores: 0,
    totalVeterinarios: 0,
    totalConsultas: 0,
    totalAdmins: 0,
  });
  const [erro, setErro] = useState("");

  useEffect(() => {
    domainService
      .getAdminDashboard()
      .then((result) => {
        setStats({
          ...(result.estatisticas || {}),
          totalTutores: result.totalTutores ?? result.estatisticas?.totalTutores ?? 0,
          totalVeterinarios:
            result.totalVeterinarios ?? result.estatisticas?.totalVeterinarios ?? 0,
          totalAdmins: result.totalAdmins ?? 0,
        });
        setErro("");
      })
      .catch((error) => {
        setErro(error.message || "Não foi possível carregar o painel administrativo.");
      });
  }, []);

  return (
    <main className="main">
      <Topbar
        title="Painel Administrativo"
        subtitle="Visão geral da plataforma"
        action={
          <Link className="btn btn-primary" to="/usuarios">
            Gerenciar usuários
          </Link>
        }
      />

      {erro ? <p className="auth-message auth-error">{erro}</p> : null}

      <section className="stats-grid">
        <article className="stat-card">
          <div className="stat-icon rose">🐶</div>
          <div>
            <div className="stat-number">{stats.totalPets ?? 0}</div>
            <div className="stat-label">Pets cadastrados</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon lav">👤</div>
          <div>
            <div className="stat-number">{stats.totalTutores ?? 0}</div>
            <div className="stat-label">Tutores</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon sand">🩺</div>
          <div>
            <div className="stat-number">{stats.totalVeterinarios ?? 0}</div>
            <div className="stat-label">Veterinários</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon mint">📋</div>
          <div>
            <div className="stat-number">{stats.totalConsultas ?? 0}</div>
            <div className="stat-label">Consultas</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon rose">🛡️</div>
          <div>
            <div className="stat-number">{stats.totalAdmins ?? 0}</div>
            <div className="stat-label">Administradores</div>
          </div>
        </article>
      </section>

      <div className="quick-access">
        <Link className="btn btn-filter" to="/usuarios">
          Listar usuários
        </Link>
        <Link className="btn btn-filter" to="/usuarios/novo">
          Novo usuário
        </Link>
        <Link className="btn btn-filter" to="/perfil">
          Editar perfil
        </Link>
      </div>
    </main>
  );
}

export default AdminDashboardPage;
