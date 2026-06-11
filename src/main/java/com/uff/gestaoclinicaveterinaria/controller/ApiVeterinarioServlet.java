package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.VeterinarioRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.VeterinarioResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/api/veterinarios", "/api/veterinarios/*"})
public class ApiVeterinarioServlet extends ApiServlet {

    private final VeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"VETERINARIO".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a veterinários.");
            return;
        }

        List<Veterinario> lista = veterinarioDAO.listarTodos();
        responderJson(response, HttpServletResponse.SC_OK, lista.stream().map(this::paraDTO).toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responderErro(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Cadastro de veterinários é feito pelo fluxo de registro.");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long idLogado = idUsuarioLogado(request);

        VeterinarioRequestDTO corpo;
        try {
            corpo = lerCorpo(request, VeterinarioRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null || corpo.id() == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador do veterinário é obrigatório.",
                    Map.of("id", "Identificador do veterinário é obrigatório."));
            return;
        }

        if (!idLogado.equals(corpo.id())) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode atualizar o seu próprio cadastro.");
            return;
        }

        if (veterinarioDAO.buscarPorId(corpo.id()) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Veterinário não encontrado.");
            return;
        }

        String crmv = corpo.crmv() != null ? corpo.crmv().toUpperCase() : null;

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(corpo.nome()) || !InputValidator.nomeCompletoValido(corpo.nome())) {
            erros.put("nome", "Informe nome e sobrenome (mínimo 2 palavras).");
        }
        if (!InputValidator.crmvValido(crmv)) {
            erros.put("crmv", "CRMV inválido. Use o formato CRMV-UF 12345.");
        }
        if (InputValidator.isNullOrBlank(corpo.especialidade())) {
            erros.put("especialidade", "Especialidade é obrigatória.");
        }
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados do veterinário inválidos.", erros);
            return;
        }

        Veterinario atualizado = new Veterinario();
        atualizado.setId(corpo.id());
        atualizado.setNome(InputSanitizer.sanitizarTexto(corpo.nome()));
        atualizado.setCrmv(InputSanitizer.sanitizarTexto(crmv));
        atualizado.setEspecialidade(InputSanitizer.sanitizarTexto(corpo.especialidade()));
        veterinarioDAO.atualizar(atualizado);

        responderJson(response, HttpServletResponse.SC_OK, paraDTO(atualizado));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long idLogado = idUsuarioLogado(request);

        Long id = extrairIdDoPath(request);
        if (id == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
            return;
        }

        if (!idLogado.equals(id)) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode excluir o seu próprio cadastro.");
            return;
        }

        if (veterinarioDAO.buscarPorId(id) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Veterinário não encontrado.");
            return;
        }

        veterinarioDAO.deletar(id);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        responderJson(response, HttpServletResponse.SC_OK, corpoJson("success", true));
    }

    private VeterinarioResponseDTO paraDTO(Veterinario veterinario) {
        return new VeterinarioResponseDTO(veterinario.getId(), veterinario.getNome(), veterinario.getCrmv(), veterinario.getEspecialidade());
    }
}
