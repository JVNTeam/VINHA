package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TongQuanController {

    @GetMapping("/admin/thong-ke")
    public String hienThiThongKe() {
        return "admin/tongQuan";
    }
}

