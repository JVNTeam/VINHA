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
import java.util.ArrayList;
import java.util.Collections;
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
    private final MonAnRepository monAnRepository;

    public CheckoutApiController(
            DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            DiaChiRepository diaChiRepository,
            VoucherService voucherService,
            ThanhToanRepository thanhToanRepository,
            MonAnRepository monAnRepository
    ) {
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.diaChiRepository = diaChiRepository;
        this.voucherService = voucherService;
        this.thanhToanRepository = thanhToanRepository;
        this.monAnRepository = monAnRepository;
    }

    @PostMapping("/place-order")
    @Transactional
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
            @RequestParam(value = "itemIds", required = false) String itemIdsStr,
            @RequestParam(value = "buyNowId", required = false) Long buyNowId,
            @RequestParam(value = "buyNowQty", defaultValue = "1") Integer buyNowQty,
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

            // Get cart items based on selection
            List<ChiTietGioHang> cartItems = new ArrayList<>();
            List<Long> itemIdsToClear = new ArrayList<>();

            if (buyNowId != null) {
                // Mua ngay 1 sản phẩm
                MonAn monAn = monAnRepository.findById(buyNowId).orElse(null);
                if (monAn != null) {
                    ChiTietGioHang fakeItem = new ChiTietGioHang();
                    fakeItem.setMonAn(monAn);
                    fakeItem.setSoLuong(buyNowQty);
                    fakeItem.setDonGia(monAn.getGia());
                    cartItems.add(fakeItem);
                }
            } else {
                // Mua từ giỏ hàng
                GioHang gioHang = gioHangRepository.findByNguoiDungId(user.getId()).orElse(null);
                if (gioHang != null) {
                    List<ChiTietGioHang> fullCart = chiTietGioHangRepository.findByGioHangId(gioHang.getId());
                    if (itemIdsStr != null && !itemIdsStr.trim().isEmpty()) {
                        String[] ids = itemIdsStr.split(",");
                        for (String idStr : ids) {
                            try {
                                itemIdsToClear.add(Long.parseLong(idStr.trim()));
                            } catch (NumberFormatException ignored) {}
                        }
                        cartItems = fullCart.stream()
                                .filter(item -> itemIdsToClear.contains(item.getId()))
                                .toList();
                    }
                }
            }

            if (cartItems.isEmpty()) {
                response.put("success", false);
                response.put("message", "Không có sản phẩm nào được chọn để thanh toán");
                return ResponseEntity.ok(response);
            }

            String fullAddress = address + ", " + province;
            
            // Check if address already exists for this user
            List<DiaChi> userAddresses = diaChiRepository.findByNguoiDungId(user.getId());
            DiaChi diaChi = null;
            
            if (userAddresses != null) {
                for (DiaChi addr : userAddresses) {
                    if (Objects.equals(addr.getTenNguoiNhan(), fullname) &&
                        Objects.equals(addr.getSdtNguoiNhan(), phone) &&
                        Objects.equals(addr.getDiaChi(), fullAddress)) {
                        diaChi = addr;
                        break;
                    }
                }
            }
            
            // Create new address if not found
            if (diaChi == null) {
                diaChi = new DiaChi();
                diaChi.setNguoiDung(user);
                diaChi.setTenNguoiNhan(fullname);
                diaChi.setSdtNguoiNhan(phone);
                diaChi.setDiaChi(fullAddress);
                // Set default if it's the first address
                diaChi.setMacDinh(userAddresses == null || userAddresses.isEmpty());
                diaChi = diaChiRepository.save(diaChi);
            }

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
                    if (!Boolean.TRUE.equals(voucher.getTrangThai())) {
                        response.put("success", false);
                        response.put("message", "Mã giảm giá không còn hoạt động.");
                        return ResponseEntity.ok(response);
                    }
                    if (voucher.getSoLuong() != null && voucher.getSoLuong() <= 0) {
                        response.put("success", false);
                        response.put("message", "Mã giảm giá đã hết lượt sử dụng.");
                        return ResponseEntity.ok(response);
                    }
                    donHang.setMaGiamGia(voucher);
                    if (voucher.getSoLuong() != null && voucher.getSoLuong() > 0) {
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

            // Clear cart if not buy-now
            if (buyNowId == null && !itemIdsToClear.isEmpty()) {
                // Delete only the checked out items
                List<ChiTietGioHang> itemsToDelete = chiTietGioHangRepository.findAllById(itemIdsToClear);
                chiTietGioHangRepository.deleteAll(itemsToDelete);
            }

            response.put("success", true);
            response.put("message", "Đặt hàng thành công");
            response.put("orderId", donHang.getId());
            response.put("redirectUrl", "/lichsudonhang");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi khi đặt hàng: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
