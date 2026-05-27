package com.example.manager.entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soCCCD;
    private String soDienThoai;
    private String email;

    public KhachHang() {
    }

    public KhachHang(String maKH, String hoTen, String soCCCD, String soDienThoai, String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soCCCD = soCCCD;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getEmail() {
        return email;
    }
}
