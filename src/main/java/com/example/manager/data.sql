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
    diaChi VARCHAR(255),
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

-- 9.1 Tài khoản (Mật khẩu demo: '123456')
INSERT INTO TaiKhoan (tenDangNhap, matKhau, hoTen, vaiTro, trangThai) VALUES
('admin01', '123456', 'Nguyễn Văn Minh (Admin)', 'QuanLy', 'HoatDong'),
('admin02', '123456', 'Hoàng Văn Đức', 'QuanLy', 'HoatDong'),
('admin03', '123456', 'Vũ Thị Hạnh', 'QuanLy', 'HoatDong'),
('admin04', '123456', 'Đinh Văn Long', 'QuanLy', 'HoatDong'),
('admin05', '123456', 'Trương Văn Quang', 'QuanLy', 'HoatDong'),
('admin06', '123456', 'Vương Thị Oanh', 'QuanLy', 'HoatDong'),
('admin07', '123456', 'Trần Thị Lan Anh', 'QuanLy', 'HoatDong'),
('admin08', '123456', 'Hoàng Văn Hải', 'QuanLy', 'HoatDong'),
('admin09', '123456', 'Ngô Văn Khánh', 'QuanLy', 'HoatDong'),
('admin10', '123456', 'Đặng Văn Phong', 'QuanLy', 'HoatDong'),
('clerk01', '123456', 'Trần Thị Thu Thảo', 'NhanVien', 'HoatDong'),
('clerk02', '123456', 'Lê Hoàng Long', 'NhanVien', 'HoatDong'),
('clerk03', '123456', 'Phạm Thị Diễm', 'NhanVien', 'HoatDong'),
('clerk04', '123456', 'Đỗ Thị Phương', 'NhanVien', 'HoatDong'),
('clerk05', '123456', 'Ngô Văn Giang', 'NhanVien', 'Khoa'),
('clerk06', '123456', 'Đặng Văn Hùng', 'NhanVien', 'HoatDong'),
('clerk07', '123456', 'Bùi Thị Khánh', 'NhanVien', 'HoatDong'),
('clerk08', '123456', 'Lý Thị Mai', 'NhanVien', 'HoatDong'),
('clerk09', '123456', 'Mai Văn Phúc', 'NhanVien', 'HoatDong'),
('clerk10', '123456', 'Phan Thị Quỳnh', 'NhanVien', 'HoatDong'),
('clerk11', '123456', 'Đào Thị Sen', 'NhanVien', 'HoatDong'),
('clerk12', '123456', 'Lưu Văn Tuấn', 'NhanVien', 'HoatDong'),
('clerk13', '123456', 'Nguyễn Văn Bình', 'NhanVien', 'HoatDong'),
('clerk14', '123456', 'Lê Văn Dũng', 'NhanVien', 'HoatDong'),
('clerk15', '123456', 'Đỗ Thị Ngọc Ánh', 'NhanVien', 'HoatDong'),
('clerk16', '123456', 'Vũ Thị Minh Trang', 'NhanVien', 'HoatDong'),
('clerk17', '123456', 'Bùi Thị Thanh Huyền', 'NhanVien', 'HoatDong');

-- 2. Thêm vào bảng KhachHang
INSERT INTO KhachHang (maKH, hoTen, soDienThoai, email, soCCCD) VALUES
-- ('KH0001','Phạm Minh Đức', '0912345678', 'duc.pm@gmail.com', '001201004567'),
-- ('KH0002','Nguyễn Thị Hoa', '0987654321', 'hoa.nt@gmail.com', '002202009876'),
('KH0000', 'Phạm Văn Anh', '0901000001', 'anh.pv@email.com', '001000000001'), -- Khôi phục bản ghi 01 cũ của bạn bị thiếu
('KH0001', 'Phạm Minh Đức', '0912345678', 'duc.pm@gmail.com', '001201004567'),
('KH0002', 'Nguyễn Thị Hoa', '0987654321', 'hoa.nt@gmail.com', '002202009876'),
('KH0003', 'Lê Văn Cường', '0901000003', 'cuong.lv@email.com', '001000000003'),
('KH0004', 'Hoàng Thị Dung', '0901000004', 'dung.ht@email.com', '001000000004'),
('KH0005', 'Nguyễn Văn Em', '0901000005', 'em.nv@email.com', '001000000005'),
('KH0006', 'Đỗ Thị Phượng', '0901000006', 'phuong.dt@email.com', '001000000006'),
('KH0007', 'Ngô Văn Giang', '0901000007', 'giang.nv@email.com', '001000000007'),
('KH0008', 'Vũ Thị Hạnh', '0901000008', 'hanh.vt@email.com', '001000000008'),
('KH0009', 'Đặng Văn Ích', '0901000009', 'ich.dv@email.com', '001000000009'),
('KH0010', 'Bùi Thị Kim', '0901000010', 'kim.bt@email.com', '001000000010'),
('KH0011', 'Đinh Văn Long', '0901000011', 'long.dv@email.com', '001000000011'),
('KH0012', 'Lý Thị Mai', '0901000012', 'mai.lt@email.com', '001000000012'),
('KH0013', 'Trịnh Văn Nam', '0901000013', 'nam.tv@email.com', '001000000013'),
('KH0014', 'Vương Thị Oanh', '0901000014', 'oanh.vt@email.com', '001000000014'),
('KH0015', 'Mai Văn Phát', '0901000015', 'phat.mv@email.com', '001000000015'),
('KH0016', 'Phan Thị Quế', '0901000016', 'que.pt@email.com', '001000000016'),
('KH0017', 'Trương Văn Rạng', '0901000017', 'rang.tv@email.com', '001000000017'),
('KH0018', 'Đào Thị Sen', '0901000018', 'sen.dt@email.com', '001000000018'),
('KH0019', 'Lưu Văn Tùng', '0901000019', 'tung.lv@email.com', '001000000019'),
('KH0020', 'Nguyễn Thị Uyên', '0901000020', 'uyen.nt@email.com', '001000000020'),
('KH0021', 'Phạm Văn Việt', '0901000021', 'viet.pv@email.com', '001000000021'),
('KH0022', 'Trần Thị Xuân', '0901000022', 'xuan.tt@email.com', '001000000022'),
('KH0023', 'Lê Văn Yên', '0901000023', 'yen.lv@email.com', '001000000023'),
('KH0024', 'Hoàng Thị Dung', '0901000024', 'dung.ht2@email.com', '001000000024'),
('KH0025', 'Nguyễn Văn Tuấn', '0901000025', 'tuan.nv@email.com', '001000000025'),
('KH0026', 'Đỗ Thị Lan', '0901000026', 'lan.dt@email.com', '001000000026'),
('KH0027', 'Ngô Văn Hùng', '0901000027', 'hung.nv@email.com', '001000000027'),
('KH0028', 'Vũ Thị Hoa', '0901000028', 'hoa.vt@email.com', '001000000028'),
('KH0029', 'Đặng Văn Minh', '0901000029', 'minh.dv@email.com', '001000000029'),
('KH0030', 'Bùi Thị Ngọc', '0901000030', 'ngoc.bt@email.com', '001000000030'),
('KH0031', 'Nguyễn Văn Nam', '0901000031', 'nam.nv31@email.com', '001000000031'),
('KH0032', 'Trần Thị Hằng', '0901000032', 'hang.tt32@email.com', '001000000032'),
('KH0033', 'Lê Văn Quân', '0901000033', 'quan.lv33@email.com', '001000000033'),
('KH0034', 'Phạm Thị Thu', '0901000034', 'thu.pt34@email.com', '001000000034'),
('KH0035', 'Hoàng Văn Tuấn', '0901000035', 'tuan.hv35@email.com', '001000000035'),
('KH0036', 'Đỗ Thị Minh', '0901000036', 'minh.dt36@email.com', '001000000036'),
('KH0037', 'Ngô Văn Hải', '0901000037', 'hai.nv37@email.com', '001000000037'),
('KH0038', 'Vũ Thị Lan', '0901000038', 'lan.vt38@email.com', '001000000038'),
('KH0039', 'Đặng Văn Quang', '0901000039', 'quang.dv39@email.com', '001000000039'),
('KH0040', 'Bùi Thị Ngọc', '0901000040', 'ngoc.bt40@email.com', '001000000040'),
('KH0041', 'Đinh Văn Thành', '0901000041', 'thanh.dv41@email.com', '001000000041'),
('KH0042', 'Lý Thị Nga', '0901000042', 'nga.lt42@email.com', '001000000042'),
('KH0043', 'Trịnh Văn Thắng', '0901000043', 'thang.tv43@email.com', '001000000043'),
('KH0044', 'Vương Thị Huệ', '0901000044', 'hue.vt44@email.com', '001000000044'),
('KH0045', 'Mai Văn Vinh', '0901000045', 'vinh.mv45@email.com', '001000000045'),
('KH0046', 'Phan Thị Dung', '0901000046', 'dung.pt46@email.com', '001000000046'),
('KH0047', 'Trương Văn Dũng', '0901000047', 'dung.tv47@email.com', '001000000047'),
('KH0048', 'Đào Thị Ly', '0901000048', 'ly.dt48@email.com', '001000000048'),
('KH0049', 'Lưu Văn Hùng', '0901000049', 'hung.lv49@email.com', '001000000049'),
('KH0050', 'Nguyễn Thị Lệ', '0901000050', 'le.nt50@email.com', '001000000050'),
('KH0051', 'Phạm Văn Khiêm', '0901000051', 'khiem.pv51@email.com', '001000000051'),
('KH0052', 'Trần Thị Thúy', '0901000052', 'thuy.tt52@email.com', '001000000052'),
('KH0053', 'Lê Văn Sang', '0901000053', 'sang.lv53@email.com', '001000000053'),
('KH0054', 'Hoàng Thị Tuyết', '0901000054', 'tuyet.ht54@email.com', '001000000054'),
('KH0055', 'Nguyễn Văn Phúc', '0901000055', 'phuc.nv55@email.com', '001000000055'),
('KH0056', 'Đỗ Thị Hồng', '0901000056', 'hong.dt56@email.com', '001000000056'),
('KH0057', 'Ngô Văn Sơn', '0901000057', 'son.nv57@email.com', '001000000057'),
('KH0058', 'Vũ Thị Mai', '0901000058', 'mai.vt58@email.com', '001000000058'),
('KH0059', 'Đặng Văn Trung', '0901000059', 'trung.dv59@email.com', '001000000059'),
('KH0060', 'Bùi Thị Phương', '0901000060', 'phuong.bt60@email.com', '001000000060'),
('KH0061', 'Trần Văn Mạnh', '0901000061', 'manh.tv61@email.com', '001000000061'),
('KH0062', 'Lê Thị Thu', '0901000062', 'thu.lt62@email.com', '001000000062'),
('KH0063', 'Phạm Văn Hùng', '0901000063', 'hung.pv63@email.com', '001000000063'),
('KH0064', 'Nguyễn Thị Bích', '0901000064', 'bich.nt64@email.com', '001000000064'),
('KH0065', 'Hoàng Văn Cường', '0901000065', 'cuong.hv65@email.com', '001000000065'),
('KH0066', 'Đỗ Thị Huyền', '0901000066', 'huyen.dt66@email.com', '001000000066'),
('KH0067', 'Ngô Văn Tùng', '0901000067', 'tung.nv67@email.com', '001000000067'),
('KH0068', 'Vũ Thị Phương', '0901000068', 'phuong.vt68@email.com', '001000000068'),
('KH0069', 'Đặng Văn Chiến', '0901000069', 'chien.dv69@email.com', '001000000069'),
('KH0070', 'Bùi Thị Hà', '0901000070', 'ha.bt70@email.com', '001000000070'),
('KH0071', 'Đinh Văn Đại', '0901000071', 'dai.dv71@email.com', '001000000071'),
('KH0072', 'Lý Thị Diệu', '0901000072', 'dieu.lt72@email.com', '001000000072'),
('KH0073', 'Trịnh Văn Quý', '0901000073', 'quy.tv73@email.com', '001000000073'),
('KH0074', 'Vương Thị Thanh', '0901000074', 'thanh.vt74@email.com', '001000000074'),
('KH0075', 'Mai Văn Tấn', '0901000075', 'tan.mv75@email.com', '001000000075'),
('KH0076', 'Phan Thị Yến', '0901000076', 'yen.pt76@email.com', '001000000076'),
('KH0077', 'Trương Văn Nghĩa', '0901000077', 'nghia.tv77@email.com', '001000000077'),
('KH0078', 'Đào Thị Nhàn', '0901000078', 'nhan.dt78@email.com', '001000000078'),
('KH0079', 'Lưu Văn Tấn', '0901000079', 'tan.lv79@email.com', '001000000079'),
('KH0080', 'Nguyễn Thị Lành', '0901000080', 'lanh.nt80@email.com', '001000000080'),
('KH0081', 'Phạm Văn Khánh', '0901000081', 'khanh.pv81@email.com', '001000000081'),
('KH0082', 'Trần Thị Lụa', '0901000082', 'lua.tt82@email.com', '001000000082'),
('KH0083', 'Lê Văn Sang', '0901000083', 'sang.lv83@email.com', '001000000083'),
('KH0084', 'Hoàng Thị Tuyết', '0901000084', 'tuyet.ht84@email.com', '001000000084'),
('KH0085', 'Nguyễn Văn Phát', '0901000085', 'phat.nv85@email.com', '001000000085'),
('KH0086', 'Đỗ Thị Hằng', '0901000086', 'hang.dt86@email.com', '001000000086'),
('KH0087', 'Ngô Văn Sỹ', '0901000087', 'sy.nv87@email.com', '001000000087'),
('KH0088', 'Vũ Thị Mến', '0901000088', 'men.vt88@email.com', '001000000088'),
('KH0089', 'Đặng Văn Trọng', '0901000089', 'trong.dv89@email.com', '001000000089'),
('KH0090', 'Bùi Thị Phượng', '0901000090', 'phuong.bt90@email.com', '001000000090'),
('KH0091', 'Lê Văn An', '0901000091', 'an.lv91@email.com', '001000000091'),
('KH0092', 'Trần Thị Bình', '0901000092', 'binh.tt92@email.com', '001000000092'),
('KH0093', 'Nguyễn Văn Cường', '0901000093', 'cuong.nv93@email.com', '001000000093'),
('KH0094', 'Phạm Thị Dung', '0901000094', 'dung.pt94@email.com', '001000000094'),
('KH0095', 'Hoàng Văn Em', '0901000095', 'em.hv95@email.com', '001000000095'),
('KH0096', 'Đỗ Thị Phượng', '0901000096', 'phuong.dt96@email.com', '001000000096'),
('KH0097', 'Ngô Văn Giang', '0901000097', 'giang.nv97@email.com', '001000000097'),
('KH0098', 'Vũ Thị Hạnh', '0901000098', 'hanh.vt98@email.com', '001000000098'),
('KH0099', 'Đặng Văn Ích', '0901000099', 'ich.dv99@email.com', '001000000099'),
('KH0100', 'Bùi Thị Kim', '0901000100', 'kim.bt100@email.com', '001000000100'),
('KH0101', 'Đinh Văn Long', '0901000101', 'long.dv101@email.com', '001000000101'),
('KH0102', 'Lý Thị Mai', '0901000102', 'mai.lt102@email.com', '001000000102'),
('KH0103', 'Trịnh Văn Nam', '0901000103', 'nam.tv103@email.com', '001000000103'),
('KH0104', 'Vương Thị Oanh', '0901000104', 'oanh.vt104@email.com', '001000000104'),
('KH0105', 'Mai Văn Phát', '0901000105', 'phat.mv105@email.com', '001000000105'),
('KH0106', 'Phan Thị Quế', '0901000106', 'que.pt106@email.com', '001000000106'),
('KH0107', 'Trương Văn Rạng', '0901000107', 'rang.tv107@email.com', '001000000107'),
('KH0108', 'Đào Thị Sen', '0901000108', 'sen.dt108@email.com', '001000000108'),
('KH0109', 'Lưu Văn Tùng', '0901000109', 'tung.lv109@email.com', '001000000109'),
('KH0110', 'Nguyễn Thị Uyên', '0901000110', 'uyen.nt110@email.com', '001000000110'),
('KH0111', 'Phạm Văn Việt', '0901000111', 'viet.pv111@email.com', '001000000111'),
('KH0112', 'Trần Thị Xuân', '0901000112', 'xuan.tt112@email.com', '001000000112'),
('KH0113', 'Lê Văn Yên', '0901000113', 'yen.lv113@email.com', '001000000113'),
('KH0114', 'Hoàng Văn Huy', '0901000114', 'huy.hv114@email.com', '001000000114'),
('KH0115', 'Nguyễn Văn Tuấn', '0901000115', 'tuan.nv115@email.com', '001000000115'),
('KH0116', 'Đỗ Thị Lan', '0901000116', 'lan.dt116@email.com', '001000000116'),
('KH0117', 'Ngô Văn Hùng', '0901000117', 'hung.nv117@email.com', '001000000117'),
('KH0118', 'Vũ Thị Hoa', '0901000118', 'hoa.vt118@email.com', '001000000118'),
('KH0119', 'Đặng Văn Minh', '0901000119', 'minh.dv119@email.com', '001000000119'),
('KH0120', 'Bùi Thị Ngọc', '0901000120', 'ngoc.bt120@email.com', '001000000120');
-- Them du lieu vao bang HanhTrinh  
INSERT INTO HanhTrinh (maHanhTrinh, tenHanhTrinh, quangDuong) VALUES
('HT000', 'Hà Nội - Hải Phòng', 102.00),
('HT001', 'Hà Nội - Lào Cai', 296.00),
('HT002', 'Hà Nội - Quán Triều', 75.00),
('HT003', 'Sài Gòn - Nha Trang', 411.00),
('HT004', 'Sài Gòn - Quy Nhơn', 667.00),
('HT005', 'Sài Gòn - Đà Nẵng', 935.00),
('HT006', 'Sài Gòn - Huế', 1032.00),
('HT007', 'Đà Nẵng - Huế', 103.00),
('HT008', 'Huế - Vinh', 370.00),
('HT009', 'Vinh - Thanh Hóa', 140.00),
('HT010', 'Thanh Hóa - Nam Định', 90.00),
('HT011', 'Sài Gòn - Phan Thiết', 187.00),
('HT012', 'Nha Trang - Đà Nẵng', 524.00),
('HT013', 'Đà Nẵng - Quy Nhơn', 324.00),
('HT014', 'Quy Nhơn - Nha Trang', 256.00),
('HT015', 'Hà Nội - Vinh', 319.00),
('HT016', 'Vinh - Quảng Bình', 230.00),
('HT017', 'Quảng Bình - Huế', 180.00),
('HT018', 'Hà Nội - Thanh Hóa', 170.00),
('HT019', 'Thanh Hóa - Quảng Bình', 350.00),
('HT020', 'Sài Gòn - Bình Thuận', 150.00),
('HT021', 'Bình Thuận - Nha Trang', 261.00),
('HT022', 'Nha Trang - Phú Yên', 120.00),
('HT023', 'Phú Yên - Quy Nhơn', 136.00),
('HT024', 'Quy Nhơn - Diêu Trì', 13.00),
('HT025', 'Diêu Trì - Đà Nẵng', 311.00),
('HT026', 'Đà Nẵng - Hà Nội', 791.00),
('HT027', 'Hà Nội - Nam Định', 87.00),
('HT028', 'Nam Định - Ninh Bình', 28.00),
('HT029', 'Ninh Bình - Vinh', 204.00),
('HT030', 'Vinh - Đồng Hới', 198.00),
('HT031', 'Đồng Hới - Đông Hà', 170.00),
('HT032', 'Đông Hà - Huế', 70.00),
('HT033', 'Huế - Đà Nẵng', 103.00),
('HT034', 'Đà Nẵng - Tam Kỳ', 72.00),
('HT035', 'Tam Kỳ - Quảng Ngãi', 65.00),
('HT036', 'Quảng Ngãi - Bồng Sơn', 85.00),
('HT037', 'Bồng Sơn - Diêu Trì', 95.00),
('HT038', 'Diêu Trì - Tuy Hòa', 100.00),
('HT039', 'Tuy Hòa - Nha Trang', 120.00),
('HT040', 'Nha Trang - Tháp Chàm', 100.00),
('HT041', 'Tháp Chàm - Bình Thuận', 95.00),
('HT042', 'Bình Thuận - Long Khánh', 90.00),
('HT043', 'Long Khánh - Đà Nẵng', 60.00),
('HT044', 'Biên Hòa - Sài Gòn', 30.00),
('HT045', 'Hà Nội - Phủ Lý', 55.00),
('HT046', 'Phủ Lý - Nam Định', 32.00),
('HT047', 'Nam Định - Ninh Bình', 28.00),
('HT048', 'Ninh Bình - Đồng Hới', 401.00),
('HT049', 'Bỉm Sơn - Thanh Hóa', 30.00),
('HT050', 'Thanh Hóa - Minh Khôi', 15.00),
('HT051', 'Minh Khôi - Chợ Lâm', 20.00),
('HT052', 'Chợ Lâm - Văn Trai', 25.00),
('HT053', 'Văn Trai - Hoàng Mai', 35.00),
('HT054', 'Hoàng Mai - Vinh', 70.00),
('HT055', 'Hà Nội - Đồng Đăng', 167.00),
('HT056', 'Hà Nội - Hạ Long', 170.00),
('HT057', 'Sài Gòn - Lộc Ninh', 140.00),
('HT058', 'Sài Gòn - Nha Trang', 411.00),
('HT059', 'Sài Gòn - Huế', 1060.00);
-- Them du lieu vao bang Tau 
INSERT INTO DoanTau (maTau, tenTau, loaiTau, trangThai) VALUES
('DT000', 'Thống Nhất SE1', 'TauNhanh', 'SanSang'),
('DT001', 'Thống Nhất SE2', 'TauNhanh', 'SanSang'),
('DT002', 'Thống Nhất SE3', 'TauNhanh', 'SanSang'),
('DT003', 'Thống Nhất SE4', 'TauNhanh', 'SanSang'),
('DT004', 'Thống Nhất SE5', 'TauNhanh', 'SanSang'),
('DT005', 'Thống Nhất SE6', 'TauNhanh', 'SanSang'),
('DT006', 'Thống Nhất SE7', 'TauNhanh', 'SanSang'),
('DT007', 'Thống Nhất SE8', 'TauNhanh', 'SanSang'),
('DT008', 'Tàu địa phương TN1', 'TauThuong', 'SanSang'),
('DT009', 'Tàu địa phương TN2', 'TauThuong', 'SanSang'),
('DT010', 'Tàu địa phương TN3', 'TauThuong', 'SanSang'),
('DT011', 'Tàu địa phương TN4', 'TauThuong', 'SanSang'),
('DT012', 'Tàu địa phương TN5', 'TauThuong', 'SanSang'),
('DT013', 'Tàu địa phương TN6', 'TauThuong', 'SanSang'),
('DT014', 'Tàu địa phương TN7', 'TauThuong', 'SanSang'),
('DT015', 'Tàu địa phương TN8', 'TauThuong', 'SanSang'),
('DT016', 'Tàu du lịch DL1', 'TauNhanh', 'SanSang'),
('DT017', 'Tàu du lịch DL2', 'TauNhanh', 'SanSang'),
('DT018', 'Tàu hàng H1', 'TauThuong', 'SanSang'),
('DT019', 'Tàu hàng H2', 'TauThuong', 'SanSang'),
('DT020', 'Tàu nhanh QN1', 'TauNhanh', 'SanSang'),
('DT021', 'Tàu nhanh QN2', 'TauNhanh', 'SanSang'),
('DT022', 'Tàu nhanh HN1', 'TauNhanh', 'SanSang'),
('DT023', 'Tàu nhanh HN2', 'TauNhanh', 'SanSang'),
('DT024', 'Tàu nhanh SG1', 'TauNhanh', 'SanSang'),
('DT025', 'Tàu nhanh SG2', 'TauNhanh', 'SanSang'),
('DT026', 'Tàu nhanh DN1', 'TauNhanh', 'SanSang'),
('DT027', 'Tàu nhanh DN2', 'TauNhanh', 'SanSang'),
('DT028', 'Tàu nhanh NT1', 'TauNhanh', 'SanSang'),
('DT029', 'Tàu nhanh NT2', 'TauNhanh', 'SanSang'),
('DT030', 'Tàu tốc hành T030', 'TauNhanh', 'SanSang'),
('DT031', 'Tàu tốc hành T031', 'TauNhanh', 'SanSang'),
('DT032', 'Tàu tốc hành T032', 'TauNhanh', 'SanSang'),
('DT033', 'Tàu tốc hành T033', 'TauNhanh', 'SanSang'),
('DT034', 'Tàu tốc hành T034', 'TauNhanh', 'SanSang'),
('DT035', 'Tàu tốc hành T035', 'TauNhanh', 'SanSang'),
('DT036', 'Tàu tốc hành T036', 'TauNhanh', 'SanSang'),
('DT037', 'Tàu tốc hành T037', 'TauNhanh', 'SanSang'),
('DT038', 'Tàu tốc hành T038', 'TauNhanh', 'SanSang'),
('DT039', 'Tàu tốc hành T039', 'TauNhanh', 'SanSang'),
('DT040', 'Tàu địa phương L040', 'TauThuong', 'SanSang'),
('DT041', 'Tàu địa phương L041', 'TauThuong', 'SanSang'),
('DT042', 'Tàu địa phương L042', 'TauThuong', 'SanSang'),
('DT043', 'Tàu địa phương L043', 'TauThuong', 'SanSang'),
('DT044', 'Tàu địa phương L044', 'TauThuong', 'SanSang'),
('DT045', 'Tàu địa phương L045', 'TauThuong', 'SanSang'),
('DT046', 'Tàu địa phương L046', 'TauThuong', 'SanSang'),
('DT047', 'Tàu địa phương L047', 'TauThuong', 'SanSang'),
('DT048', 'Tàu địa phương L048', 'TauThuong', 'SanSang'),
('DT049', 'Tàu địa phương L049', 'TauThuong', 'SanSang'),
('DT050', 'Tàu chuyên tuyến V050', 'TauNhanh', 'SanSang'),
('DT051', 'Tàu chuyên tuyến V051', 'TauNhanh', 'SanSang'),
('DT052', 'Tàu chuyên tuyến V052', 'TauNhanh', 'SanSang'),
('DT053', 'Tàu chuyên tuyến V053', 'TauNhanh', 'SanSang'),
('DT054', 'Tàu chuyên tuyến V054', 'TauNhanh', 'SanSang'),
('DT055', 'Tàu chuyên tuyến V055', 'TauNhanh', 'SanSang'),
('DT056', 'Tàu chuyên tuyến V056', 'TauNhanh', 'SanSang'),
('DT057', 'Tàu chuyên tuyến V057', 'TauNhanh', 'SanSang'),
('DT058', 'Tàu chuyên tuyến V058', 'TauNhanh', 'SanSang'),
('DT059', 'Tàu chuyên tuyến V059', 'TauNhanh', 'SanSang');

-- Them du lieu vao bang QuanLy 
INSERT INTO QuanLy (id, maQuanLy) VALUES
(1, 'QL_HANOI_01'),
(2, 'QL_HANOI_02'),
(3, 'QL_HANOI_03'),
(4, 'QL_HANOI_04'),
(5, 'QL_HANOI_05'),
(6, 'QL_HANOI_06'),
(7, 'QL_HANOI_07'),
(8, 'QL_HANOI_08'),
(9, 'QL_HANOI_09'),
(10, 'QL_HANOI_10');

-- Them du lieu vao bang NhanVien 
INSERT INTO NhanVien (id, maNhanVien) VALUES
(11, 'NV_QUAY_01'),
(12, 'NV_QUAY_02'),
(13, 'NV_QUAY_03'),
(14, 'NV_QUAY_04'),
(15, 'NV_QUAY_05'),
(16, 'NV_QUAY_06'),
(17, 'NV_QUAY_07'),
(18, 'NV_QUAY_08'),
(19, 'NV_QUAY_09'),
(20, 'NV_QUAY_10'),
(21, 'NV_QUAY_11'),
(22, 'NV_QUAY_12'),
(23, 'NV_QUAY_13'),
(24, 'NV_QUAY_14'),
(25, 'NV_QUAY_15'),
(26, 'NV_QUAY_16'),
(27, 'NV_QUAY_17');
-- Them Nha Ga
-- ==============================================================================
-- CẬP NHẬT LỆNH INSERT CHO BẢNG NHÀ GA (NhaGa)
-- Đảm bảo khớp cấu trúc cột: id, maGa, tenNhaGa, diaChi, soDienThoai, quanLyId
-- ==============================================================================
INSERT INTO NhaGa (id, maGa, tenNhaGa, diaChi, soDienThoai, quanLyId) VALUES
(1, 'GA_HANOI', 'Ga Hà Nội', '120 Lê Duẩn, Đống Đa, Hà Nội', '02439423697', 1),
(2, 'GA_VINH', 'Ga Vinh', 'Số 1 Lê Lợi, Phường Lê Lợi, TP. Vinh, Nghệ An', '02383844755', 1),
(3, 'GA_DANANG', 'Ga Đà Nẵng', '200 Hải Phòng, Tân Chính, Thanh Khê, Đà Nẵng', '02363821175', 1),
(4, 'GA_SAIGON', 'Ga Sài Gòn', '1 Nguyễn Thông, Phường 9, Quận 3, TP. Hồ Chí Minh', '02838436528', 2),
(5, 'GA_HAIPHONG', 'Ga Hải Phòng', '75 Lương Khánh Thiện, Ngô Quyền, Hải Phòng', '02253921333', 2),
(6, 'GA_NAMDINH', 'Ga Nam Định', '2 Trần Đăng Ninh, TP. Nam Định, Nam Định', '02283835222', 2),
(7, 'GA_NINHBINH', 'Ga Ninh Bình', 'Ngõ 41 Hoàng Diệu, Thanh Bình, TP. Ninh Bình, Ninh Bình', '02293633222', 3),
(8, 'GA_THANHHOA', 'Ga Thanh Hóa', '19 Dương Đình Nghệ, Tân Sơn, TP. Thanh Hóa, Thanh Hóa', '02373752222', 3),
(9, 'GA_DONGHOI', 'Ga Đồng Hới', 'Tiểu khu 4, Phường Bắc Lý, TP. Đồng Hới, Quảng Bình', '02323822222', 3),
(10, 'GA_HUE', 'Ga Huế', '2 Bùi Thị Xuân, Phường Đúc, TP. Huế, Thừa Thiên Huế', '02343822222', 4),
(11, 'GA_TAMKY', 'Ga Tam Kỳ', 'Nguyễn Hoàng, Phường An Xuân, TP. Tam Kỳ, Quảng Nam', '02353822222', 4),
(12, 'GA_QUANGNGAI', 'Ga Quảng Ngãi', '204 Nguyễn Chánh, Phường Trần Phú, TP. Quảng Ngãi, Quảng Ngãi', '02553822222', 4),
(13, 'GA_QUYNHON', 'Ga Quy Nhơn', 'Lê Hồng Phong, Phường Ngô Mây, TP. Quy Nhơn, Bình Định', '02563822222', 5),
(14, 'GA_DIEUTRI', 'Ga Diêu Trì', 'Thị trấn Diêu Trì, Huyện Tuy Phước, Bình Định', '02563833333', 5),
(15, 'GA_TUYHOA', 'Ga Tuy Hòa', '271 Lê Trung Kiên, Phường 2, TP. Tuy Hòa, Phú Yên', '02573822222', 5),
(16, 'GA_NHATRANG', 'Ga Nha Trang', '17 Thái Nguyên, Phước Tân, TP. Nha Trang, Khánh Hòa', '02583822222', 6),
(17, 'GA_BINHTHUAN', 'Ga Bình Thuận', 'Xã Hàm Hiệp, Huyện Hàm Thuận Bắc, Bình Thuận', '02523822222', 6),
(18, 'GA_PHANTHIET', 'Ga Phan Thiết', 'Phong Nẫm, TP. Phan Thiết, Bình Thuận', '02523833333', 6),
(19, 'GA_LONGKHANH', 'Ga Long Khánh', 'Hùng Vương, Xuân An, TP. Long Khánh, Đồng Nai', '02513822222', 7),
(20, 'GA_BIENHOA', 'Ga Biên Hòa', 'Hưng Đạo Vương, Trung Dũng, TP. Biên Hòa, Đồng Nai', '02513833333', 7),
(21, 'GA_LAOCAI', 'Ga Lào Cai', 'Tổ 25, Phường Phố Mới, TP. Lào Cai, Lào Cai', '02143822222', 7),
(22, 'GA_YENBAI', 'Ga Yên Bái', '218 Nguyễn Văn Cừ, Phường Đồng Tâm, TP. Yên Bái, Yên Bái', '02163822222', 8),
(23, 'GA_THAINGUYEN', 'Ga Thái Nguyên', 'Phường Quang Trung, TP. Thái Nguyên, Thái Nguyên', '02083822222', 8),
(24, 'GA_LANGSON', 'Ga Lạng Sơn', 'Lê Lợi, Phường Vĩnh Trại, TP. Lạng Sơn, Lạng Sơn', '02053822222', 8),
(25, 'GA_PHULY', 'Ga Phủ Lý', 'Phường Hai Bà Trưng, TP. Phủ Lý, Hà Nam', '02263822222', 9),
(26, 'GA_HAIDUONG', 'Ga Hải Dương', 'Hồng Quang, Phường Quang Trung, TP. Hải Dương, Hải Dương', '02203822222', 9),
(27, 'GA_HALONG', 'Ga Hạ Long', 'Giếng Đáy, TP. Hạ Long, Quảng Ninh', '02033822222', 9),
(28, 'GA_DONGDANG', 'Ga Đồng Đăng', 'Thị trấn Đồng Đăng, Huyện Cao Lộc, Lạng Sơn', '02053833333', 10),
(29, 'GA_BIMSON', 'Ga Bỉm Sơn', 'Phường Ngọc Trạo, Thị xã Bỉm Sơn, Thanh Hóa', '02373833333', 10),
(30, 'GA_DONGHA', 'Ga Đông Hà', '2 Hùng Vương, Phường 1, TP. Đông Hà, Quảng Trị', '02333822222', 10);

-- Them Toa Tau
INSERT INTO ToaTau (maToa, tenToa, soThuTu, loaiToa, soLuongGheToiDa, moTa, doanTauId) VALUES
-- Tàu Thống Nhất SE1 (id = 1)
('TOA01_DT1', 'Toa 1 - Ngồi mềm điều hòa', 1, 'NgoiMem', 64, 'Toa ghế mềm, điều hòa trung tâm', 1),
('TOA02_DT1', 'Toa 2 - Ngồi mềm điều hòa', 2, 'NgoiMem', 64, 'Toa ghế mềm, điều hòa trung tâm', 1),
('TOA03_DT1', 'Toa 3 - Ngồi cứng', 3, 'NgoiCung', 80, 'Toa ghế gỗ truyền thống', 1),
('TOA04_DT1', 'Toa 4 - Giường nằm khoang 4', 4, 'GiuongNam', 28, 'Khoang nằm VIP, đệm mềm', 1),
('TOA05_DT1', 'Toa 5 - Giường nằm khoang 6', 5, 'GiuongNam', 42, 'Khoang nằm tiêu chuẩn', 1),

-- Tàu Thống Nhất SE2 (id = 2)
('TOA01_DT2', 'Toa 1 - Ngồi mềm điều hòa', 1, 'NgoiMem', 64, 'Toa ghế mềm chất lượng cao', 2),
('TOA02_DT2', 'Toa 2 - Ngồi mềm điều hòa', 2, 'NgoiMem', 64, 'Toa ghế mềm chất lượng cao', 2),
('TOA03_DT2', 'Toa 3 - Giường nằm khoang 4', 3, 'GiuongNam', 28, 'Khoang nằm VIP hạng thương gia', 2),
('TOA04_DT2', 'Toa 4 - Giường nằm khoang 4', 4, 'GiuongNam', 28, 'Khoang nằm VIP hạng thương gia', 2),
('TOA05_DT2', 'Toa 5 - Giường nằm khoang 6', 5, 'GiuongNam', 42, 'Khoang nằm gia đình', 2),

-- Tàu Thống Nhất SE3 (id = 3)
('TOA01_DT3', 'Toa 1 - Ngồi cứng', 1, 'NgoiCung', 80, 'Toa thường không điều hòa', 3),
('TOA02_DT3', 'Toa 2 - Ngồi cứng', 2, 'NgoiCung', 80, 'Toa thường không điều hòa', 3),
('TOA03_DT3', 'Toa 3 - Ngồi mềm điều hòa', 3, 'NgoiMem', 64, 'Toa ghế mềm tiêu chuẩn', 3),
('TOA04_DT3', 'Toa 4 - Ngồi mềm điều hòa', 4, 'NgoiMem', 64, 'Toa ghế mềm tiêu chuẩn', 3),
('TOA05_DT3', 'Toa 5 - Giường nằm khoang 6', 5, 'GiuongNam', 42, 'Toa giường nằm tiêu chuẩn', 3),

-- Tàu Thống Nhất SE4 (id = 4)
('TOA01_DT4', 'Toa 1 - Giường nằm khoang 4', 1, 'GiuongNam', 28, 'Toa du lịch chất lượng cao', 4),
('TOA02_DT4', 'Toa 2 - Giường nằm khoang 4', 2, 'GiuongNam', 28, 'Toa du lịch chất lượng cao', 4),
('TOA03_DT4', 'Toa 3 - Giường nằm khoang 4', 3, 'GiuongNam', 28, 'Toa du lịch chất lượng cao', 4),
('TOA04_DT4', 'Toa 4 - Ngồi mềm điều hòa', 4, 'NgoiMem', 64, 'Toa phục vụ khách chặng ngắn', 4),
('TOA05_DT4', 'Toa 5 - Ngồi mềm điều hòa', 5, 'NgoiMem', 64, 'Toa phục vụ khách chặng ngắn', 4),

-- Tàu Thống Nhất SE5 (id = 5)
('TOA01_DT5', 'Toa 1 - Ngồi cứng', 1, 'NgoiCung', 80, 'Toa xe chở khách phổ thông', 5),
('TOA02_DT5', 'Toa 2 - Ngồi cứng', 2, 'NgoiCung', 80, 'Toa xe chở khách phổ thông', 5),
('TOA03_DT5', 'Toa 3 - Ngồi mềm điều hòa', 3, 'NgoiMem', 64, 'Toa điều hòa giá rẻ', 5),
('TOA04_DT5', 'Toa 4 - Ngồi mềm điều hòa', 4, 'NgoiMem', 64, 'Toa điều hòa giá rẻ', 5),
('TOA05_DT5', 'Toa 5 - Giường nằm khoang 6', 5, 'GiuongNam', 42, 'Toa nằm phổ thông', 5),

-- Tàu Thống Nhất SE6 (id = 6)
('TOA01_DT6', 'Toa 1 - Ngồi mềm điều hòa', 1, 'NgoiMem', 64, 'Toa ghế mềm cao cấp', 6),
('TOA02_DT6', 'Toa 2 - Ngồi mềm điều hòa', 2, 'NgoiMem', 64, 'Toa ghế mềm cao cấp', 6),
('TOA03_DT6', 'Toa 3 - Giường nằm khoang 4', 3, 'GiuongNam', 28, 'Khoang giường nằm êm ái', 6),
('TOA04_DT6', 'Toa 4 - Giường nằm khoang 4', 4, 'GiuongNam', 28, 'Khoang giường nằm êm ái', 6),
('TOA05_DT6', 'Toa 5 - Giường nằm khoang 6', 5, 'GiuongNam', 42, 'Khoang nằm gia đình', 6);

-- Them ghe ngoi 
INSERT INTO GheNgoi (id, maGhe, soGhe, viTri, trangThai, moTa, toaTauId) VALUES
-- Toa 1 - Ngồi mềm điều hòa (toaTauId = 1, mã toa: TOA01_DT1)
(9, 'TOA01_DT1_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 1', 1),
(10, 'TOA01_DT1_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 2', 1),
(11, 'TOA01_DT1_G03', 3, 'CuaSo', 'DaDat', 'Ghế ngồi mềm số 3', 1),
(12, 'TOA01_DT1_G04', 4, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 4', 1),
(13, 'TOA01_DT1_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 5', 1),

-- Toa 3 - Ngồi cứng (toaTauId = 3, mã toa: TOA03_DT1)
(14, 'TOA03_DT1_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 1', 3),
(15, 'TOA03_DT1_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 2', 3),
(16, 'TOA03_DT1_G03', 3, 'CuaSo', 'DaDat', 'Ghế ngồi cứng số 3', 3),
(17, 'TOA03_DT1_G04', 4, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 4', 3),
(18, 'TOA03_DT1_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 5', 3),

-- Toa 4 - Giường nằm khoang 4 (toaTauId = 4, mã toa: TOA04_DT1)
(19, 'TOA04_DT1_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 4),
(20, 'TOA04_DT1_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 4),
(21, 'TOA04_DT1_G03', 3, 'GiuongTang1', 'DaDat', 'Giường nằm tầng 1 số 3', 4),
(22, 'TOA04_DT1_G04', 4, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 4', 4),
(23, 'TOA04_DT1_G05', 5, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 5', 4),

-- Toa 5 - Giường nằm khoang 6 (toaTauId = 5, mã toa: TOA05_DT1)
(24, 'TOA05_DT1_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 5),
(25, 'TOA05_DT1_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 5),
(26, 'TOA05_DT1_G03', 3, 'GiuongTang2', 'DaDat', 'Giường nằm tầng 2 số 3', 5),
(27, 'TOA05_DT1_G04', 4, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 4', 5),
(28, 'TOA05_DT1_G05', 5, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 5', 5),

-- Toa 1 - Ngồi cứng (toaTauId = 11, mã toa: TOA01_DT3)
(29, 'TOA01_DT3_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 1', 11),
(30, 'TOA01_DT3_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 2', 11),
(31, 'TOA01_DT3_G03', 3, 'CuaSo', 'DaDat', 'Ghế ngồi cứng số 3', 11),
(32, 'TOA01_DT3_G04', 4, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 4', 11),
(33, 'TOA01_DT3_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 5', 11),

-- Toa 5 - Giường nằm khoang 6 (toaTauId = 15, mã toa: TOA05_DT3)
(34, 'TOA05_DT3_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 15),
(35, 'TOA05_DT3_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 15),
(36, 'TOA05_DT3_G03', 3, 'GiuongTang2', 'DaDat', 'Giường nằm tầng 2 số 3', 15),
(37, 'TOA05_DT3_G04', 4, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 4', 15),
(38, 'TOA05_DT3_G05', 5, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 5', 15),
(39, 'TOA01_DT4_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 16),
(40, 'TOA01_DT4_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 16),
(41, 'TOA01_DT4_G03', 3, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 3', 16),
(42, 'TOA01_DT4_G04', 4, 'GiuongTang2', 'DaDat', 'Giường nằm tầng 2 số 4', 16),
(43, 'TOA01_DT4_G05', 5, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 5', 16),
(44, 'TOA01_DT4_G06', 6, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 6', 16),

-- Toa 2 - Giường nằm khoang 4 (toaTauId = 17, mã toa: TOA02_DT4)
(45, 'TOA02_DT4_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 17),
(46, 'TOA02_DT4_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 17),
(47, 'TOA02_DT4_G03', 3, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 3', 17),
(48, 'TOA02_DT4_G04', 4, 'GiuongTang2', 'DaDat', 'Giường nằm tầng 2 số 4', 17),
(49, 'TOA02_DT4_G05', 5, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 5', 17),
(50, 'TOA02_DT4_G06', 6, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 6', 17),

-- Toa 4 - Ngồi mềm điều hòa (toaTauId = 19, mã toa: TOA04_DT4)
(51, 'TOA04_DT4_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 1', 19),
(52, 'TOA04_DT4_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 2', 19),
(53, 'TOA04_DT4_G03', 3, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 3', 19),
(54, 'TOA04_DT4_G04', 4, 'LoiDi', 'DaDat', 'Ghế ngồi mềm số 4', 19),
(55, 'TOA04_DT4_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 5', 19),
(56, 'TOA04_DT4_G06', 6, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 6', 19),

-- Toa 1 - Ngồi cứng (toaTauId = 21, mã toa: TOA01_DT5)
(57, 'TOA01_DT5_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 1', 21),
(58, 'TOA01_DT5_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 2', 21),
(59, 'TOA01_DT5_G03', 3, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 3', 21),
(60, 'TOA01_DT5_G04', 4, 'LoiDi', 'DaDat', 'Ghế ngồi cứng số 4', 21),
(61, 'TOA01_DT5_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi cứng số 5', 21),
(62, 'TOA01_DT5_G06', 6, 'LoiDi', 'Trong', 'Ghế ngồi cứng số 6', 21),

-- Toa 3 - Ngồi mềm điều hòa (toaTauId = 23, mã toa: TOA03_DT5)
(63, 'TOA03_DT5_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 1', 23),
(64, 'TOA03_DT5_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 2', 23),
(65, 'TOA03_DT5_G03', 3, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 3', 23),
(66, 'TOA03_DT5_G04', 4, 'LoiDi', 'DaDat', 'Ghế ngồi mềm số 4', 23),
(67, 'TOA03_DT5_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 5', 23),
(68, 'TOA03_DT5_G06', 6, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 6', 23),

-- Toa 1 - Ngồi mềm điều hòa (toaTauId = 26, mã toa: TOA01_DT6)
(69, 'TOA01_DT6_G01', 1, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 1', 26),
(70, 'TOA01_DT6_G02', 2, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 2', 26),
(71, 'TOA01_DT6_G03', 3, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 3', 26),
(72, 'TOA01_DT6_G04', 4, 'LoiDi', 'DaDat', 'Ghế ngồi mềm số 4', 26),
(73, 'TOA01_DT6_G05', 5, 'CuaSo', 'Trong', 'Ghế ngồi mềm số 5', 26),
(74, 'TOA01_DT6_G06', 6, 'LoiDi', 'Trong', 'Ghế ngồi mềm số 6', 26),

-- Toa 4 - Giường nằm khoang 4 (toaTauId = 29, mã toa: TOA04_DT6)
(75, 'TOA04_DT6_G01', 1, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 1', 29),
(76, 'TOA04_DT6_G02', 2, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 2', 29),
(77, 'TOA04_DT6_G03', 3, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 3', 29),
(78, 'TOA04_DT6_G04', 4, 'GiuongTang2', 'DaDat', 'Giường nằm tầng 2 số 4', 29),
(79, 'TOA04_DT6_G05', 5, 'GiuongTang1', 'Trong', 'Giường nằm tầng 1 số 5', 29),
(80, 'TOA04_DT6_G06', 6, 'GiuongTang2', 'Trong', 'Giường nằm tầng 2 số 6', 29);

-- Them chi tiet hanh trinh 
INSERT INTO ChiTietHanhTrinh (id, maCTHT, thuTuGa, nhaGaId, hanhTrinhId) VALUES
-- Tuyển HT000: Hà Nội - Hải Phòng
(5, 'CTHT_HT000_01', 1, 1, 1),
(1004, 'CTHT_HT000_02_MID', 2, 26, 1),
(6, 'CTHT_HT000_03', 3, 5, 1),

-- Tuyến HT001: Hà Nội - Lào Cai
(7, 'CTHT_HT001_01', 1, 1, 2),
(1005, 'CTHT_HT001_02_MID', 2, 22, 2),
(8, 'CTHT_HT001_03', 3, 21, 2),

-- Tuyến HT002: Hà Nội - Quán Triều
(9, 'CTHT_HT002_01', 1, 1, 3),
(10, 'CTHT_HT002_02', 2, 23, 3),

-- Tuyến HT003: Sài Gòn - Nha Trang
(11, 'CTHT_HT003_01', 1, 4, 4),
(1006, 'CTHT_HT003_02_MID', 2, 20, 4),
(1007, 'CTHT_HT003_03_MID', 3, 17, 4),
(12, 'CTHT_HT003_04', 4, 16, 4),

-- Tuyến HT004: Sài Gòn - Quy Nhơn
(13, 'CTHT_HT004_01', 1, 4, 5),
(14, 'CTHT_HT004_02', 2, 13, 5),

-- Tuyến HT005: Sài Gòn - Đà Nẵng
(15, 'CTHT_HT005_01', 1, 4, 6),
(1008, 'CTHT_HT005_02_MID', 2, 16, 6),
(1009, 'CTHT_HT005_03_MID', 3, 13, 6),
(1010, 'CTHT_HT005_04_MID', 4, 12, 6),
(16, 'CTHT_HT005_05', 5, 3, 6),

-- Tuyến HT006: Sài Gòn - Huế
(17, 'CTHT_HT006_01', 1, 4, 7),
(18, 'CTHT_HT006_02', 2, 10, 7),

-- Tuyến HT007: Đà Nẵng - Huế
(19, 'CTHT_HT007_01', 1, 3, 8),
(20, 'CTHT_HT007_02', 2, 10, 8),

-- Tuyến HT008: Huế - Vinh
(21, 'CTHT_HT008_01', 1, 10, 9),
(22, 'CTHT_HT008_02', 2, 2, 9),

-- Tuyến HT009: Vinh - Thanh Hóa
(23, 'CTHT_HT009_01', 1, 2, 10),
(24, 'CTHT_HT009_02', 2, 8, 10),

-- Tuyến HT010: Thanh Hóa - Nam Định
(25, 'CTHT_HT010_01', 1, 8, 11),
(26, 'CTHT_HT010_02', 2, 6, 11),

-- Tuyến HT011: Sài Gòn - Phan Thiết
(27, 'CTHT_HT011_01', 1, 4, 12),
(1011, 'CTHT_HT011_02_MID', 2, 20, 12),
(28, 'CTHT_HT011_03', 3, 18, 12),

-- Tuyến HT012: Nha Trang - Đà Nẵng
(29, 'CTHT_HT012_01', 1, 16, 13),
(30, 'CTHT_HT012_02', 2, 3, 13),

-- Tuyến HT013: Đà Nẵng - Quy Nhơn
(31, 'CTHT_HT013_01', 1, 3, 14),
(32, 'CTHT_HT013_02', 2, 13, 14),

-- Tuyến HT014: Quy Nhơn - Nha Trang
(33, 'CTHT_HT014_01', 1, 13, 15),
(34, 'CTHT_HT014_02', 2, 16, 15),

-- Tuyến HT015: Hà Nội - Vinh
(35, 'CTHT_HT015_01', 1, 1, 16),
(1001, 'CTHT_HT015_02_MID', 2, 6, 16),
(1002, 'CTHT_HT015_03_MID', 3, 7, 16),
(1003, 'CTHT_HT015_04_MID', 4, 8, 16),
(36, 'CTHT_HT015_05', 5, 2, 16),

-- Tuyến HT016: Vinh - Quảng Bình
(37, 'CTHT_HT016_01', 1, 2, 17),
(38, 'CTHT_HT016_02', 2, 9, 17),

-- Tuyến HT017: Quảng Bình - Huế
(39, 'CTHT_HT017_01', 1, 9, 18),
(40, 'CTHT_HT017_02', 2, 10, 18),

-- Tuyến HT018: Hà Nội - Thanh Hóa
(41, 'CTHT_HT018_01', 1, 1, 19),
(1012, 'CTHT_HT018_02_MID', 2, 25, 19),
(1013, 'CTHT_HT018_03_MID', 3, 6, 19),
(1014, 'CTHT_HT018_04_MID', 4, 7, 19),
(1015, 'CTHT_HT018_05_MID', 5, 29, 19),
(42, 'CTHT_HT018_06', 6, 8, 19),

-- Tuyến HT019: Thanh Hóa - Quảng Bình
(43, 'CTHT_HT019_01', 1, 8, 20),
(44, 'CTHT_HT019_02', 2, 9, 20),

-- Tuyến HT020: Sài Gòn - Bình Thuận
(45, 'CTHT_HT020_01', 1, 4, 21),
(46, 'CTHT_HT020_02', 2, 17, 21),

-- Tuyến HT021: Bình Thuận - Nha Trang
(47, 'CTHT_HT021_01', 1, 17, 22),
(48, 'CTHT_HT021_02', 2, 16, 22),

-- Tuyến HT022: Nha Trang - Phú Yên
(49, 'CTHT_HT022_01', 1, 16, 23),
(50, 'CTHT_HT022_02', 2, 15, 23),

-- Tuyến HT023: Phú Yên - Quy Nhơn
(51, 'CTHT_HT023_01', 1, 15, 24),
(52, 'CTHT_HT023_02', 2, 13, 24),

-- Tuyến HT024: Quy Nhơn - Diêu Trì
(53, 'CTHT_HT024_01', 1, 13, 25),
(54, 'CTHT_HT024_02', 2, 14, 25),

-- Tuyến HT025: Diêu Trì - Đà Nẵng
(55, 'CTHT_HT025_01', 1, 14, 26),
(56, 'CTHT_HT025_02', 2, 3, 26),

-- Tuyến HT026: Đà Nẵng - Hà Nội
(57, 'CTHT_HT026_01', 1, 3, 27),
(1016, 'CTHT_HT026_02_MID', 2, 10, 27),
(1017, 'CTHT_HT026_03_MID', 3, 9, 27),
(1018, 'CTHT_HT026_04_MID', 4, 2, 27),
(1019, 'CTHT_HT026_05_MID', 5, 8, 27),
(1020, 'CTHT_HT026_06_MID', 6, 6, 27),
(58, 'CTHT_HT026_07', 7, 1, 27),

-- Tuyến HT027: Hà Nội - Nam Định
(59, 'CTHT_HT027_01', 1, 1, 28),
(60, 'CTHT_HT027_02', 2, 6, 28),

-- Tuyến HT028: Nam Định - Ninh Bình
(61, 'CTHT_HT028_01', 1, 6, 29),
(62, 'CTHT_HT028_02', 2, 7, 29),

-- Tuyến HT029: Ninh Bình - Vinh
(63, 'CTHT_HT029_01', 1, 7, 30),
(64, 'CTHT_HT029_02', 2, 2, 30),


-- Tuyến HT030: Vinh - Đồng Hới (hanhTrinhId = 32)
(65, 'CTHT_HT030_01', 1, 2, 31),
(66, 'CTHT_HT030_02', 2, 9, 31),

-- Tuyến HT031: Đồng Hới - Đông Hà (hanhTrinhId = 33)
(67, 'CTHT_HT031_01', 1, 9, 32),
(68, 'CTHT_HT031_02', 2, 30, 32),

-- Tuyến HT032: Đông Hà - Huế (hanhTrinhId = 34)
(69, 'CTHT_HT032_01', 1, 30, 33),
(70, 'CTHT_HT032_02', 2, 10, 33),

-- Tuyến HT033: Huế - Đà Nẵng (hanhTrinhId = 35)
(71, 'CTHT_HT033_01', 1, 10, 34),
(72, 'CTHT_HT033_02', 2, 3, 34),

-- Tuyến HT034: Đà Nẵng - Tam Kỳ (hanhTrinhId = 36)
(73, 'CTHT_HT034_01', 1, 3, 35),
(74, 'CTHT_HT034_02', 2, 11, 35),

-- Tuyến HT035: Tam Kỳ - Quảng Ngãi (hanhTrinhId = 37)
(75, 'CTHT_HT035_01', 1, 11, 36),
(76, 'CTHT_HT035_02', 2, 12, 36),

-- Tuyến HT036: Quảng Ngãi - Bồng Sơn (hanhTrinhId = 38)
(77, 'CTHT_HT036_01', 1, 12, 37),
(78, 'CTHT_HT036_02', 2, 13, 37),

-- Tuyến HT037: Bồng Sơn - Diêu Trì (hanhTrinhId = 39)
(79, 'CTHT_HT037_01', 1, 13, 38),
(80, 'CTHT_HT037_02', 2, 14, 38),

-- Tuyến HT038: Diêu Trì - Tuy Hòa (hanhTrinhId = 40)
(81, 'CTHT_HT038_01', 1, 14, 39),
(82, 'CTHT_HT038_02', 2, 15, 39),

-- Tuyến HT039: Tuy Hòa - Nha Trang (hanhTrinhId = 41)
(83, 'CTHT_HT039_01', 1, 15, 40),
(84, 'CTHT_HT039_02', 2, 16, 40),

-- Tuyến HT040: Nha Trang - Tháp Chàm (hanhTrinhId = 42)
(85, 'CTHT_HT040_01', 1, 16, 41),
(86, 'CTHT_HT040_02', 2, 17, 41),

-- Tuyến HT041: Tháp Chàm - Bình Thuận (hanhTrinhId = 43)
(87, 'CTHT_HT041_01', 1, 17, 42),
(88, 'CTHT_HT041_02', 2, 18, 42), -- Đã sửa từ Ga 17 thành Ga 18 để tránh trùng lặp điểm đi/đến

-- Tuyến HT042: Bình Thuận - Long Khánh (hanhTrinhId = 44)
(89, 'CTHT_HT042_01', 1, 17, 43),
(90, 'CTHT_HT042_02', 2, 19, 43),

-- Tuyến HT043: Long Khánh - Đà Nẵng (hanhTrinhId = 45)
(91, 'CTHT_HT043_01', 1, 19, 44),
(92, 'CTHT_HT043_02', 2, 3, 44),

-- Tuyến HT044: Biên Hòa - Sài Gòn (hanhTrinhId = 46)
(93, 'CTHT_HT044_01', 1, 20, 45),
(94, 'CTHT_HT044_02', 2, 4, 45),

-- Tuyến HT045: Hà Nội - Phủ Lý (hanhTrinhId = 47)
(95, 'CTHT_HT045_01', 1, 1, 46),
(96, 'CTHT_HT045_02', 2, 25, 46),

-- Tuyến HT046: Phủ Lý - Nam Định (hanhTrinhId = 48)
(97, 'CTHT_HT046_01', 1, 25, 47),
(98, 'CTHT_HT046_02', 2, 6, 47),

-- Tuyến HT047: Nam Định - Ninh Bình (hanhTrinhId = 49)
(99, 'CTHT_HT047_01', 1, 6, 48),
(100, 'CTHT_HT047_02', 2, 7, 48),

-- Tuyến HT048: Ninh Bình - Đồng Hới (hanhTrinhId = 50)
(101, 'CTHT_HT048_01', 1, 7, 49),
(102, 'CTHT_HT048_02', 2, 9, 49),

-- Tuyến HT049: Bỉm Sơn - Thanh Hóa (hanhTrinhId = 51)
(103, 'CTHT_HT049_01', 1, 29, 50),
(104, 'CTHT_HT049_02', 2, 8, 50);

-- Them lich trinh 
-- ==============================================================================
-- BỔ SUNG 50 BẢN GHI CHO BẢNG LỊCH TRÌNH (LichTrinh) THEO ĐÚNG MẪU CHUẨN CSV
-- Đã cập nhật trangThai thành hệ thống chuẩn: DaHoanThanh, DangChay, ChuaChay
-- ==============================================================================
INSERT INTO LichTrinh (id, maLichTrinh, ngayKhoiHanh, trangThai, doanTauId, hanhTrinhId, quanLyId) VALUES
(2, 'LT_SE1_20260602', '2026-06-02 08:00:00', 'DaHoanThanh', 1, 1, 6),


(3, 'LT_DT000_20260602', '2026-06-02 12:00:00', 'DaHoanThanh', 2, 1, 8),


(4, 'LT_DT001_20260603', '2026-06-03 08:00:00', 'DaHoanThanh', 3, 2, 10),


(5, 'LT_DT002_20260603', '2026-06-03 12:00:00', 'DaHoanThanh', 4, 3, 2),


(6, 'LT_DT003_20260604', '2026-06-04 08:00:00', 'DaHoanThanh', 5, 4, 4),


(7, 'LT_DT004_20260604', '2026-06-04 12:00:00', 'DaHoanThanh', 6, 5, 6),


(8, 'LT_DT005_20260605', '2026-06-05 08:00:00', 'DaHoanThanh', 7, 6, 8),


(9, 'LT_DT006_20260605', '2026-06-05 12:00:00', 'DaHoanThanh', 8, 7, 10),


(10, 'LT_DT007_20260606', '2026-06-06 08:00:00', 'DaHoanThanh', 9, 8, 2),


(11, 'LT_DT008_20260606', '2026-06-06 12:00:00', 'DaHoanThanh', 10, 9, 4),


(12, 'LT_DT009_20260607', '2026-06-07 08:00:00', 'DaHoanThanh', 11, 10, 6),


(13, 'LT_DT010_20260607', '2026-06-07 12:00:00', 'DaHoanThanh', 12, 11, 8),


(14, 'LT_DT011_20260608', '2026-06-08 08:00:00', 'DaHoanThanh', 13, 12, 10),


(15, 'LT_DT012_20260608', '2026-06-08 12:00:00', 'DangChay', 14, 13, 2),


(16, 'LT_DT013_20260609', '2026-06-09 08:00:00', 'DangChay', 15, 14, 4),


(17, 'LT_DT014_20260609', '2026-06-09 12:00:00', 'DangChay', 16, 15, 6),


(18, 'LT_DT015_20260610', '2026-06-10 08:00:00', 'DangChay', 17, 16, 8),


(19, 'LT_DT016_20260610', '2026-06-10 12:00:00', 'DangChay', 18, 17, 10),


(20, 'LT_DT017_20260611', '2026-06-11 08:00:00', 'DangChay', 19, 18, 2),


(21, 'LT_DT018_20260611', '2026-06-11 12:00:00', 'DangChay', 20, 19, 4),


(22, 'LT_DT019_20260612', '2026-06-12 08:00:00', 'DangChay', 21, 20, 6),


(23, 'LT_DT020_20260612', '2026-06-12 12:00:00', 'DangChay', 22, 21, 8),


(24, 'LT_DT021_20260613', '2026-06-13 08:00:00', 'DangChay', 23, 22, 10),


(25, 'LT_DT022_20260613', '2026-06-13 12:00:00', 'DangChay', 24, 23, 2),


(26, 'LT_DT023_20260614', '2026-06-14 08:00:00', 'DangChay', 25, 24, 4),


(27, 'LT_DT024_20260614', '2026-06-14 12:00:00', 'DangChay', 26, 25, 6),


(28, 'LT_DT025_20260615', '2026-06-15 08:00:00', 'DangChay', 27, 26, 8),


(29, 'LT_DT026_20260615', '2026-06-15 12:00:00', 'DangChay', 28, 27, 10),


(30, 'LT_DT027_20260616', '2026-06-16 08:00:00', 'ChuaChay', 29, 28, 2),


(31, 'LT_DT028_20260616', '2026-06-16 12:00:00', 'ChuaChay', 30, 29, 4),


(32, 'LT_DT029_20260617', '2026-06-17 08:00:00', 'ChuaChay', 31, 30, 6),


(33, 'LT_DT030_20260617', '2026-06-17 12:00:00', 'ChuaChay', 32, 31, 8),


(34, 'LT_DT031_20260618', '2026-06-18 08:00:00', 'ChuaChay', 33, 32, 10),


(35, 'LT_DT032_20260618', '2026-06-18 12:00:00', 'ChuaChay', 34, 33, 2),


(36, 'LT_DT033_20260619', '2026-06-19 08:00:00', 'ChuaChay', 35, 34, 4),


(37, 'LT_DT034_20260619', '2026-06-19 12:00:00', 'ChuaChay', 36, 35, 6),


(38, 'LT_DT035_20260620', '2026-06-20 08:00:00', 'ChuaChay', 37, 36, 8),


(39, 'LT_DT036_20260620', '2026-06-20 12:00:00', 'ChuaChay', 38, 37, 10),


(40, 'LT_DT037_20260621', '2026-06-21 08:00:00', 'ChuaChay', 39, 38, 2),


(41, 'LT_DT038_20260621', '2026-06-21 12:00:00', 'ChuaChay', 40, 39, 4),


(42, 'LT_DT039_20260622', '2026-06-22 08:00:00', 'ChuaChay', 41, 40, 6),


(43, 'LT_DT040_20260622', '2026-06-22 12:00:00', 'ChuaChay', 42, 41, 8),


(44, 'LT_DT041_20260623', '2026-06-23 08:00:00', 'ChuaChay', 43, 42, 10),


(45, 'LT_DT042_20260623', '2026-06-23 12:00:00', 'ChuaChay', 44, 43, 2),


(46, 'LT_DT043_20260624', '2026-06-24 08:00:00', 'ChuaChay', 45, 44, 4),


(47, 'LT_DT044_20260624', '2026-06-24 12:00:00', 'ChuaChay', 46, 45, 6),


(48, 'LT_DT045_20260625', '2026-06-25 08:00:00', 'ChuaChay', 47, 46, 8),


(49, 'LT_DT046_20260625', '2026-06-25 12:00:00', 'ChuaChay', 48, 47, 10),


(50, 'LT_DT047_20260626', '2026-06-26 08:00:00', 'ChuaChay', 49, 48, 2),


(51, 'LT_DT048_20260626', '2026-06-26 12:00:00', 'ChuaChay', 50, 49, 4),


(52, 'LT_DT049_20260627', '2026-06-27 08:00:00', 'ChuaChay', 51, 50, 6),


(53, 'LT_DT050_20260627', '2026-06-27 12:00:00', 'ChuaChay', 52, 51, 8),


(54, 'LT_DT051_20260628', '2026-06-28 08:00:00', 'DaHoanThanh', 53, 52, 10),


(55, 'LT_DT052_20260628', '2026-06-28 12:00:00', 'DangChay', 54, 53, 2),


(56, 'LT_DT053_20260629', '2026-06-29 08:00:00', 'ChuaChay', 55, 54, 4),


(57, 'LT_DT054_20260629', '2026-06-29 12:00:00', 'DaHoanThanh', 56, 55, 6),


(58, 'LT_DT055_20260630', '2026-06-30 08:00:00', 'ChuaChay', 57, 56, 8),


(59, 'LT_DT056_20260630', '2026-06-30 12:00:00', 'ChuaChay', 58, 57, 10),


(60, 'LT_DT057_20260701', '2026-07-01 08:00:00', 'BiHuy', 59, 58, 2),


(61, 'LT_DT058_20260701', '2026-07-01 12:00:00', 'ChuaChay', 60, 59, 4),


(62, 'LT_DT059_20260702', '2026-07-02 08:00:00', 'ChuaChay', 61, 60, 6),


(63, 'LT_SE1_20260702', '2026-07-02 12:00:00', 'DaHoanThanh', 1, 1, 8),


(64, 'LT_DT000_20260703', '2026-07-03 08:00:00', 'ChuaChay', 2, 1, 10),


(65, 'LT_DT001_20260703', '2026-07-03 12:00:00', 'DangChay', 3, 2, 2),


(66, 'LT_DT002_20260704', '2026-07-04 08:00:00', 'DaHoanThanh', 4, 3, 4),


(67, 'LT_DT003_20260704', '2026-07-04 12:00:00', 'ChuaChay', 5, 4, 6),


(68, 'LT_DT004_20260705', '2026-07-05 08:00:00', 'ChuaChay', 6, 5, 8),


(69, 'LT_DT005_20260705', '2026-07-05 12:00:00', 'DaHoanThanh', 7, 6, 10),


(70, 'LT_DT006_20260706', '2026-07-06 08:00:00', 'DangChay', 8, 7, 2),


(71, 'LT_DT007_20260706', '2026-07-06 12:00:00', 'ChuaChay', 9, 8, 4),


(72, 'LT_DT008_20260707', '2026-07-07 08:00:00', 'ChuaChay', 10, 9, 6),


(73, 'LT_DT009_20260707', '2026-07-07 12:00:00', 'DaHoanThanh', 11, 10, 8),


(74, 'LT_DT010_20260708', '2026-07-08 08:00:00', 'ChuaChay', 12, 11, 10),


(75, 'LT_DT011_20260708', '2026-07-08 12:00:00', 'DangChay', 13, 12, 2),


(76, 'LT_DT012_20260709', '2026-07-09 08:00:00', 'ChuaChay', 14, 13, 4),


(77, 'LT_DT013_20260709', '2026-07-09 12:00:00', 'ChuaChay', 15, 14, 6),


(78, 'LT_DT014_20260710', '2026-07-10 08:00:00', 'DaHoanThanh', 16, 15, 8),


(79, 'LT_DT015_20260710', '2026-07-10 12:00:00', 'ChuaChay', 17, 16, 10),


(80, 'LT_DT016_20260711', '2026-07-11 08:00:00', 'DangChay', 18, 17, 2),


(81, 'LT_DT017_20260711', '2026-07-11 12:00:00', 'ChuaChay', 19, 18, 4),


(82, 'LT_DT018_20260712', '2026-07-12 08:00:00', 'ChuaChay', 20, 19, 6),


(83, 'LT_DT019_20260712', '2026-07-12 12:00:00', 'DaHoanThanh', 21, 20, 8),


(84, 'LT_DT020_20260713', '2026-07-13 08:00:00', 'ChuaChay', 22, 21, 10),


(85, 'LT_DT021_20260713', '2026-07-13 12:00:00', 'DangChay', 23, 22, 2),


(86, 'LT_DT022_20260714', '2026-07-14 08:00:00', 'ChuaChay', 24, 23, 4),


(87, 'LT_DT023_20260714', '2026-07-14 12:00:00', 'DaHoanThanh', 25, 24, 6),


(88, 'LT_DT024_20260715', '2026-07-15 08:00:00', 'ChuaChay', 26, 25, 8),


(89, 'LT_DT025_20260715', '2026-07-15 12:00:00', 'ChuaChay', 27, 26, 10),


(90, 'LT_DT026_20260716', '2026-07-16 08:00:00', 'BiHuy', 28, 27, 2),


(91, 'LT_DT027_20260716', '2026-07-16 12:00:00', 'ChuaChay', 29, 28, 4),


(92, 'LT_DT028_20260717', '2026-07-17 08:00:00', 'ChuaChay', 30, 29, 6),


(93, 'LT_DT029_20260717', '2026-07-17 12:00:00', 'DaHoanThanh', 31, 30, 8),


(94, 'LT_DT030_20260718', '2026-07-18 08:00:00', 'ChuaChay', 32, 31, 10),


(95, 'LT_DT031_20260718', '2026-07-18 12:00:00', 'DangChay', 33, 32, 2),


(96, 'LT_DT032_20260719', '2026-07-19 08:00:00', 'DaHoanThanh', 34, 33, 4),


(97, 'LT_DT033_20260719', '2026-07-19 12:00:00', 'ChuaChay', 35, 34, 6),


(98, 'LT_DT034_20260720', '2026-07-20 08:00:00', 'ChuaChay', 36, 35, 8),


(99, 'LT_DT035_20260720', '2026-07-20 12:00:00', 'DaHoanThanh', 37, 36, 10),


(100, 'LT_DT036_20260721', '2026-07-21 08:00:00', 'DangChay', 38, 37, 2),


(101, 'LT_DT037_20260721', '2026-07-21 12:00:00', 'ChuaChay', 39, 38, 4);


-- Them ChinhSachGia
INSERT INTO ChinhSachGia (id, maChinhSach, tyLeKhuyenMai, moTa) VALUES
(1, 'CSG_001', 0.0, 'Chinh sach gia mac dinh');

-- Them Hoa don 
INSERT INTO HoaDon (id, maHoaDon, loaiHoaDon, ngayGioLap, phuongThucThanhToan, tongTien, trangThai, nhanVienId, khachHangId) VALUES
(3, 'HD_0003', 'MuaVe', '2026-06-01 09:14:00', 'ChuyenKhoan', 250000, 'DaThanhToan', 13, 570),
(4, 'HD_0004', 'MuaVe', '2026-06-01 12:27:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 14, 479),
(5, 'HD_0005', 'MuaVe', '2026-06-01 15:38:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 15, 547),
(6, 'HD_0006', 'MuaVe', '2026-06-01 18:28:00', 'ChuyenKhoan', 250000, 'DaHuy', 16, 511),
(7, 'HD_0007', 'MuaVe', '2026-06-02 09:17:00', 'ChuyenKhoan', 450000, 'DaHuy', 17, 503),
(8, 'HD_0008', 'MuaVe', '2026-06-02 12:06:00', 'ChuyenKhoan', 650000, 'DaThanhToan', 18, 584),
(9, 'HD_0009', 'MuaVe', '2026-06-02 15:46:00', 'ChuyenKhoan', 150000, 'DaHuy', 19, 544),
(10, 'HD_0010', 'MuaVe', '2026-06-02 18:18:00', 'TienMat', 900000, 'DaThanhToan', 20, 555),
(11, 'HD_0011', 'MuaVe', '2026-06-03 09:04:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 21, 560),
(12, 'HD_0012', 'MuaVe', '2026-06-03 12:55:00', 'TienMat', 250000, 'DaThanhToan', 22, 524),
(13, 'HD_0013', 'MuaVe', '2026-06-03 15:23:00', 'TienMat', 250000, 'DaHuy', 23, 502),
(14, 'HD_0014', 'MuaVe', '2026-06-03 18:40:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 24, 544),
(15, 'HD_0015', 'MuaVe', '2026-06-04 09:17:00', 'ChuyenKhoan', 650000, 'DaHuy', 25, 564),
(16, 'HD_0016', 'MuaVe', '2026-06-04 12:14:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 26, 579),
(17, 'HD_0017', 'MuaVe', '2026-06-04 15:13:00', 'TienMat', 150000, 'DaThanhToan', 27, 588),
(18, 'HD_0018', 'MuaVe', '2026-06-04 18:56:00', 'ChuyenKhoan', 650000, 'DaHuy', 11, 534),
(19, 'HD_0019', 'MuaVe', '2026-06-05 09:47:00', 'TienMat', 250000, 'DaThanhToan', 12, 544),
(20, 'HD_0020', 'MuaVe', '2026-06-05 12:25:00', 'ChuyenKhoan', 900000, 'DaHuy', 13, 504),
(21, 'HD_0021', 'MuaVe', '2026-06-05 15:48:00', 'ChuyenKhoan', 150000, 'DaHuy', 14, 586),
(22, 'HD_0022', 'MuaVe', '2026-06-05 18:27:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 15, 484),
(23, 'HD_0023_TRA', 'PhatTraVe', '2026-06-06 09:35:00', 'TienMat', 50000, 'DaHuy', 16, 563),
(24, 'HD_0024', 'MuaVe', '2026-06-06 12:21:00', 'ChuyenKhoan', 1200000, 'DaHuy', 17, 513),
(25, 'HD_0025_TRA', 'PhatTraVe', '2026-06-06 15:46:00', 'ChuyenKhoan', 30000, 'DaHuy', 18, 540),
(26, 'HD_0026', 'MuaVe', '2026-06-06 18:19:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 19, 540),
(27, 'HD_0027', 'MuaVe', '2026-06-07 09:34:00', 'ChuyenKhoan', 250000, 'DaHuy', 20, 593),
(28, 'HD_0028', 'MuaVe', '2026-06-07 12:01:00', 'ChuyenKhoan', 650000, 'DaHuy', 21, 594),
(29, 'HD_0029', 'MuaVe', '2026-06-07 15:15:00', 'TienMat', 150000, 'DaThanhToan', 22, 486),
(30, 'HD_0030', 'MuaVe', '2026-06-07 18:48:00', 'ChuyenKhoan', 150000, 'DaHuy', 23, 574),
(31, 'HD_0031', 'MuaVe', '2026-06-08 09:10:00', 'ChuyenKhoan', 900000, 'DaHuy', 24, 543),
(32, 'HD_0032_TRA', 'PhatTraVe', '2026-06-08 12:25:00', 'ChuyenKhoan', 50000, 'DaThanhToan', 25, 559),
(33, 'HD_0033', 'MuaVe', '2026-06-08 15:15:00', 'TienMat', 150000, 'DaHuy', 26, 484),
(34, 'HD_0034', 'MuaVe', '2026-06-08 18:14:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 27, 485),
(35, 'HD_0035', 'MuaVe', '2026-06-09 09:55:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 11, 485),
(36, 'HD_0036', 'MuaVe', '2026-06-09 12:34:00', 'TienMat', 250000, 'DaHuy', 12, 568),
(37, 'HD_0037_TRA', 'PhatTraVe', '2026-06-09 15:12:00', 'ChuyenKhoan', 60000, 'DaHuy', 13, 488),
(38, 'HD_0038_TRA', 'PhatTraVe', '2026-06-09 18:29:00', 'TienMat', 60000, 'DaHuy', 14, 562),
(39, 'HD_0039', 'MuaVe', '2026-06-10 09:21:00', 'ChuyenKhoan', 1200000, 'DaHuy', 15, 507),
(40, 'HD_0040', 'MuaVe', '2026-06-10 12:27:00', 'ChuyenKhoan', 250000, 'DaHuy', 16, 511),
(41, 'HD_0041_TRA', 'PhatTraVe', '2026-06-10 15:51:00', 'ChuyenKhoan', 60000, 'DaThanhToan', 17, 488),
(42, 'HD_0042', 'MuaVe', '2026-06-10 18:59:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 18, 497),
(43, 'HD_0043_TRA', 'PhatTraVe', '2026-06-11 09:55:00', 'TienMat', 45000, 'DaHuy', 19, 591),
(44, 'HD_0044', 'MuaVe', '2026-06-11 12:24:00', 'ChuyenKhoan', 150000, 'DaHuy', 20, 594),
(45, 'HD_0045_TRA', 'PhatTraVe', '2026-06-11 15:09:00', 'TienMat', 60000, 'DaHuy', 21, 513),
(46, 'HD_0046', 'MuaVe', '2026-06-11 18:20:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 22, 482),
(47, 'HD_0047_TRA', 'PhatTraVe', '2026-06-12 09:32:00', 'ChuyenKhoan', 30000, 'DaThanhToan', 23, 584),
(48, 'HD_0048', 'MuaVe', '2026-06-12 12:55:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 24, 527),
(49, 'HD_0049', 'MuaVe', '2026-06-12 15:38:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 25, 555),
(50, 'HD_0050', 'MuaVe', '2026-06-12 18:13:00', 'TienMat', 450000, 'DaThanhToan', 26, 567),
(51, 'HD_0051', 'MuaVe', '2026-06-13 09:08:00', 'ChuyenKhoan', 650000, 'DaThanhToan', 27, 558),
(52, 'HD_0052', 'MuaVe', '2026-06-13 12:00:00', 'TienMat', 150000, 'DaThanhToan', 11, 555),
(53, 'HD_0053', 'MuaVe', '2026-06-13 15:16:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 12, 595),
(54, 'HD_0054', 'MuaVe', '2026-06-13 18:18:00', 'ChuyenKhoan', 450000, 'DaThanhToan', 13, 532),
(55, 'HD_0055', 'MuaVe', '2026-06-14 09:52:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 14, 514),
(56, 'HD_0056', 'MuaVe', '2026-06-14 12:56:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 15, 571),
(57, 'HD_0057', 'MuaVe', '2026-06-14 15:13:00', 'TienMat', 900000, 'DaThanhToan', 16, 502),
(58, 'HD_0058', 'MuaVe', '2026-06-14 18:57:00', 'ChuyenKhoan', 450000, 'DaHuy', 17, 487),
(59, 'HD_0059_TRA', 'PhatTraVe', '2026-06-15 09:21:00', 'TienMat', 30000, 'DaThanhToan', 18, 557),
(60, 'HD_0060', 'MuaVe', '2026-06-15 12:45:00', 'ChuyenKhoan', 900000, 'DaHuy', 19, 547),
(61, 'HD_0061', 'MuaVe', '2026-06-15 15:57:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 20, 545),
(62, 'HD_0062', 'MuaVe', '2026-06-15 18:08:00', 'TienMat', 650000, 'DaThanhToan', 21, 515),
(63, 'HD_0063', 'MuaVe', '2026-06-16 09:43:00', 'ChuyenKhoan', 250000, 'DaThanhToan', 22, 561),
(64, 'HD_0064', 'MuaVe', '2026-06-16 12:47:00', 'TienMat', 900000, 'DaHuy', 23, 594),
(65, 'HD_0065', 'MuaVe', '2026-06-16 15:01:00', 'ChuyenKhoan', 650000, 'DaThanhToan', 24, 570),
(66, 'HD_0066', 'MuaVe', '2026-06-16 18:10:00', 'TienMat', 450000, 'DaThanhToan', 25, 489),
(67, 'HD_0067_TRA', 'PhatTraVe', '2026-06-17 09:12:00', 'ChuyenKhoan', 45000, 'DaHuy', 26, 520),
(68, 'HD_0068', 'MuaVe', '2026-06-17 12:42:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 27, 527),
(69, 'HD_0069', 'MuaVe', '2026-06-17 15:22:00', 'TienMat', 450000, 'DaThanhToan', 11, 541),
(70, 'HD_0070_TRA', 'PhatTraVe', '2026-06-17 18:07:00', 'ChuyenKhoan', 30000, 'DaThanhToan', 12, 498),
(71, 'HD_0071', 'MuaVe', '2026-06-18 09:27:00', 'ChuyenKhoan', 900000, 'DaThanhToan', 13, 569),
(72, 'HD_0072', 'MuaVe', '2026-06-18 12:57:00', 'TienMat', 650000, 'DaThanhToan', 14, 500),
(73, 'HD_0073', 'MuaVe', '2026-06-18 15:33:00', 'ChuyenKhoan', 150000, 'DaHuy', 15, 563),
(74, 'HD_0074', 'MuaVe', '2026-06-18 18:42:00', 'TienMat', 150000, 'DaHuy', 16, 555),
(75, 'HD_0075', 'MuaVe', '2026-06-19 09:57:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 17, 540),
(76, 'HD_0076', 'MuaVe', '2026-06-19 12:25:00', 'ChuyenKhoan', 450000, 'DaHuy', 18, 513),
(77, 'HD_0077', 'MuaVe', '2026-06-19 15:24:00', 'ChuyenKhoan', 1200000, 'DaHuy', 19, 571),
(78, 'HD_0078', 'MuaVe', '2026-06-19 18:35:00', 'ChuyenKhoan', 650000, 'DaThanhToan', 20, 514),
(79, 'HD_0079', 'MuaVe', '2026-06-20 09:38:00', 'ChuyenKhoan', 900000, 'DaHuy', 21, 517),
(80, 'HD_0080_TRA', 'PhatTraVe', '2026-06-20 12:32:00', 'TienMat', 45000, 'DaHuy', 22, 577),
(81, 'HD_0081', 'MuaVe', '2026-06-20 15:32:00', 'ChuyenKhoan', 450000, 'DaThanhToan', 23, 557),
(82, 'HD_0082', 'MuaVe', '2026-06-20 18:19:00', 'ChuyenKhoan', 1200000, 'DaThanhToan', 24, 579),
(83, 'HD_0083', 'MuaVe', '2026-06-21 09:15:00', 'ChuyenKhoan', 150000, 'DaThanhToan', 25, 554),
(84, 'HD_0084', 'MuaVe', '2026-06-21 12:36:00', 'TienMat', 1200000, 'DaHuy', 26, 567),
(85, 'HD_0085_TRA', 'PhatTraVe', '2026-06-21 15:09:00', 'TienMat', 45000, 'DaHuy', 27, 564),
(86, 'HD_0086', 'MuaVe', '2026-06-21 18:11:00', 'ChuyenKhoan', 250000, 'DaHuy', 11, 542),
(87, 'HD_0087_TRA', 'PhatTraVe', '2026-06-22 09:29:00', 'ChuyenKhoan', 30000, 'DaThanhToan', 12, 578),
(88, 'HD_0088_TRA', 'PhatTraVe', '2026-06-22 12:39:00', 'ChuyenKhoan', 60000, 'DaThanhToan', 13, 530),
(89, 'HD_0089_TRA', 'PhatTraVe', '2026-06-22 15:16:00', 'ChuyenKhoan', 60000, 'DaHuy', 14, 583),
(90, 'HD_0090', 'MuaVe', '2026-06-22 18:15:00', 'ChuyenKhoan', 1200000, 'DaHuy', 15, 532),
(91, 'HD_0091', 'MuaVe', '2026-06-23 09:17:00', 'ChuyenKhoan', 250000, 'DaThanhToan', 16, 516),
(92, 'HD_0092', 'MuaVe', '2026-06-23 12:24:00', 'ChuyenKhoan', 250000, 'DaThanhToan', 17, 495),
(93, 'HD_0093', 'MuaVe', '2026-06-23 15:21:00', 'ChuyenKhoan', 650000, 'DaHuy', 18, 535),
(94, 'HD_0094_TRA', 'PhatTraVe', '2026-06-23 18:24:00', 'ChuyenKhoan', 60000, 'DaThanhToan', 19, 565),
(95, 'HD_0095', 'MuaVe', '2026-06-24 09:00:00', 'ChuyenKhoan', 650000, 'DaHuy', 20, 514),
(96, 'HD_0096_TRA', 'PhatTraVe', '2026-06-24 12:14:00', 'TienMat', 60000, 'DaThanhToan', 21, 531),
(97, 'HD_0097_TRA', 'PhatTraVe', '2026-06-24 15:42:00', 'ChuyenKhoan', 50000, 'DaHuy', 22, 578),
(98, 'HD_0098_TRA', 'PhatTraVe', '2026-06-24 18:58:00', 'ChuyenKhoan', 60000, 'DaThanhToan', 23, 555),
(99, 'HD_0099', 'MuaVe', '2026-06-25 09:41:00', 'TienMat', 150000, 'DaThanhToan', 24, 493),
(100, 'HD_0100_TRA', 'PhatTraVe', '2026-06-25 12:24:00', 'ChuyenKhoan', 50000, 'DaThanhToan', 25, 503),
(101, 'HD_0101_TRA', 'PhatTraVe', '2026-06-25 15:17:00', 'TienMat', 60000, 'DaThanhToan', 26, 508),
(102, 'HD_0102', 'MuaVe', '2026-06-25 18:34:00', 'TienMat', 1200000, 'DaThanhToan', 27, 520);

-- Thêm vé tàu 
INSERT INTO VeTau (id, maVe, loaiDoiTuong, giaVe, trangThai, thoiDiemBanVe, lichTrinhId, nhanVienId, khachHangId, gheNgoiId, chinhSachGiaId, hoaDonId) VALUES
(3, 'VE_0003', 'NguoiLon', 350000, 'DaBan', '2026-06-01 09:14:00', 16, 14, 570, 29, 1, 3),
(4, 'VE_0004', 'NguoiLon', 350000, 'DaDoi', '2026-06-01 12:27:00', 6, 15, 479, 76, 1, 4),
(5, 'VE_0005', 'NguoiGia', 250000, 'DaBan', '2026-06-01 15:38:00', 6, 16, 547, 28, 1, 5),
(6, 'VE_0006', 'NguoiLon', 900000, 'DaTra', '2026-06-01 18:28:00', 2, 17, 511, 72, 1, 6),
(7, 'VE_0007', 'NguoiLon', 900000, 'DaTra', '2026-06-02 09:17:00', 15, 18, 503, 58, 1, 7),
(8, 'VE_0008', 'TreEm', 150000, 'DaBan', '2026-06-02 12:06:00', 45, 19, 584, 55, 1, 8),
(9, 'VE_0009', 'TreEm', 300000, 'DaBan', '2026-06-02 15:46:00', 14, 20, 544, 44, 1, 9),
(10, 'VE_0010', 'NguoiLon', 350000, 'DaTra', '2026-06-02 18:18:00', 7, 21, 555, 46, 1, 10),
(11, 'VE_0011', 'TreEm', 300000, 'DaBan', '2026-06-03 09:04:00', 47, 22, 560, 59, 1, 11),
(12, 'VE_0012', 'NguoiLon', 750000, 'DaBan', '2026-06-03 12:55:00', 36, 23, 524, 38, 1, 12),
(13, 'VE_0013', 'NguoiLon', 750000, 'DaBan', '2026-06-03 15:23:00', 1, 24, 502, 60, 1, 13),
(14, 'VE_0014', 'NguoiGia', 350000, 'DaBan', '2026-06-03 18:40:00', 34, 25, 544, 25, 1, 14),
(15, 'VE_0015', 'NguoiGia', 350000, 'DaTra', '2026-06-04 09:17:00', 33, 26, 564, 41, 1, 15),
(16, 'VE_0016', 'NguoiLon', 600000, 'DaBan', '2026-06-04 12:14:00', 21, 27, 579, 68, 1, 16),
(17, 'VE_0017', 'TreEm', 150000, 'DaBan', '2026-06-04 15:13:00', 31, 11, 588, 30, 1, 17),
(18, 'VE_0018', 'NguoiGia', 250000, 'DaTra', '2026-06-04 18:56:00', 10, 12, 534, 5, 1, 18),
(19, 'VE_0019', 'NguoiLon', 350000, 'DaBan', '2026-06-05 09:47:00', 44, 13, 544, 35, 1, 19),
(20, 'VE_0020', 'TreEm', 200000, 'DaTra', '2026-06-05 12:25:00', 30, 14, 504, 61, 1, 20),
(21, 'VE_0021', 'NguoiLon', 350000, 'DaTra', '2026-06-05 15:48:00', 11, 15, 586, 75, 1, 21),
(22, 'VE_0022', 'TreEm', 300000, 'DaBan', '2026-06-05 18:27:00', 39, 16, 484, 21, 1, 22),
(23, 'VE_0023', 'NguoiLon', 600000, 'DaTra', '2026-06-06 09:35:00', 37, 17, 563, 62, 1, 23),
(24, 'VE_0024', 'TreEm', 150000, 'DaBan', '2026-06-06 12:21:00', 48, 18, 513, 11, 1, 24),
(25, 'VE_0025', 'NguoiLon', 600000, 'DaTra', '2026-06-06 15:46:00', 1, 19, 540, 67, 1, 25),
(26, 'VE_0026', 'NguoiGia', 350000, 'DaBan', '2026-06-06 18:19:00', 1, 20, 540, 23, 1, 26),
(27, 'VE_0027', 'TreEm', 200000, 'DaBan', '2026-06-07 09:34:00', 44, 21, 593, 19, 1, 27),
(28, 'VE_0028', 'NguoiGia', 350000, 'DaBan', '2026-06-07 12:01:00', 50, 22, 594, 25, 1, 28),
(29, 'VE_0029', 'TreEm', 200000, 'DaBan', '2026-06-07 15:15:00', 48, 23, 486, 73, 1, 29),
(30, 'VE_0030', 'NguoiLon', 600000, 'DaTra', '2026-06-07 18:48:00', 46, 24, 574, 56, 1, 30),
(31, 'VE_0031', 'NguoiLon', 350000, 'DaTra', '2026-06-08 09:10:00', 40, 25, 543, 62, 1, 31),
(32, 'VE_0032', 'NguoiGia', 500000, 'DaBan', '2026-06-08 12:25:00', 51, 26, 559, 13, 1, 32),
(33, 'VE_0033', 'TreEm', 450000, 'DaTra', '2026-06-08 15:15:00', 19, 27, 484, 56, 1, 33),
(34, 'VE_0034', 'NguoiLon', 900000, 'DaBan', '2026-06-08 18:14:00', 36, 11, 485, 41, 1, 34),
(35, 'VE_0035', 'NguoiLon', 750000, 'DaDoi', '2026-06-09 09:55:00', 33, 12, 485, 34, 1, 35),
(36, 'VE_0036', 'NguoiGia', 500000, 'DaTra', '2026-06-09 12:34:00', 38, 13, 568, 48, 1, 36),
(37, 'VE_0037', 'NguoiGia', 350000, 'DaTra', '2026-06-09 15:12:00', 2, 14, 488, 30, 1, 37),
(38, 'VE_0038', 'NguoiLon', 600000, 'DaTra', '2026-06-09 18:29:00', 47, 15, 562, 47, 1, 38),
(39, 'VE_0039', 'TreEm', 150000, 'DaTra', '2026-06-10 09:21:00', 30, 16, 507, 23, 1, 39),
(40, 'VE_0040', 'TreEm', 300000, 'DaTra', '2026-06-10 12:27:00', 10, 17, 511, 48, 1, 40),
(41, 'VE_0041', 'TreEm', 300000, 'DaBan', '2026-06-10 15:51:00', 11, 18, 488, 71, 1, 41),
(42, 'VE_0042', 'NguoiLon', 450000, 'DaBan', '2026-06-10 18:59:00', 44, 19, 497, 53, 1, 42),
(43, 'VE_0043', 'NguoiGia', 250000, 'DaTra', '2026-06-11 09:55:00', 32, 20, 591, 52, 1, 43),
(44, 'VE_0044', 'NguoiLon', 750000, 'DaTra', '2026-06-11 12:24:00', 10, 21, 594, 62, 1, 44),
(45, 'VE_0045', 'NguoiLon', 600000, 'DaTra', '2026-06-11 15:09:00', 36, 22, 513, 24, 1, 45),
(46, 'VE_0046', 'TreEm', 200000, 'DaBan', '2026-06-11 18:20:00', 41, 23, 482, 15, 1, 46),
(47, 'VE_0047', 'NguoiGia', 500000, 'DaBan', '2026-06-12 09:32:00', 39, 24, 584, 57, 1, 47),
(48, 'VE_0048', 'NguoiLon', 750000, 'DaBan', '2026-06-12 12:55:00', 50, 25, 527, 43, 1, 48),
(49, 'VE_0049', 'TreEm', 150000, 'DaBan', '2026-06-12 15:38:00', 19, 26, 555, 34, 1, 49),
(50, 'VE_0050', 'NguoiLon', 900000, 'DaBan', '2026-06-12 18:13:00', 2, 27, 567, 36, 1, 50),
(51, 'VE_0051', 'NguoiLon', 600000, 'DaBan', '2026-06-13 09:08:00', 33, 11, 558, 20, 1, 51),
(52, 'VE_0052', 'NguoiLon', 750000, 'DaBan', '2026-06-13 12:00:00', 26, 12, 555, 50, 1, 52),
(53, 'VE_0053', 'NguoiLon', 750000, 'DaBan', '2026-06-13 15:16:00', 39, 13, 595, 35, 1, 53),
(54, 'VE_0054', 'TreEm', 450000, 'DaBan', '2026-06-13 18:18:00', 5, 14, 532, 70, 1, 54),
(55, 'VE_0055', 'NguoiLon', 900000, 'DaBan', '2026-06-14 09:52:00', 17, 15, 514, 56, 1, 55),
(56, 'VE_0056', 'TreEm', 450000, 'DaBan', '2026-06-14 12:56:00', 17, 16, 571, 74, 1, 56),
(57, 'VE_0057', 'NguoiLon', 350000, 'DaBan', '2026-06-14 15:13:00', 44, 17, 502, 10, 1, 57),
(58, 'VE_0058', 'TreEm', 450000, 'DaTra', '2026-06-14 18:57:00', 24, 18, 487, 10, 1, 58),
(59, 'VE_0059', 'TreEm', 450000, 'DaBan', '2026-06-15 09:21:00', 33, 19, 557, 56, 1, 59),
(60, 'VE_0060', 'TreEm', 450000, 'DaTra', '2026-06-15 12:45:00', 5, 20, 547, 50, 1, 60),
(61, 'VE_0061', 'NguoiLon', 600000, 'DaBan', '2026-06-15 15:57:00', 36, 21, 545, 14, 1, 61),
(62, 'VE_0062', 'NguoiLon', 350000, 'DaBan', '2026-06-15 18:08:00', 17, 22, 515, 34, 1, 62),
(63, 'VE_0063', 'NguoiGia', 650000, 'DaBan', '2026-06-16 09:43:00', 46, 23, 561, 72, 1, 63),
(64, 'VE_0064', 'TreEm', 200000, 'DaTra', '2026-06-16 12:47:00', 17, 24, 594, 21, 1, 64),
(65, 'VE_0065', 'NguoiLon', 450000, 'DaBan', '2026-06-16 15:01:00', 29, 25, 570, 20, 1, 65),
(66, 'VE_0066', 'TreEm', 150000, 'DaBan', '2026-06-16 18:10:00', 15, 26, 489, 74, 1, 66),
(67, 'VE_0067', 'NguoiGia', 350000, 'DaTra', '2026-06-17 09:12:00', 38, 27, 520, 6, 1, 67),
(68, 'VE_0068', 'NguoiGia', 350000, 'DaBan', '2026-06-17 12:42:00', 45, 11, 527, 43, 1, 68),
(69, 'VE_0069', 'NguoiLon', 750000, 'DaBan', '2026-06-17 15:22:00', 50, 12, 541, 77, 1, 69),
(70, 'VE_0070', 'NguoiLon', 350000, 'DaBan', '2026-06-17 18:07:00', 4, 13, 498, 29, 1, 70),
(71, 'VE_0071', 'NguoiLon', 750000, 'DaBan', '2026-06-18 09:27:00', 19, 14, 569, 75, 1, 71),
(72, 'VE_0072', 'TreEm', 450000, 'DaBan', '2026-06-18 12:57:00', 20, 15, 500, 39, 1, 72),
(73, 'VE_0073', 'TreEm', 300000, 'DaTra', '2026-06-18 15:33:00', 46, 16, 563, 62, 1, 73),
(74, 'VE_0074', 'TreEm', 300000, 'DaTra', '2026-06-18 18:42:00', 15, 17, 555, 3, 1, 74),
(75, 'VE_0075', 'TreEm', 450000, 'DaBan', '2026-06-19 09:57:00', 21, 18, 540, 14, 1, 75),
(76, 'VE_0076', 'NguoiLon', 350000, 'DaTra', '2026-06-19 12:25:00', 15, 19, 513, 27, 1, 76),
(77, 'VE_0077', 'TreEm', 450000, 'DaTra', '2026-06-19 15:24:00', 46, 20, 571, 71, 1, 77),
(78, 'VE_0078', 'TreEm', 450000, 'DaBan', '2026-06-19 18:35:00', 26, 21, 514, 69, 1, 78),
(79, 'VE_0079', 'NguoiLon', 450000, 'DaTra', '2026-06-20 09:38:00', 37, 22, 517, 3, 1, 79),
(80, 'VE_0080', 'NguoiGia', 500000, 'DaTra', '2026-06-20 12:32:00', 12, 23, 577, 49, 1, 80),
(81, 'VE_0081', 'TreEm', 200000, 'DaBan', '2026-06-20 15:32:00', 38, 24, 557, 41, 1, 81),
(82, 'VE_0082', 'NguoiLon', 750000, 'DaBan', '2026-06-20 18:19:00', 33, 25, 579, 64, 1, 82),
(83, 'VE_0083', 'NguoiLon', 600000, 'DaBan', '2026-06-21 09:15:00', 36, 26, 554, 76, 1, 83),
(84, 'VE_0084', 'TreEm', 150000, 'DaTra', '2026-06-21 12:36:00', 10, 27, 567, 3, 1, 84),
(85, 'VE_0085', 'TreEm', 450000, 'DaTra', '2026-06-21 15:09:00', 13, 11, 564, 49, 1, 85),
(86, 'VE_0086', 'NguoiLon', 350000, 'DaTra', '2026-06-21 18:11:00', 43, 12, 542, 60, 1, 86),
(87, 'VE_0087', 'NguoiGia', 350000, 'DaBan', '2026-06-22 09:29:00', 33, 13, 578, 62, 1, 87),
(88, 'VE_0088', 'NguoiLon', 450000, 'DaBan', '2026-06-22 12:39:00', 2, 14, 530, 20, 1, 88),
(89, 'VE_0089', 'NguoiLon', 600000, 'DaTra', '2026-06-22 15:16:00', 23, 15, 583, 6, 1, 89),
(90, 'VE_0090', 'NguoiLon', 900000, 'DaTra', '2026-06-22 18:15:00', 20, 16, 532, 19, 1, 90),
(91, 'VE_0091', 'NguoiLon', 450000, 'DaBan', '2026-06-23 09:17:00', 43, 17, 516, 10, 1, 91),
(92, 'VE_0092', 'TreEm', 450000, 'DaBan', '2026-06-23 12:24:00', 40, 18, 495, 78, 1, 92),
(93, 'VE_0093', 'NguoiGia', 350000, 'DaTra', '2026-06-23 15:21:00', 14, 19, 535, 33, 1, 93),
(94, 'VE_0094', 'TreEm', 150000, 'DaBan', '2026-06-23 18:24:00', 47, 20, 565, 41, 1, 94),
(95, 'VE_0095', 'NguoiLon', 350000, 'DaTra', '2026-06-24 09:00:00', 36, 21, 514, 18, 1, 95),
(96, 'VE_0096', 'TreEm', 450000, 'DaBan', '2026-06-24 12:14:00', 19, 22, 531, 33, 1, 96),
(97, 'VE_0097', 'TreEm', 150000, 'DaTra', '2026-06-24 15:42:00', 33, 23, 578, 23, 1, 97),
(98, 'VE_0098', 'NguoiLon', 600000, 'DaBan', '2026-06-24 18:58:00', 2, 24, 555, 11, 1, 98),
(99, 'VE_0099', 'NguoiLon', 450000, 'DaBan', '2026-06-25 09:41:00', 10, 25, 493, 2, 1, 99),
(100, 'VE_0100', 'TreEm', 300000, 'DaBan', '2026-06-25 12:24:00', 43, 26, 503, 33, 1, 100),
(101, 'VE_0101', 'TreEm', 200000, 'DaBan', '2026-06-25 15:17:00', 37, 27, 508, 61, 1, 101),
(102, 'VE_0102', 'NguoiLon', 750000, 'DaBan', '2026-06-25 18:34:00', 50, 11, 520, 19, 1, 102);

-- Them PhieuTraVe (Tham chiếu VeTau, NhanVien, HoaDon)
INSERT INTO PhieuTraVe (id, maPhieuTra, thoiDiemTra, tienPhat, tienHoanLaiKhach, veTauId, nhanVienId, hoaDonId) VALUES
(2, 'PTV_662636', '2026-06-01 10:15:00', 70000, 280000, 3, 15, 3),
(3, 'PTV_229592', '2026-06-01 13:30:00', 105000, 245000, 4, 16, 4),
(4, 'PTV_576877', '2026-06-01 16:45:00', 50000, 200000, 5, 17, 5),
(5, 'PTV_486490', '2026-06-01 19:10:00', 180000, 720000, 6, 18, 6),
(6, 'PTV_805511', '2026-06-02 10:00:00', 270000, 630000, 7, 19, 7),
(7, 'PTV_885395', '2026-06-02 13:12:00', 30000, 120000, 8, 20, 8),
(8, 'PTV_805000', '2026-06-02 16:20:00', 60000, 240000, 9, 21, 9),
(9, 'PTV_374896', '2026-06-02 19:40:00', 70000, 280000, 10, 22, 10),
(10, 'PTV_712955', '2026-06-03 10:05:00', 60000, 240000, 11, 23, 11),
(11, 'PTV_499534', '2026-06-03 13:55:00', 150000, 600000, 12, 24, 12),
(12, 'PTV_962367', '2026-06-03 16:15:00', 150000, 600000, 13, 25, 13),
(13, 'PTV_769567', '2026-06-03 19:20:00', 70000, 280000, 14, 26, 14),
(14, 'PTV_489518', '2026-06-04 10:11:00', 105000, 245000, 15, 27, 15),
(15, 'PTV_213609', '2026-06-04 13:02:00', 120000, 480000, 16, 11, 16),
(16, 'PTV_807617', '2026-06-04 16:34:00', 30000, 120000, 17, 12, 17),
(17, 'PTV_345180', '2026-06-04 19:44:00', 75000, 175000, 18, 13, 18),
(18, 'PTV_594379', '2026-06-05 10:15:00', 70000, 280000, 19, 14, 19),
(19, 'PTV_126240', '2026-06-05 13:14:00', 40000, 160000, 20, 15, 20),
(20, 'PTV_749641', '2026-06-05 16:55:00', 70000, 280000, 21, 16, 21),
(21, 'PTV_688678', '2026-06-05 19:12:00', 60000, 240000, 22, 17, 22),
(22, 'PTV_443939', '2026-06-06 10:32:00', 120000, 480000, 23, 18, 23),
(23, 'PTV_739717', '2026-06-06 13:51:00', 30000, 120000, 24, 19, 24),
(24, 'PTV_332120', '2026-06-06 16:46:00', 120000, 480000, 25, 20, 25),
(25, 'PTV_779112', '2026-06-06 19:29:00', 70000, 280000, 26, 21, 26),
(26, 'PTV_166287', '2026-06-07 10:12:00', 40000, 160000, 27, 22, 27),
(27, 'PTV_766269', '2026-06-07 13:25:00', 70000, 280000, 28, 23, 28),
(28, 'PTV_963874', '2026-06-07 16:33:00', 40000, 160000, 29, 24, 29),
(29, 'PTV_586819', '2026-06-07 19:10:00', 180000, 420000, 30, 25, 30),
(30, 'PTV_835132', '2026-06-08 10:12:00', 105000, 245000, 31, 26, 31),
(31, 'PTV_416869', '2026-06-08 13:17:00', 100000, 400000, 32, 27, 32),
(32, 'PTV_780776', '2026-06-08 16:15:00', 90000, 360000, 33, 11, 33),
(33, 'PTV_528129', '2026-06-08 19:26:00', 180000, 720000, 34, 12, 34),
(34, 'PTV_222343', '2026-06-09 10:14:00', 225000, 525000, 35, 13, 35),
(35, 'PTV_246566', '2026-06-09 13:34:00', 100000, 400000, 36, 14, 36),
(36, 'PTV_147549', '2026-06-09 16:11:00', 105000, 245000, 37, 15, 37),
(37, 'PTV_139019', '2026-06-09 19:08:00', 180000, 420000, 38, 16, 38),
(38, 'PTV_419149', '2026-06-10 10:12:00', 45000, 105000, 39, 17, 39),
(39, 'PTV_616554', '2026-06-10 13:21:00', 90000, 210000, 40, 18, 40),
(40, 'PTV_221761', '2026-06-10 16:51:00', 60000, 240000, 41, 19, 41),
(41, 'PTV_201913', '2026-06-10 19:42:00', 90000, 360000, 42, 20, 42),
(42, 'PTV_346205', '2026-06-11 10:55:00', 50000, 200000, 43, 21, 43),
(43, 'PTV_663658', '2026-06-11 13:16:00', 225000, 525000, 44, 22, 44),
(44, 'PTV_242254', '2026-06-11 16:24:00', 180000, 420000, 45, 23, 45),
(45, 'PTV_507524', '2026-06-11 19:20:00', 60000, 140000, 46, 24, 46),
(46, 'PTV_575679', '2026-06-12 10:07:00', 100000, 400000, 47, 25, 47),
(47, 'PTV_489033', '2026-06-12 13:28:00', 225000, 525000, 48, 26, 48),
(48, 'PTV_803055', '2026-06-12 16:32:00', 45000, 105000, 49, 27, 49),
(49, 'PTV_879238', '2026-06-12 19:13:00', 180000, 720000, 50, 11, 50),
(50, 'PTV_830316', '2026-06-13 10:08:00', 120000, 480000, 51, 12, 51),
(51, 'PTV_666432', '2026-06-13 13:00:00', 225000, 525000, 52, 13, 52);
INSERT INTO PhieuTraVe (id, maPhieuTra, thoiDiemTra, tienPhat, tienHoanLaiKhach, veTauId, nhanVienId, hoaDonId) VALUES
(52, 'PTV_539486', '2026-06-13 16:00:00', 225000, 525000, 53, 15, 53),
(53, 'PTV_715870', '2026-06-13 19:18:00', 90000, 360000, 54, 16, 54),
(54, 'PTV_878325', '2026-06-14 10:31:00', 180000, 720000, 55, 17, 55),
(55, 'PTV_861894', '2026-06-14 13:56:00', 90000, 360000, 56, 18, 56),
(56, 'PTV_262029', '2026-06-14 16:13:00', 70000, 280000, 57, 19, 57),
(57, 'PTV_535037', '2026-06-14 19:57:00', 90000, 360000, 58, 20, 58),
(58, 'PTV_786735', '2026-06-15 10:21:00', 90000, 360000, 59, 21, 59),
(59, 'PTV_203831', '2026-06-15 13:45:00', 90000, 360000, 60, 22, 60),
(60, 'PTV_974190', '2026-06-15 16:57:00', 120000, 480000, 61, 23, 61),
(61, 'PTV_613117', '2026-06-15 19:08:00', 70000, 280000, 62, 24, 62),
(62, 'PTV_745558', '2026-06-16 10:43:00', 195000, 455000, 63, 25, 63),
(63, 'PTV_527940', '2026-06-16 13:47:00', 40000, 160000, 64, 26, 64),
(64, 'PTV_393328', '2026-06-16 16:01:00', 90000, 360000, 65, 27, 65),
(65, 'PTV_134305', '2026-06-16 19:10:00', 30000, 120000, 66, 11, 66),
(66, 'PTV_823811', '2026-06-17 10:12:00', 105000, 245000, 67, 12, 67),
(67, 'PTV_488528', '2026-06-17 13:42:00', 35000, 115000, 68, 13, 68),
(68, 'PTV_327823', '2026-06-17 16:22:00', 150000, 600000, 69, 14, 69),
(69, 'PTV_564920', '2026-06-17 19:07:00', 70000, 280000, 70, 15, 70),
(70, 'PTV_566297', '2026-06-18 10:27:00', 180000, 520000, 71, 16, 71),
(71, 'PTV_347582', '2026-06-18 13:57:00', 90000, 360000, 72, 17, 72),
(72, 'PTV_997004', '2026-06-18 16:33:00', 60000, 240000, 73, 18, 73),
(73, 'PTV_480303', '2026-06-18 19:42:00', 30000, 120000, 74, 19, 74),
(74, 'PTV_204180', '2026-06-19 10:57:00', 90000, 360000, 75, 20, 75),
(75, 'PTV_818987', '2026-06-19 13:25:00', 105000, 245000, 76, 21, 76),
(76, 'PTV_485178', '2026-06-19 16:24:00', 135000, 315000, 77, 22, 77),
(77, 'PTV_670913', '2026-06-19 19:35:00', 90000, 360000, 78, 23, 78),
(78, 'PTV_776149', '2026-06-20 10:38:00', 135000, 315000, 79, 24, 79),
(79, 'PTV_476096', '2026-06-20 13:32:00', 150000, 350000, 80, 25, 80),
(80, 'PTV_163491', '2026-06-20 16:32:00', 60000, 140000, 81, 26, 81),
(81, 'PTV_517479', '2026-06-20 19:19:00', 225000, 525000, 82, 27, 82),
(82, 'PTV_389282', '2026-06-21 10:15:00', 120000, 480000, 83, 11, 83),
(83, 'PTV_299028', '2026-06-21 13:36:00', 45000, 105000, 84, 12, 84),
(84, 'PTV_228093', '2026-06-21 16:09:00', 90000, 360000, 85, 13, 85),
(85, 'PTV_989227', '2026-06-21 19:11:00', 105000, 245000, 86, 14, 86),
(86, 'PTV_964226', '2026-06-22 10:29:00', 70000, 280000, 87, 15, 87),
(87, 'PTV_576805', '2026-06-22 13:39:00', 90000, 360000, 88, 16, 88),
(88, 'PTV_196126', '2026-06-22 16:16:00', 120000, 480000, 89, 17, 89),
(89, 'PTV_794963', '2026-06-22 19:15:00', 270000, 630000, 90, 18, 90),
(90, 'PTV_322409', '2026-06-23 10:17:00', 135000, 315000, 91, 19, 91),
(91, 'PTV_772875', '2026-06-23 12:24:00', 90000, 360000, 92, 20, 92),
(92, 'PTV_770556', '2026-06-23 15:21:00', 70000, 280000, 93, 21, 93),
(93, 'PTV_726152', '2026-06-23 18:24:00', 30000, 120000, 94, 22, 94),
(94, 'PTV_122394', '2026-06-24 10:00:00', 105000, 245000, 95, 23, 95),
(95, 'PTV_153035', '2026-06-24 13:14:00', 90000, 360000, 96, 24, 96),
(96, 'PTV_925013', '2026-06-24 16:42:00', 30000, 120000, 97, 25, 97),
(97, 'PTV_449709', '2026-06-24 18:58:00', 180000, 420000, 98, 26, 98),
(98, 'PTV_355400', '2026-06-25 10:41:00', 135000, 315000, 99, 27, 99),
(99, 'PTV_232040', '2026-06-25 13:24:00', 60000, 240000, 100, 11, 100),
(100, 'PTV_925206', '2026-06-25 16:17:00', 60000, 140000, 101, 12, 101),
(101, 'PTV_692002', '2026-06-25 19:34:00', 150000, 600000, 102, 13, 102);
-- Them BaoCao (Tham chiếu QuanLy)
INSERT INTO BaoCao (id, maBaoCao, ngayLapBaoCao, tongDoanhThu, ngayBatDau, ngayKetThuc, quanLyId) VALUES
(2, 'BC_DAILY_0002', '2026-06-03 18:00:00', 4500000, '2026-06-02', '2026-06-03', 8),

(3, 'BC_DAILY_0003', '2026-06-04 08:00:00', 12500000, '2026-06-03', '2026-06-04', 9),

(4, 'BC_DAILY_0004', '2026-06-05 17:30:00', 8900000, '2026-06-04', '2026-06-05', 10),

(5, 'BC_DAILY_0005', '2026-06-06 09:15:00', 21400000, '2026-06-05', '2026-06-06', 1),

(6, 'BC_DAILY_0006', '2026-06-07 16:45:00', 17850000, '2026-06-06', '2026-06-07', 2),

(7, 'BC_DAILY_0007', '2026-06-08 08:30:00', 31200000, '2026-06-07', '2026-06-08', 3),

(8, 'BC_DAILY_0008', '2026-06-09 18:00:00', 14600000, '2026-06-08', '2026-06-09', 4),

(9, 'BC_DAILY_0009', '2026-06-10 11:20:00', 9800000, '2026-06-09', '2026-06-10', 5),

(10, 'BC_DAILY_0010', '2026-06-11 15:40:00', 11350000, '2026-06-10', '2026-06-11', 6),

(11, 'BC_DAILY_0011', '2026-06-12 08:15:00', 22400000, '2026-06-11', '2026-06-12', 7),

(12, 'BC_DAILY_0012', '2026-06-13 17:00:00', 16700000, '2026-06-12', '2026-06-13', 8),

(13, 'BC_DAILY_0013', '2026-06-14 09:35:00', 13450000, '2026-06-13', '2026-06-14', 9),

(14, 'BC_DAILY_0014', '2026-06-15 16:50:00', 27800000, '2026-06-14', '2026-06-15', 10),

(15, 'BC_DAILY_0015', '2026-06-16 08:25:00', 35400000, '2026-06-15', '2026-06-16', 1),

(16, 'BC_DAILY_0016', '2026-06-17 18:10:00', 19150000, '2026-06-16', '2026-06-17', 2),

(17, 'BC_DAILY_0017', '2026-06-18 11:05:00', 14200000, '2026-06-17', '2026-06-18', 3),

(18, 'BC_DAILY_0018', '2026-06-19 14:30:00', 25600000, '2026-06-18', '2026-06-19', 4),

(19, 'BC_DAILY_0019', '2026-06-20 08:55:00', 18900000, '2026-06-19', '2026-06-20', 5),

(20, 'BC_DAILY_0020', '2026-06-21 17:40:00', 29450000, '2026-06-20', '2026-06-21', 6),

(21, 'BC_DAILY_0021', '2026-06-22 09:10:00', 11300000, '2026-06-21', '2026-06-22', 7),

(22, 'BC_DAILY_0022', '2026-06-23 16:20:00', 15400000, '2026-06-22', '2026-06-23', 8),

(23, 'BC_DAILY_0023', '2026-06-24 08:45:00', 20150000, '2026-06-23', '2026-06-24', 9),

(24, 'BC_DAILY_0024', '2026-06-25 18:15:00', 33600000, '2026-06-24', '2026-06-25', 10),

(25, 'BC_DAILY_0025', '2026-06-26 10:30:00', 22800000, '2026-06-25', '2026-06-26', 1),

(26, 'BC_DAILY_0026', '2026-06-27 15:50:00', 14500000, '2026-06-26', '2026-06-27', 2),

(27, 'BC_DAILY_0027', '2026-06-28 08:10:00', 19250000, '2026-06-27', '2026-06-28', 3),

(28, 'BC_DAILY_0028', '2026-06-29 17:05:00', 26400000, '2026-06-28', '2026-06-29', 4),

(29, 'BC_DAILY_0029', '2026-06-30 11:40:00', 30100000, '2026-06-29', '2026-06-30', 5),

(30, 'BC_WEEKLY_0001', '2026-06-08 09:00:00', 87600000, '2026-06-01', '2026-06-07', 6),

(31, 'BC_WEEKLY_0002', '2026-06-15 09:00:00', 114300000, '2026-06-08', '2026-06-14', 7),

(32, 'BC_WEEKLY_0003', '2026-06-22 09:00:00', 145200000, '2026-06-15', '2026-06-21', 8),

(33, 'BC_WEEKLY_0004', '2026-06-29 09:00:00', 131900000, '2026-06-22', '2026-06-28', 9),

(34, 'BC_MONTH_062026', '2026-07-01 10:00:00', 520400000, '2026-06-01', '2026-06-30', 10),

(35, 'BC_DAILY_0030', '2026-07-02 08:20:00', 15100000, '2026-07-01', '2026-07-02', 1),

(36, 'BC_DAILY_0031', '2026-07-03 17:45:00', 18350000, '2026-07-02', '2026-07-03', 2),

(37, 'BC_DAILY_0032', '2026-07-04 09:15:00', 22600000, '2026-07-03', '2026-07-04', 3),

(38, 'BC_DAILY_0033', '2026-07-05 16:30:00', 14800000, '2026-07-04', '2026-07-05', 4),

(39, 'BC_DAILY_0034', '2026-07-06 08:40:00', 11900000, '2026-07-05', '2026-07-06', 5),

(40, 'BC_DAILY_0035', '2026-07-07 18:10:00', 26400000, '2026-07-06', '2026-07-07', 6),

(41, 'BC_DAILY_0036', '2026-07-08 11:25:00', 31500000, '2026-07-07', '2026-07-08', 7),

(42, 'BC_DAILY_0037', '2026-07-09 15:15:00', 24800000, '2026-07-08', '2026-07-09', 8),

(43, 'BC_DAILY_0038', '2026-07-10 08:35:00', 17200000, '2026-07-09', '2026-07-10', 9),

(44, 'BC_DAILY_0039', '2026-07-11 17:50:00', 29100000, '2026-07-10', '2026-07-11', 10),

(45, 'BC_DAILY_0040', '2026-07-12 10:05:00', 20450000, '2026-07-11', '2026-07-12', 1),

(46, 'BC_DAILY_0041', '2026-07-13 14:20:00', 13800000, '2026-07-12', '2026-07-13', 2),

(47, 'BC_DAILY_0042', '2026-07-14 08:15:00', 21650000, '2026-07-13', '2026-07-14', 3),

(48, 'BC_DAILY_0043', '2026-07-15 18:00:00', 34100000, '2026-07-14', '2026-07-15', 4),

(49, 'BC_DAILY_0044', '2026-07-16 11:40:00', 16900000, '2026-07-15', '2026-07-16', 5),

(50, 'BC_DAILY_0045', '2026-07-17 15:30:00', 19450000, '2026-07-16', '2026-07-17', 6),

(51, 'BC_DAILY_0046', '2026-07-18 09:05:00', 25200000, '2026-07-17', '2026-07-18', 7);

INSERT INTO BaoCao (id, maBaoCao, ngayLapBaoCao, tongDoanhThu, ngayBatDau, ngayKetThuc, quanLyId) VALUES
(52, 'BC_DAILY_0047', '2026-07-19 16:45:00', 28150000, '2026-07-18', '2026-07-19', 9),

(53, 'BC_DAILY_0048', '2026-07-20 08:10:00', 32400000, '2026-07-19', '2026-07-20', 10),

(54, 'BC_DAILY_0049', '2026-07-21 17:25:00', 15650000, '2026-07-20', '2026-07-21', 1),

(55, 'BC_DAILY_0050', '2026-07-22 10:50:00', 11200000, '2026-07-21', '2026-07-22', 2),

(56, 'BC_WEEKLY_0005', '2026-07-06 09:00:00', 123800000, '2026-06-29', '2026-07-05', 3),

(57, 'BC_WEEKLY_0006', '2026-07-13 09:00:00', 145900000, '2026-07-06', '2026-07-12', 4),

(58, 'BC_WEEKLY_0007', '2026-07-20 09:00:00', 137400000, '2026-07-13', '2026-07-19', 5),

(59, 'BC_WEEKLY_0008', '2026-07-27 09:00:00', 151200000, '2026-07-20', '2026-07-26', 6),

(60, 'BC_MONTH_072026', '2026-08-01 10:00:00', 645800000, '2026-07-01', '2026-07-31', 7),

(61, 'BC_DAILY_0051', '2026-07-23 08:30:00', 19400000, '2026-07-22', '2026-07-23', 8),

(62, 'BC_DAILY_0052', '2026-07-24 16:55:00', 22150000, '2026-07-23', '2026-07-24', 9),

(63, 'BC_DAILY_0053', '2026-07-25 09:10:00', 26400000, '2026-07-24', '2026-07-25', 10),

(64, 'BC_DAILY_0054', '2026-07-26 17:20:00', 31800000, '2026-07-25', '2026-07-26', 1),

(65, 'BC_DAILY_0055', '2026-07-27 08:45:00', 14900000, '2026-07-26', '2026-07-27', 2),

(66, 'BC_DAILY_0056', '2026-07-28 18:15:00', 12350000, '2026-07-27', '2026-07-28', 3),

(67, 'BC_DAILY_0057', '2026-07-29 11:30:00', 20800000, '2026-07-28', '2026-07-29', 4),

(68, 'BC_DAILY_0058', '2026-07-30 15:40:00', 28500000, '2026-07-29', '2026-07-30', 5),

(69, 'BC_DAILY_0059', '2026-07-31 08:20:00', 33100000, '2026-07-30', '2026-07-31', 6),

(70, 'BC_DAILY_0060', '2026-08-02 17:50:00', 16400000, '2026-08-01', '2026-08-02', 7),

(71, 'BC_DAILY_0061', '2026-08-03 09:10:00', 19250000, '2026-08-02', '2026-08-03', 8),

(72, 'BC_DAILY_0062', '2026-08-04 16:30:00', 23400000, '2026-08-03', '2026-08-04', 9),

(73, 'BC_DAILY_0063', '2026-08-05 08:50:00', 27900000, '2026-08-04', '2026-08-05', 10),

(74, 'BC_DAILY_0064', '2026-08-06 18:10:00', 15300000, '2026-08-05', '2026-08-06', 1),

(75, 'BC_DAILY_0065', '2026-08-07 11:15:00', 12800000, '2026-08-06', '2026-08-07', 2),

(76, 'BC_DAILY_0066', '2026-08-08 14:40:00', 21450000, '2026-08-07', '2026-08-08', 3),

(77, 'BC_DAILY_0067', '2026-08-09 08:25:00', 30900000, '2026-08-08', '2026-08-09', 4),

(78, 'BC_DAILY_0068', '2026-08-10 17:35:00', 24600000, '2026-08-09', '2026-08-10', 5),

(79, 'BC_DAILY_0069', '2026-08-11 10:15:00', 18250000, '2026-08-10', '2026-08-11', 6),

(80, 'BC_DAILY_0070', '2026-08-12 15:55:00', 13400000, '2026-08-11', '2026-08-12', 7),

(81, 'BC_DAILY_0071', '2026-08-13 08:40:00', 22100000, '2026-08-12', '2026-08-13', 8),

(82, 'BC_DAILY_0072', '2026-08-14 17:15:00', 29650000, '2026-08-13', '2026-08-14', 9),

(83, 'BC_DAILY_0073', '2026-08-15 09:30:00', 34200000, '2026-08-14', '2026-08-15', 10),

(84, 'BC_DAILY_0074', '2026-08-16 16:45:00', 17800000, '2026-08-14', '2026-08-16', 1),

(85, 'BC_DAILY_0075', '2026-08-17 08:10:00', 14150000, '2026-08-16', '2026-08-17', 2),

(86, 'BC_DAILY_0076', '2026-08-18 18:00:00', 23600000, '2026-08-17', '2026-08-18', 3),

(87, 'BC_DAILY_0077', '2026-08-19 11:25:00', 28450000, '2026-08-18', '2026-08-19', 4),

(88, 'BC_DAILY_0078', '2026-08-20 14:50:00', 19100000, '2026-08-19', '2026-08-20', 5),

(89, 'BC_DAILY_0079', '2026-08-21 09:05:00', 15400000, '2026-08-20', '2026-08-21', 6),

(90, 'BC_DAILY_0080', '2026-08-22 17:25:00', 22850000, '2026-08-21', '2026-08-22', 7),

(91, 'BC_DAILY_0081', '2026-08-23 10:35:00', 31400000, '2026-08-22', '2026-08-23', 8),

(92, 'BC_DAILY_0082', '2026-08-24 15:10:00', 26900000, '2026-08-23', '2026-08-24', 9),

(93, 'BC_DAILY_0083', '2026-08-25 08:15:00', 18350000, '2026-08-24', '2026-08-25', 10),

(94, 'BC_DAILY_0084', '2026-08-26 17:40:00', 13700000, '2026-08-25', '2026-08-26', 1),

(95, 'BC_DAILY_0085', '2026-08-27 09:20:00', 21900000, '2026-08-26', '2026-08-27', 2),

(96, 'BC_DAILY_0086', '2026-08-28 16:55:00', 29400000, '2026-08-27', '2026-08-28', 3),

(97, 'BC_DAILY_0087', '2026-08-29 08:30:00', 35600000, '2026-08-28', '2026-08-29', 4),

(98, 'BC_DAILY_0088', '2026-08-30 18:10:00', 16250000, '2026-08-29', '2026-08-30', 5),

(99, 'BC_DAILY_0089', '2026-08-31 11:45:00', 12100000, '2026-08-30', '2026-08-31', 6),

(100, 'BC_WEEKLY_0009', '2026-08-10 09:00:00', 135400000, '2026-08-03', '2026-08-09', 7),

(101, 'BC_MONTH_082026', '2026-09-01 10:00:00', 684100000, '2026-08-01', '2026-08-31', 8);

-- Them ChiTietBaoCao (Tham chiếu LichTrinh, BaoCao)
INSERT INTO ChiTietBaoCao (id, maCTBC, soVeBan, doanhThuChuyen, tiLeLapDay, lichTrinhId, baoCaoId) VALUES
(2, 'CTBC_LT01_BC002', 24, 3600000, 78.2, 2, 2),

(3, 'CTBC_LT02_BC003', 41, 10250000, 39.07, 3, 3),

(4, 'CTBC_LT03_BC004', 23, 3450000, 68.38, 4, 4),

(5, 'CTBC_LT04_BC005', 14, 2100000, 36.09, 5, 5),

(6, 'CTBC_LT05_BC006', 39, 5850000, 66.48, 6, 6),

(7, 'CTBC_LT06_BC007', 79, 35550000, 44.33, 7, 7),

(8, 'CTBC_LT07_BC008', 45, 6750000, 79.32, 8, 8),

(9, 'CTBC_LT08_BC009', 30, 13500000, 52.12, 9, 9),

(10, 'CTBC_LT09_BC010', 29, 7250000, 92.22, 10, 10),

(11, 'CTBC_LT10_BC011', 53, 7950000, 36.03, 11, 11),

(12, 'CTBC_LT11_BC012', 22, 7700000, 85.09, 12, 12),

(13, 'CTBC_LT12_BC013', 43, 6450000, 77.43, 13, 13),

(14, 'CTBC_LT13_BC014', 78, 11700000, 93.25, 14, 14),

(15, 'CTBC_LT14_BC015', 58, 8700000, 65.88, 15, 15),

(16, 'CTBC_LT15_BC016', 56, 14000000, 75.8, 16, 16),

(17, 'CTBC_LT16_BC017', 15, 3750000, 80.25, 17, 17),

(18, 'CTBC_LT17_BC018', 20, 5000000, 86.32, 18, 18),

(19, 'CTBC_LT18_BC019', 58, 20300000, 59.47, 19, 19),

(20, 'CTBC_LT19_BC020', 56, 14000000, 54.06, 20, 20),

(21, 'CTBC_LT20_BC021', 36, 12600000, 75.62, 21, 21),

(22, 'CTBC_LT21_BC022', 19, 4750000, 64.72, 22, 22),

(23, 'CTBC_LT22_BC023', 41, 10250000, 60.05, 23, 23),

(24, 'CTBC_LT23_BC024', 44, 11000000, 74.5, 24, 24),

(25, 'CTBC_LT24_BC025', 17, 4250000, 83.42, 25, 25),

(26, 'CTBC_LT25_BC026', 50, 22500000, 47.4, 26, 26),

(27, 'CTBC_LT26_BC027', 37, 12950000, 43.82, 27, 27),

(28, 'CTBC_LT27_BC028', 73, 32850000, 87.5, 28, 28),

(29, 'CTBC_LT28_BC029', 68, 17000000, 47.22, 29, 29),

(30, 'CTBC_LT29_BC030', 41, 14350000, 78.56, 30, 30),

(31, 'CTBC_LT30_BC031', 64, 28800000, 53.53, 31, 31),

(32, 'CTBC_LT31_BC032', 27, 12150000, 35.91, 32, 32),

(33, 'CTBC_LT32_BC033', 16, 2400000, 39.93, 33, 33),

(34, 'CTBC_LT33_BC034', 30, 13500000, 68.77, 34, 34),

(35, 'CTBC_LT34_BC035', 59, 26550000, 68.73, 35, 35),

(36, 'CTBC_LT35_BC036', 69, 24150000, 93.12, 36, 36),

(37, 'BC_DAILY_0036', 11, 1650000, 74.31, 37, 37),

(38, 'CTBC_LT37_BC038', 78, 27300000, 79.96, 38, 38),

(39, 'CTBC_LT38_BC039', 53, 7950000, 49.08, 39, 39),

(40, 'CTBC_LT39_BC040', 30, 13500000, 30.21, 40, 40),

(41, 'CTBC_LT40_BC041', 43, 10750000, 63.0, 41, 41),

(42, 'CTBC_LT41_BC042', 23, 8050000, 84.71, 42, 42),

(43, 'CTBC_LT42_BC043', 74, 18500000, 39.93, 43, 43),

(44, 'CTBC_LT43_BC044', 30, 4500000, 68.93, 44, 44),

(45, 'CTBC_LT44_BC045', 72, 10800000, 37.27, 45, 45),

(46, 'CTBC_LT45_BC046', 56, 19600000, 45.56, 46, 46),

(47, 'CTBC_LT46_BC047', 40, 6000000, 35.57, 47, 47),

(48, 'CTBC_LT47_BC048', 72, 10800000, 93.57, 48, 48),

(49, 'CTBC_LT48_BC049', 78, 19500000, 38.35, 49, 49),

(50, 'CTBC_LT49_BC050', 70, 17500000, 47.23, 50, 50),

(51, 'CTBC_LT50_BC051', 64, 16000000, 90.38, 51, 51);

INSERT INTO ChiTietBaoCao (id, maCTBC, soVeBan, doanhThuChuyen, tiLeLapDay, lichTrinhId, baoCaoId) VALUES
(52, 'CTBC_LT51_BC052', 35, 12250000, 55.93, 52, 52),

(53, 'CTBC_LT01_BC053', 57, 25650000, 88.47, 2, 53),

(54, 'CTBC_LT02_BC054', 67, 10050000, 46.11, 3, 54),

(55, 'CTBC_LT03_BC055', 18, 6300000, 31.37, 4, 55),

(56, 'CTBC_LT04_BC056', 80, 20000000, 68.25, 5, 56),

(57, 'CTBC_LT05_BC057', 10, 1500000, 76.01, 6, 57),

(58, 'CTBC_LT06_BC058', 17, 4250000, 34.38, 7, 58),

(59, 'CTBC_LT07_BC059', 14, 4900000, 34.61, 8, 59),

(60, 'CTBC_LT08_BC060', 40, 14000000, 73.48, 9, 60),

(61, 'CTBC_LT09_BC061', 37, 9250000, 77.02, 10, 61),

(62, 'CTBC_LT10_BC062', 70, 17500000, 81.0, 11, 62),

(63, 'CTBC_LT11_BC063', 62, 15500000, 36.13, 12, 63),

(64, 'CTBC_LT12_BC064', 65, 22750000, 57.53, 13, 64),

(65, 'CTBC_LT13_BC065', 69, 10350000, 73.77, 14, 65),

(66, 'CTBC_LT14_BC066', 22, 3300000, 56.17, 15, 66),

(67, 'CTBC_LT15_BC067', 53, 7950000, 46.16, 16, 67),

(68, 'CTBC_LT16_BC068', 34, 15300000, 39.11, 17, 68),

(69, 'CTBC_LT17_BC069', 33, 11550000, 60.07, 18, 69),

(70, 'CTBC_LT18_BC070', 19, 8550000, 82.52, 19, 70),

(71, 'CTBC_LT19_BC071', 80, 12000000, 33.29, 20, 71),

(72, 'CTBC_LT20_BC072', 79, 11850000, 92.98, 21, 72),

(73, 'CTBC_LT21_BC073', 40, 10000000, 56.42, 22, 73),

(74, 'CTBC_LT22_BC074', 71, 17750000, 86.2, 23, 74),

(75, 'CTBC_LT23_BC075', 17, 4250000, 54.63, 24, 75),

(76, 'CTBC_LT24_BC076', 59, 20650000, 90.22, 25, 76),

(77, 'CTBC_LT25_BC077', 68, 23800000, 57.5, 26, 77),

(78, 'CTBC_LT26_BC078', 72, 18000000, 42.34, 27, 78),

(79, 'CTBC_LT27_BC079', 37, 5550000, 67.65, 28, 79),

(80, 'CTBC_LT28_BC080', 79, 11850000, 78.62, 29, 80),

(81, 'CTBC_LT29_BC081', 17, 2550000, 67.97, 30, 81),

(82, 'CTBC_LT30_BC082', 74, 18500000, 33.7, 31, 82),

(83, 'CTBC_LT31_BC083', 75, 11250000, 85.34, 32, 83),

(84, 'CTBC_LT32_BC084', 18, 2700000, 73.89, 33, 84),

(85, 'CTBC_LT33_BC085', 40, 18000000, 37.79, 34, 85),

(86, 'CTBC_LT34_BC086', 41, 6150000, 70.26, 35, 86),

(87, 'CTBC_LT35_BC087', 63, 22050000, 90.76, 36, 87),

(88, 'CTBC_LT36_BC088', 36, 12600000, 45.51, 37, 88),

(89, 'CTBC_LT37_BC089', 60, 15000000, 73.66, 38, 89),

(90, 'CTBC_LT38_BC090', 48, 21600000, 50.55, 39, 90),

(91, 'CTBC_LT39_BC091', 19, 2850000, 59.79, 40, 91),

(92, 'CTBC_LT40_BC092', 22, 3300000, 64.95, 41, 92),

(93, 'CTBC_LT41_BC093', 74, 25900000, 38.61, 42, 93),

(94, 'CTBC_LT42_BC094', 54, 8100000, 87.15, 43, 94),

(95, 'CTBC_LT43_BC095', 57, 19950000, 40.25, 44, 95),

(96, 'CTBC_LT44_BC096', 79, 27650000, 69.76, 45, 96),

(97, 'CTBC_LT45_BC097', 77, 11550000, 73.41, 46, 97),

(98, 'CTBC_LT46_BC098', 80, 28000000, 90.56, 47, 98),

(99, 'CTBC_LT47_BC099', 23, 5750000, 47.19, 48, 99),

(100, 'CTBC_LT48_BC100', 23, 5750000, 47.7, 49, 100),

(101, 'CTBC_LT49_BC101', 36, 12600000, 43.23, 50, 101);


-- Bật lại kiểm tra khóa ngoại sau khi hoàn tất
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- KẾT THÚC SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU
-- ============================================================================
-- 1. FIX CHO TEST CASE 1 (Mã vé VE_0012)
-- Đổi khách hàng về ID hợp lệ (Phạm Văn Anh - ID 1)
UPDATE VeTau SET khachHangId = 1 WHERE maVe = 'VE_0012';
UPDATE HoaDon SET khachHangId = 1 WHERE maHoaDon = 'HD_0012';
-- Thêm chi tiết lịch trình cho chuyến tàu của vé này (lichTrinhId = 36)
INSERT INTO ChiTietLichTrinh (maCTLT, gioDen, gioDi, nhaGaId, lichTrinhId) VALUES
('CTLT_36_01', NULL, '2026-06-19 08:00:00', 10, 36), -- Ga đi: Huế
('CTLT_36_02', '2026-06-19 11:30:00', NULL, 3, 36);  -- Ga đến: Đà Nẵng

-- 2. FIX CHO TEST CASE 3 (Mã vé VE_0006 - Vé đã trả)
-- Đổi khách hàng về ID hợp lệ (Phạm Minh Đức - ID 2)
UPDATE VeTau SET khachHangId = 2 WHERE maVe = 'VE_0006';
UPDATE HoaDon SET khachHangId = 2 WHERE maHoaDon = 'HD_0006';
-- Thêm chi tiết lịch trình cho chuyến tàu của vé này (lichTrinhId = 2)
INSERT INTO ChiTietLichTrinh (maCTLT, gioDen, gioDi, nhaGaId, lichTrinhId) VALUES
('CTLT_02_01', NULL, '2026-06-02 08:00:00', 1, 2), -- Ga đi: Hà Nội
('CTLT_02_02', '2026-06-02 10:30:00', NULL, 5, 2); -- Ga đến: Hải Phòng

-- Thay '2026-06-01 20:30:00' thành mốc thời gian chỉ sau thời điểm bạn bấm test khoảng 1-2 tiếng.
UPDATE LichTrinh SET ngayKhoiHanh = '2026-06-01 20:30:00' WHERE id = 6;

-- Cập nhật ID khách hàng hợp lệ để không bị lỗi NULL trên giao diện
UPDATE VeTau SET khachHangId = 1 WHERE maVe = 'VE_0005';