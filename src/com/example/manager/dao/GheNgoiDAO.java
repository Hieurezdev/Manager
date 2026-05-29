package com.example.manager.dao;

import com.example.manager.enums.TrangThaiGhe;
import com.example.manager.entity.VeTau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GheNgoiDAO extends DAO {

    public GheNgoiDAO(Connection con) {
        super(con);
    }

    private String getDbTrangThaiGhe(TrangThaiGhe tt) {
        if (tt == TrangThaiGhe.TRONG) return "Trong";
        if (tt == TrangThaiGhe.DA_DAT) return "DaDat";
        return "Trong";
    }

    public boolean updateTrangThai(String maGhe, TrangThaiGhe trangThai) {
        if (con != null) {
            String sql = "UPDATE GheNgoi SET trangThai = ? WHERE maGhe = ?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, getDbTrangThaiGhe(trangThai));
                ps.setString(2, maGhe);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            for (VeTau v : VeTauDAO.mockVeTauList) {
                if (v.getGheNgoi() != null && maGhe.equals(v.getGheNgoi().getMaGhe())) {
                    v.getGheNgoi().setTrangThai(trangThai);
                }
            }
            return true;
        }
    }
}
