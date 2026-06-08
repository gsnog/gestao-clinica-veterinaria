package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Libera o acesso de /api/* para o front React, que roda em outra origem e
 * envia cookies de sessão via fetch(..., {credentials: 'include'}). Como a
 * requisição inclui credenciais, o cabeçalho Allow-Origin precisa ecoar uma
 * origem explícita (não pode ser "*").
 *
 * Registrado em web.xml (não via @WebFilter) como o primeiro filter-mapping,
 * para garantir que rode antes do ApiAuthFilter — caso contrário, requisições
 * de preflight (OPTIONS) e respostas de erro (401/403) sairiam sem os
 * cabeçalhos CORS e o navegador bloquearia a leitura no front.
 */
public class CorsFilter implements Filter {

    private static final String ORIGEM_PERMITIDA =
            System.getenv("FRONTEND_ORIGIN") != null
                    ? System.getenv("FRONTEND_ORIGIN")
                    : "http://localhost:5173";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", ORIGEM_PERMITIDA);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Vary", "Origin");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
}
