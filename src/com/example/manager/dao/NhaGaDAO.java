package com.example.manager.dao;

import com.example.manager.entity.NhaGa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NhaGaDAO — delegates all domain logic to NhaGa entity methods,
 * consistent with the DAO pattern used in the rest of the codebase.
 * An in-memory store simulates persistence in the absence of a live DB connection.
 */
public class NhaGaDAO extends DAO {

    // In-memory store (replaces DB until a real Connection is wired in)
    private final List<NhaGa> store = new ArrayList<>();

    public NhaGaDAO(Connection con) {
        super(con);
    }

    // --- Sequence diagram step 20: layDanhSachGa() ---
    public List<NhaGa> layDanhSachGa() {
        if (con != null) {
            String sql = "SELECT maGa, tenNhaGa, diaChi, soDienThoai FROM NhaGa ORDER BY maGa";
            List<NhaGa> list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new NhaGa(
                        rs.getString("maGa"),
                        rs.getString("tenNhaGa"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        return NhaGa.layDanhSach(store);
    }

    // --- Sequence diagram step 45: timKiemTheoTen() ---
    public List<NhaGa> timKiemTheoTen(String tuKhoa) {
        if (con != null) {
            String sql = "SELECT maGa, tenNhaGa, diaChi, soDienThoai FROM NhaGa WHERE tenNhaGa LIKE ? ORDER BY maGa";
            List<NhaGa> list = new ArrayList<>();
            String keyword = tuKhoa == null ? "" : tuKhoa.trim();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + keyword + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new NhaGa(
                            rs.getString("maGa"),
                            rs.getString("tenNhaGa"),
                            rs.getString("diaChi"),
                            rs.getString("soDienThoai")
                        ));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        return NhaGa.timKiemTheoTen(store, tuKhoa);
    }

    // --- Sequence diagram step 34: taoGaMoi() ---
    public NhaGa taoGaMoi(String tenNhaGa, String diaChi, String soDienThoai) {
        if (con != null) {
            String maGa = "GA-" + System.currentTimeMillis();
            String sql = "INSERT INTO NhaGa (maGa, tenNhaGa, diaChi, soDienThoai) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maGa);
                ps.setString(2, tenNhaGa);
                ps.setString(3, diaChi);
                ps.setString(4, soDienThoai);
                if (ps.executeUpdate() > 0) {
                    return new NhaGa(maGa, tenNhaGa, diaChi, soDienThoai);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        NhaGa gaMoi = NhaGa.taoGaMoi(tenNhaGa, diaChi, soDienThoai);
        store.add(gaMoi);
        return gaMoi;
    }

    // --- Sequence diagram step 62: capNhatGa() ---
    public boolean capNhatGa(NhaGa ga, String tenNhaGa, String diaChi, String soDienThoai) {
        if (ga == null) {
            return false;
        }
        if (con != null) {
            String sql = "UPDATE NhaGa SET tenNhaGa = ?, diaChi = ?, soDienThoai = ? WHERE maGa = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tenNhaGa);
                ps.setString(2, diaChi);
                ps.setString(3, soDienThoai);
                ps.setString(4, ga.getMaGa());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        ga.capNhat(tenNhaGa, diaChi, soDienThoai);
        return true;
    }

    // --- Kiểm tra nhà ga đang được sử dụng ---
    public boolean kiemTraGaDangSuDung(String maGa) {
        if (con != null) {
            String sql = "SELECT 1 FROM NhaGa ng "
                       + "WHERE ng.maGa = ? AND ("
                       + "EXISTS (SELECT 1 FROM ChiTietHanhTrinh ctht WHERE ctht.nhaGaId = ng.id) "
                       + "OR EXISTS (SELECT 1 FROM ChiTietLichTrinh ctlt WHERE ctlt.nhaGaId = ng.id)"
                       + ")";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maGa);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // --- Sequence diagram step 70: xoaGa() ---
    public boolean xoaGa(NhaGa ga) {
        if (ga == null) {
            return false;
        }
        
        // KIỂM TRA RÀNG BUỘC CHỦ ĐỘNG TỪ HỆ THỐNG
        if (kiemTraGaDangSuDung(ga.getMaGa())) {
            System.err.println("Từ chối xóa: Nhà ga " + ga.getMaGa() + " đang nằm trong Hành Trình hoặc Lịch Trình.");
            return false; 
        }

        if (con != null) {
            String sql = "DELETE FROM NhaGa WHERE maGa = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ga.getMaGa());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return store.remove(ga);
    }

    // --- Legacy helper kept for HanhTrinh compatibility ---
    public List<NhaGa> layThongTinGaDauGaCuoi(String maGaDi, String maGaDen) {
        if (con != null) {
            String sql = "SELECT maGa, tenNhaGa, diaChi, soDienThoai FROM NhaGa WHERE maGa IN (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maGaDi);
                ps.setString(2, maGaDen);
                try (ResultSet rs = ps.executeQuery()) {
                    Map<String, NhaGa> map = new HashMap<>();
                    while (rs.next()) {
                        NhaGa ga = new NhaGa(
                            rs.getString("maGa"),
                            rs.getString("tenNhaGa"),
                            rs.getString("diaChi"),
                            rs.getString("soDienThoai")
                        );
                        map.put(ga.getMaGa(), ga);
                    }

                    List<NhaGa> ketQua = new ArrayList<>();
                    if (map.containsKey(maGaDi)) {
                        ketQua.add(map.get(maGaDi));
                    }
                    if (map.containsKey(maGaDen)) {
                        ketQua.add(map.get(maGaDen));
                    }
                    return ketQua;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        return NhaGa.layThongTinGaDauGaCuoi(maGaDi, maGaDen);
    }

}
