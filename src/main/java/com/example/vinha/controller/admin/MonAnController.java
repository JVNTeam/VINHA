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

@Controller
@RequestMapping("/mon-an")
public class MonAnController {
    @GetMapping
    public String showProductManagementPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        // 1. Danh sách Danh mục cho Dropdown Lọc
        List<String> categories = List.of("Cơm Sườn", "Cơm Gà", "Cơm Bò", "Đồ uống", "Món phụ");

        // 2. Dữ liệu Danh sách Món ăn mẫu
        List<Map<String, Object>> products = new ArrayList<>();

        products.add(createProduct("01", "Cơm Sườn Nướng", "Cơm Sườn", "45.000đ", 120, 1254, "ACTIVE", "Đang bán",
                "https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=150&auto=format&fit=crop"));

        products.add(createProduct("02", "Cơm Gà Xối Mỡ", "Cơm Gà", "55.000đ", 0, 842, "OUT_OF_STOCK", "Hết hàng",
                "https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?q=80&w=150&auto=format&fit=crop"));

        products.add(createProduct("03", "Cơm Bò Lúc Lắc", "Cơm Bò", "75.000đ", 45, 312, "STOPPED", "Ngừng bán",
                "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?q=80&w=150&auto=format&fit=crop"));

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedStatus", status);

        return "admin/monAn";
    }

    private Map<String, Object> createProduct(String stt, String name, String category, String price, int stock, int sold, String statusCode, String statusText, String image) {
        Map<String, Object> p = new HashMap<>();
        p.put("stt", stt);
        p.put("name", name);
        p.put("category", category);
        p.put("price", price);
        p.put("stock", stock);
        p.put("sold", sold);
        p.put("statusCode", statusCode);
        p.put("statusText", statusText);
        p.put("image", image);
        return p;
    }

}
