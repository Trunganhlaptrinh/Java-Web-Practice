/**
 * THE SHOW - Multi-Language i18n System (Tiếng Việt & English)
 */
const I18N_DICTIONARY = {
    vi: {
        // Navigation
        "nav.brand": "THE SHOW",
        "nav.features": "Tính Năng",
        "nav.concerts": "Sự Kiện",
        "nav.pricing": "Hạng Vé",
        "nav.faq": "Hỏi Đáp",
        "nav.login": "Đăng Nhập",
        "nav.register": "Đăng Ký",
        "nav.my_tickets": "Vé Của Tôi",
        "nav.logout": "Đăng Xuất",
        "nav.admin": "Quản Trị",

        // Hero (NO PERIODS!)
        "hero.badge": "HỆ THỐNG ĐẶT VÉ CONCERT QUỐC TẾ",
        "hero.title": "Cảm Xúc Thật <br><span>Sân Khấu Đỉnh Cao</span>",
        "hero.btn_book": "Đặt Vé Ngay",
        "hero.btn_explore": "Khám Phá Sự Kiện",

        // Stats
        "stats.audience_val": "100.000+",
        "stats.audience_lbl": "Khán giả tin dùng",
        "stats.tours_val": "50+",
        "stats.tours_lbl": "Tour diễn quốc tế",
        "stats.hold_val": "10 PHÚT",
        "stats.hold_lbl": "Giữ chỗ an toàn",
        "stats.authentic_val": "100%",
        "stats.authentic_lbl": "Vé điện tử chính hãng",

        // Features
        "features.heading": "Trải Nghiệm Đặt Vé Khác Biệt",
        "features.card1_title": "Sơ Đồ Ghế Thời Gian Thực",
        "features.card1_desc": "Quan sát toàn cảnh khán đài, lựa chọn trực quan từng hàng ghế VIP hay khu vực đứng GA với dữ liệu cập nhật theo từng giây.",
        "features.card2_title": "Khóa Ghế 10 Phút Thông Minh",
        "features.card2_desc": "Khi bạn chọn ghế, hệ thống lập tức khóa chỗ trong 10 phút. Bạn an tâm điền thông tin và thanh toán mà không sợ bị trùng ghế.",
        "features.card3_title": "Vé Điện Tử QR Code Bảo Mật",
        "features.card3_desc": "Nhận mã QR check-in độc quyền tức thì trên tài khoản và qua Email. Soát vé siêu tốc tại cổng, chống vé giả tuyệt đối.",

        // Concerts
        "concerts.heading": "Sự Kiện Đang Mở Đặt",
        "concerts.view_all": "TẤT CẢ CONCERT",
        "concerts.from_price": "Giá từ",
        "concerts.btn_book": "Đặt Vé",
        "concerts.tag_upcoming": "SẮP DIỄN RA",
        "concerts.tag_hot": "HOT TOUR",
        "concerts.tag_festival": "LỄ HỘI",
        "concerts.tag_liveshow": "LIVE SHOW",

        // Pricing
        "pricing.heading": "Bảng Hạng Vé Linh Hoạt",
        "pricing.subheading": "Lựa chọn vị trí trải nghiệm âm nhạc phù hợp nhất với phong cách của bạn.",
        "pricing.per_ticket": "₫ / vé",
        "pricing.most_popular": "PHỔ BIẾN NHẤT",
        "pricing.std_title": "GA Standard",
        "pricing.std_sub": "Khu vực đứng tự do sôi động",
        "pricing.std_p1": "• Vị trí đứng khu vực trung tâm",
        "pricing.std_p2": "• Vé điện tử QR Code bảo mật",
        "pricing.std_p3": "• Xác nhận tức thì qua Email",
        "pricing.std_btn": "Chọn Hạng Standard",
        
        "pricing.vip_title": "VIP Diamond",
        "pricing.vip_sub": "Ghế ngồi trung tâm tầm nhìn đẹp",
        "pricing.vip_p1": "• Ghế ngồi êm ái góc nhìn trực diện",
        "pricing.vip_p2": "• Lối vào ưu tiên (Fast Track)",
        "pricing.vip_p3": "• Quà lưu niệm độc quyền ban tổ chức",
        "pricing.vip_p4": "• Vòng tay kỷ niệm phát sáng",
        "pricing.vip_btn": "Chọn Hạng VIP Diamond",

        "pricing.vvip_title": "VVIP Lounge",
        "pricing.vvip_sub": "Đặc quyền giới hạn hàng đầu",
        "pricing.vvip_p1": "• Hàng ghế đầu sát mép sân khấu",
        "pricing.vvip_p2": "• Thẻ Soundcheck Pass tổng duyệt",
        "pricing.vvip_p3": "• Phòng chờ VIP với thức uống miễn phí",
        "pricing.vvip_p4": "• Chụp ảnh kỷ niệm cùng nghệ sĩ",
        "pricing.vvip_btn": "Chọn Hạng VVIP",

        // FAQ
        "faq.heading": "Câu Hỏi Thường Gặp",
        "faq.q1": "Tôi nhận vé và sử dụng mã QR như thế nào?",
        "faq.a1": "Sau khi hoàn tất thanh toán, vé điện tử kèm mã QR duy nhất được cấp ngay trong mục \"Vé Của Tôi\" trên website và đồng thời gửi qua Email đã đăng ký. Bạn chỉ cần đưa mã QR tại cổng kiểm soát để vào sự kiện.",
        "faq.q2": "Cơ chế giữ chỗ 10 phút hoạt động ra sao?",
        "faq.a2": "Khi bạn nhấn chọn ghế, hệ thống sẽ tạm thời khóa chỗ trong 10 phút. Trong thời gian này, không một ai khác có thể mua trùng vị trí của bạn, giúp bạn thoải mái điền thông tin và thanh toán.",
        "faq.q3": "Nếu quá 10 phút tôi chưa hoàn tất thanh toán thì sao?",
        "faq.a3": "Khi đồng hồ đếm ngược 10 phút kết thúc, hệ thống tự động giải phóng vị trí ghế trở lại sơ đồ để các khán giả khác có cơ hội đặt mua.",
        "faq.q4": "Hệ thống hỗ trợ những phương thức thanh toán nào?",
        "faq.a4": "THE SHOW hỗ trợ thanh toán qua Chuyển khoản ngân hàng, Thẻ ATM nội địa, Thẻ quốc tế Visa/MasterCard và Ví điện tử.",

        // Footer
        "footer.desc": "Nền tảng phân phối & đặt vé concert quốc tế hàng đầu. Chọn chỗ trực quan thời gian thực, giữ ghế 10 phút an toàn và nhận vé điện tử QR Code bảo mật tức thì.",
        "footer.language": "Ngôn Ngữ:",
        "footer.col_products": "Sản Phẩm",
        "footer.link_home": "Trang Chủ",
        "footer.link_booking": "Đặt Vé Concert",
        "footer.link_upcoming": "Sự Kiện Đang Mở Bán",
        "footer.link_my_tickets": "Vé Điện Tử Của Tôi",
        "footer.link_pricing": "Bảng Giá & Hạng Vé",
        "footer.col_policies": "Chính Sách",
        "footer.policy_privacy": "Chính sách bảo mật",
        "footer.policy_data": "Bảo vệ dữ liệu cá nhân",
        "footer.policy_pricing": "Chính sách giá vé",
        "footer.policy_payment": "Chính sách thanh toán",
        "footer.policy_refund": "Chính sách hoàn & đổi vé",
        "footer.policy_support": "Hình thức hỗ trợ trực tuyến",
        "footer.col_terms": "Điều Khoản & Quy Định",
        "footer.term_rights": "Quyền & nghĩa vụ các bên",
        "footer.term_complaint": "Tiếp nhận & giải quyết khiếu nại",
        "footer.term_conditions": "Điều kiện & hạn chế dịch vụ",
        "footer.term_provision": "Cung cấp dịch vụ vé",
        "footer.term_termination": "Chấm dứt dịch vụ",
        "footer.term_disclaimer": "Tuyên bố miễn trừ trách nhiệm",
        "footer.col_pricing": "Giá Cả & Hạng Vé",
        "footer.col_operator": "Đơn Vị Vận Hành (Demo)",
        "footer.payment_methods": "Phương Thức Thanh Toán",
        "footer.company_name": "Công ty Cổ phần Sự kiện THE SHOW (Demo Sandbox)",
        "footer.tax_id": "Mã số thuế: 1234567890-DEMO",
        "footer.address": "TRỤ SỞ CHÍNH: Tầng 18, Tòa nhà Keangnam Landmark 72, Mễ Trì, Nam Từ Liêm, Hà Nội, Việt Nam",
        "footer.hotline": "Hotline: 1900 1234 (Thử nghiệm)",
        "footer.email": "Email: support@theshow-demo.vn",
        "footer.disclaimer1": "Sử dụng nội dung ở trang này và dịch vụ tại THE SHOW có nghĩa là bạn đồng ý với Thỏa thuận sử dụng và Chính sách bảo mật của chúng tôi.",
        "footer.disclaimer2": "THE SHOW là dự án mô phỏng hệ thống phân phối vé concert độc lập phục vụ mục đích học tập và kiểm thử phần mềm (Demo Sandbox). Mọi thông tin doanh nghiệp (MST: 1234567890-DEMO), số hotline và nghệ sĩ chỉ mang tính chất minh họa chức năng kỹ thuật.",
        "footer.copyright": "© 2026 THE SHOW Entertainment Joint Stock Company (Demo / Sandbox Project). All rights reserved.",
        "footer.audience_tag": "Được tạo cho người yêu âm nhạc và khán giả concert toàn cầu."
    },
    en: {
        // Navigation
        "nav.brand": "THE SHOW",
        "nav.features": "Features",
        "nav.concerts": "Events",
        "nav.pricing": "Ticket Tiers",
        "nav.faq": "FAQ",
        "nav.login": "Login",
        "nav.register": "Register",
        "nav.my_tickets": "My Tickets",
        "nav.logout": "Logout",
        "nav.admin": "Admin",

        // Hero (NO PERIODS!)
        "hero.badge": "INTERNATIONAL CONCERT TICKETING PLATFORM",
        "hero.title": "Real Emotions <br><span>Peak Stages</span>",
        "hero.btn_book": "Book Now",
        "hero.btn_explore": "Explore Events",

        // Stats
        "stats.audience_val": "100,000+",
        "stats.audience_lbl": "Trusted Audiences",
        "stats.tours_val": "50+",
        "stats.tours_lbl": "World Tours",
        "stats.hold_val": "10 MIN",
        "stats.hold_lbl": "Secure Seat Hold",
        "stats.authentic_val": "100%",
        "stats.authentic_lbl": "Authentic E-Tickets",

        // Features
        "features.heading": "A Distinct Ticketing Experience",
        "features.card1_title": "Real-Time Seating Map",
        "features.card1_desc": "Explore panoramic venue layouts, visually choose VIP seated rows or GA standing zones with per-second live updates.",
        "features.card2_title": "Smart 10-Minute Lock",
        "features.card2_desc": "Once you pick your seats, they are instantly reserved for 10 minutes. Check out peacefully with zero risk of double booking.",
        "features.card3_title": "Secure QR Code E-Tickets",
        "features.card3_desc": "Receive exclusive QR check-in codes instantly on your account and via email. Fast gate scanning, zero counterfeit tickets.",

        // Concerts
        "concerts.heading": "Events Open for Booking",
        "concerts.view_all": "ALL CONCERTS",
        "concerts.from_price": "From",
        "concerts.btn_book": "Book Ticket",
        "concerts.tag_upcoming": "UPCOMING",
        "concerts.tag_hot": "HOT TOUR",
        "concerts.tag_festival": "FESTIVAL",
        "concerts.tag_liveshow": "LIVE SHOW",

        // Pricing
        "pricing.heading": "Flexible Ticket Tiers",
        "pricing.subheading": "Choose the optimal music experience crafted for your concert style.",
        "pricing.per_ticket": "₫ / ticket",
        "pricing.most_popular": "MOST POPULAR",
        "pricing.std_title": "GA Standard",
        "pricing.std_sub": "Vibrant standing general admission zone",
        "pricing.std_p1": "• Center standing admission zone",
        "pricing.std_p2": "• High-security QR Code e-ticket",
        "pricing.std_p3": "• Instant confirmation via email",
        "pricing.std_btn": "Select Standard",

        "pricing.vip_title": "VIP Diamond",
        "pricing.vip_sub": "Central seated tier with prime sightlines",
        "pricing.vip_p1": "• Cushioned seating with direct stage view",
        "pricing.vip_p2": "• Fast Track priority entrance lane",
        "pricing.vip_p3": "• Exclusive organizer merchandise gift",
        "pricing.vip_p4": "• Commemorative glowing wristband",
        "pricing.vip_btn": "Select VIP Diamond",

        "pricing.vvip_title": "VVIP Lounge",
        "pricing.vvip_sub": "Exclusive top-tier limited privileges",
        "pricing.vvip_p1": "• Front row right along the stage edge",
        "pricing.vvip_p2": "• Official Soundcheck Rehearsal Pass",
        "pricing.vvip_p3": "• VIP Lounge access with complimentary drinks",
        "pricing.vvip_p4": "• Exclusive artist photo opportunity",
        "pricing.vvip_btn": "Select VVIP",

        // FAQ
        "faq.heading": "Frequently Asked Questions",
        "faq.q1": "How do I receive my tickets and use the QR code?",
        "faq.a1": "Upon successful payment, an e-ticket with a unique QR code is immediately available under \"My Tickets\" and sent to your email. Simply present the QR code at the entrance turnstile.",
        "faq.q2": "How does the 10-minute hold lock work?",
        "faq.a2": "When you pick seats, the system temporarily locks them for 10 minutes. During this window, nobody else can purchase your seats, giving you ample time to check out.",
        "faq.q3": "What happens if 10 minutes elapse before payment?",
        "faq.a3": "When the 10-minute countdown ends, the seats are automatically released back to the seating chart for other fans to book.",
        "faq.q4": "Which payment methods are accepted?",
        "faq.a4": "THE SHOW supports Bank Transfer, Domestic ATM cards, International Visa/MasterCard, and E-Wallets.",

        // Footer
        "footer.desc": "Premier international concert ticketing platform. Real-time visual seat selection, 10-minute secure lock, and instant secure QR e-tickets.",
        "footer.language": "Language:",
        "footer.col_products": "Products",
        "footer.link_home": "Home",
        "footer.link_booking": "Book Concert Tickets",
        "footer.link_upcoming": "Events on Sale",
        "footer.link_my_tickets": "My E-Tickets",
        "footer.link_pricing": "Pricing & Tiers",
        "footer.col_policies": "Policies",
        "footer.policy_privacy": "Privacy Policy",
        "footer.policy_data": "Personal Data Protection",
        "footer.policy_pricing": "Ticket Pricing Policy",
        "footer.policy_payment": "Payment Policy",
        "footer.policy_refund": "Refund & Exchange Policy",
        "footer.policy_support": "Online Customer Support",
        "footer.col_terms": "Terms & Rules",
        "footer.term_rights": "Rights & Obligations",
        "footer.term_complaint": "Complaint Resolution",
        "footer.term_conditions": "Service Conditions & Limits",
        "footer.term_provision": "Ticketing Service Provision",
        "footer.term_termination": "Service Termination",
        "footer.term_disclaimer": "Disclaimer Statement",
        "footer.col_pricing": "Pricing & Tiers",
        "footer.col_operator": "Operator (Demo)",
        "footer.payment_methods": "Payment Methods",
        "footer.company_name": "THE SHOW Entertainment Joint Stock Company (Demo Sandbox)",
        "footer.tax_id": "Tax ID: 1234567890-DEMO",
        "footer.address": "HEADQUARTERS: 18th Fl, Keangnam Landmark 72, Me Tri, Nam Tu Liem, Hanoi, Vietnam",
        "footer.hotline": "Hotline: 1900 1234 (Demo Test)",
        "footer.email": "Email: support@theshow-demo.vn",
        "footer.disclaimer1": "Using this website and services at THE SHOW signifies your agreement with our Terms of Service and Privacy Policy.",
        "footer.disclaimer2": "THE SHOW is an independent concert ticketing simulation project for academic and software testing purposes (Demo Sandbox). All corporate info (Tax ID: 1234567890-DEMO), hotline and artists are for technical demonstration only.",
        "footer.copyright": "© 2026 THE SHOW Entertainment Joint Stock Company (Demo / Sandbox Project). All rights reserved.",
        "footer.audience_tag": "Created for music enthusiasts and concert fans worldwide."
    }
};

function getCurrentAppLanguage() {
    return localStorage.getItem('the_show_lang') || 'vi';
}

function i18nTranslate(key, lang) {
    lang = lang || getCurrentAppLanguage();
    const dict = I18N_DICTIONARY[lang] || I18N_DICTIONARY.vi;
    return dict[key] || I18N_DICTIONARY.vi[key] || key;
}

function setAppLanguage(lang) {
    if (lang !== 'vi' && lang !== 'en') lang = 'vi';
    localStorage.setItem('the_show_lang', lang);
    document.documentElement.lang = lang;

    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        const translated = i18nTranslate(key, lang);
        if (translated) el.textContent = translated;
    });

    document.querySelectorAll('[data-i18n-html]').forEach(el => {
        const key = el.getAttribute('data-i18n-html');
        const translated = i18nTranslate(key, lang);
        if (translated) el.innerHTML = translated;
    });

    const footerVi = document.getElementById('footer-lang-vi');
    const footerEn = document.getElementById('footer-lang-en');
    if (footerVi && footerEn) {
        if (lang === 'vi') {
            footerVi.className = 'text-xs font-bold text-black border-b border-black pb-0.5 cursor-pointer bg-transparent border-t-0 border-l-0 border-r-0';
            footerEn.className = 'text-xs font-medium text-zinc-500 hover:text-black cursor-pointer bg-transparent border-none';
        } else {
            footerEn.className = 'text-xs font-bold text-black border-b border-black pb-0.5 cursor-pointer bg-transparent border-t-0 border-l-0 border-r-0';
            footerVi.className = 'text-xs font-medium text-zinc-500 hover:text-black cursor-pointer bg-transparent border-none';
        }
    }

    const navVi = document.getElementById('nav-lang-vi');
    const navEn = document.getElementById('nav-lang-en');
    if (navVi && navEn) {
        if (lang === 'vi') {
            navVi.className = 'text-xs font-black text-black cursor-pointer';
            navEn.className = 'text-xs font-semibold text-zinc-400 hover:text-black cursor-pointer';
        } else {
            navEn.className = 'text-xs font-black text-black cursor-pointer';
            navVi.className = 'text-xs font-semibold text-zinc-400 hover:text-black cursor-pointer';
        }
    }

    window.dispatchEvent(new CustomEvent('appLanguageChanged', { detail: { lang } }));
}

document.addEventListener('DOMContentLoaded', () => {
    const curLang = getCurrentAppLanguage();
    setAppLanguage(curLang);
});


// THE SHOW - Common Application Script
const API_BASE = '/api/v1';

const Auth = {
    getToken() { return localStorage.getItem('token'); },
    setToken(token) { localStorage.setItem('token', token); },
    getUser() { 
        const u = localStorage.getItem('user'); 
        if (!u) return null;
        try {
            const parsed = JSON.parse(u);
            if (parsed.username === 'customer' && parsed.fullName === 'Nguyễn Văn Khách') {
                parsed.fullName = 'Nguyễn Văn A';
                localStorage.setItem('user', JSON.stringify(parsed));
            }
            return parsed;
        } catch(e) {
            return null;
        }
    },
    setUser(user) { 
        if (user && user.username === 'customer' && (!user.fullName || user.fullName === 'Nguyễn Văn Khách')) {
            user.fullName = 'Nguyễn Văn A';
        }
        localStorage.setItem('user', JSON.stringify(user)); 
    },
    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login.html';
    },
    isLoggedIn() { return !!this.getToken(); },
    isAdmin() { const u = this.getUser(); return !!(u && u.role === 'ADMIN'); }
};

async function apiFetch(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : (API_BASE + endpoint);
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    const token = Auth.getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;

    try {
        const response = await fetch(url, { ...options, headers });
        if (response.status === 401) {
            if (Auth.isLoggedIn()) Auth.logout();
            throw new Error('Yêu cầu chưa xác thực hoặc phiên đăng nhập đã hết hạn.');
        }
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Lỗi xử lý yêu cầu');
        return data;
    } catch (err) {
        throw err;
    }
}

function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = 'toast toast-' + type;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(-20px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3800);
}

function formatCurrency(amount) {
    if (amount == null) return '0 ₫';
    return Number(amount).toLocaleString('vi-VN') + ' ₫';
}

function formatDateTime(isoString) {
    if (!isoString) return '';
    const d = new Date(isoString);
    return d.toLocaleDateString('vi-VN', {
        weekday: 'short', year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit'
    });
}

function initNavbar() {
    const navActions = document.getElementById('nav-actions');
    if (!navActions) return;

    const lang = (typeof getCurrentAppLanguage === 'function') ? getCurrentAppLanguage() : 'vi';
    const tMyTickets = (typeof i18nTranslate === 'function') ? i18nTranslate('nav.my_tickets', lang) : (lang === 'en' ? 'My Tickets' : 'Vé Của Tôi');
    const tAdmin = (typeof i18nTranslate === 'function') ? i18nTranslate('nav.admin', lang) : (lang === 'en' ? 'Admin' : 'Quản Trị');
    const tProfile = (lang === 'en') ? 'Profile' : 'Trang Của Tôi';
    const tLogout = (typeof i18nTranslate === 'function') ? i18nTranslate('nav.logout', lang) : (lang === 'en' ? 'Logout' : 'Đăng Xuất');
    const tLogin = (typeof i18nTranslate === 'function') ? i18nTranslate('nav.login', lang) : (lang === 'en' ? 'Login' : 'Đăng Nhập');
    const tRegister = (typeof i18nTranslate === 'function') ? i18nTranslate('nav.register', lang) : (lang === 'en' ? 'Register' : 'Đăng Ký');

    if (Auth.isLoggedIn()) {
        const user = Auth.getUser();
        const role = user ? user.role : 'CUSTOMER';
        let name = user ? (user.fullName || user.username) : 'Nguyễn Văn A';
        if (name === 'Nguyễn Văn Khách') name = 'Nguyễn Văn A';

        let html = '<a href="/my-tickets.html" class="nav-btn nav-btn-outline" data-i18n="nav.my_tickets">' + tMyTickets + '</a>';
        if (role === 'ADMIN') {
            html += '<a href="/admin.html" class="nav-btn nav-btn-primary" data-i18n="nav.admin">' + tAdmin + '</a>';
            html += '<a href="/admin.html" class="nav-user-pill">admin</a>';
        } else {
            html += '<a href="/user.html" class="nav-btn nav-btn-outline">' + tProfile + '</a>';
            html += '<a href="/user.html" class="nav-user-pill">' + name + '</a>';
        }
        html += '<button onclick="Auth.logout()" class="nav-btn nav-btn-outline" data-i18n="nav.logout">' + tLogout + '</button>';

        navActions.innerHTML = html;
    } else {
        navActions.innerHTML = 
            '<a href="/login.html" class="nav-btn nav-btn-outline" data-i18n="nav.login">' + tLogin + '</a>' +
            '<a href="/register.html" class="nav-btn nav-btn-primary" data-i18n="nav.register">' + tRegister + '</a>';
    }
}
document.addEventListener('DOMContentLoaded', initNavbar);
window.addEventListener('appLanguageChanged', initNavbar);

document.addEventListener('DOMContentLoaded', initNavbar);


// ==========================================
// REAL GOOGLE & FACEBOOK OAUTH 2.0 INTEGRATION
// ==========================================
const RealOAuth = {
    STORAGE_GOOGLE_CLIENT_ID: 'theshow_oauth_google_client_id',
    STORAGE_FB_APP_ID: 'theshow_oauth_fb_app_id',
    STORAGE_PENDING_PROVIDER: 'theshow_oauth_pending_provider',
    serverConfig: null,

    async init() {
        try {
            const res = await apiFetch('/auth/oauth2/config');
            if (res && res.data) {
                this.serverConfig = res.data;
            }
        } catch (e) {
            // Ignore if backend config unreachable
        }

        // Auto handle redirect callback if token in hash
        await this.handleRedirectCallback();
    },

    getGoogleClientId() {
        const local = localStorage.getItem(this.STORAGE_GOOGLE_CLIENT_ID);
        if (local && local.trim()) return local.trim();
        if (this.serverConfig && this.serverConfig.googleClientId && this.serverConfig.googleClientId.trim()) {
            return this.serverConfig.googleClientId.trim();
        }
        return '';
    },

    getFacebookAppId() {
        const local = localStorage.getItem(this.STORAGE_FB_APP_ID);
        if (local && local.trim()) return local.trim();
        if (this.serverConfig && this.serverConfig.facebookAppId && this.serverConfig.facebookAppId.trim()) {
            return this.serverConfig.facebookAppId.trim();
        }
        return '';
    },

    setGoogleClientId(id) {
        if (id && id.trim()) localStorage.setItem(this.STORAGE_GOOGLE_CLIENT_ID, id.trim());
        else localStorage.removeItem(this.STORAGE_GOOGLE_CLIENT_ID);
    },

    setFacebookAppId(id) {
        if (id && id.trim()) localStorage.setItem(this.STORAGE_FB_APP_ID, id.trim());
        else localStorage.removeItem(this.STORAGE_FB_APP_ID);
    },

    loginWithGoogle() {
        const clientId = this.getGoogleClientId();
        if (!clientId) {
            this.showConfigModal('GOOGLE');
            return;
        }

        localStorage.setItem(this.STORAGE_PENDING_PROVIDER, 'GOOGLE');
        const redirectUri = window.location.origin + window.location.pathname;
        const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${encodeURIComponent(clientId)}&redirect_uri=${encodeURIComponent(redirectUri)}&response_type=token&scope=email%20profile%20openid&prompt=select_account`;
        window.location.href = authUrl;
    },

    loginWithFacebook() {
        const appId = this.getFacebookAppId();
        if (!appId) {
            this.showConfigModal('FACEBOOK');
            return;
        }

        localStorage.setItem(this.STORAGE_PENDING_PROVIDER, 'FACEBOOK');
        const redirectUri = window.location.origin + window.location.pathname;
        const authUrl = `https://www.facebook.com/v18.0/dialog/oauth?client_id=${encodeURIComponent(appId)}&redirect_uri=${encodeURIComponent(redirectUri)}&response_type=token&scope=email,public_profile`;
        window.location.href = authUrl;
    },

    async handleRedirectCallback() {
        if (!window.location.hash) return;
        const hash = window.location.hash.substring(1);
        const params = new URLSearchParams(hash);
        const accessToken = params.get('access_token');
        const error = params.get('error') || params.get('error_description');

        if (error) {
            showToast('Lỗi xác thực OAuth từ nhà cung cấp: ' + error, 'error');
            window.history.replaceState(null, null, window.location.pathname);
            return;
        }

        if (!accessToken) return;

        const provider = localStorage.getItem(this.STORAGE_PENDING_PROVIDER) || 'GOOGLE';
        localStorage.removeItem(this.STORAGE_PENDING_PROVIDER);

        // Clear hash from address bar immediately
        window.history.replaceState(null, null, window.location.pathname);

        if (provider === 'GOOGLE') {
            showToast('Đang kết nối xác thực tài khoản Google thật...', 'info');
            try {
                const res = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
                    headers: { 'Authorization': 'Bearer ' + accessToken }
                });
                if (!res.ok) throw new Error('Không thể tải thông tin từ Google (Mã HTTP ' + res.status + ')');
                const gUser = await res.json();
                await this.completeAuth({
                    provider: 'GOOGLE',
                    email: gUser.email,
                    fullName: gUser.name || gUser.email.split('@')[0],
                    providerId: gUser.sub,
                    avatarUrl: gUser.picture
                });
            } catch (err) {
                showToast('Xác thực tài khoản Google thất bại: ' + err.message, 'error');
            }
        } else if (provider === 'FACEBOOK') {
            showToast('Đang kết nối xác thực tài khoản Facebook thật...', 'info');
            try {
                const res = await fetch(`https://graph.facebook.com/me?fields=id,name,email,picture&access_token=${accessToken}`);
                if (!res.ok) throw new Error('Không thể tải thông tin từ Facebook (Mã HTTP ' + res.status + ')');
                const fbUser = await res.json();
                await this.completeAuth({
                    provider: 'FACEBOOK',
                    email: fbUser.email || (fbUser.id + '@facebook.com'),
                    fullName: fbUser.name || 'Người dùng Facebook',
                    providerId: fbUser.id,
                    avatarUrl: fbUser.picture && fbUser.picture.data ? fbUser.picture.data.url : null
                });
            } catch (err) {
                showToast('Xác thực tài khoản Facebook thất bại: ' + err.message, 'error');
            }
        }
    },

    async completeAuth(profile) {
        showToast(`Đang hoàn tất đăng ký & đăng nhập cho ${profile.email}...`, 'info');
        try {
            const res = await apiFetch('/auth/social-login', {
                method: 'POST',
                body: JSON.stringify(profile)
            });

            Auth.setToken(res.data.token);
            Auth.setUser({
                id: res.data.userId,
                username: res.data.username,
                email: res.data.email,
                fullName: res.data.fullName,
                role: res.data.role
            });

            showToast(`Đăng nhập thành công với tài khoản ${profile.provider} (${profile.fullName})!`, 'success');
            setTimeout(() => {
                window.location.href = '/user.html';
            }, 1000);
        } catch (err) {
            showToast(err.message, 'error');
        }
    },

    showConfigModal(focusProvider) {
        let modal = document.getElementById('oauth-config-modal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'oauth-config-modal';
            modal.style.cssText = 'position: fixed; inset: 0; background: rgba(0,0,0,0.6); display: flex; align-items: center; justify-content: center; z-index: 9999; padding: 20px;';
            modal.innerHTML = `
                <div style="background: #FFFFFF; border: 1px solid #000000; border-radius: 8px; width: 100%; max-width: 520px; padding: 28px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #E5E7EB; padding-bottom: 12px;">
                        <h3 id="oauth-modal-title" style="margin: 0; font-size: 16px; font-weight: 900; color: #000000; text-transform: uppercase; letter-spacing: 0.5px;">CẤU HÌNH XÁC THỰC OAUTH 2.0 THẬT</h3>
                        <button type="button" onclick="RealOAuth.closeConfigModal()" style="background: none; border: none; font-size: 20px; font-weight: 700; cursor: pointer; color: #6B7280;">✕</button>
                    </div>
                    
                    <p style="font-size: 13px; color: #4B5563; line-height: 1.6; margin-bottom: 16px;">
                        Để kết nối trực tiếp đến máy chủ <strong>Google</strong> hoặc <strong>Facebook</strong> thật và lấy thông tin tài khoản chính thức, bạn cần nhập Client ID / App ID tương ứng (tương tự Mật khẩu ứng dụng Gmail mà bạn cấu hình cho Bot).
                    </p>

                    <div style="background: #F9FAFB; border: 1px solid #E5E7EB; border-radius: 6px; padding: 12px 16px; margin-bottom: 20px; font-size: 12px; color: #374151; line-height: 1.5;">
                        <div style="font-weight: 800; margin-bottom: 4px; color: #111827;">HƯỚNG DẪN LẤY ID CHÍNH THỨC:</div>
                        <ul style="margin: 0; padding-left: 18px;">
                            <li><strong>Google:</strong> Vào Google Cloud Console > APIs & Services > Credentials > Create "OAuth 2.0 Client ID" (Web). Thêm redirect URI: <code id="oauth-current-redirect-uri" style="font-weight: 700; color: #000000;"></code></li>
                            <li><strong>Facebook:</strong> Vào Meta for Developers > My Apps > Thêm Facebook Login > Cấu hình redirect URI.</li>
                        </ul>
                    </div>

                    <div style="margin-bottom: 16px;">
                        <label style="display: block; font-size: 12px; font-weight: 800; margin-bottom: 6px; color: #111827;">GOOGLE CLIENT ID:</label>
                        <input type="text" id="oauth-input-google" class="form-input" placeholder="Ví dụ: 123456789-abc.apps.googleusercontent.com" style="width: 100%; font-size: 13px;">
                    </div>

                    <div style="margin-bottom: 24px;">
                        <label style="display: block; font-size: 12px; font-weight: 800; margin-bottom: 6px; color: #111827;">FACEBOOK APP ID:</label>
                        <input type="text" id="oauth-input-facebook" class="form-input" placeholder="Ví dụ: 109988776655443" style="width: 100%; font-size: 13px;">
                    </div>

                    <div style="display: flex; gap: 10px; justify-content: flex-end;">
                        <button type="button" onclick="RealOAuth.closeConfigModal()" class="btn btn-secondary" style="font-size: 13px; font-weight: 700; padding: 10px 18px;">Hủy Bỏ</button>
                        <button type="button" onclick="RealOAuth.saveConfigAndProceed()" class="btn btn-primary" style="font-size: 13px; font-weight: 800; padding: 10px 20px; background: #000000; color: #FFFFFF;">Lưu & Tiếp Tục</button>
                    </div>
                </div>
            `;
            document.body.appendChild(modal);
        }

        const currentRedirect = window.location.origin + window.location.pathname;
        const codeElem = document.getElementById('oauth-current-redirect-uri');
        if (codeElem) codeElem.textContent = currentRedirect;

        document.getElementById('oauth-input-google').value = this.getGoogleClientId();
        document.getElementById('oauth-input-facebook').value = this.getFacebookAppId();
        modal.style.display = 'flex';
        modal.dataset.provider = focusProvider || 'GOOGLE';

        if (focusProvider === 'GOOGLE') {
            document.getElementById('oauth-input-google').focus();
        } else if (focusProvider === 'FACEBOOK') {
            document.getElementById('oauth-input-facebook').focus();
        }
    },

    closeConfigModal() {
        const modal = document.getElementById('oauth-config-modal');
        if (modal) modal.style.display = 'none';
    },

    saveConfigAndProceed() {
        const gVal = document.getElementById('oauth-input-google').value.trim();
        const fbVal = document.getElementById('oauth-input-facebook').value.trim();
        this.setGoogleClientId(gVal);
        this.setFacebookAppId(fbVal);

        const modal = document.getElementById('oauth-config-modal');
        const provider = (modal && modal.dataset.provider) ? modal.dataset.provider : 'GOOGLE';
        this.closeConfigModal();

        showToast('Đã lưu cấu hình OAuth!', 'success');

        if (provider === 'GOOGLE') {
            if (gVal) {
                setTimeout(() => this.loginWithGoogle(), 400);
            } else {
                showToast('Chưa nhập Google Client ID.', 'error');
            }
        } else if (provider === 'FACEBOOK') {
            if (fbVal) {
                setTimeout(() => this.loginWithFacebook(), 400);
            } else {
                showToast('Chưa nhập Facebook App ID.', 'error');
            }
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    RealOAuth.init();
});


// ==========================================
// FIREBASE AUTHENTICATION INTEGRATION
// ==========================================
const FirebaseAuthHelper = {
    STORAGE_KEY: 'theshow_firebase_config',
    defaultConfig: {
        apiKey: "AIzaSyDpQU8lkS42uHm0ZkFM5v22zgXubRM9JpY",
        authDomain: "concertticketbooking-2c2d0.firebaseapp.com",
        projectId: "concertticketbooking-2c2d0",
        storageBucket: "concertticketbooking-2c2d0.firebasestorage.app",
        messagingSenderId: "134378842697",
        appId: "1:134378842697:web:2fc10922ed2e9c58fe2d98",
        measurementId: "G-B2EZFHYQGZ"
    },

    getConfig() {
        try {
            const saved = localStorage.getItem(this.STORAGE_KEY);
            if (saved) {
                const parsed = JSON.parse(saved);
                if (parsed.apiKey) return parsed;
            }
        } catch(e) {}
        return this.defaultConfig;
    },

    setConfig(cfg) {
        if (!cfg) {
            localStorage.removeItem(this.STORAGE_KEY);
            return;
        }
        localStorage.setItem(this.STORAGE_KEY, JSON.stringify(cfg));
        this.initFirebase();
    },

    isConfigured() {
        const c = this.getConfig();
        return !!(c && c.apiKey && c.apiKey.trim().length > 10);
    },

    initFirebase() {
        if (typeof firebase === 'undefined') return false;
        const config = this.getConfig();
        if (!config || !config.apiKey) return false;

        try {
            if (!firebase.apps.length) {
                firebase.initializeApp(config);
            }
            return true;
        } catch(e) {
            console.error('Firebase init error:', e);
            return false;
        }
    },

    async loginWithGoogle() {
        if (!this.isConfigured()) {
            this.showConfigModal('GOOGLE');
            return;
        }

        if (!this.initFirebase()) {
            showToast('Không thể khởi tạo Firebase. Vui lòng kiểm tra API Key.', 'error');
            this.showConfigModal('GOOGLE');
            return;
        }

        try {
            showToast('Đang mở cửa sổ đăng nhập Google chính thức...', 'info');
            const provider = new firebase.auth.GoogleAuthProvider();
            provider.setCustomParameters({ prompt: 'select_account' });
            
            const result = await firebase.auth().signInWithPopup(provider);
            const user = result.user;

            await this.completeSocialLogin({
                provider: 'GOOGLE',
                email: user.email,
                fullName: user.displayName || user.email.split('@')[0],
                providerId: user.uid,
                avatarUrl: user.photoURL
            });
        } catch(error) {
            console.error('Firebase Google Error:', error);
            if (error.code === 'auth/popup-closed-by-user') {
                showToast('Bạn đã đóng cửa sổ đăng nhập Google.', 'info');
            } else if (error.code === 'auth/configuration-not-found' || error.code === 'auth/operation-not-allowed') {
                showToast('Chưa bật tính năng Google trong Firebase Console > Authentication > Sign-in method!', 'error');
            } else if (error.code === 'auth/unauthorized-domain') {
                showToast('Domain localhost chưa được thêm vào Authorized domains trong Firebase Console!', 'error');
            } else {
                showToast('Lỗi đăng nhập Google: ' + (error.message || error.code), 'error');
            }
        }
    },

    async loginWithFacebook() {
        if (!this.isConfigured()) {
            this.showConfigModal('FACEBOOK');
            return;
        }

        if (!this.initFirebase()) {
            showToast('Không thể khởi tạo Firebase. Vui lòng kiểm tra cấu hình.', 'error');
            this.showConfigModal('FACEBOOK');
            return;
        }

        try {
            showToast('Đang mở cửa sổ đăng nhập Facebook chính thức...', 'info');
            const provider = new firebase.auth.FacebookAuthProvider();
            const result = await firebase.auth().signInWithPopup(provider);
            const user = result.user;

            await this.completeSocialLogin({
                provider: 'FACEBOOK',
                email: user.email || (user.uid + '@facebook.com'),
                fullName: user.displayName || 'Người dùng Facebook',
                providerId: user.uid,
                avatarUrl: user.photoURL
            });
        } catch(error) {
            console.error('Firebase Facebook Error:', error);
            if (error.code === 'auth/popup-closed-by-user') {
                showToast('Bạn đã đóng cửa sổ đăng nhập Facebook.', 'info');
            } else if (error.code === 'auth/operation-not-allowed') {
                showToast('Chưa bật Facebook trong Firebase Console > Authentication > Sign-in method!', 'error');
            } else {
                showToast('Lỗi đăng nhập Facebook: ' + (error.message || error.code), 'error');
            }
        }
    },

    async completeSocialLogin(profile) {
        showToast(`Đang hoàn tất đăng ký & đăng nhập cho ${profile.email}...`, 'info');
        try {
            const res = await apiFetch('/auth/social-login', {
                method: 'POST',
                body: JSON.stringify(profile)
            });

            Auth.setToken(res.data.token);
            Auth.setUser({
                id: res.data.userId,
                username: res.data.username,
                email: res.data.email,
                fullName: res.data.fullName,
                role: res.data.role
            });

            showToast(`Đăng nhập thành công với tài khoản ${profile.provider} (${profile.fullName})!`, 'success');
            setTimeout(() => {
                window.location.href = '/user.html';
            }, 1000);
        } catch (err) {
            showToast(err.message, 'error');
        }
    },

    showConfigModal(focusProvider) {
        let modal = document.getElementById('firebase-config-modal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'firebase-config-modal';
            modal.style.cssText = 'position: fixed; inset: 0; background: rgba(0,0,0,0.6); display: flex; align-items: center; justify-content: center; z-index: 9999; padding: 20px;';
            modal.innerHTML = `
                <div style="background: #FFFFFF; border: 1px solid #000000; border-radius: 8px; width: 100%; max-width: 540px; padding: 28px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #E5E7EB; padding-bottom: 12px;">
                        <h3 style="margin: 0; font-size: 16px; font-weight: 900; color: #000000; text-transform: uppercase; letter-spacing: 0.5px;">CẤU HÌNH FIREBASE AUTHENTICATION THẬT</h3>
                        <button type="button" onclick="FirebaseAuthHelper.closeConfigModal()" style="background: none; border: none; font-size: 20px; font-weight: 700; cursor: pointer; color: #6B7280;">✕</button>
                    </div>
                    
                    <p style="font-size: 13px; color: #4B5563; line-height: 1.6; margin-bottom: 16px;">
                        Dự án Firebase của bạn: <strong>concertticketbooking-2c2d0</strong>.<br>
                        Nhập <strong>API Key</strong> (lấy từ Firebase Console > Project settings) để kết nối đăng nhập Google & Facebook thật.
                    </p>

                    <div style="background: #F9FAFB; border: 1px solid #E5E7EB; border-radius: 6px; padding: 12px 16px; margin-bottom: 18px; font-size: 12px; color: #374151; line-height: 1.5;">
                        <div style="font-weight: 800; margin-bottom: 4px; color: #111827;">HƯỚNG DẪN 2 BƯỚC NHANH TRÊN FIREBASE:</div>
                        <ol style="margin: 0; padding-left: 18px;">
                            <li>Vào <a href="https://console.firebase.google.com/project/concertticketbooking-2c2d0/authentication/providers" target="_blank" style="color: #000000; font-weight: 700; text-decoration: underline;">Authentication > Sign-in method</a>: Bật <strong>Google</strong>.</li>
                            <li>Vào <a href="https://console.firebase.google.com/project/concertticketbooking-2c2d0/settings/general" target="_blank" style="color: #000000; font-weight: 700; text-decoration: underline;">Project settings</a> > Cuộn xuống <strong>Your apps</strong> > Tạo Web app và dán <code>apiKey</code> vào ô dưới:</li>
                        </ol>
                    </div>

                    <div style="margin-bottom: 14px;">
                        <label style="display: block; font-size: 12px; font-weight: 800; margin-bottom: 6px; color: #111827;">API KEY (BẮT BUỘC):</label>
                        <input type="text" id="firebase-input-apikey" class="form-input" placeholder="Ví dụ: AIzaSyA..." style="width: 100%; font-size: 13px;">
                    </div>

                    <div style="margin-bottom: 14px;">
                        <label style="display: block; font-size: 12px; font-weight: 800; margin-bottom: 6px; color: #111827;">APP ID (TÙY CHỌN):</label>
                        <input type="text" id="firebase-input-appid" class="form-input" placeholder="Ví dụ: 1:123456789:web:abcdef..." style="width: 100%; font-size: 13px;">
                    </div>

                    <div style="margin-bottom: 22px;">
                        <label style="display: block; font-size: 12px; font-weight: 800; margin-bottom: 6px; color: #111827;">HOẶC DÁN NGUYÊN ĐOẠN FIREBASECONFIG JSON / JS:</label>
                        <textarea id="firebase-input-json" class="form-input" placeholder='const firebaseConfig = { apiKey: "AIzaSy...", ... };' style="width: 100%; height: 60px; font-size: 11px; font-family: monospace;"></textarea>
                    </div>

                    <div style="display: flex; gap: 10px; justify-content: flex-end;">
                        <button type="button" onclick="FirebaseAuthHelper.closeConfigModal()" class="btn btn-secondary" style="font-size: 13px; font-weight: 700; padding: 10px 18px;">Hủy Bỏ</button>
                        <button type="button" onclick="FirebaseAuthHelper.saveAndProceed()" class="btn btn-primary" style="font-size: 13px; font-weight: 800; padding: 10px 20px; background: #000000; color: #FFFFFF;">Lưu & Đăng Nhập Ngay</button>
                    </div>
                </div>
            `;
            document.body.appendChild(modal);
        }

        const current = this.getConfig();
        document.getElementById('firebase-input-apikey').value = current.apiKey || '';
        document.getElementById('firebase-input-appid').value = current.appId || '';
        document.getElementById('firebase-input-json').value = '';

        modal.style.display = 'flex';
        modal.dataset.provider = focusProvider || 'GOOGLE';
        document.getElementById('firebase-input-apikey').focus();
    },

    closeConfigModal() {
        const modal = document.getElementById('firebase-config-modal');
        if (modal) modal.style.display = 'none';
    },

    saveAndProceed() {
        const rawJson = document.getElementById('firebase-input-json').value.trim();
        let newConfig = { ...this.defaultConfig };

        if (rawJson) {
            const apiKeyMatch = rawJson.match(/apiKey\s*[:=]\s*["']([^"']+)["']/);
            const appIdMatch = rawJson.match(/appId\s*[:=]\s*["']([^"']+)["']/);
            const authDomainMatch = rawJson.match(/authDomain\s*[:=]\s*["']([^"']+)["']/);
            const projectIdMatch = rawJson.match(/projectId\s*[:=]\s*["']([^"']+)["']/);

            if (apiKeyMatch) newConfig.apiKey = apiKeyMatch[1];
            if (appIdMatch) newConfig.appId = appIdMatch[1];
            if (authDomainMatch) newConfig.authDomain = authDomainMatch[1];
            if (projectIdMatch) newConfig.projectId = projectIdMatch[1];
        }

        const apiKeyInput = document.getElementById('firebase-input-apikey').value.trim();
        const appIdInput = document.getElementById('firebase-input-appid').value.trim();
        if (apiKeyInput) newConfig.apiKey = apiKeyInput;
        if (appIdInput) newConfig.appId = appIdInput;

        if (!newConfig.apiKey) {
            showToast('Vui lòng nhập API Key từ Firebase Console!', 'error');
            return;
        }

        this.setConfig(newConfig);
        const modal = document.getElementById('firebase-config-modal');
        const provider = (modal && modal.dataset.provider) ? modal.dataset.provider : 'GOOGLE';
        this.closeConfigModal();

        showToast('Đã lưu cấu hình Firebase thành công!', 'success');

        setTimeout(() => {
            if (provider === 'GOOGLE') this.loginWithGoogle();
            else this.loginWithFacebook();
        }, 500);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    FirebaseAuthHelper.initFirebase();
});
