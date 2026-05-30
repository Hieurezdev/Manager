package com.example.manager.entity;

import com.example.manager.enums.TrangThaiGhe;

public class GheNgoi {
    private String maGhe;
    private int soGhe; // Giữ nguyên kiểu int của bản GitHub
    private String viTri;
    private TrangThaiGhe trangThai; // Giữ nguyên Enum của bản GitHub
    private String moTa;
    private String maToa; // Bổ sung lại maToa để phục vụ chức năng Mua Vé của ông

    // === Constructor mặc định gốc từ GitHub ===
    public GheNgoi() {
    }

    // === Constructor đầy đủ tham số (Đã đồng bộ kiểu dữ liệu int, Enum và giữ lại maToa) ===
    public GheNgoi(String maGhe, int soGhe, String viTri, TrangThaiGhe trangThai, String maToa) {
        this.maGhe = maGhe;
        this.soGhe = soGhe;
        this.viTri = viTri;
        this.trangThai = trangThai;
        this.maToa = maToa;
    }

    // === Thêm các Getter/Setter cho thuộc tính maToa của ông ===
    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    // =========================================================================
    // TOÀN BỘ GETTER/SETTER GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để né Conflict)
    // =========================================================================
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