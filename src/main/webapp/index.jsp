<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.dto.DashboardDTO" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

    <div class="topbar">
        <div>
            <div class="page-title">Dashboard</div>
            <div class="page-subtitle">Visão geral do sistema</div>
        </div>
    </div>

    <%
        DashboardDTO estatisticas = (DashboardDTO) request.getAttribute("estatisticas");
        long totalPets = estatisticas != null ? estatisticas.getTotalPets() : 0;
        long totalTutores = estatisticas != null ? estatisticas.getTotalTutores() : 0;
        long totalVeterinarios = estatisticas != null ? estatisticas.getTotalVeterinarios() : 0;
        long totalConsultas = estatisticas != null ? estatisticas.getTotalConsultas() : 0;
    %>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon rose">🐶</div>
            <div>
                <div class="stat-number"><%= totalPets %></div>
                <div class="stat-label">Pets</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon lav">👤</div>
            <div>
                <div class="stat-number"><%= totalTutores %></div>
                <div class="stat-label">Tutores</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon sand">🩺</div>
            <div>
                <div class="stat-number"><%= totalVeterinarios %></div>
                <div class="stat-label">Veterinários</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon mint">📋</div>
            <div>
                <div class="stat-number"><%= totalConsultas %></div>
                <div class="stat-label">Consultas</div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <div class="card-title">Acesso rápido</div>
        </div>
        <div class="quick-access">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets">Pets</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas">Consultas</a>
            <% if (isVeterinario) { %>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Tutores</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Veterinários</a>
            <% } %>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/perfil">Perfil</a>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <div class="card-title">Galeria VetCare</div>
        </div>
        <div class="gallery-wrapper">
            <div class="gallery">
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet01.jpg"/></div>
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet02.jpg"/></div>
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet03.jpg"/></div>
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet04.jpg"/></div>
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet05.jpg"/></div>
                <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet06.jpg"/></div>
            </div>

            <div class="gallery">
            <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet07.jpg"/></div>
                 <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet08.jpg"/></div>
                 <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet09.jpg"/></div>
                 <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet010.jpg"/></div>
                 <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet011.jpg"/></div>
                 <div class="gallery-item"><img src="${pageContext.request.contextPath}/images/pet012.jpg"/></div>
             </div>
        </div>
    </div>

    <div class="lightbox" id="lightbox">
        <button class="lightbox-close" id="lightbox-close">&times;</button>
        <img id="lightbox-img" src="" alt="foto ampliada"/>
    </div>

</main>
</body>
</html>