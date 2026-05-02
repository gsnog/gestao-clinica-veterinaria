-- Criação das Tabelas
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY, -- PK, ID Auto-incremental
    nome VARCHAR(120) NOT NULL, -- Nome completo do tutor/ profissional
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TUTOR', 'VETERINARIO'))
);

CREATE TABLE tutor (
    usuario_id BIGINT PRIMARY KEY, -- Relacionamento 1:1 com usuario
    telefone VARCHAR(30) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE veterinario (
    usuario_id BIGINT PRIMARY KEY, -- Relacionamento 1:1 com usuario
    crmv VARCHAR(30) NOT NULL UNIQUE,
    especialidade VARCHAR(120) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE pet (
    id BIGSERIAL PRIMARY KEY, -- PK, ID Auto-incremental
    nome VARCHAR(120) NOT NULL,
    raca VARCHAR(120) NOT NULL,
    data_nascimento DATE,
    tutor_id BIGINT NOT NULL, -- FK para Tutor(id)
    FOREIGN KEY (tutor_id) REFERENCES tutor(usuario_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE -- Exclusão em cascata conforme exigido pelo DAO
);

CREATE TABLE consulta (
    id BIGSERIAL PRIMARY KEY, -- PK, ID Auto-incremental
    data_consulta TIMESTAMP NOT NULL, -- Data e hora do atendimento
    motivo VARCHAR(255) NOT NULL,
    pet_id BIGINT NOT NULL, -- FK para Pet(id)
    veterinario_id BIGINT NOT NULL, -- FK para Veterinario(id)
    FOREIGN KEY (pet_id) REFERENCES pet(id) 
        ON DELETE CASCADE,
    FOREIGN KEY (veterinario_id) REFERENCES veterinario(usuario_id) 
        ON DELETE CASCADE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TUTOR', 'VETERINARIO'))
);
