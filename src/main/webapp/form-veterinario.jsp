<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<c:choose>
<c:when test="${not empty veterinario}">

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/vet.webp" alt="Pet"/>
    </div>
</div>

<div class="form-body">

<div class="form-title">
    Editar Veterinário
</div>

<form action="veterinarios" method="post" id="vetForm" novalidate>
<%@ include file="components/csrf_token.jsp" %>

<input type="hidden" name="id" value="${veterinario.id}"/>
<input type="hidden" name="acao" value="atualizar"/>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomeVet" class="js-proper-name"
               value="${veterinario.nome}" required/>
    </div>

    <div class="form-group">
        <label>CRMV</label>
        <input type="text"
               name="crmvVet"
               id="crmv"
               placeholder="CRMV-RJ 12345"
               maxlength="14"
               pattern="CRMV-[A-Z]{2} [0-9]{5}"
               title="Formato: CRMV-UF 12345"
               value="${veterinario.crmv}"
               required/>
        <small>Formato: CRMV-UF 12345</small>
    </div>
</div>

<div class="form-row single">
    <div class="form-group">
        <label>Especialidade</label>

        <select name="especialidadeVet" id="especialidadeSelect" required>
            <option value="">Selecione uma especialidade</option>

            <option>Clinica Geral</option>
            <option>Cirurgia</option>
            <option>Dermatologia</option>
            <option>Cardiologia</option>
            <option>Ortopedia</option>
            <option>Oftalmologia</option>
            <option>Oncologia</option>
            <option>Neurologia</option>
            <option>Endocrinologia</option>
            <option>Exoticos</option>
            <option value="outro">Outros</option>
        </select>

        <input type="text"
               id="especialidadeCustom"
               name="especialidadeCustom"
               placeholder="Digite a especialidade"
             class="is-hidden mt-8"/>
    </div>
</div>

<div class="form-actions">
    <a href="veterinarios" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</c:when>
<c:otherwise>
<div class="card">
    <div class="card-header">
        <div class="card-title">Veterinário não encontrado</div>
        <a href="${pageContext.request.contextPath}/veterinarios" class="btn btn-primary">Voltar</a>
    </div>
</div>
</c:otherwise>
</c:choose>

</main>
<script src="${pageContext.request.contextPath}/scripts/form-veterinario.js" defer></script>

</body>
</html>