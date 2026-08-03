-- TEN WEB LA: VỊ NHÀ
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
        Email dùng để đăng nhập hoặc nhận thông báo.
        Không bắt buộc.
        Nếu có thì phải duy nhất.
    */
                            email NVARCHAR(100) UNIQUE NULL,
                            so_dien_thoai VARCHAR(20) UNIQUE NOT NULL,
    /*
        CCCD chỉ áp dụng cho nhân viên.
        Khách hàng để NULL.
    */
                            cccd VARCHAR(20) UNIQUE NULL,
                            mat_khau NVARCHAR(255) NOT NULL,
    -- 0: Nam | 1: Nữ
                            gioi_tinh TINYINT
                                CHECK(gioi_tinh IN(0,1)),
                            ngay_sinh DATE,
                            anh_dai_dien VARCHAR(500),
                            trang_thai NVARCHAR(20)DEFAULT N'Hoạt Động' CHECK(trang_thai IN(N'Hoạt Động',N'Khóa')),
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
        DEFAULT N'Mở'
        CHECK (trang_thai IN (N'Mở', N'Khóa')),
                          anh NVARCHAR(500),
                          so_luong INT DEFAULT 0
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
                        trang_thai NVARCHAR(30) DEFAULT N'Đang bán' CHECK (trang_thai IN(N'Đang bán',N'Hết hàng',N'Ngừng bán' )),
                        ngay_tao DATETIME2 DEFAULT GETDATE(),
                        FOREIGN KEY(danh_muc_id) REFERENCES danh_muc(id)
);

CREATE TABLE hinh_anh_mon_an (

                                 id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                 mon_an_id BIGINT NOT NULL,
                                 duong_dan VARCHAR(500) NOT NULL,
                                 trang_thai NVARCHAR(20)DEFAULT N'Mở' CHECK(trang_thai IN(N'Mở',N'Khóa')),

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
                             loai_giam NVARCHAR(20)NOT NULL CHECK(loai_giam IN(N'Phần trăm', N'Tiền')),
                             gia_tri_giam DECIMAL(12,2) NOT NULL CHECK(gia_tri_giam > 0),
    /*
        Chỉ áp dụng khi giảm theo %
        Ví dụ:
        Giảm 20%
        Tối đa 50.000đ
    */
                             giam_toi_da DECIMAL(12,2)CHECK(giam_toi_da >= 0),

    -- Đơn hàng tối thiểu để áp dụng voucher
                             don_toi_thieu DECIMAL(12,2)DEFAULT 0CHECK(don_toi_thieu >= 0),
    -- Tổng số voucher phát hành
                             so_luong INT NOT NULL CHECK(so_luong >= 0),
    -- Mỗi khách được nhận tối đa bao nhiêu lần
                             gioi_han_nhan INT DEFAULT 1 CHECK(gioi_han_nhan > 0),
    /*
        Điều kiện nhận voucher.Admin tự quy định.
        Ví dụ:
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
        Giá trị điều kiện.
        SO_DON      -> 20
        CHI_TIEU    -> 5000000
        Các điều kiện không cần giá trị thì để NULL.
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
                                        trang_thai NVARCHAR(30) DEFAULT N'Chưa sử dụng'CHECK(trang_thai IN(N'Chưa sử dụng',N'Đã sử dụng', N'Hết hạn')),
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
    (hinh_thuc_thanh_toan IN( N'Tiền mặt', N'Chuyển khoản',N'Ví điện tử' )),
                          trang_thai NVARCHAR(30) DEFAULT N'Chờ xác nhận' CHECK
    (trang_thai IN( N'Chờ xác nhận', N'Đã xác nhận', N'Đang chế biến', N'Hoàn thành', N'Đã hủy')),
                          ngay_tao DATETIME2 DEFAULT GETDATE(),
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
                            phuong_thuc NVARCHAR(30) CHECK( phuong_thuc IN(N'Tiền mặt',N'Chuyển khoản',N'Ví điện tử')),
                            trang_thai NVARCHAR(30)CHECK  (trang_thai IN( N'Chờ thanh toán', N'Thành công',  N'Thất bại')),
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
-- 1. VAI TRÒ
-- =========================
INSERT INTO vai_tro (ten)
VALUES
    (N'Khách hàng'),
    (N'Nhân viên');

INSERT INTO vai_tro (ten)
VALUES
    (N'Admin');

-- =========================
-- 2. NGƯỜI DÙNG
-- =========================
INSERT INTO nguoi_dung
(vai_tro_id,ho_ten,email,so_dien_thoai,cccd,mat_khau,gioi_tinh,ngay_sinh,anh_dai_dien)
VALUES
    (1,N'Nguyễn Văn A','vana@gmail.com','0988888888',NULL,'123456',0,'2002-01-10','avatar1.jpg'),
    (2,N'Trần Thị B','tranb@gmail.com','0977777777','001203123456','123456',1,'1999-05-20','avatar2.jpg');
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
           N'Quản trị viên',
           'admin@vinha.com',
           '0988888887',
           '001234567890',
           '123456',
           0,
           '2000-01-01',
           NULL,
           N'Hoạt Động'
       );

-- =========================
-- 3. ĐỊA CHỈ
-- =========================
INSERT INTO dia_chi
(nguoi_dung_id,ten_nguoi_nhan,sdt_nguoi_nhan,dia_chi,mac_dinh)
VALUES
    (1,N'Nguyễn Văn A','0988888888',N'123 Cầu Giấy, Hà Nội',1),
    (2,N'Trần Thị B','0977777777',N'45 Lê Đức Thọ, Hà Nội',1);

-- =========================
-- 4. DANH MỤC
-- =========================
INSERT INTO danh_muc
(ten, mo_ta, trang_thai, anh, so_luong)
VALUES
    (N'Cơm văn phòng', N'Các suất cơm hằng ngày', N'Mở', N'com-van-phong.jpg', 10),
    (N'Cơm đặc biệt', N'Cơm cao cấp', N'Mở', N'com-dac-biet.jpg', 5);

-- =========================
-- 5. MÓN ĂN
-- =========================
INSERT INTO mon_an
(danh_muc_id,ten,mo_ta,thanh_phan,gia,so_luong_con,da_ban)
VALUES
    (1,N'Cơm gà chiên',N'Cơm gà giòn',N'Gà, cơm, rau',45000,100,20),
    (2,N'Cơm bò lúc lắc',N'Bò mềm',N'Bò, cơm, rau',65000,80,15);

-- =========================
-- 6. HÌNH ẢNH MÓN ĂN
-- =========================
INSERT INTO hinh_anh_mon_an
(mon_an_id,duong_dan)
VALUES
    (1,'com-ga.jpg'),
    (2,'com-bo.jpg');

-- =========================
-- 7. GIỎ HÀNG
-- =========================
INSERT INTO gio_hang
(nguoi_dung_id)
VALUES
    (1),
    (2);

-- =========================
-- 8. CHI TIẾT GIỎ HÀNG
-- =========================
INSERT INTO chi_tiet_gio_hang
(gio_hang_id,mon_an_id,so_luong,don_gia)
VALUES
    (1,1,2,45000),
    (2,2,1,65000);

-- =========================
-- 9. MÃ GIẢM GIÁ
-- =========================
INSERT INTO ma_giam_gia
(ma,mo_ta,loai_giam,gia_tri_giam,giam_toi_da,don_toi_thieu,so_luong,gioi_han_nhan,loai_dieu_kien,gia_tri_dieu_kien,ngay_bat_dau,ngay_ket_thuc)
VALUES
    ('GIAM10',N'Giảm 10%',N'Phần trăm',10,50000,100000,100,1,NULL,NULL,'2026-01-01','2026-12-31'),
    ('KM30000',N'Giảm 30K',N'Tiền',30000,NULL,150000,50,1,NULL,NULL,'2026-01-01','2026-12-31');

-- =========================
-- 10. NGƯỜI DÙNG MÃ GIẢM GIÁ
-- =========================
INSERT INTO nguoi_dung_ma_giam_gia
(nguoi_dung_id,ma_giam_gia_id)
VALUES
    (1,1),
    (2,2);

-- =========================
-- 11. ĐƠN HÀNG
-- =========================
INSERT INTO don_hang
(nguoi_dung_id,dia_chi_id,ma_giam_gia_id,nhan_vien_id,ghi_chu,tam_tinh,phi_giao_hang,tien_giam,tong_tien,thoi_gian_du_kien,hinh_thuc_thanh_toan,trang_thai)
VALUES
    (1,1,1,2,N'Ít cay',90000,15000,9000,96000,'2026-07-25 11:30',N'Tiền mặt',N'Chờ xác nhận'),

    (1,1,2,2,N'Thêm canh',65000,15000,30000,50000,'2026-07-25 12:00',N'Chuyển khoản',N'Đã xác nhận');

-- =========================
-- 12. CHI TIẾT ĐƠN HÀNG
-- =========================
INSERT INTO chi_tiet_don_hang
(don_hang_id,mon_an_id,so_luong,don_gia)
VALUES
    (1,1,2,45000),
    (2,2,1,65000);

-- =========================
-- 13. THANH TOÁN
-- =========================
INSERT INTO thanh_toan
(don_hang_id,ma_giao_dich,so_tien,phuong_thuc,trang_thai,thoi_gian)
VALUES
    (1,'GD0001',96000,N'Tiền mặt',N'Chờ thanh toán',GETDATE()),
    (2,'GD0002',50000,N'Chuyển khoản',N'Thành công',GETDATE());

-- =========================
-- 14. LỊCH SỬ TRẠNG THÁI
-- =========================
INSERT INTO lich_su_trang_thai
(don_hang_id,nhan_vien_id,trang_thai)
VALUES
    (1,2,N'Chờ xác nhận'),
    (2,2,N'Đã xác nhận');

-- =========================
-- 15. ĐÁNH GIÁ
-- =========================
INSERT INTO danh_gia
(don_hang_id,mon_an_id,nguoi_dung_id,so_sao,binh_luan)
VALUES
    (1,1,1,5,N'Cơm rất ngon'),
    (2,2,1,4,N'Thịt mềm, giao nhanh');


UPDATE hinh_anh_mon_an
SET duong_dan = '/images/comGa.png'
WHERE mon_an_id = 1;

UPDATE hinh_anh_mon_an
SET duong_dan = '/images/comBoLucLac.jpg'
WHERE mon_an_id = 2;

-- =========================
-- 1. XÓA DỮ LIỆU CŨ
-- =========================
--DELETE FROM mon_an;
--DELETE FROM danh_muc;

-- (Tùy chọn) Nếu ID của bạn là kiểu số tự tăng (IDENTITY), hãy chạy 2 dòng dưới đây để reset ID về 1.
-- Nếu bạn tự nhập ID bằng tay thì không cần chạy 2 dòng này.
--DBCC CHECKIDENT ('mon_an', RESEED, 0);
--DBCC CHECKIDENT ('danh_muc', RESEED, 0);

-- =========================
-- 2. THÊM 5 DANH MỤC MỚI
-- =========================
INSERT INTO danh_muc (ten, mo_ta, trang_thai, anh, so_luong)
VALUES
    (N'Cơm gà', N'Các món cơm kết hợp với thịt gà', N'Mở', N'com-ga.jpg', 15),
    (N'Cơm bò', N'Các món cơm kết hợp với thịt bò', N'Mở', N'com-bo.jpg', 10),
    (N'Cơm heo', N'Các món cơm với thịt heo', N'Mở', N'com-heo.jpg', 20),
    (N'Cơm hải sản', N'Cơm chiên và xào với hải sản', N'Mở', N'com-hai-san.jpg', 12),
    (N'Cơm chay', N'Các món cơm thanh đạm', N'Mở', N'com-chay.jpg', 8);

-- =========================
-- 3. THÊM 10 MÓN ĂN MỚI
-- =========================
-- Giả sử ID danh mục tương ứng từ 1 đến 5 (theo thứ tự vừa thêm ở trên)
INSERT INTO mon_an (danh_muc_id, ten, mo_ta, thanh_phan, gia, so_luong_con, da_ban)
VALUES
    -- Danh mục 1: Cơm gà
    (3, N'Cơm gà xào nấm', N'Thịt gà mềm xào cùng nấm hương', N'Gà, nấm, cơm, hành tây', 45000, 30, 15),
    (3, N'Cơm gà xối mỡ', N'Đùi gà chiên xối mỡ giòn rụm', N'Đùi gà, cơm chiên, cà chua', 50000, 40, 25),
    (3, N'Cơm gà quay', N'Gà quay tẩm ướp đậm đà', N'Gà quay, dưa chuột, cơm trắng', 55000, 20, 10),

    -- Danh mục 2: Cơm bò
    (4, N'Cơm bò lúc lắc', N'Thịt bò thái khối xào mọng nước', N'Thịt bò, ớt chuông, hành tây, cơm', 65000, 25, 20),
    (4, N'Cơm bò xào dưa chua', N'Bò xào dưa chua đưa cơm', N'Thịt bò, dưa cải chua, tỏi, cơm', 55000, 30, 12),

    -- Danh mục 3: Cơm heo
    (5, N'Cơm ba chỉ rang cháy cạnh', N'Thịt ba chỉ rang xém cạnh đậm vị', N'Thịt ba chỉ, hành lá, cơm', 40000, 50, 40),
    (5, N'Cơm sườn nướng', N'Sườn nướng than hoa thơm lừng', N'Sườn cốt lết, đồ chua, cơm tấm', 50000, 45, 35),
    (5, N'Cơm thịt băm sốt cà chua', N'Thịt băm sốt cà chua dễ ăn', N'Thịt heo băm, cà chua, hành, cơm', 35000, 35, 18),

    -- Danh mục 4: Cơm hải sản
    (6, N'Cơm mực xào chua ngọt', N'Mực tươi xào sốt chua ngọt', N'Mực, dứa, cà chua, tỏi, cơm', 60000, 20, 8),

    -- Danh mục 5: Cơm chay
    (7, N'Cơm đậu hũ xào rau nấm', N'Món chay thanh đạm đủ chất', N'Đậu hũ, nấm, cải thìa, cơm', 30000, 15, 5);
