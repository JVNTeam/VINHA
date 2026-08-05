package com.example.vinha.controller.auth;

import com.example.vinha.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class DangKyController {

    @Autowired
    private AuthService authService;

    @GetMapping("/dangKy")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/dangKy")
    public String processRegister(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("contact") String contact, // Gộp email và soDienThoai thành contact
            @RequestParam("matKhau") String matKhau,
            @RequestParam("xacNhanMatKhau") String xacNhanMatKhau,
            @RequestParam(value = "gioiTinh", required = false) Integer gioiTinh, // Đổi sang Integer để đồng bộ với AuthService
            Model model) {

        Optional<String> errorOpt = authService.register(
                hoTen, contact, matKhau, xacNhanMatKhau, gioiTinh
        );

        if (errorOpt.isPresent()) {
            model.addAttribute("error", errorOpt.get());
            model.addAttribute("hoTen", hoTen);
            model.addAttribute("contact", contact); // Trả lại thông tin contact đã nhập khi có lỗi
            model.addAttribute("gioiTinh", gioiTinh);
            return "auth/register";
        }

        model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "auth/login";
    }
}