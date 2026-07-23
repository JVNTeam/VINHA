// Đợi cho HTML/CSS tải xong hết thì JS mới bắt đầu chạy
document.addEventListener("DOMContentLoaded", function() {

    // 1. XỬ LÝ NÚT THÊM MỚI
    const btnAdd = document.querySelector(".btn-add");
    if (btnAdd) {
        btnAdd.addEventListener("click", function() {
            // Tạm thời hiển thị thông báo, sau này ông sẽ dùng JS để mở Modal (bảng nhỏ) hoặc chuyển trang
            alert("Sắp tới chỗ này sẽ mở ra một bảng để nhập thông tin Danh mục mới!");
        });
    }

    // 2. XỬ LÝ TÌM KIẾM CƠ BẢN (Lọc trực tiếp trên bảng)
    const searchInput = document.querySelector(".input-group input");
    const btnSearch = document.querySelector(".btn-search");
    const tableRows = document.querySelectorAll("tbody tr"); // Lấy tất cả các hàng trong bảng

    btnSearch.addEventListener("click", function() {
        // Lấy chữ mà người dùng gõ vào, chuyển hết thành chữ thường để dễ so sánh
        const textTimKiem = searchInput.value.toLowerCase();

        // Duyệt qua từng hàng trong bảng
        tableRows.forEach(function(row) {
            // Lấy tên danh mục ở cột thứ 2 (chỉ số 1)
            const tenDanhMuc = row.querySelectorAll("td")[1].innerText.toLowerCase();

            // Nếu tên danh mục có chứa chữ tìm kiếm -> Hiện hàng đó lên, ngược lại thì Giấu đi
            if (tenDanhMuc.includes(textTimKiem)) {
                row.style.display = ""; // Hiện
            } else {
                row.style.display = "none"; // Giấu
            }
        });
    });

    // Xử lý nút "Làm mới" (Reset tìm kiếm)
    const btnReset = document.querySelector(".btn-reset");
    btnReset.addEventListener("click", function() {
        searchInput.value = ""; // Xóa trắng ô nhập
        tableRows.forEach(function(row) {
            row.style.display = ""; // Hiện lại tất cả các hàng
        });
    });

    // 3. XỬ LÝ NÚT XÓA (Hiện thông báo xác nhận)
    const btnDeletes = document.querySelectorAll(".actions .fa-trash");
    btnDeletes.forEach(function(iconXoa) {
        // Bắt sự kiện khi click vào cái thùng rác
        iconXoa.parentElement.addEventListener("click", function() {
            // Bật cửa sổ hỏi xác nhận
            const xacNhan = confirm("Ông có chắc chắn muốn xóa danh mục này không?");

            if (xacNhan) {
                // Nếu bấm OK -> Xóa luôn cái hàng đó khỏi giao diện
                const row = this.closest("tr");
                row.remove();
                alert("Đã xóa thành công (Đây là xóa ảo trên giao diện, sau này kết nối Backend sẽ xóa thật)!");
            }
        });
    });

    // 4. BẤM CHUYỂN TRANG (Đổi màu nút phân trang)
    const pageBtns = document.querySelectorAll(".page-btn");
    pageBtns.forEach(function(btn) {
        btn.addEventListener("click", function() {
            // Xóa màu ở nút cũ đang được chọn
            document.querySelector(".page-btn.active").classList.remove("active");
            // Thêm màu vào nút vừa được bấm
            this.classList.add("active");
        });
    });

});