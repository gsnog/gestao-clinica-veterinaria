<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

<c:set var="dataConsultaInputValue" value="${not empty consulta and not empty consulta.dataConsulta ? fn:substring(consulta.dataConsulta, 0, 16) : ''}" />

<div class="form-card">

<div class="form-sidebar">
        <div class="form-sidebar-icon-form">
            <img src="${pageContext.request.contextPath}/images/consulta.webp" alt="Pet"/>
        </div>
</div>

<div class="form-body">

<div class="form-title">
    <c:choose>
        <c:when test="${not empty consulta}">Editar Consulta</c:when>
        <c:otherwise>Nova Consulta</c:otherwise>
    </c:choose>
</div>

<form action="consultas" method="post" id="consultaForm" novalidate>
<%@ include file="components/csrf_token.jsp" %>

<c:if test="${not empty consulta}">
<input type="hidden" name="id" value="${consulta.id}"/>
<input type="hidden" name="acao" value="atualizar"/>
</c:if>

<div class="form-row">
    <div class="form-group">
        <label>Data e Hora</label>
        <input type="datetime-local" id="dataConsultaInput"
        value="${dataConsultaInputValue}" required/>
        <input type="hidden" name="dataConsulta" id="dataConsultaHidden"/>
    </div>

    <div class="form-group">
        <label>Motivo</label>
        <input type="text" name="motivo"
               value="${not empty consulta ? consulta.motivo : ''}" required/>
    </div>
</div>

<div class="form-row">
    <!-- PET -->
    <div class="form-group">
        <label>Pet</label>
        <input type="text" id="petIdInput"
               class="js-combobox-input"
               data-list-id="petOptions"
               data-hidden-target="petIdHidden"
               placeholder="Buscar e selecionar um pet"
             value="${not empty consulta and not empty consulta.pet ? '#' : ''}${not empty consulta and not empty consulta.pet ? consulta.pet.id : ''}${not empty consulta and not empty consulta.pet ? ' - ' : ''}${not empty consulta and not empty consulta.pet ? consulta.pet.nome : ''}"
               autocomplete="off"/>
        <input type="hidden" name="petId" id="petIdHidden" value="${not empty consulta and not empty consulta.pet ? consulta.pet.id : ''}"/>
        <datalist id="petOptions">
            <c:forEach var="pet" items="${listaPets}">
            <option value="#${pet.id} - ${pet.nome}" data-id="${pet.id}"></option>
            </c:forEach>
        </datalist>
    </div>

    <!-- VETERINÁRIO -->
    <div class="form-group">
        <label>Veterinário</label>
        <input type="text" id="vetIdInput"
               class="js-combobox-input"
               data-list-id="vetOptions"
               data-hidden-target="vetIdHidden"
               placeholder="Buscar e selecionar um veterinário"
             value="${not empty consulta and not empty consulta.veterinario ? '#' : ''}${not empty consulta and not empty consulta.veterinario ? consulta.veterinario.id : ''}${not empty consulta and not empty consulta.veterinario ? ' - ' : ''}${not empty consulta and not empty consulta.veterinario ? consulta.veterinario.nome : ''}"
               autocomplete="off"/>
        <input type="hidden" name="vetId" id="vetIdHidden" value="${not empty consulta and not empty consulta.veterinario ? consulta.veterinario.id : ''}"/>
        <datalist id="vetOptions">
            <c:forEach var="vet" items="${listaVets}">
            <option value="#${vet.id} - ${vet.nome}" data-id="${vet.id}"></option>
            </c:forEach>
        </datalist>
    </div>

</div>

<div class="form-row single">
    <div class="form-group">
        <label>Diagnóstico / Comentários</label>
        <textarea name="diagnostico" placeholder="Registre observações do atendimento, diagnóstico e recomendações.">${not empty consulta and not empty consulta.diagnostico ? consulta.diagnostico : ''}</textarea>
    </div>
</div>

<div class="form-actions">
    <a href="consultas" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>
</main>
<script src="${pageContext.request.contextPath}/scripts/consulta.js" defer></script>
<script src="${pageContext.request.contextPath}/scripts/form-consulta.js" defer></script>
</body>
</html>