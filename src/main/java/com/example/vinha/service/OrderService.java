package com.example.vinha.service;

import com.example.vinha.entity.DonHang;
import com.example.vinha.entity.LichSuTrangThai;
import com.example.vinha.repository.DonHangRepository;
import com.example.vinha.repository.LichSuTrangThaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private LichSuTrangThaiRepository lichSuTrangThaiRepository;

    public List<DonHang> getAllOrders() {
        return donHangRepository.findAllByOrderByNgayTaoDesc();
    }

    public DonHang getOrderById(Long id) {
        return donHangRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        DonHang order = getOrderById(id);
        if (order != null) {
            order.setTrangThai(status);
            donHangRepository.save(order);

            // Lưu vào lịch sử trạng thái
            LichSuTrangThai history = new LichSuTrangThai();
            history.setDonHang(order);
            history.setTrangThai(status);
            history.setThoiGian(LocalDateTime.now());
            lichSuTrangThaiRepository.save(history);
        }
    }
}


