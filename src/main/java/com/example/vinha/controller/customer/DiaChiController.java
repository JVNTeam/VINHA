package com.example.vinha.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dia-chi")
public class DiaChiController {
    @GetMapping
    public String showAddressPage(Model model) {
        // Giả lập dữ liệu người dùng (Sau này có thể gọi từ User/Address Service)
        Map<String, String> user = new HashMap<>();
        user.put("name", "Nguyễn Thu Hà");
        user.put("email", "thuha.nguyen@email.com");
        user.put("avatar", "/images/avatar-default.jpg");

        // Giả lập danh sách địa chỉ
        List<Map<String, Object>> addresses = new ArrayList<>();

        Map<String, Object> addr1 = new HashMap<>();
        addr1.put("id", 1);
        addr1.put("name", "Nguyễn Thu Hà");
        addr1.put("phone", "090 123 4567");
        addr1.put("detail", "123 Đường Lê Lợi, Phường Bến Thành, Quận 1, TP. HCM");
        addr1.put("isDefault", true);
        addresses.add(addr1);

        Map<String, Object> addr2 = new HashMap<>();
        addr2.put("id", 2);
        addr2.put("name", "Nguyễn Thu Hà");
        addr2.put("phone", "090 123 4567");
        addr2.put("detail", "456 Cách Mạng Tháng Tám, Quận 3, TP. HCM");
        addr2.put("isDefault", false);
        addresses.add(addr2);

        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("activeMenu", "address"); // Để active sidebar menu

        return "customer/diaChi"; // Trỏ đến file HTML trong templates/customer/
    }
}
