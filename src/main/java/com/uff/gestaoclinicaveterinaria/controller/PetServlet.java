package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pets")
public class PetServlet extends HttpServlet {

    private PetDAO petDao = new PetDAOImpl();
    private TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        Long idLogado = (Long) session.getAttribute("usuarioId");
        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            petDao.deletar(id);
            response.sendRedirect(request.getContextPath() + "/pets");

        } else if ("editar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Pet pet = petDao.buscarPorId(id);

            List<Tutor> tutores = new ArrayList<>();
            if ("TUTOR".equals(role)) {
                tutores.add(tutorDAO.buscarPorId(idLogado));
            } else {
                tutores = tutorDAO.listarTodos();
            }

            request.setAttribute("listaTutores", tutores);
            request.setAttribute("pet", pet);
            request.getRequestDispatcher("/form-pet.jsp").forward(request, response);

        } else if ("novo".equals(acao)) {

            List<Tutor> tutores = new ArrayList<>();
            if ("TUTOR".equals(role)) {
                tutores.add(tutorDAO.buscarPorId(idLogado));
            } else {
                tutores = tutorDAO.listarTodos();
            }

            request.setAttribute("listaTutores", tutores);
            request.getRequestDispatcher("/form-pet.jsp").forward(request, response);

        } else {
            List<Pet> lista;
            if ("TUTOR".equals(role)) {
                lista = petDao.buscarPorTutor(idLogado);
            } else {
                lista = petDao.listarTodos();
            }

            request.setAttribute("listaDePets", lista);
            request.getRequestDispatcher("/lista-pets.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        Long idLogado = (Long) session.getAttribute("usuarioId");
        String acao = request.getParameter("acao");

        String nome = request.getParameter("nomePet");
        String raca = request.getParameter("racaPet");
        LocalDate dataNascimento = LocalDate.parse(request.getParameter("dataNascimentoPet"));

        Long tutorId;
        if ("TUTOR".equals(role)) {
            tutorId = idLogado;
        } else {
            tutorId = Long.parseLong(request.getParameter("tutorId"));
        }

        Tutor tutorPet = new Tutor();
        tutorPet.setId(tutorId);

        if ("atualizar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));

            Pet pet = new Pet();
            pet.setId(id);
            pet.setNome(nome);
            pet.setRaca(raca);
            pet.setDataNascimento(dataNascimento);
            pet.setTutor(tutorPet);

            petDao.atualizar(pet);

        } else {
            Pet novoPet = new Pet();
            novoPet.setNome(nome);
            novoPet.setRaca(raca);
            novoPet.setDataNascimento(dataNascimento);
            novoPet.setTutor(tutorPet);

            petDao.salvar(novoPet);
        }

        response.sendRedirect(request.getContextPath() + "/pets");
    }
}