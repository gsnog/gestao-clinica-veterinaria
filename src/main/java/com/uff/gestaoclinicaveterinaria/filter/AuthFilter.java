package com.uff.gestaoclinicaveterinaria.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
    "/dashboard",
    "/perfil",
        "/consultas",
        "/pets",
        "/tutores",
        "/veterinarios"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        String uri = request.getRequestURI();

        if (uri.endsWith("/dashboard") || uri.endsWith("/veterinarios") || uri.endsWith("/tutores")) {
            if (!"VETERINARIO".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }
        }

        if (uri.endsWith("/consultas") || uri.endsWith("/pets") || uri.endsWith("/perfil")) {
            if (!"TUTOR".equals(role) && !"VETERINARIO".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}