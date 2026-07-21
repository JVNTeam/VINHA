package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lich-su-don-hang")
public class LichSuDonHangController {
    @GetMapping
    public String showOrderHistoryPage(Model model) {
        // 1. Dữ liệu giả lập User (Đầy đủ thuộc tính avatar để tránh lỗi Thymeleaf)
        Map<String, String> user = new HashMap<>();
        user.put("name", "Nguyễn Thu Hà");
        user.put("email", "thuha.nguyen@email.com");

        // 2. Dữ liệu giả lập Danh sách đơn hàng
        List<Map<String, Object>> orders = new ArrayList<>();
        orders.add(createOrder("#VN-2024-0812", "12/08/2024", "245.000đ", "pending", "Chờ xác nhận"));
        orders.add(createOrder("#VN-2024-0790", "10/08/2024", "180.000đ", "confirmed", "Đã xác nhận"));
        orders.add(createOrder("#VN-2024-0750", "05/08/2024", "560.000đ", "confirmed", "Đã xác nhận"));
        orders.add(createOrder("#VN-2024-0620", "28/07/2024", "320.000đ", "completed", "Hoàn thành"));
        orders.add(createOrder("#VN-2024-0511", "15/07/2024", "120.000đ", "cancelled", "Đã hủy"));

        // 3. Đưa dữ liệu vào Model
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);

        // 4. Trả về đúng file HTML
        return "customer/lichSuDon";
    }

    // Helper tạo Map nhanh cho từng đơn hàng
    private Map<String, Object> createOrder(String code, String date, String price, String statusCode, String statusText) {
        Map<String, Object> order = new HashMap<>();
        order.put("code", code);
        order.put("date", date);
        order.put("price", price);
        order.put("statusCode", statusCode);
        order.put("statusText", statusText);
        return order;
    }
}
