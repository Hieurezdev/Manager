package com.example.manager.entity;

public class DoanTau {
    private String maTau;
    private String tenDoanTau;
    private String loaiTau;
    private String trangThai;

    public DoanTau() {}

    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }
    public String getTenDoanTau() { return tenDoanTau; }
    public void setTenDoanTau(String tenDoanTau) { this.tenDoanTau = tenDoanTau; }
    public String getLoaiTau() { return loaiTau; }
    public void setLoaiTau(String loaiTau) { this.loaiTau = loaiTau; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}