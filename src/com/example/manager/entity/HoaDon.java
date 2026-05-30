package com.example.manager.entity;

import java.time.LocalDateTime;

public class HoaDon {
    private String maHoaDon;
    private PhieuTraVe phieuTraVe;
    private int tongTien;
    private String loaiHoaDon;
    private LocalDateTime ngayTao;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, PhieuTraVe phieuTraVe, int tongTien, String loaiHoaDon, LocalDateTime ngayTao) {
        this.maHoaDon = maHoaDon;
        this.phieuTraVe = phieuTraVe;
        this.tongTien = tongTien;
        this.loaiHoaDon = loaiHoaDon;
        this.ngayTao = ngayTao;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public PhieuTraVe getPhieuTraVe() {
        return phieuTraVe;
    }

    public void setPhieuTraVe(PhieuTraVe phieuTraVe) {
        this.phieuTraVe = phieuTraVe;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getLoaiHoaDon() {
        return loaiHoaDon;
    }

    public void setLoaiHoaDon(String loaiHoaDon) {
        this.loaiHoaDon = loaiHoaDon;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
