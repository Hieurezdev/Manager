package com.example.manager.dao;

import com.example.manager.entity.PhieuTraVe;
import com.example.manager.entity.VeTau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuTraVeDAO extends DAO {
    public static List<PhieuTraVe> mockPhieuTraVeList = new ArrayList<>();

    public PhieuTraVeDAO(Connection con) {
        super(con);
    }

    public int tinhTienPhat(VeTau veTau) {
        if (veTau == null || veTau.getLichTrinh() == null || veTau.getLichTrinh().getNgayKhoiHanh() == null) {
            return 0;
        }

        LocalDateTime gioDi = veTau.getLichTrinh().getNgayKhoiHanh();
        LocalDateTime hienTai = LocalDateTime.now();

        Duration duration = Duration.between(hienTai, gioDi);
        long hours = duration.toHours();

        if (hours >= 24) {
            return (int) (veTau.getGiaVe() * 0.1); // Phạt 10%
        } else if (hours >= 4) {
            return (int) (veTau.getGiaVe() * 0.2); // Phạt 20%
        } else {
            return -1; // Ngoại lệ: Sát giờ tàu chạy (< 4 tiếng) hoặc đã chạy, không thể trả vé
        }
    }

    public boolean createPhieuTraVe(PhieuTraVe phieu) {
        return createPhieuTraVe(phieu, null);
    }

    public boolean createPhieuTraVe(PhieuTraVe phieu, String maHoaDon) {
        if (con != null) {
            String sql = "INSERT INTO PhieuTraVe (maPhieuTra, thoiDiemTra, tienPhat, tienHoanLaiKhach, veTauId, hoaDonId) " +
                         "VALUES (?, ?, ?, ?, (SELECT id FROM VeTau WHERE maVe = ?), (SELECT id FROM HoaDon WHERE maHoaDon = ?))";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, phieu.getMaPhieu());
                ps.setTimestamp(2, Timestamp.valueOf(phieu.getNgayTao()));
                ps.setInt(3, phieu.getTienPhat());
                ps.setInt(4, phieu.getTienHoanLai());
                ps.setString(5, phieu.getVeTau() != null ? phieu.getVeTau().getMaVe() : null);
                ps.setString(6, maHoaDon);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            mockPhieuTraVeList.add(phieu);
            return true;
        }
    }
}
