package com.example.manager.dao;

import com.example.manager.entity.HoaDon;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.PhuongThucThanhToan;
import com.example.manager.enums.TrangThaiVe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoaDonDAO extends DAO {
    // Lưu lại bộ nhớ tạm Mock Data theo cấu trúc của bạn ông
    public static List<HoaDon> mockHoaDonList = new ArrayList<>();
    public static List<String> mockGheDaDatList = new ArrayList<>(); // Giả lập lưu ghế đã đặt khi dùng mock

    public HoaDonDAO(Connection con) {
        super(con);
    }

    /**
     * Hàm tạo hóa đơn gốc từ GitHub (Giữ nguyên 100% để bạn ông không bị gãy code)
     */
    public boolean createHoaDon(HoaDon hoaDon) {
        if (con != null) {
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
        } else {
            mockHoaDonList.add(hoaDon);
            return true;
        }
    }

    /**
     * ĐỒNG BỘ CHỨC NĂNG 1: Cập nhật trạng thái ghế (Bọc cơ chế Mock Data)
     */
    public boolean capNhatTrangThaiGhe(String maGhe, String trangThai) throws Exception {
        if (con != null) {
            // Chạy DB thật nếu có kết nối
            String sql = "UPDATE GheNgoi SET trangThai = ? WHERE maGhe = ? AND (trangThai = 'TRONG' OR ? = 'DA_DAT' OR ? = 'TRONG')";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, trangThai); 
                ps.setString(2, maGhe); 
                ps.setString(3, trangThai); 
                ps.setString(4, trangThai);
                return ps.executeUpdate() > 0;
            }
        } else {
            // Chạy Mock Data khi đứt kết nối hoặc theo yêu cầu của nhóm
            if ("DA_DAT".equals(trangThai)) {
                mockGheDaDatList.add(maGhe);
            } else if ("TRONG".equals(trangThai)) {
                mockGheDaDatList.remove(maGhe);
            }
            return true;
        }
    }

    /**
     * ĐỒNG BỘ CHỨC NĂNG 2: Lưu giao dịch thanh toán (Đổi CampBill thành HoaDon chuẩn hệ thống)
     */
    public void luuGiaoDichThanhToanThat(String maNV, HoaDon bill, String maLichTrinh, String maGhe, String maKH) throws Exception {
        if (con != null) {
            // Logic chạy bằng DB thật của ông (Giữ nguyên toàn bộ Transaction)
            try {
                con.setAutoCommit(false);
                
                String sqlHD = "INSERT INTO HoaDon (maHoaDon, loaiHoaDon, ngayGioLap, maNhanVienLap, maKhachHang, tongTien, phuongThucThanhToan, trangThai) VALUES (?, 'Mua ve', NOW(), ?, ?, ?, ?, 'Da thanh toan')";
                try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
                    ps.setString(1, bill.getMaHoaDon()); 
                    ps.setString(2, maNV); 
                    ps.setString(3, maKH); 
                    ps.setDouble(4, bill.getTongTien()); 
                    ps.setString(5, PhuongThucThanhToan.TIEN_MAT.name());
                    ps.executeUpdate();
                }
                
                String sqlVe = "INSERT INTO VeTau (maVe, maKH, maLichTrinh, loaiDoiTuong, maGhe, giaVe, trangThai, thoiDiemBanVe, maNhanVienBanVe, maHoaDon) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlVe)) {
                    ps.setString(1, "V-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase()); 
                    ps.setString(2, maKH); 
                    ps.setString(3, maLichTrinh); 
                    ps.setString(4, LoaiDoiTuong.NGUOI_LON.name()); 
                    ps.setString(5, maGhe); 
                    ps.setDouble(6, bill.getTongTien()); 
                    ps.setString(7, TrangThaiVe.DA_BAN.name()); 
                    ps.setString(8, maNV); 
                    ps.setString(9, bill.getMaHoaDon());
                    ps.executeUpdate();
                }
                
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
        } else {
            // Logic chạy bằng Mock Data khi không có DB
            mockHoaDonList.add(bill);
            mockGheDaDatList.add(maGhe);
            System.out.println(">>> MOCK DATA LOG: Đã lưu hóa đơn tạm " + bill.getMaHoaDon() + " và khóa ghế " + maGhe);
        }
    }
}