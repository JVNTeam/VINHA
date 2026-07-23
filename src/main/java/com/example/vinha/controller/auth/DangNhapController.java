package com.example.vinha.controller.auth;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DangNhapController {

    @Autowired
    private AuthService authService;

    @GetMapping("/dangNhap")
    public String showLoginForm(HttpSession session) {
        // Nếu đã đăng nhập thì chuyển hướng
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/trangChu";
        }
        return "/auth/login";
    }

//    @GetMapping("/dangKy")
//    public String showRegisterForm() {
//        return "/auth/register";
//    }

    @GetMapping("/quenMatKhau")
    public String showForgotPasswordForm() {
        return "/auth/ForgetPassword";
    }

    @PostMapping("/dangNhap")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Optional<NguoiDung> userOpt = authService.authenticate(username, password);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "/auth/login";
        }

        NguoiDung user = userOpt.get();

        // Lưu thông tin user vào session
        session.setAttribute("loggedInUser", user);

        // Kiểm tra role để chuyển hướng
        String roleTen = user.getVaiTro().getTen();

        if ("Admin".equals(roleTen)) {
            return "redirect:/admin/thong-ke";
        }

        // Mặc định: Khách hàng -> Trang chủ
        return "redirect:/trangChu";
    }

    @GetMapping("/dangXuat")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        return "redirect:/trangChu";
    }
}

