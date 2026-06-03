package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.manager.enums.LoaiToa;

public class ToaTau {

    private String maToa;
    private String tenToa;
    private int soThuTu;
    private LoaiToa loaiToa;
    private int soLuongGheToiDa;
    private String moTa;
    private List<GheNgoi> gheNgoi;

    private String maTau;

    // === Constructor mặc định ===
    public ToaTau() {
        this.gheNgoi = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số ===
    public ToaTau(String maToa, String tenToa, int soThuTu, LoaiToa loaiToa, int soLuongGheToiDa, String moTa) {
        this.maToa = maToa;
        this.tenToa = tenToa;
        this.soThuTu = soThuTu;
        this.loaiToa = loaiToa;
        this.soLuongGheToiDa = soLuongGheToiDa;
        this.moTa = moTa;
        this.gheNgoi = new ArrayList<>();
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    
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
