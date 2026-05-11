<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="uri" value="${pageContext.request.requestURI}" />
<c:set var="isVeterinario" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

<aside class="sidebar">

    <div class="brand">
        <div class="brand-icon">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
        </div>
        <span class="brand-name">VetCare</span>
    </div>

    <span class="nav-section">Principal</span>

    <c:if test="${isVeterinario}">
     <a class="nav-item ${fn:contains(uri, '/dashboard') ? 'active' : ''}"
         href="${pageContext.request.contextPath}/dashboard">
        <span class="nav-icon">🏠</span> Dashboard
    </a>
    </c:if>

    <a class="nav-item ${fn:contains(uri, '/pets') ? 'active' : ''}"
       href="${pageContext.request.contextPath}/pets">
        <span class="nav-icon">🐶</span> Pets
    </a>

    <a class="nav-item ${fn:contains(uri, '/consultas') ? 'active' : ''}"
       href="${pageContext.request.contextPath}/consultas">
        <span class="nav-icon">📋</span> Consultas
    </a>

    <c:if test="${isVeterinario}">
     <a class="nav-item ${fn:contains(uri, '/tutores') ? 'active' : ''}"
         href="${pageContext.request.contextPath}/tutores">
          <span class="nav-icon">👤</span> Tutores
     </a>

     <a class="nav-item ${fn:contains(uri, '/veterinarios') ? 'active' : ''}"
         href="${pageContext.request.contextPath}/veterinarios">
          <span class="nav-icon">🩺</span> Veterinários
     </a>
    </c:if>

     <span class="nav-section">Conta</span>

     <a class="nav-item ${fn:contains(uri, '/perfil') ? 'active' : ''}"
         href="${pageContext.request.contextPath}/perfil">
          <span class="nav-icon">🙍</span> Perfil
     </a>

     <a class="nav-item"
         href="${pageContext.request.contextPath}/logout">
          <span class="nav-icon">↩</span> Sair
     </a>

    <div class="sidebar-footer">
        <div class="sidebar-footer-text">VetCare v1.0<br/>Gestão Veterinária – UFF</div>
    </div>

</aside>