<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Usuario" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    String telefoneValue = (String) request.getAttribute("telefoneValue");
    boolean mostrarTelefoneTutor = usuario != null && "TUTOR".equals(usuario.getRole());
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
            <span class="badge badge-lav"><%= usuario != null && usuario.getRole() != null ? usuario.getRole() : "-" %></span>
        </div>
        <% if (mostrarTelefoneTutor) { %>
        <div class="profile-row">
            <span class="profile-label">Telefone</span>
            <span class="profile-value"><%= telefoneValue != null && !telefoneValue.isBlank() ? telefoneValue : "-" %></span>
        </div>
        <% } %>
    </div>
</div>

<% if (mostrarTelefoneTutor) { %>
<div class="card">
    <div class="card-header">
        <div class="card-title profile-card-title">
            <span class="profile-card-icon">📞</span>
            <span>Contato do Tutor</span>
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

        <div class="form-actions">
            <button class="btn btn-submit profile-submit">Salvar telefone</button>
        </div>
    </form>
    </div>
</div>
<% } %>

</main>
<script>
const telefoneInput = document.getElementById("telefone");

if (telefoneInput) {
    telefoneInput.addEventListener("input", function(e) {
        let v = e.target.value.replace(/\D/g, "");

        if (v.length > 11) v = v.slice(0, 11);

        if (v.length <= 10) {
            v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
            v = v.replace(/(\d{4})(\d)/, "$1-$2");
        } else {
            v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
            v = v.replace(/(\d{5})(\d)/, "$1-$2");
        }

        e.target.value = v;
    });
}
</script>
</body>
</html>
