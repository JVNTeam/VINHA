-- TEN WEB LA: Vá»Š NHÃ€
CREATE DATABASE ViNha;
GO
USE ViNha;
GO

CREATE TABLE vai_tro (
                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                         ten NVARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE nguoi_dung (
                            id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            vai_tro_id BIGINT NOT NULL,
                            ho_ten NVARCHAR(100) NOT NULL,
    /*
        Email dÃ¹ng Ä‘á»ƒ Ä‘Äƒng nháº­p hoáº·c nháº­n thÃ´ng bÃ¡o.
        KhÃ´ng báº¯t buá»™c.
        Náº¿u cÃ³ thÃ¬ pháº£i duy nháº¥t.
    */
                            email NVARCHAR(100) UNIQUE NULL,
                            so_dien_thoai VARCHAR(20) UNIQUE NOT NULL,
    /*
        CCCD chá»‰ Ã¡p dá»¥ng cho nhÃ¢n viÃªn.
        KhÃ¡ch hÃ ng Ä‘á»ƒ NULL.
    */
                            cccd VARCHAR(20) UNIQUE NULL,
                            mat_khau NVARCHAR(255) NOT NULL,
    -- 0: Nam | 1: Ná»¯
                            gioi_tinh TINYINT
                                CHECK(gioi_tinh IN(0,1)),
                            ngay_sinh DATE,
                            anh_dai_dien VARCHAR(500),
                            trang_thai NVARCHAR(20)DEFAULT N'Hoáº¡t Äá»™ng' CHECK(trang_thai IN(N'Hoáº¡t Äá»™ng',N'KhÃ³a')),
                            ngay_tao DATETIME2 DEFAULT GETDATE(),
                            ngay_cap_nhat DATETIME2 DEFAULT GETDATE(),
                            FOREIGN KEY(vai_tro_id) REFERENCES vai_tro(id)

);

CREATE TABLE dia_chi (
                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                         nguoi_dung_id BIGINT NOT NULL,
                         ten_nguoi_nhan NVARCHAR(100),
                         sdt_nguoi_nhan NVARCHAR(20),
                         dia_chi NVARCHAR(MAX),
                         mac_dinh BIT DEFAULT 0,
                         FOREIGN KEY(nguoi_dung_id)REFERENCES nguoi_dung(id)
);


CREATE TABLE danh_muc (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          ten NVARCHAR(100) NOT NULL,
                          mo_ta NVARCHAR(MAX),
                          trang_thai NVARCHAR(20)
        DEFAULT N'Má»Ÿ'
        CHECK (trang_thai IN (N'Má»Ÿ', N'KhÃ³a')),
                          anh NVARCHAR(500)
);

CREATE TABLE mon_an (
                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        danh_muc_id BIGINT,
                        ten NVARCHAR(200),
                        mo_ta NVARCHAR(MAX),
                        thanh_phan NVARCHAR(MAX),
                        gia DECIMAL(12,2) CHECK(gia>=0),
                        so_luong_con INT DEFAULT 0 CHECK(so_luong_con >= 0),
                        da_ban INT DEFAULT 0 CHECK(da_ban >= 0),
                        danh_gia DECIMAL(3,2)DEFAULT 0,
                        so_luot_danh_gia INT DEFAULT 0,
                        trang_thai NVARCHAR(30) DEFAULT N'Äang bÃ¡n' CHECK (trang_thai IN(N'Äang bÃ¡n',N'Háº¿t hÃ ng',N'Ngá»«ng bÃ¡n' )),
                        ngay_tao DATETIME2 DEFAULT GETDATE(),
                        FOREIGN KEY(danh_muc_id) REFERENCES danh_muc(id)
);

CREATE TABLE hinh_anh_mon_an (

                                 id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                 mon_an_id BIGINT NOT NULL,
                                 duong_dan VARCHAR(500) NOT NULL,
                                 trang_thai NVARCHAR(20)DEFAULT N'Má»Ÿ' CHECK(trang_thai IN(N'Má»Ÿ',N'KhÃ³a')),

                                 FOREIGN KEY(mon_an_id)
                                     REFERENCES mon_an(id)

);

CREATE TABLE gio_hang (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          nguoi_dung_id BIGINT NOT NULL UNIQUE FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id)
);

CREATE TABLE chi_tiet_gio_hang (
                                   id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                   gio_hang_id BIGINT,
                                   mon_an_id BIGINT,
                                   so_luong INT NOT NULL CHECK(so_luong > 0),
                                   don_gia DECIMAL(12,2) NOT NULL CHECK(don_gia >= 0),
                                   FOREIGN KEY (gio_hang_id) REFERENCES gio_hang(id),
                                   FOREIGN KEY (mon_an_id) REFERENCES mon_an(id)
);


CREATE TABLE ma_giam_gia (
                             id BIGINT IDENTITY(1,1) PRIMARY KEY,
                             ma VARCHAR(50) NOT NULL UNIQUE,
                             mo_ta NVARCHAR(255),
                             loai_giam NVARCHAR(20)NOT NULL CHECK(loai_giam IN(N'Pháº§n trÄƒm', N'Tiá»n')),
                             gia_tri_giam DECIMAL(12,2) NOT NULL CHECK(gia_tri_giam > 0),
    /*
        Chá»‰ Ã¡p dá»¥ng khi giáº£m theo %
        VÃ­ dá»¥:
        Giáº£m 20%
        Tá»‘i Ä‘a 50.000Ä‘
    */
                             giam_toi_da DECIMAL(12,2)CHECK(giam_toi_da >= 0),

    -- ÄÆ¡n hÃ ng tá»‘i thiá»ƒu Ä‘á»ƒ Ã¡p dá»¥ng voucher
                             don_toi_thieu DECIMAL(12,2)DEFAULT 0CHECK(don_toi_thieu >= 0),
    -- Tá»•ng sá»‘ voucher phÃ¡t hÃ nh
                             so_luong INT NOT NULL CHECK(so_luong >= 0),
    -- Má»—i khÃ¡ch Ä‘Æ°á»£c nháº­n tá»‘i Ä‘a bao nhiÃªu láº§n
                             gioi_han_nhan INT DEFAULT 1 CHECK(gioi_han_nhan > 0),
    /*
        Äiá»u kiá»‡n nháº­n voucher.Admin tá»± quy Ä‘á»‹nh.
        VÃ­ dá»¥:
        SO_DON
        CHI_TIEU
        VIP
        BLACK_FRIDAY
        TET_2027
        KHAI_TRUONG
        ...
    */
                             loai_dieu_kien NVARCHAR(50),
    /*
        GiÃ¡ trá»‹ Ä‘iá»u kiá»‡n.
        SO_DON      -> 20
        CHI_TIEU    -> 5000000
        CÃ¡c Ä‘iá»u kiá»‡n khÃ´ng cáº§n giÃ¡ trá»‹ thÃ¬ Ä‘á»ƒ NULL.
    */
                             gia_tri_dieu_kien DECIMAL(12,2),
                             ngay_bat_dau DATETIME2,
                             ngay_ket_thuc DATETIME2,
                             trang_thai BIT DEFAULT 1

);

CREATE TABLE nguoi_dung_ma_giam_gia (
                                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                        nguoi_dung_id BIGINT NOT NULL,
                                        ma_giam_gia_id BIGINT NOT NULL,
                                        ngay_nhan DATETIME2 DEFAULT GETDATE(),
                                        trang_thai NVARCHAR(30) DEFAULT N'ChÆ°a sá»­ dá»¥ng'CHECK(trang_thai IN(N'ChÆ°a sá»­ dá»¥ng',N'ÄÃ£ sá»­ dá»¥ng', N'Háº¿t háº¡n')),
                                        ngay_su_dung DATETIME2,
                                        FOREIGN KEY(nguoi_dung_id) REFERENCES nguoi_dung(id),
                                        FOREIGN KEY(ma_giam_gia_id)REFERENCES ma_giam_gia(id)

);
CREATE TABLE don_hang (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          nguoi_dung_id BIGINT,
                          dia_chi_id BIGINT,
                          ma_giam_gia_id BIGINT,
                          nhan_vien_id BIGINT,
                          ghi_chu NVARCHAR(MAX),
                          tam_tinh DECIMAL(12,2),
                          phi_giao_hang DECIMAL(12,2),
                          tien_giam DECIMAL(12,2),
                          tong_tien DECIMAL(12,2),
                          thoi_gian_du_kien DATETIME2,
                          hinh_thuc_thanh_toan NVARCHAR(30) CHECK
    (hinh_thuc_thanh_toan IN( N'Tiá»n máº·t', N'Chuyá»ƒn khoáº£n',N'VÃ­ Ä‘iá»‡n tá»­' )),
                          trang_thai NVARCHAR(30) DEFAULT N'Chá» xÃ¡c nháº­n' CHECK
    (trang_thai IN( N'Chá» xÃ¡c nháº­n', N'XÃ¡c nháº­n', N'HoÃ n thÃ nh', N'Há»§y')),
                          ngay_tao DATETIME2 DEFAULT GETDATE(),
                          ly_do_huy NVARCHAR(MAX),
                          FOREIGN KEY(nguoi_dung_id)REFERENCES nguoi_dung(id),
                          FOREIGN KEY(dia_chi_id) REFERENCES dia_chi(id),
                          FOREIGN KEY(ma_giam_gia_id) REFERENCES ma_giam_gia(id),
                          FOREIGN KEY(nhan_vien_id)REFERENCES nguoi_dung(id)
);

CREATE TABLE chi_tiet_don_hang (
                                   id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                   don_hang_id BIGINT,
                                   mon_an_id BIGINT,
                                   so_luong INT NOT NULL CHECK (so_luong > 0),
                                   don_gia DECIMAL(12,2) NOT NULL CHECK (don_gia >= 0),
                                   thanh_tien AS (so_luong * don_gia) PERSISTED,
                                   FOREIGN KEY (don_hang_id) REFERENCES don_hang(id),
                                   FOREIGN KEY (mon_an_id) REFERENCES mon_an(id)
);

CREATE TABLE thanh_toan (
                            id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            don_hang_id BIGINT NOT NULL,
                            ma_giao_dich VARCHAR(100),
                            so_tien DECIMAL(12,2),
                            phuong_thuc NVARCHAR(30) CHECK( phuong_thuc IN(N'Tiá»n máº·t',N'Chuyá»ƒn khoáº£n',N'VÃ­ Ä‘iá»‡n tá»­')),
                            trang_thai NVARCHAR(30)CHECK  (trang_thai IN( N'Chá» thanh toÃ¡n', N'ThÃ nh cÃ´ng',  N'Tháº¥t báº¡i')),
                            thoi_gian DATETIME2,
                            FOREIGN KEY(don_hang_id)REFERENCES don_hang(id)
);
CREATE TABLE lich_su_trang_thai (
                                    id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    don_hang_id BIGINT,
                                    nhan_vien_id BIGINT,
                                    trang_thai NVARCHAR(50),
                                    thoi_gian DATETIME2 DEFAULT GETDATE(),
                                    FOREIGN KEY (don_hang_id) REFERENCES don_hang(id),
                                    FOREIGN KEY (nhan_vien_id) REFERENCES nguoi_dung(id)
);

CREATE TABLE danh_gia (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          don_hang_id BIGINT NOT NULL,
                          mon_an_id BIGINT NOT NULL,
                          nguoi_dung_id BIGINT NOT NULL,
                          so_sao INT CHECK(so_sao BETWEEN 1 AND 5),
                          binh_luan NVARCHAR(MAX),
                          ngay_tao DATETIME2 DEFAULT GETDATE(),
                          FOREIGN KEY(don_hang_id)REFERENCES don_hang(id),
                          FOREIGN KEY(mon_an_id)REFERENCES mon_an(id),
                          FOREIGN KEY(nguoi_dung_id)REFERENCES nguoi_dung(id)
);

CREATE UNIQUE INDEX UX_danh_gia ON danh_gia(don_hang_id,mon_an_id,nguoi_dung_id);


-- =========================
-- 1. VAI TRÃ’
-- =========================
INSERT INTO vai_tro (ten)
VALUES
    (N'KhÃ¡ch hÃ ng'),
    (N'NhÃ¢n viÃªn');

INSERT INTO vai_tro (ten)
VALUES
    (N'Admin');

-- =========================
-- 2. NGÆ¯á»œI DÃ™NG
-- =========================
INSERT INTO nguoi_dung
(vai_tro_id,ho_ten,email,so_dien_thoai,cccd,mat_khau,gioi_tinh,ngay_sinh,anh_dai_dien)
VALUES
    (1,N'Nguyá»…n VÄƒn A','vana@gmail.com','0988888888',NULL,'123456',0,'2002-01-10','avatar1.jpg'),
    (2,N'Tráº§n Thá»‹ B','tranb@gmail.com','0977777777','001203123456','123456',1,'1999-05-20','avatar2.jpg');
INSERT INTO nguoi_dung (
    vai_tro_id,ho_ten,email,so_dien_thoai,cccd,
    mat_khau,
    gioi_tinh,
    ngay_sinh,
    anh_dai_dien,
    trang_thai
)
VALUES (
           3,
           N'Quáº£n trá»‹ viÃªn',
           'admin@vinha.com',
           '0988888887',
           '001234567890',
           '123456',
           0,
           '2000-01-01',
           NULL,
           N'Hoáº¡t Äá»™ng'
       );

-- =========================
-- 3. Äá»ŠA CHá»ˆ
-- =========================
INSERT INTO dia_chi
(nguoi_dung_id,ten_nguoi_nhan,sdt_nguoi_nhan,dia_chi,mac_dinh)
VALUES
    (1,N'Nguyá»…n VÄƒn A','0988888888',N'123 Cáº§u Giáº¥y, HÃ  Ná»™i',1),
    (2,N'Tráº§n Thá»‹ B','0977777777',N'45 LÃª Äá»©c Thá», HÃ  Ná»™i',1);

-- =========================
-- 4. DANH Má»¤C
-- =========================
INSERT INTO danh_muc
(ten, mo_ta, trang_thai, anh)
VALUES
    (N'CÆ¡m vÄƒn phÃ²ng', N'CÃ¡c suáº¥t cÆ¡m háº±ng ngÃ y', N'Má»Ÿ', N'com-van-phong.jpg'),
    (N'CÆ¡m Ä‘áº·c biá»‡t', N'CÆ¡m cao cáº¥p', N'Má»Ÿ', N'com-dac-biet.jpg');

-- =========================
-- 5. MÃ“N Ä‚N
-- =========================
INSERT INTO mon_an
(danh_muc_id,ten,mo_ta,thanh_phan,gia,so_luong_con,da_ban)
VALUES
    (1,N'CÆ¡m gÃ  chiÃªn',N'CÆ¡m gÃ  giÃ²n',N'GÃ , cÆ¡m, rau',45000,100,20),
    (2,N'CÆ¡m bÃ² lÃºc láº¯c',N'BÃ² má»m',N'BÃ², cÆ¡m, rau',65000,80,15);

-- =========================
-- 6. HÃŒNH áº¢NH MÃ“N Ä‚N
-- =========================
INSERT INTO hinh_anh_mon_an
(mon_an_id,duong_dan)
VALUES
    (1,'com-ga.jpg'),
    (2,'com-bo.jpg');

-- =========================
-- 7. GIá»Ž HÃ€NG
-- =========================
INSERT INTO gio_hang
(nguoi_dung_id)
VALUES
    (1),
    (2);

-- =========================
-- 8. CHI TIáº¾T GIá»Ž HÃ€NG
-- =========================
INSERT INTO chi_tiet_gio_hang
(gio_hang_id,mon_an_id,so_luong,don_gia)
VALUES
    (1,1,2,45000),
    (2,2,1,65000);

-- =========================
-- 9. MÃƒ GIáº¢M GIÃ
-- =========================
INSERT INTO ma_giam_gia
(ma,mo_ta,loai_giam,gia_tri_giam,giam_toi_da,don_toi_thieu,so_luong,gioi_han_nhan,loai_dieu_kien,gia_tri_dieu_kien,ngay_bat_dau,ngay_ket_thuc)
VALUES
    ('GIAM10',N'Giáº£m 10%',N'Pháº§n trÄƒm',10,50000,100000,100,1,NULL,NULL,'2026-01-01','2026-12-31'),
    ('KM30000',N'Giáº£m 30K',N'Tiá»n',30000,NULL,150000,50,1,NULL,NULL,'2026-01-01','2026-12-31');

-- =========================
-- 10. NGÆ¯á»œI DÃ™NG MÃƒ GIáº¢M GIÃ
-- =========================
INSERT INTO nguoi_dung_ma_giam_gia
(nguoi_dung_id,ma_giam_gia_id)
VALUES
    (1,1),
    (2,2);

-- =========================
-- 11. ÄÆ N HÃ€NG
-- =========================
INSERT INTO don_hang
(nguoi_dung_id,dia_chi_id,ma_giam_gia_id,nhan_vien_id,ghi_chu,tam_tinh,phi_giao_hang,tien_giam,tong_tien,thoi_gian_du_kien,hinh_thuc_thanh_toan,trang_thai)
VALUES
    (1,1,1,2,N'Ãt cay',90000,15000,9000,96000,'2026-07-25 11:30',N'Tiá»n máº·t',N'Chá» xÃ¡c nháº­n'),

    (1,1,2,2,N'ThÃªm canh',65000,15000,30000,50000,'2026-07-25 12:00',N'Chuyá»ƒn khoáº£n',N'XÃ¡c nháº­n');

-- =========================
-- 12. CHI TIáº¾T ÄÆ N HÃ€NG
-- =========================
INSERT INTO chi_tiet_don_hang
(don_hang_id,mon_an_id,so_luong,don_gia)
VALUES
    (1,1,2,45000),
    (2,2,1,65000);

-- =========================
-- 13. THANH TOÃN
-- =========================
INSERT INTO thanh_toan
(don_hang_id,ma_giao_dich,so_tien,phuong_thuc,trang_thai,thoi_gian)
VALUES
    (1,'GD0001',96000,N'Tiá»n máº·t',N'Chá» thanh toÃ¡n',GETDATE()),
    (2,'GD0002',50000,N'Chuyá»ƒn khoáº£n',N'ThÃ nh cÃ´ng',GETDATE());

-- =========================
-- 14. Lá»ŠCH Sá»¬ TRáº NG THÃI
-- =========================
INSERT INTO lich_su_trang_thai
(don_hang_id,nhan_vien_id,trang_thai)
VALUES
    (1,2,N'Chá» xÃ¡c nháº­n'),
    (2,2,N'XÃ¡c nháº­n');

-- =========================
-- 15. ÄÃNH GIÃ
-- =========================
INSERT INTO danh_gia
(don_hang_id,mon_an_id,nguoi_dung_id,so_sao,binh_luan)
VALUES
    (1,1,1,5,N'CÆ¡m ráº¥t ngon'),
    (2,2,1,4,N'Thá»‹t má»m, giao nhanh');


UPDATE hinh_anh_mon_an
SET duong_dan = '/images/comGa.png'
WHERE mon_an_id = 1;

UPDATE hinh_anh_mon_an
SET duong_dan = '/images/comBoLucLac.jpg'
WHERE mon_an_id = 2;

-- =========================
-- 1. XÃ“A Dá»® LIá»†U CÅ¨
-- =========================
--DELETE FROM mon_an;
--DELETE FROM danh_muc;

-- (TÃ¹y chá»n) Náº¿u ID cá»§a báº¡n lÃ  kiá»ƒu sá»‘ tá»± tÄƒng (IDENTITY), hÃ£y cháº¡y 2 dÃ²ng dÆ°á»›i Ä‘Ã¢y Ä‘á»ƒ reset ID vá» 1.
-- Náº¿u báº¡n tá»± nháº­p ID báº±ng tay thÃ¬ khÃ´ng cáº§n cháº¡y 2 dÃ²ng nÃ y.
--DBCC CHECKIDENT ('mon_an', RESEED, 0);
--DBCC CHECKIDENT ('danh_muc', RESEED, 0);

-- =========================
-- 2. THÃŠM 5 DANH Má»¤C Má»šI
-- =========================
INSERT INTO danh_muc (ten, mo_ta, trang_thai, anh)
VALUES
    (N'CÆ¡m gÃ ', N'CÃ¡c mÃ³n cÆ¡m káº¿t há»£p vá»›i thá»‹t gÃ ', N'Má»Ÿ', N'com-ga.jpg'),
    (N'CÆ¡m bÃ²', N'CÃ¡c mÃ³n cÆ¡m káº¿t há»£p vá»›i thá»‹t bÃ²', N'Má»Ÿ', N'com-bo.jpg'),
    (N'CÆ¡m heo', N'CÃ¡c mÃ³n cÆ¡m vá»›i thá»‹t heo', N'Má»Ÿ', N'com-heo.jpg'),
    (N'CÆ¡m háº£i sáº£n', N'CÆ¡m chiÃªn vÃ  xÃ o vá»›i háº£i sáº£n', N'Má»Ÿ', N'com-hai-san.jpg'),
    (N'CÆ¡m chay', N'CÃ¡c mÃ³n cÆ¡m thanh Ä‘áº¡m', N'Má»Ÿ', N'com-chay.jpg');

-- =========================
-- 3. THÃŠM 10 MÃ“N Ä‚N Má»šI
-- =========================
-- Giáº£ sá»­ ID danh má»¥c tÆ°Æ¡ng á»©ng tá»« 1 Ä‘áº¿n 5 (theo thá»© tá»± vá»«a thÃªm á»Ÿ trÃªn)
INSERT INTO mon_an (danh_muc_id, ten, mo_ta, thanh_phan, gia, so_luong_con, da_ban)
VALUES
    -- Danh má»¥c 1: CÆ¡m gÃ 
    (3, N'CÆ¡m gÃ  xÃ o náº¥m', N'Thá»‹t gÃ  má»m xÃ o cÃ¹ng náº¥m hÆ°Æ¡ng', N'GÃ , náº¥m, cÆ¡m, hÃ nh tÃ¢y', 45000, 30, 15),
    (3, N'CÆ¡m gÃ  xá»‘i má»¡', N'ÄÃ¹i gÃ  chiÃªn xá»‘i má»¡ giÃ²n rá»¥m', N'ÄÃ¹i gÃ , cÆ¡m chiÃªn, cÃ  chua', 50000, 40, 25),
    (3, N'CÆ¡m gÃ  quay', N'GÃ  quay táº©m Æ°á»›p Ä‘áº­m Ä‘Ã ', N'GÃ  quay, dÆ°a chuá»™t, cÆ¡m tráº¯ng', 55000, 20, 10),

    -- Danh má»¥c 2: CÆ¡m bÃ²
    (4, N'CÆ¡m bÃ² lÃºc láº¯c', N'Thá»‹t bÃ² thÃ¡i khá»‘i xÃ o má»ng nÆ°á»›c', N'Thá»‹t bÃ², á»›t chuÃ´ng, hÃ nh tÃ¢y, cÆ¡m', 65000, 25, 20),
    (4, N'CÆ¡m bÃ² xÃ o dÆ°a chua', N'BÃ² xÃ o dÆ°a chua Ä‘Æ°a cÆ¡m', N'Thá»‹t bÃ², dÆ°a cáº£i chua, tá»i, cÆ¡m', 55000, 30, 12),

    -- Danh má»¥c 3: CÆ¡m heo
    (5, N'CÆ¡m ba chá»‰ rang chÃ¡y cáº¡nh', N'Thá»‹t ba chá»‰ rang xÃ©m cáº¡nh Ä‘áº­m vá»‹', N'Thá»‹t ba chá»‰, hÃ nh lÃ¡, cÆ¡m', 40000, 50, 40),
    (5, N'CÆ¡m sÆ°á»n nÆ°á»›ng', N'SÆ°á»n nÆ°á»›ng than hoa thÆ¡m lá»«ng', N'SÆ°á»n cá»‘t láº¿t, Ä‘á»“ chua, cÆ¡m táº¥m', 50000, 45, 35),
    (5, N'CÆ¡m thá»‹t bÄƒm sá»‘t cÃ  chua', N'Thá»‹t bÄƒm sá»‘t cÃ  chua dá»… Äƒn', N'Thá»‹t heo bÄƒm, cÃ  chua, hÃ nh, cÆ¡m', 35000, 35, 18),

    -- Danh má»¥c 4: CÆ¡m háº£i sáº£n
    (6, N'CÆ¡m má»±c xÃ o chua ngá»t', N'Má»±c tÆ°Æ¡i xÃ o sá»‘t chua ngá»t', N'Má»±c, dá»©a, cÃ  chua, tá»i, cÆ¡m', 60000, 20, 8),

    -- Danh má»¥c 5: CÆ¡m chay
    (7, N'CÆ¡m Ä‘áº­u hÅ© xÃ o rau náº¥m', N'MÃ³n chay thanh Ä‘áº¡m Ä‘á»§ cháº¥t', N'Äáº­u hÅ©, náº¥m, cáº£i thÃ¬a, cÆ¡m', 30000, 15, 5);
