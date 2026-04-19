<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>

<main class="main">

<%
Tutor tutor = (Tutor) request.getAttribute("tutor");
%>

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon">👤</div>
    <div class="form-sidebar-title">Tutor</div>
</div>

<div class="form-body">

<div class="form-title">
    <%= tutor != null ? "Editar Tutor" : "Novo Tutor" %>
</div>

<form action="tutores" method="post">

<% if (tutor != null) { %>
<input type="hidden" name="id" value="<%= tutor.getId() %>"/>
<input type="hidden" name="acao" value="editar"/>
<% } %>

<div class="form-row single">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomeTutor" value="<%= tutor != null ? tutor.getNome() : "" %>" required/>
    </div>
</div>

<div class="form-row single">
    <div class="form-group">
        <label>Telefone</label>
        <input type="text" name="telefoneTutor" value="<%= tutor != null ? tutor.getTelefone() : "" %>" required/>
    </div>
</div>

<div class="form-actions">
    <a href="tutores" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>
</body>
</html>