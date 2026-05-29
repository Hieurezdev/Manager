package com.example.manager.dao;

import com.example.manager.entity.DoanTau;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DoanTauDAO extends DAO {
    public DoanTauDAO(Connection con) {
        super(con);
    }

    public List<DoanTau> layDanhSachDoanTau() {
        return new ArrayList<>();
    }

    public List<DoanTau> getAllDoanTau() {
        // Tự gọi phương thức call nội bộ
        return call();
    }

    private List<DoanTau> call() {
        // Thực thi câu lệnh SQL lấy danh sách tàu
        // Gọi hàm khởi tạo DoanTau() của thực thể DoanTau để đóng gói dữ liệu
        List<DoanTau> result = new ArrayList<>();
        result.add(new DoanTau()); // Minh họa đóng gói
        return result;
    }
}
