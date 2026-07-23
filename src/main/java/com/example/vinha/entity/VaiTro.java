package com.example.vinha.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vai_tro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten", nullable = false, unique = true, length = 50)
    private String ten;

    @OneToMany(mappedBy = "vaiTro")
    private List<NguoiDung> nguoiDungs;
}
