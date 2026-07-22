package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/trangChu")
    public String home() {
        return "/trangChu";
    }

    @GetMapping("/dangNhap")
    public String dangNhap() {
        return "/auth/login";
    }

    @GetMapping("/dangKy")
    public String dangKy() {
        return "/auth/register";
    }

    @GetMapping("/quenMatKhau")
    public String quenMatKhau() {
        return "/auth/ForgetPassword";
    }

    @GetMapping("/thucDon")
    public String thucDon() {
        return "/thucDon";
    }

    @GetMapping("/chiTietMonAn")
    public String CTmonAn() {
        return "/chiTietMonAn";
    }

    @GetMapping("/gioHang")
    public String gioHang() {
        return "/gioHang";
    }

    @GetMapping("/thanhToan")
    public String thanhToan() {
        return "/thanhToan";
    }

    @GetMapping("/khuyenMai")
    public String khuyenMai() {
        return "/khuyenMai";
    }

    @GetMapping("/gioiThieu")
    public String gioiThieu() {
        return "/gioiThieu";
    }

    @GetMapping("/lienHe")
    public String lienHe() {
        return "/lienHe";
    }

}
