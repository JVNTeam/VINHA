document.addEventListener("DOMContentLoaded", function() {

    // 1. Nút Thêm mới
    const btnAdd = document.querySelector(".btn-add");
    if (btnAdd) {
        btnAdd.addEventListener("click", function() {
            alert("Chuyển hướng đến trang Thêm Khách Hàng...");
        });
    }

    // 2. Chức năng Khóa / Mở khóa tài khoản ngay trên bảng
    const tableBody = document.querySelector("tbody");

    // Bắt sự kiện click chung cho toàn bộ bảng (để code gọn)
    tableBody.addEventListener("click", function(event) {
        const target = event.target.closest("button"); // Tìm nút vừa được bấm
        if (!target) return;

        const row = target.closest("tr");

        // Nếu bấm nút Xóa
        if (target.classList.contains("btn-delete")) {
            if (confirm("Ông có chắc chắn muốn xóa khách hàng này khỏi hệ thống?")) {
                row.remove();
                alert("Đã xóa!");
            }
        }

        // Nếu bấm nút Khóa tài khoản
        if (target.classList.contains("btn-lock")) {
            if (confirm("Xác nhận khóa tài khoản này?")) {
                // Đổi badge trạng thái sang màu đỏ
                const badge = row.querySelector(".status-badge");
                badge.className = "status-badge locked";
                badge.innerHTML = '<i class="fas fa-circle"></i> Bị khóa';

                // Đổi icon Khóa (lock) thành Mở khóa (lock-open)
                target.className = "btn-unlock";
                target.innerHTML = '<i class="fas fa-lock-open"></i>';
                target.title = "Mở khóa tài khoản";
            }
        }

        // Nếu bấm nút Mở khóa tài khoản
        else if (target.classList.contains("btn-unlock")) {
            if (confirm("Xác nhận mở khóa tài khoản này?")) {
                // Đổi badge trạng thái sang màu xanh
                const badge = row.querySelector(".status-badge");
                badge.className = "status-badge active";
                badge.innerHTML = '<i class="fas fa-circle"></i> Hoạt động';

                // Đổi icon Mở khóa thành Khóa
                target.className = "btn-lock";
                target.innerHTML = '<i class="fas fa-lock"></i>';
                target.title = "Khóa tài khoản";
            }
        }
    });

    // 3. Chức năng tìm kiếm theo Tên / Email / SĐT
    const searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("keyup", function() {
        const textTimKiem = searchInput.value.toLowerCase();
        const rows = document.querySelectorAll("tbody tr");

        rows.forEach(function(row) {
            // Lấy nội dung của cột Họ Tên và Liên hệ
            const hoTen = row.querySelectorAll("td")[1].innerText.toLowerCase();
            const lienHe = row.querySelectorAll("td")[2].innerText.toLowerCase();

            // Gộp lại để tìm, nếu khớp thì hiện, không thì giấu
            if (hoTen.includes(textTimKiem) || lienHe.includes(textTimKiem)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });

});