package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obterUsuarioLogado(request, response);
        if (usuario == null) {
            return;
        }

        if ("1".equals(request.getParameter("telefoneAtualizado"))) {
            request.setAttribute("sucesso", "Telefone atualizado com sucesso.");
        }

        if ("1".equals(request.getParameter("contatoAtualizado"))) {
            request.setAttribute("sucesso", "Contato atualizado com sucesso.");
        }

        carregarPerfil(request, response, usuario, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obterUsuarioLogado(request, response);
        if (usuario == null) {
            return;
        }

        String acao = request.getParameter("acao");

        if ("deletarConta".equals(acao)) {
            if (!"TUTOR".equals(usuario.getRole())) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            usuarioDAO.deletar(usuario.getId());

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String telefone = InputSanitizer.sanitizarTexto(request.getParameter("telefone"));
        String email = InputSanitizer.sanitizarTexto(request.getParameter("email"));

        if (!("TUTOR".equals(usuario.getRole()) || "VETERINARIO".equals(usuario.getRole()))) {
            response.sendRedirect(request.getContextPath() + "/perfil");
            return;
        }

        if (InputValidator.isNullOrBlank(email)) {
            request.setAttribute("erro", "Informe o e-mail.");
            carregarPerfil(request, response, usuario, telefone, email);
            return;
        }

        if (!InputValidator.emailValido(email)) {
            request.setAttribute("erro", "E-mail inválido.");
            carregarPerfil(request, response, usuario, telefone, email);
            return;
        }

        if ("TUTOR".equals(usuario.getRole())) {
            if (InputValidator.isNullOrBlank(telefone)) {
                request.setAttribute("erro", "Informe o telefone.");
                carregarPerfil(request, response, usuario, telefone, email);
                return;
            }

            if (!InputValidator.telefoneValido(telefone)) {
                request.setAttribute("erro", "Telefone inválido. Use o formato (DDD) 99999-9999.");
                carregarPerfil(request, response, usuario, telefone, email);
                return;
            }
        }

        Usuario usuarioComMesmoEmail = usuarioDAO.buscarPorEmail(email);
        if (usuarioComMesmoEmail != null && !usuario.getId().equals(usuarioComMesmoEmail.getId())) {
            request.setAttribute("erro", "Este e-mail já está em uso.");
            carregarPerfil(request, response, usuario, telefone, email);
            return;
        }

        if ("VETERINARIO".equals(usuario.getRole())) {
            usuarioDAO.atualizarEmail(usuario.getId(), email);
            response.sendRedirect(request.getContextPath() + "/perfil?contatoAtualizado=1");
            return;
        }

        Tutor tutor = tutorDAO.buscarPorId(usuario.getId());
        if (tutor == null) {
            tutor = new Tutor();
            tutor.setId(usuario.getId());
            tutor.setNome(usuario.getNome());
        }
        tutor.setTelefone(telefone);
        tutorDAO.atualizar(tutor);
        usuarioDAO.atualizarEmail(usuario.getId(), email);

        response.sendRedirect(request.getContextPath() + "/perfil?contatoAtualizado=1");
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        Long idLogado = (Long) session.getAttribute("usuarioId");
        return usuarioDAO.buscarPorId(idLogado);
    }

    private void carregarPerfil(HttpServletRequest request, HttpServletResponse response, Usuario usuario, String telefonePreenchido, String emailPreenchido)
            throws ServletException, IOException {
        request.setAttribute("usuario", usuario);
        request.setAttribute("telefoneValue", telefonePreenchido != null ? telefonePreenchido : buscarTelefone(usuario));
        request.setAttribute("emailValue", emailPreenchido != null ? emailPreenchido : (usuario != null ? usuario.getEmail() : null));
        request.getRequestDispatcher("/perfil.jsp").forward(request, response);
    }

    private String buscarTelefone(Usuario usuario) {
        if (usuario == null || !"TUTOR".equals(usuario.getRole())) {
            return null;
        }

        Tutor tutor = tutorDAO.buscarPorId(usuario.getId());
        return tutor != null ? tutor.getTelefone() : null;
    }
}