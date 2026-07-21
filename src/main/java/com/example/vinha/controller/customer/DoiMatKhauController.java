package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/doi-mat-khau")
public class DoiMatKhauController {
    @GetMapping
    public String showChangePasswordPage(Model model) {
        // Dữ liệu User cơ bản
        Map<String, String> user = new HashMap<>();
        user.put("name", "Nguyễn Thu Hà");
        user.put("email", "thuha.nguyen@email.com");

        model.addAttribute("user", user);
        return "customer/doiMatKhau";
    }

    @PostMapping
    public String processChangePassword(@RequestParam("currentPassword") String currentPassword,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        RedirectAttributes redirectAttributes) {

        // Validate cơ bản phía backend
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "redirect:/doi-mat-khau";
        }

        // TODO: Gọi Service xử lý đổi mật khẩu ở đây

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        return "/doi-mat-khau";
    }
}
