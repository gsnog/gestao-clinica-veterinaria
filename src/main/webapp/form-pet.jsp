<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="components/head.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Pet" %>
<%@ page import="com.uff.gestaoclinicaveterinaria.model.Tutor" %>
<%@ page import="java.util.List" %>


<main class="main">

<%
Pet pet = (Pet) request.getAttribute("pet");
%>

<div class="form-card">

<div class="form-sidebar">
    <div class="form-sidebar-icon-pet">
        <img src="${pageContext.request.contextPath}/images/pet.webp" alt="Pet"/>
    </div>
</div>

<div class="form-body">

<div class="form-title">
    <%= pet != null ? "Editar Pet" : "Novo Pet" %>
</div>

<form action="pets" method="post">

<% if (pet != null) { %>
<input type="hidden" name="id" value="<%= pet.getId() %>"/>
<input type="hidden" name="acao" value="atualizar"/>
<% } %>

<div class="form-row">
    <div class="form-group">
        <label>Nome</label>
        <input type="text" name="nomePet" value="<%= pet != null ? pet.getNome() : "" %>" required/>
    </div>

    <div class="form-group">
        <label>Raça</label>
        <input type="text" name="racaPet" value="<%= pet != null ? pet.getRaca() : "" %>" required/>
    </div>
</div>

<div class="form-row">
    <div class="form-group">
        <label>Nascimento</label>
        <input type="date" name="dataNascimentoPet" value="<%= pet != null ? pet.getDataNascimento().toString() : "" %>" required/>
    </div>

    <div class="form-group">
        <label>Tutor</label>
        <select name="tutorId" required>
            <option value="">Selecione um tutor</option>

            <%
            List<Tutor> tutores = (List<Tutor>) request.getAttribute("listaTutores");

            if (tutores != null) {
                for (Tutor t : tutores) {
            %>
                <option value="<%= t.getId() %>"
                    <%= pet != null && pet.getTutor()!=null && t.getId() == pet.getTutor().getId() ? "selected" : "" %>>
                    #<%= t.getId() %> - <%= t.getNome() %>
                </option>
            <%
                }
            }
            %>
        </select>
    </div>
</div>

<div class="form-actions">
    <a href="pets" class="btn btn-outline">Cancelar</a>
    <button class="btn btn-submit">Salvar</button>
</div>

</form>
</div>
</div>

</main>
</body>
</html>