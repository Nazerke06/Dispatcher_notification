<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Dispatcher UI</title></head>
<body>
<h1>${welcome}</h1>

<p>Язык: <a href="/lang?lang=en">EN</a> | <a href="/lang?lang=ru">RU</a></p>

<h2>Добавить уведомление</h2>
<form action="/add" method="post">
    <select name="channel">
        <option value="SMS">SMS</option>
        <option value="EMAIL">EMAIL</option>
    </select>
    <input name="message" placeholder="Текст сообщения" style="width:300px"/>
    <button type="submit">${addButton}</button>
</form>

<h2>Сгенерировать</h2>
<form action="/gen" method="post">
    <input name="count" type="number" value="10"/>
    <button type="submit">${genButton}</button>
</form>

<h2>Отправить всё</h2>
<form action="/send" method="post">
    <input name="threads" type="number" value="8"/>
    <button type="submit">${sendButton}</button>
</form>

<h2>Очередь (первые 20)</h2>
<pre><c:forEach var="n" items="${queue}">${n}<br></c:forEach></pre>

<h2>Последняя статистика</h2>
<pre>${statsSummary}</pre>

</body>
</html>