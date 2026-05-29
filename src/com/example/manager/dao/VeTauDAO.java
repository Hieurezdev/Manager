package com.example.manager.dao;

import com.example.manager.entity.VeTau;
import com.example.manager.entity.KhachHang;
import com.example.manager.entity.GheNgoi;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.NhanVien;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.enums.TrangThaiVe;
import com.example.manager.enums.TrangThaiGhe;
import com.example.manager.enums.TrangThaiLichTrinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeTauDAO extends DAO {
    public static List<VeTau> mockVeTauList = new ArrayList<>();

    public VeTauDAO(Connection con) {
        super(con);
    }

    private String getDbTrangThaiVe(TrangThaiVe tt) {
        if (tt == TrangThaiVe.DA_BAN) return "DaBan";
        if (tt == TrangThaiVe.DA_TRA) return "DaTra";
        if (tt == TrangThaiVe.DA_HUY) return "DaTra";
        return "DaBan";
    }

    private TrangThaiVe getJavaTrangThaiVe(String dbVal) {
        if ("DaBan".equalsIgnoreCase(dbVal)) return TrangThaiVe.DA_BAN;
        if ("DaTra".equalsIgnoreCase(dbVal)) return TrangThaiVe.DA_TRA;
        if ("DaHuy".equalsIgnoreCase(dbVal)) return TrangThaiVe.DA_TRA;
        if ("DaDoi".equalsIgnoreCase(dbVal)) return TrangThaiVe.DA_TRA;
        return TrangThaiVe.DA_BAN;
    }

    public List<VeTau> layDanhSachVeTheoLichTrinh(String maLichTrinh) {
        if (con != null) {
            List<VeTau> list = new ArrayList<>();
            String sql = "SELECT v.* FROM VeTau v " +
                         "LEFT JOIN LichTrinh l ON v.lichTrinhId = l.id " +
                         "WHERE l.maLichTrinh = ?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, maLichTrinh);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    VeTau ve = new VeTau();
                    ve.setMaVe(rs.getString("maVe"));
                    ve.setGiaVe(rs.getInt("giaVe"));
                    ve.setTrangThai(getJavaTrangThaiVe(rs.getString("trangThai")));
                    list.add(ve);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        } else {
            List<VeTau> list = new ArrayList<>();
            for (VeTau v : mockVeTauList) {
                if (v.getLichTrinh() != null && maLichTrinh.equals(v.getLichTrinh().getMaLichTrinh())) {
                    list.add(v);
                }
            }
            return list;
        }
    }

    public VeTau searchVeTau(String maVe) {
        if (con != null) {
            String sql = "SELECT v.*, " +
                         "k.maKH, k.hoTen, k.soCCCD, k.soDienThoai, k.email, " +
                         "g.maGhe, g.soGhe, g.viTri, g.trangThai as ttGhe, g.moTa as mtGhe, " +
                         "l.maLichTrinh, l.ngayKhoiHanh, l.trangThai as ttLichTrinh, " +
                         "h.maHanhTrinh, h.tenHanhTrinh, " +
                         "d.maTau, d.tenTau " +
                         "FROM VeTau v " +
                         "LEFT JOIN KhachHang k ON v.khachHangId = k.id " +
                         "LEFT JOIN GheNgoi g ON v.gheNgoiId = g.id " +
                         "LEFT JOIN LichTrinh l ON v.lichTrinhId = l.id " +
                         "LEFT JOIN HanhTrinh h ON l.hanhTrinhId = h.id " +
                         "LEFT JOIN DoanTau d ON l.doanTauId = d.id " +
                         "WHERE v.maVe = ?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, maVe);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    VeTau ve = new VeTau();
                    ve.setMaVe(rs.getString("maVe"));
                    ve.setGiaVe(rs.getInt("giaVe"));
                    ve.setTrangThai(getJavaTrangThaiVe(rs.getString("trangThai")));
                    if (rs.getTimestamp("thoiDiemBanVe") != null) {
                        ve.setThoiDiemBanVe(rs.getTimestamp("thoiDiemBanVe").toLocalDateTime());
                    }
                    
                    KhachHang kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("hoTen"),
                        rs.getString("soCCCD"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")
                    );
                    ve.setKhachHang(kh);

                    GheNgoi ghe = new GheNgoi();
                    ghe.setMaGhe(rs.getString("maGhe"));
                    ghe.setSoGhe(rs.getInt("soGhe"));
                    ghe.setViTri(rs.getString("viTri"));
                    
                    String dbGhe = rs.getString("ttGhe");
                    if ("Trong".equalsIgnoreCase(dbGhe)) {
                        ghe.setTrangThai(TrangThaiGhe.TRONG);
                    } else {
                        ghe.setTrangThai(TrangThaiGhe.DA_DAT);
                    }
                    ghe.setMoTa(rs.getString("mtGhe"));
                    ve.setGheNgoi(ghe);

                    // Khoi tao Lich trinh
                    LichTrinh lt = new LichTrinh();
                    lt.setMaLichTrinh(rs.getString("maLichTrinh"));
                    if (rs.getTimestamp("ngayKhoiHanh") != null) {
                        lt.setNgayKhoiHanh(rs.getTimestamp("ngayKhoiHanh").toLocalDateTime());
                    }
                    
                    String dbLt = rs.getString("ttLichTrinh");
                    if ("ChuaChay".equalsIgnoreCase(dbLt)) {
                        lt.setTrangThai(TrangThaiLichTrinh.DANG_CHAY);
                    } else {
                        lt.setTrangThai(TrangThaiLichTrinh.HOAN_THANH);
                    }

                    // Khoi tao Hanh trinh
                    HanhTrinh ht = new HanhTrinh();
                    ht.setMaHanhTrinh(rs.getString("maHanhTrinh"));
                    ht.setTenHanhTrinh(rs.getString("tenHanhTrinh"));
                    lt.setHanhTrinh(ht);

                    // Khoi tao Doan tau
                    DoanTau dt = new DoanTau();
                    dt.setMaTau(rs.getString("maTau"));
                    dt.setTenTau(rs.getString("tenTau"));
                    lt.setDoanTau(dt);

                    ve.setLichTrinh(lt);

                    return ve;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            for (VeTau v : mockVeTauList) {
                if (maVe.equals(v.getMaVe())) {
                    return v;
                }
            }
            return null;
        }
    }

    public boolean updateTrangThai(String maVe, TrangThaiVe trangThai) {
        if (con != null) {
            String sql = "UPDATE VeTau SET trangThai = ? WHERE maVe = ?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, getDbTrangThaiVe(trangThai));
                ps.setString(2, maVe);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            for (VeTau v : mockVeTauList) {
                if (maVe.equals(v.getMaVe())) {
                    v.setTrangThai(trangThai);
                    return true;
                }
            }
            return false;
        }
    }
}
