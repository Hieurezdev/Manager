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

public class NhaGaDAO extends DAO {
    public NhaGaDAO(Connection con) {
        super(con);
    }

    public List<NhaGa> layThongTinGaDauGaCuoi(String maGaDi, String maGaDen) {
        if (con != null) {
            String sql = "SELECT maGa, tenNhaGa, soDienThoai FROM NhaGa WHERE maGa IN (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maGaDi);
                ps.setString(2, maGaDen);
                try (ResultSet rs = ps.executeQuery()) {
                    Map<String, NhaGa> map = new HashMap<>();
                    while (rs.next()) {
                        NhaGa ga = new NhaGa(
                            rs.getString("maGa"),
                            rs.getString("tenNhaGa"),
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
