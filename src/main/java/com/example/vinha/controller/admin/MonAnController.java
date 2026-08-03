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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller("quanLyMonAnController")
// CHỐT CHUẨN ĐƯỜNG DẪN LÀ: /admin/monAn (Chữ A viết Hoa)
@RequestMapping("/admin/monAn")
public class MonAnController {

    @Autowired
    private MonAnRepository monAnRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private HinhAnhMonAnRepository hinhAnhMonAnRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String hienThiQuanLyMonAn(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            Model model) {
        String kw = keyword != null ? keyword.trim() : "";
        String st = status != null ? status.trim() : "";
        Long danhMucId = null;
        if (category != null && !category.isBlank()) {
            try {
                danhMucId = Long.parseLong(category);
            } catch (NumberFormatException ignored) {
            }
        }

        List<MonAn> monAnList = monAnRepository.searchWithFilters(
                kw.isEmpty() ? null : kw,
                st.isEmpty() ? null : st,
                danhMucId,
                null,
                null,
                null);
        model.addAttribute("monAnList", monAnList);
        model.addAttribute("categories", danhMucRepository.findAll());
        model.addAttribute("statusOptions", List.of("Đang bán", "Hết hàng", "Ngừng bán"));
        model.addAttribute("keyword", kw);
        model.addAttribute("selectedCategory", danhMucId);
        model.addAttribute("selectedStatus", st);
        model.addAttribute("message", model.asMap().get("message"));
        model.addAttribute("error", model.asMap().get("error"));
        return "admin/monan";
    }

    @GetMapping("/them")
    public String showAddMonAnForm(Model model) {
        if (!model.containsAttribute("monAn")) {
            model.addAttribute("monAn", new MonAn());
        }
        model.addAttribute("categories", danhMucRepository.findAll());
        model.addAttribute("statusOptions", List.of("Đang bán", "Hết hàng", "Ngừng bán"));
        model.addAttribute("formTitle", "Thêm món ăn mới");

        // ĐÃ FIX LỖI: Sửa "monan" thành "monAn"
        model.addAttribute("formAction", "/admin/monAn/them");

        return "admin/themMonAn";
    }

    @PostMapping("/them")
    public String createMonAn(
            @RequestParam("danhMucId") Long danhMucId,
            @RequestParam("ten") String ten,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "thanhPhan", required = false) String thanhPhan,
            @RequestParam("gia") BigDecimal gia,
            @RequestParam("soLuongCon") Integer soLuongCon,
            @RequestParam("trangThai") String trangThai,
            @RequestParam(value = "fileAnh", required = false) String fileAnh,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (ten == null || ten.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên món ăn không được để trống.");
            }
            if (monAnRepository.findByTen(ten.trim()).isPresent()) {
                throw new IllegalArgumentException("Tên món ăn đã tồn tại. Vui lòng chọn tên khác.");
            }
            if (gia == null) {
                throw new IllegalArgumentException("Giá món ăn không được để trống.");
            }
            gia = gia.setScale(2, RoundingMode.HALF_UP);
            if (gia.precision() > 12) {
                throw new IllegalArgumentException("Giá món ăn vượt quá giới hạn tối đa 9.999.999.999,99.");
            }
            if (soLuongCon == null || soLuongCon < 0) {
                throw new IllegalArgumentException("Số lượng còn phải là số nguyên không âm.");
            }
            if (soLuongCon > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Số lượng còn quá lớn.");
            }

            MonAn monAn = MonAn.builder()
                    .danhMuc(danhMucRepository.findById(danhMucId).orElse(null))
                    .ten(ten != null ? ten.trim() : "")
                    .moTa(moTa != null ? moTa.trim() : "")
                    .thanhPhan(thanhPhan != null ? thanhPhan.trim() : "")
                    .gia(gia)
                    .soLuongCon(soLuongCon)
                    .trangThai(trangThai != null ? trangThai : "Đang bán")
                    .build();

            monAnRepository.save(monAn);

            if (fileAnh != null && !fileAnh.trim().isEmpty()) {
                String imageUrl = fileAnh.trim();
                HinhAnhMonAn hinhAnh = HinhAnhMonAn.builder()
                        .monAn(monAn)
                        .duongDan(imageUrl)
                        .trangThai("Mở")
                        .build();
                hinhAnhMonAnRepository.save(hinhAnh);
            }

            redirectAttributes.addFlashAttribute("message", "Thêm món ăn thành công.");
            return "redirect:/admin/monAn";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi thêm món ăn: " + e.getMessage());
            model.addAttribute("monAn", MonAn.builder()
                    .danhMuc(danhMucRepository.findById(danhMucId).orElse(null))
                    .ten(ten)
                    .moTa(moTa)
                    .thanhPhan(thanhPhan)
                    .gia(gia)
                    .soLuongCon(soLuongCon)
                    .trangThai(trangThai)
                    .build());
            model.addAttribute("categories", danhMucRepository.findAll());
            model.addAttribute("statusOptions", List.of("Đang bán", "Ngừng bán"));
            model.addAttribute("formTitle", "Thêm món ăn mới");

            // ĐÃ FIX LỖI: Sửa "monan" thành "monAn"
            model.addAttribute("formAction", "/admin/monAn/them");

            return "admin/themMonAn";
        }
    }

    @GetMapping("/sua")
    public String showEditMonAnForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        MonAn monAn = monAnRepository.findByIdWithHinhAnh(id).orElse(null);
        if (monAn == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy món ăn cần chỉnh sửa.");

            // ĐÃ FIX LỖI: Sửa "monan" thành "monAn"
            return "redirect:/admin/monAn";
        }
        model.addAttribute("monAn", monAn);
        model.addAttribute("categories", danhMucRepository.findAll());
        model.addAttribute("statusOptions", List.of("Đang bán", "Ngừng bán"));
        model.addAttribute("formTitle", "Chỉnh sửa món ăn");
        model.addAttribute("formAction", "/admin/monAn/sua");
        return "admin/themMonAn";
    }

    @PostMapping("/sua")
    public String updateMonAn(
            @RequestParam("id") Long id,
            @RequestParam("danhMucId") Long danhMucId,
            @RequestParam("ten") String ten,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "thanhPhan", required = false) String thanhPhan,
            @RequestParam("gia") BigDecimal gia,
            @RequestParam("soLuongCon") Integer soLuongCon,
            @RequestParam("trangThai") String trangThai,
            @RequestParam(value = "fileAnh", required = false) String fileAnh,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (ten == null || ten.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên món ăn không được để trống.");
            }
            java.util.Optional<MonAn> existingMonAn = monAnRepository.findByTen(ten.trim());
            if (existingMonAn.isPresent() && !existingMonAn.get().getId().equals(id)) {
                throw new IllegalArgumentException("Tên món ăn đã tồn tại. Vui lòng chọn tên khác.");
            }
            if (gia == null) {
                throw new IllegalArgumentException("Giá món ăn không được để trống.");
            }
            gia = gia.setScale(2, RoundingMode.HALF_UP);
            if (gia.precision() > 12) {
                throw new IllegalArgumentException("Giá món ăn vượt quá giới hạn tối đa 9.999.999.999,99.");
            }
            if (soLuongCon == null || soLuongCon < 0) {
                throw new IllegalArgumentException("Số lượng còn phải là số nguyên không âm.");
            }
            if (soLuongCon > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Số lượng còn quá lớn.");
            }

            MonAn monAn = monAnRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn."));
            monAn.setDanhMuc(danhMucRepository.findById(danhMucId).orElse(null));
            monAn.setTen(ten != null ? ten.trim() : "");
            monAn.setMoTa(moTa != null ? moTa.trim() : "");
            monAn.setThanhPhan(thanhPhan != null ? thanhPhan.trim() : "");
            monAn.setGia(gia);
            monAn.setSoLuongCon(soLuongCon);
            monAn.setTrangThai(trangThai != null ? trangThai : monAn.getTrangThai());
            monAnRepository.save(monAn);

            if (fileAnh != null && !fileAnh.trim().isEmpty()) {
                String imageUrl = fileAnh.trim();
                List<HinhAnhMonAn> existingImages = hinhAnhMonAnRepository.findByMonAnId(monAn.getId());
                if (!existingImages.isEmpty()) {
                    HinhAnhMonAn existingImage = existingImages.get(0);
                    existingImage.setDuongDan(imageUrl);
                    existingImage.setTrangThai("Mở");
                    hinhAnhMonAnRepository.save(existingImage);
                } else {
                    HinhAnhMonAn hinhAnh = HinhAnhMonAn.builder()
                            .monAn(monAn)
                            .duongDan(imageUrl)
                            .trangThai("Mở")
                            .build();
                    hinhAnhMonAnRepository.save(hinhAnh);
                }
            } else {
                List<HinhAnhMonAn> existingImages = hinhAnhMonAnRepository.findByMonAnId(monAn.getId());
                if (!existingImages.isEmpty()) {
                    hinhAnhMonAnRepository.deleteAll(existingImages);
                }
            }

            redirectAttributes.addFlashAttribute("message", "Cập nhật món ăn thành công.");
            return "redirect:/admin/monAn";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi chỉnh sửa món ăn: " + e.getMessage());
            model.addAttribute("monAn", monAnRepository.findByIdWithHinhAnh(id).orElse(new MonAn()));
            model.addAttribute("categories", danhMucRepository.findAll());
            model.addAttribute("statusOptions", List.of("Đang bán", "Ngừng bán"));
            model.addAttribute("formTitle", "Chỉnh sửa món ăn");
            model.addAttribute("formAction", "/admin/monAn/sua");
            return "admin/themMonAn";
        }
    }

    @GetMapping("/xoa")
    public String deleteMonAn(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        java.util.Optional<MonAn> monAnOpt = monAnRepository.findById(id);
        if (monAnOpt.isPresent()) {
            MonAn monAn = monAnOpt.get();
            monAn.setTrangThai("Ngừng bán");
            monAnRepository.save(monAn);
            redirectAttributes.addFlashAttribute("message", "Đã xóa mềm món ăn (chuyển trạng thái thành Ngừng bán).");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy món ăn để xóa.");
        }
        return "redirect:/admin/monAn";
    }
}