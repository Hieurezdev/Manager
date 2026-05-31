package com.example.manager.entity;

public class QuanLy {
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private String trangThai;

    public QuanLy() {
    }

    public QuanLy(String tenDangNhap, String matKhau, String vaiTro, String trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public String getTrangThai() {
        return trangThai;
    }
}
