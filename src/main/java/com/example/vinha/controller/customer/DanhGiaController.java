package com.example.vinha.controller.customer;

import com.example.vinha.entity.*;
import com.example.vinha.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/danhgia")
public class DanhGiaController {

    private final NguoiDungRepository nguoiDungRepository;
    private final DonHangRepository donHangRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final MonAnRepository monAnRepository;

    public DanhGiaController(NguoiDungRepository nguoiDungRepository, DonHangRepository donHangRepository, DanhGiaRepository danhGiaRepository, MonAnRepository monAnRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.donHangRepository = donHangRepository;
        this.danhGiaRepository = danhGiaRepository;
        this.monAnRepository = monAnRepository;
    }

    @GetMapping
    public String showReviewPage(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }

        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/dangNhap";
        }

        // Fix encoding issue by using correct string "Hoàn thành"
        List<DonHang> completedOrders = donHangRepository.findByNguoiDungIdAndTrangThai(user.getId(), "Hoàn thành");
        List<DanhGia> userReviews = danhGiaRepository.findByNguoiDungId(user.getId());
        List<Map<String, Object>> pendingReviews = new ArrayList<>();
        List<Map<String, Object>> reviewedItems = new ArrayList<>();
        
        for (DonHang order : completedOrders) {
            for (ChiTietDonHang chiTiet : order.getChiTietDonHangs()) {
                MonAn monAn = chiTiet.getMonAn();
                DanhGia review = userReviews.stream()
                        .filter(r -> r.getDonHang().getId().equals(order.getId()) && r.getMonAn().getId().equals(monAn.getId()))
                        .findFirst()
                        .orElse(null);
                
                String imagePath = "https://via.placeholder.com/150";
                if (monAn.getHinhAnhs() != null && !monAn.getHinhAnhs().isEmpty()) {
                    imagePath = monAn.getHinhAnhs().get(0).getDuongDan();
                }

                Map<String, Object> itemInfo = new HashMap<>();
                itemInfo.put("id", monAn.getId());
                itemInfo.put("donHangId", order.getId());
                itemInfo.put("name", monAn.getTen());
                itemInfo.put("orderCode", "#VN-" + order.getId());
                itemInfo.put("completedDate", order.getNgayTao() != null ? order.getNgayTao().toLocalDate().toString() : "");
                itemInfo.put("image", imagePath);

                if (review == null) {
                    pendingReviews.add(itemInfo);
                } else {
                    itemInfo.put("rating", review.getSoSao());
                    itemInfo.put("comment", review.getBinhLuan());
                    itemInfo.put("reviewDate", review.getNgayTao() != null ? review.getNgayTao().toLocalDate().toString() : "");
                    reviewedItems.add(itemInfo);
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("reviewedItems", reviewedItems);
        model.addAttribute("activeMenu", "danhgia");

        return "customer/danhGia";
    }

    @PostMapping("/them")
    public String addReview(
            @org.springframework.web.bind.annotation.RequestParam("donHangId") Long donHangId,
            @org.springframework.web.bind.annotation.RequestParam("monAnId") Long monAnId,
            @org.springframework.web.bind.annotation.RequestParam("soSao") Integer soSao,
            @org.springframework.web.bind.annotation.RequestParam("binhLuan") String binhLuan,
            HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        NguoiDung user = (NguoiDung) loggedInUser;
        
        DonHang donHang = donHangRepository.findById(donHangId).orElse(null);
        MonAn monAn = monAnRepository.findById(monAnId).orElse(null);
        
        if (donHang != null && monAn != null) {
            DanhGia danhGia = new DanhGia();
            danhGia.setDonHang(donHang);
            danhGia.setMonAn(monAn);
            danhGia.setNguoiDung(user);
            danhGia.setSoSao(soSao);
            danhGia.setBinhLuan(binhLuan);
            
            danhGiaRepository.save(danhGia);
            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá sản phẩm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể đánh giá. Vui lòng thử lại.");
        }
        
        return "redirect:/danhgia";
    }
}
