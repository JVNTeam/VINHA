package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HoSoController {

    @GetMapping("/hoso")
    public String hoso() {
        return "customer/hoSo";
    }
}
