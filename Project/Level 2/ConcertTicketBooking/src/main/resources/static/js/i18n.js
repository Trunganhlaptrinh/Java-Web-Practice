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
