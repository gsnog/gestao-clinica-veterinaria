import { Link, useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';

function ConsultaFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [pets, setPets] = useState([]);
  const [veterinarios, setVeterinarios] = useState([]);
  const [form, setForm] = useState({ petId: '', veterinarioId: '', dataConsulta: '', motivo: '', diagnostico: '' });
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listPets().then((result) => setPets(result.items || result)).catch(() => {
      setPets([]);
      setErro('Não foi possível carregar pets e veterinários.');
    });
    domainService.listVeterinarios().then((result) => setVeterinarios(result.items || result)).catch(() => {
      setVeterinarios([]);
      setErro('Não foi possível carregar pets e veterinários.');
    });
  }, []);

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    try {
      await domainService.saveConsulta(form, id);
      navigate('/consultas');
    } catch (error) {
      setErro(error.message || 'Erro ao salvar consulta.');
    }
  }

  return (
    <main className="main">
      <Topbar title="Formulário de Consulta" subtitle="Registro de atendimento" />

      <section className="form-card">
        <aside className="form-sidebar">
          <div className="form-sidebar-icon">📋</div>
          <h2 className="form-sidebar-title">Consulta</h2>
          <p className="form-sidebar-text">Vincule pet, veterinário e os dados clínicos da consulta.</p>
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados da consulta</h2>
          <p className="form-subtitle">Preenchimento obrigatório para concluir o registro.</p>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="petId">Pet</label>
              <select id="petId" name="petId" value={form.petId} onChange={onChange} required>
                <option value="">Selecione</option>
                {pets.map((pet) => (
                  <option key={pet.id} value={pet.id}>{pet.nome}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="veterinarioId">Veterinário</label>
              <select id="veterinarioId" name="veterinarioId" value={form.veterinarioId} onChange={onChange} required>
                <option value="">Selecione</option>
                {veterinarios.map((veterinario) => (
                  <option key={veterinario.id} value={veterinario.id}>{veterinario.nome}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="dataConsulta">Data e hora</label>
              <input
                id="dataConsulta"
                name="dataConsulta"
                type="datetime-local"
                value={form.dataConsulta}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="motivo">Motivo</label>
              <input id="motivo" name="motivo" value={form.motivo} onChange={onChange} required />
            </div>
          </div>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="diagnostico">Diagnóstico</label>
              <textarea id="diagnostico" name="diagnostico" value={form.diagnostico} onChange={onChange} rows={4} required />
            </div>
          </div>

          {erro ? <p className="auth-message auth-error">{erro}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/consultas">Cancelar</Link>
            <button type="submit" className="btn btn-submit">Salvar</button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default ConsultaFormPage;
