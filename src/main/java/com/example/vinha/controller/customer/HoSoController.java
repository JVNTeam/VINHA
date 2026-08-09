package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/hoso")
public class HoSoController {

    private final NguoiDungRepository nguoiDungRepository;

    public HoSoController(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @GetMapping
    public String showProfilePage(HttpSession session, Model model) {
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
        return "customer/hoSo";
    }

    @PostMapping("/capnhat")
    public String updateProfile(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam("ngaySinh") String ngaySinhStr,
            @RequestParam("gioiTinh") Integer gioiTinh,
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

        user.setHoTen(hoTen);
        user.setSoDienThoai(soDienThoai);
        user.setEmail(email);

        if (ngaySinhStr != null && !ngaySinhStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            user.setNgaySinh(LocalDate.parse(ngaySinhStr, formatter));
        }

        user.setGioiTinh(gioiTinh != null ? gioiTinh.byteValue() : null);

        nguoiDungRepository.save(user);

        // Update session
        session.setAttribute("loggedInUser", user);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "redirect:/hoso";
    }
}
