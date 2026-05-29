package com.example.manager.entity;

import com.example.manager.enums.TrangThaiLichTrinh;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LichTrinh {
    private String maLichTrinh;
    private LocalDateTime ngayKhoiHanh;
    private TrangThaiLichTrinh trangThai;
    private List<ChiTietLichTrinh> chiTietLichTrinh;
    private List<VeTau> veTau;
    private HanhTrinh hanhTrinh;
    private DoanTau doanTau;

    public LichTrinh() {
        this.chiTietLichTrinh = new ArrayList<>();
        this.veTau = new ArrayList<>();
    }

    public LichTrinh(String maLichTrinh, LocalDateTime ngayKhoiHanh, TrangThaiLichTrinh trangThai,
                     HanhTrinh hanhTrinh, DoanTau doanTau) {
        this();
        this.maLichTrinh = maLichTrinh;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.trangThai = trangThai;
        this.hanhTrinh = hanhTrinh;
        this.doanTau = doanTau;
    }

    public static List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        return new ArrayList<>();
    }

    public static LichTrinh layThongTinLichTrinh(String maTau) {
        return new LichTrinh();
    }

    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public LocalDateTime getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public TrangThaiLichTrinh getTrangThai() {
        return trangThai;
    }

    public List<ChiTietLichTrinh> getChiTietLichTrinh() {
        return new ArrayList<>(chiTietLichTrinh);
    }

    public List<VeTau> getVeTau() {
        return new ArrayList<>(veTau);
    }

    public void addVeTau(VeTau ve) {
        if (veTau == null) {
            veTau = new ArrayList<>();
        }
        veTau.add(ve);
    }

    public HanhTrinh getHanhTrinh() {
        return hanhTrinh;
    }

    public DoanTau getDoanTau() {
        return doanTau;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        this.maLichTrinh = maLichTrinh;
    }

    public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public void setTrangThai(TrangThaiLichTrinh trangThai) {
        this.trangThai = trangThai;
    }

    public void setHanhTrinh(HanhTrinh hanhTrinh) {
        this.hanhTrinh = hanhTrinh;
    }

    public void setDoanTau(DoanTau doanTau) {
        this.doanTau = doanTau;
    }
}
