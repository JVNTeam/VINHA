package com.example.vinha.repository;

import com.example.vinha.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByEmail(String email);
    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);
    Optional<NguoiDung> findByEmailOrSoDienThoai(String email, String soDienThoai);
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);
    long countByVaiTroId(Long vaiTroId);
    java.util.List<NguoiDung> findByVaiTroId(Long vaiTroId);
    org.springframework.data.domain.Page<NguoiDung> findByVaiTroId(Long vaiTroId, org.springframework.data.domain.Pageable pageable);
    
    @org.springframework.data.jpa.repository.Query("SELECT n FROM NguoiDung n WHERE n.vaiTro.id = :vaiTroId AND " +
            "(:keyword IS NULL OR :keyword = '' OR n.hoTen LIKE %:keyword% OR n.email LIKE %:keyword% OR n.soDienThoai LIKE %:keyword%)")
    org.springframework.data.domain.Page<NguoiDung> searchByVaiTroIdAndKeyword(@org.springframework.data.repository.query.Param("vaiTroId") Long vaiTroId, @org.springframework.data.repository.query.Param("keyword") String keyword, org.springframework.data.domain.Pageable pageable);

    java.util.List<NguoiDung> findByVaiTroIdIn(java.util.List<Long> vaiTroIds);

    @org.springframework.data.jpa.repository.Query("SELECT n FROM NguoiDung n WHERE n.vaiTro.id IN :vaiTroIds AND " +
            "(:keyword IS NULL OR :keyword = '' OR n.hoTen LIKE %:keyword% OR n.email LIKE %:keyword% OR n.soDienThoai LIKE %:keyword%) AND " +
            "(:status IS NULL OR :status = '' OR n.trangThai = :status)")
    java.util.List<NguoiDung> searchByVaiTroIdsAndKeywordAndStatus(
            @org.springframework.data.repository.query.Param("vaiTroIds") java.util.List<Long> vaiTroIds,
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("status") String status
    );
}