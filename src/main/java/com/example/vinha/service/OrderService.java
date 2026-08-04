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

    public org.springframework.data.domain.Page<DonHang> searchOrders(String keyword, String status, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "ngayTao"));
        return donHangRepository.searchOrders(keyword, status, pageable);
    }

    public long countByStatus(String status) {
        return donHangRepository.countByTrangThai(status);
    }

    public Double sumDoanhThu() {
        return donHangRepository.sumDoanhThu();
    }

    @Transactional
    public void updateStatus(Long id, String status, String lyDoHuy, String thongTinShipper) {
        DonHang order = getOrderById(id);
        if (order != null) {
            order.setTrangThai(status);
            if (lyDoHuy != null && !lyDoHuy.trim().isEmpty()) {
                order.setLyDoHuy(lyDoHuy);
            }
            if (thongTinShipper != null && !thongTinShipper.trim().isEmpty()) {
                order.setThongTinShipper(thongTinShipper);
            }
            donHangRepository.save(order);

            // Lưu vào lịch sử trạng thái
            LichSuTrangThai history = new LichSuTrangThai();
            history.setDonHang(order);
            history.setTrangThai(status);
            history.setThoiGian(LocalDateTime.now());
            lichSuTrangThaiRepository.save(history);
        }
    }
    
    // Giữ lại hàm cũ để tránh lỗi các nơi gọi khác
    @Transactional
    public void updateStatus(Long id, String status) {
        updateStatus(id, status, null, null);
    }
}


