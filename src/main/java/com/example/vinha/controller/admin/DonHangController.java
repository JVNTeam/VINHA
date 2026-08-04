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

        List<DonHang> allOrders = orderService.getAllOrders();
        model.addAttribute("donHangs", allOrders);

        // Thống kê đơn hàng (dùng repository để đếm)
        long choXacNhan = orderService.countByStatus("Chờ xác nhận");
        long daXacNhan = orderService.countByStatus("Xác nhận");
        long daHuy = orderService.countByStatus("Hủy");
        Double doanhThu = orderService.sumDoanhThu();

        model.addAttribute("choXacNhan", choXacNhan);
        model.addAttribute("daXacNhan", daXacNhan);
        model.addAttribute("daHuy", daHuy);
        model.addAttribute("doanhThu", doanhThu != null ? doanhThu : 0.0);

        return "admin/donHang";
    }


    // Cập nhật trạng thái đơn hàng
    @PostMapping("/admin/donHang/updateStatus")
    public String updateStatus(
            @RequestParam Long id, 
            @RequestParam String status, 
            @RequestParam(required = false) String lyDoHuy, 
            jakarta.servlet.http.HttpServletRequest request) {
        orderService.updateStatus(id, status, lyDoHuy);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/donHang");
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

    // In hóa đơn
    @GetMapping("/admin/donHang/print/{id}")
    public String inHoaDon(@PathVariable Long id, Model model) {
        DonHang donHang = orderService.getOrderById(id);
        if (donHang == null) {
            return "redirect:/admin/donHang";
        }
        model.addAttribute("donHang", donHang);
        return "admin/printDonHang";
    }
}
