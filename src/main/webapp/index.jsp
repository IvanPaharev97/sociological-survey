<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Sociological survey</title>
	</head>
	<body>
		<form action="LoginServlet" method="post">
			<input type="hidden" name="command" value="login" />
			<span>Login:</span>
			<input type="text" name="login" value="" />
			<span>Password:</span>
			<input type="password" name="password" value="" />
			<input type="submit" value="Login!" />
		</form>
	</body>
</html>
