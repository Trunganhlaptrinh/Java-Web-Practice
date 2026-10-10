// Hàm gọi API dùng chung
async function fetchAPI(url, method = 'GET', body = null, isFormData = false) {
    const options = {
        method: method,
    };
    
    if (body) {
        if (isFormData) {
            options.body = body; // Trình duyệt tự set Content-Type cho form data
        } else {
            options.headers = {
                'Content-Type': 'application/json'
            };
            options.body = JSON.stringify(body);
        }
    }

    try {
        const response = await fetch(url, options);
        // Do API trả về JSON
        if (response.headers.get("content-type")?.includes("application/json")) {
            return await response.json();
        } else {
            const text = await response.text();
            try {
                return JSON.parse(text);
            } catch (e) {
                return { success: false, message: "Lỗi phản hồi từ server", data: null };
            }
        }
    } catch (error) {
        console.error("Lỗi fetch API:", error);
        return { success: false, message: "Lỗi kết nối đến server", data: null };
    }
}

// Xử lý thông báo lỗi/thành công chung trên form
function showMessage(elementId, message, isSuccess = false) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.textContent = message;
    el.className = isSuccess ? 'success-msg' : 'error-msg';
    el.style.display = 'block';
    setTimeout(() => {
        el.style.display = 'none';
    }, 5000);
}

// Kiểm tra user đăng nhập hiện tại từ localStorage
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

// Cập nhật giao diện thanh điều hướng
function updateNavbar() {
    const user = getCurrentUser();
    const navLinks = document.getElementById('nav-links');
    if (!navLinks) return;
    
    if (user) {
        navLinks.innerHTML = `
            <span>Xin chào, ${user.fullName} (${user.role})</span>
            <button onclick="logout()">Đăng xuất</button>
        `;
    } else {
        navLinks.innerHTML = `
            <a href="login.html">Đăng nhập</a>
            <a href="register.html">Đăng ký</a>
        `;
    }
}

// Xử lý đăng xuất
async function logout() {
    const res = await fetchAPI('/RoomFinder/api/auth/logout', 'POST');
    if (res.success) {
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    } else {
        alert(res.message);
    }
}

// Format tiền tệ VNĐ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

// Khởi chạy khi load trang
document.addEventListener('DOMContentLoaded', () => {
    updateNavbar();
});

