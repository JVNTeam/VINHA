package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class HoSoController {

    private final NguoiDungRepository nguoiDungRepository;

    public HoSoController(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @GetMapping({"/hoso", "/hoSo"})
    public String viewHoSo(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        // Load fresh user from DB
        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/dangNhap";
        }

        model.addAttribute("user", user);
        return "customer/hoSo";
    }

    @PostMapping({"/hoso/capnhat", "/hoSo/capnhat"})
    public String updateHoSo(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "gioiTinh", required = false) Byte gioiTinh,
            @RequestParam(value = "ngaySinh", required = false) LocalDate ngaySinh,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }

        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        
        if (user != null) {
            user.setHoTen(hoTen);
            user.setSoDienThoai(soDienThoai);
            user.setEmail(email != null && !email.trim().isEmpty() ? email : null);
            user.setGioiTinh(gioiTinh);
            user.setNgaySinh(ngaySinh);

            try {
                nguoiDungRepository.save(user);
                session.setAttribute("loggedInUser", user);
                redirectAttributes.addFlashAttribute("successMessage", "C?p nh?t h? so thành công!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "C?p nh?t th?t b?i. S? di?n tho?i ho?c Email có th? dã t?n t?i.");
            }
        }
        return "redirect:/hoso";
    }
}
