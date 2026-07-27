package com.example.vinha.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.vinha.service.FileStorageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/hinhAnh")
public class HinhAnhController {

        @Autowired
        private FileStorageService fileStorageService;

        // =========================================================
        // 1. HÀM HIỂN THỊ DANH SÁCH ẢNH (BẠN ĐÃ LỠ XÓA MẤT ĐOẠN NÀY)
        // =========================================================
        @GetMapping
        public String showImageManagementPage(
                @RequestParam(value = "keyword", required = false) String keyword,
                @RequestParam(value = "category", required = false) String category,
                @RequestParam(value = "status", required = false) String status,
                Model model) {

                List<String> categories = List.of("Cơm gà", "Cơm sườn", "Cơm cá", "Cơm bò", "Cơm niêu");
                List<Map<String, Object>> allImages = new ArrayList<>();

                allImages.add(createImageItem("Cơm Gà Xối Mỡ", "https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?q=80&w=150&auto=format&fit=crop", "/images/dishes/com-ga-xoi-mo.jpg", "ACTIVE", "Đang sử dụng", "Cơm gà"));
                allImages.add(createImageItem("Cơm Sườn", "https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=150&auto=format&fit=crop", "/images/dishes/com-suon-nuong.jpg", "LOCKED", "Khóa", "Cơm sườn"));
                allImages.add(createImageItem("Cơm Cá Lóc Kho Tộ", "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=150&auto=format&fit=crop", "/images/dishes/ca-loc-kho-to.jpg", "ACTIVE", "Đang sử dụng", "Cơm cá"));
                allImages.add(createImageItem("Cơm Bò Lúc Lắc", "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?q=80&w=150&auto=format&fit=crop", "/images/dishes/bo-luc-lac.jpg", "ACTIVE", "Đang sử dụng", "Cơm bò"));
                allImages.add(createImageItem("Cơm Niêu Cháy Cạnh", "https://images.unsplash.com/photo-1512058564366-18510be2db19?q=80&w=150&auto=format&fit=crop", "/images/dishes/com-nieu.jpg", "LOCKED", "Khóa", "Cơm niêu"));

                List<Map<String, Object>> filteredImages = allImages.stream()
                        .filter(item -> keyword == null || keyword.trim().isEmpty() || item.get("dishName").toString().toLowerCase().contains(keyword.toLowerCase().trim()))
                        .filter(item -> category == null || category.trim().isEmpty() || item.get("category").toString().equalsIgnoreCase(category))
                        .filter(item -> status == null || status.trim().isEmpty() || item.get("statusCode").toString().equalsIgnoreCase(status))
                        .collect(Collectors.toList());

                for (int i = 0; i < filteredImages.size(); i++) {
                        filteredImages.get(i).put("stt", String.format("%02d", i + 1));
                }

                model.addAttribute("categories", categories);
                model.addAttribute("imageList", filteredImages);
                model.addAttribute("keyword", keyword);
                model.addAttribute("selectedCategory", category);
                model.addAttribute("selectedStatus", status);

                // Lưu ý: Tên file HTML phải khớp với tên trong thư mục templates/admin
                return "admin/hinhAnhMonAn";
        }

        // Hàm phụ trợ tạo Mock Data
        private Map<String, Object> createImageItem(String dishName, String imageUrl, String path, String statusCode, String statusText, String category) {
                Map<String, Object> item = new HashMap<>();
                item.put("dishName", dishName);
                item.put("imageUrl", imageUrl);
                item.put("path", path);
                item.put("statusCode", statusCode);
                item.put("statusText", statusText);
                item.put("category", category);
                return item;
        }

        // =========================================================
        // 2. HÀM HIỂN THỊ FORM THÊM ẢNH
        // =========================================================
        @GetMapping("/them")
        public String showAddImageForm(Model model) {
                model.addAttribute("monAnList", List.of("Cơm Gà Xối Mỡ", "Bún Bò Huế"));
                return "admin/themHinhAnh";
        }

        // =========================================================
        // 3. HÀM XỬ LÝ LƯU ẢNH KHI NHẤN NÚT "LƯU HÌNH ẢNH"
        // =========================================================
        @PostMapping("/them")
        public String uploadImage(
                @RequestParam("monAnId") String monAnId, // Tạm thời để String vì mock data đang dùng chữ
                @RequestParam("fileAnh") MultipartFile fileAnh,
                Model model) {

                try {
                        // Lưu file vật lý và lấy đường dẫn URL
                        String imageUrl = fileStorageService.storeFile(fileAnh);

                        System.out.println("Đã lưu ảnh thành công. Đường dẫn: " + imageUrl);

                        // Truyền thông báo thành công sang trang danh sách
                        model.addAttribute("message", "Tải ảnh lên thành công: " + imageUrl);

                        // Forward (chuyển tiếp) về hàm GetMapping gốc để load lại danh sách thay vì redirect
                        return showImageManagementPage(null, null, null, model);

                } catch (Exception e) {
                        model.addAttribute("error", "Lỗi tải ảnh: " + e.getMessage());
                        model.addAttribute("monAnList", List.of("Cơm Gà Xối Mỡ", "Bún Bò Huế"));
                        return "admin/themHinhAnh";
                }
        }
}