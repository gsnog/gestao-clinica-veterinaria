<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Veterinario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
boolean isVetFiltro = "VETERINARIO".equals(session.getAttribute("usuarioRole"));
String buscaParam = request.getParameter("busca") != null ? request.getParameter("busca") : "";
String buscaVet = buscaParam.trim().toLowerCase();
boolean filtroVetAtivo = !buscaVet.isEmpty();
%>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Veterinários</div>
        <div class="page-subtitle">Equipe médica da clínica</div>
    </div>
</div>

<% if (isVetFiltro) { %>
<div class="filter-bar">
    <form action="veterinarios" method="get" class="filter-group">
        <label>Busca</label>
        <input type="text" name="busca" placeholder="Nome, CRMV, especialidade ou ID" value="<%= buscaParam %>"/>
        <button type="submit" class="btn btn-outline">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Limpar filtros</a>
    </form>
</div>
<% } %>

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
Long usuarioIdLogado = (Long) session.getAttribute("usuarioId");
Veterinario vetLogado = null;

if (lista != null && usuarioIdLogado != null) {
    for (Veterinario v : lista) {
        if (usuarioIdLogado.equals(v.getId())) {
            vetLogado = v;
            break;
        }
    }
}

if (lista != null) {
    if (vetLogado != null) {
        boolean vetLogadoPassaFiltro = !filtroVetAtivo
            || (vetLogado.getNome() != null && vetLogado.getNome().toLowerCase().contains(buscaVet))
            || (vetLogado.getCrmv() != null && vetLogado.getCrmv().toLowerCase().contains(buscaVet))
            || (vetLogado.getEspecialidade() != null && vetLogado.getEspecialidade().toLowerCase().contains(buscaVet))
            || String.valueOf(vetLogado.getId()).contains(buscaVet);
        if (vetLogadoPassaFiltro) {
%>
<tr>
    <td><%= vetLogado.getNome() %></td>
    <td><%= vetLogado.getCrmv() %></td>
    <td><%= vetLogado.getEspecialidade() %></td>
    <td class="table-id">#<%= vetLogado.getId() %></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="veterinarios?acao=editar&id=<%= vetLogado.getId() %>">Editar</a>

        <form method="post" action="veterinarios" style="display:inline"
              onsubmit="return confirm('Tem certeza?')">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="<%= vetLogado.getId() %>"/>
            <button type="submit" class="btn btn-danger">Excluir</button>
        </form>
    </td>
</tr>
<%
        }
    }

    for (Veterinario v : lista) {
        boolean isVetLogado = usuarioIdLogado != null && usuarioIdLogado.equals(v.getId());
        if (isVetLogado) {
            continue;
        }

        boolean vetPassaFiltro = !filtroVetAtivo
            || (v.getNome() != null && v.getNome().toLowerCase().contains(buscaVet))
            || (v.getCrmv() != null && v.getCrmv().toLowerCase().contains(buscaVet))
            || (v.getEspecialidade() != null && v.getEspecialidade().toLowerCase().contains(buscaVet))
            || String.valueOf(v.getId()).contains(buscaVet);
        if (!vetPassaFiltro) {
            continue;
        }
%>
<tr>
    <td><%= v.getNome() %></td>
    <td><%= v.getCrmv() %></td>
    <td><%= v.getEspecialidade() %></td>
     <td class="table-id">#<%= v.getId() %></td>

    <td>
        <div class="actions" title="O veterinário só pode alterar ou excluir o próprio perfil.">
            <span class="btn btn-edit"
                  style="opacity:.6; cursor:not-allowed;"
                  aria-disabled="true">Editar</span>

            <span class="btn btn-danger"
                  style="opacity:.6; cursor:not-allowed;"
                  aria-disabled="true">Excluir</span>
        </div>
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