package com.example.vinha.repository;

import com.example.vinha.entity.HinhAnhMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhMonAnRepository extends JpaRepository<HinhAnhMonAn, Long> {
    List<HinhAnhMonAn> findByMonAnId(Long monAnId);

    @Query("select ha from HinhAnhMonAn ha " +
            "join ha.monAn ma " +
            "left join ma.danhMuc dm " +
            "where (:keyword is null or :keyword = '' or lower(ma.ten) like lower(concat('%', :keyword, '%'))) " +
            "and (:category is null or :category = '' or " +
            "     ( :category = 'Chưa phân loại' and dm is null ) or " +
            "     ( dm is not null and dm.ten = :category ) ) " +
            "and (:status is null or :status = '' or ha.trangThai = :status)")
    List<HinhAnhMonAn> search(@Param("keyword") String keyword,
                              @Param("category") String category,
                              @Param("status") String status);
}

