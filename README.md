# 🅿️ PapaPark - Smart Parking & Automated Reservation System

![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green?style=for-the-badge&logo=springboot)
![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![MongoDB](https://img.shields.io/badge/MongoDB-Local%2FAtlas-47A248?style=for-the-badge&logo=mongodb)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**PapaPark** is a full-stack, enterprise-grade **Smart Parking Management & Automated Reservation System** built with **Spring Boot 3, Spring Security, MongoDB, Thymeleaf, and Futuristic 3D Visual Design**. It features real-time slot tracking, 3D isometric deck visualization, Razorpay test mode integration, 1-click PDF invoices, SMS & Email alerts, 6-digit OTP password recovery, digital QR entry passes, and postpaid pay-later billing.

---

## 🌟 Key Features

### 🕶️ 1. 3D Isometric Visual Parking Deck
- **Dynamic 3D Visual Map**: Interactive parking lot deck with 3D elevation effects (`rotateX(25deg) rotateZ(-6deg)`), hover lift (`translateZ(18px)`), and 3D animated active vehicle badges (`🚗`).
- **Live 5s Polling**: Automatic background synchronization every 5 seconds keeping available and occupied slot states updated live.
- **Flat 2D / 3D Toggle**: Switch seamlessly between 3D Isometric View and Flat 2D Grid View.

### 📅 2. Slot Reservation & QR Barrier Pass
- **Advance Booking**: Book slots for immediate parking or reserve for a future date & time.
- **Automated QR Gate Pass**: Generates a digital QR Pass (`PASS-XXXXXXXX`) upon booking with an interactive **"📷 Barrier Gate Scanner Simulator"** for gate entry and exit.

### 💳 3. Payment Gateway & Razorpay Integration
- **Razorpay Checkout SDK**: Seamless test mode checkout via `checkout.razorpay.com/v1/checkout.js` with test key `rzp_test_SmartPark123`.
- **Interactive PaySecure Modal**: Built-in payment gateway for Credit/Debit Cards, UPI QR Codes (GPay/PhonePe/Paytm), and 3D Secure OTP verification (`123456`).

### 📄 4. Downloadable PDF Tax Invoices
- **1-Click PDF Export**: Uses `html2pdf.js` on payment receipts to instantly download official digital tax invoices with transaction IDs, itemized breakdowns, and GSTIN details.

### ✉️ 5. Email & SMS Notifications
- **Spring Mail & FastSMS Service**: Automatic dispatch of Email & SMS notifications for:
  - 🚗 **Booking Confirmation**
  - 💳 **Payment Receipts**
  - ⏱️ **Live Duration Milestone Warnings (5+ mins / 1 hr)**
  - 🔐 **Password Reset 6-Digit OTP**

### 🔐 6. Forgot Password with OTP Recovery
- **OTP Account Recovery**: Dedicated password reset flow at `/forgot-password` with 6-digit OTP verification.

### 💰 7. Digital Wallet & Multiple Payment Sources
- **Card & UPI Wallet Top-Up**: Recharge digital wallet using Credit/Debit Cards, UPI QR Codes, or Net Banking.
- **Quick Amount Chips**: 1-click top-up chips (`+₹100`, `+₹200`, `+₹500`, `+₹1,000`).
- **100% History Retention**: DBRef fallback stream filtering ensuring 0 dropped transaction records.

### ⏳ 8. Postpaid (Pay-Later) Billing & Mandatory Rule
- **Pay-Later Checkout**: Allows drivers to release their slot immediately and defer payment.
- **Mandatory Next-Session Settlement Rule**: Users with unpaid postpaid dues are **strictly blocked** from starting a new parking session until outstanding dues are paid via 1-click settlement.

### 📊 9. Admin Monitoring & Chart.js Analytics
- **3 Dynamic Analytics Charts** on `/admin/dashboard`:
  - 🟢🔴 **Slot Occupancy Rate** (Doughnut Chart)
  - 📈 **Weekly Revenue Trends (₹)** (Bar Chart)
  - 🚗 **Vehicle Type Distribution** (Pie Chart)
- **Live System Activity Feed & User Blocking/Unblocking controls**.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Backend Framework** | Spring Boot 3.2.3, Spring MVC, Spring Data MongoDB |
| **Security** | Spring Security, JWT (JSON Web Tokens), BCrypt Password Encoder |
| **Database** | MongoDB (`smart_parking_db`) |
| **Frontend Templates** | Thymeleaf, HTML5, Vanilla CSS3 (Glassmorphic Design System), JavaScript (ES6+) |
| **3D & Visuals** | CSS3 3D Isometric Transforms, HTML5 Canvas 3D Particle Grid |
| **Analytics & PDF** | Chart.js 4.x, html2pdf.js, Razorpay Checkout SDK |
| **Notifications** | Spring Boot Starter Mail, FastSMS / Twilio Simulation Service |

---

## 🔑 Pre-Configured Demo Credentials

Upon startup, `DataInitializer` automatically seeds 12 parking slots and 2 default user accounts:

| Role | Email | Password | Initial Wallet Balance | Access Level |
|---|---|---|---|---|
| 👑 **System Admin** | `admin@smartpark.com` | `admin123` | ₹1,000.00 | Full Admin Panel & Slot Control |
| 👤 **Demo User** | `user@smartpark.com` | `user123` | ₹500.00 | User Dashboard, Parking & Wallet |

---

## 🚀 Quick Start Guide

### Prerequisites
- **JDK 17+** (JDK 25 / JDK 26 compatible)
- **MongoDB** running locally on `localhost:27017`

### 1. Clone the Repository
```bash
git clone https://github.com/Advik-harsha/PapaPark.git
cd PapaPark
```

### 2. Start MongoDB
Ensure MongoDB service is active. The default URI is:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/smart_parking_db
```

### 3. Build & Run Application

#### On Windows (PowerShell):
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-26.0.2"
.\mvnw.cmd compile spring-boot:run
```

#### On Linux / macOS:
```bash
./mvnw compile spring-boot:run
```

### 4. Access the Web Application
Open your browser and navigate to:
- **Main App**: [http://localhost:8080/login](http://localhost:8080/login)
- **Forgot Password**: [http://localhost:8080/forgot-password](http://localhost:8080/forgot-password)
- **Admin Dashboard**: [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)

---

## 📑 REST API Endpoint Reference

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/login` | User login & JWT issuance | Public |
| `POST` | `/api/auth/register` | User account registration | Public |
| `POST` | `/api/auth/forgot-password` | Request password reset 6-digit OTP | Public |
| `POST` | `/api/auth/reset-password` | Verify OTP & update password | Public |
| `GET` | `/api/parking/slots/all` | Fetch all parking slots | User / Admin |
| `POST` | `/api/parking/start` | Start parking session / reserve slot | User |
| `POST` | `/api/parking/end/{id}` | End active parking session & calculate bill | User |
| `POST` | `/api/payments/process` | Process payment (Card, UPI, Wallet, Postpaid) | User |
| `POST` | `/api/wallet/add-money` | Top-up digital wallet | User |
| `GET` | `/api/wallet/history/{userId}` | Fetch user wallet transaction history | User |

---

## 📜 License

Distributed under the **MIT License**. See `LICENSE` for details.

---
⭐ **Star this repository if you found PapaPark helpful!**
