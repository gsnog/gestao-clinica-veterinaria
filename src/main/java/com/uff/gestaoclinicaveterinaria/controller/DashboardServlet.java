package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAO;
import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.DashboardDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.DashboardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private DashboardDAO dashboardDAO = new DashboardDAO();
    private PetDAO petDAO = new PetDAOImpl();
    private ConsultaDAO consultaDAO = new ConsultaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        DashboardDTO stats = new DashboardDTO();

        if ("TUTOR".equals(role)) {
            Long idLogado = (Long) session.getAttribute("usuarioId");
            stats.setTotalPets(petDAO.buscarPorTutor(idLogado).size());
            stats.setTotalConsultas(consultaDAO.buscarPorTutor(idLogado).size());
            stats.setTotalTutores(0);
            stats.setTotalVeterinarios(0);
        } else {
            stats = dashboardDAO.obterEstatisticas();
        }

        request.setAttribute("estatisticas", stats);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}