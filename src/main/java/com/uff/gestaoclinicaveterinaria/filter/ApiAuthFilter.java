package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Garante que toda rota /api/* (exceto login) tenha sessão ativa, respondendo
 * sempre em JSON — nunca com redirect/HTML como o AuthFilter das views faz.
 * Regras de papel (TUTOR x VETERINARIO) e posse de recurso ficam por conta de
 * cada servlet, pois variam por ação dentro do mesmo endpoint.
 */
@WebFilter(urlPatterns = {"/api/*"})
public class ApiAuthFilter implements Filter {

    private static final Set<String> ROTAS_PUBLICAS = Set.of("/api/login");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;

        if (ROTAS_PUBLICAS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            responderNaoAutenticado(response);
            return;
        }

        chain.doFilter(req, res);
    }

    private void responderNaoAutenticado(HttpServletResponse response) throws IOException {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("success", false);
        corpo.put("message", "Não autenticado.");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(response.getWriter(), corpo);
    }
}
