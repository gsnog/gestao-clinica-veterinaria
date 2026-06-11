import { Link, useNavigate, useParams } from 'react-router-dom';
import { useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { formatCrmv, formatTelefone, isCrmvValid, isEmailValid, isTelefoneValid } from '../utils/formatters';

function VeterinarioFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nome: '',
    email: '',
    telefone: '',
    crmv: '',
    especialidade: '',
  });
  const [error, setError] = useState('');

  function onChange(event) {
    const { name, value } = event.target;

    let nextValue = value;
    if (name === 'telefone') nextValue = formatTelefone(value);
    if (name === 'crmv') nextValue = formatCrmv(value);

    setForm((prev) => ({ ...prev, [name]: nextValue }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    if (!isEmailValid(form.email)) {
      setError('Informe um e-mail válido.');
      return;
    }

    if (!isTelefoneValid(form.telefone)) {
      setError('Telefone inválido.');
      return;
    }

    if (!isCrmvValid(form.crmv)) {
      setError('CRMV inválido. Use CRMV-UF 12345.');
      return;
    }

    try {
      await domainService.saveVeterinario(form, id);
      setError('');
      navigate('/veterinarios');
    } catch (requestError) {
      setError(requestError.message || 'Não foi possível salvar o veterinário.');
    }
  }

  return (
    <main className="main">
      <Topbar title="Formulário de Veterinário" subtitle="Cadastro e edição de veterinário" />

      <section className="form-card">
        <aside className="form-sidebar">
          <div className="form-sidebar-icon">🩺</div>
          <h2 className="form-sidebar-title">Veterinário</h2>
          <p className="form-sidebar-text">Mantenha os dados profissionais sempre atualizados.</p>
        </aside>

        <form className="form-body" onSubmit={onSubmit}>
          <h2 className="form-title">Dados profissionais</h2>
          <p className="form-subtitle">Os campos são validados conforme o padrão da aplicação.</p>

          <div className="form-row single">
            <div className="form-group">
              <label htmlFor="nome">Nome</label>
              <input id="nome" name="nome" value={form.nome} onChange={onChange} required />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email">E-mail</label>
              <input id="email" name="email" type="email" value={form.email} onChange={onChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="telefone">Telefone</label>
              <input id="telefone" name="telefone" value={form.telefone} onChange={onChange} placeholder="(99) 99999-9999" required />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="crmv">CRMV</label>
              <input id="crmv" name="crmv" value={form.crmv} onChange={onChange} placeholder="CRMV-RJ 12345" required />
            </div>
            <div className="form-group">
              <label htmlFor="especialidade">Especialidade</label>
              <input id="especialidade" name="especialidade" value={form.especialidade} onChange={onChange} required />
            </div>
          </div>

          {error ? <p className="auth-message auth-error">{error}</p> : null}

          <div className="form-actions">
            <Link className="btn btn-outline" to="/veterinarios">Cancelar</Link>
            <button type="submit" className="btn btn-submit">Salvar</button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default VeterinarioFormPage;
