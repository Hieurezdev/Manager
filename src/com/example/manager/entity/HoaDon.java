package com.example.manager.entity;

public class HoaDon {
    private String maHoaDon;
    private String loaiHoaDon;
    private String ngayGioLap;
    private String maNhanVienLap;
    private String maKhachHang;
    private double tongTien;
    private String phuongThucThanhToan;
    private String trangThai;

    public HoaDon() {}

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String m) { this.maHoaDon = m; }
    public String getLoaiHoaDon() { return loaiHoaDon; }
    public void setLoaiHoaDon(String l) { this.loaiHoaDon = l; }
    public String getNgayGioLap() { return ngayGioLap; }
    public void setNgayGioLap(String n) { this.ngayGioLap = n; }
    public String getMaNhanVienLap() { return maNhanVienLap; }
    public void setMaNhanVienLap(String m) { this.maNhanVienLap = m; }
    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String m) { this.maKhachHang = m; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double t) { this.tongTien = t; }
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String p) { this.phuongThucThanhToan = p; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String t) { this.trangThai = t; }
}