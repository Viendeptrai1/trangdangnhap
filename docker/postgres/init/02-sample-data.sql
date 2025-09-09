-- Thêm dữ liệu mẫu cho testing
-- Mật khẩu gốc: "123456" -> hash: "e10adc3949ba59abbe56e057f20f883e"

-- Thêm user admin
INSERT INTO "User" (username, password, email, roleId) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@example.com', 1)
ON CONFLICT (username) DO NOTHING;

-- Thêm user thường
INSERT INTO "User" (username, password, email, roleId) 
VALUES ('user1', 'e10adc3949ba59abbe56e057f20f883e', 'user1@example.com', 2)
ON CONFLICT (username) DO NOTHING;

-- Thêm category mẫu cho admin
INSERT INTO Category (cate_name, icons, user_id) 
SELECT 'Công nghệ', '💻', id FROM "User" WHERE username = 'admin'
ON CONFLICT DO NOTHING;

-- Thêm category mẫu cho user1
INSERT INTO Category (cate_name, icons, user_id) 
SELECT 'Giải trí', '🎮', id FROM "User" WHERE username = 'user1'
ON CONFLICT DO NOTHING;
