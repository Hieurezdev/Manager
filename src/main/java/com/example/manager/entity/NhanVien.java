package com.example.manager.entity;

public class NhanVien extends TaiKhoan {
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;

    // === Constructor mặc định từ GitHub ===
    public NhanVien() {
        super();
    }

    // === Constructor đầy đủ tham số từ GitHub (Quản lý cả Tài Khoản) ===
    public NhanVien(String maTaiKhoan, String tenDangNhap, String matKhau, String vaiTro, String trangThai,
                    String maNhanVien, String hoTen, String soDienThoai) {
        super(maTaiKhoan, tenDangNhap, matKhau, vaiTro, trangThai);
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

    // =========================================================================
    // BỔ SUNG LẠI: Constructor 2 tham số cũ của ông (Né lỗi compile luồng cũ của ông)
    // =========================================================================
    public NhanVien(String maNhanVien, String hoTen) {
        super(); // Gọi constructor mặc định của TaiKhoan
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
    }

    // =========================================================================
    // =========================================================================
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}