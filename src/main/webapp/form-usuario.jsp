<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="edicao" value="${not empty usuario}" />

<main class="main">

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/tutor.webp" alt="Usuário"/>
    </div>
</div>

<div class="form-body">

<div class="form-title">
    <c:choose>
        <c:when test="${edicao}">Editar Usuário</c:when>
        <c:otherwise>Novo Usuário</c:otherwise>
    </c:choose>
</div>

<c:if test="${not empty erro}">
    <p class="profile-feedback auth-error"><c:out value="${erro}"/></p>
</c:if>

<form action="usuarios" method="post" novalidate>
<%@ include file="components/csrf_token.jsp" %>
<input type="hidden" name="acao" value="${edicao ? 'atualizar' : 'criar'}"/>
<c:if test="${edicao}">
    <input type="hidden" name="id" value="${usuario.id}"/>
</c:if>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nome" class="js-proper-name"
               value="<c:out value='${usuario.nome}'/>" required/>
        <small>Nome e sobrenome.</small>
    </div>

    <div class="form-group">
        <label>E-mail</label>
        <input type="email" name="email"
               value="<c:out value='${usuario.email}'/>" required/>
    </div>
</div>

<div class="form-row ${edicao ? 'single' : ''}">
    <div class="form-group">
        <label>Papel</label>
        <select name="role" required>
            <option value="TUTOR" ${usuario.role eq 'TUTOR' ? 'selected' : ''}>Tutor</option>
            <option value="VETERINARIO" ${usuario.role eq 'VETERINARIO' ? 'selected' : ''}>Veterinário</option>
            <option value="ADMIN" ${usuario.role eq 'ADMIN' ? 'selected' : ''}>Administrador</option>
        </select>
    </div>

    <c:if test="${not edicao}">
    <div class="form-group">
        <label>Senha</label>
        <input type="password" name="senha" required/>
        <small>Mínimo 8 caracteres, com maiúscula, minúscula e número.</small>
    </div>
    </c:if>
</div>

<div class="form-actions">
    <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>

<c:if test="${edicao}">
<hr/>
<div class="form-title">Redefinir senha</div>
<form action="usuarios" method="post" novalidate
      class="js-confirm-submit" data-confirm-message="Redefinir a senha deste usuário?">
<%@ include file="components/csrf_token.jsp" %>
<input type="hidden" name="acao" value="resetarSenha"/>
<input type="hidden" name="id" value="${usuario.id}"/>

<div class="form-row single">
    <div class="form-group">
        <label>Nova senha</label>
        <input type="password" name="senha" required/>
        <small>Mínimo 8 caracteres, com maiúscula, minúscula e número.</small>
    </div>
</div>

<div class="form-actions">
    <button class="btn btn-danger">Redefinir senha</button>
</div>
</form>
</c:if>

</div>
</div>

</main>
</body>
</html>
