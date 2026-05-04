package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.UsuarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;
import com.uff.gestaoclinicaveterinaria.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final TutorDAO tutorDAO = new TutorDAOImpl();
    private final VeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();

    private void preencherValoresFormulario(HttpServletRequest request, String nome, String email,
                                            String role, String telefone, String crmv, String especialidade) {
        request.setAttribute("nomeValue", nome);
        request.setAttribute("emailValue", email);
        request.setAttribute("roleValue", role);
        request.setAttribute("telefoneValue", telefone);
        request.setAttribute("crmvValue", crmv);
        request.setAttribute("especialidadeValue", especialidade);
    }

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
        String telefone = request.getParameter("telefone");
        String crmv = request.getParameter("crmv");
        String especialidade = request.getParameter("especialidade");

        preencherValoresFormulario(request, nome, email, role, telefone, crmv, especialidade);

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

        if ("TUTOR".equals(role) && InputValidator.isNullOrBlank(telefone)) {
            request.setAttribute("erro", "Informe o telefone do tutor.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if ("VETERINARIO".equals(role)
                && (InputValidator.isNullOrBlank(crmv) || InputValidator.isNullOrBlank(especialidade))) {
            request.setAttribute("erro", "Informe CRMV e especialidade do veterinário.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        nome = InputSanitizer.sanitizarTexto(nome);
        email = InputSanitizer.sanitizarTexto(email);
        telefone = telefone != null ? InputSanitizer.sanitizarTexto(telefone) : null;
        crmv = crmv != null ? InputSanitizer.sanitizarTexto(crmv).toUpperCase() : null;
        especialidade = especialidade != null ? InputSanitizer.sanitizarTexto(especialidade) : null;

        preencherValoresFormulario(request, nome, email, role, telefone, crmv, especialidade);

        if (!InputValidator.nomeCompletoValido(nome)) {
            request.setAttribute("erro", "Informe nome e sobrenome (mínimo 2 palavras).");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if ("TUTOR".equals(role)
            && (telefone == null || !telefone.matches("\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}"))) {
            request.setAttribute("erro", "Telefone inválido. Use o formato (DDD) 99999-9999.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        if ("VETERINARIO".equals(role)
            && (crmv == null || !crmv.matches("CRMV-[A-Z]{2} [0-9]{5}"))) {
            request.setAttribute("erro", "CRMV inválido. Use o formato CRMV-UF 12345.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

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
            request.setAttribute("erro", "Não foi possível concluir o cadastro. Verifique os dados e tente novamente.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("sucesso", "Usuário cadastrado com sucesso. Faça login para continuar.");
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }
}