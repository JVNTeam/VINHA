document.addEventListener("DOMContentLoaded", function () {
    // ==========================================
    // 1. XỬ LÝ TĂNG / GIẢM SỐ LƯỢNG MÓN ÁN
    // ==========================================
    const minusBtn = document.getElementById("minus");
    const plusBtn = document.getElementById("plus");
    const quantityInput = document.getElementById("quantity");
    const buyNowQuantityInput = document.getElementById("buyNowQuantity");

    if (quantityInput) {
        // Lấy số lượng tối đa từ thuộc tính data-max (mặc định là 999 nếu không có)
        const maxQty = parseInt(quantityInput.getAttribute("data-max")) || 999;

        // Hàm cập nhật số lượng và kiểm tra trạng thái nút
        function updateQuantity(newVal) {
            let val = parseInt(newVal);

            if (isNaN(val) || val < 1) {
                val = 1;
            } else if (val > maxQty) {
                val = maxQty;
            }

            quantityInput.value = val;

            // Đồng bộ sang form "Mua ngay"
            if (buyNowQuantityInput) {
                buyNowQuantityInput.value = val;
            }

            // Vô hiệu hóa nút minus nếu đạt 1
            if (minusBtn) {
                minusBtn.disabled = val <= 1;
            }

            // Vô hiệu hóa nút plus nếu đạt số lượng tối đa
            if (plusBtn) {
                plusBtn.disabled = val >= maxQty;
            }
        }

        // Nút Giảm (-)
        if (minusBtn) {
            minusBtn.addEventListener("click", function () {
                let currentVal = parseInt(quantityInput.value) || 1;
                updateQuantity(currentVal - 1);
            });
        }

        // Nút Tăng (+)
        if (plusBtn) {
            plusBtn.addEventListener("click", function () {
                let currentVal = parseInt(quantityInput.value) || 1;
                updateQuantity(currentVal + 1);
            });
        }

        // Lắng nghe người dùng nhập trực tiếp số vào ô input
        quantityInput.addEventListener("input", function () {
            updateQuantity(this.value);
        });

        // Khởi tạo kiểm tra ban đầu
        updateQuantity(quantityInput.value);
    }

    // ==========================================
    // 2. XỬ LÝ ĐÁNH GIÁ SAO (RATING)
    // ==========================================
    const starsContainer = document.querySelector(".stars");
    const reviewScoreInput = document.getElementById("reviewScore");

    if (starsContainer && reviewScoreInput) {
        const stars = starsContainer.querySelectorAll("i");

        stars.forEach((star) => {
            // Sự kiện khi click chọn số sao
            star.addEventListener("click", function () {
                const value = parseInt(this.getAttribute("data-value"));
                reviewScoreInput.value = value;
                starsContainer.setAttribute("data-rating", value);

                updateStarsVisual(value);
            });

            // Hiệu ứng rê chuột (Hover)
            star.addEventListener("mouseenter", function () {
                const value = parseInt(this.getAttribute("data-value"));
                highlightStars(value);
            });
        });

        // Khi di chuột ra ngoài vùng đánh giá, khôi phục về số sao đã chọn
        starsContainer.addEventListener("mouseleave", function () {
            const selectedRating = parseInt(reviewScoreInput.value) || 0;
            updateStarsVisual(selectedRating);
        });

        // Hàm tô màu sao đã chọn/hover
        function highlightStars(rating) {
            stars.forEach((s) => {
                const val = parseInt(s.getAttribute("data-value"));
                if (val <= rating) {
                    s.classList.remove("fa-regular");
                    s.classList.add("fa-solid", "active");
                } else {
                    s.classList.remove("fa-solid", "active");
                    s.classList.add("fa-regular");
                }
            });
        }

        // Cập nhật giao diện sao cố định
        function updateStarsVisual(rating) {
            highlightStars(rating);
        }
    }

    // Validation form Đánh giá trước khi gửi
    const reviewForm = document.querySelector(".review-form");
    if (reviewForm) {
        reviewForm.addEventListener("submit", function (e) {
            const score = parseInt(reviewScoreInput ? reviewScoreInput.value : 0);
            if (score <= 0) {
                e.preventDefault();
                alert("Vui lòng chọn số sao đánh giá trước khi gửi!");
            }
        });
    }

    // ==========================================
    // 3. XỬ LÝ ẢNH MÓN ÁN LỖI (FALLBACK)
    // ==========================================
    const mainImage = document.getElementById("mainImage");
    if (mainImage) {
        mainImage.addEventListener("error", function () {
            this.src = "/images/comGa.png"; // Đường dẫn ảnh mặc định dự phòng
        });
    }
});