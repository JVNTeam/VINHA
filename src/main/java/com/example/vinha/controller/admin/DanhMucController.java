package com.example.vinha.controller.admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Đánh dấu đây là một Controller của Spring Boot
@Controller
public class DanhMucController {

    // Tạo đường dẫn URL để truy cập trên trình duyệt
    @GetMapping("/admin/danh-muc")
    public String hienThiDanhMuc() {
        // Trả về đúng tên thư mục và tên file HTML của ông (không cần gõ chữ .html)
        // Vì nãy ông lưu ở templates/admin/danhMuc.html
        return "admin/danhMuc";
    }
}