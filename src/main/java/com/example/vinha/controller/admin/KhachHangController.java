package com.example.vinha.controller.admin;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/khachHang")
public class KhachHangController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @GetMapping
    public String hienThiKhachHang(Model model) {

        // Vai trò 1 (Khách hàng)
        List<Long> vaiTroIds = List.of(1L);
        List<NguoiDung> khachHangList = nguoiDungRepository.searchByVaiTroIdsAndKeywordAndStatus(vaiTroIds, "", "");

        model.addAttribute("khachHangList", khachHangList);

        return "admin/khachHang";
    }

    @GetMapping("/toggle")
    public String toggleTrangThai(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        NguoiDung nd = nguoiDungRepository.findById(id).orElse(null);
        if (nd != null) {
            if ("Hoạt Động".equals(nd.getTrangThai())) {
                nd.setTrangThai("Khóa");
                redirectAttributes.addFlashAttribute("message", "Đã khóa tài khoản khách hàng.");
            } else {
                nd.setTrangThai("Hoạt Động");
                redirectAttributes.addFlashAttribute("message", "Đã mở khóa tài khoản khách hàng.");
            }
            nguoiDungRepository.save(nd);
        }
        return "redirect:/admin/khachHang";
    }
}