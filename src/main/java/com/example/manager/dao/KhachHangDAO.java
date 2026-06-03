package com.example.manager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.manager.entity.HoaDon;
import com.example.manager.entity.KhachHang;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.LoaiDoiTuong;

public class KhachHangDAO extends DAO {

    public KhachHangDAO(Connection con) {
        super(con);
    }

    /**
     * Xử lý chính sách giá dựa theo đối tượng và khởi tạo Hóa Đơn tạm
     */
    public HoaDon xuLyChinhSachGiaVaTaoDonTam(String hoTen, String soCCCD, String soDT, String email, String loaiDoiTuongStr, LichTrinh lt, String toa, String maGhe, double giaGoc) throws Exception {
        // 1. Kiểm tra mã khách hàng từ DB dựa theo CCCD, ánh xạ đúng tên cột trong cơ sở dữ liệu
        String sqlKH = "SELECT maKH, hoTen, soCCCD, soDienThoai, email FROM KhachHang WHERE soCCCD = ?";
        KhachHang kh = null;
        try (PreparedStatement ps = con.prepareStatement(sqlKH)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getString("soCCCD"),
                            rs.getString("soDienThoai"),
                            rs.getString("email")
                    );
                }
            }
        }

        // 2. Nếu khách hàng chưa tồn tại -> Thêm mới xuống DB bằng các hàm Getter đã đồng bộ chuẩn
        if (kh == null) {
            String maKHNew = "KH" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            kh = new KhachHang(maKHNew, hoTen, soCCCD, soDT, email);
            String sqlInsert = "INSERT INTO KhachHang (maKH, hoTen, soCCCD, soDienThoai, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                // ĐỒNG BỘ: Gọi chuẩn tên phương thức có trong thực thể KhachHang.java
                ps.setString(1, kh.getMaKH());
                ps.setString(2, kh.getHoTen());
                ps.setString(3, kh.getSoCCCD());
                ps.setString(4, kh.getSoDienThoai());
                ps.setString(5, kh.getEmail());
                ps.executeUpdate();
            }
        }

        // 3. Đọc phần trăm giảm giá từ bảng ChinhSachGia trong DB
        LoaiDoiTuong doiTuongEnum = LoaiDoiTuong.valueOf(loaiDoiTuongStr);
        String tenLoaiDoiTuongDB = "Người lớn";
        if (doiTuongEnum == LoaiDoiTuong.SINH_VIEN) {
            tenLoaiDoiTuongDB = "Sinh viên";
        } else if (doiTuongEnum == LoaiDoiTuong.TRE_EM) {
            tenLoaiDoiTuongDB = "Trẻ em";
        }

        double phanTramGiam = 0.0;
        String sqlCS = "SELECT tiLeGiamGia FROM ChinhSachGia WHERE LoaiDoiTuong = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlCS)) {
            ps.setString(1, tenLoaiDoiTuongDB);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    phanTramGiam = rs.getDouble("tiLeGiamGia");
                }
            }
        }

        // 4. Tính toán trị số tổng tiền sau khi áp dụng chính sách giảm giá
        double giaThucTe = giaGoc * (1.0 - phanTramGiam);

        // 5. Khởi tạo đối tượng HoaDon chuẩn hệ thống
        HoaDon bill = new HoaDon();
        bill.setMaHoaDon("HD-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        bill.setLoaiHoaDon("Mua ve");
        bill.setNgayTao(LocalDateTime.now());
        bill.setTongTien((int) giaThucTe);

        return bill;
    }

    /**
     * Lấy mã khách hàng dựa vào số CCCD phục vụ cho việc lưu Vé Tàu vĩnh viễn
     */
    public String layMaKhachHangTheoCCCD(String soCCCD) throws Exception {
        String sql = "SELECT maKH FROM KhachHang WHERE soCCCD = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maKH");
                }
            }
        }
        return "";
    }
}
