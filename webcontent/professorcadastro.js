<!DOCTYPE html>
<html lang="pt-BR">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro Professor</title>
<link rel="stylesheet" href="styles/style.css">
</head>
<body>
<h1>Cadastro de Professor</h1>
<div class="container">
    <form action="/cadastro" method="post">
        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" required placeholder="Digite seu e-mail">

            <label for="password">Senha:</label>
            <input type="password" id="password" name="password" required placeholder="Digite sua senha">

                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required placeholder="Digite seu nome">

                    <button type="submit">Cadastrar</button>
    </form>
</div>
</body>
</html>
