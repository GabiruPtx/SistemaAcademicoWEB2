<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Cadastrar Disciplina</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h2>Cadastrar Nova Disciplina</h2>

    <form id="disciplinaForm" action="Disciplinas.do" method="post">
        <div class="form-group">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" required>
        </div>

        <div class="form-group">
            <label for="codigo">Código:</label>
            <input type="text" id="codigo" name="codigo" required>
        </div>

        <button type="submit">Cadastrar</button>
    </form>
    <button type="button"><a href="paginaPrincipal.jsp">Voltar</a></button>

    <h3>Disciplinas Cadastradas</h3>
    <table>
        <thead>
        <tr>
            <th>Código</th>
            <th>Nome</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${disciplinas}" var="disciplina">
            <tr>
                <td>${disciplina.codigo}</td>
                <td>${disciplina.nome}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>