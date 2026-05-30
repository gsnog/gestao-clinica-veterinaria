import { Link } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { useAuth } from '../contexts/authContext';
import { normalizeText } from '../utils/formatters';

function ConsultasPage() {
  const { role } = useAuth();
  const canManageConsultas = role === 'VETERINARIO';
  const [consultas, setConsultas] = useState([]);
  const [busca, setBusca] = useState('');
  const [dataInicio, setDataInicio] = useState('');
  const [dataFim, setDataFim] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listConsultas()
      .then((result) => {
        setConsultas(result.items || result);
        setErro('');
      })
      .catch((error) => setErro(error.message || 'Erro ao carregar consultas.'));
  }, []);

  const periodoInvalido = dataInicio && dataFim && dataInicio > dataFim;

  const consultasFiltradas = useMemo(() => {
    const query = normalizeText(busca);

    return consultas.filter((consulta) => {
      const pet = normalizeText(consulta.pet?.nome);
      const vet = normalizeText(consulta.veterinario?.nome);
      const motivo = normalizeText(consulta.motivo);
      const data = consulta.dataConsulta?.slice(0, 10);

      const matchBusca = !query || pet.includes(query) || vet.includes(query) || motivo.includes(query);
      const matchInicio = !dataInicio || data >= dataInicio;
      const matchFim = !dataFim || data <= dataFim;

      return !periodoInvalido && matchBusca && matchInicio && matchFim;
    });
  }, [busca, consultas, dataFim, dataInicio, periodoInvalido]);

  async function remover(id) {
    if (!window.confirm('Deseja excluir esta consulta?')) return;

    try {
      await domainService.deleteConsulta(id);
      setConsultas((prev) => prev.filter((consulta) => consulta.id !== id));
      setErro('');
    } catch (error) {
      setErro(error.message || 'Não foi possível excluir a consulta.');
    }
  }

  return (
    <main className="main">
      <Topbar
        title="Consultas"
        subtitle="Agenda e histórico de consultas"
        action={canManageConsultas ? <Link className="btn btn-primary" to="/consultas/nova">+ Nova consulta</Link> : null}
      />

      <div className="filter-bar">
        <div className="filter-group">
          <label htmlFor="busca-consulta">Buscar:</label>
          <input
            id="busca-consulta"
            type="text"
            placeholder="Pet, veterinário ou motivo"
            value={busca}
            onChange={(event) => setBusca(event.target.value)}
          />
          <label htmlFor="inicio-consulta">De:</label>
          <input id="inicio-consulta" type="date" value={dataInicio} onChange={(event) => setDataInicio(event.target.value)} />
          <label htmlFor="fim-consulta">Até:</label>
          <input id="fim-consulta" type="date" value={dataFim} onChange={(event) => setDataFim(event.target.value)} />
        </div>
      </div>

      {periodoInvalido ? <p className="filter-feedback mb-16">A data inicial não pode ser maior que a data final.</p> : null}
      {erro ? <p className="filter-feedback mb-16">{erro}</p> : null}

      <section className="card">
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Pet</th>
              <th>Veterinário</th>
              <th>Motivo</th>
              {canManageConsultas ? <th>Ações</th> : null}
            </tr>
          </thead>
          <tbody>
            {consultasFiltradas.map((consulta) => (
              <tr key={consulta.id}>
                <td>{new Date(consulta.dataConsulta).toLocaleString('pt-BR')}</td>
                <td>{consulta.pet?.nome}</td>
                <td>{consulta.veterinario?.nome}</td>
                <td>{consulta.motivo}</td>
                {canManageConsultas ? (
                  <td>
                    <div className="actions">
                      <Link className="btn btn-edit" to={`/consultas/${consulta.id}/editar`}>Editar</Link>
                      <button type="button" className="btn btn-danger" onClick={() => remover(consulta.id)}>Excluir</button>
                    </div>
                  </td>
                ) : null}
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </main>
  );
}

export default ConsultasPage;
