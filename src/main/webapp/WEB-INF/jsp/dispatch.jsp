<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dispatcher</title>
</head>
<body>

<h1>${message}</h1>

<form method="post" action="${pageContext.request.contextPath}/dispatch">
    <input type="text" name="message" placeholder="Enter message"/>
    <button type="submit">Send</button>
</form>

<h3>Change language:</h3>
<a href="${pageContext.request.contextPath}/lang?lang=en">English</a> |
<a href="${pageContext.request.contextPath}/lang?lang=ru">Русский</a> |
<a href="${pageContext.request.contextPath}/lang?lang=kz">Қазақша</a>

</body>
</html>