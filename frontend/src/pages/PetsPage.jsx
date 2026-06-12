import { Link } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { normalizeText } from '../utils/formatters';

function PetsPage() {
  const [pets, setPets] = useState([]);
  const [busca, setBusca] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listPets()
      .then((result) => {
        setPets(result.items || result);
        setErro('');
      })
      .catch((error) => setErro(error.message || 'Erro ao carregar pets.'));
  }, []);

  const petsFiltrados = useMemo(() => {
    const query = normalizeText(busca);
    if (!query) return pets;

    return pets.filter((pet) => {
      const nome = normalizeText(pet.nome);
      const raca = normalizeText(pet.raca);
      const tutor = normalizeText(pet.tutor?.nome);
      return nome.includes(query) || raca.includes(query) || tutor.includes(query);
    });
  }, [busca, pets]);

  async function remover(id) {
    if (!window.confirm('Deseja excluir este pet?')) return;

    try {
      await domainService.deletePet(id);
      setPets((prev) => prev.filter((pet) => pet.id !== id));
      setErro('');
    } catch (error) {
      setErro(error.message || 'Não foi possível excluir o pet.');
    }
  }

  return (
    <main className="main">
      <Topbar
        title="Pets"
        subtitle="Lista de pets cadastrados"
        action={<Link className="btn btn-primary" to="/pets/novo">+ Novo pet</Link>}
      />

      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nome, raça ou tutor"
          value={busca}
          onChange={(event) => setBusca(event.target.value)}
        />
      </div>

      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <section className="card">
        <table>
          <thead>
            <tr>
              <th>Pet</th>
              <th>Tutor</th>
              <th>Nascimento</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {petsFiltrados.map((pet) => (
              <tr key={pet.id}>
                <td>
                  <div className="pet-cell">
                    <span className="pet-avatar">🐾</span>
                    <div>
                      <div className="pet-name">{pet.nome}</div>
                      <div className="pet-breed">{pet.raca}</div>
                    </div>
                  </div>
                </td>
                <td>{pet.tutor?.nome || '-'}</td>
                <td>{new Date(pet.dataNascimento).toLocaleDateString('pt-BR')}</td>
                <td>
                  <div className="actions">
                    <Link className="btn btn-edit" to={`/pets/${pet.id}/editar`}>Editar</Link>
                    <button type="button" className="btn btn-danger" onClick={() => remover(pet.id)}>Excluir</button>
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

export default PetsPage;
