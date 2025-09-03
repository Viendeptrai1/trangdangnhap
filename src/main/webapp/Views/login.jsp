<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
<h2>Đăng nhập</h2>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<c:if test="${not empty message}">
    <p style="color:green">${message}</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
    <label>Username: <input type="text" name="username" required></label><br/>
    <label>Password: <input type="password" name="password" required></label><br/>
    <label><input type="checkbox" name="remember" value="true"> Ghi nhớ đăng nhập</label><br/>
    <button type="submit">Đăng nhập</button>
</form>

<p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
<p><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
</body>
</html>


