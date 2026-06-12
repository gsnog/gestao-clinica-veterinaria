import { Link } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { normalizeText } from '../utils/formatters';

function TutoresPage() {
  const [tutores, setTutores] = useState([]);
  const [busca, setBusca] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listTutores()
      .then((result) => {
        setTutores(result.items || result);
        setErro('');
      })
      .catch((error) => setErro(error.message || 'Erro ao carregar tutores.'));
  }, []);

  const tutoresFiltrados = useMemo(() => {
    const query = normalizeText(busca);
    if (!query) return tutores;

    return tutores.filter((tutor) => {
      const nome = normalizeText(tutor.nome);
      const telefone = normalizeText(tutor.telefone);
      return nome.includes(query) || telefone.includes(query);
    });
  }, [busca, tutores]);

  async function remover(id) {
    if (!window.confirm('Deseja excluir este tutor?')) return;

    try {
      await domainService.deleteTutor(id);
      setTutores((prev) => prev.filter((tutor) => tutor.id !== id));
      setErro('');
    } catch (error) {
      setErro(error.message || 'Não foi possível excluir o tutor.');
    }
  }

  return (
    <main className="main">
      <Topbar title="Tutores" subtitle="Cadastro de responsáveis" />

      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nome ou telefone"
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
              <th>Telefone</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {tutoresFiltrados.map((tutor) => (
              <tr key={tutor.id}>
                <td className="cap">{tutor.nome}</td>
                <td>{tutor.telefone || '-'}</td>
                <td>
                  <div className="actions">
                    <Link className="btn btn-edit" to={`/tutores/${tutor.id}/editar`}>Editar</Link>
                    <button type="button" className="btn btn-danger" onClick={() => remover(tutor.id)}>Excluir</button>
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

export default TutoresPage;
