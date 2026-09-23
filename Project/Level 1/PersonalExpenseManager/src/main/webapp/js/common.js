const API_BASE = 'api/';
const $ = id => document.getElementById(id);

async function apiCall(path, method = 'GET', body = null) {
    const options = { method, headers: {} };
    if (body !== null) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(body);
    }
    let response;
    try { response = await fetch(API_BASE + path, options); }
    catch (error) { throw new Error('Không kết nối được tới server'); }
    let json;
    try { json = await response.json(); }
    catch (error) { throw new Error('Server trả dữ liệu không hợp lệ'); }
    if (!json.success) throw new Error(json.message);
    return json.data;
}

function esc(value) {
    return value === null || value === undefined ? '' : String(value)
        .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}
function toast(message, type = 'info') {
    const box = $('toast-box'); const item = document.createElement('div');
    item.className = 'toast ' + type; item.textContent = message; box.appendChild(item);
    setTimeout(() => item.remove(), 3200);
}
function today() { return new Date().toISOString().slice(0, 10); }
function formatVnd(value) { return Number(value || 0).toLocaleString('vi-VN') + ' VND'; }
function renderNav(active) {
    $('nav').innerHTML = `<nav class="nav"><a class="brand" href="index.html">Sổ thu chi</a>
        <a class="${active === 'dashboard' ? 'active' : ''}" href="index.html">Tổng quan</a>
        <a class="${active === 'transactions' ? 'active' : ''}" href="transactions.html">Giao dịch</a>
        <a class="${active === 'categories' ? 'active' : ''}" href="categories.html">Danh mục</a>
        <a class="${active === 'methods' ? 'active' : ''}" href="payment-methods.html">Thanh toán</a></nav>`;
}
