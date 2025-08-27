<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký</title>
</head>
<body>
<h2>Đăng ký</h2>
<% String error = (String) request.getAttribute("error"); if (error != null) { %>
<p style="color:red"><%= error %></p>
<% } %>
<form method="post" action="<%= request.getContextPath() %>/register">
    <label>Username: <input type="text" name="username" required></label><br/>
    <label>Password: <input type="password" name="password" required></label><br/>
    <label>Email: <input type="email" name="email" required></label><br/>
    <button type="submit">Đăng ký</button>
    <a href="<%= request.getContextPath() %>/login">Đăng nhập</a>
    </form>
</body>
</html>


