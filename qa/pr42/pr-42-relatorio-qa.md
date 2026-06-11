# Relatório Detalhado de QA - Endpoints da API - PR #42

## 1. Identificação

- **Projeto:** Gestão de Clínica Veterinária - VetCare
- **Pull Request:** #42
- **Título do PR:** adaptando backend para integração com React
- **Branch analisada:** `feat/integracao-react-back`
- **Área validada:** Backend Java Servlet como API JSON
- **Ferramenta de apoio:** Bruno
- **Base URL local validada:** `http://localhost:8080/clinica`
- **Tipo de validação:** Testes de API documentados em Bruno + inspeção técnica dos Servlets
- **Resultado geral:** **APROVADO**

---

## 2. Objetivo do relatório

Este relatório tem como objetivo registrar, de forma detalhada, a validação dos endpoints da API adicionados no PR #42.

O PR #42 adapta o backend Java Servlet para deixar de depender exclusivamente de páginas JSP e passar a fornecer endpoints JSON consumíveis pelo frontend React.

A validação considera os principais critérios do trabalho:

- criação de API JSON;
- comunicação entre frontend React e backend Java;
- separação entre camada de apresentação e backend;
- uso de métodos HTTP adequados;
- autenticação por sessão;
- controle de autorização por perfil;
- respostas em JSON;
- estrutura testável com Bruno;
- cobertura dos fluxos principais da aplicação.

---

## 3. Contexto da API

A API do PR #42 está disponível sob o path:

```txt
/api/*
```

No ambiente local validado, a URL base utilizada no Bruno é:

```txt
http://localhost:8080/clinica
```

Portanto, um endpoint como `/api/login` deve ser acessado em:

```txt
http://localhost:8080/clinica/api/login
```

---

## 4. Observação sobre as portas

Durante a validação, foram identificadas duas portas no ambiente local:

| Porta | Função | Uso nos testes |
|---|---|---|
| `5173` | Frontend React/Vite | Usada para acessar a interface React |
| `8080` | Backend Java/Tomcat | Usada no Bruno para testar a API |

Conclusão:

```txt
Bruno/API → http://localhost:8080/clinica
Frontend → http://localhost:5173
```

---

## 5. Estratégia de validação

A validação foi dividida em quatro frentes:

1. **Mapeamento dos endpoints**
   - Verificação dos Servlets anotados com `@WebServlet`.
   - Identificação dos métodos `doGet`, `doPost`, `doPut` e `doDelete`.

2. **Validação funcional**
   - Verificação se cada endpoint cumpre o papel esperado dentro do domínio da clínica veterinária.

3. **Validação de segurança**
   - Verificação de autenticação por sessão.
   - Verificação de restrição por perfil `TUTOR` e `VETERINARIO`.

4. **Validação com Bruno**
   - Criação de requests para os principais fluxos da API.
   - Cobertura de GET, POST, PUT e DELETE.
   - Registro dos resultados esperados.

---

## 6. Resultado geral por grupo de endpoints

| Grupo | Endpoint base | Métodos cobertos | Resultado |
|---|---|---|---|
| Autenticação | `/api/login`, `/api/logout` | POST | **Aprovado** |
| Registro | `/api/registro` | POST | **Aprovado** |
| Perfil | `/api/perfil` | GET, POST | **Aprovado** |
| Dashboard | `/api/dashboard` | GET | **Aprovado** |
| Pets | `/api/pets`, `/api/pets/{id}` | GET, POST, PUT, DELETE | **Aprovado** |
| Consultas | `/api/consultas`, `/api/consultas/{id}` | GET, POST, PUT, DELETE | **Aprovado** |
| Tutores | `/api/tutores`, `/api/tutores/{id}` | GET, POST, PUT, DELETE | **Aprovado** |
| Veterinários | `/api/veterinarios`, `/api/veterinarios/{id}` | GET, POST, PUT, DELETE | **Aprovado** |

---

# 7. Validação endpoint por endpoint

---

## 7.1 Autenticação

### 7.1.1 `POST /api/login`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/login` |
| Método | `POST` |
| Autenticação necessária | Não |
| Perfil necessário | Nenhum |
| Corpo esperado | JSON com `email`, `senha` e `lembrar` |
| Status esperado | `200 OK` para credenciais válidas |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `/api/login` é responsável por autenticar usuários no sistema e iniciar uma sessão HTTP.

Ele cumpre corretamente o papel esperado porque:

- recebe dados em JSON;
- valida campos obrigatórios;
- valida formato de e-mail;
- sanitiza o e-mail informado;
- consulta o usuário no banco;
- verifica senha;
- invalida sessão antiga, quando existente;
- cria nova sessão;
- armazena `usuarioId`, `usuarioNome` e `usuarioRole`;
- retorna usuário autenticado em JSON;
- não retorna senha, hash ou salt.

#### Exemplo de body Bruno

```json
{
  "email": "tutor@exemplo.com",
  "senha": "senha123",
  "lembrar": false
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de autenticação da aplicação React com backend Java.

---

### 7.1.2 `POST /api/logout`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/logout` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | Qualquer usuário autenticado |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `/api/logout` é responsável por encerrar a sessão do usuário autenticado.

Ele cumpre corretamente o papel esperado porque:

- obtém a sessão atual;
- invalida a sessão quando ela existe;
- retorna JSON de sucesso;
- permite que o frontend React finalize o estado de autenticação.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de encerramento de sessão.

---

## 7.2 Registro

### 7.2.1 `POST /api/registro`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/registro` |
| Método | `POST` |
| Autenticação necessária | Não |
| Perfil necessário | Nenhum |
| Corpo esperado | JSON com dados de tutor ou veterinário |
| Status esperado | `201 Created` para cadastro válido |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `/api/registro` é responsável por cadastrar novos usuários no sistema, tanto tutores quanto veterinários.

Ele cumpre corretamente o papel esperado porque:

- recebe JSON com dados do usuário;
- valida nome completo;
- valida e-mail;
- valida senha mínima;
- valida tipo de usuário;
- valida telefone quando o perfil é `TUTOR`;
- valida CRMV e especialidade quando o perfil é `VETERINARIO`;
- impede cadastro com e-mail duplicado;
- gera salt e hash de senha;
- cria o usuário base;
- cria o registro complementar de tutor ou veterinário;
- retorna resposta JSON com `success` e dados públicos do usuário.

#### Exemplo de body Bruno para tutor

```json
{
  "nome": "Tutor QA",
  "email": "tutor.qa@example.com",
  "senha": "senha123",
  "role": "TUTOR",
  "telefone": "(21) 99999-9999"
}
```

#### Exemplo de body Bruno para veterinário

```json
{
  "nome": "Veterinario QA",
  "email": "vet.qa@example.com",
  "senha": "senha123",
  "role": "VETERINARIO",
  "crmv": "CRMV-RJ 12345",
  "especialidade": "Clínica Geral"
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de cadastro centralizado da aplicação.

---

## 7.3 Perfil

### 7.3.1 `GET /api/perfil`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/perfil` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | Qualquer usuário autenticado |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/perfil` é responsável por retornar os dados do usuário autenticado.

Ele cumpre corretamente o papel esperado porque:

- usa o `usuarioId` da sessão;
- busca o usuário autenticado;
- retorna dados públicos do usuário;
- retorna telefone quando aplicável;
- responde em JSON;
- retorna erro quando o usuário não é encontrado.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de consulta do perfil autenticado.

---

### 7.3.2 `POST /api/perfil`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/perfil` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | Qualquer usuário autenticado |
| Corpo esperado | JSON com `email` e `telefone` |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `POST /api/perfil` é responsável por atualizar dados editáveis do perfil.

Ele cumpre corretamente o papel esperado porque:

- identifica o usuário pela sessão;
- valida e-mail;
- valida telefone quando necessário;
- impede alteração para e-mail já utilizado por outro usuário;
- atualiza dados no banco;
- retorna os dados atualizados em JSON.

#### Exemplo de body Bruno

```json
{
  "email": "tutor@exemplo.com",
  "telefone": "(21) 99999-9999"
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de atualização de perfil.

---

## 7.4 Dashboard

### 7.4.1 `GET /api/dashboard`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/dashboard` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` para veterinário |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `/api/dashboard` é responsável por fornecer estatísticas administrativas da clínica.

Ele cumpre corretamente o papel esperado porque:

- restringe acesso ao perfil `VETERINARIO`;
- bloqueia usuários sem permissão com `403 Forbidden`;
- consulta estatísticas pelo DAO;
- retorna dados estruturados em JSON.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de fornecer estatísticas administrativas para veterinários.

---

## 7.5 Pets

### 7.5.1 `GET /api/pets`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/pets` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/pets` lista pets cadastrados.

Ele cumpre corretamente o papel esperado porque:

- tutor visualiza apenas os pets vinculados ao próprio cadastro;
- veterinário pode visualizar a lista geral;
- retorna lista em JSON;
- usa DTO para não expor diretamente o modelo interno.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de listagem de pets com regra de visibilidade por perfil.

---

### 7.5.2 `GET /api/pets/{id}`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/pets/{id}` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` para pet existente |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/pets/{id}` consulta um pet específico.

Ele cumpre corretamente o papel esperado porque:

- extrai o ID do path;
- valida identificador;
- retorna `400 Bad Request` para ID inválido;
- retorna `404 Not Found` para pet inexistente;
- impede tutor de consultar pet que não pertence a ele;
- retorna `403 Forbidden` quando o tutor tenta acessar recurso de outro tutor;
- retorna o pet em JSON quando permitido.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de consulta individual de pet com validação de posse.

---

### 7.5.3 `POST /api/pets`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/pets` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | JSON com dados do pet |
| Status esperado | `201 Created` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `POST /api/pets` cria um novo pet.

Ele cumpre corretamente o papel esperado porque:

- lê JSON do corpo da requisição;
- valida nome;
- valida raça;
- valida data de nascimento;
- impede data futura;
- resolve o tutor corretamente;
- para tutor, associa o pet ao próprio usuário autenticado;
- para veterinário, exige tutor informado e existente;
- salva o pet no banco;
- retorna o pet criado em JSON.

#### Exemplo de body Bruno

```json
{
  "nome": "QA Pet Bruno",
  "raca": "SRD",
  "dataNascimento": "2021-05-10",
  "tutorId": 1
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de cadastro de pets.

---

### 7.5.4 `PUT /api/pets`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/pets` |
| Método | `PUT` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | JSON com `id`, `nome`, `raca`, `dataNascimento` e `tutorId` |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `PUT /api/pets` atualiza um pet existente.

Ele cumpre corretamente o papel esperado porque:

- exige ID do pet;
- valida se o pet existe;
- retorna `404 Not Found` quando o pet não existe;
- impede tutor de editar pet de outro tutor;
- valida dados obrigatórios;
- sanitiza texto;
- atualiza o pet no banco;
- retorna pet atualizado em JSON.

#### Observação importante para Bruno

O ID usado no request precisa existir no banco.

Fluxo recomendado:

1. Rodar `POST /api/pets`.
2. Copiar o `id` retornado.
3. Usar esse `id` no body do `PUT /api/pets`.

Se o ID não existir, o retorno correto é:

```json
{
  "success": false,
  "message": "Pet não encontrado."
}
```

Esse comportamento é adequado e cumpre o papel de validação do endpoint.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de atualização de pet existente e bloqueio de atualização inválida.

---

### 7.5.5 `DELETE /api/pets/{id}`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/pets/{id}` |
| Método | `DELETE` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `DELETE /api/pets/{id}` exclui um pet existente.

Ele cumpre corretamente o papel esperado porque:

- extrai ID do path;
- valida identificador;
- verifica se o pet existe;
- impede tutor de excluir pet de outro tutor;
- remove o pet do banco;
- retorna JSON de sucesso.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de exclusão de pet com regra de autorização.

---

## 7.6 Consultas

### 7.6.1 `GET /api/consultas`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/consultas` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/consultas` lista consultas veterinárias.

Ele cumpre corretamente o papel esperado porque:

- tutor visualiza apenas consultas vinculadas aos seus pets;
- veterinário visualiza a lista geral;
- retorna dados em JSON;
- usa DTO para representar consulta, pet e veterinário.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de listagem de consultas respeitando o perfil autenticado.

---

### 7.6.2 `POST /api/consultas`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/consultas` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | JSON com pet, veterinário, data, motivo e diagnóstico |
| Status esperado | `201 Created` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `POST /api/consultas` cria uma nova consulta.

Ele cumpre corretamente o papel esperado porque:

- restringe criação ao perfil `VETERINARIO`;
- bloqueia tutor com `403 Forbidden`;
- valida corpo JSON;
- valida pet existente;
- valida veterinário existente;
- valida data e hora;
- valida motivo obrigatório;
- sanitiza motivo e diagnóstico;
- salva consulta no banco;
- retorna consulta criada em JSON.

#### Exemplo de body Bruno

```json
{
  "petId": 1,
  "veterinarioId": 2,
  "dataConsulta": "2026-06-20T10:30:00",
  "motivo": "Consulta QA Bruno",
  "diagnostico": "Paciente em acompanhamento"
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de criação de consulta por veterinário.

---

### 7.6.3 `PUT /api/consultas`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/consultas` |
| Método | `PUT` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | JSON com `id`, `petId`, `veterinarioId`, `dataConsulta`, `motivo` e `diagnostico` |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `PUT /api/consultas` atualiza uma consulta existente.

Ele cumpre corretamente o papel esperado porque:

- restringe edição ao perfil `VETERINARIO`;
- exige ID da consulta;
- verifica se a consulta existe;
- valida pet;
- valida veterinário;
- valida data e hora;
- valida motivo;
- sanitiza campos textuais;
- atualiza a consulta no banco;
- retorna consulta atualizada em JSON.

#### Observação para Bruno

O ID informado precisa existir. Caso contrário, o retorno correto é `Consulta não encontrada.`.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de atualização de consulta existente.

---

### 7.6.4 `DELETE /api/consultas/{id}`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/consultas/{id}` |
| Método | `DELETE` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `DELETE /api/consultas/{id}` exclui uma consulta existente.

Ele cumpre corretamente o papel esperado porque:

- restringe exclusão ao perfil `VETERINARIO`;
- extrai ID do path;
- valida identificador;
- verifica se consulta existe;
- exclui a consulta no banco;
- retorna JSON de sucesso.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de exclusão de consulta.

---

## 7.7 Tutores

### 7.7.1 `GET /api/tutores`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/tutores` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/tutores` lista tutores conforme o perfil autenticado.

Ele cumpre corretamente o papel esperado porque:

- tutor visualiza apenas o próprio cadastro;
- veterinário visualiza a lista geral;
- retorna dados em JSON;
- utiliza DTO de resposta.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de listagem controlada de tutores.

---

### 7.7.2 `POST /api/tutores`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/tutores` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | Usuário autenticado |
| Status esperado | `405 Method Not Allowed` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `POST /api/tutores` existe para bloquear cadastro direto de tutores por essa rota.

Ele cumpre corretamente o papel esperado porque:

- impede criação de tutor fora do fluxo de registro;
- centraliza cadastro em `/api/registro`;
- retorna erro controlado;
- evita duplicação de regra de negócio.

#### Conclusão QA

**Endpoint aprovado.**  
O retorno `405 Method Not Allowed` é esperado e correto, pois cadastro de tutor deve ocorrer via `/api/registro`.

---

### 7.7.3 `PUT /api/tutores`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/tutores` |
| Método | `PUT` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | JSON com `id`, `nome` e `telefone` |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `PUT /api/tutores` atualiza dados de tutor.

Ele cumpre corretamente o papel esperado porque:

- exige ID do tutor;
- impede tutor de atualizar cadastro de outro tutor;
- verifica existência do tutor;
- valida nome completo;
- valida telefone;
- sanitiza dados;
- atualiza o tutor no banco;
- retorna tutor atualizado em JSON.

#### Exemplo de body Bruno

```json
{
  "id": 1,
  "nome": "Tutor QA Bruno",
  "telefone": "(21) 98888-8888"
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de atualização de tutor com controle de permissão.

---

### 7.7.4 `DELETE /api/tutores/{id}`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/tutores/{id}` |
| Método | `DELETE` |
| Autenticação necessária | Sim |
| Perfil necessário | `TUTOR` ou `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `DELETE /api/tutores/{id}` exclui um tutor.

Ele cumpre corretamente o papel esperado porque:

- extrai ID do path;
- valida identificador;
- impede tutor de excluir outro tutor;
- verifica se o tutor existe;
- exclui o tutor no banco;
- invalida a sessão caso o próprio tutor exclua seu cadastro;
- retorna JSON de sucesso.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de exclusão de tutor com proteção de identidade.

---

## 7.8 Veterinários

### 7.8.1 `GET /api/veterinarios`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/veterinarios` |
| Método | `GET` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `GET /api/veterinarios` lista veterinários.

Ele cumpre corretamente o papel esperado porque:

- restringe acesso ao perfil `VETERINARIO`;
- bloqueia usuários sem permissão com `403 Forbidden`;
- retorna lista em JSON;
- usa DTO de resposta.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de listagem de veterinários para usuários autorizados.

---

### 7.8.2 `POST /api/veterinarios`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/veterinarios` |
| Método | `POST` |
| Autenticação necessária | Sim |
| Perfil necessário | Usuário autenticado |
| Status esperado | `405 Method Not Allowed` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `POST /api/veterinarios` existe para bloquear cadastro direto de veterinários por essa rota.

Ele cumpre corretamente o papel esperado porque:

- impede criação de veterinário fora do fluxo de registro;
- centraliza cadastro em `/api/registro`;
- retorna erro controlado;
- evita duplicação de lógica de cadastro.

#### Conclusão QA

**Endpoint aprovado.**  
O retorno `405 Method Not Allowed` é esperado e correto, pois cadastro de veterinário deve ocorrer via `/api/registro`.

---

### 7.8.3 `PUT /api/veterinarios`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/veterinarios` |
| Método | `PUT` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | JSON com `id`, `nome`, `crmv` e `especialidade` |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `PUT /api/veterinarios` atualiza os dados do veterinário autenticado.

Ele cumpre corretamente o papel esperado porque:

- exige ID do veterinário;
- impede veterinário de atualizar cadastro de outro veterinário;
- verifica se o veterinário existe;
- valida nome completo;
- valida CRMV;
- valida especialidade;
- sanitiza dados;
- atualiza o veterinário no banco;
- retorna veterinário atualizado em JSON.

#### Exemplo de body Bruno

```json
{
  "id": 2,
  "nome": "Veterinario QA Bruno",
  "crmv": "CRMV-RJ 12345",
  "especialidade": "Clínica Geral"
}
```

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de atualização do próprio cadastro veterinário.

---

### 7.8.4 `DELETE /api/veterinarios/{id}`

| Item | Descrição |
|---|---|
| URL local | `http://localhost:8080/clinica/api/veterinarios/{id}` |
| Método | `DELETE` |
| Autenticação necessária | Sim |
| Perfil necessário | `VETERINARIO` |
| Corpo esperado | Não exige body |
| Status esperado | `200 OK` |
| Resultado QA | **Aprovado** |

#### Papel do endpoint

O endpoint `DELETE /api/veterinarios/{id}` exclui o cadastro do veterinário autenticado.

Ele cumpre corretamente o papel esperado porque:

- extrai ID do path;
- valida identificador;
- impede exclusão de outro veterinário;
- verifica se o veterinário existe;
- exclui o registro no banco;
- invalida a sessão;
- retorna JSON de sucesso.

#### Conclusão QA

**Endpoint aprovado.**  
Cumpre o papel de exclusão do próprio cadastro veterinário com proteção de identidade.

---

# 8. Validação de autenticação e autorização

## 8.1 Rotas públicas

As rotas públicas são:

| Endpoint | Motivo |
|---|---|
| `POST /api/login` | Permitir autenticação |
| `POST /api/registro` | Permitir criação de conta |

Resultado: **Aprovado**

## 8.2 Rotas protegidas

Todas as demais rotas `/api/*` exigem sessão ativa.

Quando não há sessão, o comportamento esperado é retorno JSON de não autenticado:

```json
{
  "success": false,
  "message": "Não autenticado."
}
```

Resultado: **Aprovado**

## 8.3 Controle por perfil

| Perfil | Permissões principais |
|---|---|
| `TUTOR` | Acessar próprio perfil, seus pets, suas consultas e seu próprio cadastro |
| `VETERINARIO` | Acessar dashboard, listagens administrativas, consultas, tutores e veterinários conforme regra do endpoint |

Resultado: **Aprovado**

---

# 9. Cobertura dos métodos HTTP

| Método HTTP | Endpoints cobertos | Resultado |
|---|---|---|
| GET | `/api/perfil`, `/api/dashboard`, `/api/pets`, `/api/pets/{id}`, `/api/consultas`, `/api/tutores`, `/api/veterinarios` | **Aprovado** |
| POST | `/api/login`, `/api/logout`, `/api/registro`, `/api/perfil`, `/api/pets`, `/api/consultas`, `/api/tutores`, `/api/veterinarios` | **Aprovado** |
| PUT | `/api/pets`, `/api/consultas`, `/api/tutores`, `/api/veterinarios` | **Aprovado** |
| DELETE | `/api/pets/{id}`, `/api/consultas/{id}`, `/api/tutores/{id}`, `/api/veterinarios/{id}` | **Aprovado** |

Observação:

- `POST /api/tutores` e `POST /api/veterinarios` retornam `405 Method Not Allowed`.
- Esse comportamento é correto porque o cadastro dessas entidades é centralizado em `/api/registro`.

---

# 10. Cobertura no Bruno

## 10.1 Collection

A collection Bruno foi estruturada em:

```txt
bruno/vetcare-api/
```

## 10.2 Grupos de requests

| Grupo Bruno | Requests | Resultado |
|---|---|---|
| `01 Auth` | Login Tutor, Login Veterinário, Logout | **Aprovado** |
| `02 Perfil` | Get Perfil, Atualizar Perfil | **Aprovado** |
| `03 Dashboard` | Get Dashboard | **Aprovado** |
| `04 Pets` | Listar, Criar, Atualizar, Excluir | **Aprovado** |
| `05 Consultas` | Listar, Criar, Atualizar, Excluir | **Aprovado** |
| `06 Tutores` | Listar, Atualizar | **Aprovado** |
| `07 Veterinarios` | Listar, Atualizar | **Aprovado** |

## 10.3 Ordem recomendada de execução

### Fluxo de tutor

```txt
1. Login Tutor
2. Get Perfil
3. Listar Pets
4. Criar Pet
5. Copiar ID retornado
6. Atualizar Pet com ID real
7. Excluir Pet com ID real
8. Logout
```

### Fluxo de veterinário

```txt
1. Login Veterinario
2. Get Dashboard
3. Listar Consultas
4. Criar Consulta
5. Copiar ID retornado
6. Atualizar Consulta com ID real
7. Excluir Consulta com ID real
8. Listar Tutores
9. Listar Veterinarios
10. Logout
```

---

# 11. Observações sobre IDs nos testes

Alguns endpoints dependem de IDs existentes no banco.

Exemplos:

| Endpoint | Campo/Path dependente |
|---|---|
| `PUT /api/pets` | `id` no body |
| `DELETE /api/pets/{id}` | `id` no path |
| `PUT /api/consultas` | `id` no body |
| `DELETE /api/consultas/{id}` | `id` no path |
| `PUT /api/tutores` | `id` no body |
| `DELETE /api/tutores/{id}` | `id` no path |
| `PUT /api/veterinarios` | `id` no body |
| `DELETE /api/veterinarios/{id}` | `id` no path |

Caso o ID não exista, a API retorna corretamente mensagens como:

```json
{
  "success": false,
  "message": "Pet não encontrado."
}
```

Esse retorno é considerado correto, pois demonstra que a API valida a existência do recurso antes de atualizar ou excluir.

Para evidência positiva no Bruno, recomenda-se sempre:

1. Criar o recurso.
2. Copiar o ID retornado.
3. Usar o ID real no request de atualização.
4. Usar o ID real no request de exclusão.

---

# 12. Conformidade com o enunciado

| Critério do trabalho | Evidência | Resultado |
|---|---|---|
| Backend adaptado para API | Servlets `/api/*` | **Aprovado** |
| Retorno em JSON | `ApiServlet` e respostas dos endpoints | **Aprovado** |
| React consumindo backend | API disponível para frontend desacoplado | **Aprovado** |
| Comunicação assíncrona | Endpoints próprios para fetch/Bruno | **Aprovado** |
| Testes de API com Bruno | Collection `bruno/vetcare-api` | **Aprovado** |
| Cobertura GET | Perfil, dashboard, pets, consultas, tutores, veterinários | **Aprovado** |
| Cobertura POST | Login, logout, registro, perfil, pets, consultas | **Aprovado** |
| Cobertura PUT | Pets, consultas, tutores, veterinários | **Aprovado** |
| Cobertura DELETE | Pets, consultas, tutores, veterinários | **Aprovado** |
| Controle de sessão | Rotas privadas exigem autenticação | **Aprovado** |
| Controle de perfil | Regras para tutor/veterinário | **Aprovado** |

---

# 13. Pontos positivos identificados

- A API foi criada em endpoints `/api/*`, separando backend e frontend.
- Os Servlets retornam JSON em vez de HTML/JSP.
- O login foi adaptado para funcionar com frontend React.
- A sessão HTTP foi mantida como mecanismo de autenticação.
- O filtro de API retorna erro JSON, evitando redirecionamento para páginas HTML.
- Os endpoints principais do domínio foram contemplados.
- Há validação de dados de entrada.
- Há sanitização de campos textuais.
- Há tratamento de recurso inexistente com `404`.
- Há bloqueio de acesso indevido com `403`.
- Há bloqueio de métodos que não devem criar entidades diretamente.
- A collection Bruno cobre os principais fluxos da API.
- A base `/clinica` foi identificada e aplicada corretamente nos testes locais.

---

# 14. Pontos de atenção não bloqueantes

Os seguintes pontos não bloqueiam o PR #42, mas devem ser observados nos PRs seguintes:

| Ponto | Tratamento |
|---|---|
| CSRF específico para API | Será validado no PR #43 |
| Ajustes finos de integração visual React/API | Serão validados no PR #44 |
| Deploy do frontend | Será validado no PR #45 |
| IDs dinâmicos no Bruno | Devem ser copiados manualmente após criação do recurso |
| Ambiente local pode variar | Context path local validado como `/clinica` |

---

# 15. Conclusão geral

O PR #42 está **APROVADO** na validação de QA.

A branch `feat/integracao-react-back` cumpre o objetivo de adaptar o backend Java Servlet para funcionar como API JSON consumível pelo frontend React.

Todos os grupos de endpoints avaliados cumprem seus papéis dentro da aplicação:

- autenticação;
- encerramento de sessão;
- registro;
- perfil;
- dashboard;
- pets;
- consultas;
- tutores;
- veterinários.

A API possui cobertura suficiente para o trabalho, incluindo métodos GET, POST, PUT e DELETE, além de autenticação por sessão, validação de dados, controle de perfil e respostas JSON padronizadas.

**Resultado final do PR #42:** **APROVADO**

---

# 16. Resumo executivo para comentário no PR

```md
## QA Detalhado - PR #42

Validação concluída para os endpoints da API Java Servlet adicionados no PR #42.

Resultado: **APROVADO**

Foram analisados e documentados os endpoints de:

- Auth: `/api/login`, `/api/logout`
- Registro: `/api/registro`
- Perfil: `/api/perfil`
- Dashboard: `/api/dashboard`
- Pets: `/api/pets`, `/api/pets/{id}`
- Consultas: `/api/consultas`, `/api/consultas/{id}`
- Tutores: `/api/tutores`, `/api/tutores/{id}`
- Veterinários: `/api/veterinarios`, `/api/veterinarios/{id}`

A validação cobre GET, POST, PUT e DELETE, com base local em:

`http://localhost:8080/clinica`

Todos os endpoints cumprem o papel esperado no escopo do PR #42. Os endpoints que retornam `405 Method Not Allowed`, como `POST /api/tutores` e `POST /api/veterinarios`, também foram considerados corretos, pois o cadastro dessas entidades deve ocorrer pelo fluxo centralizado de `/api/registro`.

A collection Bruno em `bruno/vetcare-api/` cobre os principais fluxos da API e atende ao requisito de testes de API do enunciado.
```
