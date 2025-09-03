# Dự án Java Web - Hệ thống đăng nhập/đăng ký

## Mô tả
Đây là một ứng dụng Java Web đơn giản với các chức năng:
- Đăng ký tài khoản
- Đăng nhập/Đăng xuất
- Quên mật khẩu
- Đổi mật khẩu (khi đã đăng nhập)
- Ghi nhớ đăng nhập bằng cookie

## Công nghệ sử dụng
- Java Servlet (Jakarta EE)
- JSP (JavaServer Pages)
- SQL Server
- Maven

## Cấu trúc dự án
```
src/main/java/org/example/trangdangnhap/
├── dao/                    # Data Access Object
│   ├── UserDAO.java       # Interface cho User DAO
│   └── impl/
│       └── UserDAOImpl.java # Implementation của User DAO
├── model/
│   └── User.java          # Model User
├── service/                # Business Logic Layer
│   ├── UserService.java   # Interface cho User Service
│   └── impl/
│       └── UserServiceImpl.java # Implementation của User Service
├── util/
│   ├── DBConnection.java  # Kết nối database
│   └── DBConnectionTest.java # Test kết nối
└── UserController.java     # Servlet controller chính

src/main/webapp/
├── Views/
│   ├── login.jsp          # Trang đăng nhập
│   ├── register.jsp       # Trang đăng ký
│   ├── home.jsp           # Trang chủ (sau khi đăng nhập)
│   ├── forgot-password.jsp # Trang quên mật khẩu
│   └── change-password.jsp # Trang đổi mật khẩu
└── WEB-INF/
    └── web.xml            # Cấu hình web application
```

## Chức năng mới được thêm

### 1. Quên mật khẩu
- **URL**: `/forgot-password`
- **Chức năng**: Cho phép người dùng đổi mật khẩu chỉ bằng cách nhập email
- **Quy trình**:
  1. Người dùng nhập email
  2. Nhập mật khẩu mới
  3. Xác nhận mật khẩu mới
  4. Hệ thống kiểm tra email tồn tại và cập nhật mật khẩu
  5. Chuyển hướng về trang đăng nhập

### 2. Đổi mật khẩu (khi đã đăng nhập)
- **URL**: `/change-password`
- **Chức năng**: Cho phép người dùng đã đăng nhập đổi mật khẩu
- **Quy trình**:
  1. Người dùng nhập mật khẩu hiện tại
  2. Nhập mật khẩu mới
  3. Xác nhận mật khẩu mới
  4. Hệ thống kiểm tra mật khẩu hiện tại và cập nhật mật khẩu mới

## Cách sử dụng

### 1. Khởi chạy ứng dụng
```bash
mvn clean package
mvn tomcat7:run
```

### 2. Truy cập ứng dụng
- Mở trình duyệt và truy cập: `http://localhost:8080/trangdangnhap/`
- Ứng dụng sẽ tự động chuyển hướng đến trang đăng nhập

### 3. Sử dụng các chức năng
- **Đăng ký**: Click "Đăng ký" từ trang đăng nhập
- **Đăng nhập**: Nhập username và password
- **Quên mật khẩu**: Click "Quên mật khẩu?" từ trang đăng nhập
- **Đổi mật khẩu**: Click "Đổi mật khẩu" từ trang chủ (sau khi đăng nhập)

## Bảo mật
- Mật khẩu được mã hóa bằng SHA-256
- Kiểm tra session để bảo vệ các trang yêu cầu đăng nhập
- Validation dữ liệu đầu vào ở cả client và server

## Database
Cần có bảng `User` với cấu trúc:
```sql
CREATE TABLE [User] (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(255) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL,
    roleId INT
);
```

## Lưu ý
- Đảm bảo SQL Server đang chạy và có thể kết nối
- Cập nhật thông tin kết nối database trong `DBConnection.java` nếu cần
- Ứng dụng sử dụng Jakarta EE 6.0


