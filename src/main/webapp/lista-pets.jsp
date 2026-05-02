<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Pet" %>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Pets</div>
        <div class="page-subtitle">Gerencie os pets cadastrados</div>
    </div>

    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets?acao=novo">
        + Novo Pet
    </a>
</div>

<div class="card">
<table>
<thead>
<tr>
    <th>Pet</th>
    <th>Raça</th>
    <th>Nascimento</th>
    <th>Tutor</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<%
List<Pet> lista = (List<Pet>) request.getAttribute("listaDePets");

// meses em pt-BR sem ponto
String[] meses = {"jan", "fev", "mar", "abr", "mai", "jun",
                  "jul", "ago", "set", "out", "nov", "dez"};

if (lista != null) {
    for (Pet p : lista) {

        String dataFormatada = "";

        if (p.getDataNascimento() != null) {
            int dia = p.getDataNascimento().getDayOfMonth();
            int mes = p.getDataNascimento().getMonthValue();
            int ano = p.getDataNascimento().getYear();

            String diaFormatado = String.format("%02d", dia);

            dataFormatada = diaFormatado + " " + meses[mes - 1] + " " + ano;
        }
%>
<tr>
    <td>
        <div class="pet-cell">
            <div class="pet-avatar">🐾</div>
            <div>
                <div class="pet-name"><%= p.getNome() %></div>
                <div class="table-id">#<%= p.getId() %></div>
            </div>
        </div>
    </td>

    <td><%= p.getRaca() %></td>

    <td><%= dataFormatada %></td>

    <td>
        <% if (p.getTutor() != null) { %>
            <span class="table-id">#<%= p.getTutor().getId() %></span>
        <% } %>
    </td>

    <td class="actions">
        <a class="btn btn-edit"
           href="pets?acao=editar&id=<%= p.getId() %>">Editar</a>

        <form method="post" action="pets" style="display:inline"
              onsubmit="return confirm('Tem certeza?')">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="<%= p.getId() %>"/>
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