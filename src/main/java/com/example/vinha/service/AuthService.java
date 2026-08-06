package com.example.vinha.service;

import com.example.vinha.entity.NguoiDung;
import com.example.vinha.entity.VaiTro;
import com.example.vinha.repository.NguoiDungRepository;
import com.example.vinha.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private NguoiDungRepository userRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    /**
     * Xác thực đăng nhập
     * @param username Email hoặc số điện thoại
     * @param password Mật khẩu plain text
     * @return Optional<NguoiDung> nếu đăng nhập thành công, empty nếu thất bại
     */
    public Optional<NguoiDung> authenticate(String username, String password) {
        // Tìm user theo email hoặc số điện thoại
        Optional<NguoiDung> userOpt = userRepository.findByEmailOrSoDienThoai(username, username);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        NguoiDung user = userOpt.get();

        // Kiểm tra mật khẩu (plain text)
        if (!user.getMatKhau().equals(password)) {
            return Optional.empty();
        }

        // Kiểm tra trạng thái tài khoản
        if (!"Hoạt động".equals(user.getTrangThai())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    /**
     * Xử lý đăng ký tài khoản mới với Contact gộp (Email hoặc SĐT)
     */
    public Optional<String> register(String hoTen, String contact, String matKhau, String xacNhanMatKhau, Integer gioiTinh) {
        if (hoTen == null || hoTen.trim().isEmpty()
                || contact == null || contact.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()
                || xacNhanMatKhau == null || xacNhanMatKhau.trim().isEmpty()) {
            return Optional.of("Vui lòng nhập đầy đủ thông tin bắt buộc.");
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            return Optional.of("Mật khẩu xác nhận không khớp.");
        }

        String trimmedContact = contact.trim();
        String email = null;
        String soDienThoai = null;

        // Regex phân loại Contact
        boolean isEmail = trimmedContact.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        boolean isPhone = trimmedContact.matches("(84|0[3|5|7|8|9])+([0-9]{8})\\b");

        if (isEmail) {
            email = trimmedContact;
            if (userRepository.existsByEmail(email)) {
                return Optional.of("Email đã được sử dụng.");
            }
        } else if (isPhone) {
            soDienThoai = trimmedContact;
            if (userRepository.existsBySoDienThoai(soDienThoai)) {
                return Optional.of("Số điện thoại đã được sử dụng.");
            }
        } else {
            return Optional.of("Email hoặc Số điện thoại không đúng định dạng.");
        }

        Optional<VaiTro> vaiTroOpt = vaiTroRepository.findById(1L); // 1L is Khách hàng
        if (vaiTroOpt.isEmpty()) {
            return Optional.of("Không tìm thấy vai trò mặc định (ID 1).");
        }

        LocalDateTime now = LocalDateTime.now();

        // Chuyển Integer gioiTinh sang Byte
        Byte gioiTinhByte = (gioiTinh != null) ? gioiTinh.byteValue() : null;

        // Nếu soDienThoai bị trống (do đăng ký bằng email), tạo sđt giả để thỏa mãn NOT NULL và UNIQUE
        if (soDienThoai == null) {
            soDienThoai = "NO_PHONE_" + System.currentTimeMillis();
        }

        NguoiDung newUser = NguoiDung.builder()
                .vaiTro(vaiTroOpt.get())
                .hoTen(hoTen.trim())
                .email(email)
                .soDienThoai(soDienThoai)
                .cccd("")
                .matKhau(matKhau)
                .gioiTinh(gioiTinhByte)
                .trangThai("Hoạt động") // Đồng bộ với DB
                .ngayTao(now)
                .ngayCapNhat(now)
                .build();

        userRepository.save(newUser);
        return Optional.empty();
    }
}