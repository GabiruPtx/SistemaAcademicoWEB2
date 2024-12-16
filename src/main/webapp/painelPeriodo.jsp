<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Períodos</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        .periodo-header {
            background-color: #e0e0e0;
            font-weight: bold;
        }
        .disciplina-header {
            background-color: #f8f8f8;
        }
        .aprovado {
            color: green;
        }
        .reprovado {
            color: red;
        }
    </style>
</head>
<body>
<h1 style="text-align: center;">Lista de Períodos Acadêmicos</h1>

<c:forEach var="periodo" items="${periodos}">
    <div class="periodo">
        <h2 class="periodo-header">
            Período: ${periodo.nome} (${periodo.dataInicio} - ${periodo.dataFim})
        </h2>

        <c:forEach var="disciplinaOfertada" items="${periodo.disciplinas}">
            <div class="disciplina">
                <h3 class="disciplina-header">
                    Disciplina: ${disciplinaOfertada.disciplina.nome}
                </h3>

                <table>
                    <thead>
                    <tr>
                        <th>Aluno</th>
                        <th>Nota 1</th>
                        <th>Nota 2</th>
                        <th>Média</th>
                        <th>Situação</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="matricula"
                               items="${matriculasPorDisciplina[disciplinaOfertada.id]}">
                        <tr>
                            <td>${matricula.aluno.nome}</td>
                            <td>${matricula.nota1}</td>
                            <td>${matricula.nota2}</td>
                            <td>
                                <c:if test="${not empty matricula.nota1 and not empty matricula.nota2}">
                                    <fmt:formatNumber value="${(matricula.nota1 + matricula.nota2) / 2}"
                                                      maxFractionDigits="1"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty matricula.nota1 and not empty matricula.nota2}">
                                    <c:choose>
                                        <c:when test="${(matricula.nota1 + matricula.nota2) / 2 >= 6.0}">
                                            <span class="aprovado">Aprovado</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="reprovado">Reprovado</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:forEach>
    </div>
</c:forEach>

<div style="text-align: center; margin: 20px;">
    <a href="adicionarPeriodo.jsp"><button type="button">Novo Período</button></a>
    <a href="paginaPrincipal.jsp"><button type="button">Voltar</button></a>
</div>
</body>
</html>