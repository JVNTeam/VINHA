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

        List<DanhGia> all = danhGiaRepository.findAll();

        List<DanhGia> sorted = all.stream()
                .sorted((a, b) -> b.getNgayTao().compareTo(a.getNgayTao())) // Mới nhất lên đầu
                .toList();

        int totalReviews = all.size();
        double avgRating = totalReviews > 0 ? all.stream().mapToInt(DanhGia::getSoSao).average().orElse(0.0) : 0.0;
        long fiveStarCount = all.stream().filter(d -> d.getSoSao() != null && d.getSoSao() == 5).count();
        long oneStarCount = all.stream().filter(d -> d.getSoSao() != null && d.getSoSao() == 1).count();

        model.addAttribute("danhGiaList", sorted);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStar", star);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("fiveStarCount", fiveStarCount);
        model.addAttribute("oneStarCount", oneStarCount);

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