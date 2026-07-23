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
        return "/auth/register";
    }

    @PostMapping("/dangKy")
    public String processRegister(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("email") String email,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("matKhau") String matKhau,
            @RequestParam("xacNhanMatKhau") String xacNhanMatKhau,
            @RequestParam(value = "gioiTinh", required = false) Byte gioiTinh,
            Model model) {

        Optional<String> errorOpt = authService.register(
                hoTen, email, soDienThoai, matKhau, xacNhanMatKhau, gioiTinh
        );

        if (errorOpt.isPresent()) {
            model.addAttribute("error", errorOpt.get());
            model.addAttribute("hoTen", hoTen);
            model.addAttribute("email", email);
            model.addAttribute("soDienThoai", soDienThoai);
            model.addAttribute("gioiTinh", gioiTinh);
            return "/auth/register";
        }

        model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "/auth/login";
    }
}
