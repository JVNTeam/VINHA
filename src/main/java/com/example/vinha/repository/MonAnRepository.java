package com.example.vinha.repository;

import com.example.vinha.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    List<MonAn> findByDanhMucId(Long danhMucId);
    List<MonAn> findByTrangThai(String trangThai);
    List<MonAn> findByTenContainingIgnoreCase(String ten);
}

