package com.example.manager.dao;

import java.sql.Connection;
import com.example.manager.entity.CampBill;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.PhuongThucThanhToan;
import com.example.manager.enums.TrangThaiVe;
import java.sql.PreparedStatement;
import java.util.UUID;

public class HoaDonDAO extends DAO {

    public HoaDonDAO(Connection con) {
        super(con);
    }
    
    public boolean capNhatTrangThaiGhe(String maGhe, String trangThai) throws Exception {
        String sql = "UPDATE GheNgoi SET trangThai = ? WHERE maGhe = ? AND (trangThai = 'TRONG' OR ? = 'DA_DAT' OR ? = 'TRONG')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai); ps.setString(2, maGhe); ps.setString(3, trangThai); ps.setString(4, trangThai);
            return ps.executeUpdate() > 0;
        }
    }

    public void luuGiaoDichThanhToanThat(String maNV, CampBill bill, String maLichTrinh, String maGhe, String maKH) throws Exception {
        try {
            con.setAutoCommit(false);
            
            String sqlHD = "INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, maNhanVienLap, maKhachHang, tongTien, phuongThucThanhToan, trangThai) VALUES (?, 'Mua ve', NOW(), ?, ?, ?, ?, 'Da thanh toan')";
            try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
                ps.setString(1, bill.getMaHoaDon()); ps.setString(2, maNV); ps.setString(3, maKH); ps.setDouble(4, bill.getTongTien()); ps.setString(5, PhuongThucThanhToan.TIEN_MAT.name());
                ps.executeUpdate();
            }
            
            String sqlVe = "INSERT INTO VeTau (maVe, maKH, maLichTrinh, loaiDoiTuong, maGhe, giaVe, trangThai, thoiDiemBanVe, maNhanVienBanVe, maHoaDon) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlVe)) {
                ps.setString(1, "V-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase()); ps.setString(2, maKH); ps.setString(3, maLichTrinh); ps.setString(4, LoaiDoiTuong.NGUOI_LON.name()); ps.setString(5, maGhe); ps.setDouble(6, bill.getTongTien()); ps.setString(7, TrangThaiVe.DA_BAN.name()); ps.setString(8, maNV); ps.setString(9, bill.getMaHoaDon());
                ps.executeUpdate();
            }
            
            String sqlGhe = "UPDATE GheNgoi SET trangThai = 'DA_DAT' WHERE maGhe = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlGhe)) {
                ps.setString(1, maGhe); ps.executeUpdate();
            }
            
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }
}