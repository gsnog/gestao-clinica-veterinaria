<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Veterinario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Veterinários</div>
        <div class="page-subtitle">Equipe médica da clínica</div>
    </div>

    <a class="btn btn-primary" href="${pageContext.request.contextPath}/form-veterinario.jsp">
        + Novo Veterinário
    </a>
</div>

<div class="card">
<table>
<thead>
<tr>
    <th>Nome</th>
    <th>CRMV</th>
    <th>Especialidade</th>
    <th>ID</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<%
List<Veterinario> lista = (List<Veterinario>) request.getAttribute("listaDeVeterinarios");

if (lista != null) {
    for (Veterinario v : lista) {
%>
<tr>
    <td><%= v.getNome() %></td>
    <td><%= v.getCrmv() %></td>
    <td><%= v.getEspecialidade() %></td>
     <td class="table-id">#<%= v.getId() %></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="veterinarios?acao=editar&id=<%= v.getId() %>">Editar</a>

        <a class="btn btn-danger"
           href="veterinarios?acao=deletar&id=<%= v.getId() %>">Excluir</a>
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