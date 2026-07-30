package com.example.vinha.controller.admin;

import com.example.vinha.entity.DonHang;
import com.example.vinha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DonHangController {

    @Autowired
    private OrderService orderService;

        // Trang danh sách đơn hàng
    @GetMapping("/admin/donHang")
    public String hienThiDonHang(Model model) {
        List<DonHang> list = orderService.getAllOrders();
        model.addAttribute("donHangs", list);

        // Thống kê đơn hàng
        long choXacNhan = list.stream().filter(o -> "Chờ xác nhận".equalsIgnoreCase(o.getTrangThai())).count();
        long daXacNhan = list.stream().filter(o -> "Đã xác nhận".equalsIgnoreCase(o.getTrangThai())).count();
        long dangCheBien = list.stream().filter(o -> "Đang chế biến".equalsIgnoreCase(o.getTrangThai())).count();
        double doanhThu = list.stream()
                .filter(o -> "Đã hoàn thành".equalsIgnoreCase(o.getTrangThai()))
                .mapToDouble(o -> o.getTongTien() != null ? o.getTongTien().doubleValue() : 0)
                .sum();

        model.addAttribute("choXacNhan", choXacNhan);
        model.addAttribute("daXacNhan", daXacNhan);
        model.addAttribute("dangCheBien", dangCheBien);
        model.addAttribute("doanhThu", doanhThu);

        return "admin/donHang";
    }


    // Cập nhật trạng thái đơn hàng
    @PostMapping("/admin/donHang/updateStatus")
    public String updateStatus(@RequestParam Long id, @RequestParam String status) {
        orderService.updateStatus(id, status);
        return "redirect:/admin/donHang";
    }

    // Trang chi tiết đơn hàng
    @GetMapping("/admin/chiTietDon/{id}")
    public String hienThiChiTietDon(@PathVariable Long id, Model model) {
        DonHang donHang = orderService.getOrderById(id);
        if (donHang == null) {
            return "redirect:/admin/donHang";
        }
        model.addAttribute("donHang", donHang);
        return "admin/CTDonHang";
    }
}