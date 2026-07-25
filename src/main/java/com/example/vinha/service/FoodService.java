package com.example.vinha.service;

import com.example.vinha.entity.DanhMuc;
import com.example.vinha.entity.MonAn;
import com.example.vinha.repository.DanhMucRepository;
import com.example.vinha.repository.MonAnRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final MonAnRepository monAnRepository;
    private final DanhMucRepository danhMucRepository;

    public FoodService(MonAnRepository monAnRepository, DanhMucRepository danhMucRepository) {
        this.monAnRepository = monAnRepository;
        this.danhMucRepository = danhMucRepository;
    }

    public List<MonAn> getDanhSachMonAnHienThi() {
        List<String> trangThaiUuTien = Arrays.asList("HOAT_DONG", "ACTIVE", "ĐANG_BÁN", "Đang bán");
        for (String trangThai : trangThaiUuTien) {
            List<MonAn> monAns = monAnRepository.findByTrangThaiWithHinhAnh(trangThai);
            if (monAns != null && !monAns.isEmpty()) {
                return monAns;
            }
        }
        return monAnRepository.findAllWithHinhAnh();
    }

    public Optional<MonAn> getMonAnById(Long id) {
        return monAnRepository.findByIdWithHinhAnh(id);
    }

    public List<MonAn> searchMonAn(String keyword, String trangThai, Long danhMucId) {
        return monAnRepository.searchWithFilters(keyword, trangThai, danhMucId, null, null, null);
    }

    public List<MonAn> searchMonAn(String keyword,
            String trangThai,
            Long danhMucId,
            BigDecimal giaTu,
            BigDecimal giaDen,
            String sapXepBan) {

        List<MonAn> ds = monAnRepository.searchWithFilters(
                keyword,
                trangThai,
                danhMucId,
                giaTu,
                giaDen,
                sapXepBan);

        if ("BAN_NHIEU_NHAT".equals(sapXepBan)) {
            ds.sort(Comparator.comparing(MonAn::getDaBan).reversed());
        } else if ("BAN_IT_NHAT".equals(sapXepBan)) {
            ds.sort(Comparator.comparing(MonAn::getDaBan));
        }

        return ds;
    }

    public List<DanhMuc> getDanhSachDanhMuc() {
        return danhMucRepository.findAll();
    }
}
