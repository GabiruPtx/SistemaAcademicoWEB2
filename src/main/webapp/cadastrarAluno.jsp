<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Administrador</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Cadastrar Aluno</h1>
    <form action="Alunos.do" method="POST">
        <div class="input-group">
            <label for="name">Nome:</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="input-group">
            <label for="matricula">Matrícula:</label>
            <input type="text" id="matricula" name="matricula" required>
        </div>
        <button type="submit" class="button">Cadastrar Aluno</button>
    </form>
        <button type="button" class="button"><a href="paginaPrincipal.jsp">Voltar</a></button>
</div>
</body>
</html>

