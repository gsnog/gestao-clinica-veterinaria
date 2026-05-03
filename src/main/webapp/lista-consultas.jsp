<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Consulta" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    boolean isVetConsulta = "VETERINARIO".equals(session.getAttribute("usuarioRole"));
    String buscaParam = request.getParameter("busca") != null ? request.getParameter("busca") : "";
    String dataFiltroParam = request.getParameter("dataConsulta") != null ? request.getParameter("dataConsulta") : "";
%>


<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Consultas</div>
        <div class="page-subtitle">Gerencie atendimentos realizados</div>
    </div>

    <% if (isVetConsulta) { %>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas?acao=novo">
        + Nova Consulta
    </a>
    <% } %>
</div>

<% if (isVetConsulta) { %>
<!-- FILTROS -->
<div class="filter-bar">

<form action="consultas" method="get" class="filter-group">
    <input type="hidden" name="acao" value="filtrar"/>
    <label>Busca</label>
    <input type="text" name="busca" placeholder="Nome do pet ou veterinário" value="<%= buscaParam %>"/>
    <label>Data</label>
    <input type="date" name="dataConsulta" value="<%= dataFiltroParam %>"/>
    <button type="submit" class="btn btn-outline">Filtrar</button>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas">Limpar filtros</a>
</form>

</div>
<% } %>

<div class="card">
<table>
<thead>
<tr>
    <th>Data</th>
    <th>Motivo</th>
    <th>Pet</th>
    <th>Veterinário</th>
    <% if (isVetConsulta) { %><th>Ações</th><% } %>
</tr>
</thead>

<tbody>
<%
List<Consulta> lista = (List<Consulta>) request.getAttribute("listaDeConsultas");
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

if (lista != null) {
    for (Consulta c : lista) {
%>
<tr>
    <td><%= c.getDataConsulta() != null ? c.getDataConsulta().format(dtf) : "" %></td>
    <td><%= c.getMotivo() %></td>
    <td><%= c.getPet() != null ? c.getPet().getNome() : "" %></td>
    <td><%= c.getVeterinario() != null ? c.getVeterinario().getNome() : "" %></td>

    <% if (isVetConsulta) { %>
    <td>
        <div class="actions">
            <a class="btn btn-edit"
               href="consultas?acao=editar&id=<%= c.getId() %>">Editar</a>

            <form method="post" action="consultas" style="display:inline"
                  onsubmit="return confirm('Tem certeza?')">
                <%@ include file="components/csrf_token.jsp" %>
                <input type="hidden" name="acao" value="deletar"/>
                <input type="hidden" name="id" value="<%= c.getId() %>"/>
                <button type="submit" class="btn btn-danger">Excluir</button>
            </form>
        </div>
    </td>
    <% } %>
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