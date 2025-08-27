<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký</title>
</head>
<body>
<h2>Đăng ký</h2>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
    <label>Username: <input type="text" name="username" required></label><br/>
    <label>Password: <input type="password" name="password" required></label><br/>
    <label>Email: <input type="email" name="email" required></label><br/>
    <button type="submit">Đăng ký</button>
    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </form>
</body>
</html>


