package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/consultas")
public class ConsultaServlet extends HttpServlet {

    private static final DateTimeFormatter DATA_CONSULTA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ConsultaDAO consultaDAO = new ConsultaDAOImpl();
    private PetDAO petDAO = new PetDAOImpl();
    private VeterinarioDAO vetDAO = new VeterinarioDAOImpl();

    private Long parseLongPositivo(String valor) {
        try {
            long numero = Long.parseLong(valor);
            return numero > 0 ? numero : null;
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseData(String valor) {
        if (valor == null || !valor.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return null;
        }
        try {
            return LocalDate.parse(valor);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<Long, String> montarDatasConsultaFormatadas(List<Consulta> lista) {
        Map<Long, String> datasFormatadas = new HashMap<>();
        if (lista == null) {
            return datasFormatadas;
        }

        for (Consulta consulta : lista) {
            if (consulta == null || consulta.getId() == null || consulta.getDataConsulta() == null) {
                continue;
            }
            datasFormatadas.put(consulta.getId(), consulta.getDataConsulta().format(DATA_CONSULTA_FMT));
        }

        return datasFormatadas;
    }

    private void encaminharListaConsultas(HttpServletRequest request,
                                          HttpServletResponse response,
                                          List<Consulta> lista) throws ServletException, IOException {
        request.setAttribute("listaDeConsultas", lista);
        request.setAttribute("datasConsultaFormatadas", montarDatasConsultaFormatadas(lista));
        request.setAttribute("buscaParam", request.getParameter("busca") != null ? request.getParameter("busca") : "");
        request.setAttribute("dataFiltroParam", request.getParameter("dataConsulta") != null ? request.getParameter("dataConsulta") : "");
        request.getRequestDispatcher("/lista-consultas.jsp").forward(request, response);
    }

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

        if ("TUTOR".equals(role)
                && ("novo".equals(acao)
                || "editar".equals(acao)
                || "deletar".equals(acao)
                || "buscarPorPet".equals(acao)
                || "buscarPorData".equals(acao)
                || "filtrar".equals(acao))) {
            response.sendRedirect(request.getContextPath() + "/consultas");
            return;
        }

        if ("buscarPorPet".equals(acao)) {

            Long petId = parseLongPositivo(request.getParameter("petId"));
            if (petId == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }
            List<Consulta> lista = consultaDAO.buscarPorPet(petId);
            encaminharListaConsultas(request, response, lista);

        } else if ("buscarPorData".equals(acao)) {

            Long vetId = parseLongPositivo(request.getParameter("veterinarioId"));
            LocalDate dataConsulta = parseData(request.getParameter("dataConsulta"));
            if (vetId == null || dataConsulta == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }

            List<Consulta> lista = consultaDAO.buscarPorData(vetId, dataConsulta);
            encaminharListaConsultas(request, response, lista);

        } else if ("editar".equals(acao)) {

            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }
            Consulta consulta = consultaDAO.buscarPorId(id);
            if (consulta == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }

            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();

            request.setAttribute("consulta", consulta);
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);

            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);

        } else if ("filtrar".equals(acao)) {

            String busca = request.getParameter("busca");
            String dataParam = request.getParameter("dataConsulta");

            LocalDate data = null;

            if (dataParam != null && !dataParam.isEmpty()) {
                data = parseData(dataParam);
                if (data == null) {
                    request.setAttribute("erroFiltroData", "Data invalida. Use o formato AAAA-MM-DD.");
                }
            }

            List<Consulta> lista = consultaDAO.filtrar(busca, data);
            encaminharListaConsultas(request, response, lista);

        } else if ("novo".equals(acao)) {

            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();

            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);

            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);

        } else {

            List<Consulta> lista;
            if ("TUTOR".equals(role)) {
                lista = consultaDAO.buscarPorTutor(idLogado);
            } else {
                lista = consultaDAO.listarTodos();
            }

            encaminharListaConsultas(request, response, lista);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        if ("TUTOR".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/consultas");
            return;
        }

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }
            consultaDAO.deletar(id);
            response.sendRedirect(request.getContextPath() + "/consultas");
            return;
        }

        String dataConsultaParam = request.getParameter("dataConsulta");
        String motivo = request.getParameter("motivo");
        String diagnostico = request.getParameter("diagnostico");

        LocalDateTime dataConsulta;
        try {
            dataConsulta = LocalDateTime.parse(dataConsultaParam);
        } catch (Exception e) {
            request.setAttribute("erro", "Data e hora da consulta inválidas.");
            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);
            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);
            return;
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            request.setAttribute("erro", "Motivo da consulta é obrigatório.");
            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);
            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);
            return;
        }

        Long petId = parseLongPositivo(request.getParameter("petId"));
        if (petId == null) {
            request.setAttribute("erro", "Pet inválido.");
            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);
            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);
            return;
        }
        Pet pet = new Pet();
        pet.setId(petId);

        Long vetId = parseLongPositivo(request.getParameter("vetId"));
        if (vetId == null) {
            request.setAttribute("erro", "Veterinário inválido.");
            List<Pet> pets = petDAO.listarTodos();
            List<Veterinario> vets = vetDAO.listarTodos();
            request.setAttribute("listaPets", pets);
            request.setAttribute("listaVets", vets);
            request.getRequestDispatcher("/form-consulta.jsp").forward(request, response);
            return;
        }
        Veterinario vet = new Veterinario();
        vet.setId(vetId);

        if ("atualizar".equals(acao)) {

            Long id = parseLongPositivo(request.getParameter("id"));
            if (id == null) {
                response.sendRedirect(request.getContextPath() + "/consultas");
                return;
            }
            Consulta consulta = new Consulta();
            consulta.setId(id);
            consulta.setDataConsulta(dataConsulta);
            consulta.setMotivo(motivo);
            consulta.setDiagnostico(diagnostico);
            consulta.setPet(pet);
            consulta.setVeterinario(vet);

            consultaDAO.atualizar(consulta);

        } else {

            Consulta consultaNova = new Consulta();
            consultaNova.setDataConsulta(dataConsulta);
            consultaNova.setMotivo(motivo);
            consultaNova.setDiagnostico(diagnostico);
            consultaNova.setPet(pet);
            consultaNova.setVeterinario(vet);

            consultaDAO.salvar(consultaNova);
        }

        response.sendRedirect(request.getContextPath() + "/consultas");
    }
}