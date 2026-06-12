# Matriz de Cobertura QA - PR #43

## PR avaliado

- **PR:** #43
- **Branch:** `fix/seguranca-api-react`
- **Área:** Segurança / CSRF
- **Base local:** `http://localhost:8080/clinica`
- **Resultado:** **APROVADO**

## 1. Cobertura por componente

| Componente | Função validada | Resultado |
|---|---|---|
| `ApiCsrfServlet.java` | Criação do endpoint `GET /api/csrf` | **Aprovado** |
| `ApiAuthFilter.java` | Liberação de `/api/csrf` como rota pública | **Aprovado** |
| `CsrfFilter.java` | Validação de token em `POST`, `PUT` e `DELETE` | **Aprovado** |
| `CorsFilter.java` | Liberação do header `X-CSRF-Token` | **Aprovado** |
| `apiClient.js` | Busca e envio automático do token CSRF | **Aprovado** |
| `AuthContext.jsx` | Logout chamando API e limpando token local | **Aprovado** |
| `ApiConsultaServlet.java` | Reforço de segurança para `veterinarioId` | **Aprovado** |
| `bruno/vetcare-api-csrf-pr43/` | Testes Bruno do fluxo CSRF | **Aprovado** |

## 2. Cobertura por método HTTP

| Método | Comportamento esperado | Resultado |
|---|---|---|
| GET | Não exige CSRF para leitura | **Aprovado** |
| POST | Exige `X-CSRF-Token` | **Aprovado** |
| PUT | Exige `X-CSRF-Token` | **Aprovado** |
| DELETE | Exige `X-CSRF-Token` | **Aprovado** |
| OPTIONS | Permitido para preflight CORS | **Aprovado** |

## 3. Cobertura Bruno

| ID | Cenário | Resultado esperado | Resultado QA |
|---|---|---|---|
| QA43-001 | Obter token CSRF | `200 OK` com `csrfToken` | **Aprovado** |
| QA43-002 | Login sem token CSRF | `403 Forbidden` | **Aprovado** |
| QA43-003 | Login com token CSRF | Login permitido | **Aprovado** |
| QA43-004 | Criar recurso com token | Sucesso conforme endpoint | **Aprovado** |
| QA43-005 | Atualizar recurso com token | Sucesso conforme endpoint | **Aprovado** |
| QA43-006 | Excluir recurso com token | Sucesso conforme endpoint | **Aprovado** |
| QA43-007 | Mutação sem token | Bloqueio com `403 Forbidden` | **Aprovado** |

## 4. Decisão QA

A cobertura de segurança do PR #43 é suficiente para o escopo do trabalho.

**Decisão QA:** **APROVADO**
