# Ứng dụng đăng nhập/đăng ký (Servlet + JSP + JDBC)

Ứng dụng mẫu nhiều tầng (DAO/Service/Controller) kết nối SQL Server, hỗ trợ đăng ký và đăng nhập. Mật khẩu được băm SHA-256 trước khi lưu DB (demo).

## Ảnh giao diện

### Đăng nhập
![Login](login.png)

### Đăng ký
![Register](register.png)

### Trang chủ
![Home](home.png)

## Yêu cầu
- JDK 17+ (khuyên dùng JDK 21+)
- Maven 3.9+
- SQL Server 2019+
- Máy chủ servlet hỗ trợ Jakarta EE 10 (Tomcat 10.1+) hoặc chạy trực tiếp từ IDE

## Cấu trúc chính
- Model: `org.example.trangdangnhap.model.User`
- DAO: `org.example.trangdangnhap.dao.UserDAO`, `dao.impl.UserDAOImpl`
- Service: `org.example.trangdangnhap.service.UserService`, `service.impl.UserServiceImpl`
- Controller: `org.example.trangdangnhap.UserController`
- JSP: `src/main/webapp/Views/login.jsp`, `Views/register.jsp`, `Views/home.jsp`

## Cấu hình DB
Sửa `src/main/java/org/example/trangdangnhap/util/DBConnection.java`:
```java
private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=DBUser;encrypt=false";
private static final String JDBC_USER = "sa";
private static final String JDBC_PASSWORD = "<your_password>";
```
Driver đã khai báo trong `pom.xml` (`com.microsoft.sqlserver:mssql-jdbc`).

## Tạo bảng User (khuyến nghị)
```sql
IF OBJECT_ID(N'dbo.User', N'U') IS NOT NULL DROP TABLE dbo.[User];
CREATE TABLE dbo.[User](
  id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  username NVARCHAR(50) NOT NULL UNIQUE,
  password NVARCHAR(64) NOT NULL, -- SHA-256 hex
  email NVARCHAR(50) NULL
);
```

## Build và chạy
```bash
mvn -DskipTests package
```
Triển khai WAR lên Tomcat 10.1+ hoặc chạy từ IDE. Welcome URL trỏ đến `/login`.

## Luồng sử dụng
- Đăng ký: `/register` (form tại `Views/register.jsp`)
- Đăng nhập: `/login` (form tại `Views/login.jsp`)
- Thành công sẽ vào `Views/home.jsp`; đăng xuất: `/logout`


## Test nhanh đăng ký thành công
- Username: `alice01`
- Password: `123456`
- Email: `alice01@example.com`
Nếu lỗi: kiểm tra `IDENTITY` cho cột `id`, `username` có trùng, và cấu hình `DBConnection` đúng.

## Ghi chú bảo mật
Demo dùng SHA-256 không có salt. Sản xuất nên dùng bcrypt/argon2 và HTTPS.


