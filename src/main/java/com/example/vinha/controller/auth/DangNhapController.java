package com.example.vinha.controller.auth;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.service.AuthService;
import com.example.vinha.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
public class DangNhapController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @GetMapping("/dangNhap")
    public String showLoginForm(
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute("loggedInUser") != null) {
            if (returnUrl != null && returnUrl.startsWith("/")) {
                return "redirect:" + returnUrl;
            }
            return "redirect:/trangChu";
        }

        model.addAttribute("returnUrl", returnUrl);
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
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            HttpSession session,
            Model model) {

        Optional<NguoiDung> userOpt = authService.authenticate(username, password);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "/auth/login";
        }

        NguoiDung user = userOpt.get();

        session.setAttribute("loggedInUser", user);

        Object guestCartObj = session.getAttribute("guestCart");
        if (guestCartObj instanceof Map<?, ?> mapObj) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> guestCart = (Map<Long, Integer>) mapObj;
            cartService.mergeGuestCart(user.getId(), guestCart);
            session.removeAttribute("guestCart");
        }

        String roleTen = user.getVaiTro().getTen();

        if ("Admin".equals(roleTen)) {
            return "redirect:/admin/tongQuan";
        }

        if (returnUrl != null && returnUrl.startsWith("/")) {
            return "redirect:" + returnUrl;
        }

        return "redirect:/trangChu";
    }

    @GetMapping("/dangXuat")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        return "redirect:/trangChu";
    }
}

