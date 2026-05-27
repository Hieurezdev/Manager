package com.example.manager.entity;

public class ChiTietBaoCao {
    private String maCTBC;
    private int soVeBan;
    private int doanhThuChuyen;
    private double tiLeLapDay;
    private LichTrinh lichTrinh;

    public ChiTietBaoCao() {
    }

    public ChiTietBaoCao(String maCTBC, int soVeBan, int doanhThuChuyen, double tiLeLapDay, LichTrinh lichTrinh) {
        this.maCTBC = maCTBC;
        this.soVeBan = soVeBan;
        this.doanhThuChuyen = doanhThuChuyen;
        this.tiLeLapDay = tiLeLapDay;
        this.lichTrinh = lichTrinh;
    }

    public String getMaCTBC() {
        return maCTBC;
    }

    public int getSoVeBan() {
        return soVeBan;
    }

    public int getDoanhThuChuyen() {
        return doanhThuChuyen;
    }

    public double getTiLeLapDay() {
        return tiLeLapDay;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }
}
