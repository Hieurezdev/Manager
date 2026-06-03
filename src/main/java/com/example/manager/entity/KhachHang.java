package com.example.manager.entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soCCCD;
    private String soDienThoai;
    private String email;

    // === Constructor mặc định===
    public KhachHang() {
    }

    // === Constructor đầy đủ tham số ===
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

    public void setMaKH(String maKH) { 
        this.maKH = maKH; 
    }

    public void setHoTen(String hoTen) { 
        this.hoTen = hoTen; 
    }

    public void setSoCCCD(String soCCCD) { 
        this.soCCCD = soCCCD; 
    }

    public void setSoDienThoai(String soDienThoai) { 
        this.soDienThoai = soDienThoai; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }
}