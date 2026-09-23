
# Personal Expense Manager

Cần cài và mở Docker Desktop trước khi chạy.

## Chạy ứng dụng

### CMD

```bat
git clone https://github.com/Trunganhlaptrinh/Java-Web-Practice.git
cd /d "Java-Web-Practice/Project/Level 1/PersonalExpenseManager"
docker compose up -d --build
start "" "http://localhost:1234/Personal-Expense-Manager/"
```

### PowerShell

```powershell
git clone https://github.com/Trunganhlaptrinh/Java-Web-Practice.git
Set-Location "Java-Web-Practice/Project/Level 1/PersonalExpenseManager"
docker compose up -d --build
Start-Process "http://localhost:1234/Personal-Expense-Manager/"
```

Ứng dụng sẽ tự mở tại:

http://localhost:1234/Personal-Expense-Manager/

## Dừng ứng dụng

```bat
docker compose down
```

## Reset database

```bat
docker compose down -v
docker compose up -d --build
start "" "http://localhost:1234/Personal-Expense-Manager/"
```

Lệnh này xóa dữ liệu hiện tại và tạo lại database mẫu.
