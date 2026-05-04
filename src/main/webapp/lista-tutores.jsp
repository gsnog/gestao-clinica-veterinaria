<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isVetFiltro" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Tutores</div>
        <div class="page-subtitle">Gerencie os responsáveis pelos pets</div>
    </div>
</div>

<c:if test="${isVetFiltro}">
<div class="filter-bar">
    <form action="tutores" method="get" class="filter-group js-search-filter-form">
        <label for="tutoresBuscaInput">Busca</label>
        <input type="text" name="busca" id="tutoresBuscaInput" class="js-search-filter-input" placeholder="Nome, telefone ou ID" value="${buscaParam}" autocomplete="off"/>
        <button type="submit" class="btn btn-filter">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Limpar filtros</a>
    </form>
</div>
</c:if>

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
<c:forEach var="tutor" items="${listaTutores}">
<tr>
    <td class="cap">${tutor.nome}</td>
    <td>${tutor.telefone}</td>
     <td class="table-id">#${tutor.id}</td>

    <td class="actions">
        <a class="btn btn-edit"
           href="tutores?acao=editar&id=${tutor.id}">Editar</a>

          <form method="post" action="tutores" class="inline-form js-confirm-submit"
              data-confirm-message="Tem certeza?">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="${tutor.id}"/>
            <button type="submit" class="btn btn-danger">Excluir</button>
        </form>
    </td>
</tr>
</c:forEach>
</tbody>
</table>
</div>

</main>
</body>
</html>