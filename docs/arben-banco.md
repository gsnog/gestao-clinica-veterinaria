# Guia completo para o Arben (Banco de Dados)

**Status:** Pronto para entrega para implementacao do banco PostgreSQL.

Este documento consolida o que o back-end atual exige do banco PostgreSQL: tabelas, colunas, tipos, relacionamentos, constraints, indices e SQL inicial.

## 1) Ambiente esperado pelo codigo Java

Classe de conexao: `ConnectionFactory`

- URL: `jdbc:postgresql://localhost:5432/clinica`
- Usuario: `postgres`
- Senha: `sua_senha_aqui` (placeholder no codigo)
- Driver: `org.postgresql.Driver`

Dependencia no `pom.xml`:
- `org.postgresql:postgresql:42.7.3`

## 2) Modelo de dados inferido dos DAOs

### Entidades e cardinalidade

- `tutor` 1:N `pet`
  - `pet.tutor_id` referencia `tutor.id`
- `pet` 1:N `consulta`
  - `consulta.pet_id` referencia `pet.id`
- `veterinario` 1:N `consulta`
  - `consulta.veterinario_id` referencia `veterinario.id`

## 3) Dicionario de dados (colunas que o codigo usa)

### Tabela `tutor`

- `id` (PK)
- `nome` (String)
- `telefone` (String)

Operacoes SQL no codigo:
- `INSERT INTO tutor (nome, telefone) VALUES (?, ?)`
- `SELECT * FROM tutor`
- `SELECT * FROM tutor WHERE id = ?`
- `UPDATE tutor SET nome = ?, telefone = ? WHERE id = ?`
- `DELETE FROM tutor WHERE id = ?`

### Tabela `pet`

- `id` (PK)
- `nome` (String)
- `raca` (String)
- `data_nascimento` (Date)
- `tutor_id` (FK -> `tutor.id`)

Operacoes SQL no codigo:
- `INSERT INTO pet (nome, raca, data_nascimento, tutor_id) VALUES (?, ?, ?, ?)`
- `SELECT * FROM pet`
- `SELECT * FROM pet WHERE id = ?`
- `UPDATE pet SET nome = ?, raca = ?, data_nascimento = ?, tutor_id = ? WHERE id = ?`
- `DELETE FROM pet WHERE id = ?`

### Tabela `veterinario`

- `id` (PK)
- `nome` (String)
- `crmv` (String)
- `especialidade` (String)

Operacoes SQL no codigo:
- `INSERT INTO veterinario (nome, crmv, especialidade) VALUES (?, ?, ?)`
- `SELECT * FROM veterinario`
- `SELECT * FROM veterinario WHERE id = ?`
- `UPDATE veterinario SET nome = ?, crmv = ?, especialidade = ? WHERE id = ?`
- `DELETE FROM veterinario WHERE id = ?`

### Tabela `consulta`

- `id` (PK)
- `data_consulta` (Timestamp)
- `motivo` (String)
- `pet_id` (FK -> `pet.id`)
- `veterinario_id` (FK -> `veterinario.id`)

Operacoes SQL no codigo:
- `INSERT INTO consulta (data_consulta, motivo, pet_id, veterinario_id) VALUES (?, ?, ?, ?)`
- `UPDATE consulta SET data_consulta = ?, motivo = ?, pet_id = ?, veterinario_id = ? WHERE id = ?`
- `DELETE FROM consulta WHERE id = ?`
- `SELECT ... FROM consulta c INNER JOIN pet p ... INNER JOIN veterinario v ...`
- filtro por pet: `WHERE c.pet_id = ?`
- filtro por vet e data: `WHERE c.veterinario_id = ? AND DATE(c.data_consulta) = ?`

## 4) Tipos SQL recomendados (PostgreSQL)

Mapeamento Java -> SQL:

- `Long` -> `BIGINT`
- `String` -> `VARCHAR(...)` ou `TEXT`
- `LocalDate` -> `DATE`
- `LocalDateTime` -> `TIMESTAMP`

Recomendacao pragmatica para este projeto:

- IDs como `BIGSERIAL` (auto incremento)
- `nome`, `raca`, `especialidade`, `motivo` como `VARCHAR` com limites razoaveis
- `crmv` com `UNIQUE`

## 5) DDL sugerida (compatibilidade com o codigo atual)

```sql
CREATE TABLE IF NOT EXISTS tutor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS veterinario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    crmv VARCHAR(30) NOT NULL UNIQUE,
    especialidade VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS pet (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    raca VARCHAR(120) NOT NULL,
    data_nascimento DATE,
    tutor_id BIGINT NOT NULL,
    CONSTRAINT fk_pet_tutor
        FOREIGN KEY (tutor_id)
        REFERENCES tutor (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS consulta (
    id BIGSERIAL PRIMARY KEY,
    data_consulta TIMESTAMP NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    pet_id BIGINT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    CONSTRAINT fk_consulta_pet
        FOREIGN KEY (pet_id)
        REFERENCES pet (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_consulta_veterinario
        FOREIGN KEY (veterinario_id)
        REFERENCES veterinario (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
```

## 6) Indices recomendados para performance

Consultas mais usadas no DAO:

- por pet: `WHERE c.pet_id = ?`
- por veterinario + data: `WHERE c.veterinario_id = ? AND DATE(c.data_consulta) = ?`

Indices:

```sql
CREATE INDEX IF NOT EXISTS idx_pet_tutor_id
    ON pet (tutor_id);

CREATE INDEX IF NOT EXISTS idx_consulta_pet_id
    ON consulta (pet_id);

CREATE INDEX IF NOT EXISTS idx_consulta_veterinario_id
    ON consulta (veterinario_id);

-- Opcao 1: sem alterar codigo, index funcional para DATE(data_consulta)
CREATE INDEX IF NOT EXISTS idx_consulta_vet_data_func
    ON consulta (veterinario_id, (DATE(data_consulta)));

-- Opcao 2 (mais performatica no geral):
-- ajustar query para range de timestamp e usar index simples em (veterinario_id, data_consulta)
```

## 7) Seeds minimos para subir ambiente local

```sql
INSERT INTO tutor (nome, telefone) VALUES
('Ana Souza', '21999990001'),
('Bruno Lima', '21999990002')
ON CONFLICT DO NOTHING;

INSERT INTO veterinario (nome, crmv, especialidade) VALUES
('Dra. Carla', 'CRMV-RJ-1001', 'Clinica geral'),
('Dr. Diego', 'CRMV-RJ-1002', 'Dermatologia')
ON CONFLICT (crmv) DO NOTHING;

INSERT INTO pet (nome, raca, data_nascimento, tutor_id) VALUES
('Rex', 'Labrador', '2021-05-20', 1),
('Mia', 'Siames', '2022-08-11', 2)
ON CONFLICT DO NOTHING;

INSERT INTO consulta (data_consulta, motivo, pet_id, veterinario_id) VALUES
('2026-04-11 14:30:00', 'Vacina', 1, 1),
('2026-04-11 16:00:00', 'Retorno', 2, 2)
ON CONFLICT DO NOTHING;
```

## 8) Pontos de atencao de integridade

- Os DAOs fazem delete direto (`DELETE FROM tutor`, `pet`, `veterinario`).
  - Com `ON DELETE RESTRICT`, essas operacoes podem falhar se houver dependencia.
  - Com `ON DELETE CASCADE`, a exclusao encadeia e evita erro no app atual.
- Se a regra de negocio nao permitir cascata, o back-end precisa tratar exclusao em ordem e mensagens de erro.
- Nao ha transacoes explicitas de negocio (cada operacao e isolada por statement).

## 9) Compatibilidade com objetos Java (campos)

### `Tutor`
- `id`, `nome`, `telefone`

### `Pet`
- `id`, `nome`, `raca`, `dataNascimento`, `tutor.id`

### `Veterinario`
- `id`, `nome`, `crmv`, `especialidade`

### `Consulta`
- `id`, `dataConsulta`, `motivo`, `pet.id`, `veterinario.id`

## 10) Queries de join que precisam continuar funcionando

O `ConsultaDAOImpl` depende de aliases especificos no `ResultSet`:

- `consulta_id`
- `data_consulta`
- `motivo`
- `pet_id`, `pet_nome`, `pet_raca`
- `vet_id`, `vet_nome`, `vet_especialidade`

Isso vem da query base:

```sql
SELECT c.id AS consulta_id, c.data_consulta, c.motivo,
       p.id AS pet_id, p.nome AS pet_nome, p.raca AS pet_raca,
       v.id AS vet_id, v.nome AS vet_nome, v.especialidade AS vet_especialidade
FROM consulta c
INNER JOIN pet p ON c.pet_id = p.id
INNER JOIN veterinario v ON c.veterinario_id = v.id
```

## 11) Checklist rapido de entrega do banco

- Criar banco `clinica` no PostgreSQL local.
- Executar DDL das 4 tabelas na ordem: `tutor`, `veterinario`, `pet`, `consulta`.
- Criar indices recomendados.
- Inserir seeds minimos.
- Validar selects dos DAOs (principalmente filtros de `consulta`).
- Alinhar senha real no `ConnectionFactory` para ambiente local/equipe.

## 12) Referencias de codigo usadas

- `src/main/java/com/uff/gestaoclinicaveterinaria/util/ConnectionFactory.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/TutorDAOImpl.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/PetDAOImpl.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/VeterinarioDAOImpl.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/ConsultaDAOImpl.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Tutor.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Pet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Veterinario.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Consulta.java`

## 13) Checklist pratico - Arben

- Criar o banco `clinica` no PostgreSQL e garantir acesso local na porta `5432`.
- Executar a DDL das 4 tabelas (`tutor`, `veterinario`, `pet`, `consulta`) com PK/FK.
- Aplicar constraints importantes: `NOT NULL`, `UNIQUE` em `veterinario.crmv` e FKs consistentes.
- Criar os indices recomendados, principalmente os de `consulta` para filtros por pet e por data.
- Inserir dados de seed minimos para viabilizar os fluxos de front e backend.
- Validar as queries dos DAOs, incluindo os joins e aliases usados em `ConsultaDAOImpl`.
- Alinhar credenciais reais no `ConnectionFactory` para o ambiente compartilhado da equipe.

## 14) Criterios objetivos de aceite - Arben

- O backend conecta no banco sem erro usando a URL `jdbc:postgresql://localhost:5432/clinica`.
- CRUD de `tutor`, `pet`, `veterinario` e `consulta` executa sem erro de schema.
- Filtros de consulta por pet e por data/veterinario retornam resultados corretos.
- Nao ha erro de integridade referencial nas operacoes normais de cadastro/edicao.
- Estrutura final no banco contem 4 tabelas, FKs ativas e indices criados.
- Time consegue subir ambiente local com DDL + seeds sem ajustes estruturais adicionais.

