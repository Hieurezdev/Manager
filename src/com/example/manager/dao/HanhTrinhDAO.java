package com.example.manager.dao;

import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.NhaGa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HanhTrinhDAO extends DAO {
    public HanhTrinhDAO(Connection con) {
        super(con);
    }

    public HanhTrinh layThongTinHanhTrinh(String maHanhTrinh) {
        if (con != null) {
            String sql = "SELECT ht.maHanhTrinh, ht.tenHanhTrinh, ht.quangDuong, " +
                         "ct.maCTHT, ct.thuTuGa, ng.maGa, ng.tenNhaGa, ng.soDienThoai " +
                         "FROM HanhTrinh ht " +
                         "LEFT JOIN ChiTietHanhTrinh ct ON ct.hanhTrinhId = ht.id " +
                         "LEFT JOIN NhaGa ng ON ct.nhaGaId = ng.id " +
                         "WHERE ht.maHanhTrinh = ? " +
                         "ORDER BY ct.thuTuGa";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHanhTrinh);
                try (ResultSet rs = ps.executeQuery()) {
                    HanhTrinh hanhTrinh = null;
                    List<ChiTietHanhTrinh> chiTietList = new ArrayList<>();
                    while (rs.next()) {
                        if (hanhTrinh == null) {
                            hanhTrinh = new HanhTrinh();
                            hanhTrinh.setMaHanhTrinh(rs.getString("maHanhTrinh"));
                            hanhTrinh.setTenHanhTrinh(rs.getString("tenHanhTrinh"));
                            hanhTrinh.setQuangDuong(rs.getDouble("quangDuong"));
                        }

                        String maCTHT = rs.getString("maCTHT");
                        if (maCTHT != null) {
                            NhaGa ga = new NhaGa(
                                rs.getString("maGa"),
                                rs.getString("tenNhaGa"),
                                rs.getString("soDienThoai")
                            );
                            chiTietList.add(new ChiTietHanhTrinh(
                                maCTHT,
                                rs.getInt("thuTuGa"),
                                ga
                            ));
                        }
                    }

                    if (hanhTrinh != null) {
                        hanhTrinh.setChiTietHanhTrinh(chiTietList);
                    }
                    return hanhTrinh;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        return HanhTrinh.layThongTinHanhTrinh(maHanhTrinh);
    }
}
