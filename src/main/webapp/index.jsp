<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Administrador</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <form action="Login.do" method="post">
        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" required placeholder="Digite seu e-mail">

        <label for="password">Senha:</label>
        <input type="password" id="password" name="password" required placeholder="Digite sua senha">

        <button type="submit">Entrar</button>
    </form>
</div>
</body>
</html>
