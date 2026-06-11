import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import useBodyClass from '../hooks/useBodyClass';
import { isEmailValid } from '../utils/formatters';
import { useAuth } from '../contexts/authContext';

function LoginPage() {
  useBodyClass('auth-body');

  const navigate = useNavigate();
  const { defaultRoute } = useAuth();
  const [form, setForm] = useState({ email: '', senha: '', manterConectado: false });
  const [error, setError] = useState('');

  function onChange(event) {
    const { name, value, type, checked } = event.target;
    setForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  }

  function onSubmit(event) {
    event.preventDefault();

    if (!isEmailValid(form.email) || form.senha.trim().length < 4) {
      setError('Informe e-mail e senha válidos.');
      return;
    }

    setError('');
    navigate(defaultRoute || '/login');
  }

  return (
    <div className="auth-shell">
      <section className="auth-card" aria-label="Área de login">
        <aside className="auth-brand-panel">
          <h1 className="auth-brand-title">VetCare</h1>
          <p className="auth-brand-subtitle">
            Sistema de Gestão Veterinária.
          </p>
        </aside>

        <div className="auth-form-panel">
          <h2 className="auth-title">Entrar</h2>
          <p className="auth-text">Acesse sua conta para continuar.</p>

          <form className="auth-form" onSubmit={onSubmit}>
            <div className="form-group">
              <label htmlFor="email">E-mail</label>
              <input
                id="email"
                name="email"
                type="email"
                placeholder="seu@email.com"
                value={form.email}
                onChange={onChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="senha">Senha</label>
              <input
                id="senha"
                name="senha"
                type="password"
                placeholder="••••••••"
                value={form.senha}
                onChange={onChange}
                required
              />
            </div>

            <div className="form-group auth-inline-check">
              <input
                id="manterConectado"
                name="manterConectado"
                type="checkbox"
                checked={form.manterConectado}
                onChange={onChange}
              />
              <label htmlFor="manterConectado">Manter conectado</label>
            </div>

            <button type="submit" className="btn btn-submit auth-submit">Entrar</button>
          </form>

          {error ? <p className="auth-message auth-error">{error}</p> : null}

          <p className="auth-switch">
            Não possui conta? <Link to="/registro">Criar cadastro</Link>
          </p>
        </div>
      </section>
    </div>
  );
}

export default LoginPage;
