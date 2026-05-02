package com.uff.gestaoclinicaveterinaria.controller;

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

import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String role = request.getParameter("role");

        if (InputValidator.isNullOrBlank(nome)
                || InputValidator.isNullOrBlank(email)
                || InputValidator.isNullOrBlank(senha)
                || InputValidator.isNullOrBlank(role)) {

            request.setAttribute("erro", "Preencha todos os campos.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if (!InputValidator.emailValido(email)) {
            request.setAttribute("erro", "E-mail inválido.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if (senha.length() < 6) {
            request.setAttribute("erro", "A senha deve ter pelo menos 6 caracteres.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if (!"TUTOR".equals(role) && !"VETERINARIO".equals(role)) {
            request.setAttribute("erro", "Tipo de usuário inválido.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        nome = InputSanitizer.sanitizarTexto(nome);
        email = InputSanitizer.sanitizarTexto(email);

        Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);

        if (usuarioExistente != null) {
            request.setAttribute("erro", "Já existe um usuário cadastrado com esse e-mail.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
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

        request.setAttribute("sucesso", "Usuário cadastrado com sucesso. Faça login para continuar.");
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }
}