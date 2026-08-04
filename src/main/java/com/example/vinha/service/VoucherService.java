package com.example.vinha.service;

import com.example.vinha.dto.MaGiamGiaView;
import com.example.vinha.entity.MaGiamGia;
import com.example.vinha.repository.MaGiamGiaRepository;
import com.example.vinha.repository.NguoiDungMaGiamGiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<MaGiamGiaView> timKiemPhanTrang(String keyword, String status, String quantity, Pageable pageable) {
        String kw = keyword != null ? keyword.trim() : "";
        Boolean trangThai = null;
        if ("ACTIVE".equalsIgnoreCase(status)) {
            trangThai = true;
        } else if ("INACTIVE".equalsIgnoreCase(status)) {
            trangThai = false;
        }

        String qtyFilter = quantity != null ? quantity.trim() : "";
        
        Page<MaGiamGia> resultPage = maGiamGiaRepository.searchVouchers(kw, trangThai, qtyFilter, pageable);
        return resultPage.map(this::toView);
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
            voucher.setTrangThai(voucher.getTrangThai() == null || !voucher.getTrangThai());
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

    public static String formatGiaTriGiam(MaGiamGia voucher) {
        if (voucher == null || voucher.getGiaTriGiam() == null) {
            return "0đ";
        }
        if ("Phần trăm".equals(voucher.getLoaiGiam())) {
            return voucher.getGiaTriGiam().stripTrailingZeros().toPlainString() + "%";
        }
        return formatCurrency(voucher.getGiaTriGiam());
    }

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0đ";
        }
        return amount.stripTrailingZeros().toPlainString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".") + "đ";
    }
}
