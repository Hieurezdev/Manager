CREATE DATABASE IF NOT EXISTS manager_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE manager_db;

CREATE TABLE IF NOT EXISTS ChinhSachGia (
    maChinhSach VARCHAR(10) PRIMARY KEY,
    tenLoaiDoiTuong VARCHAR(50),
    phanTramGiamGia DOUBLE,
    moTa VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS KhachHang (
    maKH VARCHAR(10) PRIMARY KEY,
    hoTen VARCHAR(50),
    soCCCD VARCHAR(20),
    soDienThoai VARCHAR(15),
    email VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS DoanTau (
    maTau VARCHAR(10) PRIMARY KEY,
    tenDoanTau VARCHAR(50),
    loaiTau VARCHAR(30),
    trangThai VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS ToaTau (
    maToa VARCHAR(10) PRIMARY KEY,
    tenToa VARCHAR(30),
    maTau VARCHAR(10),
    soThuTu INT,
    loaiToa VARCHAR(30),
    soLuongGheToiDa INT,
    FOREIGN KEY (maTau) REFERENCES DoanTau(maTau)
);

CREATE TABLE IF NOT EXISTS GheNgoi (
    maGhe VARCHAR(10) PRIMARY KEY,
    maToa VARCHAR(10),
    soGhe VARCHAR(5),
    viTri VARCHAR(30),
    trangThai VARCHAR(30),
    FOREIGN KEY (maToa) REFERENCES ToaTau(maToa)
);

CREATE TABLE IF NOT EXISTS HanhTrinh (
    maHanhTrinh VARCHAR(10) PRIMARY KEY,
    maGaDi VARCHAR(10),
    maGaDen VARCHAR(10),
    quangDuong DOUBLE
);

CREATE TABLE IF NOT EXISTS LichTrinh (
    maLichTrinh VARCHAR(10) PRIMARY KEY,
    maHanhTrinh VARCHAR(10),
    maTau VARCHAR(10),
    ngayKhoiHanh VARCHAR(20),
    gioDi VARCHAR(10),
    gioDen VARCHAR(10),
    FOREIGN KEY (maHanhTrinh) REFERENCES HanhTrinh(maHanhTrinh),
    FOREIGN KEY (maTau) REFERENCES DoanTau(maTau)
);

CREATE TABLE IF NOT EXISTS ChiTietLichTrinh (
    maCTLT VARCHAR(10) PRIMARY KEY,
    maLichTrinh VARCHAR(10),
    gioDi VARCHAR(10),
    gioDen VARCHAR(10),
    FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh)
);

CREATE TABLE IF NOT EXISTS HoaDon (
    maHoaDon VARCHAR(10) PRIMARY KEY,
    loaiHoaDon VARCHAR(30),
    ngayGioLap DATETIME,
    maNhanVienLap VARCHAR(10),
    maKhachHang VARCHAR(10),
    tongTien DOUBLE,
    phuongThucThanhToan VARCHAR(30),
    trangThai VARCHAR(30),
    FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKH)
);

CREATE TABLE IF NOT EXISTS VeTau (
    maVe VARCHAR(10) PRIMARY KEY,
    maKH VARCHAR(10),
    maLichTrinh VARCHAR(10),
    loaiDoiTuong VARCHAR(30),
    maGhe VARCHAR(10),
    giaVe DOUBLE,
    trangThai VARCHAR(30),
    thoiDiemBanVe DATETIME,
    maNhanVienBanVe VARCHAR(10),
    maHoaDon VARCHAR(10),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh),
    FOREIGN KEY (maGhe) REFERENCES GheNgoi(maGhe),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
);

-- MỒI DỮ LIỆU BAN ĐẦU CHẠY THỬ CHUYẾN TÀU
INSERT INTO ChinhSachGia VALUES ('CS01', 'NGUOI_LON', 0.0, 'Gia goc');
INSERT INTO ChinhSachGia VALUES ('CS02', 'SINH_VIEN', 0.1, 'Giam 10%');
INSERT INTO HanhTrinh VALUES ('HT01', 'HN', 'DN', 850.0);
INSERT INTO DoanTau VALUES ('SE3', 'Tau SE3 chat luong cao', 'Tàu nhanh', 'Sẵn sàng');
INSERT INTO LichTrinh VALUES ('LT001', 'HT01', 'SE3', '2026-05-15', '19:25', '08:45');
INSERT INTO ChiTietLichTrinh VALUES ('CT01', 'LT001', '19:25', '08:45');