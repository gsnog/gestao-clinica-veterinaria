<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Consulta" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<main class="main">

<%
Consulta consulta = (Consulta) request.getAttribute("consulta");
%>

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon">📋</div>
    <div class="form-sidebar-title">Consulta</div>
</div>

<div class="form-body">

<div class="form-title">
    <%= consulta != null ? "Editar Consulta" : "Nova Consulta" %>
</div>

<form action="consultas" method="post">

<% if (consulta != null) { %>
<input type="hidden" name="id" value="<%= consulta.getId() %>"/>
<input type="hidden" name="acao" value="atualizar"/>
<% } %>

<div class="form-row">
    <div class="form-group">
        <label>Data e Hora</label>
        <input type="datetime-local" id="dataConsultaInput"
        value="<%= consulta != null ? consulta.getDataConsulta().toString().substring(0,16) : "" %>" required/>
        <input type="hidden" name="dataConsulta" id="dataConsultaHidden"/>
    </div>

    <div class="form-group">
        <label>Motivo</label>
        <input type="text" name="motivo" value="<%= consulta != null ? consulta.getMotivo() : "" %>" required/>
    </div>
</div>

<div class="form-row">
    <div class="form-group">
        <label>ID Pet</label>
        <input type="number" name="petId"
        value="<%= consulta != null && consulta.getPet()!=null ? consulta.getPet().getId() : "" %>" required/>
    </div>

    <div class="form-group">
        <label>ID Veterinário</label>
        <input type="number" name="vetId"
        value="<%= consulta != null && consulta.getVeterinario()!=null ? consulta.getVeterinario().getId() : "" %>" required/>
    </div>
</div>

<div class="form-actions">
    <a href="consultas" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>

<script>
const input = document.getElementById("dataConsultaInput");
const hidden = document.getElementById("dataConsultaHidden");

function setData() {
    if (input.value) hidden.value = input.value + ":00";
}

input.addEventListener("change", setData);
document.querySelector("form").addEventListener("submit", setData);
setData();
</script>

</body>
</html>