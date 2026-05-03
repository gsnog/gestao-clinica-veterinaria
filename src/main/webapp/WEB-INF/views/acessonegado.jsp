<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Acesso negado</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="auth-body">
    <main class="auth-shell auth-shell-compact">
        <section class="auth-card auth-card-compact">
            <div class="auth-form-panel auth-form-panel-full">
                <h1 class="auth-title">Acesso negado</h1>
                <p class="auth-text">Você não tem permissão para acessar esse recurso.</p>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets">Voltar ao sistema</a>
            </div>
        </section>
    </main>
</body>
</html>