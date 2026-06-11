## Validação QA - PR #43

Realizei a validação do PR #43 com foco no ajuste de segurança CSRF da API utilizada pelo frontend React.

### Resultado

**APROVADO**

### Escopo validado

O PR #43 foi validado considerando o objetivo de proteger requisições mutáveis da API JSON sem quebrar a integração com o frontend React.

A validação contemplou:

- criação do endpoint `GET /api/csrf`;
- geração de token CSRF por sessão;
- liberação de `/api/csrf` como rota pública;
- exigência de `X-CSRF-Token` em `POST`, `PUT` e `DELETE`;
- resposta JSON para erro de CSRF;
- CORS permitindo o header `X-CSRF-Token`;
- frontend buscando e enviando token automaticamente;
- login funcionando pelo frontend React;
- logout chamando a API e limpando o token local;
- reforço de regra em consultas para não confiar cegamente no `veterinarioId` enviado pelo frontend;
- collection Bruno específica para cenários de CSRF.

### Base local utilizada nos testes

```txt
http://localhost:8080/clinica
```

### Pontos validados

- `GET /api/csrf` retorna token CSRF em JSON.
- `/api/csrf` está liberado no filtro de autenticação.
- Métodos mutáveis da API exigem token CSRF.
- Requisições sem token são bloqueadas com `403 Forbidden`.
- O bloqueio de requisição sem token é comportamento esperado e aprovado.
- O header `X-CSRF-Token` foi liberado no CORS.
- O frontend React consegue buscar o token automaticamente.
- O frontend envia `X-CSRF-Token` em `POST`, `PUT` e `DELETE`.
- O login pelo site React foi validado com sucesso.
- O logout chama `POST /api/logout` e limpa o token local.
- A API de consultas usa o veterinário autenticado como referência segura.

### Cobertura Bruno

Foi adicionada uma collection Bruno para validação do fluxo CSRF:

```txt
bruno/vetcare-api-csrf-pr43/
```

Cenários cobertos:

- Obter token CSRF.
- Validar bloqueio de login sem token.
- Validar login com token.
- Validar mutação protegida com token.
- Validar uso de `X-CSRF-Token` em `POST`, `PUT` e `DELETE`.

### Evidências adicionadas

Foram adicionados arquivos de QA e Bruno na branch:

```txt
qa/pr43/pr-43-relatorio-qa.md
qa/pr43/pr-43-matriz-cobertura-seguranca.md
qa/pr43/pr-43-comentario-review.md
bruno/vetcare-api-csrf-pr43/
```

### Observação QA

O retorno `403 Forbidden` para requisições mutáveis sem `X-CSRF-Token` é considerado positivo, pois demonstra que a proteção CSRF está ativa.

O login pelo frontend React foi validado com sucesso após o fluxo correto de obtenção e envio automático do token CSRF.

### Decisão

QA aprovado para o escopo do PR #43.
