package com.example.vinha.repository;

import com.example.vinha.entity.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {
    List<ChiTietDonHang> findByDonHangId(Long donHangId);

    @org.springframework.data.jpa.repository.Query("SELECT c.monAn, SUM(c.soLuong) as totalSold FROM ChiTietDonHang c JOIN c.donHang d WHERE d.trangThai = 'Hoàn thành' AND (CAST(:startDate AS date) IS NULL OR d.ngayTao >= :startDate) AND (CAST(:endDate AS date) IS NULL OR d.ngayTao <= :endDate) GROUP BY c.monAn ORDER BY totalSold DESC")
    List<Object[]> getTopMonAnByDateRange(@org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate, @org.springframework.data.repository.query.Param("endDate") java.time.LocalDateTime endDate);
}

