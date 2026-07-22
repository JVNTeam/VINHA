package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DonHangController {

    @GetMapping("/admin/don-hang")
    public String hienThiDonHang() {
        return "admin/donHang";
    }
}