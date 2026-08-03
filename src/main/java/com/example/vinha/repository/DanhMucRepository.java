package com.example.vinha.repository;

import com.example.vinha.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    List<DanhMuc> findByTrangThai(String trangThai);
    java.util.Optional<DanhMuc> findByTen(String ten);

    @Query("select distinct dm from DanhMuc dm left join fetch dm.monAns ma left join fetch ma.hinhAnhs")
    List<DanhMuc> findAllWithMonAnsAndImages();
}

