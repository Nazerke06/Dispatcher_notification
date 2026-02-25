<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
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
    <select name="channel">
        <option value="EMAIL">Email</option>
        <option value="SMS">SMS</option>
        <option value="WHATSAPP">WhatsApp</option>
    </select>
    <input type="text" name="to" placeholder="Recipient" required/>
    <input type="text" name="message" placeholder="Message" required/>
    <button type="submit">Add</button>
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
    <select name="strategy">
        <option value="fixed">Fixed Thread Pool</option>
        <option value="cached">Cached Thread Pool</option>
        <option value="single">Single Thread</option>
    </select>
    <input type="number" name="threads" placeholder="Threads (for fixed)" value="5"/>
    <button type="submit">Send</button>
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
