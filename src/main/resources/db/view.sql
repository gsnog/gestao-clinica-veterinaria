-- View: Detalhes completos da consulta (Mapeia exatamente o que o DAO espera)
CREATE OR REPLACE VIEW v_detalhes_consulta AS
SELECT 
    c.id AS consulta_id, 
    c.data_consulta, 
    c.motivo,
    p.id AS pet_id, 
    p.nome AS pet_nome,
    v_u.nome AS vet_nome, -- Vem de usuario
    v.especialidade AS vet_especialidade
FROM consulta c
INNER JOIN pet p ON c.pet_id = p.id
INNER JOIN veterinario v ON c.veterinario_id = v.usuario_id
INNER JOIN usuario v_u ON v.usuario_id = v_u.id;

--View: Retorna a agenda de um veterinário específico em um dia
CREATE OR REPLACE VIEW v_agenda_veterinario AS
SELECT 
    v.crmv AS crmv_veterinario,
    v.nome AS nome_veterinario,
    DATE(c.data_consulta) AS dia_consulta,
    c.data_consulta AS hora_consulta,
    p.nome AS nome_pet,
    c.motivo AS motivo_consulta
FROM consulta c
JOIN veterinario v ON c.veterinario_id = v.id
JOIN pet p ON c.pet_id = p.id;
