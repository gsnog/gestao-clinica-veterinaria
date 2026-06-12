import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";
import { isEmailValid } from "../utils/formatters";

const PAPEIS = [
  { value: "TUTOR", label: "Tutor" },
  { value: "VETERINARIO", label: "Veterinário" },
  { value: "ADMIN", label: "Admin" },
];

function UsuarioFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nome: "",
    email: "",
    role: "TUTOR",
    senha: "",
  });
  const [erro, setErro] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});

  useEffect(() => {
    if (!id) return;

    domainService
      .listUsuarios()
      .then((result) => {
        const usuarios = result.items || result || [];
        const usuario = usuarios.find((item) => String(item?.id) === String(id));

        if (!usuario) {
          throw new Error();
        }

        setForm((prev) => ({
          ...prev,
          nome: usuario.nome || "",
          email: usuario.email || "",
          role: usuario.role || "TUTOR",
        }));
        setErro("");
      })
      .catch(() => {
        setErro("Não foi possível carregar os dados do usuário.");
      });
  }, [id]);

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();
    setFieldErrors({});

    if (!isEmailValid(form.email)) {
      setErro("Informe um e-mail válido.");
      return;
    }

    if (!id && !form.senha) {
      setErro("Informe a senha do novo usuário.");
      return;
    }

    const payload = {
      nome: form.nome,
      email: form.email,
      role: form.role,
    };
    if (form.senha) {
      payload.senha = form.senha;
    }

    try {
      await domainService.saveUsuario(payload, id);
      setErro("");
      navigate("/usuarios");
    } catch (error) {
      setErro(error.message || "Não foi possível salvar o usuário.");
      if (error.fieldErrors) setFieldErrors(error.fieldErrors);
    }
  }

  return (
    <main className="main">
      <Topbar
        title={id ? "Editar Usuário" : "Novo Usuário"}
        subtitle="Gestão de contas da plataforma"
      />

      <section className="form-card">
        <aside className="form-sidebar">
          <div className="form-sidebar-icon">🛡️</div>
          <h2 className="form-sidebar-title">Usuário</h2>
          <p className="form-sidebar-text">
            Defina o papel do usuário com cuidado: ele controla o acesso às áreas do sistema.
          </p>
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados do usuário</h2>
          <p className="form-subtitle">
            Preencha os dados obrigatórios para {id ? "atualizar" : "criar"} a conta.
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
              {fieldErrors.nome ? <p className="auth-message auth-error">{fieldErrors.nome}</p> : null}
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
              {fieldErrors.email ? <p className="auth-message auth-error">{fieldErrors.email}</p> : null}
            </div>

            <div className="form-group">
              <label htmlFor="role">Papel</label>
              <select id="role" name="role" value={form.role} onChange={onChange} required>
                {PAPEIS.map((papel) => (
                  <option key={papel.value} value={papel.value}>
                    {papel.label}
                  </option>
                ))}
              </select>
              {fieldErrors.role ? <p className="auth-message auth-error">{fieldErrors.role}</p> : null}
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="senha">
                {id ? "Nova senha (opcional)" : "Senha"}
              </label>
              <input
                id="senha"
                name="senha"
                type="password"
                value={form.senha}
                onChange={onChange}
                placeholder={id ? "Deixe em branco para manter a senha atual" : ""}
                required={!id}
              />
              {fieldErrors.senha ? <p className="auth-message auth-error">{fieldErrors.senha}</p> : null}
              <p className="form-hint">
                Mínimo de 8 caracteres, com letra maiúscula, minúscula e número.
              </p>
            </div>
          </div>

          {erro ? <p className="auth-message auth-error">{erro}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/usuarios">
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

export default UsuarioFormPage;
