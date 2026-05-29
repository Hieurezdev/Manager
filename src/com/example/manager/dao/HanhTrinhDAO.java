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

    public java.util.List<HanhTrinh> getAllHanhTrinh() {
        return callGetAll();
    }

    private java.util.List<HanhTrinh> callGetAll() {
        // Thực thi truy vấn tuyến đường
        return new java.util.ArrayList<>();
    }

    public java.util.List<com.example.manager.entity.ChiTietHanhTrinh> getGaTrungGian(String maHanhTrinh) {
        return callGetGaTrungGian(maHanhTrinh);
    }

    private java.util.List<com.example.manager.entity.ChiTietHanhTrinh> callGetGaTrungGian(String maHanhTrinh) {
        // Thực thi truy vấn các ga thuộc tuyến
        // Gọi call chuyển dữ liệu sang thực thể ChiTietHanhTrinh
        java.util.List<com.example.manager.entity.ChiTietHanhTrinh> result = new java.util.ArrayList<>();
        result.add(new com.example.manager.entity.ChiTietHanhTrinh());
        return result;
    }
}
