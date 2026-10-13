# THE SHOW (Concert Ticket Booking) — Project Status & Roadmap

**Current State**: Level 2 Architecture Complete & Verified  
**Runtime**: Live at `http://localhost:8080`  
**Test Suite**: 9/9 Tests Passing (`BUILD SUCCESS`)  

---

## 1. Implementation Progress Summary

| Phase / Step | Status | Key Deliverables & Achievements |
| :--- | :---: | :--- |
| **Step 1: Project Setup** | Completed | Spring Boot 3.2.0, Java 21, Maven POM, Directory Structure. |
| **Step 2: Database Layer** | Completed | MongoDB container (`mongodb-concert`), Models (`User`, `Concert`, `Seat`, `Booking`, `Ticket`, `Voucher`), Repositories, Ping connection test. |
| **Step 3: Security & Auth** | Completed | BCrypt, JWT Provider, JwtAuthenticationFilter, SecurityConfig with stateless session management. |
| **Step 4: Business Logic** | Completed | AuthService, ConcertService, BookingService (10-minute hold & atomicity), TicketService, EmailService (Mock + dynamic Gmail bot), BookingScheduler. |
| **Step 5: REST Endpoints** | Completed | AuthController, ConcertController, BookingController, TicketController, AdminController, Exception Handling. |
| **Step 6: Database Seeding** | Completed | Automatic Seeder with default Admin (`admin`/`admin123`) & Customer (`customer`/`customer123`), sample concert, and seat layout. |
| **Step 7: Frontend Overhaul** | Completed | Lumina Concert landing page integration with video background, white/black theme, top-center toasts, zero emojis/icons. |
| **Step 8: Brand Unification & Polish** | Completed | Replaced Tomcat favicon, unified single `logo-the-show.png`, cleared video fog, full Vietnamese accents, rich footer with full policy pages. |

---

## 2. Test Verification Evidence

```text
[INFO] Results:
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
- `MongoConnectionTest`: Validates live ping response from Docker MongoDB.
- `JwtTokenProviderTest`: Validates token creation, claim parsing, and expiration check.
- `ConcertControllerIntegrationTest`: Validates Customer Login API, Admin Login API, and Public Concert Fetch API.
- `BookingServiceIntegrationTest`: Validates seat holding, conflict detection, voucher application, and ticket generation.

---

## 3. Directory Layout & Key Files

```text
ConcertTicketBooking/
├── AGENTS.md                  # Durable instructions for AI agents & developers
├── ARCHITECTURE.md            # Architecture diagrams, data flows, and API specs
├── PROJECT_STATUS.md          # Current status, milestones, and roadmap
├── pom.xml                    # Maven dependencies (Java 21, Spring Boot 3)
└── src/
    ├── main/
    │   ├── java/com/concert/booking/
    │   │   ├── config/        # DatabaseSeeder, SchedulerConfig, WebConfig
    │   │   ├── controller/    # Auth, Concert, Booking, Ticket, Admin Controllers
    │   │   ├── dto/           # Request and Response DTOs
    │   │   ├── entity/        # MongoDB Document Entities
    │   │   ├── enums/         # BookingStatus, SeatStatus, Role, PaymentMethod
    │   │   ├── exception/     # GlobalExceptionHandler, AppException
    │   │   ├── repository/    # Spring Data Mongo Repositories
    │   │   ├── scheduler/     # BookingScheduler (auto-releases expired holds)
    │   │   ├── security/      # SecurityConfig, JwtTokenProvider, Filters
    │   │   └── service/       # Business Services & Implementations
    │   └── resources/
    │       ├── application.yml
    │       └── static/        # Frontend Client
    │           ├── index.html         # Landing page with video hero & rich footer
    │           ├── booking.html       # Visual seat map & 10-minute hold checkout
    │           ├── my-tickets.html    # Issued tickets with pure canvas QR code
    │           ├── login.html         # Customer/Admin login with demo quick-fill
    │           ├── register.html      # Account registration
    │           ├── admin.html         # Statistics, bookings list, email bot config
    │           ├── favicon.ico        # THE SHOW custom favicon
    │           ├── video.mp4          # High-definition concert hero video
    │           ├── still1-4.jpg       # Event posters
    │           ├── css/style.css      # White/Black design system
    │           ├── js/app.js          # Auth, JWT fetch, top-center toasts
    │           ├── js/qrcode.min.js   # Pure black/white canvas QR code generator
    │           ├── images/            # logo-the-show.png
    │           └── policies/          # Policy & Legal documents
    │               ├── privacy.html
    │               ├── terms.html
    │               ├── refund.html
    │               ├── payment.html
    │               └── support.html
```

---

## 4. Next Recommended Steps for Developers

1. **VNPAY / Payment Gateway Webhook (IPN)**:
   - Connect the mock payment method to real VNPAY or VietQR payment gateway sandbox with IPN callback verification.
2. **WebSocket Real-time Seat Occupancy**:
   - Implement STOMP / WebSocket channel to push seat status changes (`AVAILABLE` <-> `HELD` <-> `SOLD`) to all viewing clients in real-time.
3. **Staff Gate Check-in Scanner App**:
   - Create a mobile-friendly `/checkin.html` interface using device camera (via HTML5 QR Scanner) to scan attendee QR codes and call `/api/v1/tickets/{code}/check-in`.


### Cập nhật 4 Sự Kiện Chính Thức (Official Concerts Seeding & Frontend)
- **Anh Trai Say Hi Live Concert 2026**: Mỹ Đình, Hà Nội | Poster: `/images/AnhTraiSayHi2_logo.jpg` | Giá từ 650.000 ₫
- **Sơn Tùng M-TP x Red Bull - Đêm Nhạc Năng Lượng**: QK7, TP.HCM | Poster: `/images/RedBull-son-tung.jpg` | Giá từ 950.000 ₫
- **Sóng 26 - Đại Nhạc Hội Giao Thừa Đón Năm Mới**: Quảng trường Thời Đại | Poster: `/images/song-26.jpeg` | Giá từ 450.000 ₫
- **V-Concert - Rực Rỡ Hòa Ca Đất Trời**: Trung tâm Hội nghị Quốc gia, Hà Nội | Poster: `/images/v-concert.jpg` | Giá từ 750.000 ₫
- **Toàn bộ hệ thống**:
  - `DatabaseSeeder.java` tự động nạp 4 concert và sơ đồ ghế độc lập cho từng concert vào MongoDB.
  - `index.html` hiển thị 4 card concert kèm hình ảnh sắc nét, liên kết động tới màn hình đặt vé.
  - `booking.html` tích hợp bộ chọn sự kiện (Concert Switcher Dropdown), hiển thị poster và sơ đồ ghế tương ứng theo thời gian thực.


### Cập nhật Giao diện Khung Navbar, Trang User Riêng & Tối ưu Cấu trúc Frontend
- **Khung viền Navbar (Framed Navbar Buttons & Pills)**:
  - Tất cả các nút bấm và nhãn trong thanh điều hướng đều có khung viền bo cong sắc nét (pill frame / border). Loại bỏ hoàn toàn tình trạng chữ trần không viền.
  - Trạng thái chưa đăng nhập: "Đăng Nhập" (nút viền trắng/đen), "Đăng Ký" (nút khối đen viền sắc nét).
  - Trạng thái đã đăng nhập: "Vé Của Tôi" (nút viền), "Trang Của Tôi" (nút viền), Nhãn tên tài khoản có khung viền bo tròn, "Đăng Xuất" (nút viền).
  - Menu giữa trang chủ: Đặt trong khung viên con nhộng (capsule container) tinh tế.
- **Trang Riêng Cho Khách Hàng (`user.html`)**:
  - Tách biệt hoàn toàn khỏi Landing Page (`index.html`).
  - Đăng nhập tài khoản khách hàng chuyển hướng trực tiếp vào `user.html`.
  - Bao gồm: Lời chào cá nhân hóa, 4 thẻ thống kê số vé/điểm thưởng/mã giảm giá, 4 thẻ concert mở bán nhanh, bảng lịch sử đơn đặt vé, kho voucher độc quyền và thông tin tài khoản.
- **Nick Demo Chính Thức**: Cập nhật thành **Nguyễn Văn A** (`customer` / `customer123`).
- **Dọn dẹp & Cấu trúc lại Repo Frontend**:
  - Xóa bỏ 8 file ảnh `still1..4.jpg` thừa thãi (cả ở thư mục gốc static và images).
  - Giữ lại cấu trúc thư mục tĩnh chuẩn mực: `css/`, `js/`, `images/`, `policies/`.


### Cập nhật Email Bot (Google App Password 16 ký tự, OTP 6 số tự sinh), Social Login & Tinh chỉnh UI
- **Email Bot & Hệ Thống OTP 6 Số**:
  - Mã OTP (6 số ngẫu nhiên từ `100000` đến `999999`) do chính App tự sinh, lưu trữ cache có hiệu lực chính xác **5 phút**. Google không sinh mã này mà đóng vai trò người đưa thư (SMTP mail carrier).
  - Admin cấu hình **Mật khẩu ứng dụng (Google App Password)** gồm 16 ký tự tạo từ `myaccount.google.com/apppasswords` để đăng nhập SMTP gửi mail thay vì mật khẩu thông thường.
  - Tích hợp công cụ kiểm tra gửi thử và xác minh OTP trực tiếp trong trang quản trị (`admin.html`).
- **Tạo Tài Khoản & Đăng Nhập Mạng Xã Hội (Google & Facebook)**:
  - Cả hai trang `login.html` và `register.html` đều bổ sung phân cách `HOẶC` cùng hai nút mạng xã hội `[ Facebook ]` và `[ Google ]` chuẩn UI Shopee.
  - Backend API `/api/v1/auth/social-login` tự động tạo tài khoản hoặc đăng nhập tức thì cho người dùng Google và Facebook, cấp JWT token và chuyển hướng tới `/user.html`.
- **Tinh chỉnh Giao diện Khách Hàng (`user.html`) & Navbar**:
  - Sửa nhãn tài khoản ADMIN trên thanh điều hướng thành **`admin`** gọn gàng.
  - Bỏ layout "Thông Tin Tài Khoản Khách Hàng", bỏ dòng text phụ "Tài khoản: customer...", và bỏ nút "Đăng Xuất" trùng lặp trong hero banner của `user.html`.
  - Cập nhật địa chỉ trụ sở chính thành **`Hà Nội, Việt Nam`** toàn hệ thống.
