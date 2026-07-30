package com.example.vinha.dto;

import com.example.vinha.entity.MaGiamGia;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MaGiamGiaView {
    private MaGiamGia voucher;
    private long daNhan;
}
