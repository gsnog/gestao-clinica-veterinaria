package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAO;
import com.uff.gestaoclinicaveterinaria.dto.TutorRequestDTO;
import com.uff.gestaoclinicaveterinaria.model.Consulta;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/consultas")
public class ConsultaServlet extends HttpServlet {

    private ConsultaDAO consultaDAO = new ConsultaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String acao = request.getParameter("acao");

        if (acao != null && acao.equals("buscarPorPet")) {
            Long petId = Long.parseLong(request.getParameter("petId"));
            List<Consulta> consultaPorPet = consultaDAO.buscarPorPet(petId);
            request.setAttribute("listaDeConsultas", consultaPorPet);
            request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);
        } else if(acao != null && acao.equals("buscarPorData")){
            Long vetId = Long.parseLong(request.getParameter("veterinarioId"));
            LocalDate dataConsulta = LocalDate.parse(request.getParameter("dataConsulta"));
            List<Consulta> consultaPorData = consultaDAO.buscarPorData(vetId, dataConsulta);
            request.setAttribute("listaDeConsultas", consultaPorData);
            request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);
        } else if (acao != null && acao.equals("deletar")) {
            Long id = Long.parseLong(request.getParameter("id"));
            consultaDAO.deletar(id);
            response.sendRedirect(request.getContextPath() + "/consultas");
        }else if (acao != null && acao.equals("editar")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Consulta consulta = consultaDAO.buscarPorId(id);
            request.setAttribute("consulta", consulta);
            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);
        } else {
        List<Consulta> lista = consultaDAO.listarTodos();
        request.setAttribute("listaDeConsultas", lista);
        request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String acao = request.getParameter("acao");

        LocalDateTime dataConsulta = LocalDateTime.parse(request.getParameter("dataConsulta"));
        String motivo = request.getParameter("motivo");
        Long petId = Long.parseLong(request.getParameter("petId"));
        Pet pet = new Pet();
        pet.setId(petId);
        Long vetId = Long.parseLong(request.getParameter("vetId"));
        Veterinario vet = new Veterinario();
        vet.setId(vetId);
        if(acao != null && acao.equals("atualizar")){
            Long id = Long.parseLong(request.getParameter("id"));
            Consulta consulta = new Consulta();
            consulta.setId(id);
            consulta.setDataConsulta(dataConsulta);
            consulta.setMotivo(motivo);
            consulta.setPet(pet);
            consulta.setVeterinario(vet);
            consultaDAO.atualizar(consulta);
        }
        else{
            Consulta consultaNova = new Consulta();
            consultaNova.setDataConsulta(dataConsulta);
            consultaNova.setMotivo(motivo);
            consultaNova.setPet(pet);
            consultaNova.setVeterinario(vet);
            consultaDAO.salvar(consultaNova);
        }
        response.sendRedirect(request.getContextPath() + "/consultas");
    }
}
