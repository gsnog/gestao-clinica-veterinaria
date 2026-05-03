package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Adiciona headers de no-cache para páginas dinâmicas protegidas
 * evitando que o navegador cache dados sensíveis de usuário logado.
 */
@WebFilter(urlPatterns = {
    "/dashboard",
    "/perfil",
    "/consultas",
    "/pets",
    "/tutores",
    "/veterinarios"
})
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        // Headers de no-cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        // Garante UTF-8 em toda a resposta
        response.setContentType("text/html; charset=UTF-8");

        chain.doFilter(req, res);
    }
}
