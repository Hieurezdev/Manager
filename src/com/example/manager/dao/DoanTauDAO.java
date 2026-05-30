package com.example.manager.dao;

import com.example.manager.entity.DoanTau;
import com.example.manager.entity.ToaTau;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.LoaiToa;
import com.example.manager.enums.TrangThaiTau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoanTauDAO extends DAO {
    public DoanTauDAO(Connection con) {
        super(con);
    }

    public List<DoanTau> layDanhSachDoanTau() {
        if (con != null) {
            String sql = "SELECT id, maTau, tenTau, loaiTau, trangThai FROM DoanTau";
            List<DoanTau> list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DoanTau tau = new DoanTau();
                    tau.setMaTau(rs.getString("maTau"));
                    tau.setTenTau(rs.getString("tenTau"));
                    tau.setLoaiTau(mapLoaiTau(rs.getString("loaiTau")));
                    tau.setTrangThai(mapTrangThaiTau(rs.getString("trangThai")));

                    int doanTauId = rs.getInt("id");
                    for (ToaTau toa : loadToaTauByDoanTauId(doanTauId)) {
                        tau.addToaTau(toa);
                    }

                    list.add(tau);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        return DoanTau.layDanhSachDoanTau();
    }

    private List<ToaTau> loadToaTauByDoanTauId(int doanTauId) {
        List<ToaTau> list = new ArrayList<>();
        String sql = "SELECT maToa, tenToa, soThuTu, loaiToa, soLuongGheToiDa, moTa " +
                     "FROM ToaTau WHERE doanTauId = ? ORDER BY soThuTu";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doanTauId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ToaTau toa = new ToaTau(
                        rs.getString("maToa"),
                        rs.getString("tenToa"),
                        rs.getInt("soThuTu"),
                        mapLoaiToa(rs.getString("loaiToa")),
                        rs.getInt("soLuongGheToiDa"),
                        rs.getString("moTa")
                    );
                    list.add(toa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private LoaiTau mapLoaiTau(String dbVal) {
        if ("TauNhanh".equalsIgnoreCase(dbVal)) {
            return LoaiTau.THONG_NHAT;
        }
        if ("TauThuong".equalsIgnoreCase(dbVal)) {
            return LoaiTau.DIA_PHUONG;
        }
        return LoaiTau.KHAC;
    }

    private TrangThaiTau mapTrangThaiTau(String dbVal) {
        if ("SanSang".equalsIgnoreCase(dbVal)) {
            return TrangThaiTau.HOAT_DONG;
        }
        if ("BaoTri".equalsIgnoreCase(dbVal)) {
            return TrangThaiTau.BAO_TRI;
        }
        return TrangThaiTau.NGUNG;
    }

    private LoaiToa mapLoaiToa(String dbVal) {
        if ("NgoiCung".equalsIgnoreCase(dbVal)) {
            return LoaiToa.NGOI_CUNG;
        }
        if ("NgoiMem".equalsIgnoreCase(dbVal)) {
            return LoaiToa.NGOI_MEM;
        }
        if ("GiuongNam".equalsIgnoreCase(dbVal)) {
            return LoaiToa.GIUONG_NAM;
        }
        return LoaiToa.NGOI_CUNG;
    }

    public List<DoanTau> getAllDoanTau() {
        return layDanhSachDoanTau();
    }
}
