package com.example.manager.entity;

public class HanhTrinh {
    private String maHanhTrinh;
    private String maGaDi;
    private String maGaDen;
    private double quangDuong;

    public HanhTrinh() {}

    public String getMaHanhTrinh() { return maHanhTrinh; }
    public void setMaHanhTrinh(String maHanhTrinh) { this.maHanhTrinh = maHanhTrinh; }
    public String getMaGaDi() { return maGaDi; }
    public void setMaGaDi(String maGaDi) { this.maGaDi = maGaDi; }
    public String getMaGaDen() { return maGaDen; }
    public void setMaGaDen(String maGaDen) { this.maGaDen = maGaDen; }
    public double getQuangDuong() { return quangDuong; }
    public void setQuangDuong(double quangDuong) { this.quangDuong = quangDuong; }
}