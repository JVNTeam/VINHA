package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doimatkhau")
public class DoiMatKhauController {

    private final NguoiDungRepository nguoiDungRepository;

    public DoiMatKhauController(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @GetMapping
    public String showChangePasswordPage(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }

        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/dangNhap";
        }

        model.addAttribute("user", user);
        return "customer/doiMatKhau";
    }

    @PostMapping("/capnhat")
    public String processChangePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }

        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/dangNhap";
        }

        if (!user.getMatKhau().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "M?t kh?u hi?n t?i không dúng!");
            return "redirect:/doimatkhau";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "M?t kh?u xác nh?n không kh?p!");
            return "redirect:/doimatkhau";
        }

        user.setMatKhau(newPassword);
        nguoiDungRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Ð?i m?t kh?u thành công!");
        return "redirect:/doimatkhau";
    }
}
