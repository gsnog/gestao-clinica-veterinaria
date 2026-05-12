<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<c:set var="isVeterinario" value="${sessionScope.usuarioRole eq 'VETERINARIO'}" />

<main class="main">

    <div class="topbar">
        <div>
            <div class="page-title">Dashboard</div>
            <div class="page-subtitle">Visão geral do sistema</div>
        </div>
    </div>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon rose">🐶</div>
            <div>
                <div class="stat-number"><c:out value="${not empty estatisticas ? estatisticas.totalPets : 0}"/></div>
                <div class="stat-label">Pets</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon lav">👤</div>
            <div>
                <div class="stat-number"><c:out value="${not empty estatisticas ? estatisticas.totalTutores : 0}"/></div>
                <div class="stat-label">Tutores</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon sand">🩺</div>
            <div>
                <div class="stat-number"><c:out value="${not empty estatisticas ? estatisticas.totalVeterinarios : 0}"/></div>
                <div class="stat-label">Veterinários</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon mint">📋</div>
            <div>
                <div class="stat-number"><c:out value="${not empty estatisticas ? estatisticas.totalConsultas : 0}"/></div>
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
            <c:if test="${isVeterinario}">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Tutores</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Veterinários</a>
            </c:if>
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
<script src="${pageContext.request.contextPath}/scripts/lightbox.js" defer></script>
</body>
</html>