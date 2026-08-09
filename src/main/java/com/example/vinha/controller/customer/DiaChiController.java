package com.example.vinha.controller.customer;

import com.example.vinha.entity.DiaChi;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.DiaChiRepository;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/diachi")
public class DiaChiController {
    
    private final DiaChiRepository diaChiRepository;
    private final NguoiDungRepository nguoiDungRepository;
    
    public DiaChiController(DiaChiRepository diaChiRepository, NguoiDungRepository nguoiDungRepository) {
        this.diaChiRepository = diaChiRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @GetMapping
    public String showAddressPage(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        NguoiDung sessionUser = (NguoiDung) loggedInUser;
        NguoiDung user = nguoiDungRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/dangNhap";
        }

        List<DiaChi> addresses = diaChiRepository.findByNguoiDungId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("activeMenu", "address"); 

        return "customer/diaChi"; 
    }
    
    @PostMapping("/them")
    public String themDiaChi(
            @RequestParam("tenNguoiNhan") String tenNguoiNhan,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("diaChi") String diaChi,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
            
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        NguoiDung user = (NguoiDung) loggedInUser;
        List<DiaChi> addresses = diaChiRepository.findByNguoiDungId(user.getId());
        
        DiaChi newAddress = new DiaChi();
        newAddress.setNguoiDung(user);
        newAddress.setTenNguoiNhan(tenNguoiNhan);
        newAddress.setSdtNguoiNhan(soDienThoai);
        newAddress.setDiaChi(diaChi);
        newAddress.setMacDinh(addresses.isEmpty()); // If it is the first address, make it default
        
        diaChiRepository.save(newAddress);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm địa chỉ thành công!");
        
        return "redirect:/diachi";
    }
    
    @PostMapping("/xoa")
    public String xoaDiaChi(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        NguoiDung user = (NguoiDung) loggedInUser;
        DiaChi diaChi = diaChiRepository.findById(id).orElse(null);
        if (diaChi != null && diaChi.getNguoiDung().getId().equals(user.getId())) {
            diaChiRepository.delete(diaChi);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa địa chỉ thành công!");
        }
        
        return "redirect:/diachi";
    }
    
    @PostMapping("/macdinh")
    public String macDinhDiaChi(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/dangNhap";
        }
        
        NguoiDung user = (NguoiDung) loggedInUser;
        List<DiaChi> addresses = diaChiRepository.findByNguoiDungId(user.getId());
        
        for (DiaChi addr : addresses) {
            if (addr.getId().equals(id)) {
                addr.setMacDinh(true);
            } else {
                addr.setMacDinh(false);
            }
        }
        
        diaChiRepository.saveAll(addresses);
        redirectAttributes.addFlashAttribute("successMessage", "Đã đặt làm mặc định!");
        
        return "redirect:/diachi";
    }
}
