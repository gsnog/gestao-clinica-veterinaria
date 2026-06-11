<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Usuários</div>
        <div class="page-subtitle">Gestão de contas da plataforma</div>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios?acao=novo">Novo usuário</a>
</div>

<c:if test="${not empty erro}">
    <p class="profile-feedback auth-error"><c:out value="${erro}"/></p>
</c:if>
<c:if test="${not empty sucesso}">
    <p class="profile-feedback auth-success"><c:out value="${sucesso}"/></p>
</c:if>

<div class="filter-bar">
    <form action="usuarios" method="get" class="filter-group js-search-filter-form">
        <label for="usuariosBuscaInput">Busca</label>
        <input type="text" name="busca" id="usuariosBuscaInput" class="js-search-filter-input"
               placeholder="Nome, e-mail, papel ou ID" value="<c:out value='${buscaParam}'/>" autocomplete="off"/>
        <button type="submit" class="btn btn-filter">Filtrar</button>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios">Limpar filtros</a>
    </form>
</div>

<div class="card">
<table>
<thead>
<tr>
    <th>Nome</th>
    <th>E-mail</th>
    <th>Papel</th>
    <th>ID</th>
    <th>Ações</th>
</tr>
</thead>

<tbody>
<c:forEach var="u" items="${listaUsuarios}">
<tr>
    <td class="cap"><c:out value="${u.nome}"/></td>
    <td><c:out value="${u.email}"/></td>
    <td><c:out value="${u.role}"/></td>
    <td class="table-id">#<c:out value="${u.id}"/></td>

    <td class="actions">
        <a class="btn btn-edit" href="usuarios?acao=editar&id=${u.id}">Editar</a>

        <c:choose>
            <c:when test="${u.id eq idLogado}">
                <span class="btn btn-danger btn-disabled" aria-disabled="true"
                      title="Você não pode excluir a própria conta.">Excluir</span>
            </c:when>
            <c:otherwise>
                <form method="post" action="usuarios" class="inline-form js-confirm-submit"
                      data-confirm-message="Excluir este usuário? Esta ação não pode ser desfeita.">
                    <%@ include file="components/csrf_token.jsp" %>
                    <input type="hidden" name="acao" value="deletar"/>
                    <input type="hidden" name="id" value="${u.id}"/>
                    <button type="submit" class="btn btn-danger">Excluir</button>
                </form>
            </c:otherwise>
        </c:choose>
    </td>
</tr>
</c:forEach>

<c:if test="${empty listaUsuarios}">
<tr>
    <td colspan="5">Nenhum usuário encontrado.</td>
</tr>
</c:if>
</tbody>
</table>
</div>

</main>
</body>
</html>
