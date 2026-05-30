package com.example.manager.dao;

import com.example.manager.entity.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO extends DAO {
    public static List<HoaDon> mockHoaDonList = new ArrayList<>();

    public HoaDonDAO(Connection con) {
        super(con);
    }

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
}
