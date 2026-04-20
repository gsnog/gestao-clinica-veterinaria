<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Consulta" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Pet" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Veterinario" %>
<%@ page import="java.util.List" %>

<main class="main">

<%
Consulta consulta = (Consulta) request.getAttribute("consulta");
List<Pet> pets = (List<Pet>) request.getAttribute("listaPets");
List<Veterinario> vets = (List<Veterinario>) request.getAttribute("listaVets");
%>

<div class="form-card">

<div class="form-sidebar">
        <div class="form-sidebar-icon-form">
            <img src="${pageContext.request.contextPath}/images/consulta.webp" alt="Pet"/>
        </div>
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
        <input type="text" name="motivo"
               value="<%= consulta != null ? consulta.getMotivo() : "" %>" required/>
    </div>
</div>

<div class="form-row">
    <!-- PET -->
    <div class="form-group">
        <label>Pet</label>
        <select name="petId" required>
            <option value="">Selecione um pet</option>

            <% if (pets != null) {
                for (Pet p : pets) { %>

                <option value="<%= p.getId() %>"
                    <%= consulta != null && consulta.getPet()!=null && p.getId() == consulta.getPet().getId() ? "selected" : "" %>>
                    #<%= p.getId() %> - <%= p.getNome() %>
                </option>

            <%  }
            } %>
        </select>
    </div>

    <!-- VETERINÁRIO -->
    <div class="form-group">
        <label>Veterinário</label>
        <select name="vetId" required>
            <option value="">Selecione um veterinário</option>

            <% if (vets != null) {
                for (Veterinario v : vets) { %>

                <option value="<%= v.getId() %>"
                    <%= consulta != null && consulta.getVeterinario()!=null && v.getId() == consulta.getVeterinario().getId() ? "selected" : "" %>>
                    #<%= v.getId() %> - <%= v.getNome() %>
                </option>

            <%  }
            } %>
        </select>
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