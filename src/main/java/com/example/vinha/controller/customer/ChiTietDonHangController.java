package com.example.vinha.controller.customer;

import com.example.vinha.entity.ChiTietDonHang;
import com.example.vinha.entity.DonHang;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietDonHangRepository;
import com.example.vinha.repository.DonHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/tai-khoan/chi-tiet-don-hang")
public class ChiTietDonHangController {

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;

    public ChiTietDonHangController(DonHangRepository donHangRepository, ChiTietDonHangRepository chiTietDonHangRepository) {
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
    }

    @GetMapping("/{orderId}")
    public String showOrderDetail(
            @PathVariable Long orderId,
            HttpSession session,
            Model model
    ) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            return "redirect:/dangNhap?returnUrl=/tai-khoan/chi-tiet-don-hang/" + orderId;
        }

        Optional<DonHang> optionalOrder = donHangRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return "redirect:/tai-khoan/lich-su-don-hang";
        }

        DonHang order = optionalOrder.get();
        if (order.getNguoiDung() == null || !order.getNguoiDung().getId().equals(user.getId())) {
            return "redirect:/tai-khoan/lich-su-don-hang";
        }

        List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHangId(orderId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("id", order.getId());
        orderData.put("code", "#VN-" + order.getId());
        orderData.put("date", order.getNgayTao() != null ? order.getNgayTao().format(formatter) : "");
        orderData.put("status", order.getTrangThai());
        orderData.put("statusCode", normalizeStatus(order.getTrangThai()));
        orderData.put("totalPrice", order.getTongTien() != null ? 
            order.getTongTien().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
        orderData.put("subtotal", order.getTamTinh() != null ? 
            order.getTamTinh().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
        orderData.put("shippingFee", order.getPhiGiaoHang() != null ? 
            order.getPhiGiaoHang().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
        orderData.put("discount", order.getTienGiam() != null ? 
            order.getTienGiam().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
        orderData.put("note", order.getGhiChu() != null ? order.getGhiChu() : "");
        orderData.put("paymentMethod", order.getHinhThucThanhToan());

        if (order.getDiaChi() != null) {
            orderData.put("address", order.getDiaChi().getDiaChi());
        }

        List<Map<String, Object>> items = new ArrayList<>();
        for (ChiTietDonHang detail : details) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", detail.getMonAn() != null ? detail.getMonAn().getTen() : "Món ăn đã bị xóa");
            item.put("price", detail.getDonGia() != null ? detail.getDonGia().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
            item.put("quantity", detail.getSoLuong());
            item.put("total", detail.getDonGia() != null && detail.getSoLuong() != null ? detail.getDonGia()
                .multiply(java.math.BigDecimal.valueOf(detail.getSoLuong()))
                .toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ" : "0đ");
            items.add(item);
        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user.getHoTen() != null ? user.getHoTen() : "");
        userMap.put("email", user.getEmail() != null ? user.getEmail() : "");

        model.addAttribute("order", orderData);
        model.addAttribute("items", items);
        model.addAttribute("user", userMap);

        return "customer/chiTietDonHang";
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return "pending";
        }
        return switch (status) {
            case "Chờ xác nhận" -> "pending";
            case "Đã xác nhận" -> "confirmed";
            case "Đang chế biến" -> "processing";
            case "Đang giao hàng" -> "shipping";
            case "Hoàn thành" -> "completed";
            case "Đã hủy" -> "cancelled";
            default -> "pending";
        };
    }
}
