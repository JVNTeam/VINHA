package com.example.vinha.service;

import com.example.vinha.dto.MaGiamGiaView;
import com.example.vinha.entity.MaGiamGia;
import com.example.vinha.repository.MaGiamGiaRepository;
import com.example.vinha.repository.NguoiDungMaGiamGiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    private final MaGiamGiaRepository maGiamGiaRepository;
    private final NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository;

    public VoucherService(
            MaGiamGiaRepository maGiamGiaRepository,
            NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository
    ) {
        this.maGiamGiaRepository = maGiamGiaRepository;
        this.nguoiDungMaGiamGiaRepository = nguoiDungMaGiamGiaRepository;
    }

    public List<MaGiamGiaView> timKiem(String keyword, String status, String quantity) {
        String kw = keyword != null ? keyword.trim().toLowerCase() : "";
        String st = status != null ? status.trim() : "";
        String qty = quantity != null ? quantity.trim() : "";

        return maGiamGiaRepository.findAll().stream()
                .filter(v -> filterByKeyword(v, kw))
                .filter(v -> filterByStatus(v, st))
                .filter(v -> filterByQuantity(v, qty))
                .map(this::toView)
                .collect(Collectors.toList());
    }

    public List<MaGiamGia> layMaDangHoatDong() {
        LocalDateTime now = LocalDateTime.now();
        return maGiamGiaRepository.findAll().stream()
                .filter(v -> Boolean.TRUE.equals(v.getTrangThai()))
                .filter(v -> v.getSoLuong() != null && v.getSoLuong() > 0)
                .filter(v -> v.getNgayBatDau() == null || !v.getNgayBatDau().isAfter(now))
                .filter(v -> v.getNgayKetThuc() == null || !v.getNgayKetThuc().isBefore(now))
                .collect(Collectors.toList());
    }

    public MaGiamGia layTheoId(Long id) {
        return maGiamGiaRepository.findById(id).orElse(null);
    }

    public MaGiamGia layTheoMa(String ma) {
        if (ma == null || ma.trim().isEmpty()) {
            return null;
        }
        return maGiamGiaRepository.findByMa(ma.toUpperCase().trim()).orElse(null);
    }

    @Transactional
    public MaGiamGia taoMaGiamGia(MaGiamGia voucher) {
        return maGiamGiaRepository.save(voucher);
    }

    @Transactional
    public MaGiamGia capNhatMaGiamGia(MaGiamGia voucher) {
        return maGiamGiaRepository.save(voucher);
    }

    @Transactional
    public void xoaMaGiamGia(Long id) {
        maGiamGiaRepository.deleteById(id);
    }

    @Transactional
    public void chuyenTrangThai(Long id) {
        MaGiamGia voucher = maGiamGiaRepository.findById(id).orElse(null);
        if (voucher != null) {
            voucher.setTrangThai(!Boolean.TRUE.equals(voucher.getTrangThai()));
            maGiamGiaRepository.save(voucher);
        }
    }

    public boolean maDaTonTai(String ma, Long excludeId) {
        return maGiamGiaRepository.findByMa(ma)
                .map(existing -> excludeId == null || !existing.getId().equals(excludeId))
                .orElse(false);
    }

    public long demDaNhan(Long maGiamGiaId) {
        return nguoiDungMaGiamGiaRepository.countByMaGiamGiaId(maGiamGiaId);
    }

    private MaGiamGiaView toView(MaGiamGia voucher) {
        return new MaGiamGiaView(voucher, demDaNhan(voucher.getId()));
    }

    private boolean filterByKeyword(MaGiamGia voucher, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        return (voucher.getMa() != null && voucher.getMa().toLowerCase().contains(keyword))
                || (voucher.getMoTa() != null && voucher.getMoTa().toLowerCase().contains(keyword));
    }

    private boolean filterByStatus(MaGiamGia voucher, String status) {
        if (status.isBlank()) {
            return true;
        }
        boolean active = Boolean.TRUE.equals(voucher.getTrangThai());
        return "ACTIVE".equalsIgnoreCase(status) ? active : !active;
    }

    private boolean filterByQuantity(MaGiamGia voucher, String quantity) {
        if (quantity.isBlank() || voucher.getSoLuong() == null) {
            return true;
        }
        int soLuong = voucher.getSoLuong();
        return switch (quantity) {
            case "under_50" -> soLuong < 50;
            case "50_200" -> soLuong >= 50 && soLuong <= 200;
            case "over_200" -> soLuong > 200;
            default -> true;
        };
    }

    public String formatGiaTriGiam(MaGiamGia voucher) {
        if (voucher == null || voucher.getGiaTriGiam() == null) {
            return "0đ";
        }
        if ("Phần trăm".equals(voucher.getLoaiGiam())) {
            return voucher.getGiaTriGiam().stripTrailingZeros().toPlainString() + "%";
        }
        return formatCurrency(voucher.getGiaTriGiam());
    }

    public String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0đ";
        }
        return amount.stripTrailingZeros().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ";
    }
}
