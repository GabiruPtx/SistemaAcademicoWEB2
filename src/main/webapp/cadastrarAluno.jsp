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
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" required>
        </div>
        <div class="input-group">
            <label for="matricula">Matrícula:</label>
            <input type="text" id="matricula" name="matricula" required>
        </div>
        <button type="submit" class="button">Cadastrar Aluno</button>
    </form>
        <button type="button" class="button"><a href="paginaPrincipal.jsp">Voltar</a></button>
</div>
<script>
    document.getElementById('alunoForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);

    fetch('Alunos.do', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            if (response.ok) {
                // Limpa o formulário se o cadastro foi bem sucedido
                document.getElementById('alunoForm').reset();
            }
        })
        .catch(error => {
            alert('Erro ao processar a requisição: ' + error);
        });
});
</script>
</body>
</html>

