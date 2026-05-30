package com.example.manager.entity;

public class CampBill {
    private String maHoaDon;
    private String hoTen;
    private String cccd;
    private String chuyenTau;
    private String viTri;
    private double tongTien;

    public CampBill() {}

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String m) { this.maHoaDon = m; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String h) { this.hoTen = h; }
    public String getCccd() { return cccd; }
    public void setCccd(String c) { this.cccd = c; }
    public String getChuyenTau() { return chuyenTau; }
    public void setChuyenTau(String c) { this.chuyenTau = c; }
    public String getViTri() { return viTri; }
    public void setViTri(String v) { this.viTri = v; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double t) { this.tongTien = t; }
}