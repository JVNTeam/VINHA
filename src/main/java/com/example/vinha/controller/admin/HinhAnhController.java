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
@RequestMapping("/admin/hinh-anh") // ĐÃ SỬA: Thêm "/admin" để khớp với href trong HTML và fix lỗi 404
public class HinhAnhController {

        @GetMapping
        public String showImageManagementPage(
                @RequestParam(value = "keyword", required = false) String keyword,
                @RequestParam(value = "category", required = false) String category,
                @RequestParam(value = "status", required = false) String status, // Tham số nhận bộ lọc Trạng thái
                Model model) {

                // 1. Danh sách Danh mục cho Dropdown Lọc
                List<String> categories = List.of("Cơm gà", "Cơm sườn", "Cơm cá", "Cơm bò", "Cơm niêu");

                // 2. Tạo danh sách Hình ảnh món ăn mẫu (Mock Data)
                List<Map<String, Object>> allImages = new ArrayList<>();

                allImages.add(createImageItem("Cơm Gà Xối Mỡ",
                        "https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?q=80&w=150&auto=format&fit=crop",
                        "/images/dishes/com-ga-xoi-mo.jpg", "ACTIVE", "Đang sử dụng", "Cơm gà"));

                allImages.add(createImageItem("Cơm Sườn",
                        "https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=150&auto=format&fit=crop",
                        "/images/dishes/com-suon-nuong.jpg", "LOCKED", "Khóa", "Cơm sườn"));

                allImages.add(createImageItem("Cơm Cá Lóc Kho Tộ",
                        "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=150&auto=format&fit=crop",
                        "/images/dishes/ca-loc-kho-to.jpg", "ACTIVE", "Đang sử dụng", "Cơm cá"));

                allImages.add(createImageItem("Cơm Bò Lúc Lắc",
                        "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?q=80&w=150&auto=format&fit=crop",
                        "/images/dishes/bo-luc-lac.jpg", "ACTIVE", "Đang sử dụng", "Cơm bò"));

                allImages.add(createImageItem("Cơm Niêu Cháy Cạnh",
                        "https://images.unsplash.com/photo-1512058564366-18510be2db19?q=80&w=150&auto=format&fit=crop",
                        "/images/dishes/com-nieu.jpg", "LOCKED", "Khóa", "Cơm niêu"));

                // 3. Thực hiện LỌC DỮ LIỆU
                List<Map<String, Object>> filteredImages = allImages.stream()
                        // Lọc theo Tên món ăn (Keyword)
                        .filter(item -> keyword == null || keyword.trim().isEmpty() ||
                                item.get("dishName").toString().toLowerCase().contains(keyword.toLowerCase().trim()))
                        // Lọc theo Danh mục (Category)
                        .filter(item -> category == null || category.trim().isEmpty() ||
                                item.get("category").toString().equalsIgnoreCase(category))
                        // Lọc theo Trạng thái (Status - ACTIVE/LOCKED)
                        .filter(item -> status == null || status.trim().isEmpty() ||
                                item.get("statusCode").toString().equalsIgnoreCase(status))
                        .collect(Collectors.toList());

                // 4. Cập nhật lại STT (01, 02...) sau khi lọc để bảng luôn đánh số liên tục
                for (int i = 0; i < filteredImages.size(); i++) {
                        filteredImages.get(i).put("stt", String.format("%02d", i + 1));
                }

                // 5. Gửi dữ liệu ra file HTML
                model.addAttribute("categories", categories);
                model.addAttribute("imageList", filteredImages);

                // Gửi lại các giá trị đang lọc để hiển thị giữ nguyên trạng thái trên thanh tìm kiếm
                model.addAttribute("keyword", keyword);
                model.addAttribute("selectedCategory", category);
                model.addAttribute("selectedStatus", status);

                // Trả về đúng tên file HTML trong thư mục templates/admin/
                return "admin/quan-ly-mon-an";
        }

        // Hàm helper để khởi tạo data
        private Map<String, Object> createImageItem(String dishName, String imageUrl, String path,
                                                    String statusCode, String statusText, String category) {
                Map<String, Object> item = new HashMap<>();
                // Thuộc tính stt không cần truyền ở đây, sẽ được đánh số lại ở Bước 4
                item.put("dishName", dishName);
                item.put("imageUrl", imageUrl);
                item.put("path", path);
                item.put("statusCode", statusCode); // Mã trạng thái để lọc (ACTIVE, LOCKED)
                item.put("statusText", statusText); // Chữ hiển thị ra giao diện HTML (Đang sử dụng, Khóa)
                item.put("category", category);     // Danh mục để lọc
                return item;
        }
}