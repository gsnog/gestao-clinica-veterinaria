# VetCare API - Testes Bruno CSRF - PR #43

## Base URL

```txt
http://localhost:8080/clinica
```

## Fluxo

1. Executar `01 CSRF/01 Obter Token CSRF`.
2. Copiar o valor `csrfToken`.
3. Definir a variável `csrfToken` no Bruno.
4. Executar login e mutações com `X-CSRF-Token`.

O bloqueio `403 Forbidden` em request mutável sem token é esperado e aprovado.
