<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>
<%
boolean isVetFiltro = "VETERINARIO".equals(session.getAttribute("usuarioRole"));
String buscaParam = request.getParameter("busca") != null ? request.getParameter("busca") : "";
String buscaTutor = buscaParam.trim().toLowerCase();
boolean filtroTutorAtivo = !buscaTutor.isEmpty();
%>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Tutores</div>
        <div class="page-subtitle">Gerencie os responsáveis pelos pets</div>
    </div>
</div>

<% if (isVetFiltro) { %>
<div class="filter-bar">
    <form action="tutores" method="get" class="filter-group">
        <label>Busca</label>
        <input type="text" name="busca" placeholder="Nome, telefone ou ID" value="<%= buscaParam %>"/>
        <button type="submit" class="btn btn-outline">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Limpar filtros</a>
    </form>
</div>
<% } %>

<div class="card">
<table>
<thead>
<tr>
    <th>Nome</th>
    <th>Telefone</th>
    <th>ID</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<%
List<Tutor> lista = (List<Tutor>) request.getAttribute("listaTutores");

if (lista != null) {
    for (Tutor t : lista) {
        boolean tutorPassaFiltro = !filtroTutorAtivo
            || (t.getNome() != null && t.getNome().toLowerCase().contains(buscaTutor))
            || (t.getTelefone() != null && t.getTelefone().toLowerCase().contains(buscaTutor))
            || String.valueOf(t.getId()).contains(buscaTutor);
        if (!tutorPassaFiltro) {
            continue;
        }
%>
<tr>
    <td><%= t.getNome() %></td>
    <td><%= t.getTelefone() %></td>
     <td class="table-id">#<%= t.getId() %></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="tutores?acao=editar&id=<%= t.getId() %>">Editar</a>

        <form method="post" action="tutores" style="display:inline"
              onsubmit="return confirm('Tem certeza?')">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="<%= t.getId() %>"/>
            <button type="submit" class="btn btn-danger">Excluir</button>
        </form>
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