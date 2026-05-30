-- ============================================================================
-- KỊCH BẢN KHỞI TẠO CƠ SỞ DỮ LIỆU HỆ THỐNG QUẢN LÝ BÁN VÉ TÀU HỎA (MySQL Version)
-- Đảm bảo giữ nguyên 100% tên bảng, tên thuộc tính và cấu trúc thực thể đã thiết kế.
-- ============================================================================

-- Bật tắt kiểm tra khóa ngoại để an toàn khi DROP và CREATE
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa bảng cũ nếu tồn tại để tránh xung đột khi chạy lại script (Thứ tự xóa ngược với thứ tự tạo)
DROP TABLE IF EXISTS ChiTietBaoCao;
DROP TABLE IF EXISTS BaoCao;
DROP TABLE IF EXISTS PhieuTraVe;
DROP TABLE IF EXISTS VeTau;
DROP TABLE IF EXISTS HoaDon;
DROP TABLE IF EXISTS ChinhSachGia;
DROP TABLE IF EXISTS ChiTietLichTrinh;
DROP TABLE IF EXISTS LichTrinh;
DROP TABLE IF EXISTS GheNgoi;
DROP TABLE IF EXISTS ToaTau;
DROP TABLE IF EXISTS DoanTau;
DROP TABLE IF EXISTS ChiTietHanhTrinh;
DROP TABLE IF EXISTS NhaGa;
DROP TABLE IF EXISTS HanhTrinh;
DROP TABLE IF EXISTS KhachHang;
DROP TABLE IF EXISTS NhanVien;
DROP TABLE IF EXISTS QuanLy;
DROP TABLE IF EXISTS TaiKhoan;

-- ============================================================================
-- 1. KHỐI TÀI KHOẢN & PHÂN QUYỀN (Inheritance: TaiKhoan -> QuanLy, NhanVien)
-- ============================================================================

CREATE TABLE TaiKhoan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenDangNhap VARCHAR(100) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    hoTen VARCHAR(255) NOT NULL,
    vaiTro VARCHAR(50) NOT NULL CONSTRAINT chk_vaiTro CHECK (vaiTro IN ('QuanLy', 'NhanVien')),
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiTK CHECK (trangThai IN ('HoatDong', 'Khoa'))
);

CREATE TABLE QuanLy (
    id INT PRIMARY KEY,
    maQuanLy VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (id) REFERENCES TaiKhoan(id) ON DELETE CASCADE
);

CREATE TABLE NhanVien (
    id INT PRIMARY KEY,
    maNhanVien VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (id) REFERENCES TaiKhoan(id) ON DELETE CASCADE
);

-- ============================================================================
-- 2. KHỐI KHÁCH HÀNG
-- ============================================================================

CREATE TABLE KhachHang (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maKH VARCHAR(50) UNIQUE NOT NULL,
    hoTen VARCHAR(255) NOT NULL,
    soCCCD VARCHAR(20) UNIQUE NOT NULL,
    soDienThoai VARCHAR(20),
    email VARCHAR(100)
);

-- ============================================================================
-- 3. KHỐI NHÀ GA & HÀNH TRÌNH (Tuyến đường)
-- ============================================================================

CREATE TABLE HanhTrinh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maHanhTrinh VARCHAR(50) UNIQUE NOT NULL,
    tenHanhTrinh VARCHAR(255) NOT NULL,
    quangDuong DECIMAL(10,2) NOT NULL CONSTRAINT chk_quangDuong CHECK (quangDuong > 0)
);

-- Mỗi Nhà Ga chịu sự quản lý trực tiếp từ một Quản Lý (Mối quan hệ 1-N Aggregation)
CREATE TABLE NhaGa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maGa VARCHAR(50) UNIQUE NOT NULL,
    tenNhaGa VARCHAR(255) NOT NULL,
    soDienThoai VARCHAR(20),
    quanLyId INT REFERENCES QuanLy(id) ON DELETE SET NULL
);

-- Bảng trung gian giải quyết quan hệ N-N giữa HanhTrinh và NhaGa
CREATE TABLE ChiTietHanhTrinh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maCTHT VARCHAR(50) UNIQUE NOT NULL,
    thuTuGa INT NOT NULL CONSTRAINT chk_thuTuGa CHECK (thuTuGa > 0),
    nhaGaId INT NOT NULL REFERENCES NhaGa(id) ON DELETE RESTRICT,
    hanhTrinhId INT NOT NULL REFERENCES HanhTrinh(id) ON DELETE CASCADE,
    CONSTRAINT uq_hanhtrinh_ga UNIQUE (hanhTrinhId, nhaGaId)
);

-- ============================================================================
-- 4. KHỐI PHƯƠNG TIỆN (DoanTau -> ToaTau -> GheNgoi)
-- ============================================================================

CREATE TABLE DoanTau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maTau VARCHAR(50) UNIQUE NOT NULL,
    tenTau VARCHAR(255) NOT NULL,
    loaiTau VARCHAR(50) NOT NULL CONSTRAINT chk_loaiTau CHECK (loaiTau IN ('TauNhanh', 'TauThuong')),
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiDT CHECK (trangThai IN ('SanSang', 'BaoTri'))
);

CREATE TABLE ToaTau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maToa VARCHAR(50) UNIQUE NOT NULL,
    tenToa VARCHAR(100) NOT NULL,
    soThuTu INT NOT NULL CONSTRAINT chk_soThuTu CHECK (soThuTu > 0),
    loaiToa VARCHAR(50) NOT NULL CONSTRAINT chk_loaiToa CHECK (loaiToa IN ('NgoiCung', 'NgoiMem', 'GiuongNam')),
    soLuongGheToiDa INT NOT NULL CONSTRAINT chk_soGheMax CHECK (soLuongGheToiDa > 0),
    moTa TEXT,
    doanTauId INT NOT NULL REFERENCES DoanTau(id) ON DELETE CASCADE
);

CREATE TABLE GheNgoi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maGhe VARCHAR(50) UNIQUE NOT NULL,
    soGhe INT NOT NULL CONSTRAINT chk_soGhe CHECK (soGhe > 0),
    viTri VARCHAR(50) NOT NULL CONSTRAINT chk_viTri CHECK (viTri IN ('CuaSo', 'LoiDi', 'GiuongTang1', 'GiuongTang2', 'GiuongTang3')),
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiGhe CHECK (trangThai IN ('Trong', 'DaDat')),
    moTa TEXT,
    toaTauId INT NOT NULL REFERENCES ToaTau(id) ON DELETE CASCADE
);

-- ============================================================================
-- 5. KHỐI LỊCH TRÌNH VẬN HÀNH
-- ============================================================================

CREATE TABLE LichTrinh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maLichTrinh VARCHAR(50) UNIQUE NOT NULL,
    ngayKhoiHanh DATETIME NOT NULL,
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiLT CHECK (trangThai IN ('ChuaChay', 'DangChay', 'DaHoanThanh', 'BiHuy')),
    doanTauId INT NOT NULL REFERENCES DoanTau(id) ON DELETE RESTRICT,
    hanhTrinhId INT NOT NULL REFERENCES HanhTrinh(id) ON DELETE RESTRICT,
    quanLyId INT REFERENCES QuanLy(id) ON DELETE SET NULL
);

-- Bảng trung gian giải quyết quan hệ N-N giữa LichTrinh và NhaGa
CREATE TABLE ChiTietLichTrinh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maCTLT VARCHAR(50) UNIQUE NOT NULL,
    gioDen DATETIME,
    gioDi DATETIME,
    nhaGaId INT NOT NULL REFERENCES NhaGa(id) ON DELETE RESTRICT,
    lichTrinhId INT NOT NULL REFERENCES LichTrinh(id) ON DELETE CASCADE,
    CONSTRAINT chk_thoiGian_lichtrinh CHECK (gioDen IS NULL OR gioDi IS NULL OR gioDen <= gioDi)
);

-- ============================================================================
-- 6. KHỐI THIẾT LẬP GIÁ & GIAO DỊCH (ChinhSachGia -> HoaDon -> VeTau -> PhieuTraVe)
-- ============================================================================

CREATE TABLE ChinhSachGia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maChinhSach VARCHAR(50) UNIQUE NOT NULL,
    loaiDoiTuong VARCHAR(100) NOT NULL,
    tiLeGiamGia DECIMAL(5,2) NOT NULL DEFAULT 0.00 CONSTRAINT chk_tiLeGiamGia CHECK (tiLeGiamGia >= 0 AND tiLeGiamGia <= 100),
    moTa TEXT
);

CREATE TABLE HoaDon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maHoaDon VARCHAR(50) UNIQUE NOT NULL,
    loaiHoaDon VARCHAR(50) NOT NULL CONSTRAINT chk_loaiHD CHECK (loaiHoaDon IN ('MuaVe', 'PhatTraVe')),
    ngayGioLap DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    phuongThucThanhToan VARCHAR(50) NOT NULL CONSTRAINT chk_phuongThuc CHECK (phuongThucThanhToan IN ('TienMat', 'ChuyenKhoan')),
    tongTien INT NOT NULL DEFAULT 0 CONSTRAINT chk_tongTien CHECK (tongTien >= 0),
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiHD CHECK (trangThai IN ('DaThanhToan', 'DaHuy')),
    nhanVienId INT REFERENCES NhanVien(id) ON DELETE SET NULL,
    khachHangId INT REFERENCES KhachHang(id) ON DELETE SET NULL
);

CREATE TABLE VeTau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maVe VARCHAR(50) UNIQUE NOT NULL,
    loaiDoiTuong VARCHAR(50) NOT NULL CONSTRAINT chk_doiTuong CHECK (loaiDoiTuong IN ('NguoiLon', 'TreEm', 'NguoiGia')),
    giaVe INT NOT NULL CONSTRAINT chk_giaVe CHECK (giaVe >= 0),
    trangThai VARCHAR(50) NOT NULL CONSTRAINT chk_trangThaiVe CHECK (trangThai IN ('DaBan', 'DaTra', 'DaDoi')),
    thoiDiemBanVe DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lichTrinhId INT NOT NULL REFERENCES LichTrinh(id) ON DELETE RESTRICT,
    nhanVienId INT REFERENCES NhanVien(id) ON DELETE SET NULL,
    khachHangId INT REFERENCES KhachHang(id) ON DELETE SET NULL,
    gheNgoiId INT NOT NULL REFERENCES GheNgoi(id) ON DELETE RESTRICT,
    chinhSachGiaId INT REFERENCES ChinhSachGia(id) ON DELETE RESTRICT,
    hoaDonId INT REFERENCES HoaDon(id) ON DELETE CASCADE
);

CREATE TABLE PhieuTraVe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maPhieuTra VARCHAR(50) UNIQUE NOT NULL,
    thoiDiemTra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tienPhat INT NOT NULL DEFAULT 0 CONSTRAINT chk_tienPhat CHECK (tienPhat >= 0),
    tienHoanLaiKhach INT NOT NULL DEFAULT 0 CONSTRAINT chk_tienHoan CHECK (tienHoanLaiKhach >= 0),
    veTauId INT UNIQUE NOT NULL REFERENCES VeTau(id) ON DELETE RESTRICT,
    nhanVienId INT REFERENCES NhanVien(id) ON DELETE SET NULL,
    hoaDonId INT REFERENCES HoaDon(id) ON DELETE CASCADE
);

-- ============================================================================
-- 7. KHỐI THỐNG KÊ & BÁO CÁO (BaoCao -> ChiTietBaoCao)
-- ============================================================================

CREATE TABLE BaoCao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maBaoCao VARCHAR(50) UNIQUE NOT NULL,
    ngayLapBaoCao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tongDoanhThu BIGINT NOT NULL DEFAULT 0,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    quanLyId INT REFERENCES QuanLy(id) ON DELETE SET NULL,
    CONSTRAINT chk_khoangThoiGian CHECK (ngayBatDau <= ngayKetThuc)
);

CREATE TABLE ChiTietBaoCao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maCTBC VARCHAR(50) UNIQUE NOT NULL,
    soVeBan INT NOT NULL DEFAULT 0 CONSTRAINT chk_soVeBan CHECK (soVeBan >= 0),
    doanhThuChuyen INT NOT NULL DEFAULT 0 CONSTRAINT chk_doanhThuChuyen CHECK (doanhThuChuyen >= 0),
    tiLeLapDay DECIMAL(5,2) NOT NULL DEFAULT 0.00 CONSTRAINT chk_tiLeLapDay CHECK (tiLeLapDay >= 0 AND tiLeLapDay <= 100),
    lichTrinhId INT NOT NULL REFERENCES LichTrinh(id) ON DELETE RESTRICT,
    baoCaoId INT NOT NULL REFERENCES BaoCao(id) ON DELETE CASCADE,
    CONSTRAINT uq_baocao_lichtrinh UNIQUE (baoCaoId, lichTrinhId)
);

-- ============================================================================
-- 8. TẠO INDEXES TỐI ƯU TRUY VẤN (Performance Tuning)
-- ============================================================================

-- Tìm kiếm nhanh theo mã
CREATE INDEX idx_ve_maVe ON VeTau(maVe);
CREATE INDEX idx_hoadon_maHD ON HoaDon(maHoaDon);
CREATE INDEX idx_lichtrinh_maLT ON LichTrinh(maLichTrinh);

-- Tìm kiếm khách hàng theo CCCD và SĐT
CREATE INDEX idx_khachhang_cccd ON KhachHang(soCCCD);
CREATE INDEX idx_khachhang_sdt ON KhachHang(soDienThoai);

-- Truy vấn sơ đồ ghế nhanh theo Toa Tàu
CREATE INDEX idx_ghengoi_toa ON GheNgoi(toaTauId);

-- Tra cứu lịch trình theo khoảng thời gian khởi hành
CREATE INDEX idx_lichtrinh_khoihanh ON LichTrinh(ngayKhoiHanh);


-- ============================================================================
-- 9. CHÈN DỮ LIỆU MẪU ĐỂ CHẠY THỬ NGHIỆM (Seed Data)
-- ============================================================================

-- 9.1 Tài khoản (Mật khẩu demo: 'pbkdf2_sha256$hash_code_here')
INSERT INTO TaiKhoan (tenDangNhap, matKhau, hoTen, vaiTro, trangThai) VALUES
('admin01', 'hash_admin_pw_1', 'Nguyễn Văn Minh (Admin)', 'QuanLy', 'HoatDong'),
('clerk01', 'hash_clerk_pw_1', 'Trần Thị Thu Thảo', 'NhanVien', 'HoatDong'),
('clerk02', 'hash_clerk_pw_2', 'Lê Hoàng Long', 'NhanVien', 'HoatDong');

-- Khớp thông tin vào các bảng kế thừa
INSERT INTO QuanLy (id, maQuanLy) VALUES
(1, 'QL_HANOI_01');

INSERT INTO NhanVien (id, maNhanVien) VALUES
(2, 'NV_QUAY_01'),
(3, 'NV_QUAY_02');

-- 9.2 Khách hàng
INSERT INTO KhachHang (maKH, hoTen, soCCCD, soDienThoai, email) VALUES
('KH0001', 'Phạm Minh Đức', '001201004567', '0912345678', 'duc.pm@gmail.com'),
('KH0002', 'Nguyễn Thị Hoa', '002202009876', '0987654321', 'hoa.nt@gmail.com');

-- 9.3 Nhà ga & Hành trình
INSERT INTO HanhTrinh (maHanhTrinh, tenHanhTrinh, quangDuong) VALUES
('HT_HNSG', 'Tuyến Bắc Nam - Hà Nội Sài Gòn', 1726.00);

INSERT INTO NhaGa (maGa, tenNhaGa, soDienThoai, quanLyId) VALUES
('GA_HANOI', 'Ga Hà Nội', '02439423697', 1),
('GA_VINH', 'Ga Vinh', '02383844755', 1),
('GA_DANANG', 'Ga Đà Nẵng', '02363821175', 1),
('GA_SAIGON', 'Ga Sài Gòn', '02838436528', 1);

-- Chi tiết hành trình
INSERT INTO ChiTietHanhTrinh (maCTHT, thuTuGa, nhaGaId, hanhTrinhId) VALUES
('CTHT_01_HN', 1, 1, 1), -- Ga Hà Nội (Ga xuất phát)
('CTHT_01_VI', 2, 2, 1), -- Ga Vinh
('CTHT_01_DN', 3, 3, 1), -- Ga Đà Nẵng
('CTHT_01_SG', 4, 4, 1); -- Ga Sài Gòn (Ga cuối)

-- 9.4 Đoàn tàu -> Toa -> Ghế
INSERT INTO DoanTau (maTau, tenTau, loaiTau, trangThai) VALUES
('SE1', 'Tàu Thống Nhất SE1', 'TauNhanh', 'SanSang');

-- Toa 1: Ngồi Mềm
INSERT INTO ToaTau (maToa, tenToa, soThuTu, loaiToa, soLuongGheToiDa, moTa, doanTauId) VALUES
('TOA01_SE1', 'Toa 1 - Ghế ngồi mềm điều hòa', 1, 'NgoiMem', 4, 'Toa chất lượng cao', 1);

-- Toa 2: Giường Nằm
INSERT INTO ToaTau (maToa, tenToa, soThuTu, loaiToa, soLuongGheToiDa, moTa, doanTauId) VALUES
('TOA02_SE1', 'Toa 2 - Giường nằm khoang 4', 2, 'GiuongNam', 4, 'Khoa vip yên tĩnh', 1);

-- Ghế cho Toa 1
INSERT INTO GheNgoi (maGhe, soGhe, viTri, trangThai, moTa, toaTauId) VALUES
('SE1_T1_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi mềm sát cửa sổ', 1),
('SE1_T1_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi mềm gần lối đi', 1),
('SE1_T1_G03', 3, 'CuaSo', 'DaDat', 'Ghế ngồi mềm sát cửa sổ', 1),
('SE1_T1_G04', 4, 'LoiDi', 'Trong', 'Ghế ngồi mềm gần lối đi', 1);

-- Giường cho Toa 2
INSERT INTO GheNgoi (maGhe, soGhe, viTri, trangThai, moTa, toaTauId) VALUES
('SE1_T2_G01', 1, 'GiuongTang1', 'DaDat', 'Giường tầng 1 thoải mái', 2),
('SE1_T2_G02', 2, 'GiuongTang2', 'Trong', 'Giường tầng 2 có thang leo', 2),
('SE1_T2_G03', 3, 'GiuongTang1', 'Trong', 'Giường tầng 1 thoải mái', 2),
('SE1_T2_G04', 4, 'GiuongTang2', 'Trong', 'Giường tầng 2 có thang leo', 2);

-- 9.5 Lịch trình vận hành
INSERT INTO LichTrinh (maLichTrinh, ngayKhoiHanh, trangThai, doanTauId, hanhTrinhId, quanLyId) VALUES
('LT_SE1_20260601', '2026-06-01 19:30:00', 'ChuaChay', 1, 1, 1);

-- Thiết lập giờ đến, đi dự kiến tại các ga dừng
INSERT INTO ChiTietLichTrinh (maCTLT, gioDen, gioDi, nhaGaId, lichTrinhId) VALUES
('CTLT_HN', NULL, '2026-06-01 19:30:00', 1, 1),                 -- Xuất phát Ga Hà Nội
('CTLT_VI', '2026-06-02 01:15:00', '2026-06-02 01:25:00', 2, 1), -- Dừng Ga Vinh 10p
('CTLT_DN', '2026-06-02 08:30:00', '2026-06-02 08:45:00', 3, 1), -- Dừng Ga Đà Nẵng 15p
('CTLT_SG', '2026-06-02 21:00:00', NULL, 4, 1);                 -- Kết thúc Ga Sài Gòn

-- 9.6 Chính sách giảm giá
INSERT INTO ChinhSachGia (maChinhSach, loaiDoiTuong, tiLeGiamGia, moTa) VALUES
('CS_NGUOILON', 'Người lớn', 0.00, 'Vé giá chuẩn, không miễn giảm'),
('CS_TREEM', 'Trẻ em', 25.00, 'Giảm 25% cho trẻ em từ 6 đến 10 tuổi'),
('CS_NGUOIGIA', 'Người cao tuổi', 15.00, 'Giảm 15% cho người từ 60 tuổi trở lên');

-- 9.7 Hóa đơn giao dịch mua vé & Vé đã bán
-- Hóa đơn mua vé của khách hàng Phạm Minh Đức (NV 2 lập)
INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, phuongThucThanhToan, tongTien, trangThai, nhanVienId, khachHangId) VALUES
('HD_0001', 'MuaVe', '2026-05-27 10:00:00', 'ChuyenKhoan', 1250000, 'DaThanhToan', 2, 1);

-- Chi tiết các vé gắn liền với hóa đơn trên
INSERT INTO VeTau (maVe, loaiDoiTuong, giaVe, trangThai, thoiDiemBanVe, lichTrinhId, nhanVienId, khachHangId, gheNgoiId, chinhSachGiaId, hoaDonId) VALUES
('VE_0001', 'NguoiLon', 450000, 'DaBan', '2026-05-27 10:00:00', 1, 2, 1, 3, 1, 1),  -- Ghế Toa 1 (SE1_T1_G03)
('VE_0002', 'NguoiLon', 800000, 'DaBan', '2026-05-27 10:00:00', 1, 2, 1, 5, 1, 1);  -- Giường Toa 2 (SE1_T2_G01)

-- 9.8 Hóa đơn hoàn vé & Phiếu trả vé (Giả lập trả lại chiếc vé số 1)
-- Tạo hóa đơn ghi nhận phạt hoàn trả tiền
INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, phuongThucThanhToan, tongTien, trangThai, nhanVienId, khachHangId) VALUES
('HD_0002_TRA', 'PhatTraVe', '2026-05-27 15:30:00', 'TienMat', 45000, 'DaThanhToan', 2, 1);

-- Tạo phiếu trả vé phạt 10% (45k VNĐ), hoàn lại 405k VNĐ cho khách
INSERT INTO PhieuTraVe (maPhieuTra, thoiDiemTra, tienPhat, tienHoanLaiKhach, veTauId, nhanVienId, hoaDonId) VALUES
('PT_0001', '2026-05-27 15:30:00', 45000, 405000, 1, 2, 2);

-- Cập nhật lại trạng thái vé bị trả
UPDATE VeTau SET trangThai = 'DaTra' WHERE id = 1;
-- Giải phóng trạng thái ghế 'SE1_T1_G03' thành trống sau khi hủy
UPDATE GheNgoi SET trangThai = 'Trong' WHERE id = 3;

-- 9.9 Tạo một báo cáo doanh thu
INSERT INTO BaoCao (maBaoCao, ngayLapBaoCao, tongDoanhThu, ngayBatDau, ngayKetThuc, quanLyId) VALUES
('BC_JUNE_2026', '2026-06-03 08:00:00', 1250000, '2026-06-01', '2026-06-02', 1);

INSERT INTO ChiTietBaoCao (maCTBC, soVeBan, doanhThuChuyen, tiLeLapDay, lichTrinhId, baoCaoId) VALUES
('CTBC_SE1_01', 2, 1250000, 25.00, 1, 1); -- SE1 có tổng 8 ghế, bán 2 vé -> Tỉ lệ lấp đầy 25%

-- Bật lại kiểm tra khóa ngoại sau khi hoàn tất
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- KẾT THÚC SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU
-- ============================================================================