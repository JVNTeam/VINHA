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
@RequestMapping("/hinh-anh")
public class HinhAnhController {
        @GetMapping
        public String showImageManagementPage(
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "category", required = false) String category,
                        Model model) {

                // 1. Danh sách Danh mục cho Dropdown Lọc
                List<String> categories = List.of("Tất cả món ăn", "Cơm gà", "Cơm sườn", "Cơm cá", "Cơm bò",
                                "Cơm niêu");

                // 2. Danh sách Hình ảnh món ăn mẫu
                List<Map<String, Object>> imageList = new ArrayList<>();

                imageList.add(createImageItem("01", "Cơm Gà Xối Mỡ",
                                "https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?q=80&w=150&auto=format&fit=crop",
                                "/images/dishes/com-ga-xoi-mo.jpg", "Khóa"));

                imageList.add(createImageItem("02", "Cơm Sườn",
                                "https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=150&auto=format&fit=crop",
                                "/images/dishes/com-suon-nuong.jpg", "Khóa"));

                imageList.add(createImageItem("03", "Cơm Cả Lóc Kho Tộ",
                                "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=150&auto=format&fit=crop",
                                "/images/dishes/ca-loc-kho-to.jpg", "Khóa"));

                imageList.add(createImageItem("04", "Cơm Bò Lúc Lắc",
                                "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?q=80&w=150&auto=format&fit=crop",
                                "/images/dishes/bo-luc-lac.jpg", "Khóa"));

                imageList.add(createImageItem("05", "Cơm Niêu Cháy Cạnh",
                                "https://images.unsplash.com/photo-1512058564366-18510be2db19?q=80&w=150&auto=format&fit=crop",
                                "/images/dishes/com-nieu.jpg", "Khóa"));

                model.addAttribute("categories", categories);
                model.addAttribute("imageList", imageList);
                model.addAttribute("keyword", keyword);
                model.addAttribute("selectedCategory", category);

                return "admin/hinhAnhMonAn";
        }

        private Map<String, Object> createImageItem(String stt, String dishName, String imageUrl, String path,
                        String status) {
                Map<String, Object> item = new HashMap<>();
                item.put("stt", stt);
                item.put("dishName", dishName);
                item.put("imageUrl", imageUrl);
                item.put("path", path);
                item.put("status", status);
                return item;
        }
}
