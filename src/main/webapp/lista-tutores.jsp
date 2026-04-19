<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Tutores</div>
        <div class="page-subtitle">Gerencie os responsáveis pelos pets</div>
    </div>

    <a class="btn btn-primary" href="${pageContext.request.contextPath}/form-tutor.jsp">
        + Novo Tutor
    </a>
</div>

<div class="card">
<table>
<thead>
<tr>
    <th>Nome</th>
    <th>Telefone</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<%
List<Tutor> lista = (List<Tutor>) request.getAttribute("listaTutores");

if (lista != null) {
    for (Tutor t : lista) {
%>
<tr>
    <td><%= t.getNome() %></td>
    <td><%= t.getTelefone() %></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="tutores?acao=editar&id=<%= t.getId() %>">Editar</a>

        <a class="btn btn-danger"
           href="tutores?acao=deletar&id=<%= t.getId() %>">Excluir</a>
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