# TODO - Đồng bộ Header/Footer + Active Menu

- [x] Cập nhật fragments/header.html để hỗ trợ active theo `activePage`
- [x] Tạo fragments/footer.html dùng chung
- [x] Sửa HomeController truyền `activePage` cho 5 trang
- [x] Cập nhật trangChu.html dùng fragment header/footer
- [x] Cập nhật thucDon.html dùng fragment header/footer
- [x] Cập nhật khuyenMai.html dùng fragment header/footer
- [x] Cập nhật gioiThieu.html dùng fragment header/footer
- [x] Cập nhật lienHe.html dùng fragment header/footer
- [x] Rà soát CSS header/footer và kiểm tra active underline

# TODO - Điều chỉnh footer mới + header khi đăng nhập

- [x] Cập nhật `footer-secondary` dịch phải 32%
- [x] Cập nhật header: khi đăng nhập hiển thị icon người thay cho nút đăng nhập
- [x] Thêm CSS cho icon người ở header

# TODO - Đồng bộ style nút Đăng xuất

- [x] Thêm class riêng cho nút Đăng xuất trong `fragments/header.html`
- [x] Cập nhật CSS `.logout-btn` trong `static/css/header.css` để đồng bộ style
- [x] Rà soát hiển thị ở trang `khuyenMai` và `gioiThieu`

# TODO - Hiển thị món ăn từ DB ở trang thực đơn

- [x] Cập nhật `FoodService` để lấy danh sách món ăn từ `MonAnRepository`
- [x] Cập nhật `HomeController#thucDon` truyền `danhSachMonAn` vào model
- [x] Cập nhật `thucDon.html` dùng `th:each` render món từ DB + fallback rỗng
- [x] Rà soát và đánh dấu hoàn thành

# TODO - Thêm tìm kiếm và lọc ở trang thực đơn

- [x] Cập nhật `MonAnRepository` thêm query tìm kiếm/lọc theo tên món, trạng thái, danh mục (kèm ảnh)
- [x] Cập nhật `FoodService` xử lý filter + lấy danh mục cho dropdown
- [x] Cập nhật `HomeController#thucDon` nhận query params và trả dữ liệu filter
- [x] Cập nhật `thucDon.html` thành form GET tìm kiếm/lọc + giữ trạng thái filter
- [ ] Kiểm tra click "Chi tiết" sau lọc vẫn mở đúng món

# TODO - Mở rộng lọc trạng thái/số lượng/giá

- [x] Thêm trạng thái `Ngừng bán` vào bộ lọc
- [x] Thêm lọc theo số lượng bán (`Bán nhiều nhất`, `Bán ít nhất`)
- [x] Thêm lọc theo giá nhập vào rồi tìm kiếm
- [x] Cập nhật backend xử lý các điều kiện lọc mới
- [ ] Rà soát hiển thị và giữ giá trị filter sau submit

# TODO - Đồng bộ thêm vào giỏ hàng từ Thực đơn + Chi tiết

- [ ] Cập nhật `HomeController#/gioHang` load dữ liệu giỏ hàng theo user đăng nhập
- [ ] Cập nhật `gioHang.html` hiển thị dữ liệu giỏ hàng thật bằng Thymeleaf
- [ ] Cập nhật `thucDon.html` thêm form POST "Thêm vào giỏ hàng" cho từng món
- [ ] Cập nhật redirect sau thêm giỏ hàng để hiển thị ngay trang giỏ

# TODO - Cập nhật icon giỏ hàng theo số lượng

- [ ] Tạo `cartItemCount` chung cho tất cả trang dùng header bằng `@ModelAttribute` hoặc `@ControllerAdvice`
- [ ] Tính tổng số lượng sản phẩm trong `giỏ hàng` từ session guest hoặc `ChiTietGioHang` của user đăng nhập
- [ ] Cập nhật `fragments/header.html` hiển thị `cartItemCount` trong `span`
- [ ] Cập nhật class/icon trong header để hiển thị khác nhau với `1`, `2`, `3` món
- [ ] Kiểm tra hiển thị header với 1, 2, 3 món và verify khi reload trang

# TODO - Chỉ bắt đăng nhập khi thanh toán

- [ ] Cập nhật `CartController` cho phép guest thêm/cập nhật/xóa giỏ hàng bằng session
- [ ] Cập nhật `HomeController#/gioHang` cho guest xem giỏ hàng, `#/thanhToan` bắt buộc đăng nhập
- [ ] Cập nhật `DangNhapController` hỗ trợ `returnUrl` và merge guest cart sau đăng nhập
- [ ] Cập nhật `CartService` bổ sung hàm merge session cart vào DB cart
- [ ] Rà soát và cập nhật JS liên quan (`chiTietMonAn.js`, `giohang.js`, `thanhtoan.js`) theo flow mới
