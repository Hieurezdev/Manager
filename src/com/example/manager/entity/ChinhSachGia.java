package com.example.manager.entity;

public class ChinhSachGia {
    private String maChinhSach;
    private String tenLoaiDoiTuong;
    private double phanTramGiamGia;
    private String moTa;

    public ChinhSachGia() {}

    public String getMaChinhSach() { return maChinhSach; }
    public void setMaChinhSach(String maChinhSach) { this.maChinhSach = maChinhSach; }
    public String getTenLoaiDoiTuong() { return tenLoaiDoiTuong; }
    public void setTenLoaiDoiTuong(String tenLoaiDoiTuong) { this.tenLoaiDoiTuong = tenLoaiDoiTuong; }
    public double getPhanTramGiamGia() { return phanTramGiamGia; }
    public void setPhanTramGiamGia(double phanTramGiamGia) { this.phanTramGiamGia = phanTramGiamGia; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}