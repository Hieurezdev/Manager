package com.example.manager.dao;
import com.example.manager.entity.QuanLy;
import java.sql.Connection;

/**
 * QuanLyDAO — verifies manager credentials.
 * checkDangNhap delegates to QuanLy entity logic (UML sequence diagram steps 6-8).
 */
public class QuanLyDAO extends DAO {

    public QuanLyDAO(Connection con) {
        super(con);
    }

    /**
     * Sequence diagram step 7: checkDangNhap(quanly: QuanLy) : boolean
     * Validates that the given QuanLy object has non-blank credentials.
     * A real implementation would query the DB here.
     */
    public boolean checkDangNhap(QuanLy quanLy) {
        if (quanLy == null) {
            return false;
        }
        String ten = quanLy.getTenDangNhap();
        String mk = quanLy.getMatKhau();
        return ten != null && !ten.isBlank() && mk != null && !mk.isBlank();
    }
}
