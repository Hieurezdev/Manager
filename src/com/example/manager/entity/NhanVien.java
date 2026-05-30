package com.example.manager.entity;

public class NhanVien extends TaiKhoan {
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;

    public NhanVien() {
        super();
    }

    public NhanVien(String maTaiKhoan, String tenDangNhap, String matKhau, String vaiTro, String trangThai,
                    String maNhanVien, String hoTen, String soDienThoai) {
        super(maTaiKhoan, tenDangNhap, matKhau, vaiTro, trangThai);
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

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
