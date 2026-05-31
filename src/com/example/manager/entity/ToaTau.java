package com.example.manager.entity;

import com.example.manager.enums.LoaiToa;
import java.util.ArrayList;
import java.util.List;

public class ToaTau {

    // =========================================================================
    // CÁC THUỘC TÍNH GỐC TỪ GITHUB (Giữ nguyên vẹn 100%)
    // =========================================================================
    private String maToa;
    private String tenToa;
    private int soThuTu;
    private LoaiToa loaiToa;
    private int soLuongGheToiDa;
    private String moTa;
    private List<GheNgoi> gheNgoi;

    // === BỔ SUNG: Trường maTau phục vụ luồng xử lý định vị sơ đồ của ông Đạt ===
    private String maTau;

    // === Constructor mặc định từ GitHub ===
    public ToaTau() {
        this.gheNgoi = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số từ GitHub ===
    public ToaTau(String maToa, String tenToa, int soThuTu, LoaiToa loaiToa, int soLuongGheToiDa, String moTa) {
        this.maToa = maToa;
        this.tenToa = tenToa;
        this.soThuTu = soThuTu;
        this.loaiToa = loaiToa;
        this.soLuongGheToiDa = soLuongGheToiDa;
        this.moTa = moTa;
        this.gheNgoi = new ArrayList<>();
    }

    // === Thêm bộ Getter/Setter cho thuộc tính bổ sung của ông Đạt ===
    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    // =========================================================================
    // TOÀN BỘ GETTER GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để né Conflict)
    // =========================================================================
    public String getMaToa() {
        return maToa;
    }

    public String getTenToa() {
        return tenToa;
    }

    public int getSoThuTu() {
        return soThuTu;
    }

    public LoaiToa getLoaiToa() {
        return loaiToa;
    }

    public int getSoLuongGheToiDa() {
        return soLuongGheToiDa;
    }

    public String getMoTa() {
        return moTa;
    }

    public List<GheNgoi> getGheNgoi() {
        return new ArrayList<>(gheNgoi);
    }

    // =========================================================================
    // CÁC HÀM SETTER BỔ SUNG (Phục vụ gán dữ liệu thực tế cho module Mua Vé)
    // =========================================================================
    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    public void setTenToa(String tenToa) {
        this.tenToa = tenToa;
    }

    public void setSoThuTu(int soThuTu) {
        this.soThuTu = soThuTu;
    }

    public void setLoaiToa(LoaiToa loaiToa) {
        this.loaiToa = loaiToa;
    }

    public void setSoLuongGheToiDa(int soLuongGheToiDa) {
        this.soLuongGheToiDa = soLuongGheToiDa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setGheNgoi(List<GheNgoi> gheNgoi) {
        this.gheNgoi = gheNgoi;
    }
}
