package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;
import java.util.Set;

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
 * Valida o token CSRF em todas as requisições POST, exceto /login e /registro
 * (que são formulários pré-autenticação e não possuem token de sessão ainda).
 */
@WebFilter(urlPatterns = {"/*"})
public class CsrfFilter implements Filter {

    private static final Set<String> ROTAS_PUBLICAS = Set.of("/login", "/registro");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        if (session != null) {
            request.setAttribute("csrfToken", CsrfUtil.obterToken(session));
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String contextPath = request.getContextPath();
            String uri = request.getRequestURI();
            String path = uri.startsWith(contextPath)
                    ? uri.substring(contextPath.length())
                    : uri;

            if (!ROTAS_PUBLICAS.contains(path)) {
                // Se não há sessão, AuthFilter já redirecionará; deixar passar.
                if (session != null) {
                    String tokenRecebido = request.getParameter("_csrf");
                    if (!CsrfUtil.validarToken(session, tokenRecebido)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                "Requisição bloqueada: token CSRF inválido ou ausente.");
                        return;
                    }
                }
            }
        }

        chain.doFilter(req, res);
    }
}
