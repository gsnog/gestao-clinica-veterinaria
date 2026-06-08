import { useEffect, useMemo, useState } from 'react';
import Topbar from '../components/Topbar';
import domainService from '../services/domainService';
import { formatTelefone, isTelefoneValid } from '../utils/formatters';

const roleBadgeClass = {
  ADMIN: 'badge-sand',
  TUTOR: 'badge-rose',
  VETERINARIO: 'badge-lav',
};

function PerfilPage() {
  const [perfil, setPerfil] = useState(null);
  const [telefone, setTelefone] = useState('');
  const [email, setEmail] = useState('');
  const [feedback, setFeedback] = useState({ type: '', message: '' });

  useEffect(() => {
    domainService.getPerfil().then((result) => {
      const usuario = result.usuario || result;
      setPerfil(usuario);
      setTelefone(result.telefone || usuario.telefone || '');
      setEmail(result.email || usuario.email || '');
      setFeedback({ type: '', message: '' });
    }).catch((error) => {
      setFeedback({ type: 'error', message: error.message || 'Erro ao carregar perfil.' });
    });
  }, []);

  const badgeClass = useMemo(() => roleBadgeClass[perfil?.role] || 'badge-mint', [perfil?.role]);

  function onSubmit(event) {
    event.preventDefault();

    if (!isTelefoneValid(telefone)) {
      setFeedback({ type: 'error', message: 'Telefone inválido. Use o padrão (99) 99999-9999.' });
      return;
    }

    domainService.updatePerfil({ email, telefone })
      .then(() => {
        setFeedback({ type: 'success', message: 'Perfil atualizado com sucesso.' });
      })
      .catch((error) => {
        setFeedback({ type: 'error', message: error.message || 'Não foi possível atualizar o perfil.' });
      });
  }

  return (
    <main className="main profile-layout">
      <Topbar title="Perfil" subtitle="Informações do usuário logado" />

      <section className="card mb-24">
        <div className="card-header">
          <h2 className="card-title">Dados da conta</h2>
        </div>

        {perfil ? (
          <div className="profile-grid">
            <div className="profile-row">
              <span className="profile-label">Nome</span>
              <span className="profile-value">{perfil.nome}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">E-mail</span>
              <span className="profile-value">{perfil.email}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Perfil</span>
              <span className={`badge profile-role-badge ${badgeClass}`}>{perfil.role}</span>
            </div>
          </div>
        ) : null}
      </section>

      <section className="card">
        <div className="card-header">
          <h2 className="profile-card-title">
            <span className="profile-card-icon">✎</span>
            Editar contato
          </h2>
        </div>

        <div className="profile-form-wrap">
          <form className="profile-form" onSubmit={onSubmit}>
            <div className="form-row single mb-0">
              <div className="form-group">
                <label htmlFor="email">E-mail</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  required
                />
              </div>
            </div>

            <div className="form-row single mb-0">
              <div className="form-group">
                <label htmlFor="telefone">Telefone</label>
                <input
                  id="telefone"
                  name="telefone"
                  value={telefone}
                  onChange={(event) => setTelefone(formatTelefone(event.target.value))}
                  placeholder="(99) 99999-9999"
                  required
                />
              </div>
            </div>

            {feedback.message ? (
              <p className={`profile-feedback ${feedback.type === 'success' ? 'auth-success' : 'auth-error'}`}>
                {feedback.message}
              </p>
            ) : null}

            <button type="submit" className="btn btn-submit profile-submit">Salvar alterações</button>
          </form>
        </div>
      </section>
    </main>
  );
}

export default PerfilPage;
