package com.uff.gestaoclinicaveterinaria.controller;

import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAO;
import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Consulta;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/consultas")
public class ConsultaServlet extends HttpServlet {

    private ConsultaDAO consultaDAO = new ConsultaDAOImpl();
    private PetDAO petDAO = new PetDAOImpl();
    private VeterinarioDAO vetDAO = new VeterinarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("buscarPorPet".equals(acao)) {

            Long petId = Long.parseLong(request.getParameter("petId"));
            List<Consulta> lista = consultaDAO.buscarPorPet(petId);

            request.setAttribute("listaDeConsultas", lista);
            request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);

        } else if ("buscarPorData".equals(acao)) {

            Long vetId = Long.parseLong(request.getParameter("veterinarioId"));
            LocalDate dataConsulta = LocalDate.parse(request.getParameter("dataConsulta"));

            List<Consulta> lista = consultaDAO.buscarPorData(vetId, dataConsulta);

            request.setAttribute("listaDeConsultas", lista);
            request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);

        } else if ("deletar".equals(acao)) {

            Long id = Long.parseLong(request.getParameter("id"));
            consultaDAO.deletar(id);

            response.sendRedirect(request.getContextPath() + "/consultas");

        } else if ("editar".equals(acao)) {

            Long id = Long.parseLong(request.getParameter("id"));
            Consulta consulta = consultaDAO.buscarPorId(id);

            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();

            request.setAttribute("consulta", consulta);
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);

            request.getRequestDispatcher("/form-consulta.jsp")
                    .forward(request, response);

        } else if ("filtrar".equals(acao)) {

            String busca = request.getParameter("busca");
            String dataParam = request.getParameter("dataConsulta");

            LocalDate data = null;

            if (dataParam != null && !dataParam.isEmpty()) {
                data = LocalDate.parse(dataParam);
            }

            List<Consulta> lista = consultaDAO.filtrar(busca, data);

            request.setAttribute("listaDeConsultas", lista);

            request.getRequestDispatcher("/lista-consultas.jsp")
                    .forward(request, response);
        } else if ("novo".equals(acao)) {

            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();

            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);

            request.getRequestDispatcher("/form-consulta.jsp")
                    .forward(request, response);

        } else {

            List<Consulta> lista = consultaDAO.listarTodos();
            request.setAttribute("listaDeConsultas", lista);

            request.getRequestDispatcher("/lista-consultas.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // evita bug de acento
        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");

        LocalDateTime dataConsulta = LocalDateTime.parse(request.getParameter("dataConsulta"));
        String motivo = request.getParameter("motivo");

        Long petId = Long.parseLong(request.getParameter("petId"));
        Pet pet = new Pet();
        pet.setId(petId);

        Long vetId = Long.parseLong(request.getParameter("vetId"));
        Veterinario vet = new Veterinario();
        vet.setId(vetId);

        if ("atualizar".equals(acao)) {

            Long id = Long.parseLong(request.getParameter("id"));

            Consulta consulta = new Consulta();
            consulta.setId(id);
            consulta.setDataConsulta(dataConsulta);
            consulta.setMotivo(motivo);
            consulta.setPet(pet);
            consulta.setVeterinario(vet);

            consultaDAO.atualizar(consulta);

        } else {

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