-- Criação das Tabelas
CREATE TABLE tutor (
    id BIGSERIAL NOT NULL, -- PK, ID Auto-incremental
    nome VARCHAR(120) NOT NULL, -- Nome completo do tutor
    telefone VARCHAR(30) NOT NULL, -- Telefone de contato
    PRIMARY KEY (id),
    UNIQUE (nome, telefone) -- Garante que não haja duplicidade exata no seed
);

CREATE TABLE veterinario (
    id BIGSERIAL NOT NULL, -- PK, ID Auto-incremental
    nome VARCHAR(120) NOT NULL, -- Nome do profissional
    crmv VARCHAR(30) NOT NULL, -- Registro profissional
    especialidade VARCHAR(120) NOT NULL, -- Área de atuação
    PRIMARY KEY (id),
    UNIQUE (crmv) -- Restrição de unicidade para a chave natural
);

CREATE TABLE pet (
    id BIGSERIAL NOT NULL, -- PK, ID Auto-incremental
    nome VARCHAR(120) NOT NULL,
    raca VARCHAR(120) NOT NULL,
    data_nascimento DATE,
    tutor_id BIGINT NOT NULL, -- FK para Tutor(id)
    PRIMARY KEY (id),
    FOREIGN KEY (tutor_id) REFERENCES tutor(id)
        ON DELETE CASCADE -- Exclusão em cascata conforme exigido pelo DAO atual
        ON UPDATE CASCADE
);

CREATE TABLE consulta (
    id BIGSERIAL NOT NULL, -- PK, ID Auto-incremental
    data_consulta TIMESTAMP NOT NULL, -- Data e hora do atendimento
    motivo VARCHAR(255) NOT NULL,
    pet_id BIGINT NOT NULL, -- FK para Pet(id)
    veterinario_id BIGINT NOT NULL, -- FK para Veterinario(id)
    PRIMARY KEY (id),
    FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    FOREIGN KEY (veterinario_id) REFERENCES veterinario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TUTOR', 'VETERINARIO'))
);
