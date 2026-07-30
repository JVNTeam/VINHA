package com.example.vinha.controller.admin;

import com.example.vinha.entity.DonHang;
import com.example.vinha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserDonHangController {

    @Autowired
    private OrderService orderService;

    @GetMapping({"/admin/chitietdon", "/admin/chiTietDon"})
    public String xemChiTietDonHang(Model model) {
        List<DonHang> list = orderService.getAllOrders();
        if (list != null && !list.isEmpty()) {
            model.addAttribute("donHang", list.get(0));
        }
        return "admin/CTDonHang";
    }
}