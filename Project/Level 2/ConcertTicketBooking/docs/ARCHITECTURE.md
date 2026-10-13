# THE SHOW (Concert Ticket Booking) — Architecture & Technical Specifications

This document outlines the architecture, data models, workflows, and API specifications for **THE SHOW**.

---

## 1. High-Level Architecture

```text
┌───────────────────────────────────────────────────────────────────┐
│                           CLIENT BROWSER                          │
│  Tailwind CSS · Vanilla JS · HTML5 · Web Crypto / Pure Canvas QR  │
└──────────────────┬─────────────────────────────▲──────────────────┘
                   │ HTTPS / REST (JSON)         │ Live Toasts
                   ▼                             │ Static HTML
┌────────────────────────────────────────────────┴──────────────────┐
│                   SPRING BOOT APPLICATION (PORT 8080)             │
│                                                                   │
│  ┌───────────────────────┐       ┌─────────────────────────────┐  │
│  │   Security Filter     │       │    Scheduled Background     │  │
│  │  JwtAuthentication    │       │      BookingScheduler       │  │
│  │  BCrypt PasswordHash  │       │  (Auto-releases expired)    │  │
│  └───────────┬───────────┘       └──────────────┬──────────────┘  │
│              ▼                                  ▼                 │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                  Service Layer (Business Logic)             │  │
│  │   Auth · Concert · Booking (10m Hold) · Ticket · EmailBot   │  │
│  └───────────────────────────┬─────────────────────────────────┘  │
│                              ▼                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │               Repository Layer (Spring Data MongoDB)         │  │
│  └───────────────────────────┬─────────────────────────────────┘  │
└──────────────────────────────┼────────────────────────────────────┘
                               ▼ MongoDB Wire Protocol (Port 27017)
┌───────────────────────────────────────────────────────────────────┐
│                     MONGODB DATABASE (DOCKER)                     │
│  Collections: users · concerts · seats · bookings · tickets · ... │
└───────────────────────────────────────────────────────────────────┘
```

---

## 2. Core Workflows & Sequence Diagrams

### 2.1. 10-Minute Seat Hold & Checkout Flow

```text
User                      Frontend                        Backend                         MongoDB
 │                           │                               │                               │
 │── Select Seats (max 4) ──>│                               │                               │
 │                           │── POST /bookings/hold ───────>│                               │
 │                           │   { concertId, seatIds }      │── Query Seat Status ─────────>│
 │                           │                               │<── Seats: AVAILABLE ──────────│
 │                           │                               │── Set Seats: HELD ───────────>│
 │                           │                               │── Insert Booking: HOLDING ───>│
 │                           │<── 200 OK (Booking + Timer) ──│                               │
 │                           │                               │                               │
 │── Apply Voucher (optional)│── POST /apply-voucher ───────>│── Validate Voucher ──────────>│
 │                           │<── 200 OK (Updated Bill) ─────│                               │
 │                           │                               │                               │
 │── Confirm Payment ───────>│── POST /pay ─────────────────>│── Verify Booking != EXPIRED ─>│
 │                           │   { paymentMethod }           │── Update Booking: PAID ───────>│
 │                           │                               │── Update Seats: SOLD ─────────>│
 │                           │                               │── Generate Tickets + QR ─────>│
 │                           │                               │── Async Trigger Email Bot ───>│
 │                           │<── 200 OK (Ticket Issued) ────│                               │
 │── View Ticket & QR ──────>│                               │                               │
```

---

## 3. Database Schema Specifications (MongoDB)

### Collection: `users`
- `id`: String (ObjectId)
- `username`: String (Unique)
- `password`: String (BCrypt hashed)
- `email`: String (Unique)
- `fullName`: String
- `phone`: String
- `role`: String (`CUSTOMER`, `ADMIN`)
- `createdAt`, `updatedAt`: Date

### Collection: `concerts`
- `id`: String (ObjectId)
- `title`: String
- `artist`: String
- `venue`: String
- `startTime`: Date
- `posterUrl`: String
- `tiers`: List of `{ name, price, color, totalSeats }`
- `status`: String (`OPEN_FOR_SALE`, `SOLD_OUT`, `COMPLETED`)

### Collection: `seats`
- `id`: String (ObjectId)
- `concertId`: String
- `seatCode`: String (e.g., `A-01`, `VIP-05`)
- `row`: String
- `number`: Integer
- `tierName`: String
- `price`: Double
- `status`: String (`AVAILABLE`, `HELD`, `SOLD`)
- `currentBookingId`: String (Nullable)

### Collection: `bookings`
- `id`: String (ObjectId)
- `bookingCode`: String (e.g., `BKG-1791884627579-36EF5`)
- `userId`: String
- `concertId`: String
- `seatIds`: List<String>
- `totalAmount`: Double
- `discountAmount`: Double
- `finalAmount`: Double
- `status`: String (`HOLDING`, `PAID`, `CANCELLED`, `EXPIRED`)
- `expiresAt`: Date (Current time + 10 minutes)
- `createdAt`: Date

### Collection: `tickets`
- `id`: String (ObjectId)
- `ticketCode`: String (e.g., `TCK-1791884627626-DF5A`)
- `bookingId`: String
- `userId`: String
- `concertId`: String
- `seatCode`: String
- `tierName`: String
- `price`: Double
- `qrCodeData`: String (JSON payload signed for verification)
- `status`: String (`VALID`, `USED`, `REVOKED`)

---

## 4. REST API Endpoint Reference

| Method | Endpoint | Access | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/auth/register` | Public | Register new customer account |
| `POST` | `/api/v1/auth/login` | Public | Authenticate user & return JWT token |
| `GET` | `/api/v1/concerts` | Public | Get all active concerts |
| `GET` | `/api/v1/concerts/{id}` | Public | Get concert details & tier definitions |
| `GET` | `/api/v1/concerts/{id}/seats` | Public | Get real-time seat map and status |
| `POST` | `/api/v1/bookings/hold` | User | Lock up to 4 seats for 10 minutes |
| `POST` | `/api/v1/bookings/{id}/apply-voucher` | User | Apply discount code to booking |
| `POST` | `/api/v1/bookings/{id}/pay` | User | Complete payment & issue tickets |
| `GET` | `/api/v1/tickets/my` | User | Fetch all digital tickets of current user |
| `GET` | `/api/v1/admin/stats` | Admin | Fetch system revenue & booking totals |
| `GET` | `/api/v1/admin/bookings` | Admin | Fetch all system orders |
| `POST` | `/api/v1/admin/email-bot` | Admin | Configure live Gmail bot credentials |
