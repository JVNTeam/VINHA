package com.example.vinha.controller.api;

import com.example.vinha.entity.ChiTietDonHang;
import com.example.vinha.entity.DonHang;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietDonHangRepository;
import com.example.vinha.repository.DonHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;

    public OrderApiController(DonHangRepository donHangRepository, ChiTietDonHangRepository chiTietDonHangRepository) {
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable Long orderId,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();

        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để hủy đơn hàng");
            return ResponseEntity.ok(response);
        }

        try {
            Optional<DonHang> optionalOrder = donHangRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                response.put("success", false);
                response.put("message", "Đơn hàng không tồn tại");
                return ResponseEntity.ok(response);
            }

            DonHang order = optionalOrder.get();

            if (order.getNguoiDung() == null || !order.getNguoiDung().getId().equals(user.getId())) {
                response.put("success", false);
                response.put("message", "Bạn không có quyền hủy đơn hàng này");
                return ResponseEntity.ok(response);
            }

            if (!order.getTrangThai().equals("Chờ xác nhận")) {
                response.put("success", false);
                response.put("message", "Chỉ có thể hủy đơn hàng ở trạng thái 'Chờ xác nhận'");
                return ResponseEntity.ok(response);
            }

            order.setTrangThai("Đã hủy");
            donHangRepository.save(order);

            response.put("success", true);
            response.put("message", "Hủy đơn hàng thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi khi hủy đơn hàng: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(
            @PathVariable Long orderId,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();

        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập");
            return ResponseEntity.ok(response);
        }

        try {
            Optional<DonHang> optionalOrder = donHangRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                response.put("success", false);
                response.put("message", "Đơn hàng không tồn tại");
                return ResponseEntity.ok(response);
            }

            DonHang order = optionalOrder.get();

            if (order.getNguoiDung() == null || !order.getNguoiDung().getId().equals(user.getId())) {
                response.put("success", false);
                response.put("message", "Bạn không có quyền xem đơn hàng này");
                return ResponseEntity.ok(response);
            }

            List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHangId(orderId);

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("id", order.getId());
            orderData.put("code", "#VN-" + order.getId());
            orderData.put("date", order.getNgayTao() != null ? order.getNgayTao().toString() : "");
            orderData.put("status", order.getTrangThai());
            orderData.put("totalPrice", order.getTongTien());
            orderData.put("subtotal", order.getTamTinh());
            orderData.put("shippingFee", order.getPhiGiaoHang());
            orderData.put("discount", order.getTienGiam());
            orderData.put("note", order.getGhiChu());
            orderData.put("paymentMethod", order.getHinhThucThanhToan());

            if (order.getDiaChi() != null) {
                orderData.put("address", order.getDiaChi().getDiaChi());
            }

            List<Map<String, Object>> items = new ArrayList<>();
            for (ChiTietDonHang detail : details) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", detail.getMonAn() != null ? detail.getMonAn().getTen() : "Món ăn đã bị xóa");
                item.put("price", detail.getDonGia());
                item.put("quantity", detail.getSoLuong());
                item.put("total", detail.getDonGia() != null && detail.getSoLuong() != null ? detail.getDonGia().multiply(java.math.BigDecimal.valueOf(detail.getSoLuong())) : java.math.BigDecimal.ZERO);
                items.add(item);
            }

            orderData.put("items", items);

            response.put("success", true);
            response.put("data", orderData);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
