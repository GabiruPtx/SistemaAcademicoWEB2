<%--
  Created by IntelliJ IDEA.
  User: biele
  Date: 14/12/2024
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Adicionar Nota</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Adicionar Notas de Aluno</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <form action="Notas.do" method="post">
        <div class="form-group">
            <label for="aluno">Selecione o Aluno:</label>
            <select id="aluno" name="aluno_id" required class="form-control">
                <option value="">Selecione um Aluno</option>
                <c:forEach var="aluno" items="${alunos}">
                    <option value="${aluno.id}">${aluno.nome} - ${aluno.matricula}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="disciplina">Selecione a Disciplina:</label>
            <select id="disciplina" name="disciplina_id" required class="form-control">
                <option value="">Selecione uma Disciplina</option>
                <c:forEach var="disciplina" items="${disciplinasOfertadas}">
                    <option value="${disciplina.id}">
                            ${disciplina.disciplina.nome} - ${disciplina.periodo}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="nota1">Primeira Nota:</label>
            <input type="number" id="nota1" name="nota1" step="0.1" min="0" max="10" required class="form-control">
        </div>

        <div class="form-group">
            <label for="nota2">Segunda Nota:</label>
            <input type="number" id="nota2" name="nota2" step="0.1" min="0" max="10" required class="form-control">
        </div>

        <button type="submit" class="btn btn-primary">Cadastrar Notas</button>
    </form>
</div>
</body>
</html>
