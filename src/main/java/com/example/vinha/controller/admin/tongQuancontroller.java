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

    @Autowired
    private com.example.vinha.repository.ChiTietDonHangRepository chiTietDonHangRepository;

    @GetMapping("/admin/tongQuan")
    public String hienThiTrangThongKe(
            @org.springframework.web.bind.annotation.RequestParam(value = "startDate", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDateReq,
            @org.springframework.web.bind.annotation.RequestParam(value = "endDate", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDateReq,
            Model model) {

        java.time.LocalDateTime startDate = startDateReq != null ? startDateReq.atStartOfDay() : null;
        java.time.LocalDateTime endDate = endDateReq != null ? endDateReq.atTime(23, 59, 59) : null;
        // 1. Tổng doanh thu (Đơn hàng Hoàn thành)
        Double tongDoanhThu = donHangRepository.sumDoanhThuByDateRange(startDate, endDate);
        if (tongDoanhThu == null) tongDoanhThu = 0.0;

        // 2. Tổng số đơn hàng (Tất cả)
        long tongDonHang = donHangRepository.countByDateRange(startDate, endDate);

        // 3. Số lượng khách hàng (Vai trò = 1)
        long soKhachHang = nguoiDungRepository.countByVaiTroIdAndDateRange(1L, startDate, endDate);

        // 4. Số món ăn sắp hết (soLuongCon < 10) - Không phụ thuộc ngày
        long soMonSapHet = monAnRepository.findAll().stream().filter(m -> m.getSoLuongCon() != null && m.getSoLuongCon() < 10).count();

        // 5. 5 đơn hàng mới nhất
        List<com.example.vinha.entity.DonHang> donHangMoiNhat = donHangRepository.findDonHangMoiNhatByDateRange(startDate, endDate).stream().limit(5).collect(Collectors.toList());

        // 6. Top món bán chạy & bán chậm (dựa trên ChiTietDonHang)
        List<Object[]> topItemsData = chiTietDonHangRepository.getTopMonAnByDateRange(startDate, endDate);
        List<com.example.vinha.entity.MonAn> sortedMonAn = new java.util.ArrayList<>();
        for (Object[] row : topItemsData) {
            com.example.vinha.entity.MonAn m = (com.example.vinha.entity.MonAn) row[0];
            m.setDaBan(((Number) row[1]).intValue());
            sortedMonAn.add(m);
        }

        // Bổ sung các món chưa bán được (nếu cần, tạm thời chỉ lấy các món có doanh thu)
        // Lấy 3 món đầu tiên (Bán chạy)
        List<com.example.vinha.entity.MonAn> topBanChay = sortedMonAn.stream().limit(3).collect(Collectors.toList());
        
        // Lấy 3 món cuối cùng (Bán chậm)
        List<com.example.vinha.entity.MonAn> topBanCham = new java.util.ArrayList<>();
        if (sortedMonAn.size() > 3) {
            int size = sortedMonAn.size();
            topBanCham = sortedMonAn.subList(Math.max(3, size - 3), size);
            java.util.Collections.reverse(topBanCham); // Lật lại để hiển thị từ thấp nhất
        } else {
            // Nếu ít hơn 3 món, bán chậm lấy các món còn lại hoặc rỗng
            topBanCham = new java.util.ArrayList<>();
        }

        // 7. Biểu đồ doanh thu (12 tháng của năm nay và năm trước)
        int currentYear = java.time.LocalDate.now().getYear();
        List<Object[]> revenueThisYear = donHangRepository.getMonthlyRevenueByYear(currentYear);
        List<Object[]> revenueLastYear = donHangRepository.getMonthlyRevenueByYear(currentYear - 1);

        Double[] dataThisYear = new Double[12];
        Double[] dataLastYear = new Double[12];
        java.util.Arrays.fill(dataThisYear, 0.0);
        java.util.Arrays.fill(dataLastYear, 0.0);

        for (Object[] row : revenueThisYear) {
            int month = ((Number) row[0]).intValue();
            double sum = ((Number) row[1]).doubleValue();
            dataThisYear[month - 1] = sum;
        }

        for (Object[] row : revenueLastYear) {
            int month = ((Number) row[0]).intValue();
            double sum = ((Number) row[1]).doubleValue();
            dataLastYear[month - 1] = sum;
        }

        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("tongDonHang", tongDonHang);
        model.addAttribute("soKhachHang", soKhachHang);
        model.addAttribute("soMonSapHet", soMonSapHet);
        model.addAttribute("donHangMoiNhat", donHangMoiNhat);
        model.addAttribute("topBanChay", topBanChay);
        model.addAttribute("topBanCham", topBanCham);
        
        List<Double> listThisYear = java.util.Arrays.asList(dataThisYear);
        List<Double> listLastYear = java.util.Arrays.asList(dataLastYear);
        double maxThis = java.util.Collections.max(listThisYear);
        double maxLast = java.util.Collections.max(listLastYear);
        double maxRevenue = Math.max(maxThis, maxLast);
        if (maxRevenue == 0) maxRevenue = 1000000.0; // Avoid division by zero, default to 1M

        model.addAttribute("chartDataThisYear", listThisYear);
        model.addAttribute("chartDataLastYear", listLastYear);
        model.addAttribute("chartMaxRevenue", maxRevenue);

        model.addAttribute("startDateVal", startDateReq != null ? startDateReq.toString() : "");
        model.addAttribute("endDateVal", endDateReq != null ? endDateReq.toString() : "");

        String dateNote = "";
        if (startDateReq != null && endDateReq != null) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateNote = "(" + startDateReq.format(formatter) + " - " + endDateReq.format(formatter) + ")";
        } else {
            dateNote = "(Tất cả thời gian)";
        }
        model.addAttribute("dateNote", dateNote);

        return "admin/tongQuan";
    }
}