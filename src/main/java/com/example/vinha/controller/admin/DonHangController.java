package com.example.vinha.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DonHangController {

    // Trang danh sách đơn hàng
    @GetMapping("/admin/donHang")
    public String hienThiDonHang() {
        return "admin/donHang";
    }

    // Trang chi tiết đơn hàng (Link mà mình vừa gắn vào nút bấm)
    @GetMapping("/admin/chiTietDon")
    public String hienThiChiTietDon() {
        // Tên file của bác đang đặt là chi-tiet-don-khach.html
        // (Hoặc nếu bác đổi tên file rồi thì nhớ update chuỗi return này cho khớp)
        return "admin/ChiTietDonHang";
    }
}