package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.PerfilRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.UsuarioResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/perfil")
public class ApiPerfilServlet extends ApiServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuarioLogado(request));
        if (usuario == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
            return;
        }

        responderJson(response, HttpServletResponse.SC_OK, corpoJson(
                "success", true,
                "usuario", new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole()),
                "telefone", buscarTelefone(usuario)
        ));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuarioLogado(request));
        if (usuario == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
            return;
        }

        PerfilRequestDTO corpo;
        try {
            corpo = lerCorpo(request, PerfilRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        String email = InputSanitizer.sanitizarTexto(corpo != null ? corpo.email() : null);
        String telefone = InputSanitizer.sanitizarTexto(corpo != null ? corpo.telefone() : null);
        boolean usaTelefone = "TUTOR".equals(usuario.getRole()) || "VETERINARIO".equals(usuario.getRole());

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(email)) {
            erros.put("email", "Informe o e-mail.");
        } else if (!InputValidator.emailValido(email)) {
            erros.put("email", "E-mail inválido.");
        }

        if (usaTelefone) {
            if (InputValidator.isNullOrBlank(telefone)) {
                erros.put("telefone", "Informe o telefone.");
            } else if (!InputValidator.telefoneValido(telefone)) {
                erros.put("telefone", "Telefone inválido. Use o formato (DDD) 99999-9999.");
            }
        }

        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados inválidos.", erros);
            return;
        }

        Usuario usuarioComMesmoEmail = usuarioDAO.buscarPorEmail(email);
        if (usuarioComMesmoEmail != null && !usuario.getId().equals(usuarioComMesmoEmail.getId())) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Este e-mail já está em uso.",
                    Map.of("email", "Este e-mail já está em uso."));
            return;
        }

        usuarioDAO.atualizarEmail(usuario.getId(), email);

        String telefoneAtualizado = null;
        if (usaTelefone) {
            tutorDAO.salvarTelefonePorUsuarioId(usuario.getId(), telefone);
            telefoneAtualizado = telefone;
        }

        responderJson(response, HttpServletResponse.SC_OK, corpoJson(
                "success", true,
                "usuario", new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), email, usuario.getRole()),
                "telefone", telefoneAtualizado
        ));
    }

    private String buscarTelefone(Usuario usuario) {
        if (!"TUTOR".equals(usuario.getRole()) && !"VETERINARIO".equals(usuario.getRole())) {
            return null;
        }
        return tutorDAO.buscarTelefonePorUsuarioId(usuario.getId());
    }
}
