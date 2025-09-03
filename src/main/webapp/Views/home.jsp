<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
</head>
<body>
<c:choose>
    <c:when test="${empty sessionScope.currentUser}">
        <c:redirect url="${pageContext.request.contextPath}/login"/>
    </c:when>
    <c:otherwise>
        <h2>Xin chào, ${sessionScope.currentUser.username}!</h2>
        <p>Email: ${sessionScope.currentUser.email}</p>
        <p><a href="${pageContext.request.contextPath}/change-password">Đổi mật khẩu</a></p>
        <p><a href="${pageContext.request.contextPath}/admin/category/list">Quản trị danh mục</a></p>
        <p><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></p>
    </c:otherwise>
</c:choose>
</body>
</html>


