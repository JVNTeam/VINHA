package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/trangChu")
    public String home(Model model) {
        model.addAttribute("activePage", "trang-chu");
        return "/trangChu";
    }

    @GetMapping("/thucDon")
    public String thucDon(Model model) {
        model.addAttribute("activePage", "thuc-don");
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
