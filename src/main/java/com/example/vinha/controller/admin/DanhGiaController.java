package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminDanhGiaController")
@RequestMapping("/admin/danhGia")
public class DanhGiaController {

    @GetMapping
    public String hienThiTrangDanhGia(Model model) {
        return "/admin/danhGia";
    }
}