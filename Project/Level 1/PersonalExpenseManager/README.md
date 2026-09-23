
# Personal Expense Manager

Cần cài và mở Docker Desktop trước khi chạy.

## Chạy ứng dụng trong "1 lệnh duy nhất"

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

(hoặc muốn xem lại thì nhấn vào link này)

http://localhost:1234/Personal-Expense-Manager/



## Xóa ứng dụng (xóa container này)

```bat
docker compose down
```

## Reset database và chạy lại 

```bat
docker compose down -v
docker compose up -d --build
start "" "http://localhost:1234/Personal-Expense-Manager/"
```

