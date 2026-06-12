# Guia completo — Perfil ADMIN (Banco, Backend e Frontend)

**Status:** Implementado. Pronto para revisão e testes.

Este documento descreve a adição do terceiro perfil de usuário ao VetCare — o **ADMIN**
(administrador da plataforma) — cobrindo as alterações no banco PostgreSQL, no backend
(Servlets/DAO) e no frontend (JSP), além dos testes e critérios de aceite.

## Contexto e motivação

Neste projeto, minha responsabilidade era o **banco de dados**. Enquanto trabalhava no schema,
percebi uma lacuna: não existia nenhuma forma de **administrar as contas** dos usuários sem
editar o banco manualmente — criar, remover ou mudar o papel de alguém exigia rodar SQL na mão.

Como eu já tinha familiaridade com **backend** e **frontend** além da minha área, implementei o
perfil ADMIN de ponta a ponta: do banco (minha responsabilidade) à lógica de backend e às telas.

## 1) Visão geral do que foi implementado

O sistema possuía dois perfis (`role`): `TUTOR` e `VETERINARIO`. Foi adicionado o perfil
`ADMIN`, com as seguintes capacidades:

- Painel administrativo (`/admin`) com o resumo da plataforma.
- Gestão de usuários (`/usuarios`): listar, buscar, criar, editar, redefinir senha e excluir.
- Alteração do perfil (`role`) de um usuário.
- Acesso às telas de supervisão (consultas, pets, tutores e veterinários).

## 2) Decisão de segurança: como o administrador é criado

A validação **não fica no login** — o login apenas autentica e redireciona conforme o `role`
gravado no banco. O ponto crítico é **não permitir que qualquer pessoa vire admin**: por isso
o perfil ADMIN **não é exposto no cadastro público** (`/registro`).

O administrador nasce de duas formas controladas:

1. O **primeiro admin** é criado por um *seed* SQL (`seed_admin.sql`).
2. Os **próximos admins** são criados por um admin autenticado, na tela protegida `/usuarios`.

O cadastro público continua aceitando apenas `TUTOR` e `VETERINARIO` (não foi alterado).

## 3) Alterações no banco de dados

### 3.1 Tabela `usuario` — restrição do campo `role`

A coluna `role` passou a aceitar o valor `ADMIN`:

```sql
-- antes
role VARCHAR(20) NOT NULL CHECK (role IN ('TUTOR', 'VETERINARIO'))
-- depois
role VARCHAR(20) NOT NULL CHECK (role IN ('TUTOR', 'VETERINARIO', 'ADMIN'))
```

Migração para banco já existente (sem recriar a tabela):

```sql
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_role_check;
ALTER TABLE usuario ADD CONSTRAINT usuario_role_check
    CHECK (role IN ('TUTOR', 'VETERINARIO', 'ADMIN'));
```

### 3.2 Seed do primeiro administrador (`seed_admin.sql`)

O hash segue o esquema do `PasswordUtil`: `Base64(SHA-256(senha + salt))`.

- E-mail: `admin@vetcare.com`
- Senha: `Admin@123` (trocar após o primeiro login)

```sql
INSERT INTO usuario (nome, email, senha_hash, salt, role) VALUES
('Administrador VetCare', 'admin@vetcare.com',
 '2n+PBIZynVTx4ofWZkFbF/VdJNdvv3GiWzDOpaCjMwo=',
 'c2VlZEFkbWluU2FsdDIwMjY=', 'ADMIN')
ON CONFLICT (email) DO NOTHING;
```

## 4) Operações SQL adicionadas (camada DAO)

No `UsuarioDAOImpl` (todas com `PreparedStatement`, padrão anti-injection do projeto):

- `SELECT id, nome, email, role FROM usuario ORDER BY nome`  → listar todos os usuários
- `UPDATE usuario SET role = ? WHERE id = ?`                 → alterar o perfil
- `SELECT COUNT(*) FROM usuario WHERE role = ?`              → contar admins (trava do último admin)

Reaproveitadas do código existente: `salvar`, `deletar`, `atualizarEmail`, `atualizarSenha`,
`buscarPorEmail`, `buscarPorId`.

## 5) Backend — componentes

- `controller/LoginServlet.java` — redireciona o ADMIN para `/admin` após o login.
- `filter/AuthFilter.java` — protege `/admin` e `/usuarios` (exclusivos do ADMIN) e libera o
  ADMIN nas telas de supervisão (`/dashboard`, `/tutores`, `/veterinarios`).
- `controller/AdminServlet.java` — monta o painel administrativo.
- `controller/UsuarioServlet.java` — CRUD de usuários; aplica as regras de segurança.
- `util/UsuarioPolicy.java` — regras de segurança puras (testáveis sem banco).
- `dao/UsuarioDAO.java` e `dao/UsuarioDAOImpl.java` — novas operações da seção 4.
- `controller/PerfilServlet.java` — ADMIN pode atualizar o próprio e-mail.

## 6) Regras de segurança (guardrails)

| Regra | Justificativa |
|-------|---------------|
| Admin não pode excluir a própria conta | evita perda de acesso ao sistema |
| Não excluir/rebaixar o **último** admin | garante sempre ao menos um administrador |
| `role` só pode ser TUTOR, VETERINARIO ou ADMIN | impede valores inválidos no banco |
| Escrita exige token CSRF | proteção já existente no projeto (CsrfFilter) |
| Cada tela administrativa revalida o `role` | defesa em profundidade além do AuthFilter |

## 7) Frontend — telas

- `webapp/components/sidebar.jsp` — exibe "Admin" e "Usuários" quando o perfil é ADMIN.
- `webapp/admin-dashboard.jsp` — painel com os totais da plataforma.
- `webapp/lista-usuarios.jsp` — tabela de usuários, com busca e ações.
- `webapp/form-usuario.jsp` — criar/editar usuário e redefinir senha.
- `webapp/perfil.jsp` — formulário de e-mail também disponível para o ADMIN.

As telas reutilizam o design system e os componentes (`head.jsp`, `csrf_token.jsp`) existentes.

## 8) Testes

- `src/test/java/.../util/UsuarioPolicyTest.java` — 9 testes das regras de segurança
  (último admin, auto-exclusão, perfil inválido, promoção/rebaixamento).
- `pom.xml` — `maven-surefire-plugin` 3.2.5 adicionado para executar os testes JUnit 5.

## 9) Referências de código (arquivos tocados)

Criados:
- `src/main/resources/db/seed_admin.sql`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/AdminServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/UsuarioServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/util/UsuarioPolicy.java`
- `src/main/webapp/admin-dashboard.jsp`
- `src/main/webapp/lista-usuarios.jsp`
- `src/main/webapp/form-usuario.jsp`
- `src/test/java/com/uff/gestaoclinicaveterinaria/util/UsuarioPolicyTest.java`

Alterados:
- `src/main/resources/db/tables.sql`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/LoginServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/PerfilServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/filter/AuthFilter.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/UsuarioDAO.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/dao/UsuarioDAOImpl.java`
- `src/main/webapp/components/sidebar.jsp`
- `src/main/webapp/perfil.jsp`
- `pom.xml`

## 10) Checklist prático de validação

- [ ] Rodar `seed_admin.sql` (e a migração do `CHECK`, se o banco já existir).
- [ ] `mvn test` — `UsuarioPolicyTest` passa com 9 testes OK.
- [ ] `mvn clean package` e subir a aplicação (Tomcat ou IntelliJ).
- [ ] Login `admin@vetcare.com` / `Admin@123` → cai em `/admin`.
- [ ] `/registro` não oferece a opção ADMIN.
- [ ] Tutor/Vet tentando abrir `/usuarios` → acesso negado.
- [ ] Criar, editar, redefinir senha e excluir usuários em `/usuarios`.
- [ ] Tentar excluir o próprio admin e tentar deixar o sistema sem admin → ambos bloqueados.
- [ ] Trocar a senha do seed e logar com a nova senha.

## 11) Critérios objetivos de aceite

- O banco aceita o valor `ADMIN` no campo `role` e o seed cria o primeiro administrador.
- O ADMIN não pode ser criado pelo cadastro público (`/registro`).
- As rotas `/admin` e `/usuarios` são acessíveis apenas pelo ADMIN.
- O CRUD de usuários funciona e respeita as regras de segurança (seção 6).
- Os testes automatizados das regras de segurança passam.
- O administrador autentica e é direcionado ao painel administrativo.

## 12) Observação técnica para evolução futura

O `PasswordUtil` usa `SHA-256(senha+salt)`, e não bcrypt como o README sugere. É um hash
rápido, mais frágil contra força bruta. Recomenda-se migrar para bcrypt/Argon2 em um trabalho
separado, por afetar todos os usuários — registrado aqui por o ADMIN ser a conta mais sensível.
