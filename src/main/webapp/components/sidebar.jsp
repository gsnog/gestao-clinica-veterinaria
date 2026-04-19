<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Detecta a URL atual para marcar o item ativo na sidebar
    String uri = request.getRequestURI();
%>

<aside class="sidebar">

    <div class="brand">
        <div class="brand-icon">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
        </div>
        <span class="brand-name">VetCare</span>
    </div>

    <span class="nav-section">Principal</span>

    <a class="nav-item <%= uri.endsWith("index.jsp") || uri.endsWith("/") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/">
        <span class="nav-icon">🏠</span> Dashboard
    </a>

    <a class="nav-item <%= uri.contains("/tutores") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/tutores">
        <span class="nav-icon">👤</span> Tutores
    </a>

    <a class="nav-item <%= uri.contains("/pets") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/pets">
        <span class="nav-icon">🐶</span> Pets
    </a>

    <a class="nav-item <%= uri.contains("/veterinarios") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/veterinarios">
        <span class="nav-icon">🩺</span> Veterinários
    </a>

    <a class="nav-item <%= uri.contains("/consultas") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/consultas">
        <span class="nav-icon">📋</span> Consultas
    </a>

    <div class="sidebar-footer">
        <div class="sidebar-footer-text">VetCare v1.0<br/>Gestão Veterinária – UFF</div>
    </div>

</aside>
