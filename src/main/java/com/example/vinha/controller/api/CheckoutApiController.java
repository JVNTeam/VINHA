package com.example.vinha.controller.api;

import com.example.vinha.entity.*;
import com.example.vinha.repository.*;
import com.example.vinha.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutApiController {

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final DiaChiRepository diaChiRepository;
    private final VoucherService voucherService;
    private final ThanhToanRepository thanhToanRepository;

    public CheckoutApiController(
            DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            DiaChiRepository diaChiRepository,
            VoucherService voucherService,
            ThanhToanRepository thanhToanRepository
    ) {
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.diaChiRepository = diaChiRepository;
        this.voucherService = voucherService;
        this.thanhToanRepository = thanhToanRepository;
    }

    @PostMapping("/place-order")
    public ResponseEntity<Map<String, Object>> placeOrder(
            @RequestParam("fullname") String fullname,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("province") String province,
            @RequestParam("payment") String payment,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "voucherId", required = false) Long voucherId,
            @RequestParam(value = "discount", defaultValue = "0") String discountStr,
            @RequestParam(value = "subtotal", required = false) String subtotalStr,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để đặt hàng");
            return ResponseEntity.ok(response);
        }

        try {
            // Convert discount and subtotal
            BigDecimal discount = BigDecimal.ZERO;
            BigDecimal subtotal = BigDecimal.ZERO;
            
            if (discountStr != null && !discountStr.isEmpty()) {
                try {
                    discount = new BigDecimal(discountStr);
                } catch (Exception e) {
                    discount = BigDecimal.ZERO;
                }
            }
            
            if (subtotalStr != null && !subtotalStr.isEmpty()) {
                try {
                    subtotal = new BigDecimal(subtotalStr);
                } catch (Exception e) {
                    subtotal = BigDecimal.ZERO;
                }
            }
            
            // Validate input
            if (fullname == null || fullname.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng nhập họ tên");
                return ResponseEntity.ok(response);
            }

            if (!phone.matches("^0\\d{9}$")) {
                response.put("success", false);
                response.put("message", "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và có 10 chữ số)");
                return ResponseEntity.ok(response);
            }

            if (address == null || address.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng nhập địa chỉ");
                return ResponseEntity.ok(response);
            }

            // Get cart items
            GioHang gioHang = gioHangRepository.findByNguoiDungId(user.getId()).orElse(null);
            if (gioHang == null) {
                response.put("success", false);
                response.put("message", "Giỏ hàng trống");
                return ResponseEntity.ok(response);
            }

            List<ChiTietGioHang> cartItems = chiTietGioHangRepository.findByGioHangId(gioHang.getId());
            if (cartItems.isEmpty()) {
                response.put("success", false);
                response.put("message", "Giỏ hàng trống");
                return ResponseEntity.ok(response);
            }

            // Create address
            DiaChi diaChi = new DiaChi();
            diaChi.setNguoiDung(user);
            diaChi.setTenNguoiNhan(fullname);
            diaChi.setSdtNguoiNhan(phone);
            diaChi.setDiaChi(address + ", " + province);
            diaChi.setMacDinh(false);
            diaChi = diaChiRepository.save(diaChi);

            // Calculate totals
            BigDecimal tamTinh = cartItems.stream()
                    .map(item -> item.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal phiGiaoHang = BigDecimal.ZERO;
            BigDecimal tienGiam = discount;
            BigDecimal tongTien = tamTinh.add(phiGiaoHang).subtract(tienGiam);

            String mappedPayment = "Tiền mặt";
            if ("bank".equals(payment)) {
                mappedPayment = "Chuyển khoản";
            } else if ("wallet".equals(payment)) {
                mappedPayment = "Ví điện tử";
            }

            // Create order
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(user);
            donHang.setDiaChi(diaChi);
            donHang.setGhiChu(note != null ? note : "");
            donHang.setTamTinh(tamTinh);
            donHang.setPhiGiaoHang(phiGiaoHang);
            donHang.setTienGiam(tienGiam);
            donHang.setTongTien(tongTien);
            donHang.setHinhThucThanhToan(mappedPayment);
            donHang.setTrangThai("Chờ xác nhận");
            donHang.setNgayTao(LocalDateTime.now());

            // Set voucher if applied
            if (voucherId != null) {
                MaGiamGia voucher = voucherService.layTheoId(voucherId);
                if (voucher != null) {
                    donHang.setMaGiamGia(voucher);
                    if (voucher.getSoLuong() > 0) {
                        voucher.setSoLuong(voucher.getSoLuong() - 1);
                        voucherService.capNhatMaGiamGia(voucher);
                    }
                }
            }

            donHang = donHangRepository.save(donHang);

            // Create order details
            for (ChiTietGioHang cartItem : cartItems) {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setMonAn(cartItem.getMonAn());
                chiTiet.setSoLuong(cartItem.getSoLuong());
                chiTiet.setDonGia(cartItem.getDonGia());
                chiTietDonHangRepository.save(chiTiet);
            }

            // Create payment record
            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setDonHang(donHang);
            thanhToan.setSoTien(tongTien);
            thanhToan.setPhuongThuc(mappedPayment);
            thanhToan.setTrangThai("Chờ thanh toán");
            thanhToan.setThoiGian(LocalDateTime.now());
            thanhToanRepository.save(thanhToan);

            // Clear cart
            chiTietGioHangRepository.deleteAll(cartItems);

            response.put("success", true);
            response.put("message", "Đặt hàng thành công");
            response.put("orderId", donHang.getId());
            response.put("redirectUrl", "/tai-khoan/lich-su-don-hang");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi khi đặt hàng: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
