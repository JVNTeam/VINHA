package com.example.vinha.controller.admin;

import com.example.vinha.entity.MaGiamGia;
import com.example.vinha.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/phieuGiamGia")
public class PhieuGiamGiaController {

    private static final DateTimeFormatter DATETIME_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final VoucherService voucherService;

    public PhieuGiamGiaController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping
    public String hienThiPhieuGiamGia(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "quantity", required = false) String quantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("vouchersPage", voucherService.timKiemPhanTrang(keyword, status, quantity, pageable));
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("selectedStatus", status != null ? status : "");
        model.addAttribute("selectedQuantity", quantity != null ? quantity : "");
        model.addAttribute("message", model.asMap().get("message"));
        model.addAttribute("error", model.asMap().get("error"));
        return "admin/phieuGiamGia";
    }

    @GetMapping("/them")
    public String showAddForm(Model model) {
        prepareFormModel(model, new MaGiamGia(), "Thêm mã giảm giá mới", "/admin/phieuGiamGia/them");
        return "admin/themPhieuGiamGia";
    }

    @PostMapping("/them")
    public String createVoucher(
            @RequestParam("ma") String ma,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam("loaiGiam") String loaiGiam,
            @RequestParam("giaTriGiam") BigDecimal giaTriGiam,
            @RequestParam(value = "giamToiDa", required = false) BigDecimal giamToiDa,
            @RequestParam(value = "donToiThieu", required = false) BigDecimal donToiThieu,
            @RequestParam("soLuong") Integer soLuong,
            @RequestParam(value = "gioiHanNhan", required = false) Integer gioiHanNhan,
            @RequestParam(value = "ngayBatDau", required = false) String ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) String ngayKetThuc,
            RedirectAttributes redirectAttributes
    ) {
        MaGiamGia voucher = buildVoucher(null, ma, moTa, loaiGiam, giaTriGiam, giamToiDa, donToiThieu,
                soLuong, gioiHanNhan, ngayBatDau, ngayKetThuc, true);

        try {
            String error = validateVoucher(voucher, null);
            if (error != null) {
                redirectAttributes.addFlashAttribute("error", error);
                redirectAttributes.addFlashAttribute("voucher", voucher);
                return "redirect:/admin/phieuGiamGia/them";
            }

            voucherService.taoMaGiamGia(voucher);
            redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá thành công.");
            return "redirect:/admin/phieuGiamGia";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu dữ liệu: " + e.getMessage());
            redirectAttributes.addFlashAttribute("voucher", voucher);
            return "redirect:/admin/phieuGiamGia/them";
        }
    }

    @GetMapping("/sua")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        MaGiamGia voucher = voucherService.layTheoId(id);
        if (voucher == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá cần sửa.");
            return "redirect:/admin/phieuGiamGia";
        }
        prepareFormModel(model, voucher, "Chỉnh sửa mã giảm giá", "/admin/phieuGiamGia/sua");
        return "admin/themPhieuGiamGia";
    }

    @PostMapping("/sua")
    public String updateVoucher(
            @RequestParam("id") Long id,
            @RequestParam("ma") String ma,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam("loaiGiam") String loaiGiam,
            @RequestParam("giaTriGiam") BigDecimal giaTriGiam,
            @RequestParam(value = "giamToiDa", required = false) BigDecimal giamToiDa,
            @RequestParam(value = "donToiThieu", required = false) BigDecimal donToiThieu,
            @RequestParam("soLuong") Integer soLuong,
            @RequestParam(value = "gioiHanNhan", required = false) Integer gioiHanNhan,
            @RequestParam(value = "ngayBatDau", required = false) String ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) String ngayKetThuc,
            RedirectAttributes redirectAttributes
    ) {
        MaGiamGia existing = voucherService.layTheoId(id);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá cần chỉnh sửa.");
            return "redirect:/admin/phieuGiamGia";
        }

        MaGiamGia voucher = buildVoucher(id, ma, moTa, loaiGiam, giaTriGiam, giamToiDa, donToiThieu,
                soLuong, gioiHanNhan, ngayBatDau, ngayKetThuc, existing.getTrangThai());

        try {
            String error = validateVoucher(voucher, id);
            if (error != null) {
                redirectAttributes.addFlashAttribute("error", error);
                redirectAttributes.addFlashAttribute("voucher", voucher);
                return "redirect:/admin/phieuGiamGia/sua?id=" + id;
            }

            voucherService.capNhatMaGiamGia(voucher);
            redirectAttributes.addFlashAttribute("message", "Cập nhật mã giảm giá thành công.");
            return "redirect:/admin/phieuGiamGia";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật dữ liệu: " + e.getMessage());
            redirectAttributes.addFlashAttribute("voucher", voucher);
            return "redirect:/admin/phieuGiamGia/sua?id=" + id;
        }
    }

    @GetMapping("/toggle")
    public String toggleStatus(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        MaGiamGia voucher = voucherService.layTheoId(id);
        if (voucher == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá.");
        } else {
            voucherService.chuyenTrangThai(id);
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái mã " + voucher.getMa() + ".");
        }
        return "redirect:/admin/phieuGiamGia";
    }

    @GetMapping("/xoa")
    public String deleteVoucher(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        MaGiamGia voucher = voucherService.layTheoId(id);
        if (voucher == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá cần xóa.");
        } else {
            voucherService.xoaMaGiamGia(id);
            redirectAttributes.addFlashAttribute("message", "Xóa mã giảm giá thành công.");
        }
        return "redirect:/admin/phieuGiamGia";
    }

    private void prepareFormModel(Model model, MaGiamGia voucher, String formTitle, String formAction) {
        if (!model.containsAttribute("voucher")) {
            model.addAttribute("voucher", voucher);
        }
        model.addAttribute("loaiGiamOptions", List.of("Phần trăm", "Tiền"));
        model.addAttribute("formTitle", formTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("ngayBatDauInput", formatDateTimeInput(voucher.getNgayBatDau()));
        model.addAttribute("ngayKetThucInput", formatDateTimeInput(voucher.getNgayKetThuc()));
    }

    private MaGiamGia buildVoucher(
            Long id,
            String ma,
            String moTa,
            String loaiGiam,
            BigDecimal giaTriGiam,
            BigDecimal giamToiDa,
            BigDecimal donToiThieu,
            Integer soLuong,
            Integer gioiHanNhan,
            String ngayBatDau,
            String ngayKetThuc,
            Boolean trangThai
    ) {
        return MaGiamGia.builder()
                .id(id)
                .ma(ma != null ? ma.trim().toUpperCase() : "")
                .moTa(moTa != null ? moTa.trim() : "")
                .loaiGiam(loaiGiam)
                .giaTriGiam(giaTriGiam)
                .giamToiDa(giamToiDa)
                .donToiThieu(donToiThieu != null ? donToiThieu : BigDecimal.ZERO)
                .soLuong(soLuong)
                .gioiHanNhan(gioiHanNhan != null ? gioiHanNhan : 1)
                .ngayBatDau(parseDateTimeInput(ngayBatDau))
                .ngayKetThuc(parseDateTimeInput(ngayKetThuc))
                .trangThai(trangThai)
                .build();
    }

    private String validateVoucher(MaGiamGia voucher, Long excludeId) {
        if (voucher.getMa() == null || voucher.getMa().isBlank()) {
            return "Mã giảm giá không được để trống.";
        }
        if (voucherService.maDaTonTai(voucher.getMa(), excludeId)) {
            return "Mã giảm giá đã tồn tại trên hệ thống.";
        }
        if (voucher.getGiaTriGiam() == null || voucher.getGiaTriGiam().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá trị giảm phải lớn hơn 0.";
        }
        if ("Phần trăm".equals(voucher.getLoaiGiam()) && voucher.getGiaTriGiam().compareTo(new BigDecimal("100")) > 0) {
            return "Phần trăm giảm không được vượt quá 100%.";
        }
        if (voucher.getSoLuong() == null || voucher.getSoLuong() < 0) {
            return "Số lượng phát hành không hợp lệ.";
        }
        if (voucher.getNgayBatDau() != null && voucher.getNgayKetThuc() != null
                && voucher.getNgayKetThuc().isBefore(voucher.getNgayBatDau())) {
            return "Ngày kết thúc phải sau ngày bắt đầu.";
        }
        return null;
    }

    private LocalDateTime parseDateTimeInput(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(value, DATETIME_INPUT);
    }

    private String formatDateTimeInput(LocalDateTime value) {
        return value != null ? value.format(DATETIME_INPUT) : "";
    }
}