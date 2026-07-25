package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/gioHang/them")
    public String themVaoGioHang(
            @RequestParam("monAnId") Long monAnId,
            @RequestParam(value = "soLuong", defaultValue = "1") Integer soLuong,
            HttpSession session
    ) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung)) {
            return "redirect:/dangNhap";
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        cartService.addToCart(nguoiDung.getId(), monAnId, soLuong);

        return "redirect:/gioHang";
    }

    @PostMapping("/gioHang/capNhatSoLuong")
    public String capNhatSoLuong(
            @RequestParam("chiTietGioHangId") Long chiTietGioHangId,
            @RequestParam("soLuong") Integer soLuong,
            HttpSession session
    ) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung)) {
            return "redirect:/dangNhap";
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        cartService.capNhatSoLuong(nguoiDung.getId(), chiTietGioHangId, soLuong);

        return "redirect:/gioHang";
    }

    @PostMapping("/gioHang/xoa")
    public String xoaMonKhoiGio(
            @RequestParam("chiTietGioHangId") Long chiTietGioHangId,
            HttpSession session
    ) {
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof NguoiDung)) {
            return "redirect:/dangNhap";
        }

        NguoiDung nguoiDung = (NguoiDung) userObj;
        cartService.xoaMonKhoiGio(nguoiDung.getId(), chiTietGioHangId);

        return "redirect:/gioHang";
    }
}
