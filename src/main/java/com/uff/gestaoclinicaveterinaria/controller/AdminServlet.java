package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.DashboardDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.DashboardDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Defesa em profundidade: além do AuthFilter, o servlet confere o papel.
        if (session == null || !"ADMIN".equals(session.getAttribute("usuarioRole"))) {
            response.sendRedirect(request.getContextPath() + "/acesso-negado");
            return;
        }

        DashboardDTO estatisticas = dashboardDAO.obterEstatisticas();

        request.setAttribute("estatisticas", estatisticas);
        request.setAttribute("totalTutores", usuarioDAO.contarPorRole("TUTOR"));
        request.setAttribute("totalVeterinarios", usuarioDAO.contarPorRole("VETERINARIO"));
        request.setAttribute("totalAdmins", usuarioDAO.contarPorRole("ADMIN"));

        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }
}
