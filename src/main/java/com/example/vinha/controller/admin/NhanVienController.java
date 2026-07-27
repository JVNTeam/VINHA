package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhanVienController {

    // ĐÃ FIX: Đổi thành camelCase để khớp với link trên menu
    @GetMapping("/admin/nhanVien")
    public String hienThiNhanVien() {
        return "admin/nhanVien";
    }
}