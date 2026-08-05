package com.example.vinha.controller.customer;

import com.example.vinha.entity.DonHang;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.DonHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LichSuDonHangController {

    private final DonHangRepository donHangRepository;

    public LichSuDonHangController(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    @GetMapping({"/lichsudonhang", "/lichsudonhang"})
    public String showOrderHistoryPage(HttpSession session, Model model) {
        Object userObj = session.getAttribute("loggedInUser");

        if (!(userObj instanceof NguoiDung user)) {
            return "redirect:/dangNhap?returnUrl=/lichsudonhang";
        }

        // Get user's orders
        List<DonHang> orders = donHangRepository.findByNguoiDungIdOrderByNgayTaoDesc(user.getId());

        // Convert to display format
        List<Map<String, Object>> orderList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (DonHang order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("code", "#VN-" + order.getId());
            orderMap.put("date", order.getNgayTao() != null ? order.getNgayTao().format(formatter) : "");
            orderMap.put("price", order.getTongTien() != null ? 
                order.getTongTien().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
            orderMap.put("statusCode", normalizeStatus(order.getTrangThai()));
            orderMap.put("statusText", order.getTrangThai());
            orderList.add(orderMap);
        }

        // Create user map
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user.getHoTen() != null ? user.getHoTen() : "");
        userMap.put("email", user.getEmail() != null ? user.getEmail() : "");

        model.addAttribute("user", userMap);
        model.addAttribute("orders", orderList);

        return "customer/lichSuDon";
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return "pending";
        }
        return switch (status) {
            case "Chờ xác nhận" -> "pending";
            case "Xác nhận" -> "confirmed";
            case "Hoàn thành" -> "completed";
            case "Hủy" -> "cancelled";
            default -> "pending";
        };
    }
}
