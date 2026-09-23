
## Chạy bằng Docker (khuyên dùng)
 
Chỉ cần cài **Docker Desktop**, không cần cài Java/Maven/MySQL/Tomcat trên máy.
 
--> vào cmd và copy + paste:

```bash
git clone https://github.com/Trunganhlaptrinh/Java-Web-Practice.git
cd "Java-Web-Practice/Project/Level 1/StudentManagementUplevel"
docker compose up --build
```
 
Lần đầu build sẽ mất khoảng 2-3 phút (tải image Maven/Tomcat/MySQL + build). Khi thấy dòng `Server startup in [...] milliseconds`, mở trình duyệt:
 
**http://localhost:1234**