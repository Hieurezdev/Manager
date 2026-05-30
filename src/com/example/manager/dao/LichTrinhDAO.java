package com.example.manager.dao;

import com.example.manager.entity.LichTrinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDAO extends DAO {
    public LichTrinhDAO() {
        super();
    }

    public LichTrinhDAO(Connection con) {
        super(con);
    }

    public List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        return new ArrayList<>();
    }

    public LichTrinh layThongTinLichTrinh(String maTau) {
        return new LichTrinh();
    }

    public boolean addLichTrinh(LichTrinh lichTrinh) {
        System.out.println("Mocking: Đã thêm Lịch Trình thành công vào hệ thống.");
        System.out.println("Mã Tàu: " + (lichTrinh.getDoanTau() != null ? lichTrinh.getDoanTau().getMaTau() : "null"));
        System.out.println("Mã Hành Trình: " + (lichTrinh.getHanhTrinh() != null ? lichTrinh.getHanhTrinh().getMaHanhTrinh() : "null"));
        System.out.println("Ngày khởi hành: " + lichTrinh.getNgayKhoiHanh());
        System.out.println("Số lượng chi tiết lịch trình: " + lichTrinh.getChiTietLichTrinh().size());
        return true;
    }
}
