package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhieuGiamGiaController {

    // Đã sửa thành camelCase cho chuẩn phong cách code
    @GetMapping("/admin/phieuGiamGia")
    public String hienThiPhieuGiamGia() {
        return "admin/phieuGiamGia";
    }
}