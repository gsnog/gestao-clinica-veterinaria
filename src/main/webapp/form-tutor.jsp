<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>

<main class="main">

<%
Tutor tutor = (Tutor) request.getAttribute("tutor");
%>

<div class="form-card">

    <div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/tutor.webp" alt="Pet"/>
    </div>
</div>

    <div class="form-body">

        <div class="form-title">
            <%= tutor != null ? "Editar Tutor" : "Novo Tutor" %>
        </div>

        <form action="tutores" method="post">

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

<script>
const telefoneInput = document.getElementById("telefoneTutor");

if (telefoneInput) {
    telefoneInput.addEventListener("input", function(e) {
        let v = e.target.value.replace(/\D/g, "");

        if (v.length > 11) v = v.slice(0, 11);

        if (v.length <= 10) {
            v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
            v = v.replace(/(\d{4})(\d)/, "$1-$2");
        } else {
            v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
            v = v.replace(/(\d{5})(\d)/, "$1-$2");
        }

        e.target.value = v;
    });
}
</script>

</body>
</html>