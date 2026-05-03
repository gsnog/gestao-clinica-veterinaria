<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<c:choose>
<c:when test="${not empty tutor}">

<div class="form-card">

    <div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/tutor.webp" alt="Pet"/>
    </div>
</div>

    <div class="form-body">

        <div class="form-title">
            Editar Tutor
        </div>

        <form action="tutores" method="post" id="tutorForm" novalidate>

        <%@ include file="components/csrf_token.jsp" %>
            <input type="hidden" name="id" value="${tutor.id}"/>
            <input type="hidden" name="acao" value="editar"/>

        <!-- NOME -->
        <div class="form-row single">
            <div class="form-group">
                <label>Nome</label>
                <input
                    type="text"
                    name="nomeTutor"
                    class="js-proper-name"
                    value="${tutor.nome}"
                    required
                />
            </div>
        </div>

        <!-- TELEFONE -->
        <div class="form-row single">
            <div class="form-group">
                <label>Telefone</label>
                <input
                    type="tel"
                    name="telefoneTutor"
                    id="telefoneTutor"
                    placeholder="(21) 99999-9999"
                    maxlength="15"
                    pattern="\(\d{2}\)\s\d{4,5}-\d{4}"
                    value="${tutor.telefone}"
                    required
                />
                <small class="text-muted">Formato: (DDD) 99999-9999</small>
            </div>
        </div>

        <div class="form-actions">
            <a href="tutores" class="btn btn-outline">Cancelar</a>
            <button class="btn btn-submit">Salvar</button>
        </div>

        </form>
    </div>
</div>

</c:when>
<c:otherwise>
<div class="card">
    <div class="card-header">
        <div class="card-title">Tutor não encontrado</div>
        <a href="${pageContext.request.contextPath}/tutores" class="btn btn-primary">Voltar</a>
    </div>
</div>
</c:otherwise>
</c:choose>

</main>
<script src="${pageContext.request.contextPath}/scripts/form-tutor.js" defer></script>

</body>
</html>