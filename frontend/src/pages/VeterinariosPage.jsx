import { Link } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import Topbar from "../components/Topbar";
import domainService from "../services/domainService";
import { normalizeText } from "../utils/formatters";

function VeterinariosPage() {
  const [veterinarios, setVeterinarios] = useState([]);
  const [busca, setBusca] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    domainService
      .listVeterinarios()
      .then((result) => {
        setVeterinarios(result.items || result);
        setErro("");
      })
      .catch((error) =>
        setErro(error.message || "Erro ao carregar veterinários."),
      );
  }, []);

  const veterinariosFiltrados = useMemo(() => {
    const query = normalizeText(busca);
    if (!query) return veterinarios;

    return veterinarios.filter((veterinario) => {
      const nome = normalizeText(veterinario.nome);
      const crmv = normalizeText(veterinario.crmv);
      const especialidade = normalizeText(veterinario.especialidade);
      const id = veterinario.id.toString();
      return (
        nome.includes(query) ||
        crmv.includes(query) ||
        especialidade.includes(query) ||
        id.includes(query)
      );
    });
  }, [busca, veterinarios]);

  async function remover(id) {
    if (!window.confirm("Deseja excluir este veterinário?")) return;

    try {
      await domainService.deleteVeterinario(id);
      setVeterinarios((prev) =>
        prev.filter((veterinario) => veterinario.id !== id),
      );
      setErro("");
    } catch (error) {
      setErro(error.message || "Não foi possível excluir o veterinário.");
    }
  }

  return (
    <main className="main">
      <Topbar title="Veterinários" subtitle="Equipe médica da clínica" />

      <div className="search-bar">
        <input
          type="text"
          placeholder="Busca Nome, CRMV, especialidade ou ID"
          value={busca}
          onChange={(event) => setBusca(event.target.value)}
        />
      </div>

      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <section className="card">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>CRMV</th>
              <th>Especialidade</th>
              <th>ID</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {veterinariosFiltrados.map((veterinario) => (
              <tr key={veterinario.id}>
                <td>{veterinario.nome}</td>
                <td>{veterinario.crmv}</td>
                <td>{veterinario.especialidade}</td>
                <td>#{veterinario.id}</td>
                <td>
                  <div className="actions">
                    <Link
                      className="btn btn-edit"
                      to={`/veterinarios/${veterinario.id}/editar`}
                    >
                      Editar
                    </Link>
                    <button
                      type="button"
                      className="btn btn-danger"
                      onClick={() => remover(veterinario.id)}
                    >
                      Excluir
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </main>
  );
}

export default VeterinariosPage;
