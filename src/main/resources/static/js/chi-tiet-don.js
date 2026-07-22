document.addEventListener("DOMContentLoaded", function() {

    // Nút Hủy đơn
    const btnHuy = document.getElementById("btnHuy");
    if (btnHuy) {
        btnHuy.addEventListener("click", function() {
            const xacNhan = confirm("Bạn có chắc chắn muốn HỦY đơn hàng này?");
            if (xacNhan) {
                // Đổi badge trạng thái
                const badge = document.querySelector(".badge-status");
                badge.className = "badge-status";
                badge.style.backgroundColor = "#feebeb";
                badge.style.color = "#e74c3c";
                badge.innerText = "Đã hủy";
                alert("Đã hủy đơn hàng thành công!");
            }
        });
    }

    // Nút Xác nhận đơn
    const btnXacNhan = document.getElementById("btnXacNhan");
    if (btnXacNhan) {
        btnXacNhan.addEventListener("click", function() {
            // Đổi badge trạng thái
            const badge = document.querySelector(".badge-status");
            badge.className = "badge-status";
            badge.style.backgroundColor = "#e8f8f5";
            badge.style.color = "#2ecc71";
            badge.innerText = "Đã xác nhận";

            // Ẩn nút xác nhận đi
            this.style.display = "none";
            alert("Đã xác nhận đơn hàng thành công!");
        });
    }

});