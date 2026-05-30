package com.example.manager.dao;

import com.example.manager.entity.CampBill;
import com.example.manager.entity.KhachHang;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.LoaiDoiTuong;
import java.sql.*;
import java.util.UUID;

public class KhachHangDAO extends DAO {

    public KhachHangDAO(Connection con) {
        super(con);
    }
    
    public CampBill xuLyChinhSachGiaVaTaoHoaDonTam(String hoTen, String soCCCD, String soDT, String email, String loaiDoiTuongStr, LichTrinh lt, String toa, String maGhe, double giaGoc) throws Exception {
        String sqlKH = "SELECT * FROM KhachHang WHERE soCCCD = ?";
        KhachHang kh = null;
        try (PreparedStatement ps = con.prepareStatement(sqlKH)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHang(rs.getString("maKH"), rs.getString("hoTen"), rs.getString("soCCCD"), rs.getString("soDienThoai"), rs.getString("email"));
                }
            }
        }

        if (kh == null) {
            kh = new KhachHang("KH" + UUID.randomUUID().toString().substring(0, 4).toUpperCase(), hoTen, soCCCD, soDT, email);
            String sqlInsert = "INSERT INTO KhachHang (maKH, hoTen, soCCCD, soDienThoai, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setString(1, kh.getMaKH()); ps.setString(2, kh.getHoTen()); ps.setString(3, kh.getSoCCCD()); ps.setString(4, kh.getSoDienThoai()); ps.setString(5, kh.getEmail());
                ps.executeUpdate();
            }
        }

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

        double giaThucTe = giaGoc * (1.0 - phanTramGiam);

        CampBill bill = new CampBill();
        bill.setMaHoaDon("HD-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        bill.setHoTen(kh.getHoTen()); bill.setCccd(kh.getSoCCCD());
        bill.setChuyenTau(lt.getMaTau()); bill.setViTri(toa + " - Ghe " + maGhe);
        bill.setTongTien(giaThucTe);
        return bill;
    }

    public String layMaKhachHangTheoCCCD(String soCCCD) throws Exception {
        String sql = "SELECT maKH FROM KhachHang WHERE soCCCD = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soCCCD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maKH");
            }
        }
        return "";
    }
}