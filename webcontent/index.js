<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page language="java" %>
    <!DOCTYPE html>
<html lang="pt-BR">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bem-vindo a FDG</title>
<style>
    body {
    margin: 0;
    font-family: Arial, sans-serif;
    text-align: center;
    background-color: #d884d6;
}

    h1 {
    color: white;
    font-size: 2.5em;
    margin-top: 50px;
}

    .line {
    width: 60%;
    height: 5px;
    background: linear-gradient(to right, #d884d6, white);
    margin: 20px auto;
}

    .button-container {
    background-color: #0a3d91;
    border-radius: 10px;
    width: 80%;
    max-width: 600px;
    margin: 30px auto;
    padding: 30px;
    display: flex;
    justify-content: space-around;
    align-items: center;
}

    .button {
    background-color: white;
    border: none;
    border-radius: 5px;
    padding: 15px 30px;
    font-size: 1.2em;
    font-weight: bold;
    color: #0a3d91;
    cursor: pointer;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);
    transition: transform 0.2s, background-color 0.2s;
}

    .button:hover {
    background-color: #f1f1f1;
    transform: scale(1.1);
}

    .label {
    color: white;
    font-size: 1.2em;
    margin: 20px;
}
</style>
</head>
<body>
<h1>Bem-vindo a FDG</h1>
<div class="line"></div>
<div class="label">VOCÊ É:</div>
<div class="button-container">
    <form action="RoleController" method="post">
        <input type="hidden" name="role" value="professor">
            <button type="submit" class="button">PROFESSOR</button>
    </form>
    <form action="RoleController" method="post">
        <input type="hidden" name="role" value="administrador">
            <button type="submit" class="button">ADMINISTRADOR</button>
    </form>
</div>
</body>
</html>
