package com.example.vinha.repository;

import com.example.vinha.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    List<MonAn> findByDanhMucId(Long danhMucId);
    List<MonAn> findByTrangThai(String trangThai);
    List<MonAn> findByTenContainingIgnoreCase(String ten);
    Optional<MonAn> findByTen(String ten);

    @Query("select distinct m from MonAn m left join fetch m.hinhAnhs")
    List<MonAn> findAllWithHinhAnh();

    @Query("select distinct m from MonAn m left join fetch m.hinhAnhs where m.trangThai = :trangThai")
    List<MonAn> findByTrangThaiWithHinhAnh(String trangThai);

    @Query("select m from MonAn m left join fetch m.hinhAnhs where m.id = :id")
    Optional<MonAn> findByIdWithHinhAnh(Long id);

//    @Query("""
//            select distinct m
//            from MonAn m
//            left join fetch m.hinhAnhs ha
//            left join m.danhMuc dm
//            where (:keyword is null or :keyword = '' or lower(m.ten) like lower(concat('%', :keyword, '%')))
//              and (:trangThai is null or :trangThai = '' or m.trangThai = :trangThai)
//              and (:danhMucId is null or dm.id = :danhMucId)
//              and (:giaTu is null or m.gia >= :giaTu)
//              and (:giaDen is null or m.gia <= :giaDen)
//            order by
//              case when :sapXepBan = 'BAN_NHIEU_NHAT' then m.daBan end desc,
//              case when :sapXepBan = 'BAN_IT_NHAT' then m.daBan end asc
//            """)
        @Query("""
        select distinct m
        from MonAn m
        left join fetch m.hinhAnhs ha
        left join m.danhMuc dm
        where (:keyword is null or :keyword = '' or lower(m.ten) like lower(concat('%', :keyword, '%')))
          and (:trangThai is null or :trangThai = '' or m.trangThai = :trangThai)
          and (:danhMucId is null or dm.id = :danhMucId)
          and (:giaTu is null or m.gia >= :giaTu)
          and (:giaDen is null or m.gia <= :giaDen)
        """)
    List<MonAn> searchWithFilters(String keyword, String trangThai, Long danhMucId, java.math.BigDecimal giaTu, java.math.BigDecimal giaDen, String sapXepBan);
}

