package com.example.manager.entity;

import com.example.manager.enums.TrangThaiGhe;

public class GheNgoi {

    private String maGhe;
    private int soGhe;
    private String viTri;
    private TrangThaiGhe trangThai;
    private String moTa;
    // Bổ sung maToa để phục vụ luồng xử lý định vị sơ đồ ghế của ông Đạt
    private String maToa;

    // === Constructor mặc định gốc từ GitHub ===
    public GheNgoi() {
    }

    // === Constructor 1: Chuẩn Enum gốc của bạn ông trên GitHub ===
    public GheNgoi(String maGhe, int soGhe, String viTri, TrangThaiGhe trangThai, String maToa) {
        this.maGhe = maGhe;
        this.soGhe = soGhe;
        this.viTri = viTri;
        this.trangThai = trangThai;
        this.maToa = maToa;
    }

    // === Constructor 2: Nạp chồng nhận String (Cứu cánh các đoạn gọi hàm JDBC cũ của ông Đạt không bị Compile Error) ===
    public GheNgoi(String maGhe, String soGhe, String viTri, String trangThaiStr, String maToa) {
        this.maGhe = maGhe;
        try {
            this.soGhe = Integer.parseInt(soGhe); // Tự động ép String sang int
        } catch (Exception e) {
            this.soGhe = 0;
        }
        this.viTri = viTri;
        this.maToa = maToa;

        // Tự động chuyển đổi chuỗi String từ DB sang Enum TrangThaiGhe chuẩn của nhóm
        try {
            this.trangThai = TrangThaiGhe.valueOf(trangThaiStr.toUpperCase());
        } catch (Exception e) {
            this.trangThai = TrangThaiGhe.TRONG; // Mặc định nếu lỗi là ghế trống
        }
    }

    // === Getter/Setter cho thuộc tính maToa bổ sung ===
    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    // =========================================================================
    // TOÀN BỘ GETTER/SETTER GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để diệt tận gốc Conflict)
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
