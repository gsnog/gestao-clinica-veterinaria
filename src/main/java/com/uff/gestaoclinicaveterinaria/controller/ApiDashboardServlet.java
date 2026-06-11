package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.DashboardDAO;
import com.uff.gestaoclinicaveterinaria.dto.DashboardDTO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/dashboard")
public class ApiDashboardServlet extends ApiServlet {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"VETERINARIO".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a veterinários.");
            return;
        }

        DashboardDTO estatisticas = dashboardDAO.obterEstatisticas();
        responderJson(response, HttpServletResponse.SC_OK, corpoJson("estatisticas", estatisticas));
    }
}
