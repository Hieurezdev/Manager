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

    public java.util.List<HanhTrinh> getAllHanhTrinh() {
        if (con != null) {
            String sql = "SELECT maHanhTrinh, tenHanhTrinh, quangDuong FROM HanhTrinh ORDER BY maHanhTrinh";
            List<HanhTrinh> list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HanhTrinh ht = new HanhTrinh();
                    ht.setMaHanhTrinh(rs.getString("maHanhTrinh"));
                    ht.setTenHanhTrinh(rs.getString("tenHanhTrinh"));
                    ht.setQuangDuong(rs.getDouble("quangDuong"));
                    list.add(ht);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        return new ArrayList<>();
    }

    public java.util.List<com.example.manager.entity.ChiTietHanhTrinh> getGaTrungGian(String maHanhTrinh) {
        if (con != null) {
            String sql = "SELECT ct.maCTHT, ct.thuTuGa, ng.maGa, ng.tenNhaGa, ng.soDienThoai " +
                         "FROM ChiTietHanhTrinh ct " +
                         "JOIN HanhTrinh ht ON ct.hanhTrinhId = ht.id " +
                         "JOIN NhaGa ng ON ct.nhaGaId = ng.id " +
                         "WHERE ht.maHanhTrinh = ? " +
                         "ORDER BY ct.thuTuGa";
            List<ChiTietHanhTrinh> result = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHanhTrinh);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        NhaGa ga = new NhaGa(
                            rs.getString("maGa"),
                            rs.getString("tenNhaGa"),
                            rs.getString("soDienThoai")
                        );
                        result.add(new ChiTietHanhTrinh(
                            rs.getString("maCTHT"),
                            rs.getInt("thuTuGa"),
                            ga
                        ));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        return new ArrayList<>();
    }
}
