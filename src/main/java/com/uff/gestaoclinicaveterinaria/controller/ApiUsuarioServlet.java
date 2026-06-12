package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.UsuarioAdminRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.UsuarioResponseDTO;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;
import com.uff.gestaoclinicaveterinaria.util.UsuarioPolicy;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Equivalente em JSON do UsuarioServlet (JSP), para o front React: gestão de
 * usuários exclusiva do ADMIN, com os mesmos guardrails de {@link UsuarioPolicy}.
 */
@WebServlet({"/api/usuarios", "/api/usuarios/*"})
public class ApiUsuarioServlet extends ApiServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"ADMIN".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        List<Usuario> lista = usuarioDAO.listarTodos();
        responderJson(response, HttpServletResponse.SC_OK,
                lista.stream().map(this::paraDTO).toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"ADMIN".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        UsuarioAdminRequestDTO corpo;
        try {
            corpo = lerCorpo(request, UsuarioAdminRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        String nome = InputSanitizer.sanitizarTexto(corpo.nome());
        String email = InputSanitizer.sanitizarTexto(corpo.email());
        String senha = corpo.senha();
        String role = corpo.role();

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(nome) || !InputValidator.nomeCompletoValido(nome)) {
            erros.put("nome", "Informe nome e sobrenome (mínimo 2 palavras).");
        }
        if (InputValidator.isNullOrBlank(email) || !InputValidator.emailValido(email)) {
            erros.put("email", "Informe um e-mail válido.");
        }
        if (!UsuarioPolicy.papelValido(role)) {
            erros.put("role", "Papel inválido.");
        }
        if (InputValidator.isNullOrBlank(senha) || !InputValidator.senhaSegura(senha)) {
            erros.put("senha", "A senha deve ter ao menos 8 caracteres, com maiúscula, minúscula e número.");
        }
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Verifique os dados informados.", erros);
            return;
        }

        if (usuarioDAO.buscarPorEmail(email) != null) {
            responderErro(response, HttpServletResponse.SC_CONFLICT, "Já existe um usuário com esse e-mail.",
                    Map.of("email", "Já existe um usuário com esse e-mail."));
            return;
        }

        String salt = PasswordUtil.gerarSalt();
        String senhaHash = PasswordUtil.gerarHash(senha, salt);

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSalt(salt);
        usuario.setSenhaHash(senhaHash);
        usuario.setRole(role);
        usuarioDAO.salvar(usuario);

        criarRegistroEspecifico(usuario);

        responderJson(response, HttpServletResponse.SC_CREATED, paraDTO(usuario));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"ADMIN".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        UsuarioAdminRequestDTO corpo;
        try {
            corpo = lerCorpo(request, UsuarioAdminRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null || corpo.id() == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador do usuário é obrigatório.",
                    Map.of("id", "Identificador do usuário é obrigatório."));
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorId(corpo.id());
        if (usuario == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
            return;
        }

        String nome = InputSanitizer.sanitizarTexto(corpo.nome());
        String email = InputSanitizer.sanitizarTexto(corpo.email());
        String role = corpo.role();
        String senha = corpo.senha();

        Map<String, String> erros = new LinkedHashMap<>();
        if (InputValidator.isNullOrBlank(nome) || !InputValidator.nomeCompletoValido(nome)) {
            erros.put("nome", "Informe nome e sobrenome (mínimo 2 palavras).");
        }
        if (InputValidator.isNullOrBlank(email) || !InputValidator.emailValido(email)) {
            erros.put("email", "Informe um e-mail válido.");
        }
        if (!UsuarioPolicy.papelValido(role)) {
            erros.put("role", "Papel inválido.");
        }
        if (senha != null && !senha.isBlank() && !InputValidator.senhaSegura(senha)) {
            erros.put("senha", "A senha deve ter ao menos 8 caracteres, com maiúscula, minúscula e número.");
        }
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Verifique os dados informados.", erros);
            return;
        }

        Usuario comMesmoEmail = usuarioDAO.buscarPorEmail(email);
        if (comMesmoEmail != null && !comMesmoEmail.getId().equals(usuario.getId())) {
            responderErro(response, HttpServletResponse.SC_CONFLICT, "Este e-mail já está em uso.",
                    Map.of("email", "Este e-mail já está em uso."));
            return;
        }

        if (!UsuarioPolicy.podeAlterarPapel(usuario.getRole(), role, usuarioDAO.contarPorRole("ADMIN"))) {
            responderErro(response, HttpServletResponse.SC_CONFLICT,
                    "Operação bloqueada: o sistema precisa de pelo menos um administrador.");
            return;
        }

        usuarioDAO.atualizarNome(usuario.getId(), nome);
        usuarioDAO.atualizarEmail(usuario.getId(), email);
        usuarioDAO.atualizarRole(usuario.getId(), role);

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setRole(role);

        if (senha != null && !senha.isBlank()) {
            String salt = PasswordUtil.gerarSalt();
            String senhaHash = PasswordUtil.gerarHash(senha, salt);
            usuarioDAO.atualizarSenha(usuario.getId(), senhaHash, salt);
        }

        criarRegistroEspecifico(usuario);

        responderJson(response, HttpServletResponse.SC_OK, paraDTO(usuario));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"ADMIN".equals(roleUsuarioLogado(request))) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        Long id = extrairIdDoPath(request);
        if (id == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
            return;
        }

        Long idLogado = idUsuarioLogado(request);
        if (!UsuarioPolicy.podeExcluir(id, idLogado, usuario.getRole(), usuarioDAO.contarPorRole("ADMIN"))) {
            if (id.equals(idLogado)) {
                responderErro(response, HttpServletResponse.SC_CONFLICT,
                        "Você não pode excluir a própria conta de administrador.");
            } else {
                responderErro(response, HttpServletResponse.SC_CONFLICT,
                        "Operação bloqueada: o sistema precisa de pelo menos um administrador.");
            }
            return;
        }

        usuarioDAO.deletar(id);
        responderJson(response, HttpServletResponse.SC_OK, corpoJson("success", true));
    }

    /** Garante que TUTOR/VETERINARIO tenham o registro específico ao serem criados/promovidos pelo ADMIN. */
    private void criarRegistroEspecifico(Usuario usuario) {
        if ("TUTOR".equals(usuario.getRole())) {
            TutorDAO tutorDAO = new TutorDAOImpl();
            if (tutorDAO.buscarPorId(usuario.getId()) == null) {
                Tutor tutor = new Tutor();
                tutor.setId(usuario.getId());
                tutor.setNome(usuario.getNome());
                tutor.setTelefone("(00) 00000-0000");
                tutorDAO.salvar(tutor);
            }
        } else if ("VETERINARIO".equals(usuario.getRole())) {
            VeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();
            if (veterinarioDAO.buscarPorId(usuario.getId()) == null) {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(usuario.getId());
                veterinario.setNome(usuario.getNome());
                veterinario.setCrmv(String.format("CRMV-XX %05d", usuario.getId()));
                veterinario.setEspecialidade("Clínica Geral");
                veterinarioDAO.salvar(veterinario);
            }
        }
    }

    private UsuarioResponseDTO paraDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
