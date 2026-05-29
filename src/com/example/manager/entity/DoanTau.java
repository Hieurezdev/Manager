package com.example.manager.entity;

import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.TrangThaiTau;
import com.example.manager.dao.DoanTauDAO;

import java.util.ArrayList;
import java.util.List;

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
        // Gọi DAO để truy xuất danh sách tàu từ CSDL
        DoanTauDAO dao = new DoanTauDAO(null); // Sử dụng null do chưa có Connection thực tế
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
}
