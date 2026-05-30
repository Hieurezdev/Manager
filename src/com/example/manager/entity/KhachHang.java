package com.example.manager.entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soCCCD;
    private String soDienThoai;
    private String email;

    public KhachHang() {}
    public KhachHang(String maKH, String hoTen, String soCCCD, String soDienThoai, String email) {
        this.maKH = maKH; this.hoTen = hoTen; this.soCCCD = soCCCD; this.soDienThoai = soDienThoai; this.email = email;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoCCCD() { return soCCCD; }
    public void setSoCCCD(String soCCCD) { this.soCCCD = soCCCD; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}