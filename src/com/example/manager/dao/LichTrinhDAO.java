package com.example.manager.dao;

import com.example.manager.entity.LichTrinh;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDAO extends DAO {
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
        // Thực thi lưu trữ thành công và trả về giá trị true
        return true;
    }
}
