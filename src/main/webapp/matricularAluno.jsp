<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Matricular Aluno</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Matricular Aluno em Disciplina</h1>

    <c:if test="${empty periodoAtual}">
        <div class="alert alert-warning">
            Não há período letivo ativo no momento.
            <a href="paginaPrincipal.jsp"><button type="button">Voltar</button></a>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="alert alert-success">
                ${success}
        </div>
    </c:if>

    <c:if test="${not empty periodoAtual}">
        <div class="card">
            <div class="card-header">
                Período Atual: ${periodoAtual.nome}
            </div>
            <div class="card-body">
                <form action="MatricularAluno.do" method="post">
                    <div class="form-group">
                        <label for="matriculaAluno">Matrícula do Aluno:</label>
                        <input type="text" class="form-control" id="matriculaAluno" name="matriculaAluno" required>
                    </div>

                    <div class="form-group">
                        <label for="disciplinaOfertadaId">Disciplina:</label>
                        <select class="form-control" id="disciplinaOfertadaId" name="disciplinaOfertadaId" required>
                            <option value="">Selecione uma disciplina</option>
                            <c:forEach var="disciplina" items="${disciplinasOfertadas}">
                                <option value="${disciplina.id}">
                                        ${disciplina.disciplina.codigo} - ${disciplina.disciplina.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">Matricular</button>
                    <a href="paginaPrincipal.jsp"><button type="button">Voltar</button></a>
                </form>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>