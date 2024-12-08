<!DOCTYPE html>
<html lang="pt-BR">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Administrador</title>
<link rel="stylesheet" href="styles/style.css">
</head>
<body>
<h1>Login Administrador</h1>
<div class="container">
    <form action="/loginAdmin" method="post">
        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" required placeholder="Digite seu e-mail">

            <label for="password">Senha:</label>
            <input type="password" id="password" name="password" required placeholder="Digite sua senha">

                <button type="submit">Entrar</button>
    </form>
    <button class="register-button" onclick="window.location.href='/admcadastro.jsp'">Não tem uma conta? Cadastre-se</button>
</div>
</body>
</html>
