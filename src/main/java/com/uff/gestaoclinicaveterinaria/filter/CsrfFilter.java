package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uff.gestaoclinicaveterinaria.util.CsrfUtil;

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
 * Valida o token CSRF em formulários JSP legados e nas mutações da API JSON.
 * A API usa o header X-CSRF-Token; formulários antigos continuam usando _csrf.
 */
@WebFilter(urlPatterns = {"/*"})
public class CsrfFilter implements Filter {

    private static final Set<String> ROTAS_PUBLICAS = Set.of("/login", "/registro");
    private static final String PREFIXO_API = "/api/";
    private static final String CSRF_HEADER = "X-CSRF-Token";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        if (session != null) {
            request.setAttribute("csrfToken", CsrfUtil.obterToken(session));
        }

        String path = extrairPath(request);

        if (path.startsWith(PREFIXO_API) && metodoMutante(request)) {
            String tokenRecebido = request.getHeader(CSRF_HEADER);
            if (!CsrfUtil.validarToken(session, tokenRecebido)) {
                responderJsonProibido(response);
                return;
            }
        } else if ("POST".equalsIgnoreCase(request.getMethod()) && !ROTAS_PUBLICAS.contains(path)) {
            // Se não há sessão, AuthFilter das telas legadas redirecionará; deixar passar.
            if (session != null) {
                String tokenRecebido = request.getParameter("_csrf");
                if (!CsrfUtil.validarToken(session, tokenRecebido)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Requisição bloqueada: token CSRF inválido ou ausente.");
                    return;
                }
            }
        }

        chain.doFilter(req, res);
    }

    private String extrairPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        return uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;
    }

    private boolean metodoMutante(HttpServletRequest request) {
        String metodo = request.getMethod();
        return "POST".equalsIgnoreCase(metodo)
                || "PUT".equalsIgnoreCase(metodo)
                || "DELETE".equalsIgnoreCase(metodo);
    }

    private void responderJsonProibido(HttpServletResponse response) throws IOException {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("success", false);
        corpo.put("message", "Requisição bloqueada: token CSRF inválido ou ausente.");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(response.getWriter(), corpo);
    }
}
