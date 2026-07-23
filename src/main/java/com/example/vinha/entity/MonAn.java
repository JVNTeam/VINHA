package com.example.vinha.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mon_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "danh_muc_id")
    private DanhMuc danhMuc;

    @Column(name = "ten", length = 200)
    private String ten;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(max)")
    private String moTa;

    @Column(name = "thanh_phan", columnDefinition = "nvarchar(max)")
    private String thanhPhan;

    @Column(name = "gia", precision = 12, scale = 2)
    private BigDecimal gia;

    @Column(name = "so_luong_con")
    private Integer soLuongCon;

    @Column(name = "da_ban")
    private Integer daBan;

    @Column(name = "danh_gia", precision = 3, scale = 2)
    private BigDecimal danhGia;

    @Column(name = "so_luot_danh_gia")
    private Integer soLuotDanhGia;

    @Column(name = "trang_thai", length = 30)
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (this.ngayTao == null) {
            this.ngayTao = LocalDateTime.now();
        }
        if (this.soLuongCon == null) {
            this.soLuongCon = 0;
        }
        if (this.daBan == null) {
            this.daBan = 0;
        }
        if (this.danhGia == null) {
            this.danhGia = BigDecimal.ZERO;
        }
        if (this.soLuotDanhGia == null) {
            this.soLuotDanhGia = 0;
        }
    }

    @OneToMany(mappedBy = "monAn")
    private List<HinhAnhMonAn> hinhAnhs;
}
