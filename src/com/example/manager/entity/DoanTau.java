package com.example.manager.entity;

import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.TrangThaiTau;

import java.util.ArrayList;
import java.util.List;

public class DoanTau {
    private String maTau;
    private String tenTau; // Đồng bộ tên thuộc tính theo bản GitHub
    private LoaiTau loaiTau; // Đồng bộ sang Enum theo bản GitHub
    private TrangThaiTau trangThai; // Đồng bộ sang Enum theo bản GitHub
    private List<ToaTau> toaTau; // Giữ lại danh sách quan hệ N-1 của bạn ông

    // === Constructor mặc định gốc từ GitHub ===
    public DoanTau() {
        this.toaTau = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số gốc từ GitHub ===
    public DoanTau(String maTau, String tenTau, LoaiTau loaiTau, TrangThaiTau trangThai, List<ToaTau> toaTau) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.loaiTau = loaiTau;
        this.trangThai = trangThai;
        this.toaTau = toaTau == null ? new ArrayList<>() : new ArrayList<>(toaTau);
    }

    // === Hàm static hỗ trợ thống kê gốc từ GitHub ===
    public static List<DoanTau> layDanhSachDoanTau() {
        return new ArrayList<>();
    }

    // === Danh sách các hàm Getter và Setter giữ nguyên vẹn 100% bản GitHub ===
    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

    public LoaiTau getLoaiTau() {
        return loaiTau;
    }

    public void setLoaiTau(LoaiTau loaiTau) {
        this.loaiTau = loaiTau;
    }

    public TrangThaiTau getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiTau trangThai) {
        this.trangThai = trangThai;
    }

    public List<ToaTau> getToaTau() {
        return new ArrayList<>(toaTau);
    }

    public void addToaTau(ToaTau toa) {
        if (toaTau == null) {
            toaTau = new ArrayList<>();
        }
        toaTau.add(toa);
    }
}