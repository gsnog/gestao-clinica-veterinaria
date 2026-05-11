<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isVetFiltro" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Veterinários</div>
        <div class="page-subtitle">Equipe médica da clínica</div>
    </div>
</div>

<c:if test="${isVetFiltro}">
<div class="filter-bar">
    <form action="veterinarios" method="get" class="filter-group js-search-filter-form">
        <label for="vetsBuscaInput">Busca</label>
        <input type="text" name="busca" id="vetsBuscaInput" class="js-search-filter-input" placeholder="Nome, CRMV, especialidade ou ID" value="<c:out value='${buscaParam}'/>" autocomplete="off"/>
        <button type="submit" class="btn btn-filter">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Limpar filtros</a>
    </form>
</div>
</c:if>

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
<c:if test="${not empty vetLogado}">
<tr>
    <td class="cap"><c:out value="${vetLogado.nome}"/></td>
    <td><c:out value="${vetLogado.crmv}"/></td>
    <td><c:out value="${vetLogado.especialidade}"/></td>
    <td class="table-id">#<c:out value="${vetLogado.id}"/></td>

    <td class="actions">
        <a class="btn btn-edit"
           href="veterinarios?acao=editar&id=${vetLogado.id}">Editar</a>

          <form method="post" action="veterinarios" class="inline-form js-confirm-submit"
              data-confirm-message="Tem certeza?">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="${vetLogado.id}"/>
            <button type="submit" class="btn btn-danger">Excluir</button>
        </form>
    </td>
</tr>
</c:if>

<c:forEach var="vet" items="${listaDeVeterinarios}">
<tr>
    <td class="cap"><c:out value="${vet.nome}"/></td>
    <td><c:out value="${vet.crmv}"/></td>
    <td><c:out value="${vet.especialidade}"/></td>
     <td class="table-id">#<c:out value="${vet.id}"/></td>

    <td>
        <div class="actions" title="O veterinário só pode alterar ou excluir o próprio perfil.">
            <span class="btn btn-edit btn-disabled"
                  aria-disabled="true">Editar</span>

            <span class="btn btn-danger btn-disabled"
                  aria-disabled="true">Excluir</span>
        </div>
    </td>
</tr>
</c:forEach>
</tbody>
</table>
</div>

</main>
</body>
</html>