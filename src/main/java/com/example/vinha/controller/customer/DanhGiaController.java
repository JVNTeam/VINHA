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
@RequestMapping("/danh-gia")
public class DanhGiaController {
    @GetMapping
    public String showReviewPage(Model model) {
        // Dữ liệu User
        Map<String, String> user = new HashMap<>();
        user.put("name", "Nguyễn Thu Hà");
        user.put("email", "thuha.nguyen@email.com");

        // Dữ liệu món ăn chờ đánh giá
        List<Map<String, Object>> pendingReviews = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 101);
        item1.put("name", "Cơm Sườn Nướng Muối Ớt");
        item1.put("orderCode", "#VN-2024-0620");
        item1.put("completedDate", "28/07/2024");
        item1.put("image", "https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=300&auto=format&fit=crop");
        item1.put("isNew", true);
        item1.put("isExpanding", true); // Mở sẵn form đánh giá cho món đầu
        pendingReviews.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 102);
        item2.put("name", "Cơm rang");
        item2.put("orderCode", "#VN-2024-0618");
        item2.put("completedDate", "25/07/2024");
        item2.put("image", "https://images.unsplash.com/photo-1603133872878-684f208fb84b?q=80&w=300&auto=format&fit=crop");
        item2.put("isNew", false);
        item2.put("isExpanding", false);
        pendingReviews.add(item2);

        model.addAttribute("user", user);
        model.addAttribute("pendingReviews", pendingReviews);

        return "customer/danhGia";
    }
}
