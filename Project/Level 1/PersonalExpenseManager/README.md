
## Chạy bằng Docker (khuyên dùng)

Chỉ cần cài **Docker Desktop**, không cần cài Java, Maven, MySQL hoặc Tomcat trên máy.

Mở CMD và copy/paste:

```bat
git clone https://github.com/Trunganhlaptrinh/Java-Web-Practice.git
cd /d "Java-Web-Practice/Project/Level 1/PersonalExpenseManager"
docker compose up --build
```

Lần đầu build sẽ mất khoảng 2-3 phút để tải image Maven, Tomcat, MySQL và build ứng dụng.

Khi thấy dòng `Server startup in [...] milliseconds`, mở trình duyệt tại:

**http://localhost:1234**


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

