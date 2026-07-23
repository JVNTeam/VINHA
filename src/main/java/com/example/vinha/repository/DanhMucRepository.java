package com.example.vinha.repository;

import com.example.vinha.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    List<DanhMuc> findByTrangThai(String trangThai);
}

