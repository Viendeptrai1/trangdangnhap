-- Tạo bảng User cho PostgreSQL
CREATE TABLE IF NOT EXISTS "User" (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    roleId INTEGER NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng Category
CREATE TABLE IF NOT EXISTS Category (
    cate_id SERIAL PRIMARY KEY,
    cate_name VARCHAR(255) NOT NULL,
    icons VARCHAR(255) NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Category_User FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

-- Tạo index để tối ưu hiệu suất
CREATE INDEX IF NOT EXISTS IX_User_username ON "User"(username);
CREATE INDEX IF NOT EXISTS IX_User_email ON "User"(email);
CREATE INDEX IF NOT EXISTS IX_Category_user_id ON Category(user_id);

-- Tạo function để tự động cập nhật updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Tạo trigger cho bảng User
CREATE TRIGGER update_user_updated_at BEFORE UPDATE ON "User"
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Tạo trigger cho bảng Category
CREATE TRIGGER update_category_updated_at BEFORE UPDATE ON Category
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
