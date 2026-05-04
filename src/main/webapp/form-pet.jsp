<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isTutorRole" value="${sessionScope.usuarioRole eq 'TUTOR'}" />
<c:set var="tutorUnico" value="${not empty listaTutores ? listaTutores[0] : null}" />

<main class="main">

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/pet.webp" alt="Pet"/>
    </div>
</div>

<div class="form-body">

<div class="form-title">
    <c:choose>
        <c:when test="${not empty pet}">Editar Pet</c:when>
        <c:otherwise>Novo Pet</c:otherwise>
    </c:choose>
</div>

<form action="pets" method="post" id="petForm" novalidate>
<%@ include file="components/csrf_token.jsp" %>

<c:if test="${not empty pet}">
<input type="hidden" name="id" value="${pet.id}"/>
<input type="hidden" name="acao" value="atualizar"/>
</c:if>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomePet" class="js-proper-name" value="${not empty pet ? pet.nome : ''}" required/>
    </div>

    <div class="form-group">
        <label>Raça</label>
        <input type="text" name="racaPet" value="${not empty pet ? pet.raca : ''}" required/>
    </div>
</div>

<div class="form-row">
    <div class="form-group">
        <label>Nascimento</label>
        <input type="date" name="dataNascimentoPet" value="${not empty pet and not empty pet.dataNascimento ? pet.dataNascimento : ''}" max="${dataMaxHoje}" required/>
    </div>

    <div class="form-group">
        <label>Tutor</label>
        <c:choose>
        <c:when test="${isTutorRole and not empty tutorUnico}">
            <input type="hidden" name="tutorId" value="${tutorUnico.id}"/>
            <input type="text" value="#${tutorUnico.id} - ${tutorUnico.nome}"
                     class="input-disabled" disabled/>
        </c:when>
        <c:otherwise>
        <input type="text" id="tutorIdInput"
               class="js-combobox-input"
               data-list-id="tutorOptions"
               data-hidden-target="tutorIdHidden"
               placeholder="Buscar e selecionar um tutor"
             value="${not empty pet and not empty pet.tutor ? '#' : ''}${not empty pet and not empty pet.tutor ? pet.tutor.id : ''}${not empty pet and not empty pet.tutor ? ' - ' : ''}${not empty pet and not empty pet.tutor ? pet.tutor.nome : ''}"
               autocomplete="off"/>
        <input type="hidden" name="tutorId" id="tutorIdHidden" value="${not empty pet and not empty pet.tutor ? pet.tutor.id : ''}"/>
        <datalist id="tutorOptions">
            <c:forEach var="tutor" items="${listaTutores}">
                <option value="#${tutor.id} - ${tutor.nome}" data-id="${tutor.id}"></option>
            </c:forEach>
        </datalist>
        </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="form-actions">
    <a href="pets" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>
<script src="${pageContext.request.contextPath}/scripts/consulta.js" defer></script>
<script src="${pageContext.request.contextPath}/scripts/form-pet.js" defer></script>
</body>
</html>