package com.example.vinha.service;

import com.example.vinha.entity.ChiTietGioHang;
import com.example.vinha.entity.GioHang;
import com.example.vinha.entity.MonAn;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietGioHangRepository;
import com.example.vinha.repository.GioHangRepository;
import com.example.vinha.repository.MonAnRepository;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {

    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final MonAnRepository monAnRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public CartService(
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            MonAnRepository monAnRepository,
            NguoiDungRepository nguoiDungRepository
    ) {
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.monAnRepository = monAnRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @Transactional
    public void addToCart(Long nguoiDungId, Long monAnId, Integer soLuong) {
        if (nguoiDungId == null || monAnId == null) {
            throw new IllegalArgumentException("Thiếu thông tin người dùng hoặc món ăn.");
        }

        int soLuongHopLe = (soLuong == null || soLuong < 1) ? 1 : soLuong;

        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));

        MonAn monAn = monAnRepository.findById(monAnId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn."));

        GioHang gioHang = gioHangRepository.findByNguoiDungId(nguoiDungId)
                .orElseGet(() -> gioHangRepository.save(
                        GioHang.builder()
                                .nguoiDung(nguoiDung)
                                .build()
                ));

        ChiTietGioHang chiTiet = chiTietGioHangRepository
                .findByGioHangIdAndMonAnId(gioHang.getId(), monAnId)
                .orElse(null);

        if (chiTiet != null) {
            int soLuongMoi = chiTiet.getSoLuong() + soLuongHopLe;
            chiTiet.setSoLuong(soLuongMoi);
        } else {
            BigDecimal donGia = monAn.getGia() != null ? monAn.getGia() : BigDecimal.ZERO;
            chiTiet = ChiTietGioHang.builder()
                    .gioHang(gioHang)
                    .monAn(monAn)
                    .soLuong(soLuongHopLe)
                    .donGia(donGia)
                    .build();
        }

        chiTietGioHangRepository.save(chiTiet);
    }

    @Transactional
    public void capNhatSoLuong(Long nguoiDungId, Long chiTietGioHangId, Integer soLuongMoi) {
        if (nguoiDungId == null || chiTietGioHangId == null) {
            throw new IllegalArgumentException("Thiếu thông tin cập nhật giỏ hàng.");
        }

        int soLuongHopLe = (soLuongMoi == null || soLuongMoi < 1) ? 1 : soLuongMoi;

        ChiTietGioHang chiTiet = chiTietGioHangRepository.findById(chiTietGioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết giỏ hàng."));

        if (chiTiet.getGioHang() == null
                || chiTiet.getGioHang().getNguoiDung() == null
                || !chiTiet.getGioHang().getNguoiDung().getId().equals(nguoiDungId)) {
            throw new IllegalArgumentException("Không có quyền cập nhật sản phẩm trong giỏ hàng này.");
        }

        chiTiet.setSoLuong(soLuongHopLe);

        chiTietGioHangRepository.save(chiTiet);
    }

    @Transactional
    public void xoaMonKhoiGio(Long nguoiDungId, Long chiTietGioHangId) {
        if (nguoiDungId == null || chiTietGioHangId == null) {
            throw new IllegalArgumentException("Thiếu thông tin xóa sản phẩm khỏi giỏ hàng.");
        }

        ChiTietGioHang chiTiet = chiTietGioHangRepository.findById(chiTietGioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết giỏ hàng."));

        if (chiTiet.getGioHang() == null
                || chiTiet.getGioHang().getNguoiDung() == null
                || !chiTiet.getGioHang().getNguoiDung().getId().equals(nguoiDungId)) {
            throw new IllegalArgumentException("Không có quyền xóa sản phẩm trong giỏ hàng này.");
        }

        chiTietGioHangRepository.delete(chiTiet);
    }
}
