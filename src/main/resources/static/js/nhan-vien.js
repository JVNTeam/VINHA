document.addEventListener("DOMContentLoaded", function () {

    // 1. Nút Thêm nhân viên mới
    const btnThemMoi = document.getElementById("btnThemMoi");
    if (btnThemMoi) {
        btnThemMoi.addEventListener("click", function () {
            alert("Đang chuyển đến trang Thêm Nhân Viên...");
        });
    }

    // 2. Chuyển hướng sang trang Khách Hàng
    const btnChuyenKhachHang = document.getElementById("btnChuyenKhachHang");
    if (btnChuyenKhachHang) {
        btnChuyenKhachHang.addEventListener("click", function () {
            // Chuyển URL sang trang quản lý khách hàng
            window.location.href = "/admin/khach-hang";
        });
    }

    // 3. Khóa / Mở khóa / Xóa
    const tableBody = document.querySelector("tbody");
    tableBody.addEventListener("click", function (event) {
        const target = event.target.closest("button");
        if (!target) return;
        const row = target.closest("tr");

        // Xóa
        if (target.classList.contains("btn-delete")) {
            if (confirm("Xóa nhân viên này khỏi hệ thống?")) {
                row.remove();
            }
        }
        // Khóa
        else if (target.classList.contains("btn-lock")) {
            if (confirm("Khóa tài khoản nhân viên này?")) {
                const badge = row.querySelector(".status-badge");
                badge.className = "status-badge locked";
                badge.innerHTML = '<i class="fas fa-circle"></i> Bị khóa';

                target.className = "btn-unlock";
                target.innerHTML = '<i class="fas fa-lock-open"></i>';
                target.title = "Mở khóa tài khoản";
            }
        }
        // Mở khóa
        else if (target.classList.contains("btn-unlock")) {
            if (confirm("Mở khóa cho nhân viên này?")) {
                const badge = row.querySelector(".status-badge");
                badge.className = "status-badge active";
                badge.innerHTML = '<i class="fas fa-circle"></i> Hoạt động';

                target.className = "btn-lock";
                target.innerHTML = '<i class="fas fa-lock"></i>';
                target.title = "Khóa tài khoản";
            }
        }
    });

    // 4. Tìm kiếm cơ bản
    const searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("keyup", function () {
        const textTimKiem = searchInput.value.toLowerCase();
        const rows = document.querySelectorAll("tbody tr");

        rows.forEach(function (row) {
            const hoTen = row.querySelectorAll("td")[1].innerText.toLowerCase();
            const lienHe = row.querySelectorAll("td")[2].innerText.toLowerCase();

            if (hoTen.includes(textTimKiem) || lienHe.includes(textTimKiem)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });
});