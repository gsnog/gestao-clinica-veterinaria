<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Login - Clínica Veterinária</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="${pageContext.request.contextPath}/scripts/validation.js" defer></script>
    <script src="${pageContext.request.contextPath}/scripts/login.js" defer></script>
</head>
<body class="auth-body">
    <main class="auth-shell">
        <section class="auth-card">
            <div class="auth-brand-panel">
                <h1 class="auth-brand-title">VetCare</h1>
                <p class="auth-brand-subtitle">Bem-vinda de volta ao painel da clínica.</p>
            </div>

            <div class="auth-form-panel">
                <h2 class="auth-title">Entrar</h2>
                <p class="auth-text">Use seu e-mail e senha para continuar.</p>

                <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form" id="loginForm" novalidate>
                    <div class="form-group">
                        <label for="email">E-mail</label>
                        <input type="email" id="email" name="email"
                               value="${emailSalvo}" required autocomplete="email">
                    </div>

                    <div class="form-group">
                        <label for="senha">Senha</label>
                        <input type="password" id="senha" name="senha" required autocomplete="current-password">
                    </div>

                    <div class="form-group" style="flex-direction:row;align-items:center;gap:8px;">
                        <input type="checkbox" id="lembrar" name="lembrar" value="true"
                               style="width:auto;margin:0;">
                        <label for="lembrar" style="margin:0;font-weight:400;cursor:pointer;">Lembrar meu e-mail</label>
                    </div>

                    <button type="submit" class="btn btn-submit auth-submit">Entrar</button>
                </form>

                <% if (request.getAttribute("erro") != null) { %>
                <p class="auth-message auth-error">${erro}</p>
                <% } %>

                <% if (request.getAttribute("sucesso") != null) { %>
                <p class="auth-message auth-success">${sucesso}</p>
                <% } %>

                <p class="auth-switch">
                    Não tem conta?
                    <a href="${pageContext.request.contextPath}/registro">Cadastre-se</a>
                </p>
            </div>
        </section>
    </main>
</body>
</html>