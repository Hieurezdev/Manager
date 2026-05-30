package com.example.manager.dao;

import com.example.manager.entity.NhaGa;

import java.sql.Connection;
import java.util.ArrayList;
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
        seedDemoData();
    }

    // --- Sequence diagram step 20: layDanhSachGa() ---
    public List<NhaGa> layDanhSachGa() {
        return NhaGa.layDanhSach(store);
    }

    // --- Sequence diagram step 45: timKiemTheoTen() ---
    public List<NhaGa> timKiemTheoTen(String tuKhoa) {
        return NhaGa.timKiemTheoTen(store, tuKhoa);
    }

    // --- Sequence diagram step 34: taoGaMoi() ---
    public NhaGa taoGaMoi(String tenNhaGa, String diaChi, String soDienThoai) {
        NhaGa gaMoi = NhaGa.taoGaMoi(tenNhaGa, diaChi, soDienThoai);
        store.add(gaMoi);
        return gaMoi;
    }

    // --- Sequence diagram step 62: capNhatGa() ---
    public boolean capNhatGa(NhaGa ga, String tenNhaGa, String diaChi, String soDienThoai) {
        ga.capNhat(tenNhaGa, diaChi, soDienThoai);
        return true;
    }

    // --- Sequence diagram step 70: xoaGa() ---
    public boolean xoaGa(NhaGa ga) {
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

    private void seedDemoData() {
        store.add(new NhaGa("GA-HN", "Hà Nội", "Số 120 Lê Duẩn, Đống Đa, Hà Nội", "024-38253340"));
        store.add(new NhaGa("GA-SG", "Sài Gòn", "Số 1 Nguyễn Thông, Quận 3, TP.HCM", "028-38436528"));
        store.add(new NhaGa("GA-DN", "Đà Nẵng", "Số 202 Hải Phòng, Thanh Khê, Đà Nẵng", "0236-3823810"));
    }
}
