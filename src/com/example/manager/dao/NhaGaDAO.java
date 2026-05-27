package com.example.manager.dao;

import com.example.manager.entity.NhaGa;

import java.sql.Connection;
import java.util.List;

public class NhaGaDAO extends DAO {
    public NhaGaDAO(Connection con) {
        super(con);
    }

    public List<NhaGa> layThongTinGaDauGaCuoi(String maGaDi, String maGaDen) {
        return NhaGa.layThongTinGaDauGaCuoi(maGaDi, maGaDen);
    }
}
