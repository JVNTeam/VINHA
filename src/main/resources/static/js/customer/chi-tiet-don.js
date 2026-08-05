// =========================================================
// SCRIPT XỬ LÝ CHI TIẾT ĐƠN HÀNG (CHI TIẾT ĐƠN HÀNG - JS)
// =========================================================

document.addEventListener("DOMContentLoaded", function () {
    console.log("Chi tiết đơn hàng đã sẵn sàng.");

    // 1. Khởi tạo sự kiện cho nút Hủy đơn hàng
    const btnHuy = document.getElementById("btnHuy") || document.getElementById("cancelOrderBtn");
    if (btnHuy) {
        btnHuy.addEventListener("click", function () {
            const orderId = this.getAttribute("data-id");
            if (orderId) {
                cancelOrderFromDetail(orderId);
            } else {
                // Fallback nếu không truyền data-id từ backend
                if (confirm("Bạn có chắc chắn muốn HỦY đơn hàng này?")) {
                    updateOrderStatusUI("cancelled", "Đã hủy");
                    this.style.display = "none";
                    alert("Đã hủy đơn hàng thành công!");
                }
            }
        });
    }

    // 2. Khởi tạo sự kiện cho nút Xác nhận đơn hàng (Dành cho Admin/Quản lý)
    const btnXacNhan = document.getElementById("btnXacNhan");
    if (btnXacNhan) {
        btnXacNhan.addEventListener("click", function () {
            const orderId = this.getAttribute("data-id");
            if (orderId) {
                confirmOrderFromDetail(orderId, this);
            } else {
                // Fallback nếu không truyền data-id từ backend
                if (confirm("Xác nhận đơn hàng này?")) {
                    updateOrderStatusUI("confirmed", "Đã xác nhận");
                    this.style.display = "none";
                    alert("Xác nhận đơn hàng thành công!");
                }
            }
        });
    }
});

/**
 * Gọi API Hủy đơn hàng từ phía Client/Admin
 * @param {string|number} orderId - Mã định danh đơn hàng
 */
function cancelOrderFromDetail(orderId) {
    if (!confirm("Bạn có chắc chắn muốn HỦY đơn hàng này không?")) {
        return;
    }

    fetch("/api/order/cancel/" + orderId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Hủy đơn hàng thành công!");
                updateOrderStatusUI("cancelled", "Đã hủy");

                const btnHuy = document.getElementById("btnHuy") || document.getElementById("cancelOrderBtn");
                if (btnHuy) btnHuy.style.display = "none";
            } else {
                alert("Lỗi: " + (data.message || "Không thể hủy đơn hàng này."));
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Có lỗi xảy ra khi kết nối tới máy chủ.");
        });
}

/**
 * Gọi API Xác nhận đơn hàng
 * @param {string|number} orderId - Mã định danh đơn hàng
 * @param {HTMLElement} btnElement - Nút vừa bấm
 */
function confirmOrderFromDetail(orderId, btnElement) {
    fetch("/api/order/confirm/" + orderId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Đã xác nhận đơn hàng thành công!");
                updateOrderStatusUI("confirmed", "Đã xác nhận");
                if (btnElement) btnElement.style.display = "none";
            } else {
                alert("Lỗi: " + (data.message || "Không thể xác nhận đơn hàng."));
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Có lỗi xảy ra khi xác nhận đơn hàng.");
        });
}

/**
 * Cập nhật giao diện Trạng Thái (Badge & Timeline) tương thích chuẩn CSS Variables
 * @param {string} statusCode - Trạng thái mới: 'pending' | 'confirmed' | 'completed' | 'cancelled'
 * @param {string} statusText - Tên trạng thái bằng tiếng Việt
 */
function updateOrderStatusUI(statusCode, statusText) {
    // 1. Cập nhật Badge Trạng Thái ở Header
    const badge = document.querySelector(".badge-status");
    if (badge) {
        badge.className = "badge-status " + statusCode;
        badge.innerText = statusText;

        // Xử lý đổi màu theo biến CSS nếu trạng thái khác
        if (statusCode === "cancelled") {
            badge.style.backgroundColor = "#fef2f2";
            badge.style.color = "var(--danger-color)";
        } else if (statusCode === "confirmed" || statusCode === "completed") {
            badge.style.backgroundColor = "#ecfdf5";
            badge.style.color = "var(--success-color)";
        }
    }

    // 2. Cập nhật Timeline Tiến Trình (Nếu trang có Timeline)
    const timelineItems = document.querySelectorAll(".timeline li");
    if (timelineItems.length > 0) {
        if (statusCode === "cancelled") {
            timelineItems.forEach(item => {
                item.className = "";
            });
            const lastItem = timelineItems[timelineItems.length - 1];
            if (lastItem) {
                lastItem.className = "current";
                const icon = lastItem.querySelector(".icon i");
                if (icon) icon.className = "fa-solid fa-xmark";
                const title = lastItem.querySelector(".text strong");
                if (title) title.innerText = "Đã hủy đơn";
            }
        } else if (statusCode === "confirmed") {
            if (timelineItems[0]) timelineItems[0].className = "done";
            if (timelineItems[1]) timelineItems[1].className = "current";
        }
    }
}