package com.example.manager.dao;

import com.example.manager.entity.QuanLy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * QuanLyDAO — verifies manager credentials. checkDangNhap delegates to QuanLy
 * entity logic (UML sequence diagram steps 6-8).
 */
public class QuanLyDAO extends DAO {

    public QuanLyDAO(Connection con) {
        super(con);
    }

    public QuanLyDAO() {
        super();
    }

    /**
     * Sequence diagram step 7: checkDangNhap(quanly: QuanLy) : boolean
     * Validates that the given QuanLy object has non-blank credentials. A real
     * implementation would query the DB here.
     */
    public boolean checkDangNhap(QuanLy quanLy) {
        if (quanLy == null) {
            return false;
        }
        String ten = quanLy.getTenDangNhap();
        String mk = quanLy.getMatKhau();
        if (ten == null || ten.isBlank() || mk == null || mk.isBlank()) {
            return false;
        }

        if (con != null) {
            String sql = "SELECT 1 FROM TaiKhoan tk "
                    + "JOIN QuanLy ql ON ql.id = tk.id "
                    + "WHERE tk.tenDangNhap = ? AND tk.matKhau = ? "
                    + "AND tk.vaiTro = 'QuanLy' AND tk.trangThai = 'HoatDong' "
                    + "LIMIT 1";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ten);
                ps.setString(2, mk);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * Trả về vai trò ("QuanLy" hoặc "NhanVien") nếu đăng nhập thành công, ngược
     * lại trả về null.
     */
    public String layVaiTroDangNhap(String ten, String mk) {
        if (ten == null || ten.isBlank() || mk == null || mk.isBlank() || con == null) {
            return null;
        }

        String sql = "SELECT vaiTro FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ? AND trangThai = 'HoatDong'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setString(2, mk);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("vaiTro"); // Trả về giá trị trong cột vaiTro (VD: "QuanLy" hoặc "NhanVien")
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
