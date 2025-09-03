<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
</head>
<body>
    <h2>Quên mật khẩu</h2>
    
    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
    <c:if test="${not empty message}">
        <p style="color:green">${message}</p>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/forgot-password">
        <label>Email: <input type="email" name="email" required></label><br/>
        <label>Mật khẩu mới: <input type="password" name="newPassword" required></label><br/>
        <label>Xác nhận mật khẩu: <input type="password" name="confirmPassword" required></label><br/>
        <button type="submit">Đổi mật khẩu</button>
    </form>
    
    <p><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
    <p><a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</body>
</html>
