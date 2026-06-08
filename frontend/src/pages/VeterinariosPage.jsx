import { Link } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { useAuth } from '../contexts/authContext';
import { normalizeText } from '../utils/formatters';

function VeterinariosPage() {
  const { user } = useAuth();
  const [veterinarios, setVeterinarios] = useState([]);
  const [busca, setBusca] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listVeterinarios()
      .then((result) => {
        setVeterinarios(result.items || result);
        setErro('');
      })
      .catch((error) => setErro(error.message || 'Erro ao carregar veterinários.'));
  }, []);

  const veterinariosFiltrados = useMemo(() => {
    const query = normalizeText(busca);
    const visiveis = veterinarios;

    if (!query) return visiveis;

    return visiveis.filter((veterinario) => {
      const nome = normalizeText(veterinario.nome);
      const crmv = normalizeText(veterinario.crmv);
      const especialidade = normalizeText(veterinario.especialidade);
      return nome.includes(query) || crmv.includes(query) || especialidade.includes(query);
    });
  }, [busca, veterinarios]);

  const veterinarioLogado = useMemo(
    () => veterinarios.find((veterinario) => veterinario.id === user?.id) || null,
    [user?.id, veterinarios]
  );

  async function remover(id) {
    if (!window.confirm('Deseja excluir este veterinário?')) return;

    try {
      await domainService.deleteVeterinario(id);
      setVeterinarios((prev) => prev.filter((veterinario) => veterinario.id !== id));
      setErro('');
    } catch (error) {
      setErro(error.message || 'Não foi possível excluir o veterinário.');
    }
  }

  return (
    <main className="main">
      <Topbar title="Veterinários" subtitle="Equipe clínica" />

      {veterinarioLogado ? (
        <section className="card mb-24">
          <div className="card-header vet-highlight-header">
            <div>
              <h2 className="card-title">Meu perfil profissional</h2>
              <p className="page-subtitle">Seu acesso de edição fica centralizado aqui.</p>
            </div>
            <Link className="btn btn-primary" to="/perfil">Editar meu perfil</Link>
          </div>
          <div className="profile-grid">
            <div className="profile-row">
              <span className="profile-label">Nome</span>
              <span className="profile-value">{veterinarioLogado.nome}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">CRMV</span>
              <span className="profile-value">{veterinarioLogado.crmv}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Especialidade</span>
              <span className="badge badge-lav">{veterinarioLogado.especialidade}</span>
            </div>
          </div>
        </section>
      ) : null}

      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nome, CRMV ou especialidade"
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
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {veterinariosFiltrados.map((veterinario) => (
              <tr key={veterinario.id}>
                <td className="cap">{veterinario.nome}</td>
                <td>{veterinario.crmv}</td>
                <td><span className="badge badge-lav">{veterinario.especialidade}</span></td>
                <td>
                  <div className="actions">
                    {veterinario.id === user?.id ? (
                      <Link className="btn btn-edit" to="/perfil">Meu perfil</Link>
                    ) : (
                      <button
                        type="button"
                        className="btn btn-edit btn-disabled"
                        title="Não é possível editar outros veterinários."
                        disabled
                      >
                        Editar
                      </button>
                    )}
                    <button
                      type="button"
                      className="btn btn-danger"
                      onClick={() => remover(veterinario.id)}
                      disabled={veterinario.id !== user?.id}
                      title={veterinario.id !== user?.id ? 'Não é possível excluir outros veterinários.' : undefined}
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
