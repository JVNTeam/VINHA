package com.example.vinha.controller.customer;

import com.example.vinha.entity.DanhGia;
import com.example.vinha.entity.DonHang;
import com.example.vinha.entity.MonAn;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.DanhGiaRepository;
import com.example.vinha.repository.DonHangRepository;
import com.example.vinha.repository.MonAnRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReviewController {

    private final DanhGiaRepository danhGiaRepository;
    private final MonAnRepository monAnRepository;
    private final DonHangRepository donHangRepository;

    public ReviewController(DanhGiaRepository danhGiaRepository,
                            MonAnRepository monAnRepository,
                            DonHangRepository donHangRepository) {
        this.danhGiaRepository = danhGiaRepository;
        this.monAnRepository = monAnRepository;
        this.donHangRepository = donHangRepository;
    }

    @PostMapping("/danhGia/them")
    public String themDanhGia(
            @RequestParam("monAnId") Long monAnId,
            @RequestParam("soSao") Integer soSao,
            @RequestParam(value = "binhLuan", required = false) String binhLuan,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung nguoiDung)) {
            redirectAttributes.addFlashAttribute("reviewMessage", "Vui lòng đăng nhập để đánh giá.");
            return "redirect:/dangNhap?returnUrl=/chiTietMonAn?id=" + monAnId;
        }

        MonAn monAn = monAnRepository.findById(monAnId).orElse(null);
        if (monAn == null) {
            redirectAttributes.addFlashAttribute("reviewMessage", "Không tìm thấy món ăn.");
            return "redirect:/chiTietMonAn?id=" + monAnId;
        }

        DonHang donHang = donHangRepository.findAll().stream()
                .filter(dh -> dh.getNguoiDung() != null && dh.getNguoiDung().getId().equals(nguoiDung.getId()))
                .findFirst()
                .orElse(null);

        if (donHang == null) {
            redirectAttributes.addFlashAttribute("reviewMessage", "Bạn cần có đơn hàng trước khi đánh giá.");
            return "redirect:/chiTietMonAn?id=" + monAnId;
        }

        boolean alreadyReviewed = danhGiaRepository.findByMonAnId(monAnId).stream()
                .anyMatch(dg -> dg.getNguoiDung() != null && dg.getNguoiDung().getId().equals(nguoiDung.getId()));

        if (alreadyReviewed) {
            redirectAttributes.addFlashAttribute("reviewMessage", "Bạn đã đánh giá món này rồi.");
            return "redirect:/chiTietMonAn?id=" + monAnId;
        }

        DanhGia danhGia = DanhGia.builder()
                .monAn(monAn)
                .nguoiDung(nguoiDung)
                .donHang(donHang)
                .soSao(soSao)
                .binhLuan(binhLuan)
                .ngayTao(LocalDateTime.now())
                .build();

        danhGiaRepository.save(danhGia);
        redirectAttributes.addFlashAttribute("reviewMessage", "Cảm ơn bạn đã đánh giá.");
        return "redirect:/chiTietMonAn?id=" + monAnId;
    }
}
