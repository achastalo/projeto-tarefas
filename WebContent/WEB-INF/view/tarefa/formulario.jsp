<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<!-- O form ao ser submetido, chama	uma	ação, um método	dentro de um @Controller que responde pela URL adicionaTarefa.
		Esse método precisa receber os dados da requisição e gravar a tarefa que o usuário informou na tela. -->
	<h3>Adicionar tarefas</h3>
	<form action="adicionaTarefa" method="post">
		Descrição: <br />
		<textarea name="descricao" rows="5" cols="100"></textarea>
		<br />
		<form:errors path="tarefa.descricao" cssStyle="color:red" />
		<br /> 
		<input type="submit" value="Adicionar">
	</form>
</body>
</html>