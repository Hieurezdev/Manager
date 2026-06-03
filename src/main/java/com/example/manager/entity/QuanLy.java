package com.example.manager.entity;

public class QuanLy extends TaiKhoan {
    private String maQuanLy;

    public QuanLy() {
        super();
    }

    public QuanLy(String maTaiKhoan, String tenDangNhap, String matKhau,
                  String hoTen, String vaiTro, String trangThai, String maQuanLy) {
        super(maTaiKhoan, tenDangNhap, matKhau, hoTen, vaiTro, trangThai);
        this.maQuanLy = maQuanLy;
    }

    public String getMaQuanLy() { return maQuanLy; }
    public void setMaQuanLy(String maQuanLy) { this.maQuanLy = maQuanLy; }
}
