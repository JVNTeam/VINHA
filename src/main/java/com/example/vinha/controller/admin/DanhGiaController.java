package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Đặt tên tường minh cho Controller của Admin
@Controller("adminDanhGiaController")
@RequestMapping("/admin/danh-gia")
public class DanhGiaController {

    @GetMapping
    public String hienThiTrangDanhGia(Model model) {
        return "danhGia";
    }
}