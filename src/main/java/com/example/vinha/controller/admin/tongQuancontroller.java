package com.example.vinha.controller.admin;

import com.example.vinha.repository.DonHangRepository;
import com.example.vinha.repository.MonAnRepository;
import com.example.vinha.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class tongQuancontroller {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private MonAnRepository monAnRepository;

    @GetMapping("/admin/tongQuan")
    public String hienThiTrangThongKe(Model model) {
        // 1. Tổng doanh thu (Đơn hàng Hoàn thành)
        Double tongDoanhThu = donHangRepository.sumDoanhThu();
        if (tongDoanhThu == null) tongDoanhThu = 0.0;

        // 2. Tổng số đơn hàng (Tất cả)
        long tongDonHang = donHangRepository.count();

        // 3. Số lượng khách hàng (Vai trò = 1)
        long soKhachHang = nguoiDungRepository.countByVaiTroId(1L);

        // 4. Số món ăn sắp hết (soLuongCon < 10)
        long soMonSapHet = monAnRepository.findAll().stream().filter(m -> m.getSoLuongCon() != null && m.getSoLuongCon() < 10).count();

        // 5. 5 đơn hàng mới nhất
        List<com.example.vinha.entity.DonHang> donHangMoiNhat = donHangRepository.findAllByOrderByNgayTaoDesc().stream().limit(5).collect(Collectors.toList());

        // 6. Top món bán chạy & bán chậm
        List<com.example.vinha.entity.MonAn> tatCaMon = monAnRepository.findAll();
        List<com.example.vinha.entity.MonAn> topBanChay = tatCaMon.stream()
                .sorted((a, b) -> Integer.compare(b.getDaBan() != null ? b.getDaBan() : 0, a.getDaBan() != null ? a.getDaBan() : 0))
                .limit(3).collect(Collectors.toList());
        List<com.example.vinha.entity.MonAn> topBanCham = tatCaMon.stream()
                .sorted((a, b) -> Integer.compare(a.getDaBan() != null ? a.getDaBan() : 0, b.getDaBan() != null ? b.getDaBan() : 0))
                .limit(3).collect(Collectors.toList());

        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("tongDonHang", tongDonHang);
        model.addAttribute("soKhachHang", soKhachHang);
        model.addAttribute("soMonSapHet", soMonSapHet);
        model.addAttribute("donHangMoiNhat", donHangMoiNhat);
        model.addAttribute("topBanChay", topBanChay);
        model.addAttribute("topBanCham", topBanCham);

        return "admin/tongQuan";
    }
}