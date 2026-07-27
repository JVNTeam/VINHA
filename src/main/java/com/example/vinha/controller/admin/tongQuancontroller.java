package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class tongQuancontroller {

    @GetMapping("/admin/tongQuan")
    public String hienThiTrangThongKe(Model model) {
        return "admin/thong-ke";
    }
}