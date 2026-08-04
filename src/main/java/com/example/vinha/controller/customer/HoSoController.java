package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class HoSoController {

    @GetMapping({"/hoso", "/hoSo"})
    public String viewHoSo(HttpSession session, Model model) {
        return "customer/hoSo";
    }
}
