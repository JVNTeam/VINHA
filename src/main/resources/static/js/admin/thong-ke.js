document.addEventListener("DOMContentLoaded", function() {

    // 0. Xử lý dropdown "Thời gian" tự động điền ngày
    const filterType = document.getElementById("filterType");
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");

    if (filterType && startDateInput && endDateInput) {
        filterType.addEventListener("change", function() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');

            if (this.value === 'year') {
                startDateInput.value = `${year}-01-01`;
                endDateInput.value = `${year}-12-31`;
            } else if (this.value === 'month') {
                const lastDay = new Date(year, today.getMonth() + 1, 0).getDate();
                startDateInput.value = `${year}-${month}-01`;
                endDateInput.value = `${year}-${month}-${String(lastDay).padStart(2, '0')}`;
            } else if (this.value === 'day') {
                const todayStr = `${year}-${month}-${day}`;
                startDateInput.value = todayStr;
                endDateInput.value = todayStr;
            }
        });
    }

    // 1. Nút Thống Kê (Form tự submit)
    const btnThongKe = document.getElementById("btnThongKe");

    // 2. Nút Làm Mới (Form tự lo liệu qua link GET /admin/tongQuan)
    const btnLamMoi = document.getElementById("btnLamMoi");
    // Code JS cũ đã được thay bằng href trong HTML

    // 3. Nút Xuất file Excel
    const btnXuatExcel = document.getElementById("btnXuatExcel");
    if (btnXuatExcel) {
        btnXuatExcel.addEventListener("click", function() {
            if(confirm("Bạn có muốn tải xuống báo cáo doanh thu dưới dạng file Excel không?")) {
                if (typeof XLSX === 'undefined') {
                    alert("Đang tải thư viện xuất Excel, vui lòng thử lại sau giây lát...");
                    return;
                }
                
                // Lấy các chỉ số tổng quan
                const tongDoanhThu = document.querySelectorAll(".card-info h3")[0].innerText;
                const tongDonHang = document.querySelectorAll(".card-info h3")[1].innerText;
                const monSapHet = document.querySelectorAll(".card-info h3")[2].innerText;
                const tongKhachHang = document.querySelectorAll(".card-info h3")[3].innerText;
                
                // Lấy danh sách món bán chạy
                const banChayItems = document.querySelectorAll(".top-list:first-of-type .top-item");
                const banChayData = [];
                banChayItems.forEach(item => {
                    const spans = item.querySelectorAll(".item-text span");
                    if (spans.length >= 2) {
                        banChayData.push({ "Món Ăn": spans[0].innerText, "Đã Bán": spans[1].innerText });
                    }
                });

                // Lấy danh sách món bán chậm
                const banChamItems = document.querySelectorAll(".top-list:last-of-type .top-item");
                const banChamData = [];
                banChamItems.forEach(item => {
                    const spans = item.querySelectorAll(".item-text span");
                    if (spans.length >= 2) {
                        banChamData.push({ "Món Ăn": spans[0].innerText, "Đã Bán": spans[1].innerText });
                    }
                });

                // Tạo Workbook
                const wb = XLSX.utils.book_new();

                // 1. Sheet Tổng quan
                const wsTongQuanData = [
                    ["Chỉ Tiêu", "Giá Trị"],
                    ["Tổng Doanh Thu", tongDoanhThu],
                    ["Tổng Đơn Hàng", tongDonHang],
                    ["Tổng Khách Hàng", tongKhachHang],
                    ["Món Sắp Hết Hàng", monSapHet]
                ];
                const wsTongQuan = XLSX.utils.aoa_to_sheet(wsTongQuanData);
                XLSX.utils.book_append_sheet(wb, wsTongQuan, "Tổng Quan");

                // 2. Sheet Món Bán Chạy
                if (banChayData.length > 0) {
                    const wsBanChay = XLSX.utils.json_to_sheet(banChayData);
                    XLSX.utils.book_append_sheet(wb, wsBanChay, "Top Bán Chạy");
                }

                // 3. Sheet Món Bán Chậm
                if (banChamData.length > 0) {
                    const wsBanCham = XLSX.utils.json_to_sheet(banChamData);
                    XLSX.utils.book_append_sheet(wb, wsBanCham, "Top Bán Chậm");
                }

                // Xuất file
                XLSX.writeFile(wb, "Bao_Cao_Doanh_Thu.xlsx");
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