package com.example.vinha.controller.admin;

import com.example.vinha.entity.HinhAnhMonAn;
import com.example.vinha.entity.MonAn;
import com.example.vinha.repository.DanhMucRepository;
import com.example.vinha.repository.HinhAnhMonAnRepository;
import com.example.vinha.repository.MonAnRepository;
import com.example.vinha.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/admin/hinhAnh", "/admin/hinh-anh"})
public class HinhAnhController {

    @Autowired
    private HinhAnhMonAnRepository hinhAnhMonAnRepository;

    @Autowired
    private MonAnRepository monAnRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String showImageManagementPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        String kw = keyword != null ? keyword.trim() : "";
        String cat = category != null ? category.trim() : "";
        String st = status != null ? status.trim() : "";

        List<HinhAnhMonAn> allImages = hinhAnhMonAnRepository.search(
                kw.isEmpty() ? null : kw,
                cat.isEmpty() ? null : cat,
                st.isEmpty() ? null : st
        );

        List<String> categories = danhMucRepository.findAll().stream()
                .map(dm -> dm.getTen())
                .filter(name -> name != null && !name.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        categories.add(0, "Chưa phân loại");

        model.addAttribute("categories", categories);
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("imageList", allImages);
        model.addAttribute("keyword", kw);
        model.addAttribute("selectedCategory", cat);
        model.addAttribute("selectedStatus", st);
        model.addAttribute("message", model.asMap().get("message"));
        model.addAttribute("error", model.asMap().get("error"));

        return "admin/hinhAnhMonAn";
    }

    @GetMapping("/them")
    public String showAddImageForm(Model model) {
        model.addAttribute("monAnList", monAnRepository.findAll());
        model.addAttribute("categories", danhMucRepository.findAll());
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("formTitle", "Thêm hình ảnh mới");
        model.addAttribute("formAction", "/admin/hinhAnh/them");
        model.addAttribute("image", new HinhAnhMonAn());
        return "admin/themHinhAnh";
    }

    @PostMapping("/them")
    public String uploadImage(
            @RequestParam("monAnId") Long monAnId,
            @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (fileAnh == null || fileAnh.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn ảnh để tải lên.");
            }

            MonAn monAn = monAnRepository.findById(monAnId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn được chọn."));

            String imageUrl = fileStorageService.storeFile(fileAnh);
            HinhAnhMonAn image = HinhAnhMonAn.builder()
                    .monAn(monAn)
                    .duongDan(imageUrl)
                    .trangThai(trangThai != null && !trangThai.isBlank() ? trangThai : "Mở")
                    .build();
            hinhAnhMonAnRepository.save(image);

            redirectAttributes.addFlashAttribute("message", "Tải ảnh lên thành công.");
            return "redirect:/admin/hinhAnh";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi tải ảnh: " + e.getMessage());
            model.addAttribute("monAnList", monAnRepository.findAll());
            model.addAttribute("categories", danhMucRepository.findAll());
            model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
            model.addAttribute("formTitle", "Thêm hình ảnh mới");
            model.addAttribute("formAction", "/admin/hinhAnh/them");
            model.addAttribute("image", new HinhAnhMonAn());
            return "admin/themHinhAnh";
        }
    }

    @GetMapping("/sua")
    public String showEditImageForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        HinhAnhMonAn image = hinhAnhMonAnRepository.findById(id).orElse(null);
        if (image == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy hình ảnh cần chỉnh sửa.");
            return "redirect:/admin/hinhAnh";
        }
        model.addAttribute("monAnList", monAnRepository.findAll());
        model.addAttribute("categories", danhMucRepository.findAll());
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("formTitle", "Chỉnh sửa hình ảnh");
        model.addAttribute("formAction", "/admin/hinhAnh/sua");
        model.addAttribute("image", image);
        return "admin/themHinhAnh";
    }

    @PostMapping("/sua")
    public String updateImage(
            @RequestParam("id") Long id,
            @RequestParam("monAnId") Long monAnId,
            @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            HinhAnhMonAn image = hinhAnhMonAnRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh cần chỉnh sửa."));
            MonAn monAn = monAnRepository.findById(monAnId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn được chọn."));

            image.setMonAn(monAn);
            if (fileAnh != null && !fileAnh.isEmpty()) {
                image.setDuongDan(fileStorageService.storeFile(fileAnh));
            }
            image.setTrangThai(trangThai != null && !trangThai.isBlank() ? trangThai : image.getTrangThai());
            hinhAnhMonAnRepository.save(image);

            redirectAttributes.addFlashAttribute("message", "Cập nhật hình ảnh thành công.");
            return "redirect:/admin/hinhAnh";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi chỉnh sửa ảnh: " + e.getMessage());
            model.addAttribute("monAnList", monAnRepository.findAll());
            model.addAttribute("categories", danhMucRepository.findAll());
            model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
            model.addAttribute("formTitle", "Chỉnh sửa hình ảnh");
            model.addAttribute("formAction", "/admin/hinhAnh/sua");
            model.addAttribute("image", hinhAnhMonAnRepository.findById(id).orElse(new HinhAnhMonAn()));
            return "admin/themHinhAnh";
        }
    }

    @GetMapping("/xoa")
    public String deleteImage(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (hinhAnhMonAnRepository.existsById(id)) {
                hinhAnhMonAnRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("message", "Xóa hình ảnh thành công.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy hình ảnh để xóa.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa hình ảnh: Có thể ảnh đang được sử dụng ở nơi khác.");
        }
        return "redirect:/admin/hinhAnh";
    }
}