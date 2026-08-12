package com.example.vinha.controller.customer;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.NguoiDungRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/quenMatKhau")
public class QuenMatKhauController {

    private final NguoiDungRepository nguoiDungRepository;

    public QuenMatKhauController(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @GetMapping
    public String showQuenMatKhauPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/trangChu"; // User is already logged in
        }
        return "auth/ForgetPassword";
    }

    @PostMapping("/guiMa")
    public String xacNhanTaiKhoan(
            @RequestParam("taiKhoan") String taiKhoan,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<NguoiDung> userOpt = nguoiDungRepository.findByEmailOrSoDienThoai(taiKhoan, taiKhoan);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tài khoản với Email hoặc Số điện thoại này.");
            return "redirect:/quenMatKhau";
        }

        NguoiDung user = userOpt.get();

        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save into session for 5 minutes (in memory)
        session.setAttribute("RESET_OTP", otp);
        session.setAttribute("RESET_USER_ID", user.getId());

        // Simulate sending email/SMS by printing to console
        System.out.println("=================================================");
        System.out.println(" YÊU CẦU ĐẶT LẠI MẬT KHẨU CHO: " + taiKhoan);
        System.out.println(" MÃ OTP CỦA BẠN LÀ: " + otp);
        System.out.println("=================================================");

        redirectAttributes.addFlashAttribute("successMessage", "Mã xác nhận (OTP) đã được gửi (Kiểm tra màn hình console).");
        session.setAttribute("SHOW_RESET_FORM", true);
        
        return "redirect:/quenMatKhau";
    }

    @PostMapping("/datLai")
    public String datLaiMatKhau(
            @RequestParam("otp") String otp,
            @RequestParam("matKhauMoi") String matKhauMoi,
            @RequestParam("xacNhanMatKhau") String xacNhanMatKhau,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String sessionOtp = (String) session.getAttribute("RESET_OTP");
        Long userId = (Long) session.getAttribute("RESET_USER_ID");

        if (sessionOtp == null || userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phiên thay đổi mật khẩu đã hết hạn hoặc không hợp lệ. Vui lòng thử lại.");
            return "redirect:/quenMatKhau";
        }

        if (!sessionOtp.equals(otp)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mã xác nhận OTP không chính xác.");
            session.setAttribute("SHOW_RESET_FORM", true); // Keep the reset form open
            return "redirect:/quenMatKhau";
        }

        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
            session.setAttribute("SHOW_RESET_FORM", true);
            return "redirect:/quenMatKhau";
        }

        // Update password
        NguoiDung user = nguoiDungRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setMatKhau(matKhauMoi);
            nguoiDungRepository.save(user);
        }

        // Clear session attributes
        session.removeAttribute("RESET_OTP");
        session.removeAttribute("RESET_USER_ID");
        session.removeAttribute("SHOW_RESET_FORM");

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
        return "redirect:/dangNhap";
    }
}
