import { Link, useNavigate, useParams } from "react-router-dom";
import { useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";
import {
  formatTelefone,
  isEmailValid,
  isTelefoneValid,
} from "../utils/formatters";

function TutorFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ nome: "", email: "", telefone: "" });
  const [error, setError] = useState("");

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "telefone" ? formatTelefone(value) : value,
    }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    if (!isEmailValid(form.email)) {
      setError("Informe um e-mail válido.");
      return;
    }

    if (!isTelefoneValid(form.telefone)) {
      setError("Telefone inválido.");
      return;
    }

    try {
      await domainService.saveTutor(form, id);
      setError("");
      navigate("/tutores");
    } catch (requestError) {
      setError(requestError.message || "Não foi possível salvar o tutor.");
    }
  }

  return (
    <main className="main">
      <Topbar
        title="Formulário de Tutor"
        subtitle="Cadastro e edição de tutor"
      />

      <section className="form-card">
        <aside className="form-sidebar">
          <img
            src="/images/tutor.webp"
            alt="Tutor"
            className="form-sidebar-image"
          />
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados do tutor</h2>
          <p className="form-subtitle">
            Preencha corretamente os campos para continuar.
          </p>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="nome">Nome</label>
              <input
                id="nome"
                name="nome"
                value={form.nome}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email">E-mail</label>
              <input
                id="email"
                name="email"
                type="email"
                value={form.email}
                onChange={onChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="telefone">Telefone</label>
              <input
                id="telefone"
                name="telefone"
                value={form.telefone}
                onChange={onChange}
                placeholder="(99) 99999-9999"
                required
              />
            </div>
          </div>

          {error ? <p className="auth-message auth-error">{error}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/tutores">
              Cancelar
            </Link>
            <button type="submit" className="btn btn-submit">
              Salvar
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default TutorFormPage;
