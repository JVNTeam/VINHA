package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeController {

    // Đây là đường dẫn duy nhất và chính chủ cho trang Thống kê doanh thu
    @GetMapping("/admin/thong-ke")
    public String hienThiTrangThongKe(Model model) {

        /*
         * Sau này viết logic lấy dữ liệu doanh thu, biểu đồ, món bán chạy ở đây:
         * model.addAttribute("tongDoanhThu", ...);
         */

        // Trả về đúng file thong-ke.html trong thư mục templates/admin/
        return "admin/thong-ke";
    }
}