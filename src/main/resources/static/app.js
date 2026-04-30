const resultMethod = document.getElementById("result-method");
const resultUrl = document.getElementById("result-url");
const resultStatus = document.getElementById("result-status");
const resultBody = document.getElementById("result-body");
const sessionStatusChip = document.getElementById("session-status-chip");
const roleChip = document.getElementById("role-chip");
const loginGate = document.getElementById("login-gate");
const appShell = document.getElementById("app-shell");
const themeToggle = document.getElementById("theme-toggle");
const sidebarToggle = document.getElementById("sidebar-toggle");
const errorScreen = document.getElementById("error-screen");
const errorCode = document.getElementById("error-code");
const errorTitle = document.getElementById("error-title");
const errorMessage = document.getElementById("error-message");

const state = {
    customers: [],
    products: [],
    orders: [],
    admins: [],
    counts: {
        customers: 0,
        products: 0,
        orders: 0,
        admins: 0
    },
    currentRole: null,
    currentAdmin: null
};

function badgeTone(value) {
    const normalized = String(value ?? "").toUpperCase();
    if (["ACTIVE", "APPROVED", "ON_SALE", "DELIVERED", "SHIPPING", "SUCCESS", "CS_ADMIN", "SUPER_ADMIN"].includes(normalized)) {
        return "success";
    }
    if (["PENDING", "WAITING", "READY", "OPERATOR"].includes(normalized)) {
        return "neutral";
    }
    if (["CANCELED", "REJECTED", "DISCONTINUED", "INACTIVE", "FAIL"].includes(normalized)) {
        return "error";
    }
    return "neutral";
}

function statusBadge(value) {
    return `<span class="status-pill ${badgeTone(value)}">${escapeHtml(value || "-")}</span>`;
}

function setGateVisible(visible) {
    loginGate.classList.toggle("hidden", !visible);
    appShell.classList.toggle("locked", visible);
    if (visible) {
        const message = state.currentAdmin ? "로그아웃되었습니다." : "로그인 후 이용 가능합니다.";
        const copy = document.querySelector(".login-gate-copy");
        if (copy) copy.textContent = message;
    }
}

function hideErrorScreen() {
    errorScreen.classList.add("hidden");
}

function showErrorScreen(status, payload) {
    const message = payload?.message || payload?.error || "요청을 처리할 수 없습니다.";
    const titleMap = {
        400: "잘못된 요청입니다.",
        401: "로그인이 필요합니다.",
        403: "접근 권한이 없습니다.",
        404: "대상을 찾을 수 없습니다.",
        500: "서버 처리 중 오류가 발생했습니다."
    };

    errorCode.textContent = String(status || 500);
    errorTitle.textContent = titleMap[status] || "요청 처리에 실패했습니다.";
    errorMessage.textContent = message;
    errorScreen.classList.remove("hidden");
}

function activateSection(target) {
    const section = document.getElementById(target);
    if (!section || section.classList.contains("is-hidden")) {
        return;
    }

    document.querySelectorAll(".nav-link").forEach((node) => node.classList.remove("active"));
    document.querySelectorAll(".content-section").forEach((node) => node.classList.remove("active"));
    const navButton = document.querySelector(`.nav-link[data-target="${target}"]`);
    if (navButton) navButton.classList.add("active");
    if (section) section.classList.add("active");
}

function syncSidebarToggle() {
    sidebarToggle.textContent = appShell.classList.contains("sidebar-collapsed") ? "›" : "‹";
}

function applyRoleLayout(role) {
    const adminNav = document.querySelector('.nav-link[data-target="admins"]');
    const adminSection = document.getElementById("admins");
    const customerNav = document.querySelector('.nav-link[data-target="customers"]');
    const productNav = document.querySelector('.nav-link[data-target="products"]');
    const orderNav = document.querySelector('.nav-link[data-target="orders"]');
    const customerSection = document.getElementById("customers");
    const productSection = document.getElementById("products");
    const orderSection = document.getElementById("orders");
    const customerUpdateForm = document.getElementById("customer-update-form");
    const customerDeleteForm = document.getElementById("customer-delete-form");
    const productCreateForm = document.getElementById("product-create-form");
    const productUpdateForm = document.getElementById("product-update-form");
    const productStatusForm = document.getElementById("product-status-form");
    const productDeleteForm = document.getElementById("product-delete-form");
    const orderStatusForm = document.getElementById("order-status-form");
    const orderCancelForm = document.getElementById("order-cancel-form");
    const heroProductsButton = document.getElementById("hero-products-button");
    const heroOrdersButton = document.getElementById("hero-orders-button");

    adminNav?.classList.remove("is-hidden");
    adminSection?.classList.remove("is-hidden");
    customerNav?.classList.remove("is-hidden");
    productNav?.classList.remove("is-hidden");
    orderNav?.classList.remove("is-hidden");
    customerSection?.classList.remove("is-hidden");
    productSection?.classList.remove("is-hidden");
    orderSection?.classList.remove("is-hidden");
    customerUpdateForm?.classList.remove("is-hidden");
    customerDeleteForm?.classList.remove("is-hidden");
    productCreateForm?.classList.remove("is-hidden");
    productUpdateForm?.classList.remove("is-hidden");
    productStatusForm?.classList.remove("is-hidden");
    productDeleteForm?.classList.remove("is-hidden");
    orderStatusForm?.classList.remove("is-hidden");
    orderCancelForm?.classList.remove("is-hidden");
    heroProductsButton?.classList.remove("is-hidden");
    heroOrdersButton?.classList.remove("is-hidden");

    if (role === "SUPER_ADMIN") {
        roleChip.textContent = "SUPER ADMIN";
        return;
    }

    if (role === "CS_ADMIN") {
        roleChip.textContent = "CS ADMIN";
        adminNav?.classList.add("is-hidden");
        adminSection?.classList.add("is-hidden");
        productNav?.classList.add("is-hidden");
        productSection?.classList.add("is-hidden");
        heroProductsButton?.classList.add("is-hidden");
        customerUpdateForm?.classList.add("is-hidden");
        customerDeleteForm?.classList.add("is-hidden");
        orderStatusForm?.classList.add("is-hidden");
        if (document.getElementById("admins").classList.contains("active")) {
            activateSection("overview");
        }
        return;
    }

    roleChip.textContent = "OPERATOR";
    adminNav?.classList.add("is-hidden");
    adminSection?.classList.add("is-hidden");
    customerUpdateForm?.classList.add("is-hidden");
    customerDeleteForm?.classList.add("is-hidden");
    orderCancelForm?.classList.add("is-hidden");
}

async function getCurrentAdminProfile() {
    const { response, parsed } = await request("GET", "/admins/me", undefined, {
        silent: true,
        showErrorScreen: false
    });
    if (!response.ok) return null;
    return parsed?.data ?? parsed;
}

async function resolveRole() {
    const adminProbe = await request("GET", "/admins?page=1&size=1", undefined, {
        silent: true,
        showErrorScreen: false
    });
    if (adminProbe.response.ok) return "SUPER_ADMIN";

    const productProbe = await request("GET", "/products?page=1&size=1", undefined, {
        silent: true,
        showErrorScreen: false
    });
    if (productProbe.response.ok) return "CS_ADMIN";

    return "OPERATOR";
}

async function hydrateWorkspace() {
    const profile = await getCurrentAdminProfile();
    if (!profile) {
        state.currentRole = null;
        state.currentAdmin = null;
        sessionStatusChip.textContent = "세션 없음";
        roleChip.textContent = "ROLE UNKNOWN";
        setGateVisible(true);
        return false;
    }

    state.currentAdmin = profile;
    state.currentRole = await resolveRole();
    sessionStatusChip.textContent = `${profile.name} · ${profile.email}`;
    document.getElementById("summary-role").textContent = state.currentRole || "-";
    document.getElementById("summary-admin-name").textContent = profile.name || "-";
    document.getElementById("summary-admin-email").textContent = profile.email || "-";
    document.getElementById("profile-role").textContent = state.currentRole || "-";
    document.getElementById("profile-name").textContent = profile.name || "-";
    document.getElementById("profile-email").textContent = profile.email || "-";
    renderDetailCard("profile-card", "내 프로필", profile);
    applyRoleLayout(state.currentRole);
    setGateVisible(false);

    if (state.currentRole === "SUPER_ADMIN") {
        await Promise.all([loadCustomers(), loadProducts(), loadOrders(), loadAdmins()]);
    } else if (state.currentRole === "CS_ADMIN") {
        await Promise.all([loadCustomers(), loadProducts(), loadOrders()]);
        renderAdminTable([]);
        state.counts.admins = 0;
        updateStats();
    } else {
        renderAdminTable([]);
        renderCustomerTable([]);
        renderProductTable([]);
        renderOrderTable([]);
        state.counts = { customers: 0, products: 0, orders: 0, admins: 0 };
        updateStats();
    }

    return true;
}

function setResult(method, url, status, data) {
    resultMethod.textContent = method;
    resultUrl.textContent = url;
    resultStatus.textContent = status === 0 ? "대기중" : `${status}`;
    resultStatus.className = `status-pill ${status === 0 ? "neutral" : (status >= 200 && status < 300 ? "success" : "error")}`;
    resultBody.textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

async function request(method, url, body, requestOptions = {}) {
    const fetchOptions = {
        method,
        headers: {}
    };

    if (body !== undefined) {
        fetchOptions.headers["Content-Type"] = "application/json";
        fetchOptions.body = JSON.stringify(body);
    }

    const response = await fetch(url, fetchOptions);
    const text = await response.text();

    let parsed = text;
    try {
        parsed = text ? JSON.parse(text) : {};
    } catch (_) {
        parsed = text || {};
    }

    if (!requestOptions.silent) {
        setResult(method, url, response.status, parsed);
    }
    if (response.ok) {
        hideErrorScreen();
    }
    if (!response.ok && requestOptions.showErrorScreen !== false) {
        showErrorScreen(response.status, parsed);
    }
    return { response, parsed };
}

function formDataObject(form) {
    return Object.fromEntries(new FormData(form).entries());
}

function queryString(values) {
    const params = new URLSearchParams();
    Object.entries(values).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") {
            params.append(key, value);
        }
    });
    return params.toString();
}

function money(value) {
    if (value === undefined || value === null) return "-";
    return Number(value).toLocaleString("ko-KR") + "원";
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

function renderCustomerTable(customers) {
    const tbody = document.getElementById("customer-table");
    if (!customers.length) {
        tbody.innerHTML = `<tr><td colspan="6" class="empty-cell">조회된 고객이 없다.</td></tr>`;
        return;
    }

    tbody.innerHTML = customers.map((customer) => `
        <tr>
            <td>${escapeHtml(customer.id)}</td>
            <td>${escapeHtml(customer.name)}</td>
            <td>${escapeHtml(customer.email)}</td>
            <td>${escapeHtml(customer.phoneNumber)}</td>
            <td>${statusBadge(customer.status)}</td>
            <td>${escapeHtml(customer.createdAt)}</td>
        </tr>
    `).join("");
}

function renderProductTable(products) {
    const tbody = document.getElementById("product-table");
    const preview = document.getElementById("product-preview");

    if (!products.length) {
        tbody.innerHTML = `<tr><td colspan="7" class="empty-cell">조회된 상품이 없다.</td></tr>`;
        preview.innerHTML = "상품 데이터를 아직 안 불러옴";
        preview.className = "preview-grid empty-state";
        return;
    }

    tbody.innerHTML = products.map((product) => `
        <tr>
            <td>${escapeHtml(product.productId)}</td>
            <td>${escapeHtml(product.productName)}</td>
            <td>${escapeHtml(product.category)}</td>
            <td>${money(product.price)}</td>
            <td>${escapeHtml(product.stock)}</td>
            <td>${statusBadge(product.status)}</td>
            <td>${escapeHtml(product.userName)}</td>
        </tr>
    `).join("");

    preview.className = "preview-grid";
    preview.innerHTML = products.slice(0, 3).map((product) => `
        <article class="preview-card">
            <strong>${escapeHtml(product.productName)}</strong>
            <span class="preview-meta">${escapeHtml(product.category)}</span>
            <span class="preview-stock">재고 ${escapeHtml(product.stock)}개</span>
            <p>${money(product.price)} / 재고 ${escapeHtml(product.stock)}</p>
            <div>${statusBadge(product.status)}</div>
        </article>
    `).join("");
}

function renderOrderTable(orders) {
    const tbody = document.getElementById("order-table");
    const preview = document.getElementById("order-preview");

    if (!orders.length) {
        tbody.innerHTML = `<tr><td colspan="8" class="empty-cell">조회된 주문이 없다.</td></tr>`;
        preview.innerHTML = "주문 데이터를 아직 안 불러옴";
        preview.className = "preview-list empty-state";
        return;
    }

    tbody.innerHTML = orders.map((order) => `
        <tr>
            <td>${escapeHtml(order.id)}</td>
            <td>${escapeHtml(order.orderNumber)}</td>
            <td>${escapeHtml(order.customerName)}</td>
            <td>${escapeHtml(order.productName)}</td>
            <td>${escapeHtml(order.quantity)}</td>
            <td>${money(order.totalPrice)}</td>
            <td>${statusBadge(order.status)}</td>
            <td>${escapeHtml(order.adminName)}</td>
        </tr>
    `).join("");

    preview.className = "preview-list";
    preview.innerHTML = orders.slice(0, 4).map((order) => `
        <article class="order-preview-item">
            <strong>#${escapeHtml(order.orderNumber)}</strong>
            <span>${escapeHtml(order.customerName)} · ${escapeHtml(order.productName)}</span>
            <p>${money(order.totalPrice)}</p>
            <div>${statusBadge(order.status)}</div>
        </article>
    `).join("");
}

function renderAdminTable(admins) {
    const tbody = document.getElementById("admin-table");
    if (!admins.length) {
        tbody.innerHTML = `<tr><td colspan="6" class="empty-cell">조회된 관리자 데이터가 없거나 권한이 없다.</td></tr>`;
        return;
    }

    tbody.innerHTML = admins.map((admin) => `
        <tr>
            <td>${escapeHtml(admin.id)}</td>
            <td>${escapeHtml(admin.name)}</td>
            <td>${escapeHtml(admin.email)}</td>
            <td>${escapeHtml(admin.phoneNumber)}</td>
            <td>${statusBadge(admin.adminRole)}</td>
            <td>${statusBadge(admin.adminStatus)}</td>
        </tr>
    `).join("");
}

function detailLabel(key) {
    const labels = {
        id: "ID",
        productId: "상품 ID",
        orderId: "주문 ID",
        orderNumber: "주문번호",
        name: "이름",
        email: "이메일",
        phoneNumber: "전화번호",
        status: "상태",
        createdAt: "생성일",
        modifiedAt: "수정일",
        customerName: "고객명",
        customerEmail: "고객 이메일",
        productName: "상품명",
        quantity: "수량",
        totalPrice: "총 금액",
        price: "가격",
        stock: "재고",
        category: "카테고리",
        adminName: "담당 관리자",
        adminEmail: "담당 관리자 이메일",
        role: "권한",
        adminRole: "권한",
        adminStatus: "관리자 상태",
        cancelReason: "취소 사유",
        userName: "담당자"
    };
    return labels[key] || key;
}

function decorateStatusLabel(label) {
    const normalized = String(label ?? "").trim().toUpperCase();
    const emojiMap = {
        "활성": "🟢",
        "ACTIVE": "🟢",
        "정지": "⏸️",
        "SUSPENDED": "⏸️",
        "비활성": "⚪",
        "INACTIVE": "⚪",
        "대기": "🟡",
        "PENDING": "🟡",
        "취소": "🔴",
        "CANCELED": "🔴",
        "배송중": "🚚",
        "SHIPPING": "🚚",
        "배송완료": "📦",
        "DELIVERED": "📦"
    };

    const emoji = emojiMap[String(label ?? "").trim()] || emojiMap[normalized];
    return emoji ? `${emoji} ${label}` : label;
}

function formatDetailValue(key, value) {
    if (value === null || value === undefined || value === "") return "-";
    if (["status", "adminRole", "adminStatus", "role"].includes(key)) {
        return statusBadge(value);
    }
    if (["price", "totalPrice"].includes(key) && !Number.isNaN(Number(value))) {
        return money(value);
    }
    if (Array.isArray(value) || (typeof value === "object" && value !== null)) {
        return `<pre>${escapeHtml(JSON.stringify(value, null, 2))}</pre>`;
    }
    return escapeHtml(value);
}

function renderDetailCard(targetId, title, payload) {
    const target = document.getElementById(targetId);
    const body = payload?.data ?? payload;
    const metaStatus = payload?.status;
    const metaMessage = payload?.message;

    if (!body || typeof body !== "object" || Array.isArray(body)) {
        target.className = "detail-card fade-in";
        target.innerHTML = `<strong>${escapeHtml(title)}</strong><pre>${escapeHtml(JSON.stringify(payload, null, 2))}</pre>`;
        return;
    }

    const fields = Object.entries(body)
        .filter(([, value]) => typeof value !== "function")
        .map(([key, value]) => `
            <div class="detail-item">
                <span class="detail-key">${escapeHtml(detailLabel(key))}</span>
                <strong class="detail-value">${formatDetailValue(key, value)}</strong>
            </div>
        `)
        .join("");

    target.className = "detail-card fade-in";
    target.innerHTML = `
        <div class="detail-card-head">
            <strong class="detail-card-title">${escapeHtml(title)}</strong>
            ${metaStatus ? `<span class="status-pill neutral">HTTP ${escapeHtml(metaStatus)}</span>` : ""}
        </div>
        ${metaMessage ? `<p class="detail-card-copy">${escapeHtml(metaMessage)}</p>` : ""}
        <div class="detail-grid">${fields}</div>
    `;
}

function renderCountBoard(targetId, entries, emptyText) {
    const target = document.getElementById(targetId);
    if (!target) return;
    if (!entries.length) {
        target.className = "insight-grid empty-state";
        target.innerHTML = emptyText;
        return;
    }

    target.className = "insight-grid";
    target.innerHTML = entries.map(([label, count]) => `
        <article class="insight-card">
            <span>${escapeHtml(decorateStatusLabel(label))}</span>
            <strong>${escapeHtml(count)}</strong>
        </article>
    `).join("");
}

function renderCustomerStatusBoard(customers) {
    const counts = customers.reduce((acc, customer) => {
        const key = customer.status || "UNKNOWN";
        acc[key] = (acc[key] || 0) + 1;
        return acc;
    }, {});
    renderCountBoard("customer-status-board", Object.entries(counts), "고객 데이터를 아직 안 불러옴");
}

function renderOrderStatusBoard(orders) {
    const counts = orders.reduce((acc, order) => {
        const key = order.status || "UNKNOWN";
        acc[key] = (acc[key] || 0) + 1;
        return acc;
    }, {});
    renderCountBoard("order-status-board", Object.entries(counts), "주문 데이터를 아직 안 불러옴");
}

function renderRecentOrders(orders) {
    const target = document.getElementById("recent-orders-board");
    if (!target) return;
    if (!orders.length) {
        target.className = "preview-list empty-state";
        target.innerHTML = "주문 데이터를 아직 안 불러옴";
        return;
    }

    target.className = "preview-list";
    target.innerHTML = orders.slice(0, 5).map((order) => `
        <article class="order-preview-item">
            <strong>#${escapeHtml(order.orderNumber)}</strong>
            <span>${escapeHtml(order.customerName)} · ${escapeHtml(order.productName)}</span>
            <p>${money(order.totalPrice)}</p>
            <div>${statusBadge(order.status)}</div>
        </article>
    `).join("");
}

function updateStats() {
    document.getElementById("stat-customers").textContent = state.counts.customers || "-";
    document.getElementById("stat-products").textContent = state.counts.products || "-";
    document.getElementById("stat-orders").textContent = state.counts.orders || "-";
    document.getElementById("stat-admins").textContent = state.counts.admins || "-";
}

function syncThemeLabel() {
    const isDark = document.body.classList.contains("dark-mode");
    themeToggle.textContent = isDark ? "☀️" : "🌙";
    themeToggle.setAttribute("aria-label", isDark ? "라이트모드 전환" : "다크모드 전환");
    themeToggle.setAttribute("title", isDark ? "라이트모드 전환" : "다크모드 전환");
}

async function loadCustomers(values = { page: 1, size: 10, sortBy: "createdAt", direction: "asc" }) {
    const qs = queryString(values);
    const { response, parsed } = await request("GET", `/customers${qs ? `?${qs}` : ""}`);
    const customers = parsed?.data?.customers ?? [];
    if (response.ok) {
        state.customers = customers;
        state.counts.customers = parsed?.data?.totalCount ?? customers.length;
        renderCustomerTable(customers);
        renderCustomerStatusBoard(customers);
        updateStats();
    }
}

async function loadProducts(values = { page: 1, size: 10, sortBy: "createAt", direction: "desc" }) {
    const qs = queryString(values);
    const { response, parsed } = await request("GET", `/products${qs ? `?${qs}` : ""}`);
    const products = parsed?.response ?? [];
    if (response.ok) {
        state.products = products;
        state.counts.products = parsed?.pageableResponse?.totalElements ?? products.length;
        renderProductTable(products);
        updateStats();
    }
}

async function loadOrders(values = { page: 1, size: 10, sortBy: "createdAt", direction: "desc" }) {
    const qs = queryString(values);
    const { response, parsed } = await request("GET", `/orders${qs ? `?${qs}` : ""}`);
    const orders = parsed?.data?.adminList ?? [];
    if (response.ok) {
        state.orders = orders;
        state.counts.orders = parsed?.data?.totalCount ?? orders.length;
        renderOrderTable(orders);
        renderOrderStatusBoard(orders);
        renderRecentOrders(orders);
        updateStats();
    }
}

async function loadAdmins(values = { page: 1, size: 10, sortBy: "name", order: "asc" }) {
    const qs = queryString(values);
    const { response, parsed } = await request("GET", `/admins${qs ? `?${qs}` : ""}`);
    const admins = parsed?.data?.adminList ?? [];
    if (response.ok) {
        state.admins = admins;
        state.counts.admins = parsed?.data?.totalCount ?? admins.length;
        renderAdminTable(admins);
        updateStats();
    } else {
        state.admins = [];
        state.counts.admins = 0;
        renderAdminTable([]);
        updateStats();
    }
}

document.querySelectorAll(".nav-link").forEach((button) => {
    button.addEventListener("click", () => activateSection(button.dataset.target));
});

document.querySelectorAll("[data-target-jump]").forEach((button) => {
    button.addEventListener("click", () => {
        const target = button.dataset.targetJump;
        const navButton = document.querySelector(`.nav-link[data-target="${target}"]`);
        if (navButton) navButton.click();
    });
});

async function handleLogin(event) {
    event.preventDefault();
    const payload = formDataObject(event.target);
    const { response, parsed } = await request("POST", "/admins/login", payload);
    if (response.ok) {
        hideErrorScreen();
        await hydrateWorkspace();
    }
}

document.getElementById("login-gate-form").addEventListener("submit", handleLogin);

document.getElementById("logout-button").addEventListener("click", async () => {
    const confirmed = window.confirm("로그아웃하시겠습니까?");
    if (!confirmed) return;

    const { response } = await request("POST", "/admins/logout");
    if (response.ok) {
        state.currentAdmin = null;
        sessionStatusChip.textContent = "세션 없음";
        roleChip.textContent = "ROLE UNKNOWN";
        hideErrorScreen();
        setGateVisible(true);
    }
});

document.getElementById("session-button").addEventListener("click", async () => {
    const hasSession = await hydrateWorkspace();
    if (!hasSession) {
        await request("GET", "/test");
    }
});

document.getElementById("refresh-overview").addEventListener("click", async () => {
    const confirmed = window.confirm("대시보드를 새로고침하시겠습니까?");
    if (!confirmed) return;
    await hydrateWorkspace();
});

document.getElementById("load-profile").addEventListener("click", async () => {
    const confirmed = window.confirm("프로필 정보를 새로고침하시겠습니까?");
    if (!confirmed) return;
    await hydrateWorkspace();
    activateSection("profile");
});

document.querySelectorAll("[data-load='products']").forEach((button) => {
    button.addEventListener("click", () => loadProducts());
});

document.querySelectorAll("[data-load='orders']").forEach((button) => {
    button.addEventListener("click", () => loadOrders());
});

document.getElementById("customer-list-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadCustomers(formDataObject(event.target));
});

document.getElementById("customer-detail-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { id } = formDataObject(event.target);
    const { response, parsed } = await request("GET", `/customers/${id}`);
    if (response.ok) {
        renderDetailCard("customer-detail-card", "고객 상세 결과", parsed);
    }
});

document.getElementById("customer-update-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { id, ...payload } = formDataObject(event.target);
    await request("PATCH", `/customers/${id}`, payload);
    await loadCustomers();
});

document.getElementById("customer-delete-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { id } = formDataObject(event.target);
    await request("DELETE", `/customers/${id}`);
    await loadCustomers();
});

document.getElementById("product-list-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadProducts(formDataObject(event.target));
});

document.getElementById("product-detail-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { productId } = formDataObject(event.target);
    const { response, parsed } = await request("GET", `/products/${productId}`);
    if (response.ok) {
        renderDetailCard("product-detail-card", "상품 상세 결과", parsed);
    }
});

document.getElementById("product-create-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await request("POST", "/products/add", formDataObject(event.target));
    await loadProducts();
});

document.getElementById("product-update-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { productId, ...payload } = formDataObject(event.target);
    await request("PATCH", `/products/${productId}`, payload);
    await loadProducts();
});

document.getElementById("product-status-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { productId, status } = formDataObject(event.target);
    await request("PATCH", `/products/${productId}/status`, { status });
    await loadProducts();
});

document.getElementById("product-delete-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { productId } = formDataObject(event.target);
    await request("DELETE", `/products/${productId}`);
    await loadProducts();
});

document.getElementById("order-list-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadOrders(formDataObject(event.target));
});

document.getElementById("order-detail-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { orderId } = formDataObject(event.target);
    const { response, parsed } = await request("GET", `/orders/${orderId}`);
    if (response.ok) {
        renderDetailCard("order-detail-card", "주문 상세 결과", parsed);
    }
});

document.getElementById("order-status-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { id, status } = formDataObject(event.target);
    await request("PATCH", `/orders/${id}`, { status });
    await loadOrders();
});

document.getElementById("order-cancel-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const { id, cancelReason } = formDataObject(event.target);
    await request("DELETE", `/orders/${id}`, { cancelReason });
    await loadOrders();
});

document.getElementById("admin-list-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadAdmins(formDataObject(event.target));
});

document.getElementById("admin-signup-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const payload = formDataObject(event.target);
    await request("POST", "/admins/signup", payload);
    await loadAdmins();
});

document.getElementById("load-customers").addEventListener("click", async () => {
    const confirmed = window.confirm("고객 목록을 새로고침하시겠습니까?");
    if (!confirmed) return;
    await loadCustomers();
});

document.getElementById("load-products").addEventListener("click", async () => {
    const confirmed = window.confirm("상품 목록을 새로고침하시겠습니까?");
    if (!confirmed) return;
    await loadProducts();
});

document.getElementById("load-orders").addEventListener("click", async () => {
    const confirmed = window.confirm("주문 목록을 새로고침하시겠습니까?");
    if (!confirmed) return;
    await loadOrders();
});

document.getElementById("load-admins").addEventListener("click", async () => {
    const confirmed = window.confirm("관리자 목록을 새로고침하시겠습니까?");
    if (!confirmed) return;
    await loadAdmins();
});

document.getElementById("clear-result").addEventListener("click", () => {
    setResult("-", "-", 0, "아직 요청 안 보냄");
});

document.getElementById("error-back-button").addEventListener("click", () => {
    hideErrorScreen();
    window.history.back();
});

document.getElementById("error-home-button").addEventListener("click", () => {
    hideErrorScreen();
    activateSection("overview");
});

themeToggle.addEventListener("click", () => {
    document.body.classList.toggle("dark-mode");
    localStorage.setItem("jotgarak-theme", document.body.classList.contains("dark-mode") ? "dark" : "light");
    syncThemeLabel();
});

if (localStorage.getItem("jotgarak-theme") === "dark") {
    document.body.classList.add("dark-mode");
}

syncThemeLabel();

if (localStorage.getItem("jotgarak-sidebar") === "collapsed") {
    appShell.classList.add("sidebar-collapsed");
}

sidebarToggle.addEventListener("click", () => {
    appShell.classList.toggle("sidebar-collapsed");
    localStorage.setItem("jotgarak-sidebar", appShell.classList.contains("sidebar-collapsed") ? "collapsed" : "expanded");
    syncSidebarToggle();
});

syncSidebarToggle();

hydrateWorkspace();
