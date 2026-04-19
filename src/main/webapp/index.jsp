<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>

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
        <div class="stat-number">—</div>
        <div class="stat-label">Pets</div>
    </div>
</div>

<div class="stat-card">
    <div class="stat-icon lav">👤</div>
    <div>
        <div class="stat-number">—</div>
        <div class="stat-label">Tutores</div>
    </div>
</div>

<div class="stat-card">
    <div class="stat-icon sand">🩺</div>
    <div>
        <div class="stat-number">—</div>
        <div class="stat-label">Veterinários</div>
    </div>
</div>

<div class="stat-card">
    <div class="stat-icon mint">📋</div>
    <div>
        <div class="stat-number">—</div>
        <div class="stat-label">Consultas</div>
    </div>
</div>

</div>

<div class="card">
<div class="card-header">
    <div class="card-title">Acesso rápido</div>
</div>

<div style="padding:20px; display:flex; gap:12px; flex-wrap:wrap;">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets">Pets</a>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/tutores">Tutores</a>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/veterinarios">Veterinários</a>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/consultas">Consultas</a>
</div>
</div>

</main>
</body>
</html>