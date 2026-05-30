import { Link, useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';

function PetFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [tutores, setTutores] = useState([]);
  const [form, setForm] = useState({ nome: '', raca: '', dataNascimento: '', tutorId: '' });
  const [erro, setErro] = useState('');

  useEffect(() => {
    domainService.listTutores().then((result) => setTutores(result.items || result)).catch(() => {
      setTutores([]);
      setErro('Não foi possível carregar os tutores.');
    });
  }, []);

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    try {
      await domainService.savePet(form, id);
      navigate('/pets');
    } catch (error) {
      setErro(error.message || 'Erro ao salvar pet.');
    }
  }

  return (
    <main className="main">
      <Topbar title="Formulário de Pet" subtitle="Cadastro e edição de pets" />

      <section className="form-card">
        <aside className="form-sidebar">
          <div className="form-sidebar-icon">🐶</div>
          <h2 className="form-sidebar-title">Pet</h2>
          <p className="form-sidebar-text">Preencha os dados para manter o cadastro atualizado.</p>
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados do pet</h2>
          <p className="form-subtitle">Todos os campos são obrigatórios.</p>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="nome">Nome</label>
              <input id="nome" name="nome" value={form.nome} onChange={onChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="raca">Raça</label>
              <input id="raca" name="raca" value={form.raca} onChange={onChange} required />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="dataNascimento">Data de nascimento</label>
              <input
                id="dataNascimento"
                name="dataNascimento"
                type="date"
                value={form.dataNascimento}
                onChange={onChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="tutorId">Tutor</label>
              <select id="tutorId" name="tutorId" value={form.tutorId} onChange={onChange} required>
                <option value="">Selecione</option>
                {tutores.map((tutor) => (
                  <option key={tutor.id} value={tutor.id}>{tutor.nome}</option>
                ))}
              </select>
            </div>
          </div>

          {erro ? <p className="auth-message auth-error">{erro}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/pets">Cancelar</Link>
            <button type="submit" className="btn btn-submit">Salvar</button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default PetFormPage;
