<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isVetFiltro" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

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

<c:if test="${isVetFiltro}">
<div class="filter-bar">
    <form action="pets" method="get" class="filter-group js-search-filter-form">
        <label for="petsBuscaInput">Busca</label>
        <input type="text" name="busca" id="petsBuscaInput" class="js-search-filter-input" placeholder="Nome ou ID do pet" value="${buscaParam}" autocomplete="off"/>
        <button type="submit" class="btn btn-filter">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets">Limpar filtros</a>
    </form>
</div>
</c:if>

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
<c:forEach var="pet" items="${listaDePets}">
<tr>
    <td>
        <div class="pet-cell">
            <div class="pet-avatar">🐾</div>
            <div>
                <div class="pet-name">${pet.nome}</div>
                <div class="table-id">#${pet.id}</div>
            </div>
        </div>
    </td>

    <td>${pet.raca}</td>

    <td>${datasNascimentoFormatadas[pet.id]}</td>

    <td>
        <c:if test="${not empty pet.tutor}">
            <span class="table-id">#${pet.tutor.id}</span>
        </c:if>
    </td>

    <td class="actions">
        <a class="btn btn-edit"
           href="pets?acao=editar&id=${pet.id}">Editar</a>

          <form method="post" action="pets" class="inline-form js-confirm-submit"
              data-confirm-message="Tem certeza?">
            <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="acao" value="deletar"/>
            <input type="hidden" name="id" value="${pet.id}"/>
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