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
}
