package com.example.manager;

import com.example.manager.dao.DBConnection;
import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;
import java.sql.Connection;

public class TestXoaGa {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            System.err.println("Kết nối database thất bại!");
            return;
        }

        NhaGaDAO dao = new NhaGaDAO(con);
        
        // Tạo đối tượng NhaGa đại diện cho Ga Hà Nội
        NhaGa ga = new NhaGa("GA_HANOI", "Ga Hà Nội", "120 Lê Duẩn", "0243");
        
        System.out.println("Thử xóa nhà ga: " + ga.getMaGa());
        boolean ketQua = dao.xoaGa(ga);
        
        if (ketQua) {
            System.out.println("Xóa thành công (Lỗi: Lẽ ra không được xóa!)");
        } else {
            System.out.println("Xóa thất bại (Đúng như kỳ vọng vì nhà ga đang nằm trong hành trình).");
        }
    }
}
