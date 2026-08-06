package com.example.vinha.controller.admin;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.entity.VaiTro;
import com.example.vinha.repository.NguoiDungRepository;
import com.example.vinha.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/nhanVien")
public class NhanVienController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @GetMapping
    public String hienThiNhanVien(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        String kw = keyword != null ? keyword.trim() : "";
        String st = status != null ? status.trim() : "";

        // Vai trò 2 (Nhân viên) và 3 (Admin)
        List<Long> vaiTroIds = List.of(2L, 3L);
        List<NguoiDung> nhanVienList = nguoiDungRepository.searchByVaiTroIdsAndKeywordAndStatus(vaiTroIds, kw, st);

        model.addAttribute("nhanVienList", nhanVienList);
        model.addAttribute("keyword", kw);
        model.addAttribute("selectedStatus", st);

        return "admin/nhanVien";
    }

    @GetMapping("/them")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("nhanVien")) {
            model.addAttribute("nhanVien", new NguoiDung());
        }
        model.addAttribute("formTitle", "Thêm nhân viên mới");
        model.addAttribute("formAction", "/admin/nhanVien/them");
        model.addAttribute("vaiTroList", vaiTroRepository.findAll().stream().filter(v -> v.getId() == 2L || v.getId() == 3L).toList());
        return "admin/themNhanVien";
    }

    @PostMapping("/them")
    public String createNhanVien(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("email") String email,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("matKhau") String matKhau,
            @RequestParam("vaiTroId") Long vaiTroId,
            RedirectAttributes redirectAttributes) {

        if (nguoiDungRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
            return "redirect:/admin/nhanVien/them";
        }
        if (nguoiDungRepository.existsBySoDienThoai(soDienThoai)) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được sử dụng.");
            return "redirect:/admin/nhanVien/them";
        }

        VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);
        if (vaiTro == null) {
            redirectAttributes.addFlashAttribute("error", "Vai trò không hợp lệ.");
            return "redirect:/admin/nhanVien/them";
        }

        NguoiDung nd = NguoiDung.builder()
                .hoTen(hoTen.trim())
                .email(email.trim())
                .soDienThoai(soDienThoai.trim())
                .matKhau(matKhau) // Chưa hash vì đơn giản hóa
                .vaiTro(vaiTro)
                .trangThai("ACTIVE")
                .build();
        nguoiDungRepository.save(nd);

        redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công.");
        return "redirect:/admin/nhanVien";
    }

    @GetMapping("/sua")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        NguoiDung nhanVien = nguoiDungRepository.findById(id).orElse(null);
        if (nhanVien == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên.");
            return "redirect:/admin/nhanVien";
        }
        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("formTitle", "Chỉnh sửa nhân viên");
        model.addAttribute("formAction", "/admin/nhanVien/sua");
        model.addAttribute("vaiTroList", vaiTroRepository.findAll().stream().filter(v -> v.getId() == 2L || v.getId() == 3L).toList());
        return "admin/themNhanVien";
    }

    @PostMapping("/sua")
    public String updateNhanVien(
            @RequestParam("id") Long id,
            @RequestParam("hoTen") String hoTen,
            @RequestParam("email") String email,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam(value = "matKhau", required = false) String matKhau,
            @RequestParam("vaiTroId") Long vaiTroId,
            @RequestParam("trangThai") String trangThai,
            RedirectAttributes redirectAttributes) {

        NguoiDung nd = nguoiDungRepository.findById(id).orElse(null);
        if (nd == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên.");
            return "redirect:/admin/nhanVien";
        }

        // Kiểm tra trùng email/sđt với ID khác
        java.util.Optional<NguoiDung> byEmail = nguoiDungRepository.findByEmail(email);
        if (byEmail.isPresent() && !byEmail.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại.");
            return "redirect:/admin/nhanVien/sua?id=" + id;
        }

        java.util.Optional<NguoiDung> bySdt = nguoiDungRepository.findBySoDienThoai(soDienThoai);
        if (bySdt.isPresent() && !bySdt.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại.");
            return "redirect:/admin/nhanVien/sua?id=" + id;
        }

        nd.setHoTen(hoTen.trim());
        nd.setEmail(email.trim());
        nd.setSoDienThoai(soDienThoai.trim());
        if (matKhau != null && !matKhau.isEmpty()) {
            nd.setMatKhau(matKhau);
        }
        nd.setVaiTro(vaiTroRepository.findById(vaiTroId).orElse(nd.getVaiTro()));
        nd.setTrangThai(trangThai);
        nguoiDungRepository.save(nd);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công.");
        return "redirect:/admin/nhanVien";
    }

    @GetMapping("/xoa")
    public String deleteNhanVien(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        NguoiDung nd = nguoiDungRepository.findById(id).orElse(null);
        if (nd != null) {
            if (nd.getVaiTro().getId() == 3L) {
                redirectAttributes.addFlashAttribute("error", "Không thể khóa tài khoản Quản trị viên (Admin).");
            } else {
                if ("Hoạt động".equals(nd.getTrangThai())) {
                    nd.setTrangThai("Khóa");
                } else {
                    nd.setTrangThai("Hoạt động");
                }
                nguoiDungRepository.save(nd);
                redirectAttributes.addFlashAttribute("message", "Đã thay đổi trạng thái nhân viên.");
            }
        }
        return "redirect:/admin/nhanVien";
    }
}