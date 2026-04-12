# Guia completo para a Vitoria (Front-end)

**Status:** Pronto para entrega para implementacao do front (HTML5 + CSS3 + JavaScript com JSP).

Este documento descreve **exatamente** como o back-end atual funciona (Servlet + JSP + DAO) para voce conseguir montar telas e formularios sem erro de integracao.

## 1) Visao geral tecnica

- Stack atual do back-end: Java 21, Jakarta Servlet 6.1, Maven WAR.
- Nao existe API REST/JSON neste momento. O fluxo e server-side com JSP:
  - Front envia formulario (`POST` com `application/x-www-form-urlencoded`).
  - Servlet processa e faz `sendRedirect(...)` ou `forward(...)` para JSP.
- Endpoints publicados via `@WebServlet`:
  - `/tutores`
  - `/pets`
  - `/veterinarios`
  - `/consultas`
  - `/hello-servlet` (somente exemplo)
- O `contextPath` depende do servidor (exemplo comum: `/gestao-clinica-veterinaria`).

## 2) Padrrao de navegacao e contrato dos Servlets

Todos os modulos usam o parametro `acao` para definir comportamento.

### 2.1 Tutor (`/tutores`)

#### GET `/tutores`

- `acao=deletar` + `id` -> deleta e redireciona para `/tutores`.
- `acao=editar` + `id` -> busca tutor e encaminha para `/form-tutor.jsp`.
  - Attribute setado: `tutor`
- sem `acao` -> lista todos e encaminha para `/lista-tutores.jsp`.
  - Attribute setado: `listaTutores`

#### POST `/tutores`

Campos esperados no formulario:

- `nomeTutor` (String)
- `telefoneTutor` (String)
- `acao` (opcional)
- `id` (obrigatorio quando atualizar)

Comportamento:

- `acao=editar` -> atualiza tutor pelo `id`.
- qualquer outro caso -> cria tutor novo.
- no final sempre redireciona para `/tutores`.

> Atencao: neste servlet o valor de atualizacao e `editar` (nao `atualizar`).

---

### 2.2 Pet (`/pets`)

#### GET `/pets`

- `acao=deletar` + `id` -> deleta e redireciona para `/pets`.
- `acao=editar` + `id` -> busca pet e encaminha para `/form-pet.jsp`.
  - Attribute setado: `pet`
- sem `acao` -> lista pets e encaminha para `/lista-pets.jsp`.
  - Attribute setado: `listaDePets`

#### POST `/pets`

Campos esperados no formulario:

- `nomePet` (String)
- `racaPet` (String)
- `dataNascimentoPet` (String -> `LocalDate.parse`)
- `tutorId` (Long)
- `acao` (opcional)
- `id` (obrigatorio quando atualizar)

Formato obrigatorio de data:

- `dataNascimentoPet`: `yyyy-MM-dd`
- Exemplo: `2023-09-17`

Comportamento:

- `acao=atualizar` -> atualiza pet pelo `id`.
- qualquer outro caso -> cria pet novo.
- redireciona para `/pets`.

---

### 2.3 Veterinario (`/veterinarios`)

#### GET `/veterinarios`

- `acao=deletar` + `id` -> deleta e redireciona para `/veterinarios`.
- `acao=editar` + `id` -> busca vet e encaminha para `/form-veterinario.jsp`.
  - Attribute setado: `veterinario`
- sem `acao` -> lista veterinarios e encaminha para `/lista-veterinarios.jsp`.
  - Attribute setado: `listaDeVeterinarios`

#### POST `/veterinarios`

Campos esperados no formulario:

- `nomeVet` (String)
- `crmvVet` (String)
- `especialidadeVet` (String)
- `acao` (opcional)
- `id` (obrigatorio quando atualizar)

Comportamento:

- `acao=atualizar` -> atualiza veterinario pelo `id`.
- qualquer outro caso -> cria veterinario novo.
- redireciona para `/veterinarios`.

---

### 2.4 Consulta (`/consultas`)

#### GET `/consultas`

- `acao=buscarPorPet` + `petId` -> lista consultas do pet e encaminha para `/lista-consultas.jsp`.
  - Attribute setado: `listaDeConsultas`
- `acao=buscarPorData` + `veterinarioId` + `dataConsulta` -> lista consultas por veterinario/data.
  - `dataConsulta` aqui e `LocalDate` (`yyyy-MM-dd`)
  - Attribute setado: `listaDeConsultas`
- `acao=deletar` + `id` -> deleta e redireciona para `/consultas`.
- `acao=editar` + `id` -> busca consulta e encaminha para `/form-consulta.jsp`.
  - Attribute setado: `consulta`
- sem `acao` -> lista tudo em `/lista-consultas.jsp`.
  - Attribute setado: `listaDeConsultas`

#### POST `/consultas`

Campos esperados no formulario:

- `dataConsulta` (String -> `LocalDateTime.parse`)
- `motivo` (String)
- `petId` (Long)
- `vetId` (Long)
- `acao` (opcional)
- `id` (obrigatorio quando atualizar)

Formato obrigatorio de data/hora:

- `dataConsulta`: `yyyy-MM-dd'T'HH:mm:ss` (ISO LocalDateTime)
- Exemplo: `2026-04-11T14:30:00`

Comportamento:

- `acao=atualizar` -> atualiza consulta pelo `id`.
- qualquer outro caso -> cria consulta nova.
- redireciona para `/consultas`.

> Atencao importante de nomenclatura:
>
> - Em `GET buscarPorData` o parametro e `veterinarioId`.
> - Em `POST` de consulta o parametro e `vetId`.
> - Para nao quebrar, o front precisa respeitar exatamente esses nomes atuais.

## 3) Nomes exatos de atributos enviados para JSP

Quando voce montar JSPs/listagens, use os mesmos nomes:

- Tutor:
  - formulario de edicao: `tutor`
  - listagem: `listaTutores`
- Pet:
  - formulario de edicao: `pet`
  - listagem: `listaDePets`
- Veterinario:
  - formulario de edicao: `veterinario`
  - listagem: `listaDeVeterinarios`
- Consulta:
  - formulario de edicao: `consulta`
  - listagem/filtros: `listaDeConsultas`

## 4) Campos por entidade (dominio)

### Tutor
- `id: Long`
- `nome: String`
- `telefone: String`

### Pet
- `id: Long`
- `nome: String`
- `raca: String`
- `dataNascimento: LocalDate`
- `tutor.id: Long`

### Veterinario
- `id: Long`
- `nome: String`
- `crmv: String`
- `especialidade: String`

### Consulta
- `id: Long`
- `dataConsulta: LocalDateTime`
- `motivo: String`
- `pet.id: Long`
- `veterinario.id: Long`

## 5) JSPs que o back-end espera (ainda nao existem no projeto)

As seguintes paginas sao referenciadas pelos servlets e precisam existir:

- `/form-tutor.jsp`
- `/lista-tutores.jsp`
- `/form-pet.jsp`
- `/lista-pets.jsp`
- `/form-veterinario.jsp`
- `/lista-veterinarios.jsp`
- `/form-consulta.jsp`
- `/lista-consultas.jsp`

No momento, o projeto so possui `index.jsp`.

## 6) Fluxo sugerido de implementacao do front

1. Criar primeiro as listagens (`lista-*.jsp`) com botoes de editar/deletar usando query string.
2. Criar formularios (`form-*.jsp`) com `method="post"` e campos com os nomes exatos acima.
3. Em edicao, enviar `id` + `acao` correto de cada modulo.
4. Para datas, usar `input type="date"` e `input type="datetime-local"` com adaptacao para formato aceito.
5. Garantir links sempre com `${pageContext.request.contextPath}` para evitar erro de caminho.

## 7) Alertas de integracao (pontos que podem quebrar)

- `TutorServlet` atualiza com `acao=editar`, diferente dos outros (`atualizar`).
- `ConsultaServlet` usa `veterinarioId` no filtro GET e `vetId` no POST.
- Nao ha tratamento de validacao/erro amigavel: parse invalido de `Long`/data pode gerar excecao.
- Nao ha API JSON; todo contrato e por formulario/JSP.

## 8) Mapa rapido de exemplos de requests

### Criar tutor
`POST /tutores`
- `nomeTutor=Ana`
- `telefoneTutor=21999999999`

### Atualizar pet
`POST /pets`
- `acao=atualizar`
- `id=10`
- `nomePet=Rex`
- `racaPet=Labrador`
- `dataNascimentoPet=2021-05-20`
- `tutorId=3`

### Filtrar consulta por data/veterinario
`GET /consultas?acao=buscarPorData&veterinarioId=2&dataConsulta=2026-04-11`

### Criar consulta
`POST /consultas`
- `dataConsulta=2026-04-11T14:30:00`
- `motivo=Vacina`
- `petId=10`
- `vetId=2`

## 9) Referencias de codigo usadas

- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/TutorServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/PetServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/VeterinarioServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/controller/ConsultaServlet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Tutor.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Pet.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Veterinario.java`
- `src/main/java/com/uff/gestaoclinicaveterinaria/model/Consulta.java`

## 10) Checklist pratico - Vitoria

- Criar `lista-tutores.jsp`, `lista-pets.jsp`, `lista-veterinarios.jsp` e `lista-consultas.jsp`.
- Criar `form-tutor.jsp`, `form-pet.jsp`, `form-veterinario.jsp` e `form-consulta.jsp`.
- Garantir que os `name` dos campos sejam exatamente os do contrato (`nomeTutor`, `telefoneTutor`, `nomePet`, `vetId`, etc.).
- Implementar botoes/links de editar e deletar usando `acao` e `id` na query string.
- Em cada form de edicao, enviar `id` + `acao` correto do modulo (`editar` em tutor, `atualizar` nos demais).
- Garantir datas no formato esperado pelo backend (`yyyy-MM-dd` para pet e `yyyy-MM-dd'T'HH:mm:ss` para consulta).
- Usar `${pageContext.request.contextPath}` em todos os `href` e `action`.
- Validar no navegador os fluxos completos: criar, listar, editar, deletar e filtrar consultas.

## 11) Criterios objetivos de aceite - Vitoria

- As 8 JSPs referenciadas pelos servlets existem e abrem sem erro de rota.
- Criacao e edicao de `tutor`, `pet`, `veterinario` e `consulta` funcionam com redirect para listagem.
- Exclusao funciona nos 4 modulos com retorno para tela de lista.
- Filtro `buscarPorPet` e filtro `buscarPorData` em consultas retornam resultados esperados.
- Nao ocorre erro por nome de parametro incorreto (`veterinarioId` no GET de filtro e `vetId` no POST).
- Todos os links/formularios funcionam mesmo com `contextPath` diferente do ambiente local.

