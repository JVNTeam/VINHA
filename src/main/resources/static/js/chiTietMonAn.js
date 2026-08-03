/*==============================
        QUANTITY
==============================*/

document.addEventListener("DOMContentLoaded", function () {

    const minusBtn = document.getElementById("minus");
    const plusBtn = document.getElementById("plus");
    const quantityInput = document.getElementById("quantity");

    if (minusBtn && plusBtn && quantityInput) {
        const maxStock = parseInt(quantityInput.getAttribute("data-max") || "1", 10);

        const syncBuyNowQuantity = () => {
            const buyNowQuantity = document.getElementById("buyNowQuantity");
            if (buyNowQuantity) {
                buyNowQuantity.value = quantityInput.value;
            }
        };

        minusBtn.addEventListener("click", function () {

            let value = parseInt(quantityInput.value);

            if (value > 1) {
                quantityInput.value = value - 1;
            }
            syncBuyNowQuantity();

        });

        plusBtn.addEventListener("click", function () {

            let value = parseInt(quantityInput.value);
            let nextValue = value + 1;

            if (!Number.isNaN(maxStock) && nextValue <= maxStock) {
                quantityInput.value = nextValue;
            } else if (!Number.isNaN(maxStock)) {
                quantityInput.value = maxStock;
            } else {
                quantityInput.value = nextValue;
            }
            syncBuyNowQuantity();

        });

        quantityInput.addEventListener("input", function () {

            let value = parseInt(this.value);

            if (isNaN(value) || value < 1) {
                this.value = 1;
            } else if (!Number.isNaN(maxStock) && value > maxStock) {
                this.value = maxStock;
            }
            syncBuyNowQuantity();

        });

    }

    /*==============================
            STAR RATING
    ==============================*/

    const stars = document.querySelectorAll(".stars i");
    const scoreInput = document.getElementById("reviewScore");

    stars.forEach((star) => {

        star.addEventListener("click", function () {

            const value = parseInt(star.getAttribute("data-value") || "0", 10);

            stars.forEach((item, i) => {

                if (i < value) {

                    item.classList.remove("fa-regular");
                    item.classList.add("fa-solid");
                    item.classList.add("active");

                } else {

                    item.classList.remove("fa-solid");
                    item.classList.add("fa-regular");
                    item.classList.remove("active");

                }

            });

            if (scoreInput) {
                scoreInput.value = value;
            }

        });

    });

    /*==============================
            IMAGE ZOOM
    ==============================*/

    const image = document.getElementById("mainImage");

    if (image) {

        image.addEventListener("mousemove", function () {

            image.style.transform = "scale(1.15)";

        });

        image.addEventListener("mouseleave", function () {

            image.style.transform = "scale(1)";

        });

    }

    /*==============================
            BUTTON EFFECT
    ==============================*/

    document.querySelectorAll('.add-to-cart-form, .buy-now-form').forEach(form => {
        const submitButton = form.querySelector('button.cart-btn, button.buy-now');
        if (!submitButton) {
            return;
        }

        submitButton.addEventListener('click', async (event) => {
            event.preventDefault();
            event.stopPropagation();

            const quantityInput = document.getElementById('quantity');
            if (quantityInput && form.classList.contains('buy-now-form')) {
                const hiddenQty = form.querySelector('input[name="soLuong"]');
                if (hiddenQty) {
                    hiddenQty.value = quantityInput.value;
                }
            }

            const formData = new FormData(form);
            const action = form.getAttribute('action') || '/gioHang/them';
            const redirectTo = formData.get('redirectTo');

            try {
                const response = await fetch(action, {
                    method: 'POST',
                    body: formData,
                    headers: { 'X-Requested-With': 'XMLHttpRequest' }
                });

                if (!response.ok) {
                    throw new Error('Không thể xử lý giỏ hàng');
                }

                let payload = null;
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    payload = await response.json();
                }

                const targetUrl = payload?.redirectTo || redirectTo || '/chiTietMonAn';
                if (targetUrl === '/thanhToan' || targetUrl.endsWith('/thanhToan')) {
                    window.location.href = '/thanhToan';
                    return;
                }
                if (targetUrl === '/gioHang' || targetUrl.endsWith('/gioHang')) {
                    window.location.href = '/gioHang';
                    return;
                }

                const badge = document.querySelector('.cart span');
                if (badge) {
                    const current = parseInt(badge.textContent.trim(), 10) || 0;
                    badge.textContent = current + 1;
                }

                const button = submitButton;
                if (button) {
                    const originalHtml = button.innerHTML;
                    button.innerHTML = '<i class="fa-solid fa-check"></i> Đã thêm';
                    button.disabled = true;
                    setTimeout(() => {
                        button.innerHTML = originalHtml;
                        button.disabled = false;
                    }, 1200);
                }
            } catch (error) {
                console.error(error);
                alert('Có lỗi khi thao tác giỏ hàng');
            }
        });
    });

    /*==============================
            REVIEW
    ==============================*/

    const reviewForm = document.querySelector(".review-form");

    if (reviewForm) {

        reviewForm.addEventListener("submit", function (e) {

            if (!scoreInput || !scoreInput.value || Number(scoreInput.value) < 1) {
                e.preventDefault();
                alert("Vui lòng chọn số sao trước khi gửi đánh giá.");
            }

        });

    }

    /*==============================
            SCROLL ANIMATION
    ==============================*/

    const cards = document.querySelectorAll(".review-card");

    const observer = new IntersectionObserver((entries) => {

        entries.forEach(entry => {

            if (entry.isIntersecting) {

                entry.target.style.opacity = "1";
                entry.target.style.transform = "translateY(0)";

            }

        });

    }, {

        threshold: 0.15

    });

    cards.forEach(card => {

        card.style.opacity = "0";
        card.style.transform = "translateY(40px)";
        card.style.transition = ".5s";

        observer.observe(card);

    });

});