// ================== ĐỊNH DẠNG TIỀN ==================
function formatMoney(number) {
    return number.toLocaleString("vi-VN") + "đ";
}

// ================== CẬP NHẬT TẠM TÍNH VÀ TỔNG CỘNG TRÊN CLIENT ==================
function updateCartSummary() {
    const cartItems = document.querySelectorAll(".cart-item");
    let subtotal = 0;
    let totalItems = cartItems.length; // Đếm số loại món ăn trong giỏ

    cartItems.forEach(item => {
        const priceText = item.querySelector(".price") ? item.querySelector(".price").innerText : "0";
        const price = Number(priceText.replace(/[^\d]/g, ""));

        // Lấy đúng ô input text hiển thị số lượng
        const quantityInput = item.querySelector(".quantity input[type='text']");
        const quantity = quantityInput ? parseInt(quantityInput.value) || 1 : 1;

        const itemTotal = price * quantity;

        // Cập nhật dòng thành tiền của item
        const totalElem = item.querySelector(".total");
        if (totalElem) {
            totalElem.innerText = formatMoney(itemTotal);
        }

        subtotal += itemTotal;
    });

    // Cập nhật Tạm tính & Tổng cộng (Đồng bộ không cộng phí ẩn)
    const subtotalElem = document.getElementById("subtotal");
    if (subtotalElem) subtotalElem.innerText = formatMoney(subtotal);

    const grandTotalElem = document.getElementById("grand-total");
    if (grandTotalElem) grandTotalElem.innerText = formatMoney(subtotal);

    // Cập nhật số loại món ăn
    const itemCountElem = document.getElementById("item-count");
    if (itemCountElem) itemCountElem.innerText = totalItems;

    // Quản lý trạng thái nút Thanh toán
    const checkoutBtn = document.getElementById("checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.disabled = totalItems === 0;
    }
}

// ================== BẮT SỰ KIỆN SUBMIT FORM TĂNG GIẢM/XÓA ==================
document.addEventListener("DOMContentLoaded", function () {
    // Không chạy updateCart() làm đè value=1 nữa

    // Kiểm tra nút thanh toán
    const checkoutBtn = document.getElementById("checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", function (e) {
            if (this.disabled) {
                e.preventDefault();
                alert("Giỏ hàng đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
            }
        });
    }
});