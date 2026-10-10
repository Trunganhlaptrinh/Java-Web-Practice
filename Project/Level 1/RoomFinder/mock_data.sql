USE room_finder;

DELETE FROM rental_requests;
DELETE FROM rooms;
DELETE FROM users;

-- Tạo 2 users
INSERT INTO users (id, username, password, full_name, phone, role) 
VALUES (1, 'chunha', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Chủ nhà Test', '0987654321', 'LANDLORD');

INSERT INTO users (id, username, password, full_name, phone, role) 
VALUES (2, 'nguoithue', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Người thuê Test', '0123456789', 'RENTER');

-- Tạo vài phòng cho chunha
INSERT INTO rooms (id, landlord_id, title, description, price, area, address, image_url, status) 
VALUES (1, 1, 'Phòng Trọ Ban Công Lớn', 'Phòng thoáng mát, có điều hòa nóng lạnh đầy đủ.', 3500000, 25.0, 'Khúc Thừa Dụ, Cầu Giấy', 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=800&q=80', 'AVAILABLE');

INSERT INTO rooms (id, landlord_id, title, description, price, area, address, image_url, status) 
VALUES (2, 1, 'Căn Hộ Mini Studio', 'Giờ giấc tự do, an ninh 24/7.', 5000000, 40.0, 'Triều Khúc, Thanh Xuân', 'https://images.unsplash.com/photo-1502672260266-1c1de2d93688?auto=format&fit=crop&w=800&q=80', 'AVAILABLE');

INSERT INTO rooms (id, landlord_id, title, description, price, area, address, image_url, status) 
VALUES (3, 1, 'Phòng Trọ Sinh Viên Giá Rẻ', 'Gần các trường đại học, tiện đi lại.', 2000000, 18.0, 'Đê La Thành, Đống Đa', 'https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&w=800&q=80', 'RENTED');

-- Tạo yêu cầu thuê phòng cho nguoithue (gửi tới room 1)
INSERT INTO rental_requests (id, room_id, renter_id, message, status)
VALUES (1, 1, 2, 'Chào anh/chị, em sinh viên năm nhất muốn thuê phòng này ạ.', 'PENDING');
