package com.example.vinha.repository;

import com.example.vinha.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Long> {
    @Query("SELECT d FROM DiaChi d WHERE d.nguoiDung.id = :nguoiDungId AND (d.daXoa IS NULL OR d.daXoa = false)")
    List<DiaChi> findByNguoiDungId(@Param("nguoiDungId") Long nguoiDungId);
}
