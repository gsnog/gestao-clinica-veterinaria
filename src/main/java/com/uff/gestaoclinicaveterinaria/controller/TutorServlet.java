package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dto.TutorRequestDTO;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/tutores")
public class    TutorServlet extends HttpServlet {

    private TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if(acao != null && acao.equals("deletar")){
            Long id = Long.parseLong(request.getParameter("id"));
            tutorDAO.deletar(id);
            response.sendRedirect(request.getContextPath() + "/tutores");
        }else if(acao != null && acao.equals("editar")){
            Long id = Long.parseLong(request.getParameter("id"));
            Tutor tutorEditado = tutorDAO.buscarPorId(id);
            request.setAttribute("tutor", tutorEditado);
            request.getRequestDispatcher("/form-tutor.jsp").forward(request, response);
        }else{
            List<Tutor> listaDeTutores = tutorDAO.listarTodos();
            request.setAttribute("listaTutores", listaDeTutores);
            request.getRequestDispatcher("/lista-tutores.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String acao = request.getParameter("acao");

        String nome = request.getParameter("nomeTutor");
        String telefone = request.getParameter("telefoneTutor");

        if(acao != null && acao.equals("editar")){
            Long id = Long.parseLong(request.getParameter("id"));
            Tutor tutorAtualizado = new Tutor();
            tutorAtualizado.setId(id);
            tutorAtualizado.setNome(nome);
            tutorAtualizado.setTelefone(telefone);
            tutorDAO.atualizar(tutorAtualizado);
        }else{
            TutorRequestDTO dto = new TutorRequestDTO(nome, telefone);
            Tutor novoTutor = new Tutor(dto.nome(), dto.telefone());
            tutorDAO.salvar(novoTutor);
        }
        response.sendRedirect(request.getContextPath() + "/tutores");
    }
}
