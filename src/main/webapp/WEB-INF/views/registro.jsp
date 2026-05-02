<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro - Clínica Veterinária</title>
</head>
<body>
    <h1>Cadastro de Usuário</h1>

    <form action="${pageContext.request.contextPath}/registro" method="post">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" required>

        <br><br>

        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" required>

        <br><br>

        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha" required minlength="6">

        <br><br>

        <label for="role">Tipo de usuário:</label>
        <select id="role" name="role" required>
            <option value="">Selecione</option>
            <option value="TUTOR">Tutor</option>
            <option value="VETERINARIO">Veterinário</option>
        </select>

        <br><br>

        <button type="submit">Cadastrar</button>
    </form>

    <p style="color:red;">
        ${erro}
    </p>

    <p style="color:green;">
        ${sucesso}
    </p>

    <p>
        Já tem conta?
        <a href="${pageContext.request.contextPath}/login">Entrar</a>
    </p>
</body>
</html>