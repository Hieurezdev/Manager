package com.example.manager.entity;

public class NhanVien extends TaiKhoan {
    private String maNhanVien;
    private String soDienThoai;

    // === Constructor mặc định từ GitHub ===
    public NhanVien() {
        super();
    }

    // === Constructor đầy đủ tham số từ GitHub (Quản lý cả Tài Khoản) ===
    public NhanVien(String maTaiKhoan, String tenDangNhap, String matKhau, String hoTen, String vaiTro, String trangThai,
                    String maNhanVien, String soDienThoai) {
        super(maTaiKhoan, tenDangNhap, matKhau, hoTen, vaiTro, trangThai);
        this.maNhanVien = maNhanVien;
        this.soDienThoai = soDienThoai;
    }
    
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}