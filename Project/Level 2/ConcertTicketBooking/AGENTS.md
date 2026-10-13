# THE SHOW (Concert Ticket Booking) — Instructions for AI Agents & Developers

This document defines the durable engineering standards, domain invariants, and visual style rules for the **THE SHOW** project. Future AI agents and developers must strictly follow these instructions.

---

## 1. Purpose & Product Model

**THE SHOW** is a high-concurrency concert ticket booking platform.
- **Audience**: Fans seeking authentic concert tickets, venue organizers, and system administrators.
- **Core User Flow**:
  1. Explore upcoming concerts and tours with multimedia previews.
  2. Select seats visually on a real-time interactive seat map (VIP, Standard, GA).
  3. **10-Minute Seat Hold Mechanism**: Locking selected seats so no other user can double-book them while checkout is underway.
  4. Voucher application and payment confirmation (VNPAY-QR, VietQR, MoMo).
  5. Immediate issuance of unique digital tickets with verifiable QR codes, stored in user accounts and emailed via the automated Email Bot.

---

## 2. Technology Stack & Architectural Constraints

- **Language & Framework**: Java 21, Spring Boot 3.2.0, Spring Security 6, Spring Data MongoDB.
- **Persistence**: MongoDB 7 (running on Docker container `mongodb-concert` at `localhost:27017`).
- **Frontend Architecture**: Vanilla HTML5, Vanilla JavaScript (ES6+), Tailwind CSS (via CDN), Google Fonts Inter.
- **No Heavy Frontend Frameworks**: Do not introduce React, Vue, Angular, or complex bundlers unless explicitly authorized by the user. Keep frontend files lightweight, static, and served by Spring Boot under `src/main/resources/static/`.

---

## 3. Strict Frontend & Visual Design Rules

Future agents MUST maintain the following design invariants:

1. **Monochrome High-End Palette**:
   - Primary Background: Pure White (`#FFFFFF`), subtle section separation (`#F9FAFB` / `#FAFAFA`).
   - Typography: Black (`#000000` / `#111827`), secondary subtitles (`#4B5563` / `#6B7280`).
   - Primary Buttons: Solid Black (`#000000`) background, White (`#FFFFFF`) text, hover `#262626`.
   - Secondary / Outline Buttons: White background with black border (`#000000`) and black text.

2. **ZERO Icons, ZERO Emojis**:
   - **Strictly NO icon libraries**: FontAwesome, Lucide, Iconify, Heroicons, Bootstrap Icons are forbidden.
   - **Strictly NO emojis**: Characters like 🎫, 🔒, 💳, 🎤, 📍, 🗓, ⚡, 🤖, 📋, 💡 are forbidden everywhere across the app. Use clean typography, numbered badges, or structured text labels instead.

3. **Toast Notifications Positioning**:
   - Success / Error / Info toasts must ALWAYS be anchored at **TOP CENTER** (`#toast-container`: `top: 24px; left: 50%; transform: translateX(-50%)`). Never move it to bottom-right or top-right.

4. **Brand Logo & Favicon**:
   - Only use `/images/logo-the-show.png` and `/favicon.ico`.
   - NEVER display the default Tomcat cat logo.
   - NEVER create or display arbitrary letter "T" square icons next to the logo.

5. **Full Vietnamese Accents**:
   - All Vietnamese copy on buttons, navigation, forms, and alerts must have complete and accurate accents (e.g., `Đăng nhập`, `Đăng ký`, `Đăng xuất`, `Vé của tôi`, `Quản trị`).

6. **Hero Video Visibility**:
   - The background video (`/video.mp4`) must be displayed clearly, vividly, and without heavy milky fog. Keep the overlay minimal (soft vignette) and place text inside a clean frosted glass card for maximum legibility.

---

## 4. Database & Concurrency Rules

- **Collections**:
  - `users`: Credentials, full names, roles (`ROLE_CUSTOMER`, `ROLE_ADMIN`).
  - `concerts`: Title, artist, venue, date, poster, pricing tiers.
  - `seats`: Seat code, row, number, status (`AVAILABLE`, `HELD`, `SOLD`), tier, current booking ID.
  - `bookings`: Booking code, user ID, seat IDs, total amount, discount amount, final amount, status (`HOLDING`, `PAID`, `CANCELLED`, `EXPIRED`), expire timestamp (`expiresAt`).
  - `tickets`: Ticket code, seat code, price, QR code data string, issued date.
  - `vouchers`: Code, discount percent, limits, valid date.
  - `email_bot_configs`: Dynamic Gmail App Password config managed by Admin.

- **Concurrency & 10-Minute Hold Invariants**:
  - Seat holding must be atomic. If any requested seat is already `HELD` or `SOLD`, the hold operation must fail immediately with a conflict message.
  - Expired hold cleanup is automated via `BookingScheduler` running `@Scheduled(fixedRate = 60000)`. It checks for `HOLDING` bookings with `expiresAt < now`, sets status to `EXPIRED`, and releases seats back to `AVAILABLE`.

---

## 5. Security & Authorization Invariants

- **Stateless JWT**: Bearer token stored in `localStorage` by `app.js` and included in HTTP `Authorization: Bearer <token>`.
- **Public Endpoints**:
  - `/api/v1/auth/**`: Register, Login.
  - `GET /api/v1/concerts/**`, `GET /api/v1/seats/**`: Public browse.
  - Static resources: `/*.html`, `/*.mp4`, `/*.jpg`, `/*.png`, `/css/**`, `/js/**`, `/images/**`, `/policies/**`, `/favicon.ico`.
- **Protected Endpoints**:
  - `/api/v1/bookings/**`, `/api/v1/tickets/**`: Requires authenticated user.
  - `/api/v1/admin/**`: Requires `ROLE_ADMIN`.

---

## 6. Testing & Build Instructions

- To run unit and integration tests:
  ```powershell
  mvn test
  ```
  Ensure all 9 tests pass with `BUILD SUCCESS` before completing any task.
- To run the application locally:
  ```powershell
  mvn spring-boot:run
  ```
  Access live at: `http://localhost:8080/index.html`.
