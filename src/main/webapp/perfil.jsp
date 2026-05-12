<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="usuario" value="${requestScope.usuario}" />
<c:set var="telefoneValue" value="${requestScope.telefoneValue}" />
<c:set var="emailValue" value="${requestScope.emailValue}" />
<c:set var="mostrarTelefoneTutor" value="${not empty usuario and usuario.role eq 'TUTOR'}" />
<c:set var="mostrarFormContato" value="${not empty usuario and (usuario.role eq 'TUTOR' or usuario.role eq 'VETERINARIO')}" />

<main class="main">

<div class="topbar">
    <div>
        <div class="page-title">Meu Perfil</div>
        <div class="page-subtitle">Informacoes da sua conta no sistema</div>
    </div>

    <a class="btn btn-outline" href="${pageContext.request.contextPath}/logout">Sair</a>
</div>

<div class="card">
    <div class="card-header">
        <div class="card-title">Dados da Conta</div>
    </div>

    <div class="profile-grid">
        <div class="profile-row">
            <span class="profile-label">ID</span>
            <span class="profile-value"><c:out value="${not empty usuario and not empty usuario.id ? usuario.id : '-'}"/></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">Nome</span>
            <span class="profile-value"><c:out value="${not empty usuario and not empty usuario.nome ? usuario.nome : '-'}"/></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">E-mail</span>
            <span class="profile-value"><c:out value="${not empty usuario and not empty usuario.email ? usuario.email : '-'}"/></span>
        </div>
        <div class="profile-row">
            <span class="profile-label">Perfil</span>
            <span class="profile-value">
                <span class="badge badge-lav profile-role-badge"><c:out value="${not empty usuario and not empty usuario.role ? usuario.role : '-'}"/></span>
            </span>
        </div>
        <c:if test="${mostrarTelefoneTutor}">
        <div class="profile-row">
            <span class="profile-label">Telefone</span>
            <span class="profile-value"><c:out value="${not empty telefoneValue ? telefoneValue : '-'}"/></span>
        </div>
        </c:if>
    </div>
</div>

<c:if test="${mostrarFormContato}">
<div class="card">
    <div class="card-header">
        <div class="card-title profile-card-title">
            <span class="profile-card-icon"><c:out value="${mostrarTelefoneTutor ? '📞' : '✉️'}"/></span>
            <span><c:out value="${mostrarTelefoneTutor ? 'Contato do Tutor' : 'E-mail'}"/></span>
        </div>
    </div>

    <div class="profile-form-wrap">

    <c:if test="${not empty requestScope.erro}">
    <p class="profile-feedback auth-error"><c:out value="${requestScope.erro}"/></p>
    </c:if>

    <c:if test="${not empty requestScope.sucesso}">
    <p class="profile-feedback auth-success"><c:out value="${requestScope.sucesso}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/perfil" method="post" class="profile-form">
        <%@ include file="components/csrf_token.jsp" %>

        <div class="form-row single">
            <div class="form-group">
                <label for="email">E-mail</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    placeholder="seuemail@exemplo.com"
                    value="<c:out value='${not empty emailValue ? emailValue : (not empty usuario and not empty usuario.email ? usuario.email : "" )}'/>"
                    required
                />
            </div>
        </div>

        <c:if test="${mostrarTelefoneTutor}">
        <div class="form-row single">
            <div class="form-group">
                <label for="telefone">Telefone</label>
                <input
                    type="tel"
                    name="telefone"
                    id="telefone"
                    placeholder="(21) 99999-9999"
                    maxlength="15"
                    pattern="\(\d{2}\)\s\d{4,5}-\d{4}"
                    value="<c:out value='${not empty telefoneValue ? telefoneValue : ""}'/>"
                    required
                />
                <small class="text-muted">Formato: (DDD) 99999-9999</small>
            </div>
        </div>
        </c:if>

        <div class="form-actions">
            <button class="btn btn-submit profile-submit">Salvar informações de contato</button>
        </div>
    </form>

    <c:if test="${mostrarTelefoneTutor}">
        <form action="${pageContext.request.contextPath}/perfil" method="post"
            class="mt-12 js-confirm-submit"
            data-confirm-message="Tem certeza que deseja excluir sua conta? Essa ação não pode ser desfeita.">
        <%@ include file="components/csrf_token.jsp" %>
        <input type="hidden" name="acao" value="deletarConta"/>
        <button type="submit" class="btn btn-danger">Excluir minha conta</button>
    </form>
    </c:if>
    </div>
</div>
</c:if>

</main>
<script src="${pageContext.request.contextPath}/scripts/perfil.js" defer></script>
</body>
</html>
