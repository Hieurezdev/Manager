package com.example.manager.dao;

import com.example.manager.entity.GheNgoi;
import com.example.manager.entity.LichTrinh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDAO extends DAO {
    private double giaVeGocTieuChuan = 850000;

    public LichTrinhDAO(Connection con) {
        super(con);
    }

    public double getGiaVeGocTieuChuan() { 
        return giaVeGocTieuChuan; 
    }

    public List<LichTrinh> layDanhSachChuyenTauPhuHop(String gaDi, String gaDen, String ngayDi) throws Exception {
        String sqlQD = "SELECT quangDuong FROM HanhTrinh WHERE maGaDi = ? AND maGaDen = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlQD)) {
            ps.setString(1, gaDi); ps.setString(2, gaDen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.giaVeGocTieuChuan = rs.getDouble("quangDuong") * 1000; 
                }
            }
        }

        List<LichTrinh> list = new ArrayList<>();
        String sqlLT = "SELECT lt.maLichTrinh, lt.maTau, ctt.gioDi, ctt.gioDen FROM LichTrinh lt JOIN ChiTietLichTrinh ctt ON lt.maLichTrinh = ctt.maLichTrinh WHERE lt.ngayKhoiHanh = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlLT)) {
            ps.setString(1, ngayDi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LichTrinh(rs.getString("maLichTrinh"), rs.getString("maTau"), rs.getString("gioDi"), rs.getString("gioDen")));
                }
            }
        }
        return list;
    }

    // ĐÃ SỬA: Thêm tham số tenToa vào hàm
    public List<GheNgoi> layThongTinLichTrinh(String maLichTrinh, String tenToa) throws Exception {
        List<GheNgoi> list = new ArrayList<>();
        // ĐÃ SỬA: Thêm điều kiện AND t.tenToa = ? ở cuối câu SQL
        String sql = "SELECT g.maGhe, g.soGhe, g.viTri, g.trangThai, g.maToa FROM GheNgoi g JOIN ToaTau t ON g.maToa = t.maToa WHERE t.maTau = (SELECT maTau FROM LichTrinh WHERE maLichTrinh = ?) AND t.tenToa = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            ps.setString(2, tenToa); // Nạp tên toa (Ví dụ: "Toa 1", "Toa 4") vào đây
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new GheNgoi(rs.getString("maGhe"), rs.getString("soGhe"), rs.getString("viTri"), rs.getString("trangThai"), rs.getString("maToa")));
                }
            }
        }
        return list;
    }
}