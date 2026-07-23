document.addEventListener("DOMContentLoaded", function () {

    // Logic thay đổi trạng thái ngay trên bảng
    const statusBtns = document.querySelectorAll(".status-btn");

    statusBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            // Lấy cái khung chứa 4 nút của hàng hiện tại
            const stack = this.closest(".status-stack");

            // Tắt màu cam (class active) của tất cả các nút trong khung này
            const allBtnsInStack = stack.querySelectorAll(".status-btn");
            allBtnsInStack.forEach(b => b.classList.remove("active"));

            // Bật màu cam cho cái nút vừa được click
            this.classList.add("active");

            // Có thể thêm thông báo để báo cáo cho sinh động
            const trangThaiMoi = this.innerText;
            const maDon = this.closest("tr").querySelector(".text-orange.font-bold").innerText;
            console.log(`Đã cập nhật đơn ${maDon} thành: ${trangThaiMoi}`);
        });
    });

    // Nút Xem chi tiết
    const btnDetails = document.querySelectorAll(".btn-detail");
    btnDetails.forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault(); // Ngăn trình duyệt chuyển trang nếu href đang để "#"
            const maDon = this.closest("tr").querySelector(".text-orange.font-bold").innerText;
            alert(`Chuyển đến trang chi tiết của đơn hàng: ${maDon}`);
        });
    });

    // Nút Tạo món mới
    const btnCreate = document.querySelector(".btn-create");
    if (btnCreate) {
        btnCreate.addEventListener("click", function () {
            alert("Mở form tạo món ăn mới!");
        });
    }

});