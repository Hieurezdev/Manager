package com.example.manager.entity;

import com.example.manager.enums.LoaiToa;

import java.util.ArrayList;
import java.util.List;

public class ToaTau {
    private String maToa;
    private String tenToa;
    private int soThuTu;
    private LoaiToa loaiToa;
    private int soLuongGheToiDa;
    private String moTa;
    private List<GheNgoi> gheNgoi;

    public ToaTau() {
        this.gheNgoi = new ArrayList<>();
    }

    public ToaTau(String maToa, String tenToa, int soThuTu, LoaiToa loaiToa, int soLuongGheToiDa, String moTa) {
        this.maToa = maToa;
        this.tenToa = tenToa;
        this.soThuTu = soThuTu;
        this.loaiToa = loaiToa;
        this.soLuongGheToiDa = soLuongGheToiDa;
        this.moTa = moTa;
        this.gheNgoi = new ArrayList<>();
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
}
