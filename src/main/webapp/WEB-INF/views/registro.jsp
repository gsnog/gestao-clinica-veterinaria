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

    <script>
    (function() {
        const roleSelect = document.getElementById('role');
        const tutorFields = document.getElementById('tutorFields');
        const vetFields = document.getElementById('vetFields');
        const telefoneInput = document.getElementById('telefone');
        const crmvInput = document.getElementById('crmv');
        const especialidadeInput = document.getElementById('especialidade');

        function atualizarCamposPorRole() {
            const role = roleSelect.value;
            const isTutor = role === 'TUTOR';
            const isVet = role === 'VETERINARIO';

            tutorFields.style.display = isTutor ? 'block' : 'none';
            vetFields.style.display = isVet ? 'block' : 'none';

            telefoneInput.required = isTutor;
            crmvInput.required = isVet;
            especialidadeInput.required = isVet;
        }

        if (telefoneInput) {
            telefoneInput.addEventListener('input', function(e) {
                let v = e.target.value.replace(/\D/g, '');
                if (v.length > 11) v = v.slice(0, 11);
                if (v.length <= 10) {
                    v = v.replace(/^(\d{2})(\d)/g, '($1) $2');
                    v = v.replace(/(\d{4})(\d)/, '$1-$2');
                } else {
                    v = v.replace(/^(\d{2})(\d)/g, '($1) $2');
                    v = v.replace(/(\d{5})(\d)/, '$1-$2');
                }
                e.target.value = v;
            });
        }

        if (crmvInput) {
            crmvInput.addEventListener('input', function(e) {
                let v = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
                v = v.replace(/^CRMV/, '');
                let resultado = 'CRMV-';
                const letras = v.replace(/[^A-Z]/g, '').substring(0, 2);
                const numeros = v.replace(/[^0-9]/g, '').substring(0, 5);
                resultado += letras;
                if (letras.length === 2) {
                    resultado += ' ' + numeros;
                }
                e.target.value = resultado;
            });
        }

        roleSelect.addEventListener('change', atualizarCamposPorRole);
        atualizarCamposPorRole();
    })();
    </script>
</body>
</html>