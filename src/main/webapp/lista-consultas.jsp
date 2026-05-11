<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isVetConsulta" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Consultas</div>
        <div class="page-subtitle">Gerencie atendimentos realizados</div>
    </div>

    <c:if test="${isVetConsulta}">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas?acao=novo">
        + Nova Consulta
    </a>
    </c:if>
</div>

<c:if test="${isVetConsulta}">
<div class="filter-bar">

<form action="consultas" method="get" class="filter-group js-search-filter-form" id="consultaFiltroForm">
    <input type="hidden" name="acao" value="filtrar"/>
    <label for="consultaBuscaInput">Busca</label>
    <input type="text" name="busca" id="consultaBuscaInput" class="js-search-filter-input" placeholder="Nome do pet ou veterinário" value="<c:out value='${buscaParam}'/>" autocomplete="off"/>
    <label>Data</label>
    <input type="date" name="dataConsulta" id="filtroDataConsulta" value="<c:out value='${dataFiltroParam}'/>"/>
    <button type="submit" class="btn btn-filter">Filtrar</button>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas">Limpar filtros</a>
</form>

<c:if test="${not empty erroFiltroData}">
<span class="filter-feedback"><c:out value="${erroFiltroData}"/></span>
</c:if>

</div>
</c:if>

<div class="card">
<table>
<thead>
<tr>
    <th>Data</th>
    <th>Motivo</th>
    <th>Diagnóstico</th>
    <th>Pet</th>
    <th>Veterinário</th>
    <c:if test="${isVetConsulta}"><th>Ações</th></c:if>
</tr>
</thead>

<tbody>
<c:forEach var="consultaItem" items="${listaDeConsultas}">
<tr>
    <td><c:out value="${datasConsultaFormatadas[consultaItem.id]}"/></td>
    <td><c:out value="${consultaItem.motivo}"/></td>
    <td>
        <c:choose>
            <c:when test="${empty consultaItem.diagnostico}">-</c:when>
            <c:otherwise><c:out value="${consultaItem.diagnostico}"/></c:otherwise>
        </c:choose>
    </td>
    <td class="cap">
        <c:if test="${not empty consultaItem.pet}"><c:out value="${consultaItem.pet.nome}"/></c:if>
    </td>
    <td class="cap">
        <c:if test="${not empty consultaItem.veterinario}"><c:out value="${consultaItem.veterinario.nome}"/></c:if>
    </td>

    <c:if test="${isVetConsulta}">
    <td>
        <div class="actions">
            <a class="btn btn-edit"
               href="consultas?acao=editar&id=${consultaItem.id}">Editar</a>

            <form method="post" action="consultas" class="inline-form js-confirm-submit"
                data-confirm-message="Tem certeza?">
                <%@ include file="components/csrf_token.jsp" %>
                <input type="hidden" name="acao" value="deletar"/>
                <input type="hidden" name="id" value="${consultaItem.id}"/>
                <button type="submit" class="btn btn-danger">Excluir</button>
            </form>
        </div>
    </td>
    </c:if>
</tr>
</c:forEach>
</tbody>
</table>
</div>

</main>
<script src="${pageContext.request.contextPath}/scripts/consulta.js" defer></script>
</body>
</html>
