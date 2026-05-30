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
        List<DoanTau> result = new ArrayList<>();
        result.add(new DoanTau("SE1", "Tàu Thống Nhất SE1", com.example.manager.enums.LoaiTau.THONG_NHAT, com.example.manager.enums.TrangThaiTau.HOAT_DONG, new ArrayList<>()));
        result.add(new DoanTau("SE2", "Tàu Thống Nhất SE2", com.example.manager.enums.LoaiTau.THONG_NHAT, com.example.manager.enums.TrangThaiTau.HOAT_DONG, new ArrayList<>()));
        result.add(new DoanTau("TN1", "Tàu Địa Phương TN1", com.example.manager.enums.LoaiTau.DIA_PHUONG, com.example.manager.enums.TrangThaiTau.HOAT_DONG, new ArrayList<>()));
        return result;
    }
}
