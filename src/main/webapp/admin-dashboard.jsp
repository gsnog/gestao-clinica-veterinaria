<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main class="main">

    <div class="topbar">
        <div>
            <div class="page-title">Administração</div>
            <div class="page-subtitle">Visão geral da plataforma e contas</div>
        </div>
    </div>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon lav">👤</div>
            <div>
                <div class="stat-number"><c:out value="${totalTutores}"/></div>
                <div class="stat-label">Tutores</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon sand">🩺</div>
            <div>
                <div class="stat-number"><c:out value="${totalVeterinarios}"/></div>
                <div class="stat-label">Veterinários</div>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon rose">🛡️</div>
            <div>
                <div class="stat-number"><c:out value="${totalAdmins}"/></div>
                <div class="stat-label">Administradores</div>
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
            <div class="card-title">Gestão</div>
        </div>
        <div class="quick-access">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios">Gerenciar usuários</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Tutores</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Veterinários</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas">Consultas</a>
        </div>
    </div>

</main>
</body>
</html>
