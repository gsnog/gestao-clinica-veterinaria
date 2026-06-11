package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;
import com.uff.gestaoclinicaveterinaria.util.SearchFilterUtil;
import com.uff.gestaoclinicaveterinaria.util.UsuarioPolicy;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Gestão de usuários, exclusiva do papel ADMIN.
 *
 * Guardrails de segurança aplicados aqui (e cobertos por testes de QA):
 * - somente ADMIN acessa (defesa em profundidade além do AuthFilter);
 * - papel recebido validado contra uma lista fixa (sem valores arbitrários no banco);
 * - admin não pode excluir a própria conta;
 * - o sistema nunca pode ficar sem nenhum ADMIN (exclusão/rebaixamento do último é bloqueado);
 * - escrita protegida por token CSRF (via CsrfFilter global).
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    private boolean naoEhAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("usuarioRole"))) {
            response.sendRedirect(request.getContextPath() + "/acesso-negado");
            return true;
        }
        return false;
    }

    private Long idLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (Long) session.getAttribute("usuarioId") : null;
    }

    private List<Usuario> filtrar(List<Usuario> usuarios, String busca) {
        String buscaNormalizada = SearchFilterUtil.normalize(busca);
        if (buscaNormalizada.isEmpty()) {
            return usuarios;
        }

        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (SearchFilterUtil.startsWithNormalized(u.getNome(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(u.getEmail(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(u.getRole(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(String.valueOf(u.getId()), buscaNormalizada)) {
                filtrados.add(u);
            }
        }
        return filtrados;
    }

    private String mensagemSucesso(String ok) {
        if (ok == null) {
            return null;
        }
        return switch (ok) {
            case "criado" -> "Usuário criado com sucesso.";
            case "atualizado" -> "Usuário atualizado com sucesso.";
            case "senhaResetada" -> "Senha redefinida com sucesso.";
            case "excluido" -> "Usuário excluído com sucesso.";
            default -> null;
        };
    }

    private String mensagemErro(String erro) {
        if (erro == null) {
            return null;
        }
        return switch (erro) {
            case "autoExclusao" -> "Você não pode excluir a própria conta de administrador.";
            case "ultimoAdmin" -> "Operação bloqueada: o sistema precisa de pelo menos um administrador.";
            case "naoEncontrado" -> "Usuário não encontrado.";
            default -> null;
        };
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (naoEhAdmin(request, response)) {
            return;
        }

        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/usuarios?erro=naoEncontrado");
                return;
            }
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
            return;
        }

        if ("novo".equals(acao)) {
            request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
            return;
        }

        List<Usuario> lista = filtrar(usuarioDAO.listarTodos(), request.getParameter("busca"));

        request.setAttribute("listaUsuarios", lista);
        request.setAttribute("idLogado", idLogado(request));
        request.setAttribute("buscaParam", request.getParameter("busca") != null ? request.getParameter("busca") : "");
        request.setAttribute("sucesso", mensagemSucesso(request.getParameter("ok")));
        request.setAttribute("erro", mensagemErro(request.getParameter("erro")));
        request.getRequestDispatcher("/lista-usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (naoEhAdmin(request, response)) {
            return;
        }

        String acao = request.getParameter("acao");

        if ("criar".equals(acao)) {
            criar(request, response);
            return;
        }

        if ("atualizar".equals(acao)) {
            atualizar(request, response);
            return;
        }

        if ("resetarSenha".equals(acao)) {
            resetarSenha(request, response);
            return;
        }

        if ("deletar".equals(acao)) {
            deletar(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private void criar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = InputSanitizer.sanitizarTexto(request.getParameter("nome"));
        String email = InputSanitizer.sanitizarTexto(request.getParameter("email"));
        String senha = request.getParameter("senha");
        String role = request.getParameter("role");

        if (InputValidator.isNullOrBlank(nome) || InputValidator.isNullOrBlank(email)
                || InputValidator.isNullOrBlank(senha) || InputValidator.isNullOrBlank(role)) {
            recarregarForm(request, response, null, "Preencha todos os campos.");
            return;
        }

        if (!UsuarioPolicy.papelValido(role)) {
            recarregarForm(request, response, null, "Papel inválido.");
            return;
        }

        if (!InputValidator.nomeCompletoValido(nome)) {
            recarregarForm(request, response, null, "Informe nome e sobrenome (mínimo 2 palavras).");
            return;
        }

        if (!InputValidator.emailValido(email)) {
            recarregarForm(request, response, null, "E-mail inválido.");
            return;
        }

        if (!InputValidator.senhaSegura(senha)) {
            recarregarForm(request, response, null,
                    "A senha deve ter ao menos 8 caracteres, com maiúscula, minúscula e número.");
            return;
        }

        if (usuarioDAO.buscarPorEmail(email) != null) {
            recarregarForm(request, response, null, "Já existe um usuário com esse e-mail.");
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

        if ("TUTOR".equals(role)) {
            TutorDAO tutorDAO = new TutorDAOImpl();
            Tutor t = new Tutor();
            t.setId(usuario.getId());
            t.setTelefone("(00) 00000-0000");
            t.setNome(usuario.getNome());
            try { tutorDAO.salvar(t); } catch (Exception ignored) {}
        } else if ("VETERINARIO".equals(role)) {
            VeterinarioDAO vetDAO = new VeterinarioDAOImpl();
            Veterinario v = new Veterinario();
            v.setId(usuario.getId());
            v.setCrmv(String.format("CRMV-XX %05d", usuario.getId()));
            v.setEspecialidade("Clínica Geral");
            v.setNome(usuario.getNome());
            try { vetDAO.salvar(v); } catch (Exception ignored) {}
        }

        response.sendRedirect(request.getContextPath() + "/usuarios?ok=criado");
    }

    private void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/usuarios?erro=naoEncontrado");
            return;
        }

        String nome = InputSanitizer.sanitizarTexto(request.getParameter("nome"));
        String email = InputSanitizer.sanitizarTexto(request.getParameter("email"));
        String role = request.getParameter("role");

        if (InputValidator.isNullOrBlank(nome) || InputValidator.isNullOrBlank(email)
                || InputValidator.isNullOrBlank(role)) {
            recarregarForm(request, response, usuario, "Preencha todos os campos.");
            return;
        }

        if (!UsuarioPolicy.papelValido(role)) {
            recarregarForm(request, response, usuario, "Papel inválido.");
            return;
        }

        if (!InputValidator.emailValido(email)) {
            recarregarForm(request, response, usuario, "E-mail inválido.");
            return;
        }

        Usuario comMesmoEmail = usuarioDAO.buscarPorEmail(email);
        if (comMesmoEmail != null && !comMesmoEmail.getId().equals(id)) {
            recarregarForm(request, response, usuario, "Este e-mail já está em uso.");
            return;
        }

        // Guardrail: rebaixar o último admin deixaria o sistema sem administrador.
        if (!UsuarioPolicy.podeAlterarPapel(usuario.getRole(), role, usuarioDAO.contarPorRole("ADMIN"))) {
            recarregarForm(request, response, usuario,
                    "Operação bloqueada: o sistema precisa de pelo menos um administrador.");
            return;
        }

        usuarioDAO.atualizarEmail(id, email);
        usuarioDAO.atualizarRole(id, role);

        if ("TUTOR".equals(role)) {
            TutorDAO tutorDAO = new TutorDAOImpl();
            if (tutorDAO.buscarPorId(id) == null || tutorDAO.buscarPorId(id).getTelefone() == null) {
                Tutor t = new Tutor();
                t.setId(id);
                t.setTelefone("(00) 00000-0000");
                t.setNome(nome);
                try { tutorDAO.salvar(t); } catch (Exception ignored) {}
            }
        } else if ("VETERINARIO".equals(role)) {
            VeterinarioDAO vetDAO = new VeterinarioDAOImpl();
            if (vetDAO.buscarPorId(id) == null || vetDAO.buscarPorId(id).getCrmv() == null) {
                Veterinario v = new Veterinario();
                v.setId(id);
                v.setCrmv(String.format("CRMV-XX %05d", id));
                v.setEspecialidade("Clínica Geral");
                v.setNome(nome);
                try { vetDAO.salvar(v); } catch (Exception ignored) {}
            }
        }

        response.sendRedirect(request.getContextPath() + "/usuarios?ok=atualizado");
    }

    private void resetarSenha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/usuarios?erro=naoEncontrado");
            return;
        }

        String senha = request.getParameter("senha");
        if (!InputValidator.senhaSegura(senha)) {
            recarregarForm(request, response, usuario,
                    "A senha deve ter ao menos 8 caracteres, com maiúscula, minúscula e número.");
            return;
        }

        String salt = PasswordUtil.gerarSalt();
        String senhaHash = PasswordUtil.gerarHash(senha, salt);
        usuarioDAO.atualizarSenha(id, senhaHash, salt);

        response.sendRedirect(request.getContextPath() + "/usuarios?ok=senhaResetada");
    }

    private void deletar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/usuarios?erro=naoEncontrado");
            return;
        }

        if (!UsuarioPolicy.podeExcluir(id, idLogado(request), usuario.getRole(), usuarioDAO.contarPorRole("ADMIN"))) {
            if (id.equals(idLogado(request))) {
                response.sendRedirect(request.getContextPath() + "/usuarios?erro=autoExclusao");
            } else {
                response.sendRedirect(request.getContextPath() + "/usuarios?erro=ultimoAdmin");
            }
            return;
        }

        usuarioDAO.deletar(id);
        response.sendRedirect(request.getContextPath() + "/usuarios?ok=excluido");
    }

    private void recarregarForm(HttpServletRequest request, HttpServletResponse response,
                                Usuario usuario, String erro) throws ServletException, IOException {
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
    }
}
