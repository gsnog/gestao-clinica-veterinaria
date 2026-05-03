package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/veterinarios")
public class VeterinarioServlet extends HttpServlet {

    private VeterinarioDAO veterinarioDao = new VeterinarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if (acao != null && acao.equals("editar")) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            Veterinario vet = veterinarioDao.buscarPorId(id);
            if (vet == null) {
                response.sendRedirect(request.getContextPath() + "/veterinarios");
                return;
            }
            request.setAttribute("veterinario", vet);
            request.getRequestDispatcher("/form-veterinario.jsp").forward(request, response);

        } else {
            List<Veterinario> lista = veterinarioDao.listarTodos();
            request.setAttribute("listaDeVeterinarios", lista);
            request.getRequestDispatcher("/lista-veterinarios.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            veterinarioDao.deletar(id);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Pega os dados do formulário
        String nome = request.getParameter("nomeVet");
        String crmv = request.getParameter("crmvVet");
        String especialidade = request.getParameter("especialidadeVet");

        if (acao != null && acao.equals("atualizar")) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            Veterinario vet = new Veterinario();
            vet.setId(id);
            vet.setNome(nome);
            vet.setCrmv(crmv);
            vet.setEspecialidade(especialidade);

            veterinarioDao.atualizar(vet);
        }

        response.sendRedirect(request.getContextPath() + "/veterinarios");
    }
}