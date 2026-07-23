package com.example.vinha.controller.admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/danh-gia")
public class AdminDanhGiaController {

    @GetMapping
    public String showReviewManagement(
            @RequestParam(value = "foodName", required = false) String foodName,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "reviewDate", required = false) String reviewDate,
            Model model) {

        // 1. Dữ liệu giả lập (Mock Data) cho đánh giá
        List<Map<String, Object>> allReviews = new ArrayList<>();

        allReviews.add(createReview(
                "Nguyễn Văn Hoàng", "0987 654 321", "Cơm Sườn Nướng Đặc Biệt", 5,
                "Sườn ướp rất đậm vị, thịt mềm không bị khô, nước mắm pha ngon tuyệt vời! Sẽ ủng hộ dài dài.",
                List.of("https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=150&auto=format&fit=crop"),
                "10:24 - 25/10/2026", "2026-10-25"));

        allReviews.add(createReview(
                "Trần Thị Lan", "0901 234 567", "Phở Bò Tái Nạm", 1,
                "Nước dùng nguội ngắt, thịt bò bị dai và ít bánh phở. Thất vọng quá!",
                new ArrayList<>(),
                "08:15 - 25/10/2026", "2026-10-25"));

        allReviews.add(createReview(
                "Phạm Minh Mạnh", "0912 888 999", "Cơm Gà Xối Mỡ", 4,
                "Gà giòn rụm, cơm thơm, nước tương vừa miệng.",
                new ArrayList<>(),
                "14:10 - 24/10/2026", "2026-10-24"));

        // 2. Logic Lọc dữ liệu (Filtering)
        List<Map<String, Object>> filteredReviews = allReviews.stream()
                // Lọc theo tên món ăn
                .filter(item -> foodName == null || foodName.trim().isEmpty() ||
                        item.get("foodName").toString().toLowerCase().contains(foodName.toLowerCase().trim()))
                // Lọc theo số sao
                .filter(item -> rating == null || (Integer) item.get("rating") == rating)
                // Lọc theo ngày (so sánh chuỗi ngày YYYY-MM-DD từ thẻ input type="date")
                .filter(item -> reviewDate == null || reviewDate.trim().isEmpty() ||
                        item.get("rawDate").toString().equalsIgnoreCase(reviewDate))
                .collect(Collectors.toList());

        // 3. Đánh lại STT (01, 02...) sau khi lọc dữ liệu
        for (int i = 0; i < filteredReviews.size(); i++) {
            filteredReviews.get(i).put("stt", String.format("%02d", i + 1));
        }

        // 4. Truyền dữ liệu ra giao diện Thymeleaf
        model.addAttribute("reviews", filteredReviews);
        model.addAttribute("foodName", foodName);
        model.addAttribute("selectedRating", rating);
        model.addAttribute("selectedDate", reviewDate);

        // Trả về file: src/main/resources/templates/admin/quan-ly-danh-gia.html
        return "admin/quan-ly-danh-gia";
    }

    // Hàm hỗ trợ tạo dữ liệu mẫu
    private Map<String, Object> createReview(String customerName, String phone,
                                             String foodName, int rating, String content,
                                             List<String> images, String time, String rawDate) {
        Map<String, Object> item = new HashMap<>();
        item.put("customerName", customerName);
        item.put("phone", phone);
        item.put("foodName", foodName);
        item.put("rating", rating);
        item.put("content", content);
        item.put("images", images);
        item.put("time", time);
        item.put("rawDate", rawDate); // Dùng để đối chiếu với giá trị input type="date"
        return item;
    }
}