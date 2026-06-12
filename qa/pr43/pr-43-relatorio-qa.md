# Relatório de QA - PR #43 - Segurança CSRF da API

## 1. Identificação

- **Pull Request:** #43
- **Título:** fix(seguranca): ajustar CSRF da API para integração com React
- **Branch analisada:** `fix/seguranca-api-react`
- **Base local validada:** `http://localhost:8080/clinica`
- **Área:** Segurança / CSRF / Integração React + API Java Servlet
- **Tipo de validação:** Inspeção técnica da branch + validação funcional + testes Bruno
- **Resultado geral:** **APROVADO**

## 2. Objetivo da validação

Validar se o PR #43 ajusta corretamente o fluxo de segurança CSRF da API JSON utilizada pelo frontend React.

O foco principal foi garantir que métodos mutáveis da API, como `POST`, `PUT` e `DELETE`, sejam protegidos por token CSRF sem impedir o funcionamento do login, logout e demais fluxos do frontend.

## 3. Escopo validado

Foram validados os seguintes pontos:

- criação do endpoint `GET /api/csrf`;
- geração de token CSRF associado à sessão;
- liberação de `/api/csrf` como rota pública;
- exigência do header `X-CSRF-Token` em requisições mutáveis da API;
- bloqueio correto de requisições sem token;
- retorno JSON para erro de CSRF;
- liberação do header `X-CSRF-Token` no CORS;
- busca automática do token pelo frontend;
- envio automático do token em `POST`, `PUT` e `DELETE`;
- login funcional pelo frontend React;
- logout chamando a API e limpando o token local;
- reforço de segurança no endpoint de consultas.

## 4. Pontos validados

| Ponto validado | Resultado |
|---|---|
| `GET /api/csrf` retorna token CSRF em JSON | **Aprovado** |
| `/api/csrf` está liberado no filtro de autenticação | **Aprovado** |
| `POST`, `PUT` e `DELETE` exigem `X-CSRF-Token` | **Aprovado** |
| Requisições mutáveis sem token retornam `403 Forbidden` | **Aprovado** |
| Erros de CSRF retornam JSON | **Aprovado** |
| CORS permite o header `X-CSRF-Token` | **Aprovado** |
| Frontend busca token antes de mutações | **Aprovado** |
| Frontend envia token automaticamente em mutações | **Aprovado** |
| Login pelo React funciona com token automático | **Aprovado** |
| Logout chama `POST /api/logout` e limpa token local | **Aprovado** |
| API de consultas não confia cegamente no `veterinarioId` do frontend | **Aprovado** |

## 5. Endpoints e fluxos validados

| Endpoint/Fluxo | Método | Papel | Resultado |
|---|---|---|---|
| `/api/csrf` | GET | Gerar/obter token CSRF da sessão | **Aprovado** |
| `/api/login` com token | POST | Autenticar usuário com CSRF válido | **Aprovado** |
| `/api/login` sem token | POST | Bloquear mutação sem CSRF | **Aprovado** |
| `/api/logout` com token | POST | Encerrar sessão de forma protegida | **Aprovado** |
| Mutações da API | POST | Criar recursos com CSRF | **Aprovado** |
| Mutações da API | PUT | Atualizar recursos com CSRF | **Aprovado** |
| Mutações da API | DELETE | Excluir recursos com CSRF | **Aprovado** |
| `/api/consultas` | POST/PUT | Usar veterinário autenticado como referência segura | **Aprovado** |

## 6. Cobertura Bruno

Foi adicionada uma collection Bruno específica para o PR #43:

```txt
bruno/vetcare-api-csrf-pr43/
```

Cenários cobertos:

- obter token CSRF;
- validar bloqueio de login sem token;
- validar login com token;
- validar mutação protegida com token;
- validar uso de `X-CSRF-Token` em `POST`, `PUT` e `DELETE`.

## 7. Observações QA

O retorno `403 Forbidden` para requisições mutáveis sem `X-CSRF-Token` é considerado comportamento correto e positivo, pois demonstra que a proteção CSRF está ativa.

O login pelo frontend React foi validado com sucesso após o fluxo correto de obtenção e envio automático do token CSRF.

Para testes positivos no Bruno, o fluxo correto é:

1. Executar `GET /api/csrf`.
2. Copiar o valor retornado em `csrfToken`.
3. Definir esse valor na variável `csrfToken` do Bruno.
4. Executar `POST /api/login` com o header `X-CSRF-Token`.
5. Executar os demais requests mutáveis usando o mesmo header.

## 8. Conclusão

O PR #43 está **APROVADO** na validação de QA.

A implementação protege corretamente as requisições mutáveis da API, mantém o login funcional no frontend React e melhora a segurança geral do sistema.

**Status final do PR #43:** **APROVADO**
