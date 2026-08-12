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




    @PostMapping("/dangNhap")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            HttpSession session,
            Model model) {

        Optional<NguoiDung> userOpt = authService.authenticate(username, password);

        // Đăng nhập thất bại
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "/auth/login";
        }

        // Đăng nhập thành công
        NguoiDung user = userOpt.get();

        // Lưu user vào session
        session.setAttribute("loggedInUser", user);

        // Gộp giỏ hàng của khách với giỏ hàng trong tài khoản
        Object guestCartObj = session.getAttribute("guestCart");

        if (guestCartObj instanceof Map<?, ?> mapObj) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> guestCart = (Map<Long, Integer>) mapObj;

            cartService.mergeGuestCart(user.getId(), guestCart);

            session.removeAttribute("guestCart");
        }

        String roleTen = user.getVaiTro().getTen();

        if (roleTen != null && "Admin".equalsIgnoreCase(roleTen.trim())) {
            return "redirect:/admin/tongQuan";
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

