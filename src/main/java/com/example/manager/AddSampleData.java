package com.example.manager;

import com.example.manager.dao.DBConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class AddSampleData {
    public static void main(String[] args) {
        System.out.println("=== DANG KET NOI CSDL MYSQL DE THEM DU LIEU TU DATA.SQL ===");
        Connection con = DBConnection.getConnection();
        if (con == null) {
            System.err.println("❌ KHONG THE KET NOI CSDL MYSQL!");
            System.err.println("Vui long kiem tra lai thong so cau hinh (DB_URL, USER, PASSWORD) trong file:");
            System.err.println("-> src/com/example/manager/dao/DBConnection.java");
            return;
        }

        String sqlFilePath = "src\\main\\java\\com\\example\\manager\\data.sql";
        System.out.println("-> Dang doc file: " + sqlFilePath);

        try (Statement stmt = con.createStatement()) {
            // Thiet lap ANSI_QUOTES de MySQL xu ly dau nhay kep \" giong nhu PostgreSQL/ANSI SQL
            stmt.execute("SET SESSION sql_mode = 'ANSI_QUOTES'");
            
            con.setAutoCommit(false); // Bat dau Transaction de bao dam toan ven du lieu

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(sqlFilePath, java.nio.charset.StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Loai bo comments dang --
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }
            }

            // Phan tach cac cau lenh SQL bang dau cham phay ;
            String[] queries = sb.toString().split(";");
            int count = 0;
            System.out.println("-> Dang thuc thi cac cau lenh SQL vao MySQL...");
            for (String query : queries) {
                String trimmedQuery = query.trim();
                if (!trimmedQuery.isEmpty()) {
                    stmt.execute(trimmedQuery);
                    count++;
                }
            }

            con.commit(); // Hoan tat transaction
            System.out.println("🎉 KET QUA: Da nap thanh cong " + count + " cau lenh SQL tu file data.sql vao CSDL MySQL!");
        } catch (IOException e) {
            System.err.println("❌ LOI DOC FILE SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback neu xay ra bat ky loi nao
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ LOI SQL KHI THUC THI SCRIPT: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
