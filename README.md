# VetCare - Gestão de Clínica Veterinária

Aplicação web completa para gestão de consultórios veterinários, desenvolvida em Java com Servlets/JSP e PostgreSQL.

## 📋 Visão Geral

VetCare é um sistema de gestão que permite:
- **Tutores**: Cadastrar e gerenciar seus pets, visualizar histórico de consultas
- **Veterinários**: Gerenciar consultórios, atender tutores e pets, controlar equipe

## 🏗️ Arquitetura

```
src/main/
├── java/com/uff/gestaoclinicaveterinaria/
│   ├── controller/      # Servlets (handlers de requisição)
│   ├── dao/             # Data Access Objects (persistência)
│   ├── model/           # Entidades de negócio
│   ├── dto/             # Data Transfer Objects
│   ├── filter/          # Filtros (autenticação, CSRF, cache)
│   └── util/            # Utilitários (segurança, validação, preferências)
├── webapp/
│   ├── WEB-INF/
│   │   ├── views/       # JSPs públicas (login, registro)
│   │   └── web.xml      # Configuração da aplicação
│   ├── css/             # Design system
│   ├── scripts/         # JavaScript de cliente
│   ├── images/          # Recursos visuais
│   ├── components/      # JSPs reutilizáveis (sidebar, header, CSRF)
│   └── *.jsp            # JSPs protegidas (dashboards, formulários, listas)
└── resources/db/        # Scripts SQL (schema, dados de teste)
```

### Stack Tecnológico
- **Backend**: Java 21, Jakarta Servlet 6.1, PostgreSQL 42.7
- **Frontend**: HTML5, CSS3, JavaScript
- **Build**: Maven 3.x
- **Padrões**: MVC, DAO, CSRF, SessionCookie

## 🚀 Como Rodar

### Opção A — Docker (recomendado)

Sobe o backend (Tomcat) + banco de dados (PostgreSQL) com um único comando, sem precisar instalar Java, Maven, Tomcat ou Postgres na máquina.

#### Pré-requisitos
- Docker e Docker Compose (Docker Desktop já inclui os dois)

#### 1. Build e start

Na raiz do projeto:

```bash
docker compose up -d --build
```

Isso cria dois containers:
- **db**: PostgreSQL 16, banco `clinica`, usuário `vet_admin` / senha `vet_admin`, exposto em `localhost:5433` (as tabelas/views/índices de `src/main/resources/db` são criados automaticamente na primeira vez)
- **backend**: build do projeto com Maven + deploy no Tomcat 11, exposto em `localhost:8080`

#### 2. Acessar a aplicação

```
http://localhost:8080/clinica
```

Será redirecionado para login automaticamente.

#### 3. Comandos úteis

```bash
# ver logs do backend em tempo real
docker compose logs -f backend

# parar os containers (mantém os dados do banco)
docker compose down

# parar e apagar também os dados do banco
docker compose down -v

# rebuildar depois de alterar o código Java
docker compose up -d --build
```

> Nota: o Postgres do Docker usa a porta **5433** (e não 5432) para não conflitar com um Postgres que já esteja instalado/rodando na sua máquina.

### Opção B — Ambiente local (sem Docker)

#### Pré-requisitos
- JDK 21+
- Maven 3.9+
- PostgreSQL 12+
- Tomcat 10/11 (ou outro servlet container compatível com Jakarta Servlet 6.1)

#### 1. Configurar Banco de Dados

```bash
# Criar banco e usuário
psql -U postgres -c "CREATE DATABASE clinica;"
psql -U postgres -c "CREATE USER vet_admin WITH PASSWORD 'sua_senha';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE clinica TO vet_admin;"

# Executar scripts SQL
psql -U vet_admin -d clinica -f src/main/resources/db/tables.sql
psql -U vet_admin -d clinica -f src/main/resources/db/view.sql
psql -U vet_admin -d clinica -f src/main/resources/db/index.sql
```

#### 2. Configurar Variáveis de Ambiente

```bash
export DB_URL="jdbc:postgresql://localhost:5432/clinica"
export DB_USER="vet_admin"
export DB_PASSWORD="sua_senha"
```

Importante: as variáveis devem estar no mesmo terminal que inicia o servidor (Tomcat/Jetty).

Exemplo (macOS/Linux, Tomcat):

```bash
export DB_URL="jdbc:postgresql://localhost:5432/clinica"
export DB_USER="vet_admin"
export DB_PASSWORD="sua_senha"
$CATALINA_HOME/bin/catalina.sh run
```

#### 3. Build e Deploy

```bash
# Compilar e empacotar
mvn clean package

# Deploy em servidor (ex: Tomcat), com context path /clinica
cp target/gestao-clinica-veterinaria-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/clinica.war
```

#### 4. Acessar a Aplicação

```
http://localhost:8080/clinica
```

Será redirecionado para login automaticamente.

## 🔐 Fluxo de Autenticação

### Cadastro
1. Usuário acessa `/registro`
2. Escolhe papel: **TUTOR** ou **VETERINARIO**
3. Se **TUTOR**: informar telefone
4. Se **VETERINARIO**: informar CRMV e especialidade
5. Sistema cria registro em `usuario` + registro específico (`tutor` ou `veterinario`)
6. Hash de senha com salt aleatório (bcrypt-like)

### Login
1. Email + Senha
2. Valida credenciais contra `usuario.senha_hash + salt`
3. Cria sessão HTTP com:
   - `usuarioId` (PK)
   - `usuarioNome`
   - `usuarioRole` (TUTOR | VETERINARIO)
4. **TUTOR** → redireciona para `/pets`
5. **VETERINARIO** → redireciona para `/dashboard`

### Autorização
- **AuthFilter**: bloqueia rotas protegidas sem sessão
  - Vet-only: `/dashboard`, `/veterinarios`, `/tutores`
  - Tutor|Vet: `/consultas`, `/pets`, `/perfil`
- **ApiAuthFilter**: bloqueia `/api/*` sem sessão, exceto `/api/login`, `/api/registro` e `/api/csrf`, sempre com resposta JSON
- **CSRF Filter**: valida tokens em formulários JSP via `_csrf` e em mutações da API via header `X-CSRF-Token`

## 👤 Fluxo de Papéis

### Tutor
- ✅ Ver seus próprios pets
- ✅ Cadastrar novo pet
- ✅ Editar informações do pet
- ✅ Ver consultas dos seus pets
- ✅ Editar perfil
- ❌ Criar consultas
- ❌ Ver tutores/veterinários
- ❌ Dashboard administrativo

### Veterinário
- ✅ Ver todos os pets
- ✅ Listar e gerenciar tutores
- ✅ Listar e gerenciar veterinários
- ✅ Criar/editar/deletar consultas
- ✅ Filtrar consultas por pet/veterinário/data
- ✅ Dashboard com estatísticas
- ✅ Editar perfil

## 🗄️ Modelo de Dados

### Tabelas Principais

**usuario**
```sql
id (PK)
nome
email (UNIQUE)
senha_hash
salt
role (TUTOR | VETERINARIO)
```

**tutor** (FK: usuario_id)
```sql
usuario_id (PK, FK)
telefone
```

**veterinario** (FK: usuario_id)
```sql
usuario_id (PK, FK)
crmv (UNIQUE)
especialidade
```

**pet** (FK: tutor_id)
```sql
id (PK)
nome
raca
data_nascimento
tutor_id (FK → usuario.id onde role=TUTOR)
```

**consulta** (FK: pet_id, veterinario_id)
```sql
id (PK)
data_consulta
motivo
pet_id (FK → pet.id)
veterinario_id (FK → usuario.id onde role=VETERINARIO)
```

## 🔒 Segurança

### Implementado
- ✅ **Autenticação**: Login com email + senha
- ✅ **Autorização**: Role-based access control (RBAC)
- ✅ **Criptografia**: Senhas com salt + bcrypt (PasswordUtil)
- ✅ **CSRF**: Token validado em formulários POST e em `POST`/`PUT`/`DELETE` de `/api/*`
- ✅ **Injection**: PreparedStatements em todas as queries
- ✅ **IDOR**: Validação de propriedade em editar/deletar (pet, consulta)
- ✅ **HTTP-only Cookies**: Sessão não acessível via JavaScript
- ✅ **No-Cache Headers**: Evita cache de páginas sensíveis
- ✅ **Charset UTF-8**: Previne encoding attacks

### Boas Práticas
- Input sanitization (InputSanitizer, InputValidator)
- Sensitive fields not exposed to client (senha_hash, salt)
- GET deletes bloqueadas (POST obrigatório)
- Logout invalida sessão anterior

## 🎨 Design System

### Cores
- **Primary**: Azul (ações principais)
- **Accent**: Rosa (destaques)
- **Neutral**: Cream/Lavender (backgrounds)
- **Text**: Slate (legibilidade)

### Tipografia
- **Display**: Playfair Display (títulos)
- **Body**: DM Sans (conteúdo)

### Layout
- Responsive (mobile-first)
- Sidebar fixo em desktop
- Grid fluido
- Dark mode pronto (CSS vars)

## 📦 Dependências Principais

```xml
<!-- Jakarta Servlet API -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
</dependency>

<!-- PostgreSQL JDBC -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>

<!-- JUnit 5 (testes) -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.13.2</version>
    <scope>test</scope>
</dependency>
```

## 📝 Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DB_URL` | URL de conexão PostgreSQL | `jdbc:postgresql://localhost:5432/clinica` |
| `DB_USER` | Usuário BD | `postgres` |
| `DB_PASSWORD` | Senha BD | `postgres` |
| `FRONTEND_ORIGIN` | Origem do front liberada no CORS (`CorsFilter`) | `http://localhost:5173` |

> No `docker-compose.yml`, `DB_URL`/`DB_USER`/`DB_PASSWORD` já vêm configurados para o container `db` (Postgres na porta 5433 do host, `5432` dentro da rede do Docker).

## 🌐 Frontend (React) + GitHub Pages

O frontend React fica em `frontend/` e é publicado no GitHub Pages via `.github/workflows/deploy.yml` (build + deploy automático a cada push na `main`).

- **URL de produção**: https://gsnog.github.io/gestao-clinica-veterinaria/
- **Base URL da API**: configurada em `frontend/.env` (`VITE_API_BASE_URL`), apontando por padrão para `http://localhost:8080/clinica` — ou seja, mesmo com o front publicado no GitHub Pages, ele continua chamando o backend rodando na sua máquina local.
- **CORS**: o `CorsFilter` (`src/main/java/.../filter/CorsFilter.java`) libera as origens `http://localhost:5173` (dev local do Vite) e `https://gsnog.github.io` (GitHub Pages) para acessar `/api/*` com cookies de sessão.

Para rodar o front localmente:

```bash
cd frontend
npm install
npm run dev
```

## 🧪 Testando Localmente

### Usuário Tutor (teste)
```
Email: tutor@exemplo.com
Senha: senha123
```

### Usuário Veterinário (teste)
```
Email: vet@exemplo.com
Senha: senha123
```

## 📚 Endpoints Principais

| Método | Rota | Descrição |
|--------|------|-----------|
| GET/POST | `/login` | Autenticação |
| GET/POST | `/registro` | Cadastro de novo usuário |
| POST | `/logout` | Encerrar sessão |
| GET | `/dashboard` | Dashboard vet (stats) |
| GET/POST | `/pets` | CRUD de pets (filtrado por tutor) |
| GET/POST | `/consultas` | CRUD de consultas |
| GET/POST | `/tutores` | CRUD de tutores (vet only) |
| GET/POST | `/veterinarios` | CRUD de veterinários (vet only) |
| GET | `/perfil` | Perfil do usuário logado |

## 🐛 Troubleshooting

### "Conexão recusada ao BD"
- Verificar se PostgreSQL está rodando: `psql -l`
- Revisar variáveis de ambiente: `echo $DB_URL`
- Testar credenciais: `psql -h localhost -U vet_admin -d clinica`

### "Encoding error (VeterinÃ¡rio)"
- Garantir charset UTF-8 na JVM: `-Dfile.encoding=UTF-8`
- Verificar JSP: `<%@ page contentType="text/html; charset=UTF-8" %>`
- NoCacheFilter garante `charset=UTF-8` na resposta

### "CSRF token inválido"
- Token armazenado em sessão server-side
- Incluir `csrf_token.jsp` em todos os formulários POST
- No React/API, chamar `GET /api/csrf` e enviar `X-CSRF-Token` nas requisições mutantes
- Não usar formulários dinâmicos sem token

## Vídeo do fluxo da aplicação
- https://drive.google.com/file/d/1MhwRkFLC3_1SiI5TNvc2ZsMz_Y9O5Ikj/view?usp=drive_link

## 📄 Licença

Projeto educacional - UFF 2026

## 👥 Contribuidores

- **Vítoria** (Frontend, UX)
- **Arben** (Banco de Dados)
- **Giovana** (Backend)
- **Sara** (Backend, Segurança)
- **Enzo** (QA)
---
