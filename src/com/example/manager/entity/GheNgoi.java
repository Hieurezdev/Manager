package com.example.manager.entity;

public class GheNgoi {
    private String maGhe;
    private String maToa;
    private String soGhe;
    private String viTri;
    private String trangThai;

    public GheNgoi() {}
    public GheNgoi(String maGhe, String soGhe, String viTri, String trangThai, String maToa) {
        this.maGhe = maGhe; this.soGhe = soGhe; this.viTri = viTri; this.trangThai = trangThai; this.maToa = maToa;
    }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }
    public String getMaToa() { return maToa; }
    public void setMaToa(String maToa) { this.maToa = maToa; }
    public String getSoGhe() { return soGhe; }
    public void setSoGhe(String soGhe) { this.soGhe = soGhe; }
    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}