package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.LoginRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.UsuarioResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;
import com.uff.gestaoclinicaveterinaria.util.PreferenceUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/login")
public class ApiLoginServlet extends ApiServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LoginRequestDTO corpo;
        try {
            corpo = lerCorpo(request, LoginRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        String email = corpo != null ? corpo.email() : null;
        String senha = corpo != null ? corpo.senha() : null;

        if (InputValidator.isNullOrBlank(email) || InputValidator.isNullOrBlank(senha)) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Preencha todos os campos.");
            return;
        }

        if (!InputValidator.emailValido(email)) {
            responderErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Credenciais inválidas.");
            return;
        }

        String emailSanitizado = InputSanitizer.sanitizarTexto(email);
        Usuario usuario = usuarioDAO.buscarPorEmail(emailSanitizado);

        if (usuario == null || !PasswordUtil.verificarSenha(senha, usuario.getSalt(), usuario.getSenhaHash())) {
            responderErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Credenciais inválidas.");
            return;
        }

        if (Boolean.TRUE.equals(corpo.lembrar())) {
            PreferenceUtil.setPreference(response, "email", emailSanitizado);
        } else {
            PreferenceUtil.removePreference(response, "email");
        }

        HttpSession sessaoAntiga = request.getSession(false);
        if (sessaoAntiga != null) {
            sessaoAntiga.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNome", usuario.getNome());
        session.setAttribute("usuarioRole", usuario.getRole());
        session.setMaxInactiveInterval(30 * 60);

        responderJson(response, HttpServletResponse.SC_OK, corpoJson(
                "success", true,
                "user", new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole())
        ));
    }
}
