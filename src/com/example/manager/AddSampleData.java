package com.example.manager;

import com.example.manager.dao.DBConnection;
import java.sql.Connection;
import java.sql.Statement;

public class AddSampleData {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement()) {
            System.out.println("Dang nap du lieu kiem thu toan dien vao MySQL...");
            
            st.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            st.executeUpdate("DELETE FROM VeTau;");
            st.executeUpdate("DELETE FROM HoaDon;");
            st.executeUpdate("DELETE FROM GheNgoi;");
            st.executeUpdate("DELETE FROM ToaTau;");
            st.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");

            st.executeUpdate("INSERT INTO DoanTau VALUES('SE3', 'Tau Hoa Chat Luong Cao', 'Tau nhanh', 'San sang') ON DUPLICATE KEY UPDATE trangThai='San sang';");
            
            // ĐÃ SỬA: Sinh đầy đủ cấu trúc vật lý cho cả 4 TOA
            for (int t = 1; t <= 44; t++) { // Vòng lặp đẻ 4 toa
                st.executeUpdate("INSERT INTO ToaTau VALUES('TOA" + t + "', 'Toa " + t + "', 'SE3', " + t + ", 'Nam', 44);");
                
                // Với mỗi toa, sinh ra đúng 44 cái ghế tương ứng
                for (int i = 1; i <= 44; i++) {
                    // maGhe phân tách rõ ràng theo Toa để không bị trùng khóa chính (Ví dụ: T1-G15, T4-G11)
                    st.executeUpdate("INSERT INTO GheNgoi (maGhe, maToa, soGhe, viTri, trangThai) VALUES ('T" + t + "-G" + i + "', 'TOA" + t + "', '" + i + "', 'Cua so', 'TRONG')");
                }
            }
            System.out.println("Nap du lieu kiem thu thanh cong! Da phu kin 44 ghe cho ca 4 Toa.");
        } catch (Exception e) {
            System.err.println("Loi nap du lieu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}