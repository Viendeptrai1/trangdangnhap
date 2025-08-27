<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.trangdangnhap.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
</head>
<body>
<%
    User current = (User) session.getAttribute("currentUser");
    if (current == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<h2>Xin chào, <%= current.getUsername() %>!</h2>
<p>Email: <%= current.getEmail() %></p>
<p><a href="<%= request.getContextPath() %>/logout">Đăng xuất</a></p>
</body>
</html>


