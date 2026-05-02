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

        <form method="post" action="veterinarios" style="display:inline"
              onsubmit="return confirm('Tem certeza?')">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="<%= v.getId() %>"/>
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