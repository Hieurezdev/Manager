package com.example.manager.dao;

import com.example.manager.entity.HoaDon;
import com.example.manager.entity.KhachHang;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.LoaiDoiTuong;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class KhachHangDAO extends DAO {

    public KhachHangDAO(Connection con) {
        super(con);
    }
    
    /**
     * Xử lý chính sách giá dựa theo đối tượng và khởi tạo Hóa Đơn tạm (Đã đồng bộ sang entity HoaDon)
     */
    public HoaDon xuLyChinhSachGiaVaTaoHoaDonTam(String hoTen, String soCCCD, String soDT, String email, String loaiDoiTuongStr, LichTrinh lt, String toa, String maGhe, double giaGoc) throws Exception {
        String sqlKH = "SELECT * FROM KhachHang WHERE soCCCD = ?";
        KhachHang kh = null;
        try (PreparedStatement ps = con.prepareStatement(sqlKH)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Tận dụng constructor KhachHang phù hợp với DB
                    kh = new KhachHang(rs.getString("maKhachHang"), rs.getString("tenKhachHang"), rs.getString("soCCCD"), rs.getString("soDienThoai"), rs.getString("email"));
                }
            }
        }

        // Nếu khách hàng chưa tồn tại trong hệ thống -> Tự động thêm mới
        if (kh == null) {
            String maKHNew = "KH" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            kh = new KhachHang(maKHNew, hoTen, soCCCD, soDT, email);
            String sqlInsert = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soCCCD, soDienThoai, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setString(1, kh.getMaKhachHang()); 
                ps.setString(2, kh.getTenKhachHang()); 
                ps.setString(3, kh.getSoCCCD()); 
                ps.setString(4, kh.getSoDienThoai()); 
                ps.setString(5, kh.getEmail());
                ps.executeUpdate();
            }
        }

        // Đọc phần trăm giảm giá từ bảng ChinhSachGia trong DB thật
        LoaiDoiTuong doiTuongEnum = LoaiDoiTuong.valueOf(loaiDoiTuongStr);
        String tenLoaiDoiTuongDB = "Người lớn";
        if (doiTuongEnum == LoaiDoiTuong.SINH_VIEN) tenLoaiDoiTuongDB = "Sinh viên";
        else if (doiTuongEnum == LoaiDoiTuong.TRE_EM) tenLoaiDoiTuongDB = "Trẻ em";

        double phanTramGiam = 0.0;
        String sqlCS = "SELECT phanTramGiamGia FROM ChinhSachGia WHERE tenLoaiDoiTuong = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlCS)) {
            ps.setString(1, tenLoaiDoiTuongDB);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) phanTramGiam = rs.getDouble("phanTramGiamGia");
            }
        }

        // Tính toán trị số tổng tiền sau khi áp dụng chính sách giảm giá
        double giaThucTe = giaGoc * (1.0 - phanTramGiam);

        // Khởi tạo đối tượng HoaDon chuẩn hệ thống thay vì CampBill cũ
        HoaDon bill = new HoaDon();
        bill.setMaHoaDon("HD-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        bill.setLoaiDon("Mua ve");
        bill.setNgayTao(LocalDateTime.now());
        bill.setTongTien((int) giaThucTe); // Ép kiểu về int theo cấu trúc setTongTien(int) của bạn ông
        
        return bill;
    }

    /**
     * Lấy mã khách hàng dựa vào số CCCD phục vụ cho việc lưu Vé Tàu
     */
    public String layMaKhachHangTheoCCCD(String soCCCD) throws Exception {
        String sql = "SELECT maKhachHang FROM KhachHang WHERE soCCCD = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maKhachHang");
            }
        }
        return "";
    }
}