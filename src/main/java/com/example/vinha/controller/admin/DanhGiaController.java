package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Đặt tên tường minh cho Controller của Admin
@Controller("adminDanhGiaController")
@RequestMapping("/admin/danhGia")
public class DanhGiaController {

    @GetMapping
    public String hienThiTrangDanhGia(Model model) {
<<<<<<< Updated upstream
        return "admin/danh-gia";
=======
        return "/admin/danhGia";
>>>>>>> Stashed changes
    }
}