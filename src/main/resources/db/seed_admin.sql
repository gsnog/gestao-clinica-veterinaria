-- Seed do primeiro administrador da plataforma.
-- O ADMIN nunca é criado pelo cadastro público (/registro); ele entra por este seed
-- e, a partir daí, novos admins são criados pela tela protegida /usuarios.
--
-- Credenciais iniciais (TROCAR a senha após o primeiro login, em /usuarios):
--   E-mail : admin@vetcare.com
--   Senha  : Admin@123
--
-- O hash segue o esquema do PasswordUtil: Base64(SHA-256(senha + salt)).

-- Para banco já existente, garantir que o CHECK aceita ADMIN antes de inserir:
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_role_check;
ALTER TABLE usuario ADD CONSTRAINT usuario_role_check
    CHECK (role IN ('TUTOR', 'VETERINARIO', 'ADMIN'));

INSERT INTO usuario (nome, email, senha_hash, salt, role) VALUES
('Administrador VetCare', 'admin@vetcare.com',
 '2n+PBIZynVTx4ofWZkFbF/VdJNdvv3GiWzDOpaCjMwo=',
 'c2VlZEFkbWluU2FsdDIwMjY=', 'ADMIN')
ON CONFLICT (email) DO NOTHING;
