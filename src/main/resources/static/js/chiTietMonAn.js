/*==============================
        QUANTITY
==============================*/

const minusBtn = document.getElementById("minus");
const plusBtn = document.getElementById("plus");
const quantityInput = document.getElementById("quantity");

if (minusBtn && plusBtn && quantityInput) {

    minusBtn.addEventListener("click", function () {

        let value = parseInt(quantityInput.value);

        if (value > 1) {
            quantityInput.value = value - 1;
        }

    });

    plusBtn.addEventListener("click", function () {

        let value = parseInt(quantityInput.value);

        quantityInput.value = value + 1;

    });

    quantityInput.addEventListener("input", function () {

        let value = parseInt(this.value);

        if (isNaN(value) || value < 1) {
            this.value = 1;
        }

    });

}

/*==============================
        STAR RATING
==============================*/

const stars = document.querySelectorAll(".stars i");

stars.forEach((star, index) => {

    star.addEventListener("click", function () {

        stars.forEach((item, i) => {

            if (i <= index) {

                item.classList.remove("fa-regular");
                item.classList.add("fa-solid");
                item.classList.add("active");

            } else {

                item.classList.remove("fa-solid");
                item.classList.add("fa-regular");
                item.classList.remove("active");

            }

        });

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

const cartBtn = document.querySelector(".cart-btn");
const buyBtn = document.querySelector(".buy-now");

if (cartBtn) {

    cartBtn.addEventListener("click", function () {

        cartBtn.innerHTML =
            '<i class="fa-solid fa-check"></i> Đã thêm';

        cartBtn.style.background = "#2E7D32";

        setTimeout(() => {

            cartBtn.innerHTML =
                '<i class="fa-solid fa-bag-shopping"></i> Thêm vào giỏ hàng';

            cartBtn.style.background = "#FF8A00";

        }, 1800);

    });

}

if (buyBtn) {

    buyBtn.addEventListener("click", function () {

        alert("Chuyển sang trang thanh toán.");

    });

}

/*==============================
        REVIEW
==============================*/

const reviewForm = document.querySelector(".review-form");

if (reviewForm) {

    reviewForm.addEventListener("submit", function (e) {

        e.preventDefault();

        alert("Đánh giá của bạn đã được gửi.");

        reviewForm.reset();

        stars.forEach(star => {

            star.classList.remove("fa-solid");
            star.classList.add("fa-regular");
            star.classList.remove("active");

        });

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