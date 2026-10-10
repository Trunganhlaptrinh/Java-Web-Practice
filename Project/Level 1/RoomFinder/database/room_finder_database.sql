-- ============================================
-- Room Finder - DB script (Level 1)
-- ============================================

DROP DATABASE IF EXISTS room_finder;
CREATE DATABASE room_finder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE room_finder;

-- ============================================
-- Bảng users: Lưu tài khoản người dùng
-- ============================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role ENUM('RENTER', 'LANDLORD') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Bảng rooms: Thông tin phòng trọ của Landlord
-- ============================================
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    landlord_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price BIGINT NOT NULL,
    area FLOAT NOT NULL,
    address VARCHAR(255) NOT NULL,
    image_url VARCHAR(500),
    status ENUM('AVAILABLE', 'NEGOTIATING', 'RENTED') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_room_landlord
        FOREIGN KEY (landlord_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- ============================================
-- Bảng rental_requests: Yêu cầu thuê/xem phòng từ Renter
-- ============================================
CREATE TABLE rental_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    renter_id INT NOT NULL,
    message TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_req_room
        FOREIGN KEY (room_id) REFERENCES rooms(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_req_renter
        FOREIGN KEY (renter_id) REFERENCES users(id)
        ON DELETE CASCADE,
    -- Tránh việc 1 Renter spam nhiều request vào 1 phòng cùng lúc
    CONSTRAINT uq_room_renter UNIQUE (room_id, renter_id)
);

-- ============================================
-- Dữ liệu mẫu (Tạo sẵn 1 Landlord và 1 Renter để test)
-- Mật khẩu mặc định đều là "123456" đã mã hóa SHA-256 (Tạm dùng hex đơn giản)
-- Ở bản thực tế Java sẽ tự hash ra mã tương ứng. Ở đây insert tạm plain text/hash mẫu
-- Mã hash SHA-256 của "123456" là "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
-- ============================================

INSERT INTO users (username, password, full_name, phone, role) VALUES
    ('chunha', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Trần Chủ Nhà', '0988111222', 'LANDLORD'),
    ('nguoithue', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Nguyễn Khách Thuê', '0912333444', 'RENTER');

INSERT INTO rooms (landlord_id, title, description, price, area, address, image_url, status) VALUES
    (1, 'Phòng trọ khép kín Cầu Giấy', 'Phòng mới xây, có điều hòa, bình nóng lạnh. Điện 4k/số, nước 30k/khối.', 3500000, 25, 'Ngõ 1, Cầu Giấy, Hà Nội', 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800', 'AVAILABLE'),
    (1, 'Phòng trọ mini Đống Đa', 'Phù hợp sinh viên, gần các trường đại học.', 2500000, 18, 'Ngõ 10, Đống Đa, Hà Nội', '', 'AVAILABLE');

