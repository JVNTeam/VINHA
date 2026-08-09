package com.example.vinha.controller.customer;

import com.example.vinha.entity.*;
import com.example.vinha.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    public DanhGiaController(NguoiDungRepository nguoiDungRepository, DonHangRepository donHangRepository, DanhGiaRepository danhGiaRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.donHangRepository = donHangRepository;
        this.danhGiaRepository = danhGiaRepository;
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

        // L?y t?t c? don hàng dã hoàn thành c?a user
        List<DonHang> completedOrders = donHangRepository.findByNguoiDungIdAndTrangThai(user.getId(), "Hoàn thành");
        
        // L?y t?t c? dánh giá c?a user
        List<DanhGia> userReviews = danhGiaRepository.findByNguoiDungId(user.getId());
        
        List<Map<String, Object>> pendingReviews = new ArrayList<>();
        
        for (DonHang order : completedOrders) {
            for (ChiTietDonHang chiTiet : order.getChiTietDonHangs()) {
                MonAn monAn = chiTiet.getMonAn();
                
                // Ki?m tra xem món an trong don hàng này dã du?c dánh giá chua
                boolean hasReviewed = userReviews.stream()
                        .anyMatch(r -> r.getDonHang().getId().equals(order.getId()) && r.getMonAn().getId().equals(monAn.getId()));
                
                if (!hasReviewed) {
                    Map<String, Object> itemInfo = new HashMap<>();
                    itemInfo.put("id", monAn.getId());
                    itemInfo.put("donHangId", order.getId());
                    itemInfo.put("name", monAn.getTen());
                    itemInfo.put("orderCode", "#VN-" + order.getId());
                    itemInfo.put("completedDate", order.getNgayTao() != null ? order.getNgayTao().toLocalDate().toString() : "");
                    
                    String imagePath = "https://via.placeholder.com/150";
                    if (monAn.getHinhAnhMonAns() != null && !monAn.getHinhAnhMonAns().isEmpty()) {
                        imagePath = monAn.getHinhAnhMonAns().get(0).getDuongDan();
                    }
                    itemInfo.put("image", imagePath);
                    
                    pendingReviews.add(itemInfo);
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("activeMenu", "danhgia");

        return "customer/danhGia";
    }
}
