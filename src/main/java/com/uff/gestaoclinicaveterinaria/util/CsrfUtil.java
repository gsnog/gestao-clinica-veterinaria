package com.uff.gestaoclinicaveterinaria.util;

import java.util.UUID;

import jakarta.servlet.http.HttpSession;

public final class CsrfUtil {

    public static final String TOKEN_ATTR = "_csrf_token";

    private CsrfUtil() {}

    /** Retorna o token CSRF da sessão, gerando um novo se ainda não existir. */
    public static String obterToken(HttpSession session) {
        String token = (String) session.getAttribute(TOKEN_ATTR);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(TOKEN_ATTR, token);
        }
        return token;
    }

    /** Retorna true somente quando o token recebido é igual ao da sessão. */
    public static boolean validarToken(HttpSession session, String tokenRecebido) {
        if (session == null || tokenRecebido == null || tokenRecebido.isBlank()) {
            return false;
        }
        String tokenSessao = (String) session.getAttribute(TOKEN_ATTR);
        return tokenRecebido.equals(tokenSessao);
    }
}
