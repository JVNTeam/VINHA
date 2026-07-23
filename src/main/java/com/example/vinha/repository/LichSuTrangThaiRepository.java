package com.example.vinha.repository;

import com.example.vinha.entity.LichSuTrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuTrangThaiRepository extends JpaRepository<LichSuTrangThai, Long> {
    List<LichSuTrangThai> findByDonHangIdOrderByThoiGianAsc(Long donHangId);
}

