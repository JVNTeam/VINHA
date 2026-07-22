package com.example.vinha.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhanVienController {

    @GetMapping("/admin/nhan-vien")
    public String hienThiNhanVien() {
        return "admin/nhanVien";
    }
}