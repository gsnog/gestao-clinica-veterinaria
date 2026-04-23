<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Veterinario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<main class="main">

<%
Veterinario vet = (Veterinario) request.getAttribute("veterinario");
%>

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon-form">
        <img src="${pageContext.request.contextPath}/images/vet.webp" alt="Pet"/>
    </div>
</div>

<div class="form-body">

<div class="form-title">
    <%= vet != null ? "Editar Veterinário" : "Novo Veterinário" %>
</div>

<form action="veterinarios" method="post" id="vetForm">

<% if (vet != null) { %>
<input type="hidden" name="id" value="<%= vet.getId() %>"/>
<input type="hidden" name="acao" value="atualizar"/>
<% } %>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomeVet"
               value="<%= vet != null ? vet.getNome() : "" %>" required/>
    </div>

    <div class="form-group">
        <label>CRMV</label>
        <input type="text"
               name="crmvVet"
               id="crmv"
               placeholder="CRMV-RJ 12345"
               maxlength="14"
               pattern="CRMV-[A-Z]{2} [0-9]{5}"
               title="Formato: CRMV-UF 12345"
               value="<%= vet != null ? vet.getCrmv() : "" %>"
               required/>
        <small>Formato: CRMV-UF 12345</small>
    </div>
</div>

<div class="form-row single">
    <div class="form-group">
        <label>Especialidade</label>

        <select name="especialidadeVet" id="especialidadeSelect" required>
            <option value="">Selecione uma especialidade</option>

            <option>Clinica Geral</option>
            <option>Cirurgia</option>
            <option>Dermatologia</option>
            <option>Cardiologia</option>
            <option>Ortopedia</option>
            <option>Oftalmologia</option>
            <option>Oncologia</option>
            <option>Neurologia</option>
            <option>Endocrinologia</option>
            <option>Exoticos</option>
            <option value="outro">Outros</option>
        </select>

        <input type="text"
               id="especialidadeCustom"
               name="especialidadeCustom"
               placeholder="Digite a especialidade"
               style="display:none; margin-top:8px;"/>
    </div>
</div>

<div class="form-actions">
    <a href="veterinarios" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>

<script>
const crmvInput = document.getElementById("crmv");

crmvInput.addEventListener("input", function(e) {
    let v = e.target.value.toUpperCase();

    // remove tudo que não for letra ou número
    v = v.replace(/[^A-Z0-9]/g, "");

    // remove "CRMV" se digitarem
    v = v.replace(/^CRMV/, "");

    let resultado = "CRMV-";

    let letras = v.replace(/[^A-Z]/g, "").substring(0, 2);
    resultado += letras;

    let numeros = v.replace(/[^0-9]/g, "").substring(0, 5);

    if (letras.length === 2) {
        resultado += " " + numeros;
    }

    e.target.value = resultado;
});
const select = document.getElementById("especialidadeSelect");
const custom = document.getElementById("especialidadeCustom");

select.addEventListener("change", () => {
    if (select.value === "outro") {
        custom.style.display = "block";
        custom.required = true;
    } else {
        custom.style.display = "none";
        custom.required = false;
    }
});

document.getElementById("vetForm").addEventListener("submit", function() {
    if (select.value === "outro") {
        select.value = custom.value;
    }
});
</script>

</body>
</html>