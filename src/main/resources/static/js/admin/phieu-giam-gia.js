document.addEventListener("DOMContentLoaded", function () {

    // 1. Lắng nghe sự kiện BẬT/TẮT mã giảm giá
    const toggleSwitches = document.querySelectorAll('.toggle-switch input[type="checkbox"]');

    toggleSwitches.forEach(toggle => {
        toggle.addEventListener('change', function () {
            const row = this.closest('tr');
            const maGiamGia = row.querySelector('.code-badge').innerText;

            if (this.checked) {
                // Nếu gạt sang màu xanh (Bật)
                alert(`Đã KÍCH HOẠT mã giảm giá: ${maGiamGia}`);
                row.querySelector('.code-badge').className = "code-badge bg-orange-light text-orange";
            } else {
                // Nếu gạt sang màu xám (Tắt)
                alert(`Đã TẮT mã giảm giá: ${maGiamGia}`);
                row.querySelector('.code-badge').className = "code-badge bg-gray-light text-gray-dark";
            }

            // Note: Sau này chỗ này ông sẽ dùng Fetch/Ajax gửi API báo cho Java biết để lưu vào SQL
        });
    });

    // 2. Nút Thêm mới
    document.getElementById("btnThemMoi").addEventListener("click", function () {
        alert("Mở form thêm Mã giảm giá mới...");
    });

    // 3. Xử lý nút Xóa (có xác nhận)
    const btnDeletes = document.querySelectorAll(".btn-delete");
    btnDeletes.forEach(btn => {
        btn.addEventListener("click", function () {
            const row = this.closest("tr");
            const maGiamGia = row.querySelector('.code-badge').innerText;

            if (confirm(`Ông có chắc chắn muốn xóa vĩnh viễn mã [${maGiamGia}] không?`)) {
                row.remove();
                alert("Đã xóa thành công!");
            }
        });
    });

    // 4. Reset Form lọc
    document.getElementById("btnLamMoi").addEventListener("click", function () {
        document.querySelector(".search-box input").value = "";
        document.querySelectorAll(".form-control").forEach(input => input.value = "");
        // Đặt thẻ select về option đầu tiên
        document.querySelector("select.form-control").selectedIndex = 0;
    });

    // 5. Nút Tìm kiếm
    document.getElementById("btnTimKiem").addEventListener("click", function () {
        const textTimKiem = document.querySelector(".search-box input").value;
        if (textTimKiem) {
            alert(`Đang tìm kiếm mã: ${textTimKiem}`);
        } else {
            alert("Vui lòng nhập thông tin để tìm kiếm!");
        }
    });

});