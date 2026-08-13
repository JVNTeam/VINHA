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

// ======================= Nút Lưu =======================

// Sao chép mã giảm giá
document.querySelectorAll(".copy-btn").forEach(btn => {
    btn.addEventListener("click", () => {

        const code = btn.dataset.code;

        navigator.clipboard.writeText(code);

        btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã chép';

        setTimeout(() => {
            btn.innerHTML = '<i class="fa-regular fa-copy"></i> Sao chép';
        }, 1500);

    });
});


//Lưu mã giảm
document.querySelectorAll(".save-btn").forEach(button => {
    button.addEventListener("click", function () {
        const isLoggedIn = this.getAttribute("data-logged-in") === "true";
        if (!isLoggedIn) {
            if(confirm("Bạn cần đăng nhập để lưu mã ưu đãi. Đăng nhập ngay?")) {
                window.location.href = "/dangNhap";
            }
            return;
        }

        // Đổi nội dung và màu nút
        this.innerHTML = '<i class="fa-solid fa-check"></i> Đã lưu';
        this.classList.add("saved");

        // Sau 2 giây trở về trạng thái ban đầu
        setTimeout(() => {
            this.innerHTML = '<i class="fa-regular fa-bookmark"></i> Lưu';
            this.classList.remove("saved");
        }, 2000);

    });
});

// ======================= REAL-TIME VOUCHER POLLING =======================
let currentVoucherHash = null;

function checkVoucherUpdates() {
    fetch('/api/voucher/active-hash')
        .then(res => res.json())
        .then(data => {
            if (!data.hash) return;
            
            if (currentVoucherHash === null) {
                currentVoucherHash = data.hash;
                return;
            }
            
            if (currentVoucherHash !== data.hash) {
                currentVoucherHash = data.hash;
                fetchVouchersAndUpdateDOM();
            }
        })
        .catch(err => console.error('Error polling vouchers:', err));
}

function fetchVouchersAndUpdateDOM() {
    fetch('/khuyenMai')
        .then(res => res.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newVoucherList = doc.querySelector('.voucher-list');
            const currentVoucherList = document.querySelector('.voucher-list');
            
            if (newVoucherList && currentVoucherList) {
                currentVoucherList.innerHTML = newVoucherList.innerHTML;
                
                // Gán lại sự kiện cho các nút mới
                attachEventsToNewVouchers(currentVoucherList);
            }
        })
        .catch(err => console.error('Error fetching updated vouchers:', err));
}

function attachEventsToNewVouchers(container) {
    container.querySelectorAll(".copy-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const code = btn.dataset.code;
            navigator.clipboard.writeText(code);
            btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã chép';
            setTimeout(() => {
                btn.innerHTML = '<i class="fa-regular fa-copy"></i> Sao chép';
            }, 1500);
        });
    });

    container.querySelectorAll(".save-btn").forEach(button => {
        button.addEventListener("click", function () {
            const isLoggedIn = this.getAttribute("data-logged-in") === "true";
            if (!isLoggedIn) {
                if(confirm("Bạn cần đăng nhập để lưu mã ưu đãi. Đăng nhập ngay?")) {
                    window.location.href = "/dangNhap";
                }
                return;
            }
            this.innerHTML = '<i class="fa-solid fa-check"></i> Đã lưu';
            this.classList.add("saved");
            setTimeout(() => {
                this.innerHTML = '<i class="fa-regular fa-bookmark"></i> Lưu';
                this.classList.remove("saved");
            }, 2000);
        });
    });
}

// Check every 3 seconds
setInterval(checkVoucherUpdates, 3000);
checkVoucherUpdates();