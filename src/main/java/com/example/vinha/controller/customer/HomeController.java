package com.example.vinha.controller.customer;

import com.example.vinha.entity.ChiTietGioHang;
import com.example.vinha.entity.DanhGia;
import com.example.vinha.entity.DiaChi;
import com.example.vinha.entity.GioHang;
import com.example.vinha.entity.MaGiamGia;
import com.example.vinha.entity.MonAn;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.entity.NguoiDungMaGiamGia;
import com.example.vinha.repository.ChiTietGioHangRepository;
import com.example.vinha.repository.DanhGiaRepository;
import com.example.vinha.repository.DiaChiRepository;
import com.example.vinha.repository.GioHangRepository;
import com.example.vinha.repository.MonAnRepository;
import com.example.vinha.repository.NguoiDungMaGiamGiaRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class HomeController {

    private final FoodService foodService;
    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final VoucherService voucherService;
    private final MonAnRepository monAnRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final DiaChiRepository diaChiRepository;
    private final NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository;

    public HomeController(
            FoodService foodService,
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            VoucherService voucherService,
            MonAnRepository monAnRepository,
            DanhGiaRepository danhGiaRepository,
            DiaChiRepository diaChiRepository,
            NguoiDungMaGiamGiaRepository nguoiDungMaGiamGiaRepository
    ) {
        this.foodService = foodService;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.voucherService = voucherService;
        this.monAnRepository = monAnRepository;
        this.danhGiaRepository = danhGiaRepository;
        this.diaChiRepository = diaChiRepository;
        this.nguoiDungMaGiamGiaRepository = nguoiDungMaGiamGiaRepository;
    }

    @GetMapping("/trangChu")
    public String home(Model model) {
        model.addAttribute("activePage", "trang-chu");

        List<com.example.vinha.entity.DanhMuc> danhMucs = foodService.getDanhSachDanhMuc();
        model.addAttribute("danhMucs", danhMucs.size() > 6 ? danhMucs.subList(0, 6) : danhMucs);

        List<MonAn> allDishes = foodService.getDanhSachMonAnHienThi();
        List<MonAn> topDishes = new ArrayList<>(allDishes);
        topDishes.sort((a, b) -> {
            int aSold = a.getDaBan() != null ? a.getDaBan() : 0;
            int bSold = b.getDaBan() != null ? b.getDaBan() : 0;
            return Integer.compare(bSold, aSold);
        });
        model.addAttribute("topDishes", topDishes.size() > 4 ? topDishes.subList(0, 4) : topDishes);

        List<DanhGia> latestReviews = danhGiaRepository.findTop3ByOrderByNgayTaoDesc();
        model.addAttribute("latestReviews", latestReviews);

        return "trangChu";
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

        List<Map<String, Object>> menuItemsWithRatings = new ArrayList<>();
        for (MonAn mon : danhSachMonAn) {
            List<DanhGia> reviews = danhGiaRepository.findByMonAnId(mon.getId());
            BigDecimal avgRating = BigDecimal.ZERO;
            int reviewCount = reviews.size();
            if (reviewCount > 0) {
                avgRating = reviews.stream()
                        .map(DanhGia::getSoSao)
                        .filter(Objects::nonNull)
                        .map(BigDecimal::valueOf)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(reviewCount), 1, java.math.RoundingMode.HALF_UP);
            }
            Map<String, Object> item = new HashMap<>();
            item.put("mon", mon);
            item.put("avgRating", avgRating);
            item.put("reviewCount", reviewCount);
            menuItemsWithRatings.add(item);
        }

        model.addAttribute("danhSachMonAn", danhSachMonAn);
        model.addAttribute("menuItemsWithRatings", menuItemsWithRatings);
        model.addAttribute("danhSachDanhMuc", foodService.getDanhSachDanhMuc());
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("danhMucId", danhMucId);
        model.addAttribute("giaTu", giaTu);
        model.addAttribute("giaDen", giaDen);
        model.addAttribute("sapXepBan", sapXepBan);

        return "thucDon";
    }

    @GetMapping("/chiTietMonAn")
    public String CTmonAn(@RequestParam(value = "id", required = false) Long id, Model model) {
        // Neu khong truyen ID, chuyen huong ve trang thuc don
        if (id == null) {
            return "redirect:/thucDon";
        }

        model.addAttribute("activePage", "thuc-don");
        MonAn monAn = foodService.getMonAnById(id).orElse(null);

        // Neu ID khong ton tai trong DB, chuyen huong ve thuc don
        if (monAn == null) {
            return "redirect:/thucDon";
        }

        model.addAttribute("mon", monAn);

        List<MonAn> relatedDishes = new ArrayList<>();
        List<MonAn> allDishes = foodService.getDanhSachMonAnHienThi();

        relatedDishes = allDishes.stream()
                .filter(item -> item != null && !Objects.equals(item.getId(), id))
                .filter(item -> monAn.getDanhMuc() != null && item.getDanhMuc() != null
                        && Objects.equals(item.getDanhMuc().getId(), monAn.getDanhMuc().getId()))
                .limit(4)
                .toList();

        if (relatedDishes.isEmpty()) {
            relatedDishes = allDishes.stream()
                    .filter(item -> item != null && !Objects.equals(item.getId(), id))
                    .limit(4)
                    .toList();
        }

        List<DanhGia> reviews = danhGiaRepository.findByMonAnId(id);
        model.addAttribute("relatedDishes", relatedDishes);
        model.addAttribute("reviews", reviews);

        return "chiTietMonAn";
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

        return "gioHang";
    }

    @GetMapping("/thanhToan")
    public String thanhToan(HttpSession session, Model model) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung user)) {
            return "redirect:/dangNhap?returnUrl=/thanhToan";
        }

        List<ChiTietGioHang> cartItems = gioHangRepository.findByNguoiDungId(user.getId())
                .map(GioHang::getId)
                .map(chiTietGioHangRepository::findByGioHangId)
                .orElse(Collections.emptyList());

        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itemCount = cartItems.stream()
                .mapToInt(ChiTietGioHang::getSoLuong)
                .sum();

        List<DiaChi> addresses = diaChiRepository.findByNguoiDungId(user.getId());
        DiaChi defaultAddress = addresses.stream()
                .filter(Objects::nonNull)
                .filter(DiaChi::getMacDinh)
                .findFirst()
                .orElseGet(() -> addresses.stream().filter(Objects::nonNull).findFirst().orElse(null));

        String customerName = user.getHoTen() != null ? user.getHoTen() : "";
        String customerPhone = user.getSoDienThoai() != null ? user.getSoDienThoai() : "";
        String customerAddress = defaultAddress != null && defaultAddress.getDiaChi() != null
                ? defaultAddress.getDiaChi()
                : "";
        String customerProvince = "";
        if (customerAddress.contains("Hà Nội")) {
            customerProvince = "Hà Nội";
        } else if (customerAddress.contains("Hồ Chí Minh")) {
            customerProvince = "Hồ Chí Minh";
        } else if (customerAddress.contains("Đà Nẵng")) {
            customerProvince = "Đà Nẵng";
        } else if (customerAddress.contains("Hải Phòng")) {
            customerProvince = "Hải Phòng";
        } else if (customerAddress.contains("Cần Thơ")) {
            customerProvince = "Cần Thơ";
        }

        List<MaGiamGia> availableVouchers = voucherService.layMaDangHoatDong();
        List<MaGiamGia> userVouchers = nguoiDungMaGiamGiaRepository.findByNguoiDungId(user.getId()).stream()
                .map(NguoiDungMaGiamGia::getMaGiamGia)
                .filter(Objects::nonNull)
                .filter(v -> Boolean.TRUE.equals(v.getTrangThai()))
                .filter(v -> v.getSoLuong() == null || v.getSoLuong() > 0)
                .toList();

        List<MaGiamGia> allVouchers = new ArrayList<>();
        allVouchers.addAll(userVouchers);
        for (MaGiamGia voucher : availableVouchers) {
            if (allVouchers.stream().noneMatch(v -> Objects.equals(v.getId(), voucher.getId()))) {
                allVouchers.add(voucher);
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerPhone", customerPhone);
        model.addAttribute("customerAddress", customerAddress);
        model.addAttribute("customerProvince", customerProvince);
        model.addAttribute("vouchers", allVouchers);

        return "thanhToan";
    }

    @GetMapping("/khuyenMai")
    public String khuyenMai(Model model) {
        model.addAttribute("activePage", "khuyen-mai");
        model.addAttribute("vouchers", voucherService.layMaDangHoatDong());
        return "khuyenMai";
    }

    @GetMapping("/gioiThieu")
    public String gioiThieu(Model model) {
        model.addAttribute("activePage", "gioi-thieu");
        return "gioiThieu";
    }

    @GetMapping("/lienHe")
    public String lienHe(Model model) {
        model.addAttribute("activePage", "lien-he");
        return "lienHe";
    }

}