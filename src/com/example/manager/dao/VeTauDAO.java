package com.example.manager.dao;

import com.example.manager.entity.VeTau;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class VeTauDAO extends DAO {
    public VeTauDAO(Connection con) {
        super(con);
    }

    public List<VeTau> layDanhSachVeTheoLichTrinh(String maLichTrinh) {
        return new ArrayList<>();
    }
}
