package com.example.manager.entity;

import com.example.manager.enums.TrangThaiGhe;

public class GheNgoi {
    private String maGhe;
    private int soGhe;
    private String viTri;
    private TrangThaiGhe trangThai;
    private String moTa;

    public GheNgoi() {
    }

    public String getMaGhe() {
        return maGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public String getViTri() {
        return viTri;
    }

    public TrangThaiGhe getTrangThai() {
        return trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public void setTrangThai(TrangThaiGhe trangThai) {
        this.trangThai = trangThai;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
