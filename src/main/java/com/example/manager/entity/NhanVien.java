package com.example.manager.entity;

public class NhanVien extends TaiKhoan {
    private String maNhanVien;
    private String soDienThoai;

    // === Constructor mặc định ===
    public NhanVien() {
        super();
    }

    // === Constructor đầy đủ tham số ===
    public NhanVien(String maTaiKhoan, String tenDangNhap, String matKhau, String vaiTro, String trangThai,
                    String maNhanVien, String hoTen, String soDienThoai) {
        super(maTaiKhoan, tenDangNhap, matKhau, hoTen, vaiTro, trangThai);
        this.maNhanVien = maNhanVien;
        this.soDienThoai = soDienThoai;
    }

    
    public NhanVien(String maNhanVien, String hoTen) {
        super(); // Gọi constructor mặc định của TaiKhoan
        this.maNhanVien = maNhanVien;
        this.setHoTen(hoTen);
    }

    
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}