package com.example.vinha.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("quanLyMonAnController") // Đổi tên này thành một tên khác hoàn toàn độc lập
@RequestMapping("/admin/mon-an")
public class MonAnController {

    @GetMapping
    public String hienThiQuanLyMonAn(Model model) {
        return "admin/mon-an";
    }
}