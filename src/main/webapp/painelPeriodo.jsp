<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Períodos</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1 style="text-align: center;">Lista de Períodos Acadêmicos</h1>
<table>
    <thead>
    <tr>
        <th>Nome</th>
        <th>Data Início</th>
        <th>Data Fim</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="periodo" items="${periodos}">
        <tr>
            <td>${periodo.nome}</td>
            <td>${periodo.dataInicio}</td>
            <td>${periodo.dataFim}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div style="text-align: center; margin: 20px;">
    <a href="adicionarPeriodo.jsp">Adicionar Novo Período</a>
</div>
</body>
</html>