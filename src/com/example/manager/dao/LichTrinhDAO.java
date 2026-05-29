package com.example.manager.dao;

import com.example.manager.entity.LichTrinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDAO extends DAO {
    public LichTrinhDAO() {
        super();
    }

    public LichTrinhDAO(Connection con) {
        super(con);
    }

    public List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        return new ArrayList<>();
    }

    public LichTrinh layThongTinLichTrinh(String maTau) {
        return new LichTrinh();
    }

    public boolean addLichTrinh(LichTrinh lichTrinh) {
        if (this.con == null) {
            System.err.println("Lỗi: Không có kết nối tới CSDL");
            return false;
        }
        
        String sql = "INSERT INTO LichTrinh (maLichTrinh, ngayKhoiHanh, trangThai, doanTauId, hanhTrinhId, quanLyId) " +
                     "VALUES (?, ?, 'ChuaChay', " +
                     "(SELECT id FROM DoanTau WHERE maTau = ?), " +
                     "(SELECT id FROM HanhTrinh WHERE maHanhTrinh = ?), 1)";
                     
        try (PreparedStatement stmt = this.con.prepareStatement(sql)) {
            // Tự sinh mã lịch trình (VD: LT_SE1_123456789)
            String maLichTrinh = "LT_" + lichTrinh.getDoanTau().getMaTau() + "_" + (System.currentTimeMillis() % 1000000);
            
            stmt.setString(1, maLichTrinh);
            stmt.setTimestamp(2, Timestamp.valueOf(lichTrinh.getNgayKhoiHanh()));
            stmt.setString(3, lichTrinh.getDoanTau().getMaTau());
            stmt.setString(4, lichTrinh.getHanhTrinh().getMaHanhTrinh());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Đã thêm Lịch Trình thành công vào DB: " + maLichTrinh);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
