package com.example.vinha.repository;

import com.example.vinha.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    List<DanhGia> findByMonAnId(Long monAnId);
    List<DanhGia> findByNguoiDungId(Long nguoiDungId);
    List<DanhGia> findByDonHangId(Long donHangId);
}

