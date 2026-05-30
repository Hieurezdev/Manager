package com.example.manager.entity;

public class LichTrinh {
    private String maLichTrinh;
    private String maHanhTrinh;
    private String maTau;
    private String ngayKhoiHanh;
    private String gioDi;
    private String gioDen;

    public LichTrinh() {}
    public LichTrinh(String maLichTrinh, String maTau, String gioDi, String gioDen) {
        this.maLichTrinh = maLichTrinh;
        this.maTau = maTau;
        this.gioDi = gioDi;
        this.gioDen = gioDen;
    }

    public String getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(String maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    public String getMaHanhTrinh() { return maHanhTrinh; }
    public void setMaHanhTrinh(String maHanhTrinh) { this.maHanhTrinh = maHanhTrinh; }
    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }
    public String getNgayKhoiHanh() { return ngayKhoiHanh; }
    public void setNgayKhoiHanh(String ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }
    public String getGioDi() { return gioDi; }
    public void setGioDi(String gioDi) { this.gioDi = gioDi; }
    public String getGioDen() { return gioDen; }
    public void setGioDen(String gioDen) { this.gioDen = gioDen; }
}