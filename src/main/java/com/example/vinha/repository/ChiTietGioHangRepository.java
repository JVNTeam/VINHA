package com.example.vinha.repository;

import com.example.vinha.entity.ChiTietGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long> {
    List<ChiTietGioHang> findByGioHangId(Long gioHangId);
    Optional<ChiTietGioHang> findByGioHangIdAndMonAnId(Long gioHangId, Long monAnId);
}

