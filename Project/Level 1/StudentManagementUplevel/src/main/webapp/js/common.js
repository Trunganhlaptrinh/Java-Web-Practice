/*
 * Code dung chung cho moi trang HTML.
 * Luong: HTML -> apiCall() -> fetch toi Servlet (/api/...) -> Servlet tra JSON
 *        { success, message, data } -> JS render ra DOM.
 */

// Dung duong dan TUONG DOI (khong co dau "/" o dau) de chay dung du context path
// cua Tomcat la gi (vd /StudentManagementUplevel-1.0-SNAPSHOT/).
const API_BASE = 'api/';

const $ = id => document.getElementById(id);

// Trung Anh - API cầu nối giao tiếp giữa HTML và Controller 

/**
 * Goi API va tra ve object JSON { success, message, data }.
 * Neu success = false hoac loi mang -> throw Error(message) de trang bat va hien toast.
 */
async function apiCall(path, method = 'GET', body = null) {
    const options = { method: method, headers: {} };
    if (body !== null) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(body);
    }

    let res;
    try {
        res = await fetch(API_BASE + path, options);
    } catch (e) {
        throw new Error('Không kết nối được tới server');
    }

    let json;
    try {
        json = await res.json();
    } catch (e) {
        throw new Error('Server trả dữ liệu không hợp lệ (HTTP ' + res.status + ')');
    }

    if (!json.success) {
        throw new Error(json.message);
    }
    return json;
}

// chong XSS: luon escape du lieu tu server truoc khi dua vao innerHTML
function esc(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function toast(message, type = 'info') {
    let box = $('toast-box');
    if (!box) {
        box = document.createElement('div');
        box.id = 'toast-box';
        document.body.appendChild(box);
    }
    const el = document.createElement('div');
    el.className = 'toast ' + type;
    el.textContent = message;
    box.appendChild(el);
    setTimeout(() => el.remove(), 3500);
}

// ngay hom nay dang yyyy-MM-dd (gio may local)
function today() {
    const d = new Date();
    return d.getFullYear() + '-'
            + String(d.getMonth() + 1).padStart(2, '0') + '-'
            + String(d.getDate()).padStart(2, '0');
}

// do du lieu vao <select>
function fillSelect(select, items, valueFn, labelFn, placeholder) {
    let html = placeholder ? `<option value="">${esc(placeholder)}</option>` : '';
    html += items.map(it => `<option value="${esc(valueFn(it))}">${esc(labelFn(it))}</option>`).join('');
    select.innerHTML = html;
}

// thanh menu chung
function renderNav(active) {
    const items = [
        ['index', 'index.html', 'Trang chủ'],
        ['departments', 'departments.html', 'Khoa'],
        ['teachers', 'teachers.html', 'Giáo viên'],
        ['courses', 'courses.html', 'Môn học'],
        ['students', 'students.html', 'Sinh viên'],
        ['enrollments', 'enrollments.html', 'Đăng ký môn']
    ];
    const nav = document.createElement('nav');
    nav.className = 'nav';
    nav.innerHTML = '<span class="brand">Student Course Management</span>'
            + items.map(([key, href, label]) =>
                `<a href="${href}" class="${key === active ? 'active' : ''}">${label}</a>`).join('');
    document.body.prepend(nav);
}
