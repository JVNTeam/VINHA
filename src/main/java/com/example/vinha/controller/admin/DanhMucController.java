package com.example.vinha.controller.admin;

import com.example.vinha.entity.DanhMuc;
import com.example.vinha.repository.DanhMucRepository;
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
@RequestMapping("/admin/danhMuc")
public class DanhMucController {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String hienThiDanhMuc(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        String kw = keyword != null ? keyword.trim().toLowerCase() : "";
        String st = status != null ? status.trim() : "";

        List<DanhMuc> danhMucList = danhMucRepository.findAll().stream()
                .filter(dm -> filterByKeyword(dm, kw))
                .filter(dm -> filterByStatus(dm, st))
                .collect(Collectors.toList());

        model.addAttribute("danhMucs", danhMucList);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("selectedStatus", st);
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("message", model.asMap().get("message"));
        model.addAttribute("error", model.asMap().get("error"));

        return "admin/danhMuc";
    }

    @GetMapping("/them")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("danhMuc")) {
            model.addAttribute("danhMuc", new DanhMuc());
        }
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("formTitle", "Thêm danh mục mới");
        model.addAttribute("formAction", "/admin/danhMuc/them");
        return "admin/themDanhMuc";
    }

    @PostMapping("/them")
    public String createDanhMuc(
            @RequestParam("ten") String ten,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "anh", required = false) String anh,
            @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh,
            @RequestParam(value = "soLuong", required = false) Integer soLuong,
            RedirectAttributes redirectAttributes) {

        if (ten == null || ten.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên danh mục không được để trống.");
            redirectAttributes.addFlashAttribute("danhMuc", DanhMuc.builder().ten(ten).moTa(moTa).trangThai(trangThai).anh(anh).soLuong(0).build());
            return "redirect:/admin/danhMuc/them";
        }

        if (danhMucRepository.findByTenIgnoreCase(ten.trim()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tên danh mục đã tồn tại. Vui lòng chọn tên khác.");
            redirectAttributes.addFlashAttribute("danhMuc", DanhMuc.builder().ten(ten).moTa(moTa).trangThai(trangThai).anh(anh).soLuong(0).build());
            return "redirect:/admin/danhMuc/them";
        }

        String imagePath = anh != null ? anh.trim() : "";
        if (fileAnh != null && !fileAnh.isEmpty()) {
            imagePath = fileStorageService.storeFile(fileAnh);
        }

        DanhMuc danhMuc = DanhMuc.builder()
                .ten(ten.trim())
                .moTa(moTa != null ? moTa.trim() : "")
                .trangThai(trangThai != null && !trangThai.isBlank() ? trangThai : "Mở")
                .anh(imagePath)
                .soLuong(0)
                .build();
        danhMucRepository.save(danhMuc);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công.");
        return "redirect:/admin/danhMuc";
    }

    @GetMapping("/sua")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        DanhMuc danhMuc = danhMucRepository.findById(id).orElse(null);
        if (danhMuc == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục cần sửa.");
            return "redirect:/admin/danhMuc";
        }
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("statusOptions", List.of("Mở", "Khóa"));
        model.addAttribute("formTitle", "Chỉnh sửa danh mục");
        model.addAttribute("formAction", "/admin/danhMuc/sua");
        return "admin/themDanhMuc";
    }

    @PostMapping("/sua")
    public String updateDanhMuc(
            @RequestParam("id") Long id,
            @RequestParam("ten") String ten,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "anh", required = false) String anh,
            @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh,
            @RequestParam(value = "soLuong", required = false) Integer soLuong,
            RedirectAttributes redirectAttributes) {

        DanhMuc danhMuc = danhMucRepository.findById(id).orElse(null);
        if (danhMuc == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục cần chỉnh sửa.");
            return "redirect:/admin/danhMuc";
        }

        if (ten == null || ten.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên danh mục không được để trống.");
            redirectAttributes.addFlashAttribute("danhMuc", DanhMuc.builder().id(id).ten(ten).moTa(moTa).trangThai(trangThai).anh(anh).soLuong(danhMuc.getSoLuong()).build());
            return "redirect:/admin/danhMuc/sua?id=" + id;
        }

        java.util.Optional<DanhMuc> existingDM = danhMucRepository.findByTenIgnoreCase(ten.trim());
        if (existingDM.isPresent() && !existingDM.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Tên danh mục đã tồn tại. Vui lòng chọn tên khác.");
            redirectAttributes.addFlashAttribute("danhMuc", DanhMuc.builder().id(id).ten(ten).moTa(moTa).trangThai(trangThai).anh(anh).soLuong(danhMuc.getSoLuong()).build());
            return "redirect:/admin/danhMuc/sua?id=" + id;
        }

        String imagePath = anh != null ? anh.trim() : "";
        if (fileAnh != null && !fileAnh.isEmpty()) {
            imagePath = fileStorageService.storeFile(fileAnh);
        }
        danhMuc.setTen(ten.trim());
        danhMuc.setMoTa(moTa != null ? moTa.trim() : "");
        danhMuc.setTrangThai(trangThai != null && !trangThai.isBlank() ? trangThai : "Mở");
        danhMuc.setAnh(imagePath);
        // Khong cap nhat so luong tu request vi da bi an tren form
        danhMucRepository.save(danhMuc);

        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công.");
        return "redirect:/admin/danhMuc";
    }

    @GetMapping("/xoa")
    public String deleteDanhMuc(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        java.util.Optional<DanhMuc> danhMucOpt = danhMucRepository.findById(id);
        if (danhMucOpt.isPresent()) {
            DanhMuc danhMuc = danhMucOpt.get();
            danhMuc.setTrangThai("Khóa");
            danhMucRepository.save(danhMuc);
            redirectAttributes.addFlashAttribute("message", "Đã xóa mềm danh mục (chuyển trạng thái thành Khóa).");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục cần xóa.");
        }
        return "redirect:/admin/danhMuc";
    }

    private boolean filterByKeyword(DanhMuc danhMuc, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        return danhMuc.getTen() != null && danhMuc.getTen().toLowerCase().contains(keyword);
    }

    private boolean filterByStatus(DanhMuc danhMuc, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        return danhMuc.getTrangThai() != null && danhMuc.getTrangThai().equalsIgnoreCase(status);
    }
}
