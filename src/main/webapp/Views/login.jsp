<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
<h2>Đăng nhập</h2>
<% String error = (String) request.getAttribute("error"); if (error != null) { %>
<p style="color:red"><%= error %></p>
<% } %>
<% String message = (String) request.getAttribute("message"); if (message != null) { %>
<p style="color:green"><%= message %></p>
<% } %>
<form method="post" action="<%= request.getContextPath() %>/login">
    <label>Username: <input type="text" name="username" required></label><br/>
    <label>Password: <input type="password" name="password" required></label><br/>
    <button type="submit">Đăng nhập</button>
</form>

<p>Chưa có tài khoản? <a href="<%= request.getContextPath() %>/register">Đăng ký</a></p>
</body>
</html>


