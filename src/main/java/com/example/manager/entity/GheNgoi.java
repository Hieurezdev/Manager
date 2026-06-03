package com.example.manager.entity;

import com.example.manager.enums.TrangThaiGhe;

public class GheNgoi {

    private String maGhe;
    private int soGhe;
    private String viTri;
    private TrangThaiGhe trangThai;
    private String moTa;
    private String maToa;

    public GheNgoi() {
    }

    public GheNgoi(String maGhe, int soGhe, String viTri, TrangThaiGhe trangThai, String maToa) {
        this.maGhe = maGhe;
        this.soGhe = soGhe;
        this.viTri = viTri;
        this.trangThai = trangThai;
        this.maToa = maToa;
    }

    public GheNgoi(String maGhe, String soGhe, String viTri, String trangThaiStr, String maToa) {
        this.maGhe = maGhe;
        try {
            this.soGhe = Integer.parseInt(soGhe); 
        } catch (Exception e) {
            this.soGhe = 0;
        }
        this.viTri = viTri;
        this.maToa = maToa;

        // Tự động chuyển đổi chuỗi String từ DB sang Enum TrangThaiGhe
        try {
            this.trangThai = TrangThaiGhe.valueOf(trangThaiStr.toUpperCase());
        } catch (Exception e) {
            this.trangThai = TrangThaiGhe.TRONG;
        }
    }

    // === Getter/Setter cho thuộc tính maToa bổ sung ===
    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
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
