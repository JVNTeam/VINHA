package com.example.vinha.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDonHangController {

    @GetMapping("/admin/chi-tiet-don")
    public String xemChiTietDonHang() {
        // Sửa chữ "admin" thành "user" để nó mò đúng vào thư mục user tìm file
        return "admin/CTDonHang";
    }
}