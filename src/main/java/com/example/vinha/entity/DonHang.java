package com.example.vinha.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "don_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "dia_chi_id")
    private DiaChi diaChi;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id")
    private MaGiamGia maGiamGia;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id")
    private NguoiDung nhanVien;

    @Column(name = "ghi_chu", columnDefinition = "nvarchar(max)")
    private String ghiChu;

    @Column(name = "tam_tinh", precision = 12, scale = 2)
    private BigDecimal tamTinh;

    @Column(name = "phi_giao_hang", precision = 12, scale = 2)
    private BigDecimal phiGiaoHang;

    @Column(name = "tien_giam", precision = 12, scale = 2)
    private BigDecimal tienGiam;

    @Column(name = "tong_tien", precision = 12, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "thoi_gian_du_kien")
    private LocalDateTime thoiGianDuKien;

    @Column(name = "hinh_thuc_thanh_toan", length = 30)
    private String hinhThucThanhToan;

    @Column(name = "trang_thai", length = 30)
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (this.ngayTao == null) {
            this.ngayTao = LocalDateTime.now();
        }
    }

    @OneToMany(mappedBy = "donHang")
    private List<ChiTietDonHang> chiTietDonHangs;

    @OneToMany(mappedBy = "donHang")
    private List<ThanhToan> thanhToans;

    @OneToMany(mappedBy = "donHang")
    private List<LichSuTrangThai> lichSuTrangThais;

    @OneToMany(mappedBy = "donHang")
    private List<DanhGia> danhGias;
}
