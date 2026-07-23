package com.example.vinha.repository;

import com.example.vinha.entity.HinhAnhMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhMonAnRepository extends JpaRepository<HinhAnhMonAn, Long> {
    List<HinhAnhMonAn> findByMonAnId(Long monAnId);
}

