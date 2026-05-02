<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Clínica Veterinária</title>
</head>
<body>
    <h1>Login</h1>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" required>

        <br><br>

        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha" required>

        <br><br>

        <button type="submit">Entrar</button>
    </form>

    <p style="color:red;">
        ${erro}
    </p>

    <p>
        Não tem conta?
        <a href="${pageContext.request.contextPath}/registro">Cadastre-se</a>
    </p>
    
</body>
</html>