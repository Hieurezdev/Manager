package com.example.manager.dao;

import com.example.manager.entity.HoaDon;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.PhuongThucThanhToan;
import com.example.manager.enums.TrangThaiVe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class HoaDonDAO extends DAO {

    public HoaDonDAO(Connection con) {
        super(con);
    }

    /**
     * Hàm tạo hóa đơn gốc từ GitHub (Giữ nguyên logic lõi của bạn ông để né
     * conflict)
     */
    public boolean createHoaDon(HoaDon hoaDon) {
        if (con == null) {
            return false;
        }

        String sql = "INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, phuongThucThanhToan, tongTien, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, hoaDon.getMaHoaDon());
            ps.setString(2, hoaDon.getLoaiHoaDon());
            ps.setTimestamp(3, Timestamp.valueOf(hoaDon.getNgayTao()));
            ps.setString(4, "TienMat");
            ps.setInt(5, hoaDon.getTongTien());
            ps.setString(6, "DaThanhToan");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * CHỨC NĂNG 1: Cập nhật trạng thái ghế chạy trực tiếp trên Database thật
     */
    public boolean capNhatTrangThaiGhe(String maGhe, String trangThai) throws Exception {
        if (con == null) {
            return false;
        }

        String sql = "UPDATE GheNgoi SET trangThai = ? WHERE maGhe = ? AND (trangThai = 'TRONG' OR ? = 'DA_DAT' OR ? = 'TRONG')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maGhe);
            ps.setString(3, trangThai);
            ps.setString(4, trangThai);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * CHỨC NĂNG 2: Lưu giao dịch thanh toán thực tế xuống DB thật của nhóm (Đảm
     * bảo tính toàn vẹn dữ liệu)
     */
    public void luuGiaoDichThanhToanThat(String maNV, HoaDon bill, String maLichTrinh, String maGhe, String maKH) throws Exception {
        if (con == null) {
            return;
        }

        try {
            con.setAutoCommit(false);

            // 1. Lưu thông tin hóa đơn (Đồng bộ CURRENT_TIMESTAMP chạy xuyên suốt các hệ quản trị DB)
            String sqlHD = "INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, maNhanVienLap, maKhachHang, tongTien, phuongThucThanhToan, trangThai) "
                    + "VALUES (?, 'Mua ve', CURRENT_TIMESTAMP, ?, ?, ?, ?, 'Da thanh toan')";
            try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
                ps.setString(1, bill.getMaHoaDon());
                ps.setString(2, maNV);
                ps.setString(3, maKH);
                // Ép kiểu double về int cho khớp thuộc tính tổng tiền của bạn ông làm Thống kê
                ps.setInt(4, (int) bill.getTongTien());
                ps.setString(5, PhuongThucThanhToan.TIEN_MAT.name());
                ps.executeUpdate();
            }

            // 2. Lưu thông tin vé tàu phát sinh vào hệ thống
            String sqlVe = "INSERT INTO VeTau (maVe, maKH, maLichTrinh, loaiDoiTuong, maGhe, giaVe, trangThai, thoiDiemBanVe, maNhanVienBanVe, maHoaDon) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlVe)) {
                ps.setString(1, "V-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
                ps.setString(2, maKH);
                ps.setString(3, maLichTrinh);
                ps.setString(4, LoaiDoiTuong.NGUOI_LON.name());
                ps.setString(5, maGhe);
                ps.setInt(6, (int) bill.getTongTien());
                ps.setString(7, TrangThaiVe.DA_BAN.name());
                ps.setString(8, maNV);
                ps.setString(9, bill.getMaHoaDon());
                ps.executeUpdate();
            }

            // 3. Cập nhật dứt điểm trạng thái ghế sang 'DA_DAT'
            String sqlGhe = "UPDATE GheNgoi SET trangThai = 'DA_DAT' WHERE maGhe = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlGhe)) {
                ps.setString(1, maGhe);
                ps.executeUpdate();
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
