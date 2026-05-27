package com.example.manager.dao;

import com.example.manager.entity.HanhTrinh;

import java.sql.Connection;

public class HanhTrinhDAO extends DAO {
    public HanhTrinhDAO(Connection con) {
        super(con);
    }

    public HanhTrinh layThongTinHanhTrinh(String maHanhTrinh) {
        return HanhTrinh.layThongTinHanhTrinh(maHanhTrinh);
    }
}
