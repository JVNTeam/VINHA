package com.example.vinha.repository;

import com.example.vinha.entity.NguoiDungMaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungMaGiamGiaRepository extends JpaRepository<NguoiDungMaGiamGia, Long> {
    List<NguoiDungMaGiamGia> findByNguoiDungId(Long nguoiDungId);
    List<NguoiDungMaGiamGia> findByNguoiDungIdAndTrangThai(Long nguoiDungId, String trangThai);
}

