package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangController {

    @GetMapping("/admin/khachHang")
    public String hienThiKhachHang() {
        // Trỏ về đúng file khachHang.html trong thư mục templates/admin
        return "admin/khachHang";
    }
}