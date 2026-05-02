<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Consulta" %>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Consultas</div>
        <div class="page-subtitle">Gerencie atendimentos realizados</div>
    </div>

    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas?acao=novo">
        + Nova Consulta
    </a>
</div>

<!-- FILTROS -->
<div class="filter-bar">

<form action="consultas" method="get" class="filter-group">
    <input type="hidden" name="acao" value="buscarPorPet"/>
    <label>Pet</label>
    <input type="number" name="petId"/>
</form>

<form action="consultas" method="get" class="filter-group">
    <input type="hidden" name="acao" value="buscarPorData"/>
    <label>Vet</label>
    <input type="number" name="veterinarioId"/>
    <input type="date" name="dataConsulta"/>
</form>

</div>

<div class="card">
<table>
<thead>
<tr>
    <th>Data</th>
    <th>Motivo</th>
    <th>Pet</th>
    <th>Veterinário</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<%
List<Consulta> lista = (List<Consulta>) request.getAttribute("listaDeConsultas");

if (lista != null) {
    for (Consulta c : lista) {
%>
<tr>
    <td><%= c.getDataConsulta() %></td>
    <td><%= c.getMotivo() %></td>
    <td>#<%= c.getPet() != null ? c.getPet().getId() : "" %></td>
    <td>#<%= c.getVeterinario() != null ? c.getVeterinario().getId() : "" %></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="consultas?acao=editar&id=<%= c.getId() %>">Editar</a>

        <a class="btn btn-danger"
           href="consultas?acao=deletar&id=<%= c.getId() %>"
           onclick="return confirm('Tem certeza?')">
           Excluir
        </a>
    </td>
</tr>
<%
    }
}
%>
</tbody>
</table>
</div>

</main>
</body>
</html>