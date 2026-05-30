package com.example.manager;

import com.example.manager.dao.DBConnection;
import com.example.manager.dao.LichTrinhDAO;
import java.sql.Connection;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== KICH HOAT KIEM THU TU DONG KET NOI MY_SQL ===");
        try (Connection con = DBConnection.getConnection()) {
            if (con != null) {
                LichTrinhDAO dao = new LichTrinhDAO(con);
                dao.layDanhSachChuyenTauPhuHop("HN", "DN", "2026-05-15");
                System.out.println("[PASS] Kiem thu ket noi lay quang duong tu MySQL thanh cong!");
            } else {
                System.err.println("[FAIL] Doi tuong Connection bi null. Vui long kiem tra lai phan mem MySQL Server!");
            }
        } catch (Exception e) {
            System.err.println("[FAIL] Chuoi xac thuc kiem thu bi loi.");
            e.printStackTrace();
        }
    }
}