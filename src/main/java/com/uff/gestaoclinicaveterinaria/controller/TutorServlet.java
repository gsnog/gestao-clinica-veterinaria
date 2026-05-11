package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.SearchFilterUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tutores")
public class    TutorServlet extends HttpServlet {

    private TutorDAO tutorDAO = new TutorDAOImpl();

    private Long parseLongPositivo(String valor) {
        try {
            long numero = Long.parseLong(valor);
            return numero > 0 ? numero : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void encaminharErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("erro", mensagem);
        request.getRequestDispatcher("/WEB-INF/views/acessonegado.jsp").forward(request, response);
    }

    private List<Tutor> filtrarTutores(List<Tutor> tutores, String busca) {
        String buscaNormalizada = SearchFilterUtil.normalize(busca);
        if (buscaNormalizada.isEmpty()) {
            return tutores;
        }

        List<Tutor> filtrados = new ArrayList<>();
        for (Tutor tutor : tutores) {
            if (SearchFilterUtil.startsWithNormalized(tutor.getNome(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(tutor.getTelefone(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(String.valueOf(tutor.getId()), buscaNormalizada)) {
                filtrados.add(tutor);
            }
        }
        return filtrados;
    }

    private void encaminharListaTutores(HttpServletRequest request,
                                        HttpServletResponse response,
                                        List<Tutor> lista) throws ServletException, IOException {
        request.setAttribute("listaTutores", lista);
        request.setAttribute("buscaParam", request.getParameter("busca") != null ? request.getParameter("busca") : "");
        request.getRequestDispatcher("/lista-tutores.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if(acao != null && acao.equals("editar")){
            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                encaminharErro(request, response, "Requisição inválida.");
                return;
            }
            Tutor tutorEditado = tutorDAO.buscarPorId(id);
            if (tutorEditado == null) {
                response.sendRedirect(request.getContextPath() + "/tutores");
                return;
            }
            request.setAttribute("tutor", tutorEditado);
            request.getRequestDispatcher("/form-tutor.jsp").forward(request, response);
        }else{
            List<Tutor> listaDeTutores = tutorDAO.listarTodos();
            listaDeTutores = filtrarTutores(listaDeTutores, request.getParameter("busca"));
            encaminharListaTutores(request, response, listaDeTutores);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                encaminharErro(request, response, "Requisição inválida.");
                return;
            }
            tutorDAO.deletar(id);
            response.sendRedirect(request.getContextPath() + "/tutores");
            return;
        }

        String nome = request.getParameter("nomeTutor");
        String telefone = request.getParameter("telefoneTutor");

        if (nome == null || nome.isBlank() || telefone == null || telefone.isBlank()) {
            encaminharErro(request, response, "Dados do tutor inválidos.");
            return;
        }

        if(acao != null && acao.equals("editar")){
            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                encaminharErro(request, response, "Requisição inválida.");
                return;
            }
            Tutor tutorAtualizado = new Tutor();
            tutorAtualizado.setId(id);
            tutorAtualizado.setNome(nome);
            tutorAtualizado.setTelefone(telefone);
            tutorDAO.atualizar(tutorAtualizado);
        }
        response.sendRedirect(request.getContextPath() + "/tutores");
    }
}
