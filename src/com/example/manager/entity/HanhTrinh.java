package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

public class HanhTrinh {
    private String maHanhTrinh;
    private String tenHanhTrinh;
    private double quangDuong;
    private List<ChiTietHanhTrinh> chiTietHanhTrinh;

    public HanhTrinh() {
        this.chiTietHanhTrinh = new ArrayList<>();
    }

    public HanhTrinh(String maHanhTrinh, String tenHanhTrinh, double quangDuong,
                     List<ChiTietHanhTrinh> chiTietHanhTrinh) {
        this.maHanhTrinh = maHanhTrinh;
        this.tenHanhTrinh = tenHanhTrinh;
        this.quangDuong = quangDuong;
        this.chiTietHanhTrinh = chiTietHanhTrinh == null ? new ArrayList<>() : new ArrayList<>(chiTietHanhTrinh);
    }

    public static HanhTrinh layThongTinHanhTrinh(String maHanhTrinh) {
        HanhTrinh hanhTrinh = new HanhTrinh();
        hanhTrinh.maHanhTrinh = maHanhTrinh;
        return hanhTrinh;
    }

    public List<ChiTietHanhTrinh> getDanhSachGaTrungGian() {
        // Truy xuất từ DAO
        com.example.manager.dao.HanhTrinhDAO dao = new com.example.manager.dao.HanhTrinhDAO(null);
        return dao.getGaTrungGian(this.maHanhTrinh);
    }

    public String getMaHanhTrinh() {
        return maHanhTrinh;
    }

    public String getTenHanhTrinh() {
        return tenHanhTrinh;
    }

    public double getQuangDuong() {
        return quangDuong;
    }

    public List<ChiTietHanhTrinh> getChiTietHanhTrinh() {
        return new ArrayList<>(chiTietHanhTrinh);
    }

    public NhaGa getGaDau() {
        ChiTietHanhTrinh dau = null;
        for (ChiTietHanhTrinh ct : chiTietHanhTrinh) {
            if (dau == null || ct.getThuTuGa() < dau.getThuTuGa()) {
                dau = ct;
            }
        }
        return dau == null ? null : dau.getNhaGa();
    }

    public NhaGa getGaCuoi() {
        ChiTietHanhTrinh cuoi = null;
        for (ChiTietHanhTrinh ct : chiTietHanhTrinh) {
            if (cuoi == null || ct.getThuTuGa() > cuoi.getThuTuGa()) {
                cuoi = ct;
            }
        }
        return cuoi == null ? null : cuoi.getNhaGa();
    }
}
