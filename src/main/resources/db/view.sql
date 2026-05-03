-- View: Detalhes completos da consulta (Mapeia exatamente o que o DAO espera)
CREATE OR REPLACE VIEW v_detalhes_consulta AS
SELECT 
    c.id AS consulta_id, 
    c.data_consulta, 
    c.motivo,
    p.id AS pet_id, 
    p.nome AS pet_nome, 
    p.raca AS pet_raca,
    v.usuario_id AS vet_id,     -- PK agora é usuario_id
    u.nome AS vet_nome,          -- Nome vem da tabela usuario
    v.especialidade AS vet_especialidade
FROM consulta c
INNER JOIN pet p ON c.pet_id = p.id
INNER JOIN veterinario v ON c.veterinario_id = v.usuario_id
INNER JOIN usuario u ON v.usuario_id = u.id;

--View: Retorna a agenda de um veterinário específico em um dia
CREATE OR REPLACE VIEW v_agenda_veterinario AS
SELECT 
    v.crmv AS crmv_veterinario,
    u.nome AS nome_veterinario,  -- Nome vem da tabela usuario
    DATE(c.data_consulta) AS dia_consulta,
    c.data_consulta AS hora_consulta,
    p.nome AS nome_pet,
    c.motivo AS motivo_consulta
FROM consulta c
INNER JOIN veterinario v ON c.veterinario_id = v.usuario_id
INNER JOIN usuario u ON v.usuario_id = u.id
INNER JOIN pet p ON c.pet_id = p.id;

-- View: Estatísticas do dashboard
CREATE OR REPLACE VIEW v_estatisticas_dashboard AS
SELECT
    (SELECT COUNT(*) FROM pet) AS total_pets,
    (SELECT COUNT(*) FROM tutor) AS total_tutores,
    (SELECT COUNT(*) FROM veterinario) AS total_veterinarios,
    (SELECT COUNT(*) FROM consulta) AS total_consultas;
