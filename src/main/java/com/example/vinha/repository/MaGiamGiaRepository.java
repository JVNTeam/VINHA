package com.example.vinha.repository;

import com.example.vinha.entity.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Long> {
    Optional<MaGiamGia> findByMa(String ma);

    @Query("SELECT m FROM MaGiamGia m WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(m.ma) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(m.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:trangThai IS NULL OR m.trangThai = :trangThai) " +
           "AND (:quantityFilter IS NULL OR :quantityFilter = '' " +
           "  OR (:quantityFilter = 'under_50' AND m.soLuong < 50) " +
           "  OR (:quantityFilter = '50_200' AND m.soLuong >= 50 AND m.soLuong <= 200) " +
           "  OR (:quantityFilter = 'over_200' AND m.soLuong > 200))")
    Page<MaGiamGia> searchVouchers(
            @Param("keyword") String keyword,
            @Param("trangThai") Boolean trangThai,
            @Param("quantityFilter") String quantityFilter,
            Pageable pageable
    );
}

