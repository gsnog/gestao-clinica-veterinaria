import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";
import { useAuth } from "../contexts/authContext";
import { formatTelefone, isTelefoneValid } from "../utils/formatters";

const roleBadgeClass = {
  ADMIN: "badge-sand",
  TUTOR: "badge-rose",
  VETERINARIO: "badge-lav",
};

function PerfilPage() {
  const navigate = useNavigate();
  const { logout, user } = useAuth();
  const [perfil, setPerfil] = useState(null);
  const [email, setEmail] = useState("");
  const [telefone, setTelefone] = useState("");
  const [feedback, setFeedback] = useState({ type: "", message: "" });

  useEffect(() => {
    domainService
      .getPerfil()
      .then((result) => {
        const usuario = result.usuario || result;
        setPerfil(usuario);
        setEmail(result.email || usuario.email || "");
        setTelefone(result.telefone || usuario.telefone || "");
        setFeedback({ type: "", message: "" });
      })
      .catch((error) => {
        setFeedback({
          type: "error",
          message: error.message || "Erro ao carregar perfil.",
        });
      });
  }, []);

  const badgeClass = useMemo(
    () => roleBadgeClass[perfil?.role] || "badge-mint",
    [perfil?.role],
  );

  function handleLogout() {
    logout();
    navigate("/login");
  }

  async function handleDeleteConta() {
    if (perfil?.role !== "TUTOR" || !user?.id) return;
    if (
      !window.confirm(
        "Deseja excluir sua conta? Esta ação não pode ser desfeita.",
      )
    )
      return;

    try {
      await domainService.deleteTutor(user.id);
      logout();
      navigate("/login");
    } catch (error) {
      setFeedback({
        type: "error",
        message: error.message || "Não foi possível excluir a conta.",
      });
    }
  }

  function onSubmit(event) {
    event.preventDefault();

    const roleComTelefone =
      perfil?.role === "TUTOR" || perfil?.role === "VETERINARIO";

    if (roleComTelefone && !isTelefoneValid(telefone)) {
      setFeedback({
        type: "error",
        message: "Telefone invalido. Use o formato (DDD) 99999-9999.",
      });
      return;
    }

    const payload = roleComTelefone ? { email, telefone } : { email };

    domainService
      .updatePerfil(payload)
      .then(() => {
        setFeedback({
          type: "success",
          message: "Perfil atualizado com sucesso.",
        });
      })
      .catch((error) => {
        setFeedback({
          type: "error",
          message: error.message || "Não foi possível atualizar o perfil.",
        });
      });
  }

  return (
    <main className="main profile-layout">
      <Topbar
        title="Meu Perfil"
        subtitle="Informações da sua conta no sistema"
        action={
          <button
            className="btn btn-outline profile-logout-btn"
            onClick={handleLogout}
          >
            Sair
          </button>
        }
      />

      <section className="card mb-24">
        <div className="card-header">
          <h2 className="card-title">Dados da Conta</h2>
        </div>

        {perfil ? (
          <div className="profile-grid">
            <div className="profile-row">
              <span className="profile-label">ID</span>
              <span className="profile-value">{user?.id}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Nome</span>
              <span className="profile-value">{perfil.nome}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">E-mail</span>
              <span className="profile-value">{perfil.email}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Perfil</span>
              <span className={`badge profile-role-badge ${badgeClass}`}>
                {perfil.role}
              </span>
            </div>
          </div>
        ) : null}
      </section>

      <section className="card">
        <div className="card-header">
          <h2 className="profile-card-title">
            <span className="profile-card-icon">✉</span>
            E-mail
          </h2>
        </div>

        <div className="profile-form-wrap">
          <form className="profile-form" onSubmit={onSubmit}>
            <div className="form-row single mb-0">
              <div className="form-group">
                <label htmlFor="email">E-mail</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  required
                />
              </div>
            </div>

            {perfil?.role === "TUTOR" || perfil?.role === "VETERINARIO" ? (
              <div className="form-row single mb-0">
                <div className="form-group">
                  <label htmlFor="telefone">Telefone</label>
                  <input
                    id="telefone"
                    name="telefone"
                    type="text"
                    value={telefone}
                    onChange={(event) =>
                      setTelefone(formatTelefone(event.target.value))
                    }
                    placeholder="(DDD) 99999-9999"
                    required
                  />
                  <small className="text-muted">
                    Formato: (DDD) 99999-9999
                  </small>
                </div>
              </div>
            ) : null}

            {feedback.message ? (
              <p
                className={`profile-feedback ${feedback.type === "success" ? "auth-success" : "auth-error"}`}
              >
                {feedback.message}
              </p>
            ) : null}

            <div className="profile-actions">
              <button type="submit" className="btn btn-submit">
                Salvar informações de contato
              </button>
              {perfil?.role === "TUTOR" ? (
                <button
                  type="button"
                  className="btn btn-danger-outline"
                  onClick={handleDeleteConta}
                >
                  Excluir minha conta
                </button>
              ) : null}
            </div>
          </form>
        </div>
      </section>
    </main>
  );
}

export default PerfilPage;
