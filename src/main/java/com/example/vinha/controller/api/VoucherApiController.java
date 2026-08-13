package com.example.vinha.controller.api;

import com.example.vinha.entity.*;
import com.example.vinha.repository.*;
import com.example.vinha.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/voucher")
public class VoucherApiController {

    private final VoucherService voucherService;
    private final NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository;

    public VoucherApiController(
            VoucherService voucherService,
            NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository
    ) {
        this.voucherService = voucherService;
        this.nguoiDungMaGiamGiaRepository = nguoiDungMaGiamGiaRepository;
    }

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyVoucher(
            @RequestParam("code") String code,
            @RequestParam("subtotal") BigDecimal subtotal,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để sử dụng mã giảm giá");
            return ResponseEntity.ok(response);
        }

        if (code == null || code.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng nhập mã giảm giá");
            return ResponseEntity.ok(response);
        }

        MaGiamGia voucher = voucherService.layTheoMa(code.toUpperCase().trim());
        
        if (voucher == null) {
            response.put("success", false);
            response.put("message", "Mã giảm giá không tồn tại");
            return ResponseEntity.ok(response);
        }

        if (!Boolean.TRUE.equals(voucher.getTrangThai())) {
            response.put("success", false);
            response.put("message", "Mã giảm giá không còn hoạt động");
            return ResponseEntity.ok(response);
        }

        if (voucher.getSoLuong() != null && voucher.getSoLuong() <= 0) {
            response.put("success", false);
            response.put("message", "Mã giảm giá đã hết lượt sử dụng");
            return ResponseEntity.ok(response);
        }

        if (voucher.getDonToiThieu() != null && subtotal.compareTo(voucher.getDonToiThieu()) < 0) {
            response.put("success", false);
            response.put("message", "Đơn hàng phải tối thiểu " + VoucherService.formatCurrency(voucher.getDonToiThieu()));
            return ResponseEntity.ok(response);
        }

        BigDecimal discountAmount = calculateDiscount(voucher, subtotal);

        response.put("success", true);
        response.put("message", "Áp dụng mã " + voucher.getMa() + " thành công");
        response.put("discountAmount", discountAmount.intValue());
        response.put("discountType", voucher.getLoaiGiam());
        response.put("voucherId", voucher.getId());
        
        return ResponseEntity.ok(response);
    }

    private BigDecimal calculateDiscount(MaGiamGia voucher, BigDecimal subtotal) {
        BigDecimal discountAmount;
        
        if ("Phần trăm".equals(voucher.getLoaiGiam())) {
            discountAmount = subtotal.multiply(voucher.getGiaTriGiam()).divide(new BigDecimal("100"), 0, java.math.RoundingMode.HALF_UP);
        } else {
            discountAmount = voucher.getGiaTriGiam();
        }

        if (voucher.getGiamToiDa() != null && discountAmount.compareTo(voucher.getGiamToiDa()) > 0) {
            discountAmount = voucher.getGiamToiDa();
        }

        return discountAmount;
    }

    @GetMapping("/active-hash")
    public ResponseEntity<Map<String, String>> getActiveVouchersHash() {
        List<MaGiamGia> activeVouchers = voucherService.layMaDangHoatDong();
        StringBuilder sb = new StringBuilder();
        for (MaGiamGia v : activeVouchers) {
            sb.append(v.getId()).append("-").append(v.getTrangThai()).append("|");
        }
        String hash = Integer.toHexString(sb.toString().hashCode());
        return ResponseEntity.ok(Map.of("hash", hash));
    }
}
