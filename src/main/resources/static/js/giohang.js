// ================== ĐỊNH DẠNG TIỀN ==================
function formatMoney(number) {
    return number.toLocaleString("vi-VN") + "đ";
}

// ================== CẬP NHẬT TẠM TÍNH VÀ TỔNG CỘNG TRÊN CLIENT ==================
function updateCartSummary() {
    const checkboxes = document.querySelectorAll(".item-checkbox:checked");
    let subtotal = 0;
    let totalItems = 0;

    checkboxes.forEach(cb => {
        const item = cb.closest(".cart-item");
        const priceText = item.querySelector(".price") ? item.querySelector(".price").innerText : "0";
        const price = Number(priceText.replace(/[^\d]/g, ""));
        const quantityInput = item.querySelector(".quantity input[type='text']");
        const quantity = quantityInput ? parseInt(quantityInput.value) || 1 : 1;
        
        const itemTotal = price * quantity;
        subtotal += itemTotal;
        totalItems += quantity;
    });

    const subtotalElem = document.getElementById("subtotal");
    if (subtotalElem) subtotalElem.innerText = formatMoney(subtotal);

    const grandTotalElem = document.getElementById("grand-total");
    if (grandTotalElem) grandTotalElem.innerText = formatMoney(subtotal);

    const itemCountElem = document.getElementById("item-count");
    if (itemCountElem) itemCountElem.innerText = totalItems;

    const checkoutBtn = document.getElementById("checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.disabled = checkboxes.length === 0;
    }
}

// ================== BẮT SỰ KIỆN LÚC LOAD TRANG ==================
document.addEventListener("DOMContentLoaded", function () {
    // Lắng nghe thay đổi của checkbox
    const checkboxes = document.querySelectorAll(".item-checkbox");
    checkboxes.forEach(cb => {
        cb.addEventListener("change", updateCartSummary);
    });

    // Cập nhật giá trị ban đầu (thường là 0 vì không có checkbox nào checked)
    updateCartSummary();

    // Nút thanh toán
    const checkoutBtn = document.getElementById("checkoutBtn");
    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", function (e) {
            e.preventDefault();
            if (this.disabled) {
                alert("Vui lòng chọn ít nhất 1 sản phẩm để thanh toán.");
                return;
            }
            const checkedBoxes = document.querySelectorAll(".item-checkbox:checked");
            const itemIds = Array.from(checkedBoxes).map(cb => cb.getAttribute("data-id"));
            window.location.href = `/thanhToan?itemIds=${itemIds.join(",")}`;
        });
    }
});