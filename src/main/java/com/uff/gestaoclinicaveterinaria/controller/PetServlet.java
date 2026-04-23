package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/pets")
public class PetServlet extends HttpServlet {

    private PetDAO petDao = new PetDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if (acao != null && acao.equals("deletar")) {
            Long id = Long.parseLong(request.getParameter("id"));
            petDao.deletar(id);
            response.sendRedirect(request.getContextPath() + "/pets");

        } else if (acao != null && acao.equals("editar")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Pet pet = petDao.buscarPorId(id);
            request.setAttribute("pet", pet);
            request.getRequestDispatcher("/form-pet.jsp").forward(request, response);
        } else {
            List<Pet> lista = petDao.listarTodos();
            request.setAttribute("listaDePets", lista);
            request.getRequestDispatcher("/lista-pets.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        String nome = request.getParameter("nomePet");
        String raca = request.getParameter("racaPet");
        LocalDate dataNascimento = LocalDate.parse(request.getParameter("dataNascimentoPet"));
        Long tutorId = Long.parseLong(request.getParameter("tutorId"));
        Tutor tutorPet = new Tutor();
        tutorPet.setId(tutorId);

        if (acao != null && acao.equals("atualizar")) {
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