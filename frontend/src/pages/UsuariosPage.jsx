import { Link } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import Topbar from "../components/Topbar";
import SearchBar from "../components/SearchBar";
import DataTable from "../components/DataTable";
import domainService from "../services/domainService";
import { useAuth } from "../contexts/authContext";
import { normalizeText } from "../utils/formatters";

const ROLE_LABELS = {
  TUTOR: "Tutor",
  VETERINARIO: "Veterinário",
  ADMIN: "Admin",
};

function UsuariosPage() {
  const { user } = useAuth();
  const [usuarios, setUsuarios] = useState([]);
  const [busca, setBusca] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    domainService
      .listUsuarios()
      .then((result) => {
        setUsuarios(result.items || result);
        setErro("");
      })
      .catch((error) => setErro(error.message || "Erro ao carregar usuários."));
  }, []);

  const usuariosFiltrados = useMemo(() => {
    const query = normalizeText(busca);
    if (!query) return usuarios;

    return usuarios.filter((usuario) => {
      const nome = normalizeText(usuario.nome);
      const email = normalizeText(usuario.email);
      const role = normalizeText(usuario.role);
      return nome.includes(query) || email.includes(query) || role.includes(query);
    });
  }, [busca, usuarios]);

  async function remover(usuario) {
    if (!window.confirm(`Deseja excluir o usuário "${usuario.nome}"?`)) return;

    try {
      await domainService.deleteUsuario(usuario.id);
      setUsuarios((prev) => prev.filter((item) => item.id !== usuario.id));
      setErro("");
    } catch (error) {
      setErro(error.message || "Não foi possível excluir o usuário.");
    }
  }

  const columns = [
    { key: "nome", header: "Nome", className: "cap", render: (usuario) => usuario.nome },
    { key: "email", header: "E-mail", render: (usuario) => usuario.email },
    { key: "papel", header: "Papel", render: (usuario) => ROLE_LABELS[usuario.role] || usuario.role },
    {
      key: "acoes",
      header: "Ações",
      render: (usuario) => (
        <div className="actions">
          <Link className="btn btn-edit" to={`/usuarios/${usuario.id}/editar`}>
            Editar
          </Link>
          <button
            type="button"
            className="btn btn-danger"
            disabled={usuario.id === user?.id}
            title={usuario.id === user?.id ? "Você não pode excluir a própria conta." : ""}
            onClick={() => remover(usuario)}
          >
            Excluir
          </button>
        </div>
      ),
    },
  ];

  return (
    <main className="main">
      <Topbar
        title="Usuários"
        subtitle="Gestão de contas da plataforma"
        action={
          <Link className="btn btn-primary" to="/usuarios/novo">
            + Novo usuário
          </Link>
        }
      />

      <SearchBar placeholder="Buscar por nome, e-mail ou papel" value={busca} onChange={setBusca} />

      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <DataTable columns={columns} data={usuariosFiltrados} rowKey={(usuario) => usuario.id} emptyMessage="Nenhum usuário encontrado." />
    </main>
  );
}

export default UsuariosPage;
