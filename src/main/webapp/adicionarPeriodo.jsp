<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Adicionar Novo Período</title>
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1 style="text-align: center;">Adicionar Novo Período</h1>

<c:if test="${not empty error}">
  <div class="error" style="color: red; text-align: center; margin-bottom: 20px;">
      ${error}
  </div>
</c:if>

<form action="Periodos.do" method="post" style="width: 70%; margin: auto;">
  <label for="nome">Nome do Período:</label><br>
  <input type="text" id="nome" name="nome" required><br><br>

  <label for="dataInicio">Data Início:</label><br>
  <input type="date" id="dataInicio" name="dataInicio" required><br><br>

  <label for="dataFim">Data Fim:</label><br>
  <input type="date" id="dataFim" name="dataFim" required><br><br>

  <fieldset>
    <legend>Disciplinas Ofertadas</legend>
    <div style="max-height: 200px; overflow-y: auto;">
      <c:forEach items="${disciplinas}" var="disciplina">
        <div>
          <input type="checkbox" id="disciplina${disciplina.id}"
                 name="disciplinasOfertadas" value="${disciplina.id}">
          <label for="disciplina${disciplina.id}">${disciplina.nome}</label>
        </div>
      </c:forEach>
    </div>
  </fieldset><br>

  <button type="submit" style="margin-top: 20px;">Salvar</button>
  <button type="button"><a href="paginaPrincipal.jsp">Voltar</a></button>

</form>
  <div style="text-align: center; margin-top: 20px;">
    <a href="Periodos.do">Voltar para a lista de períodos</a>
  </div>
</body>
</html>