-- Usuários de teste para login no front (senha em texto plano: senha123)
-- Hash gerado conforme PasswordUtil.gerarHash: Base64(SHA-256(senha + salt))
INSERT INTO usuario (nome, email, senha_hash, salt, role) VALUES
    ('Tutor Exemplo', 'tutor@exemplo.com', 'Yvxzs0MKbhNalMt7rqMBK4fq1cSaWNS7iDtcY2cNGLw=', 'c2VlZC1zYWx0LXR1dG9yMQ==', 'TUTOR'),
    ('Vet Exemplo', 'vet@exemplo.com', '5teiiKVpYEJJ45unWvZ0bue2pPh+ZtuInGQSIi3ZH2s=', 'c2VlZC1zYWx0LXZldDAwMQ==', 'VETERINARIO');

INSERT INTO tutor (usuario_id, telefone)
    SELECT id, '(21) 99999-9999' FROM usuario WHERE email = 'tutor@exemplo.com';

INSERT INTO veterinario (usuario_id, crmv, especialidade)
    SELECT id, 'CRMV-SP 12345', 'Clinico Geral' FROM usuario WHERE email = 'vet@exemplo.com';
