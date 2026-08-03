package com.example.vinha.controller.customer;

import com.example.vinha.entity.ChiTietGioHang;
import com.example.vinha.entity.GioHang;
import com.example.vinha.entity.MaGiamGia;
import com.example.vinha.entity.MonAn;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietGioHangRepository;
import com.example.vinha.repository.GioHangRepository;
import com.example.vinha.repository.MonAnRepository;
import com.example.vinha.service.FoodService;
import com.example.vinha.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final FoodService foodService;
    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final VoucherService voucherService;
    private final MonAnRepository monAnRepository;

    public HomeController(
            FoodService foodService,
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            VoucherService voucherService,
            MonAnRepository monAnRepository
    ) {
        this.foodService = foodService;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.voucherService = voucherService;
        this.monAnRepository = monAnRepository;
    }

    @GetMapping("/trangChu")
    public String home(Model model) {
        model.addAttribute("activePage", "trang-chu");
        
        // Pass real data instead of fake data
        List<com.example.vinha.entity.DanhMuc> danhMucs = foodService.getDanhSachDanhMuc();
        // Get top 4 or 6 danh muc to display
        model.addAttribute("danhMucs", danhMucs.size() > 6 ? danhMucs.subList(0, 6) : danhMucs);
        
        // Get top best seller dishes
        List<MonAn> allDishes = foodService.getDanhSachMonAnHienThi();
        List<MonAn> topDishes = new ArrayList<>(allDishes);
        topDishes.sort((a, b) -> {
            int aSold = a.getDaBan() != null ? a.getDaBan() : 0;
            int bSold = b.getDaBan() != null ? b.getDaBan() : 0;
            return Integer.compare(bSold, aSold);
        });
        model.addAttribute("topDishes", topDishes.size() > 4 ? topDishes.subList(0, 4) : topDishes);
        
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

    @SuppressWarnings("unchecked")
    private List<ChiTietGioHang> convertGuestCartToItems(Map<Long, Integer> guestCart) {
        if (guestCart == null || guestCart.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChiTietGioHang> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : guestCart.entrySet()) {
            Long monAnId = entry.getKey();
            Integer soLuong = entry.getValue();
            if (monAnId == null || soLuong == null || soLuong < 1) {
                continue;
            }

            MonAn monAn = monAnRepository.findById(monAnId).orElse(null);
            if (monAn == null) {
                continue;
            }

            BigDecimal donGia = monAn.getGia() != null ? monAn.getGia() : BigDecimal.ZERO;
            ChiTietGioHang item = ChiTietGioHang.builder()
                    .id(monAnId)
                    .monAn(monAn)
                    .soLuong(soLuong)
                    .donGia(donGia)
                    .build();
            items.add(item);
        }
        return items;
    }

    @GetMapping("/gioHang")
    public String gioHang(HttpSession session, Model model) {
        Object userObj = session.getAttribute("loggedInUser");
        List<ChiTietGioHang> cartItems;

        if (userObj instanceof NguoiDung nguoiDung) {
            cartItems = gioHangRepository.findByNguoiDungId(nguoiDung.getId())
                    .map(GioHang::getId)
                    .map(chiTietGioHangRepository::findByGioHangId)
                    .orElse(Collections.emptyList());
        } else {
            Object guestCartObj = session.getAttribute("guestCart");
            Map<Long, Integer> guestCart = (guestCartObj instanceof Map<?, ?>)
                    ? (Map<Long, Integer>) guestCartObj
                    : Collections.emptyMap();
            cartItems = convertGuestCartToItems(guestCart);
        }

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
    public String thanhToan(HttpSession session) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung)) {
            return "redirect:/dangNhap?returnUrl=/thanhToan";
        }
        return "/thanhToan";
    }

    @GetMapping("/khuyenMai")
    public String khuyenMai(Model model) {
        model.addAttribute("activePage", "khuyen-mai");
        model.addAttribute("vouchers", voucherService.layMaDangHoatDong());
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
