/**
 * Smart Parking System - Frontend Javascript Utilities
 * Handles API calls, authentication, and UI updates
 */

class ApiClient {
    constructor() {
        this.BASE_URL = ''; // Same origin
    }

    getToken() {
        return localStorage.getItem('token');
    }

    getUser() {
        const userStr = localStorage.getItem('user');
        if (!userStr) return null;
        try {
            return JSON.parse(userStr);
        } catch (e) {
            return null;
        }
    }

    isLoggedIn() {
        return !!this.getToken();
    }

    isAdmin() {
        const user = this.getUser();
        return user && user.role === 'ROLE_ADMIN';
    }

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
    }

    getUserId() {
        return this.getUser()?.id;
    }

    async login(email, password) {
        try {
            const response = await this.post('/api/auth/login', { email, password });
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify({
                id: response.id,
                email: response.email,
                fullName: response.fullName,
                role: response.role
            }));
            
            showAlert("Login successful", "success");
            setTimeout(() => {
                window.location.href = '/dashboard';
            }, 1000);
            return true;
        } catch (error) {
            showAlert(error.message, "danger");
            return false;
        }
    }

    async register(data) {
        try {
            await this.post('/api/auth/register', data);
            showAlert("Registration successful! Please login.", "success");
            setTimeout(() => {
                window.location.href = '/login';
            }, 1500);
            return true;
        } catch (error) {
            showAlert(error.message, "danger");
            return false;
        }
    }

    async request(method, url, body = null) {
        const headers = {
            'Content-Type': 'application/json'
        };

        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const options = {
            method,
            headers
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        try {
            const response = await fetch(this.BASE_URL + url, options);

            if (response.status === 401 && !url.includes('/api/auth/')) {
                this.logout();
                throw new Error("Session expired. Please login again.");
            }

            if (response.status === 403) {
                showAlert("Access Denied", "danger");
                throw new Error("Access Denied");
            }

            if (response.status === 204) {
                return null;
            }

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                const errorMsg = data.message || data.error || 'An error occurred';
                throw new Error(errorMsg);
            }

            return data;
        } catch (error) {
            console.error(`API Error (${method} ${url}):`, error);
            throw error;
        }
    }

    async get(url) { return this.request('GET', url); }
    async post(url, body) { return this.request('POST', url, body); }
    async put(url, body) { return this.request('PUT', url, body); }
    async delete(url) { return this.request('DELETE', url); }
}

const api = new ApiClient();

/* --- Auth Helper Functions --- */
async function login(email, password) { return api.login(email, password); }
async function register(data) { return api.register(data); }

/* --- UI Utilities --- */
function showAlert(message, type = 'info') {
    let container = document.getElementById('alert-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'alert-container';
        document.body.appendChild(container);
    }

    const alertEl = document.createElement('div');
    alertEl.className = `alert alert-${type}`;
    
    let icon = 'ℹ️';
    if (type === 'success') icon = '✅';
    if (type === 'danger') icon = '❌';
    if (type === 'warning') icon = '⚠️';

    alertEl.innerHTML = `<span>${icon}</span> <span>${message}</span>`;
    container.appendChild(alertEl);

    setTimeout(() => {
        alertEl.classList.add('hiding');
        setTimeout(() => alertEl.remove(), 300);
    }, 4000);
}

function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('en-IN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatCurrency(amount) {
    if (amount === null || amount === undefined) return '₹0.00';
    return '₹' + Number(amount).toFixed(2);
}

/* --- Page Guards --- */
function requireAuth() {
    if (!api.isLoggedIn()) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

function requireAdmin() {
    if (!requireAuth()) return false;
    if (!api.isAdmin()) {
        showAlert("Admin access required", "danger");
        setTimeout(() => {
            window.location.href = '/dashboard';
        }, 1000);
        return false;
    }
    return true;
}

function initSidebar() {
    const toggleBtn = document.getElementById('mobile-menu-toggle');
    const sidebar = document.getElementById('sidebar');
    
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });

        document.addEventListener('click', (e) => {
            if (window.innerWidth <= 768 && 
                !sidebar.contains(e.target) && 
                !toggleBtn.contains(e.target) && 
                sidebar.classList.contains('open')) {
                sidebar.classList.remove('open');
            }
        });
    }

    // Set active nav link based on current URL
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
        } else if (href === '/' && currentPath === '/') {
            link.classList.add('active');
        }
    });

    // Handle Admin section visibility properly
    const adminSections = document.querySelectorAll('.admin-only');
    if (api.isAdmin()) {
        adminSections.forEach(el => el.style.display = 'block');
    } else {
        adminSections.forEach(el => el.style.display = 'none');
    }
}

/* --- Global Interactive Payment Gateway --- */

function createGatewayModalHTML() {
    if (document.getElementById('gatewayModalOverlay')) return;

    const div = document.createElement('div');
    div.id = 'gatewayModalOverlay';
    div.className = 'modal-overlay';
    div.innerHTML = `
        <div class="modal glass-panel" style="max-width: 520px;">
            <div class="modal-header" style="background: linear-gradient(135deg, #00d4ff, #0099ff); color: white;">
                <h3>🛡️ SmartPark PaySecure Gateway</h3>
                <button class="btn-close" onclick="closeGatewayModal()" style="color: white;">&times;</button>
            </div>
            <div class="modal-body" style="padding: 20px;">
                <div style="text-align: center; margin-bottom: 20px;">
                    <span style="color: var(--text-secondary); font-size: 0.9rem;">Amount Payable</span>
                    <h1 id="gtwAmountDisplay" style="font-size: 2.2rem; color: var(--accent-primary); margin: 5px 0;">₹0.00</h1>
                    <span id="gtwSessionTag" class="badge badge-info">Session ID: -</span>
                </div>

                <!-- Payment Tabs -->
                <div style="display: flex; gap: 10px; border-bottom: 1px solid var(--glass-border); margin-bottom: 20px; flex-wrap: wrap;">
                    <button id="tabBtnCard" class="btn btn-sm btn-primary" onclick="switchGtwTab('CARD')">💳 Card</button>
                    <button id="tabBtnUpi" class="btn btn-sm btn-secondary" onclick="switchGtwTab('UPI')">📱 UPI / QR</button>
                    <button id="tabBtnWallet" class="btn btn-sm btn-secondary" onclick="switchGtwTab('WALLET')">💰 Wallet</button>
                    <button id="tabBtnPostpaid" class="btn btn-sm btn-secondary" onclick="switchGtwTab('POSTPAID')">⏳ Postpaid (Pay Later)</button>
                </div>

                <!-- Tab 1: Card Form -->
                <div id="gtwTabCard" class="gtw-tab-content">
                    <div class="form-group">
                        <label>Card Number</label>
                        <input type="text" id="gtwCardNum" class="form-input" placeholder="4532 ···· ···· 8901" maxlength="19">
                    </div>
                    <div style="display: flex; gap: 15px;">
                        <div class="form-group" style="flex: 1;">
                            <label>Expiry Date</label>
                            <input type="text" id="gtwCardExp" class="form-input" placeholder="MM/YY" maxlength="5">
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label>CVV</label>
                            <input type="password" id="gtwCardCvv" class="form-input" placeholder="•••" maxlength="3">
                        </div>
                    </div>
                </div>

                <!-- Tab 2: UPI QR Code -->
                <div id="gtwTabUpi" class="gtw-tab-content" style="display: none; text-align: center;">
                    <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 10px;">Scan QR with GPay / PhonePe / Paytm or enter UPI ID</p>
                    <div style="background: white; padding: 15px; border-radius: 12px; display: inline-block; margin-bottom: 15px;">
                        <img src="https://api.qrserver.com/v1/create-qr-code/?size=140x140&data=upi://pay?pa=smartpark@upi" alt="UPI QR" style="width: 140px; height: 140px;">
                    </div>
                    <div class="form-group" style="text-align: left;">
                        <label>UPI ID</label>
                        <input type="text" id="gtwUpiId" class="form-input" placeholder="username@upi">
                    </div>
                </div>

                <!-- Tab 3: Wallet -->
                <div id="gtwTabWallet" class="gtw-tab-content" style="display: none;">
                    <div style="background: rgba(0, 212, 255, 0.1); border: 1px solid var(--accent-primary); padding: 15px; border-radius: 8px; text-align: center;">
                        <p style="margin: 0; color: var(--text-secondary);">SmartPark Digital Wallet Balance</p>
                        <h3 id="gtwWalletBalance" style="color: var(--success); margin: 5px 0;">₹0.00</h3>
                        <p style="font-size: 0.8rem; margin: 0; color: var(--text-muted);">Amount will be instantly deducted upon authorization.</p>
                    </div>
                </div>

                <!-- Tab 4: Postpaid / Pay Later -->
                <div id="gtwTabPostpaid" class="gtw-tab-content" style="display: none;">
                    <div style="background: rgba(255, 171, 0, 0.1); border: 1px solid var(--warning); padding: 15px; border-radius: 8px; text-align: center;">
                        <span style="font-size: 2rem;">⏳</span>
                        <h4 style="color: var(--warning); margin: 5px 0;">Postpaid Pay-Later Mode Selected</h4>
                        <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 5px;">Your parking slot will be released immediately.</p>
                        <p style="font-size: 0.8rem; color: #ffaa00; font-weight: 600;">⚠️ MANDATORY RULE: Outstanding Postpaid dues MUST be paid before you can book another parking slot in the future!</p>
                    </div>
                </div>


                <!-- OTP Screen (Hidden initially) -->
                <div id="gtwOtpScreen" style="display: none; text-align: center; padding: 20px 0;">
                    <span style="font-size: 3rem;">🔐</span>
                    <h4>3D Secure OTP Verification</h4>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">Enter the 6-digit OTP sent to your registered mobile number</p>
                    <input type="text" id="gtwOtpInput" class="form-input" style="width: 180px; font-size: 1.5rem; text-align: center; letter-spacing: 4px; margin: 15px auto;" maxlength="6" value="123456">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="closeGatewayModal()">Cancel</button>
                <button id="gtwPayBtn" class="btn btn-primary" onclick="processGtwAction()">Pay Now</button>
            </div>
        </div>
    `;
    document.body.appendChild(div);
}

let activeGtwSessionId = null;
let activeGtwAmount = 0;
let activeGtwMethod = 'PAY_NOW';
let activeGtwCallback = null;
let gtwStep = 'INPUT'; // 'INPUT' or 'OTP'

function switchGtwTab(tab) {
    document.getElementById('gtwTabCard').style.display = (tab === 'CARD') ? 'block' : 'none';
    document.getElementById('gtwTabUpi').style.display = (tab === 'UPI') ? 'block' : 'none';
    document.getElementById('gtwTabWallet').style.display = (tab === 'WALLET') ? 'block' : 'none';
    document.getElementById('gtwTabPostpaid').style.display = (tab === 'POSTPAID') ? 'block' : 'none';

    document.getElementById('tabBtnCard').className = `btn btn-sm ${tab === 'CARD' ? 'btn-primary' : 'btn-secondary'}`;
    document.getElementById('tabBtnUpi').className = `btn btn-sm ${tab === 'UPI' ? 'btn-primary' : 'btn-secondary'}`;
    document.getElementById('tabBtnWallet').className = `btn btn-sm ${tab === 'WALLET' ? 'btn-primary' : 'btn-secondary'}`;
    document.getElementById('tabBtnPostpaid').className = `btn btn-sm ${tab === 'POSTPAID' ? 'btn-primary' : 'btn-secondary'}`;

    if (tab === 'POSTPAID') {
        activeGtwMethod = 'PAY_LATER';
        document.getElementById('gtwPayBtn').innerText = 'Confirm Postpaid Pay-Later';
    } else if (tab === 'WALLET') {
        activeGtwMethod = 'WALLET';
        document.getElementById('gtwPayBtn').innerText = `Pay ₹${Number(activeGtwAmount).toFixed(2)} from Wallet`;
    } else {
        activeGtwMethod = 'PAY_NOW';
        document.getElementById('gtwPayBtn').innerText = `Pay ₹${Number(activeGtwAmount).toFixed(2)}`;
    }
}


function openInteractivePaymentGateway(sessionId, amount, onSuccess) {
    createGatewayModalHTML();
    activeGtwSessionId = sessionId;
    activeGtwAmount = amount;
    activeGtwCallback = onSuccess;
    gtwStep = 'INPUT';

    document.getElementById('gtwAmountDisplay').innerText = `₹${Number(amount).toFixed(2)}`;
    document.getElementById('gtwSessionTag').innerText = `Session ID: ${sessionId}`;
    document.getElementById('gtwOtpScreen').style.display = 'none';
    document.getElementById('gtwPayBtn').innerText = `Pay ₹${Number(amount).toFixed(2)}`;
    
    switchGtwTab('CARD');

    // Fetch wallet balance asynchronously for tab 3
    const userId = api.getUserId();
    if (userId) {
        api.get('/api/wallet/balance/' + userId).then(bal => {
            const el = document.getElementById('gtwWalletBalance');
            if (el) el.innerText = `₹${(bal || 0).toFixed(2)}`;
        }).catch(() => {});
    }

    document.getElementById('gatewayModalOverlay').classList.add('active');
}

function closeGatewayModal() {
    const modal = document.getElementById('gatewayModalOverlay');
    if (modal) modal.classList.remove('active');
}

async function processGtwAction() {
    if (gtwStep === 'INPUT') {
        if (activeGtwMethod === 'PAY_NOW') {
            // Show OTP Screen for 3D Secure Card/UPI simulation
            gtwStep = 'OTP';
            document.getElementById('gtwTabCard').style.display = 'none';
            document.getElementById('gtwTabUpi').style.display = 'none';
            document.getElementById('gtwTabWallet').style.display = 'none';
            document.getElementById('gtwOtpScreen').style.display = 'block';
            document.getElementById('gtwPayBtn').innerText = 'Authorize & Confirm';
            return;
        }
    }

    // Process actual backend payment call
    try {
        document.getElementById('gtwPayBtn').innerText = 'Processing...';
        document.getElementById('gtwPayBtn').disabled = true;

        await api.post(`/api/payments/process?sessionId=${activeGtwSessionId}&paymentType=${activeGtwMethod}`);
        
        showAlert('Payment successful! Invoice generated.', 'success');
        closeGatewayModal();

        if (activeGtwCallback) {
            activeGtwCallback();
        }
    } catch (error) {
        showAlert(error.message || 'Payment authorization failed', 'danger');
    } finally {
        const btn = document.getElementById('gtwPayBtn');
        if (btn) {
            btn.disabled = false;
            btn.innerText = 'Pay Now';
        }
    }
}

/* --- Global Initialization --- */
document.addEventListener('DOMContentLoaded', () => {
    if (api.isLoggedIn()) {
        const user = api.getUser();
        
        const nameEls = document.querySelectorAll('.user-name-display');
        nameEls.forEach(el => el.textContent = user.fullName || user.email);
        
        const roleEls = document.querySelectorAll('.user-role-display');
        roleEls.forEach(el => el.textContent = user.role ? user.role.replace('ROLE_', '') : 'USER');

        if (document.getElementById('sidebar')) {
            initSidebar();
        }

        const logoutBtns = document.querySelectorAll('.btn-logout, #logout-btn');
        logoutBtns.forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                api.logout();
            });
        });
    }

    const modals = document.querySelectorAll('.modal-overlay');
    modals.forEach(modal => {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('active');
            }
        });
    });

    // Request Browser Notification Permission
    if ("Notification" in window && Notification.permission !== "granted" && Notification.permission !== "denied") {
        Notification.requestPermission();
    }
});

/**
 * Send Web Browser Push Notification
 */
function sendBrowserNotification(title, body) {
    if ("Notification" in window && Notification.permission === "granted") {
        new Notification(title, {
            body: body,
            icon: "https://api.qrserver.com/v1/create-qr-code/?size=100x100&data=SmartPark"
        });
    }
    showAlert(`🔔 ${title}: ${body}`, 'info');
}

/**
 * Trigger SMS Alert simulation
 */
/**
 * Initialize 3D Sci-Fi Background Particle Grid Canvas
 */
function init3DCanvas() {
    if (document.getElementById('bg3dCanvas')) return;

    const canvas = document.createElement('canvas');
    canvas.id = 'bg3dCanvas';
    document.body.appendChild(canvas);

    const ctx = canvas.getContext('2d');
    let width = canvas.width = window.innerWidth;
    let height = canvas.height = window.innerHeight;

    window.addEventListener('resize', () => {
        width = canvas.width = window.innerWidth;
        height = canvas.height = window.innerHeight;
    });

    const particles = [];
    const particleCount = 45;

    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: Math.random() * width,
            y: Math.random() * height,
            z: Math.random() * 2 + 0.5,
            radius: Math.random() * 2.5 + 1,
            vx: (Math.random() - 0.5) * 0.6,
            vy: (Math.random() - 0.5) * 0.6,
            color: Math.random() > 0.5 ? '#00d4ff' : '#00e676'
        });
    }

    function render3D() {
        ctx.clearRect(0, 0, width, height);

        // Draw connecting 3D grid lines
        for (let i = 0; i < particleCount; i++) {
            const p1 = particles[i];
            p1.x += p1.vx;
            p1.y += p1.vy;

            if (p1.x < 0 || p1.x > width) p1.vx *= -1;
            if (p1.y < 0 || p1.y > height) p1.vy *= -1;

            ctx.beginPath();
            ctx.arc(p1.x, p1.y, p1.radius * p1.z, 0, Math.PI * 2);
            ctx.fillStyle = p1.color;
            ctx.shadowBlur = 10;
            ctx.shadowColor = p1.color;
            ctx.fill();

            for (let j = i + 1; j < particleCount; j++) {
                const p2 = particles[j];
                const dx = p1.x - p2.x;
                const dy = p1.y - p2.y;
                const dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < 130) {
                    ctx.beginPath();
                    ctx.moveTo(p1.x, p1.y);
                    ctx.lineTo(p2.x, p2.y);
                    ctx.strokeStyle = `rgba(0, 212, 255, ${0.25 - dist / 520})`;
                    ctx.lineWidth = 0.8;
                    ctx.stroke();
                }
            }
        }
        requestAnimationFrame(render3D);
    }

    render3D();
}

// Auto-start 3D particle canvas on DOM load
document.addEventListener('DOMContentLoaded', () => {
    init3DCanvas();
});


