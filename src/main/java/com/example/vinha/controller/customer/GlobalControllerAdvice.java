package com.example.vinha.controller.customer;

import com.example.vinha.entity.ChiTietGioHang;
import com.example.vinha.entity.GioHang;
import com.example.vinha.entity.NguoiDung;
import com.example.vinha.repository.ChiTietGioHangRepository;
import com.example.vinha.repository.GioHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;

    public GlobalControllerAdvice(
            GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository
    ) {
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("cartItemCount")
    public int populateCartItemCount(HttpSession session) {
        Object userObj = session.getAttribute("loggedInUser");

        if (userObj instanceof NguoiDung nguoiDung) {
            return gioHangRepository.findByNguoiDungId(nguoiDung.getId())
                    .map(GioHang::getId)
                    .map(chiTietGioHangRepository::findByGioHangId)
                    .stream()
                    .flatMap(List::stream)
                    .mapToInt(ChiTietGioHang::getSoLuong)
                    .sum();
        }

        Object guestCartObj = session.getAttribute("guestCart");
        if (guestCartObj instanceof Map<?, ?> guestCart) {
            return guestCart.values().stream()
                    .filter(Objects::nonNull)
                    .mapToInt(value -> {
                        try {
                            return Integer.parseInt(value.toString());
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .sum();
        }

        return 0;
    }
}
