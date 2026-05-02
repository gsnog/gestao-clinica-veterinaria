<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String roleValue = (String) request.getAttribute("roleValue");
    String telefoneValue = (String) request.getAttribute("telefoneValue");
    String crmvValue = (String) request.getAttribute("crmvValue");
    String especialidadeValue = (String) request.getAttribute("especialidadeValue");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Cadastro - Clínica Veterinária</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="${pageContext.request.contextPath}/scripts/validation.js" defer></script>
    <script src="${pageContext.request.contextPath}/scripts/registro.js" defer></script>
</head>
<body class="auth-body">
    <main class="auth-shell">
        <section class="auth-card">
            <div class="auth-brand-panel">
                <h1 class="auth-brand-title">VetCare</h1>
                <p class="auth-brand-subtitle">Crie sua conta para gerenciar pets e consultas.</p>
            </div>

            <div class="auth-form-panel">
                <h2 class="auth-title">Cadastrar</h2>
                <p class="auth-text">Preencha seus dados para começar.</p>

                <form action="${pageContext.request.contextPath}/registro" method="post" class="auth-form">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" value="${nomeValue}" required>
                    </div>

                    <div class="form-group">
                        <label for="email">E-mail</label>
                        <input type="email" id="email" name="email" value="${emailValue}" required>
                    </div>

                    <div class="form-group">
                        <label for="senha">Senha</label>
                        <input type="password" id="senha" name="senha" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label for="role">Tipo de usuário</label>
                        <select id="role" name="role" required>
                            <option value="">Selecione</option>
                            <option value="TUTOR" <%= "TUTOR".equals(roleValue) ? "selected" : "" %>>Tutor</option>
                            <option value="VETERINARIO" <%= "VETERINARIO".equals(roleValue) ? "selected" : "" %>>Veterinário</option>
                        </select>
                    </div>

                    <div class="form-group" id="tutorFields" style="display:none;">
                        <label for="telefone">Telefone</label>
                        <input type="tel" id="telefone" name="telefone"
                               placeholder="(21) 99999-9999"
                               maxlength="15"
                               pattern="\(\d{2}\)\s\d{4,5}-\d{4}"
                               value="<%= telefoneValue != null ? telefoneValue : "" %>">
                    </div>

                    <div id="vetFields" style="display:none;">
                        <div class="form-group">
                            <label for="crmv">CRMV</label>
                            <input type="text" id="crmv" name="crmv"
                                   placeholder="CRMV-RJ 12345"
                                   maxlength="14"
                                   pattern="CRMV-[A-Z]{2} [0-9]{5}"
                                   value="<%= crmvValue != null ? crmvValue : "" %>">
                        </div>

                        <div class="form-group">
                            <label for="especialidade">Especialidade</label>
                            <input type="text" id="especialidade" name="especialidade"
                                   placeholder="Ex.: Clínica Geral"
                                   value="<%= especialidadeValue != null ? especialidadeValue : "" %>">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-submit auth-submit">Cadastrar</button>
                </form>

                <% if (request.getAttribute("erro") != null) { %>
                <p class="auth-message auth-error">${erro}</p>
                <% } %>

                <% if (request.getAttribute("sucesso") != null) { %>
                <p class="auth-message auth-success">${sucesso}</p>
                <% } %>

                <p class="auth-switch">
                    Já tem conta?
                    <a href="${pageContext.request.contextPath}/login">Entrar</a>
                </p>
            </div>
        </section>
    </main>
</body>
</html>