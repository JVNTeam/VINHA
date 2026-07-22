// ======================= COPY MÃ GIẢM GIÁ =======================

const copyButtons = document.querySelectorAll(".copy-btn");

copyButtons.forEach(button => {

    button.addEventListener("click", () => {

        const code = button.dataset.code;

        navigator.clipboard.writeText(code)
            .then(() => {

                const oldText = button.innerText;

                button.innerText = "Đã sao chép ✓";

                button.style.background = "#2ecc71";

                setTimeout(() => {

                    button.innerText = oldText;

                    button.style.background = "";

                }, 2000);

            })
            .catch(() => {

                alert("Không thể sao chép mã.");

            });

    });

});


// ======================= CUỘN ĐẾN KHO MÃ =======================

const voucherButton = document.querySelector(".btn-primary");

if (voucherButton) {

    voucherButton.addEventListener("click", function (e) {

        e.preventDefault();

        document.querySelector("#voucher")
            .scrollIntoView({

                behavior: "smooth"

            });

    });

}


// ======================= HIỆU ỨNG KHI CUỘN =======================

const revealItems = document.querySelectorAll(
    ".voucher-card,.step,.special-card"
);

function revealOnScroll() {

    const trigger = window.innerHeight - 100;

    revealItems.forEach(item => {

        const top = item.getBoundingClientRect().top;

        if (top < trigger) {

            item.style.opacity = "1";
            item.style.transform = "translateY(0)";

        }

    });

}

revealItems.forEach(item => {

    item.style.opacity = "0";
    item.style.transform = "translateY(40px)";
    item.style.transition = "all .6s ease";

});

window.addEventListener("scroll", revealOnScroll);

revealOnScroll();


// ======================= HOVER VOUCHER =======================

const vouchers = document.querySelectorAll(".voucher-card");

vouchers.forEach(card => {

    card.addEventListener("mouseenter", () => {

        card.style.transform = "translateY(-8px)";

    });

    card.addEventListener("mouseleave", () => {

        card.style.transform = "translateY(0)";

    });

});


// ======================= NÚT XEM THỰC ĐƠN =======================

const menuButton = document.querySelector(".btn-outline");

if (menuButton) {

    menuButton.addEventListener("click", function () {

        window.location.href = "menu.html";

    });

}


// ======================= THÔNG BÁO CHÀO =======================

window.addEventListener("load", () => {

    console.log("Trang Khuyến mãi đã tải thành công.");

});