# VetCare API - Collection Bruno

Collection consolidada para testar a API REST (`/api/*`) do back-end VetCare
(Java Servlets), usada pelo front-end React.

## Base URL

```txt
http://localhost:8080/clinica
```

Se o Tomcat estiver rodando com outro context path, ajuste a URL em cada request.

## Como abrir no Bruno

1. Abra o Bruno.
2. **Open Collection** → selecione a pasta `bruno/vetcare-api`.
3. Suba o backend (`docker compose up -d --build`, veja o README da raiz do projeto).

## Fluxo recomendado

A API exige um token CSRF (header `X-CSRF-Token`) em toda mutação (`POST`/`PUT`/`DELETE`),
e autenticação via sessão (cookie) para a maioria das rotas.

1. **`00 Auth e CSRF/01 Obter Token CSRF`** — obtém o token e guarda em `csrfToken`
   (via script `post-response`, reaproveitado pelos demais requests).
2. **`00 Auth e CSRF/02 Login sem CSRF - Bloqueio Esperado`** — confirma que uma
   mutação sem o header `X-CSRF-Token` é bloqueada com `403`.
3. **`00 Auth e CSRF/03 Login Tutor`**, **`04 Login Veterinario`** ou
   **`05 Login Admin`** — autentica e cria a sessão (cookie) usada pelas demais rotas.
4. A partir daí, execute as pastas conforme o papel logado:

| Pasta | Papel necessário |
|---|---|
| `01 Perfil` | Qualquer usuário autenticado |
| `02 Dashboard` | `01` Veterinário/Admin · `02` Admin |
| `03 Pets` | Tutor (próprios pets) / Veterinário (todos) |
| `04 Consultas` | Veterinário (TUTOR só lista) |
| `05 Tutores` | Veterinário/Admin |
| `06 Veterinarios` | Veterinário/Admin |
| `07 Usuarios (Admin)` | Admin |
| `00 Auth e CSRF/06 Logout` | Qualquer usuário autenticado |

## Usuários de teste (seed)

| Papel | E-mail | Senha |
|---|---|---|
| Tutor | `tutor@exemplo.com` | `senha123` |
| Veterinário | `vet@exemplo.com` | `senha123` |
| Admin | `admin@vetcare.com` | `Admin@123` |

## Observações

- Os requests de criação/edição usam IDs (`1`, `2`, ...) compatíveis com os
  dados do `seed.sql`/`seed_admin.sql`. Ajuste os IDs caso seu banco tenha
  outros registros.
- Cada request valida o status HTTP esperado e, quando aplicável, que a
  resposta é `application/json`.
