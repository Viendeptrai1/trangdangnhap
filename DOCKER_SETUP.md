# Docker Setup cho PostgreSQL - macOS ARM

## Yêu cầu hệ thống
- macOS với chip ARM (M1/M2/M3)
- Docker Desktop for Mac
- Maven (để build ứng dụng Java)

## 🎯 Tại sao PostgreSQL?

Dự án này sử dụng PostgreSQL thay vì SQL Server vì:
- ✅ Native ARM support trên macOS
- ✅ Hiệu suất tốt hơn trên ARM Mac
- ✅ Không gặp lỗi memory mapping
- ✅ Setup đơn giản hơn

## 🚀 Cài đặt và chạy PostgreSQL

### 1. Khởi động PostgreSQL container

```bash
# Chạy PostgreSQL container
./docker-scripts.sh start-db

# Hoặc sử dụng docker-compose trực tiếp
docker-compose up -d postgres
```

**Thông tin kết nối PostgreSQL:**
- Server: `localhost:5432`
- Database: `DBUser`
- Username: `postgres`
- Password: `postgres123`
- Connection String: `jdbc:postgresql://localhost:5432/DBUser`

### 2. Kiểm tra kết nối

```bash
# Kết nối vào PostgreSQL container
docker exec -it trangdangnhap-postgres psql -U postgres -d DBUser

# Hoặc sử dụng pgAdmin (khuyến nghị)
# Tải từ: https://www.pgadmin.org/download/
```

### 3. Kiểm tra trạng thái container
```bash
# Xem trạng thái
./docker-scripts.sh status

# Xem logs
./docker-scripts.sh logs postgres
```

## Chạy ứng dụng Java

### 1. Build ứng dụng
```bash
mvn clean package
```

### 2. Deploy lên Tomcat/Jetty
- Copy file `target/trangdangnhap-1.0-SNAPSHOT.war` vào thư mục `webapps` của Tomcat
- Hoặc sử dụng IDE để deploy

### 3. Truy cập ứng dụng
- URL: `http://localhost:8080/trangdangnhap-1.0-SNAPSHOT/`
- Đăng nhập với tài khoản mẫu:
  - Username: `admin`, Password: `123456`
  - Username: `user1`, Password: `123456`

## Quản lý Docker

### Dừng container
```bash
docker-compose down
```

### Xóa dữ liệu (reset database)
```bash
docker-compose down -v
docker-compose up -d sqlserver
```

### Backup database
```bash
# Tạo backup
./docker-scripts.sh backup

# Hoặc thủ công
docker exec trangdangnhap-postgres pg_dump -U postgres -d DBUser > ./backup/DBUser_$(date +%Y%m%d_%H%M%S).sql
```

### Restore database
```bash
# Restore từ backup
./docker-scripts.sh restore backup/DBUser_20240101_120000.sql

# Hoặc thủ công
docker exec -i trangdangnhap-postgres psql -U postgres -d DBUser < ./backup/DBUser_backup.sql
```

## Troubleshooting

### Container không khởi động được
```bash
# Kiểm tra logs
docker-compose logs sqlserver

# Kiểm tra tài nguyên hệ thống
docker system df
docker system prune  # Dọn dẹp nếu cần
```

### Lỗi kết nối từ ứng dụng Java
1. Kiểm tra container đang chạy: `docker-compose ps`
2. Kiểm tra port 5432: `netstat -an | grep 5432`
3. Kiểm tra firewall settings
4. Thử kết nối bằng pgAdmin trước

### Lỗi "FATAL: password authentication failed"
- Đảm bảo password đúng: `postgres123`
- Kiểm tra container đã khởi động hoàn toàn (thường mất 5-10 giây)

## Cấu hình nâng cao

### Thay đổi password
Sửa trong file `docker-compose.yml`:
```yaml
environment:
  - POSTGRES_PASSWORD=YourNewPassword123
```

### Thay đổi port
Sửa trong file `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Thay đổi port host từ 5432 thành 5433
```

### Thêm volume cho backup
```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
  - ./backup:/backup  # Thêm dòng này
```

## Lưu ý bảo mật
- Password mặc định chỉ dùng cho development
- Trong production, sử dụng password mạnh hơn
- Không expose port 5432 ra ngoài internet
- Sử dụng SSL/TLS trong production
