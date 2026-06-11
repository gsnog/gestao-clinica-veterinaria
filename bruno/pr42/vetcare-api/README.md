# VetCare API - Collection Bruno Corrigida

## Objetivo

Collection Bruno para validação da API do PR #42.

Esta versão usa URLs completas fixas nos requests para evitar o erro **Invalid URL** causado por ambiente/variável não selecionada no Bruno.

## Base URL usada

```txt
http://localhost:8080/clinica
```

## Como abrir no Bruno

1. Abra o Bruno.
2. Clique em **Open Collection**.
3. Selecione a pasta:

```txt
bruno/vetcare-api
```

4. Abra os requests pelas pastas.
5. Execute primeiro um request de login.

## Ordem recomendada

### Fluxo tutor

1. `01 Auth/01 Login Tutor`
2. `02 Perfil/01 Get Perfil`
3. `04 Pets/01 Listar Pets`
4. `04 Pets/02 Criar Pet`
5. `04 Pets/03 Atualizar Pet`
6. `04 Pets/04 Excluir Pet`

### Fluxo veterinário

1. `01 Auth/02 Login Veterinario`
2. `03 Dashboard/01 Get Dashboard`
3. `05 Consultas/01 Listar Consultas`
4. `05 Consultas/02 Criar Consulta`
5. `05 Consultas/03 Atualizar Consulta`
6. `05 Consultas/04 Excluir Consulta`
7. `06 Tutores/01 Listar Tutores`
8. `07 Veterinarios/01 Listar Veterinarios`

## Atenção

Se a aplicação estiver rodando com outro contexto no Tomcat, por exemplo:

```txt
http://localhost:8080/gestao-clinica-veterinaria-1.0-SNAPSHOT
```

troque a URL dos requests para esse contexto.

## Resultado esperado

Os requests devem deixar de apresentar **Invalid URL**.

Caso o servidor não esteja rodando, o erro esperado passa a ser conexão recusada ou 404, não Invalid URL.
