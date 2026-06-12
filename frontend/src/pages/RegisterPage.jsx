import { Link, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import useBodyClass from '../hooks/useBodyClass';
import { formatCrmv, formatTelefone, isCrmvValid, isEmailValid, isTelefoneValid } from '../utils/formatters';
import domainService from '../services/domainService';

function RegisterPage() {
  useBodyClass('auth-body');

  const navigate = useNavigate();
  const [form, setForm] = useState({
    nome: '',
    email: '',
    telefone: '',
    senha: '',
    role: 'TUTOR',
    crmv: '',
    especialidade: '',
  });
  const [error, setError] = useState('');

  const isVeterinario = useMemo(() => form.role === 'VETERINARIO', [form.role]);

  function onChange(event) {
    const { name, value } = event.target;
    let nextValue = value;

    if (name === 'telefone') nextValue = formatTelefone(value);
    if (name === 'crmv') nextValue = formatCrmv(value);

    setForm((prev) => ({ ...prev, [name]: nextValue }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    if (!form.nome.trim()) {
      setError('Informe o nome.');
      return;
    }

    if (!isEmailValid(form.email)) {
      setError('Informe um e-mail válido.');
      return;
    }

    if (form.senha.trim().length < 4) {
      setError('A senha precisa ter pelo menos 4 caracteres.');
      return;
    }

    if (!isVeterinario && !isTelefoneValid(form.telefone)) {
      setError('Telefone inválido. Use o padrão (99) 99999-9999.');
      return;
    }

    if (isVeterinario && !isCrmvValid(form.crmv)) {
      setError('CRMV inválido. Use o padrão CRMV-UF 12345.');
      return;
    }

    if (isVeterinario && !form.especialidade.trim()) {
      setError('Informe a especialidade.');
      return;
    }

    setError('');
    try {
      await domainService.registro({
        nome: form.nome,
        email: form.email,
        senha: form.senha,
        role: form.role,
        telefone: isVeterinario ? null : form.telefone,
        crmv: isVeterinario ? form.crmv : null,
        especialidade: isVeterinario ? form.especialidade : null,
      });
      navigate('/login');
    } catch (requestError) {
      setError(requestError.message || 'Não foi possível concluir o cadastro.');
    }
  }

  return (
    <div className="auth-shell auth-shell-compact">
      <section className="auth-card auth-card-compact" aria-label="Área de registro">
        <div className="auth-form-panel">
          <h2 className="auth-title">Criar conta</h2>
          <p className="auth-text">Preencha os dados para cadastrar seu usuário.</p>

          <form className="auth-form" onSubmit={onSubmit}>
            <div className="form-group">
              <label htmlFor="nome">Nome</label>
              <input id="nome" name="nome" value={form.nome} onChange={onChange} required />
            </div>

            <div className="form-group">
              <label htmlFor="email">E-mail</label>
              <input id="email" name="email" type="email" value={form.email} onChange={onChange} required />
            </div>

            {!isVeterinario ? (
              <div className="form-group">
                <label htmlFor="telefone">Telefone</label>
                <input
                  id="telefone"
                  name="telefone"
                  value={form.telefone}
                  onChange={onChange}
                  placeholder="(99) 99999-9999"
                  required
                />
              </div>
            ) : null}

            <div className="form-group">
              <label htmlFor="senha">Senha</label>
              <input id="senha" name="senha" type="password" value={form.senha} onChange={onChange} required />
            </div>

            <div className="form-group">
              <label>Perfil</label>
              <div className="toggle-group">
                <button
                  type="button"
                  className={`toggle-pill ${form.role === 'TUTOR' ? 'selected' : ''}`}
                  onClick={() => setForm((prev) => ({ ...prev, role: 'TUTOR', crmv: '' }))}
                >
                  Tutor
                </button>
                <button
                  type="button"
                  className={`toggle-pill ${form.role === 'VETERINARIO' ? 'selected' : ''}`}
                  onClick={() => setForm((prev) => ({ ...prev, role: 'VETERINARIO' }))}
                >
                  Veterinário
                </button>
              </div>
            </div>

            {isVeterinario ? (
              <>
                <div className="form-group">
                  <label htmlFor="crmv">CRMV</label>
                  <input
                    id="crmv"
                    name="crmv"
                    value={form.crmv}
                    onChange={onChange}
                    placeholder="CRMV-RJ 12345"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="especialidade">Especialidade</label>
                  <input
                    id="especialidade"
                    name="especialidade"
                    value={form.especialidade}
                    onChange={onChange}
                    placeholder="Ex.: Clínica geral"
                    required
                  />
                </div>
              </>
            ) : null}

            <button type="submit" className="btn btn-submit auth-submit">Cadastrar</button>
          </form>

          {error ? <p className="auth-message auth-error">{error}</p> : null}

          <p className="auth-switch">
            Já possui conta? <Link to="/login">Entrar</Link>
          </p>
        </div>
      </section>
    </div>
  );
}

export default RegisterPage;
