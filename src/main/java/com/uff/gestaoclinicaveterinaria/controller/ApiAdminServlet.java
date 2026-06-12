package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.DashboardDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.DashboardDTO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Equivalente em JSON do AdminServlet (JSP): painel administrativo com os
 * totais da plataforma, exclusivo do ADMIN.
 */
@WebServlet("/api/admin/dashboard")
public class ApiAdminServlet extends ApiServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"ADMIN".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        DashboardDTO estatisticas = dashboardDAO.obterEstatisticas();

        responderJson(response, HttpServletResponse.SC_OK, corpoJson(
                "estatisticas", estatisticas,
                "totalTutores", usuarioDAO.contarPorRole("TUTOR"),
                "totalVeterinarios", usuarioDAO.contarPorRole("VETERINARIO"),
                "totalAdmins", usuarioDAO.contarPorRole("ADMIN")
        ));
    }
}
