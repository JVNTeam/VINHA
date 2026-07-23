document.addEventListener("DOMContentLoaded", function() {

    // Nút Hủy đơn hàng
    const btnHuyDon = document.getElementById("btnHuyDon");
    if (btnHuyDon) {
        btnHuyDon.addEventListener("click", function() {
            const maDon = document.querySelector(".order-id").innerText;
            const xacNhan = confirm(`Bạn có thực sự muốn yêu cầu hủy ${maDon} không? Lệnh này sẽ được gửi tới nhà hàng.`);

            if (xacNhan) {
                alert("Yêu cầu hủy đơn đã được gửi đi. Vui lòng theo dõi trạng thái đơn hàng!");
            }
        });
    }

    // Nút Quay lại
    const btnQuayLai = document.getElementById("btnQuayLai");
    if (btnQuayLai) {
        btnQuayLai.addEventListener("click", function() {
            // Lệnh này tương đương nút Back trên trình duyệt
            window.history.back();
        });
    }

});