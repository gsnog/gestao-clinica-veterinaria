package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.RegistroRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.UsuarioResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Equivalente em JSON do RegistroServlet (JSP) para o front React: mesma
 * validação e persistência, porém respondendo no envelope {success, user}.
 */
@WebServlet("/api/registro")
public class ApiRegistroServlet extends ApiServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final TutorDAO tutorDAO = new TutorDAOImpl();
    private final VeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RegistroRequestDTO corpo;
        try {
            corpo = lerCorpo(request, RegistroRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        String nome = corpo.nome();
        String email = corpo.email();
        String senha = corpo.senha();
        String role = corpo.role();
        String telefone = corpo.telefone();
        String crmv = corpo.crmv();
        String especialidade = corpo.especialidade();

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(nome) || !InputValidator.nomeCompletoValido(nome)) {
            erros.put("nome", "Informe nome e sobrenome (mínimo 2 palavras).");
        }
        if (InputValidator.isNullOrBlank(email) || !InputValidator.emailValido(email)) {
            erros.put("email", "Informe um e-mail válido.");
        }
        if (InputValidator.isNullOrBlank(senha) || senha.trim().length() < 6) {
            erros.put("senha", "A senha deve ter pelo menos 6 caracteres.");
        }
        if (!"TUTOR".equals(role) && !"VETERINARIO".equals(role)) {
            erros.put("role", "Tipo de usuário inválido.");
        } else if ("TUTOR".equals(role)) {
            if (InputValidator.isNullOrBlank(telefone) || !InputValidator.telefoneValido(telefone)) {
                erros.put("telefone", "Telefone inválido. Use o formato (DDD) 99999-9999.");
            }
        } else {
            if (InputValidator.isNullOrBlank(crmv) || !InputValidator.crmvValido(crmv)) {
                erros.put("crmv", "CRMV inválido. Use o formato CRMV-UF 12345.");
            }
            if (InputValidator.isNullOrBlank(especialidade)) {
                erros.put("especialidade", "Informe a especialidade do veterinário.");
            }
        }

        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Verifique os dados informados.", erros);
            return;
        }

        nome = InputSanitizer.sanitizarTexto(nome);
        String emailSanitizado = InputSanitizer.sanitizarTexto(email);
        telefone = "TUTOR".equals(role) ? InputSanitizer.sanitizarTexto(telefone) : null;
        crmv = "VETERINARIO".equals(role) ? InputSanitizer.sanitizarTexto(crmv).toUpperCase() : null;
        especialidade = "VETERINARIO".equals(role) ? InputSanitizer.sanitizarTexto(especialidade) : null;

        if (usuarioDAO.buscarPorEmail(emailSanitizado) != null) {
            responderErro(response, HttpServletResponse.SC_CONFLICT,
                    "Já existe um usuário cadastrado com esse e-mail.",
                    Map.of("email", "Já existe um usuário cadastrado com esse e-mail."));
            return;
        }

        String salt = PasswordUtil.gerarSalt();
        String senhaHash = PasswordUtil.gerarHash(senha, salt);

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(emailSanitizado);
        usuario.setSalt(salt);
        usuario.setSenhaHash(senhaHash);
        usuario.setRole(role);

        try {
            usuarioDAO.salvar(usuario);

            if (usuario.getId() == null) {
                throw new RuntimeException("Não foi possível obter o identificador do usuário cadastrado.");
            }

            if ("TUTOR".equals(role)) {
                Tutor tutor = new Tutor();
                tutor.setId(usuario.getId());
                tutor.setNome(usuario.getNome());
                tutor.setTelefone(telefone);
                tutorDAO.salvar(tutor);
            } else {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(usuario.getId());
                veterinario.setNome(usuario.getNome());
                veterinario.setCrmv(crmv);
                veterinario.setEspecialidade(especialidade);
                veterinarioDAO.salvar(veterinario);
            }
        } catch (RuntimeException e) {
            if (usuario.getId() != null) {
                try {
                    usuarioDAO.deletar(usuario.getId());
                } catch (RuntimeException ignored) {
                    // Melhor esforço para evitar usuário órfão em cadastro parcial.
                }
            }
            responderErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Não foi possível concluir o cadastro. Verifique os dados e tente novamente.");
            return;
        }

        responderJson(response, HttpServletResponse.SC_CREATED, corpoJson(
                "success", true,
                "user", new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole())
        ));
    }
}
