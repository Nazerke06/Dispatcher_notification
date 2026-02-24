<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Dispatcher UI</title>
</head>
<body>

<h2>
    <c:choose>
        <c:when test="${lang == 'ru'}">Очередь уведомлений</c:when>
        <c:otherwise>Notification Queue</c:otherwise>
    </c:choose>
</h2>

<a href="${pageContext.request.contextPath}/lang?lang=ru">RU</a> |
<a href="${pageContext.request.contextPath}/lang?lang=en">EN</a>

<hr/>

<form action="${pageContext.request.contextPath}/add" method="post">
    <input type="text" name="message" placeholder="Message"/>
    <button type="submit">
        <c:choose>
            <c:when test="${lang == 'ru'}">Добавить</c:when>
            <c:otherwise>Add</c:otherwise>
        </c:choose>
    </button>
</form>

<br/>

<form action="${pageContext.request.contextPath}/gen" method="post">
    <input type="number" name="count" placeholder="N"/>
    <button type="submit">
        <c:choose>
            <c:when test="${lang == 'ru'}">Сгенерировать</c:when>
            <c:otherwise>Generate</c:otherwise>
        </c:choose>
    </button>
</form>

<br/>

<form action="${pageContext.request.contextPath}/send" method="post">
    <input type="number" name="threads" placeholder="Threads"/>
    <button type="submit">
        <c:choose>
            <c:when test="${lang == 'ru'}">Отправить</c:when>
            <c:otherwise>Send</c:otherwise>
        </c:choose>
    </button>
</form>

<hr/>

<h3>
    <c:choose>
        <c:when test="${lang == 'ru'}">Первые 20 в очереди:</c:when>
        <c:otherwise>First 20 in queue:</c:otherwise>
    </c:choose>
</h3>

<ul>
    <c:forEach var="item" items="${queue}">
        <li>${item}</li>
    </c:forEach>
</ul>

<hr/>

<h3>
    <c:choose>
        <c:when test="${lang == 'ru'}">Последняя статистика:</c:when>
        <c:otherwise>Last Statistics:</c:otherwise>
    </c:choose>
</h3>

<c:if test="${not empty stats}">
    <p>
        <c:choose>
            <c:when test="${lang == 'ru'}">
                Отправлено: ${stats.sent}, Ошибки: ${stats.failed}
            </c:when>
            <c:otherwise>
                Sent: ${stats.sent}, Failed: ${stats.failed}
            </c:otherwise>
        </c:choose>
    </p>
</c:if>

</body>
</html>