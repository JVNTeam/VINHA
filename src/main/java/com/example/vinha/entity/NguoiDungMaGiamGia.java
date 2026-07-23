package com.example.vinha.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung_ma_giam_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDungMaGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id", nullable = false)
    private MaGiamGia maGiamGia;

    @Column(name = "ngay_nhan")
    private LocalDateTime ngayNhan;

    @Column(name = "trang_thai", length = 30)
    private String trangThai;

    @Column(name = "ngay_su_dung")
    private LocalDateTime ngaySuDung;

    @PrePersist
    public void prePersist() {
        if (this.ngayNhan == null) {
            this.ngayNhan = LocalDateTime.now();
        }
    }
}
