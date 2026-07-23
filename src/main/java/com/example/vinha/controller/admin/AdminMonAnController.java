package com.example.vinha.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/mon-an")
public class AdminMonAnController {

    @GetMapping
    public String listMonAn(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "minPrice", required = false) Long minPrice,
            @RequestParam(value = "maxPrice", required = false) Long maxPrice,
            Model model) {

        // 1. DỮ LIỆU GIẢ LẬP (Thay thế bằng ProductService.findAll() trong thực tế)
        List<ProductDTO> allProducts = getMockProducts();

        // 2. XỬ LÝ LỌC DỮ LIỆU (Mô phỏng logic của SQL/JPA Specification)
        List<ProductDTO> filteredProducts = allProducts.stream()
                // Lọc theo Tên món ăn (Keyword)
                .filter(p -> (keyword == null || keyword.trim().isEmpty())
                        || p.getName().toLowerCase().contains(keyword.toLowerCase().trim()))
                // Lọc theo Danh mục
                .filter(p -> (category == null || category.trim().isEmpty())
                        || p.getCategory().equalsIgnoreCase(category))
                // Lọc theo Trạng thái
                .filter(p -> (status == null || status.trim().isEmpty())
                        || p.getStatusCode().equalsIgnoreCase(status))
                // Lọc theo Giá Tối Thiểu (Min Price)
                .filter(p -> minPrice == null || p.getRawPrice() >= minPrice)
                // Lọc theo Giá Tối Đa (Max Price)
                .filter(p -> maxPrice == null || p.getRawPrice() <= maxPrice)
                .collect(Collectors.toList());

        // Cập nhật lại STT sau khi lọc
        for (int i = 0; i < filteredProducts.size(); i++) {
            filteredProducts.get(i).setStt(String.format("%02d", i + 1));
        }

        // 3. DANH SÁCH DANH MỤC ĐỂ HIỂN THỊ LÊN THẺ <SELECT>
        List<String> categories = Arrays.asList("Cơm Sườn", "Cơm Gà", "Phở - Bún", "Đồ Uống", "Tráng Miệng");

        // 4. TRUYỀN DỮ LIỆU RA GIAO DIỆN (Thymeleaf)
        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categories);

        // Truyền lại các giá trị đã nhập để giữ trạng thái bộ lọc trên giao diện
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        // Trả về tên file HTML: src/main/resources/templates/admin/quan-ly-mon-an.html
        return "admin/quan-ly-mon-an";
    }

    // =========================================================
    // CLASS DTO & MOCK DATA (Dành cho việc Test giao diện)
    // =========================================================

    private List<ProductDTO> getMockProducts() {
        List<ProductDTO> list = new ArrayList<>();
        list.add(new ProductDTO(1L, "Cơm Sườn Nướng", "/images/foods/com-suon.jpg", "Cơm Sườn", 45000L, 1254, "ACTIVE", "Đang bán"));
        list.add(new ProductDTO(2L, "Cơm Gà Xối Mỡ", "/images/foods/com-ga.jpg", "Cơm Gà", 40000L, 850, "ACTIVE", "Đang bán"));
        list.add(new ProductDTO(3L, "Phở Bò Tái Nạm", "/images/foods/pho-bo.jpg", "Phở - Bún", 55000L, 620, "OUT_OF_STOCK", "Hết hàng"));
        list.add(new ProductDTO(4L, "Bún Chả Hà Nội", "/images/foods/bun-cha.jpg", "Phở - Bún", 50000L, 940, "ACTIVE", "Đang bán"));
        list.add(new ProductDTO(5L, "Trà Đào Cam Sả", null, "Đồ Uống", 25000L, 310, "STOPPED", "Ngừng bán")); // Test trường hợp không có ảnh
        return list;
    }

    public static class ProductDTO {
        private Long id;
        private String stt;
        private String name;
        private String image; // Thuộc tính lưu đường dẫn hình ảnh
        private String category;
        private Long rawPrice; // Giá trị số để phục vụ tính toán/lọc
        private String price;  // Giá trị chuỗi đã format (VD: 45.000đ) để in ra View
        private Integer sold;
        private String statusCode; // Dùng để đổi màu Badge (ACTIVE, OUT_OF_STOCK, STOPPED)
        private String statusText; // Text hiển thị (Đang bán, Hết hàng,...)

        public ProductDTO(Long id, String name, String image, String category, Long rawPrice, Integer sold, String statusCode, String statusText) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.category = category;
            this.rawPrice = rawPrice;
            this.sold = sold;
            this.statusCode = statusCode;
            this.statusText = statusText;

            // Format giá tiền tự động sang chuẩn Việt Nam (45.000đ)
            NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
            this.price = format.format(rawPrice) + "đ";
        }

        // --- Getters & Setters ---
        public Long getId() { return id; }
        public String getStt() { return stt; }
        public void setStt(String stt) { this.stt = stt; }
        public String getName() { return name; }
        public String getImage() { return image; }
        public String getCategory() { return category; }
        public Long getRawPrice() { return rawPrice; }
        public String getPrice() { return price; }
        public Integer getSold() { return sold; }
        public String getStatusCode() { return statusCode; }
        public String getStatusText() { return statusText; }
    }
}