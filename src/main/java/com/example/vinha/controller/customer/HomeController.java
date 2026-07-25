package com.example.vinha.controller.customer;

import com.example.vinha.entity.ChiTietGioHang;
import com.example.vinha.entity.GioHang;
import com.example.vinha.entity.MonAn;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietGioHangRepository;
import com.example.vinha.repository.GioHangRepository;
import com.example.vinha.service.FoodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final FoodService foodService;
    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;

    public HomeController(
            FoodService foodService,
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository
    ) {
        this.foodService = foodService;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
    }

    @GetMapping("/trangChu")
    public String home(Model model) {
        model.addAttribute("activePage", "trang-chu");
        return "/trangChu";
    }

    @GetMapping("/thucDon")
    public String thucDon(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "danhMucId", required = false) Long danhMucId,
            @RequestParam(value = "giaTu", required = false) BigDecimal giaTu,
            @RequestParam(value = "giaDen", required = false) BigDecimal giaDen,
            @RequestParam(value = "sapXepBan", required = false) String sapXepBan,
            Model model
    ) {
        model.addAttribute("activePage", "thuc-don");

        boolean hasFilter =
                (keyword != null && !keyword.trim().isEmpty()) ||
                (trangThai != null && !trangThai.trim().isEmpty()) ||
                danhMucId != null ||
                giaTu != null ||
                giaDen != null ||
                (sapXepBan != null && !sapXepBan.trim().isEmpty());

        List<MonAn> danhSachMonAn = hasFilter
                ? foodService.searchMonAn(keyword, trangThai, danhMucId, giaTu, giaDen, sapXepBan)
                : foodService.getDanhSachMonAnHienThi();

        model.addAttribute("danhSachMonAn", danhSachMonAn);
        model.addAttribute("danhSachDanhMuc", foodService.getDanhSachDanhMuc());
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("danhMucId", danhMucId);
        model.addAttribute("giaTu", giaTu);
        model.addAttribute("giaDen", giaDen);
        model.addAttribute("sapXepBan", sapXepBan);

        return "/thucDon";
    }

    @GetMapping("/chiTietMonAn")
    public String CTmonAn(@RequestParam("id") Long id, Model model) {
        model.addAttribute("activePage", "thuc-don");
        MonAn monAn = foodService.getMonAnById(id).orElse(null);
        model.addAttribute("mon", monAn);
        return "/chiTietMonAn";
    }

    @GetMapping("/gioHang")
    public String gioHang(HttpSession session, Model model) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung)) {
            return "redirect:/dangNhap";
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        List<ChiTietGioHang> cartItems = gioHangRepository.findByNguoiDungId(nguoiDung.getId())
                .map(GioHang::getId)
                .map(chiTietGioHangRepository::findByGioHangId)
                .orElse(Collections.emptyList());

        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itemCount = cartItems.stream()
                .mapToInt(ChiTietGioHang::getSoLuong)
                .sum();

        model.addAttribute("activePage", "gio-hang");
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("itemCount", itemCount);

        return "/gioHang";
    }

    @GetMapping("/thanhToan")
    public String thanhToan() {
        return "/thanhToan";
    }

    @GetMapping("/khuyenMai")
    public String khuyenMai(Model model) {
        model.addAttribute("activePage", "khuyen-mai");
        return "/khuyenMai";
    }

    @GetMapping("/gioiThieu")
    public String gioiThieu(Model model) {
        model.addAttribute("activePage", "gioi-thieu");
        return "/gioiThieu";
    }

    @GetMapping("/lienHe")
    public String lienHe(Model model) {
        model.addAttribute("activePage", "lien-he");
        return "/lienHe";
    }

}
