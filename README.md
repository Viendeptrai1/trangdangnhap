# Dự án Java Web - Hệ thống đăng nhập/đăng ký

## Mô tả
Đây là một ứng dụng Java Web đơn giản với các chức năng:
- Đăng ký tài khoản
- Đăng nhập/Đăng xuất
- Quên mật khẩu
- Đổi mật khẩu (khi đã đăng nhập)
- Ghi nhớ đăng nhập bằng cookie
- Quản trị danh mục (CRUD Category)

## Công nghệ sử dụng
- Java Servlet (Jakarta EE)
- JSP (JavaServer Pages)
- PostgreSQL
- Maven
- Docker

## Cấu trúc dự án
```
src/main/java/org/example/trangdangnhap/
├── dao/                    # Data Access Object
│   ├── UserDAO.java
│   ├── CategoryDAO.java
│   └── impl/
│       ├── UserDAOImpl.java
│       └── CategoryDAOImpl.java
├── model/
│   ├── User.java
│   └── Category.java
├── service/                # Business Logic Layer
│   ├── UserService.java
│   ├── CategoryService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       └── CategoryServiceImpl.java
├── util/
│   ├── DBConnection.java
│   └── DBConnectionTest.java
├── UserController.java     # Auth/Session, Home
└── CategoryController.java # CRUD Category

src/main/webapp/
├── Views/
│   ├── login.jsp
│   ├── register.jsp
│   ├── home.jsp
│   ├── forgot-password.jsp
│   ├── change-password.jsp
│   └── admin/
│       ├── list-category.jsp
│       ├── add-category.jsp
│       └── edit-category.jsp
└── WEB-INF/
    └── web.xml
```

## Chức năng mới được thêm

### 1) Quên mật khẩu
- URL: `/forgot-password`
- Quy trình: Nhập email → nhập & xác nhận mật khẩu mới → cập nhật

### 2) Đổi mật khẩu (đã đăng nhập)
- URL: `/change-password`
- Quy trình: Nhập mật khẩu hiện tại → mật khẩu mới → xác nhận → cập nhật

### 3) CRUD Category
- Controller: `CategoryController`
- Model/DAO/Service: `Category.java`, `CategoryDAO/CategoryDAOImpl`, `CategoryService/CategoryServiceImpl`
- Quan hệ: 1 User có nhiều Category (mỗi category gắn với `user_id`)
- Các URL:
  - Danh sách: `GET /admin/category/list` (lọc theo `currentUser`)
  - Thêm mới: `GET /admin/category/add` → `POST /admin/category/add` (gắn `user_id = currentUser.id`)
  - Sửa: `GET /admin/category/edit?id={cate_id}` → `POST /admin/category/edit` (kiểm tra quyền theo `user_id`)
  - Xóa: `GET /admin/category/delete?id={cate_id}` (kiểm tra quyền theo `user_id`)
- JSP:
  - `Views/admin/list-category.jsp`
  - `Views/admin/add-category.jsp`
  - `Views/admin/edit-category.jsp`
- Điều hướng nhanh từ trang chủ: link "Quản trị danh mục" trong `Views/home.jsp`
- Ghi chú: hiện tại trường `icons` nhập dạng text/url (chưa xử lý upload file)

## Cách sử dụng

### Build & chạy (ví dụ)
```bash
mvn clean package
# chạy theo cách bạn cấu hình server (Tomcat/Jetty/IDE)
```

### Truy cập ứng dụng
- Trang đăng nhập: `http://localhost:8080/.../login`
- Trang chủ (sau đăng nhập): `http://localhost:8080/.../home`
- Quên mật khẩu: `http://localhost:8080/.../forgot-password`
- Đổi mật khẩu: `http://localhost:8080/.../change-password`
- CRUD Category (danh sách): `http://localhost:8080/.../admin/category/list`

## Bảo mật
- Mật khẩu băm SHA-256 trước khi lưu DB
- Kiểm tra session cho trang cần đăng nhập

## Database

Dự án sử dụng PostgreSQL với Docker. Database sẽ được tự động tạo khi khởi động container.

Ví dụ tạo bảng `User` (PostgreSQL):
```sql
CREATE TABLE "User" (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  roleId INTEGER NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Ví dụ tạo bảng `Category` (PostgreSQL):
```sql
CREATE TABLE Category (
  cate_id SERIAL PRIMARY KEY,
  cate_name VARCHAR(255) NOT NULL,
  icons VARCHAR(255) NULL,
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_Category_User FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);
```

## Lưu ý
- Cấu hình kết nối PostgreSQL trong `util/DBConnection.java`
- Dự án dùng Jakarta Servlet API (annotation `@WebServlet`), `web.xml` chỉ để welcome-file
- Sử dụng Docker để chạy PostgreSQL: `./docker-scripts.sh start-db`


