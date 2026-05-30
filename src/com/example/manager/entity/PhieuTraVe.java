package com.example.manager.entity;

import java.time.LocalDateTime;

public class PhieuTraVe {
    private String maPhieu;
    private VeTau veTau;
    private int tienPhat;
    private int tienHoanLai;
    private LocalDateTime ngayTao;

    public PhieuTraVe() {
    }

    public PhieuTraVe(String maPhieu, VeTau veTau, int tienPhat, int tienHoanLai, LocalDateTime ngayTao) {
        this.maPhieu = maPhieu;
        this.veTau = veTau;
        this.tienPhat = tienPhat;
        this.tienHoanLai = tienHoanLai;
        this.ngayTao = ngayTao;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public VeTau getVeTau() {
        return veTau;
    }

    public void setVeTau(VeTau veTau) {
        this.veTau = veTau;
    }

    public int getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(int tienPhat) {
        this.tienPhat = tienPhat;
    }

    public int getTienHoanLai() {
        return tienHoanLai;
    }

    public void setTienHoanLai(int tienHoanLai) {
        this.tienHoanLai = tienHoanLai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
