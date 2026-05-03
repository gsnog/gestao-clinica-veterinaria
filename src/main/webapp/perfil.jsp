<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Usuario" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    String telefoneValue = (String) request.getAttribute("telefoneValue");
    String emailValue = (String) request.getAttribute("emailValue");
    boolean mostrarTelefoneTutor = usuario != null && "TUTOR".equals(usuario.getRole());
    boolean mostrarFormContato = usuario != null && ("TUTOR".equals(usuario.getRole()) || "VETERINARIO".equals(usuario.getRole()));
%>

<div class="topbar">
    <div>
        <div class="page-title">Meu Perfil</div>
        <div class="page-subtitle">Informacoes da sua conta no sistema</div>
    </div>

    <a class="btn btn-outline" href="${pageContext.request.contextPath}/logout">Sair</a>
</div>

<div class="card">
    <div class="card-header">
        <div class="card-title">Dados da Conta</div>
    </div>

    <div class="profile-grid">
        <div class="profile-row">
            <span class="profile-label">ID</span>
            <span class="profile-value"><%= usuario != null && usuario.getId() != null ? usuario.getId() : "-" %></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">Nome</span>
            <span class="profile-value"><%= usuario != null && usuario.getNome() != null ? usuario.getNome() : "-" %></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">E-mail</span>
            <span class="profile-value"><%= usuario != null && usuario.getEmail() != null ? usuario.getEmail() : "-" %></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">Perfil</span>
            <span class="profile-value">
                <span class="badge badge-lav profile-role-badge"><%= usuario != null && usuario.getRole() != null ? usuario.getRole() : "-" %></span>
            </span>
        </div>
        <% if (mostrarTelefoneTutor) { %>
        <div class="profile-row">
            <span class="profile-label">Telefone</span>
            <span class="profile-value"><%= telefoneValue != null && !telefoneValue.isBlank() ? telefoneValue : "-" %></span>
        </div>
        <% } %>
    </div>
</div>

<% if (mostrarFormContato) { %>
<div class="card">
    <div class="card-header">
        <div class="card-title profile-card-title">
            <span class="profile-card-icon"><%= mostrarTelefoneTutor ? "📞" : "✉️" %></span>
            <span><%= mostrarTelefoneTutor ? "Contato do Tutor" : "E-mail" %></span>
        </div>
    </div>

    <div class="profile-form-wrap">

    <% if (request.getAttribute("erro") != null) { %>
    <p class="profile-feedback auth-error"><%= request.getAttribute("erro") %></p>
    <% } %>

    <% if (request.getAttribute("sucesso") != null) { %>
    <p class="profile-feedback auth-success"><%= request.getAttribute("sucesso") %></p>
    <% } %>

    <form action="${pageContext.request.contextPath}/perfil" method="post" class="profile-form">
        <%@ include file="components/csrf_token.jsp" %>

        <div class="form-row single">
            <div class="form-group">
                <label for="email">E-mail</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    placeholder="seuemail@exemplo.com"
                    value="<%= emailValue != null ? emailValue : (usuario != null && usuario.getEmail() != null ? usuario.getEmail() : "") %>"
                    required
                />
            </div>
        </div>

        <% if (mostrarTelefoneTutor) { %>
        <div class="form-row single">
            <div class="form-group">
                <label for="telefone">Telefone</label>
                <input
                    type="tel"
                    name="telefone"
                    id="telefone"
                    placeholder="(21) 99999-9999"
                    maxlength="15"
                    pattern="\(\d{2}\)\s\d{4,5}-\d{4}"
                    value="<%= telefoneValue != null ? telefoneValue : "" %>"
                    required
                />
                <small class="text-muted">Formato: (DDD) 99999-9999</small>
            </div>
        </div>
        <% } %>

        <div class="form-actions">
            <button class="btn btn-submit profile-submit">Salvar informações de contato</button>
        </div>
    </form>

    <% if (mostrarTelefoneTutor) { %>
        <form action="${pageContext.request.contextPath}/perfil" method="post"
            class="mt-12 js-confirm-submit"
            data-confirm-message="Tem certeza que deseja excluir sua conta? Essa ação não pode ser desfeita.">
        <%@ include file="components/csrf_token.jsp" %>
        <input type="hidden" name="acao" value="deletarConta"/>
        <button type="submit" class="btn btn-danger">Excluir minha conta</button>
    </form>
    <% } %>
    </div>
</div>
<% } %>

</main>
<script src="${pageContext.request.contextPath}/scripts/perfil.js" defer></script>
</body>
</html>
