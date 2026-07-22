package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhieuGiamGiaController { // Đổi tên Class

    @GetMapping("/admin/phieu-giam-gia") // Đổi link hiển thị trên web
    public String hienThiPhieuGiamGia() {
        return "admin/phieuGiamGia"; // Trả về đúng tên file phieuGiamGia.html
    }
}