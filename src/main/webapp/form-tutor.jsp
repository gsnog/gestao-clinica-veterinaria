<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>

<%
Tutor tutor = (Tutor) request.getAttribute("tutor");
if (tutor == null) {
    response.sendRedirect(request.getContextPath() + "/tutores");
    return;
}
%>

<main class="main">

<div class="form-card">

    <div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/tutor.webp" alt="Pet"/>
    </div>
</div>

    <div class="form-body">

        <div class="form-title">
            Editar Tutor
        </div>

        <form action="tutores" method="post" id="tutorForm" novalidate>

        <%@ include file="components/csrf_token.jsp" %>

        <%@ include file="components/csrf_token.jsp" %>

        <% if (tutor != null) { %>
            <input type="hidden" name="id" value="<%= tutor.getId() %>"/>
            <input type="hidden" name="acao" value="editar"/>
        <% } %>

        <!-- NOME -->
        <div class="form-row single">
            <div class="form-group">
                <label>Nome</label>
                <input
                    type="text"
                    name="nomeTutor"
                    value="<%= tutor != null ? tutor.getNome() : "" %>"
                    required
                />
            </div>
        </div>

        <!-- TELEFONE -->
        <div class="form-row single">
            <div class="form-group">
                <label>Telefone</label>
                <input
                    type="tel"
                    name="telefoneTutor"
                    id="telefoneTutor"
                    placeholder="(21) 99999-9999"
                    maxlength="15"
                    pattern="\(\d{2}\)\s\d{4,5}-\d{4}"
                    value="<%= tutor != null ? tutor.getTelefone() : "" %>"
                    required
                />
                <small class="text-muted">Formato: (DDD) 99999-9999</small>
            </div>
        </div>

        <div class="form-actions">
            <a href="tutores" class="btn btn-outline">Cancelar</a>
            <button class="btn btn-submit">Salvar</button>
        </div>

        </form>
    </div>
</div>

</main>
<script src="${pageContext.request.contextPath}/scripts/form-tutor.js" defer></script>

</body>
</html>