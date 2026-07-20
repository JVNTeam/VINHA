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
    trang_thai NVARCHAR(20)DEFAULT N'Mở' CHECK(trang_thai IN(N'Mở',N'Khóa'))

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