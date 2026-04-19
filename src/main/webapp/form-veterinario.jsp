<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Veterinario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<main class="main">

<%
Veterinario vet = (Veterinario) request.getAttribute("veterinario");
%>

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon">🩺</div>
    <div class="form-sidebar-title">Veterinário</div>
</div>

<div class="form-body">

<div class="form-title">
    <%= vet != null ? "Editar Veterinário" : "Novo Veterinário" %>
</div>

<form action="veterinarios" method="post">

<% if (vet != null) { %>
<input type="hidden" name="id" value="<%= vet.getId() %>"/>
<input type="hidden" name="acao" value="atualizar"/>
<% } %>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomeVet" value="<%= vet != null ? vet.getNome() : "" %>" required/>
    </div>

    <div class="form-group">
        <label>CRMV</label>
        <input type="text" name="crmvVet" value="<%= vet != null ? vet.getCrmv() : "" %>" required/>
    </div>
</div>

<div class="form-row single">
    <div class="form-group">
        <label>Especialidade</label>
        <input type="text" name="especialidadeVet" value="<%= vet != null ? vet.getEspecialidade() : "" %>" required/>
    </div>
</div>

<div class="form-actions">
    <a href="veterinarios" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>
</body>
</html>