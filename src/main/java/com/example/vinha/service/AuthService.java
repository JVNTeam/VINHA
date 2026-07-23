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
     * @return Optional<User> nếu đăng nhập thành công, empty nếu thất bại
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
        if (!"Hoạt Động".equals(user.getTrangThai())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public Optional<String> register(String hoTen, String email, String soDienThoai, String matKhau, String xacNhanMatKhau, Byte gioiTinh) {
        if (hoTen == null || hoTen.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || soDienThoai == null || soDienThoai.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()
                || xacNhanMatKhau == null || xacNhanMatKhau.trim().isEmpty()) {
            return Optional.of("Vui lòng nhập đầy đủ thông tin bắt buộc.");
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            return Optional.of("Mật khẩu xác nhận không khớp.");
        }

        if (userRepository.existsByEmail(email.trim())) {
            return Optional.of("Email đã được sử dụng.");
        }

        if (userRepository.existsBySoDienThoai(soDienThoai.trim())) {
            return Optional.of("Số điện thoại đã được sử dụng.");
        }

        Optional<VaiTro> vaiTroOpt = vaiTroRepository.findByTen("Khách hàng");
        if (vaiTroOpt.isEmpty()) {
            return Optional.of("Không tìm thấy vai trò mặc định Khách hàng.");
        }

        LocalDateTime now = LocalDateTime.now();

        NguoiDung newUser = NguoiDung.builder()
                .vaiTro(vaiTroOpt.get())
                .hoTen(hoTen.trim())
                .email(email.trim())
                .soDienThoai(soDienThoai.trim())
                .cccd("")
                .matKhau(matKhau)
                .gioiTinh(gioiTinh)
                .trangThai("Hoạt Động")
                .ngayTao(now)
                .ngayCapNhat(now)
                .build();

        userRepository.save(newUser);
        return Optional.empty();
    }
}

