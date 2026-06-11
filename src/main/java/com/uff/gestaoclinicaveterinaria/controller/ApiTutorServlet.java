package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.TutorRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.TutorResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Diferente da tela legada (/tutores, restrita a veterinários), aqui o tutor
 * pode acessar — mas só enxerga o próprio cadastro, nunca a lista geral.
 */
@WebServlet({"/api/tutores", "/api/tutores/*"})
public class ApiTutorServlet extends ApiServlet {

    private final TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        if ("TUTOR".equals(role)) {
            Tutor proprio = tutorDAO.buscarPorId(idLogado);
            List<TutorResponseDTO> corpo = proprio != null ? List.of(paraDTO(proprio)) : List.of();
            responderJson(response, HttpServletResponse.SC_OK, corpo);
            return;
        }

        List<Tutor> lista = tutorDAO.listarTodos();
        responderJson(response, HttpServletResponse.SC_OK, lista.stream().map(this::paraDTO).toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responderErro(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Cadastro de tutores é feito pelo fluxo de registro.");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        TutorRequestDTO corpo;
        try {
            corpo = lerCorpo(request, TutorRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null || corpo.id() == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador do tutor é obrigatório.",
                    Map.of("id", "Identificador do tutor é obrigatório."));
            return;
        }

        if ("TUTOR".equals(role) && !idLogado.equals(corpo.id())) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode atualizar o seu próprio cadastro.");
            return;
        }

        if (tutorDAO.buscarPorId(corpo.id()) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Tutor não encontrado.");
            return;
        }

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(corpo.nome()) || !InputValidator.nomeCompletoValido(corpo.nome())) {
            erros.put("nome", "Informe nome e sobrenome (mínimo 2 palavras).");
        }
        if (InputValidator.isNullOrBlank(corpo.telefone()) || !InputValidator.telefoneValido(corpo.telefone())) {
            erros.put("telefone", "Telefone inválido. Use o formato (DDD) 99999-9999.");
        }
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados do tutor inválidos.", erros);
            return;
        }

        Tutor atualizado = new Tutor();
        atualizado.setId(corpo.id());
        atualizado.setNome(InputSanitizer.sanitizarTexto(corpo.nome()));
        atualizado.setTelefone(InputSanitizer.sanitizarTexto(corpo.telefone()));
        tutorDAO.atualizar(atualizado);

        responderJson(response, HttpServletResponse.SC_OK, paraDTO(atualizado));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        Long id = extrairIdDoPath(request);
        if (id == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
            return;
        }

        if ("TUTOR".equals(role) && !idLogado.equals(id)) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode excluir o seu próprio cadastro.");
            return;
        }

        if (tutorDAO.buscarPorId(id) == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Tutor não encontrado.");
            return;
        }

        tutorDAO.deletar(id);

        if ("TUTOR".equals(role)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }

        responderJson(response, HttpServletResponse.SC_OK, corpoJson("success", true));
    }

    private TutorResponseDTO paraDTO(Tutor tutor) {
        return new TutorResponseDTO(tutor.getId(), tutor.getNome(), tutor.getTelefone());
    }
}
