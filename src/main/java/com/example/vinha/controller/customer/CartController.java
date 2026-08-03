package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    private final CartService cartService;
    private final com.example.vinha.repository.MonAnRepository monAnRepository;

    public CartController(CartService cartService, com.example.vinha.repository.MonAnRepository monAnRepository) {
        this.cartService = cartService;
        this.monAnRepository = monAnRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getOrCreateGuestCart(HttpSession session) {
        Object guestCartObj = session.getAttribute("guestCart");
        if (guestCartObj instanceof Map<?, ?> mapObj) {
            return (Map<Long, Integer>) mapObj;
        }
        Map<Long, Integer> guestCart = new HashMap<>();
        session.setAttribute("guestCart", guestCart);
        return guestCart;
    }

    @PostMapping("/gioHang/them")
    public String themVaoGioHang(
            @RequestParam("monAnId") Long monAnId,
            @RequestParam(value = "soLuong", defaultValue = "1") Integer soLuong,
            HttpSession session
    ) {
        int soLuongHopLe = (soLuong == null || soLuong < 1) ? 1 : soLuong;
        Object userObj = session.getAttribute("loggedInUser");

        if (userObj instanceof NguoiDung nguoiDung) {
            cartService.addToCart(nguoiDung.getId(), monAnId, soLuongHopLe);
        } else {
            Map<Long, Integer> guestCart = getOrCreateGuestCart(session);
            com.example.vinha.entity.MonAn monAn = monAnRepository.findById(monAnId).orElse(null);
            if (monAn != null) {
                int maxQ = monAn.getSoLuongCon() != null ? monAn.getSoLuongCon() : 0;
                int currentQ = guestCart.getOrDefault(monAnId, 0);
                int newQ = currentQ + soLuongHopLe;
                if (newQ > maxQ) {
                    newQ = maxQ;
                }
                guestCart.put(monAnId, newQ);
                session.setAttribute("guestCart", guestCart);
            }
        }

        return "redirect:/gioHang";
    }

    @PostMapping("/gioHang/capNhatSoLuong")
    public String capNhatSoLuong(
            @RequestParam("chiTietGioHangId") Long chiTietGioHangId,
            @RequestParam("soLuong") Integer soLuong,
            HttpSession session
    ) {
        Object userObj = session.getAttribute("loggedInUser");

        if (userObj instanceof NguoiDung nguoiDung) {
            cartService.capNhatSoLuong(nguoiDung.getId(), chiTietGioHangId, soLuong);
        } else {
            Map<Long, Integer> guestCart = getOrCreateGuestCart(session);
            int soLuongHopLe = (soLuong == null || soLuong < 1) ? 1 : soLuong;
            
            com.example.vinha.entity.MonAn monAn = monAnRepository.findById(chiTietGioHangId).orElse(null);
            if (monAn != null) {
                int maxQ = monAn.getSoLuongCon() != null ? monAn.getSoLuongCon() : 0;
                if (soLuongHopLe > maxQ) {
                    soLuongHopLe = maxQ;
                }
                guestCart.put(chiTietGioHangId, soLuongHopLe);
                session.setAttribute("guestCart", guestCart);
            }
        }

        return "redirect:/gioHang";
    }

    @PostMapping("/gioHang/xoa")
    public String xoaMonKhoiGio(
            @RequestParam("chiTietGioHangId") Long chiTietGioHangId,
            HttpSession session
    ) {
        Object userObj = session.getAttribute("loggedInUser");

        if (userObj instanceof NguoiDung nguoiDung) {
            cartService.xoaMonKhoiGio(nguoiDung.getId(), chiTietGioHangId);
        } else {
            Map<Long, Integer> guestCart = getOrCreateGuestCart(session);
            guestCart.remove(chiTietGioHangId);
            session.setAttribute("guestCart", guestCart);
        }

        return "redirect:/gioHang";
    }
}
