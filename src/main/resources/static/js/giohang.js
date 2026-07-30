// ================== ĐỊNH DẠNG TIỀN ==================

function formatMoney(number) {
    return number.toLocaleString("vi-VN") + "đ";
}

// ================== LẤY CÁC ITEM ==================

const cartItems = document.querySelectorAll(".cart-item");

function updateCart() {

    let subtotal = 0;
    let totalQuantity = 0;

    cartItems.forEach(item => {

        const priceText = item.querySelector(".price").innerText;
        const price = Number(priceText.replace(/[^\d]/g, ""));

        const input = item.querySelector("input");
        const quantity = parseInt(input.value);

        const total = price * quantity;

        item.querySelector(".total").innerText = formatMoney(total);

        subtotal += total;
        totalQuantity += quantity;

    });

    document.getElementById("subtotal").innerText = formatMoney(subtotal);

    // Phí giao hàng
    const shipping = 5000;

    document.getElementById("grand-total").innerText =
        formatMoney(subtotal + shipping);

    document.getElementById("item-count").innerText = totalQuantity;

    // Badge trên icon giỏ hàng
    const badge = document.querySelector(".cart-count");

    if (badge) {
        badge.innerText = totalQuantity;
    }

    updateHeaderCartIcon(totalQuantity);
}

function updateHeaderCartIcon(totalQuantity) {
    const cartLink = document.querySelector(".right .cart");
    const cartIcon = cartLink ? cartLink.querySelector("i") : null;

    if (!cartLink || !cartIcon) {
        return;
    }

    cartLink.classList.remove("cart-1", "cart-2", "cart-3", "cart-empty");
    cartIcon.classList.remove("fa-cart-shopping", "fa-cart-plus", "fa-bag-shopping");

    if (totalQuantity === 1) {
        cartLink.classList.add("cart-1");
        cartIcon.classList.add("fa-cart-shopping");
    } else if (totalQuantity === 2) {
        cartLink.classList.add("cart-2");
        cartIcon.classList.add("fa-cart-plus");
    } else if (totalQuantity >= 3) {
        cartLink.classList.add("cart-3");
        cartIcon.classList.add("fa-bag-shopping");
    } else {
        cartLink.classList.add("cart-empty");
        cartIcon.classList.add("fa-cart-shopping");
    }
}

// ================== TĂNG GIẢM ==================

cartItems.forEach(item => {

    const minus = item.querySelector(".minus");
    const plus = item.querySelector(".plus");
    const input = item.querySelector("input");

    plus.onclick = function () {

        input.value = parseInt(input.value) + 1;

        updateCart();

    };

    minus.onclick = function () {

        if (parseInt(input.value) > 1) {

            input.value = parseInt(input.value) - 1;

            updateCart();

        }

    };

});

// ================== XÓA ==================

document.querySelectorAll(".delete").forEach(btn => {

    btn.onclick = function () {

        if (confirm("Bạn có muốn xóa món ăn này không?")) {

            btn.closest(".cart-item").remove();

            updateAfterDelete();

        }

    };

});


// ================== SAU KHI XÓA ==================

function updateAfterDelete() {

    const items = document.querySelectorAll(".cart-item");

    let subtotal = 0;
    let totalQuantity = 0;

    items.forEach(item => {

        const price = Number(
            item.querySelector(".price").innerText.replace(/[^\d]/g, "")
        );

        const quantity = parseInt(item.querySelector("input").value);

        subtotal += price * quantity;
        totalQuantity += quantity;

    });

    document.getElementById("subtotal").innerText = formatMoney(subtotal);

    document.getElementById("grand-total").innerText =
        formatMoney(subtotal + (items.length ? 5000 : 0));

    document.getElementById("item-count").innerText = totalQuantity;

    const badge = document.querySelector(".cart-count");

    if (badge) {
        badge.innerText = totalQuantity;
    }

    updateHeaderCartIcon(totalQuantity);

    // Nếu giỏ hàng trống
    if (items.length === 0) {

        document.querySelector(".cart-left").innerHTML = `

            <div class="empty-cart">

                <i class="fa-solid fa-cart-shopping"></i>

                <h2>Giỏ hàng của bạn đang trống</h2>

                <p>Hãy chọn những món ăn yêu thích nhé.</p>

                <a href="menu.html" class="continue">
                    <i class="fa-solid fa-arrow-left"></i>
                    Quay lại thực đơn
                </a>

            </div>

        `;

    }

}

// ================== THANH TOÁN ==================

const checkoutBtn = document.querySelector(".checkout-btn");

checkoutBtn.onclick = function () {

    alert("Chuyển sang trang thanh toán.");

};

// ================== KHỞI TẠO ==================

updateCart();