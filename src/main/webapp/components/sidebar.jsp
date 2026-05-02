<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    // Detecta a URL atual para marcar o item ativo na sidebar
    String uri = request.getRequestURI();
    HttpSession sessao = request.getSession(false);
    String role = sessao != null ? (String) sessao.getAttribute("usuarioRole") : null;
    boolean isTutor = "TUTOR".equals(role);
    boolean isVeterinario = "VETERINARIO".equals(role);
%>

<aside class="sidebar">

    <div class="brand">
        <div class="brand-icon">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
        </div>
        <span class="brand-name">VetCare</span>
    </div>

    <span class="nav-section">Principal</span>

     <% if (isVeterinario) { %>
     <a class="nav-item <%= uri.contains("/dashboard") || uri.endsWith("index.jsp") || uri.endsWith("/") ? "active" : "" %>"
         href="${pageContext.request.contextPath}/dashboard">
        <span class="nav-icon">🏠</span> Dashboard
    </a>
     <% } %>

    <a class="nav-item <%= uri.contains("/pets") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/pets">
        <span class="nav-icon">🐶</span> Pets
    </a>

    <a class="nav-item <%= uri.contains("/consultas") ? "active" : "" %>"
       href="${pageContext.request.contextPath}/consultas">
        <span class="nav-icon">📋</span> Consultas
    </a>

     <% if (isVeterinario) { %>
     <a class="nav-item <%= uri.contains("/tutores") ? "active" : "" %>"
         href="${pageContext.request.contextPath}/tutores">
          <span class="nav-icon">👤</span> Tutores
     </a>

     <a class="nav-item <%= uri.contains("/veterinarios") ? "active" : "" %>"
         href="${pageContext.request.contextPath}/veterinarios">
          <span class="nav-icon">🩺</span> Veterinários
     </a>
     <% } %>

     <span class="nav-section">Conta</span>

     <a class="nav-item <%= uri.contains("/perfil") ? "active" : "" %>"
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
