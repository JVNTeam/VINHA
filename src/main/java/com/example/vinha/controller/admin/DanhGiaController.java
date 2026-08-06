package com.example.vinha.controller.admin;

import com.example.vinha.entity.DanhGia;
import com.example.vinha.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller("adminDanhGiaController")
@RequestMapping("/admin/danhGia")
public class DanhGiaController {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    @GetMapping
    public String hienThiTrangDanhGia(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "star", required = false) Integer star,
            Model model) {

        String kw = keyword != null ? keyword.trim().toLowerCase() : "";

        List<DanhGia> all = danhGiaRepository.findAll();

        List<DanhGia> filtered = all.stream()
                .filter(d -> {
                    boolean matchKeyword = kw.isEmpty() ||
                            (d.getBinhLuan() != null && d.getBinhLuan().toLowerCase().contains(kw)) ||
                            (d.getMonAn() != null && d.getMonAn().getTen().toLowerCase().contains(kw)) ||
                            (d.getNguoiDung() != null && d.getNguoiDung().getHoTen().toLowerCase().contains(kw));
                    boolean matchStar = star == null || (d.getSoSao() != null && d.getSoSao().equals(star));
                    return matchKeyword && matchStar;
                })
                .sorted((a, b) -> b.getNgayTao().compareTo(a.getNgayTao())) // Mới nhất lên đầu
                .toList();

        model.addAttribute("danhGiaList", filtered);
        model.addAttribute("keyword", kw);
        model.addAttribute("selectedStar", star);

        return "admin/danhgia";
    }

    @GetMapping("/xoa")
    public String xoaDanhGia(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            danhGiaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa đánh giá thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa đánh giá này.");
        }
        return "redirect:/admin/danhGia";
    }
}