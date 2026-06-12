import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";

const galleryImages = [
  "pet01.jpg",
  "pet02.jpg",
  "pet03.jpg",
  "pet04.jpg",
  "pet05.jpg",
  "pet06.jpg",
  "pet07.jpg",
  "pet08.jpg",
  "pet09.jpg",
  "pet010.jpg",
  "pet011.jpg",
  "pet012.jpg",
].map((file) => `${import.meta.env.BASE_URL}images/${file}`);

function DashboardPage() {
  const [stats, setStats] = useState({
    totalPets: 0,
    totalTutores: 0,
    totalVeterinarios: 0,
    totalConsultas: 0,
  });
  const [consultas, setConsultas] = useState([]);
  const [erro, setErro] = useState("");
  const [lightbox, setLightbox] = useState("");

  useEffect(() => {
    Promise.all([domainService.getDashboard(), domainService.listConsultas()])
      .then(([dashboardResult, consultasResult]) => {
        setStats(dashboardResult.estatisticas || dashboardResult);
        setConsultas(consultasResult.items || consultasResult);
        setErro("");
      })
      .catch((error) => {
        setErro(
          error.message || "Não foi possível carregar os dados do dashboard.",
        );
      });
  }, []);

  return (
    <main className="main">
      <Topbar
        title="Dashboard"
        subtitle="Visão geral da clínica"
        action={
          <Link className="btn btn-primary" to="/consultas/nova">
            + Nova consulta
          </Link>
        }
      />

      <section className="stats-grid">
        <article className="stat-card">
          <div className="stat-icon rose">🐶</div>
          <div>
            <div className="stat-number">{stats.totalPets}</div>
            <div className="stat-label">Pets cadastrados</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon lav">👤</div>
          <div>
            <div className="stat-number">{stats.totalTutores}</div>
            <div className="stat-label">Tutores</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon sand">🩺</div>
          <div>
            <div className="stat-number">{stats.totalVeterinarios}</div>
            <div className="stat-label">Veterinários</div>
          </div>
        </article>

        <article className="stat-card">
          <div className="stat-icon mint">📋</div>
          <div>
            <div className="stat-number">{stats.totalConsultas}</div>
            <div className="stat-label">Consultas</div>
          </div>
        </article>
      </section>

      <section className="card">
        <div className="card-header">
          <h2 className="card-title">Consultas recentes</h2>
        </div>
        {erro ? (
          <p
            className="filter-feedback mb-16"
            style={{ padding: "14px 28px 0" }}
          >
            {erro}
          </p>
        ) : null}
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Pet</th>
              <th>Veterinário</th>
              <th>Motivo</th>
            </tr>
          </thead>
          <tbody>
            {consultas.map((consulta) => (
              <tr key={consulta.id}>
                <td>
                  {new Date(consulta.dataConsulta).toLocaleString("pt-BR")}
                </td>
                <td>{consulta.pet?.nome}</td>
                <td>{consulta.veterinario?.nome}</td>
                <td>{consulta.motivo}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      <div className="quick-access">
        <Link className="btn btn-filter" to="/pets/novo">
          Novo pet
        </Link>
        <Link className="btn btn-filter" to="/consultas/nova">
          Nova consulta
        </Link>
        <Link className="btn btn-filter" to="/perfil">
          Editar perfil
        </Link>
      </div>

      <section className="card">
        <div className="card-header">
          <h2 className="card-title">Galeria</h2>
        </div>

        <div className="gallery-wrapper">
          <div className="gallery">
            {galleryImages.map((src) => (
              <button
                key={src}
                type="button"
                className="gallery-item"
                onClick={() => setLightbox(src)}
              >
                <img src={src} alt="pet" />
              </button>
            ))}
          </div>
        </div>
      </section>

      <div
        className={`lightbox ${lightbox ? "open" : ""}`}
        onClick={() => setLightbox("")}
      >
        <button
          type="button"
          className="lightbox-close"
          onClick={() => setLightbox("")}
        >
          ×
        </button>
        {lightbox ? <img src={lightbox} alt="pet ampliado" /> : null}
      </div>
    </main>
  );
}

export default DashboardPage;
