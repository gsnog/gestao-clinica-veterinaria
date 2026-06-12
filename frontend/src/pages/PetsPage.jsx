import { Link } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import SearchBar from '../components/SearchBar';
import DataTable from '../components/DataTable';
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

  const columns = [
    {
      key: 'pet',
      header: 'Pet',
      render: (pet) => (
        <div className="pet-cell">
          <span className="pet-avatar">🐾</span>
          <div>
            <div className="pet-name">{pet.nome}</div>
            <div className="pet-breed">{pet.raca}</div>
          </div>
        </div>
      ),
    },
    { key: 'tutor', header: 'Tutor', render: (pet) => pet.tutor?.nome || '-' },
    {
      key: 'nascimento',
      header: 'Nascimento',
      render: (pet) => new Date(pet.dataNascimento).toLocaleDateString('pt-BR'),
    },
    {
      key: 'acoes',
      header: 'Ações',
      render: (pet) => (
        <div className="actions">
          <Link className="btn btn-edit" to={`/pets/${pet.id}/editar`}>Editar</Link>
          <button type="button" className="btn btn-danger" onClick={() => remover(pet.id)}>Excluir</button>
        </div>
      ),
    },
  ];

  return (
    <main className="main">
      <Topbar
        title="Pets"
        subtitle="Lista de pets cadastrados"
        action={<Link className="btn btn-primary" to="/pets/novo">+ Novo pet</Link>}
      />

      <SearchBar placeholder="Buscar por nome, raça ou tutor" value={busca} onChange={setBusca} />

      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <DataTable columns={columns} data={petsFiltrados} rowKey={(pet) => pet.id} emptyMessage="Nenhum pet encontrado." />
    </main>
  );
}

export default PetsPage;
