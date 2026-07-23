document.addEventListener("DOMContentLoaded", function() {

    // 1. Nút Thống Kê
    const btnThongKe = document.getElementById("btnThongKe");
    if (btnThongKe) {
        btnThongKe.addEventListener("click", function() {
            // Lấy giá trị từ các ô input để test
            const inputs = document.querySelectorAll(".filter-section input");
            alert(`Đang lấy dữ liệu từ ngày ${inputs[0].value} đến ngày ${inputs[1].value}...`);
        });
    }

    // 2. Nút Làm Mới
    const btnLamMoi = document.getElementById("btnLamMoi");
    if (btnLamMoi) {
        btnLamMoi.addEventListener("click", function() {
            document.querySelectorAll(".filter-section input").forEach(input => {
                input.value = ""; // Xóa trắng ô input
            });
            alert("Đã làm mới bộ lọc!");
        });
    }

    // 3. Nút Xuất file Excel
    const btnXuatExcel = document.getElementById("btnXuatExcel");
    if (btnXuatExcel) {
        btnXuatExcel.addEventListener("click", function() {
            if(confirm("Ông có muốn tải xuống báo cáo doanh thu dưới dạng file Excel không?")) {
                alert("Đang tải file Báo_cáo_doanh_thu.xlsx...");
                // Sau này phần Backend Java sẽ trả về 1 file thật ở đây
            }
        });
    }

    // 4. Hiệu ứng click vào cột biểu đồ (đổi màu)
    const bars = document.querySelectorAll(".bar");
    bars.forEach(bar => {
        bar.addEventListener("click", function() {
            // Xóa class active ở cột cũ
            document.querySelector(".active-bar")?.classList.remove("active-bar");
            // Thêm class active vào cột vừa click (chuyển sang màu nâu đậm)
            this.classList.add("active-bar");
        });
    });

});