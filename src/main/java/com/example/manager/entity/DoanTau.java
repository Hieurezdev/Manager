package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.manager.dao.DoanTauDAO;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.TrangThaiTau;

public class DoanTau {

    private String maTau;
    private String tenTau;
    private LoaiTau loaiTau;
    private TrangThaiTau trangThai;
    private List<ToaTau> toaTau;

    public DoanTau() {
        this.toaTau = new ArrayList<>();
    }

    public DoanTau(String maTau, String tenTau, LoaiTau loaiTau, TrangThaiTau trangThai, List<ToaTau> toaTau) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.loaiTau = loaiTau;
        this.trangThai = trangThai;
        this.toaTau = toaTau == null ? new ArrayList<>() : new ArrayList<>(toaTau);
    }

    public static List<DoanTau> layDanhSachDoanTau() {
        return new ArrayList<>();
    }

    public static List<DoanTau> getDanhSachTau() {
        DoanTauDAO dao = new DoanTauDAO(com.example.manager.dao.DBConnection.getConnection());
        return dao.getAllDoanTau();
    }

    public String getMaTau() {
        return maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public LoaiTau getLoaiTau() {
        return loaiTau;
    }

    public TrangThaiTau getTrangThai() {
        return trangThai;
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

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

    public void setLoaiTau(LoaiTau loaiTau) {
        this.loaiTau = loaiTau;
    }

    public void setTrangThai(TrangThaiTau trangThai) {
        this.trangThai = trangThai;
    }
}
