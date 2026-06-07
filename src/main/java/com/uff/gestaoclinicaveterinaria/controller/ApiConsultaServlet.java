package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAO;
import com.uff.gestaoclinicaveterinaria.dao.ConsultaDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.ConsultaRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.ConsultaResponseDTO;
import com.uff.gestaoclinicaveterinaria.dto.RefDTO;
import com.uff.gestaoclinicaveterinaria.model.Consulta;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/api/consultas", "/api/consultas/*"})
public class ApiConsultaServlet extends ApiServlet {

    private final ConsultaDAO consultaDAO = new ConsultaDAOImpl();
    private final PetDAO petDAO = new PetDAOImpl();
    private final VeterinarioDAO vetDAO = new VeterinarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        List<Consulta> lista = "TUTOR".equals(role) ? consultaDAO.buscarPorTutor(idLogado) : consultaDAO.listarTodos();
        responderJson(response, HttpServletResponse.SC_OK, lista.stream().map(this::paraDTO).toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"VETERINARIO".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Apenas veterinários podem criar consultas.");
            return;
        }

        ConsultaRequestDTO corpo;
        try {
            corpo = lerCorpo(request, ConsultaRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        ValidacaoConsulta validacao = validarConsulta(corpo);
        if (!validacao.erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados da consulta inválidos.", validacao.erros);
            return;
        }

        Consulta nova = new Consulta();
        nova.setDataConsulta(corpo.dataConsulta());
        nova.setMotivo(InputSanitizer.sanitizarTexto(corpo.motivo()));
        nova.setDiagnostico(InputSanitizer.sanitizarTexto(corpo.diagnostico()));
        nova.setPet(validacao.pet);
        nova.setVeterinario(validacao.veterinario);

        consultaDAO.salvar(nova);

        responderJson(response, HttpServletResponse.SC_CREATED, paraDTO(nova));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"VETERINARIO".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Apenas veterinários podem editar consultas.");
            return;
        }

        ConsultaRequestDTO corpo;
        try {
            corpo = lerCorpo(request, ConsultaRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null || corpo.id() == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador da consulta é obrigatório.",
                    Map.of("id", "Identificador da consulta é obrigatório."));
            return;
        }

        if (consultaDAO.buscarPorId(corpo.id()) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta não encontrada.");
            return;
        }

        ValidacaoConsulta validacao = validarConsulta(corpo);
        if (!validacao.erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados da consulta inválidos.", validacao.erros);
            return;
        }

        Consulta atualizada = new Consulta();
        atualizada.setId(corpo.id());
        atualizada.setDataConsulta(corpo.dataConsulta());
        atualizada.setMotivo(InputSanitizer.sanitizarTexto(corpo.motivo()));
        atualizada.setDiagnostico(InputSanitizer.sanitizarTexto(corpo.diagnostico()));
        atualizada.setPet(validacao.pet);
        atualizada.setVeterinario(validacao.veterinario);

        consultaDAO.atualizar(atualizada);

        responderJson(response, HttpServletResponse.SC_OK, paraDTO(atualizada));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"VETERINARIO".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Apenas veterinários podem excluir consultas.");
            return;
        }

        Long id = extrairIdDoPath(request);
        if (id == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
            return;
        }

        if (consultaDAO.buscarPorId(id) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Consulta não encontrada.");
            return;
        }

        consultaDAO.deletar(id);
        responderJson(response, HttpServletResponse.SC_OK, corpoJson("success", true));
    }

    private ValidacaoConsulta validarConsulta(ConsultaRequestDTO corpo) {
        Map<String, String> erros = new LinkedHashMap<>();

        Pet pet = (corpo != null && corpo.petId() != null) ? petDAO.buscarPorId(corpo.petId()) : null;
        if (pet == null) {
            erros.put("petId", "Pet inválido.");
        }

        Veterinario veterinario = (corpo != null && corpo.veterinarioId() != null) ? vetDAO.buscarPorId(corpo.veterinarioId()) : null;
        if (veterinario == null) {
            erros.put("veterinarioId", "Veterinário inválido.");
        }

        if (corpo == null || corpo.dataConsulta() == null) {
            erros.put("dataConsulta", "Data e hora da consulta inválidas.");
        }

        if (corpo == null || InputValidator.isNullOrBlank(corpo.motivo())) {
            erros.put("motivo", "Motivo da consulta é obrigatório.");
        }

        return new ValidacaoConsulta(erros, pet, veterinario);
    }

    private ConsultaResponseDTO paraDTO(Consulta consulta) {
        Pet pet = consulta.getPet();
        Veterinario veterinario = consulta.getVeterinario();
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getDataConsulta(),
                consulta.getMotivo(),
                consulta.getDiagnostico(),
                pet != null ? new RefDTO(pet.getId(), pet.getNome()) : null,
                veterinario != null ? new RefDTO(veterinario.getId(), veterinario.getNome()) : null
        );
    }

    private record ValidacaoConsulta(Map<String, String> erros, Pet pet, Veterinario veterinario) {
    }
}
