package com.example.vinha.repository;

import com.example.vinha.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findAllByOrderByNgayTaoDesc();
    List<DonHang> findByNguoiDungIdOrderByNgayTaoDesc(Long nguoiDungId);
    List<DonHang> findByTrangThai(String trangThai);
    List<DonHang> findByNguoiDungIdAndTrangThai(Long nguoiDungId, String trangThai);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM DonHang d LEFT JOIN d.nguoiDung u LEFT JOIN d.diaChi c WHERE " +
            "(:status IS NULL OR :status = '' OR d.trangThai = :status) AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "CAST(d.id AS string) LIKE %:keyword% OR " +
            "u.hoTen LIKE %:keyword% OR " +
            "u.soDienThoai LIKE %:keyword% OR " +
            "c.tenNguoiNhan LIKE %:keyword% OR " +
            "c.sdtNguoiNhan LIKE %:keyword%)")
    org.springframework.data.domain.Page<DonHang> searchOrders(@org.springframework.data.repository.query.Param("keyword") String keyword, @org.springframework.data.repository.query.Param("status") String status, org.springframework.data.domain.Pageable pageable);

    long countByTrangThai(String trangThai);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.trangThai = 'Hoàn thành'")
    Double sumDoanhThu();
}

