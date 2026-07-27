package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class tongQuancontroller {

    // Đường dẫn để vào trang này là localhost:8080/admin/tongQuan
    @GetMapping("/admin/tongQuan")
    public String hienThiTrangThongKe(Model model) {

        /*
         * Sau này viết logic lấy dữ liệu doanh thu, biểu đồ, món bán chạy ở đây:
         * model.addAttribute("tongDoanhThu", ...);
         */

<<<<<<< Updated upstream:src/main/java/com/example/vinha/controller/admin/ThongKeController.java
        // Trả về đúng file thong-ke.html trong thư mục templates/admin/
        return "admin/thong-ke";
=======
        // ĐÃ FIX: Bỏ dấu "/" ở đầu. Trả về đúng file tongQuan.html trong templates/admin/
        return "admin/tongQuan";
>>>>>>> Stashed changes:src/main/java/com/example/vinha/controller/admin/tongQuancontroller.java
    }
}