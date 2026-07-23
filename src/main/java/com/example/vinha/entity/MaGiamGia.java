package com.example.vinha.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ma_giam_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma", nullable = false, unique = true, length = 50)
    private String ma;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    @Column(name = "loai_giam", nullable = false, length = 20)
    private String loaiGiam;

    @Column(name = "gia_tri_giam", nullable = false, precision = 12, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "giam_toi_da", precision = 12, scale = 2)
    private BigDecimal giamToiDa;

    @Column(name = "don_toi_thieu", precision = 12, scale = 2)
    private BigDecimal donToiThieu;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "gioi_han_nhan")
    private Integer gioiHanNhan;

    @Column(name = "loai_dieu_kien", length = 50)
    private String loaiDieuKien;

    @Column(name = "gia_tri_dieu_kien", precision = 12, scale = 2)
    private BigDecimal giaTriDieuKien;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @PrePersist
    public void prePersist() {
        if (this.donToiThieu == null) {
            this.donToiThieu = BigDecimal.ZERO;
        }
        if (this.gioiHanNhan == null) {
            this.gioiHanNhan = 1;
        }
        if (this.trangThai == null) {
            this.trangThai = true;
        }
    }

    @OneToMany(mappedBy = "maGiamGia")
    private List<NguoiDungMaGiamGia> nguoiDungMaGiamGias;
}
