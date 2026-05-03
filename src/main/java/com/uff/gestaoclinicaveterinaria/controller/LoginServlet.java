package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("recuperada"))) {
            request.setAttribute("sucesso", "Senha redefinida com sucesso. Faça seu login.");
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (InputValidator.isNullOrBlank(email) || InputValidator.isNullOrBlank(senha)) {
            request.setAttribute("erro", "Preencha todos os campos.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.emailValido(email)) {
            request.setAttribute("erro", "Credenciais inválidas.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        email = InputSanitizer.sanitizarTexto(email);

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null || !PasswordUtil.verificarSenha(senha, usuario.getSalt(), usuario.getSenhaHash())) {
            request.setAttribute("erro", "Credenciais inválidas.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
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

        String destino = "VETERINARIO".equals(usuario.getRole()) ? "/dashboard" : "/pets";
        response.sendRedirect(request.getContextPath() + destino);
    }
}