## Chạy bằng Docker (khuyên dùng)

Chỉ cần cài **Docker Desktop**, không cần cài Java, Maven hoặc MongoDB trên máy.

Mở CMD và copy/paste:

```bat
git clone https://github.com/Trunganhlaptrinh/Java-Web-Practice.git
cd /d "Java-Web-Practice/Project/Level 2/ConcertTicketBooking"
docker compose up --build
```

Lần đầu build sẽ mất khoảng 2-3 phút để tải image Maven, Temurin JDK 21, MongoDB và build ứng dụng.

NẾU THẤY MongoDB nó chạy liên tục thì k phải lỗi đâu nhé! thành công và hoàn toàn bình thường

**http://localhost:8080**


## Tài khoản mẫu (DEMO)

| Loại tài khoản | Tên đăng nhập | Mật khẩu | Họ và tên / Quyền |
| :--- | :--- | :--- | :--- |
| **Khách hàng (Customer)** | `customer` | `customer123` | Nguyễn Văn A (`ROLE_CUSTOMER`) |
| **Quản trị viên (Admin)** | `admin` | `admin123` | Quản trị hệ thống (`ROLE_ADMIN`) |

* **Mã giảm giá thử nghiệm khi đặt vé**:
  * `VIP50K`: Giảm trực tiếp 50.000 ₫
  * `ANHTRAI10`: Giảm 10% tổng đơn


## Dừng ứng dụng

Nhấn `Ctrl+C` trong cửa sổ đang chạy Docker, sau đó dùng:

```bat
docker compose down
```


## Xóa database và chạy lại từ đầu

```bat
docker compose down -v
docker compose up --build
```
