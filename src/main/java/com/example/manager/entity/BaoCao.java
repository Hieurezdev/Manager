package com.example.manager.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BaoCao {
    private String maBaoCao;
    private LocalDate ngayLapBaoCao;
    private int tongDoanhThu;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private List<ChiTietBaoCao> chiTietBaoCao;

    public BaoCao() {
        this.chiTietBaoCao = new ArrayList<>();
    }

    public BaoCao(LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this();
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ngayLapBaoCao = LocalDate.now();
    }

    public boolean kiemTraHopLe(LocalDate ngayBD, LocalDate ngayKT) {
        if (ngayBD == null || ngayKT == null) {
            return false;
        }
        return !ngayKT.isBefore(ngayBD);
    }

    public static BaoCao taoMoiBaoCao(LocalDate ngayBD, LocalDate ngayKT) {
        return new BaoCao(ngayBD, ngayKT);
    }

    public void tinhDoanhThu() {
        int tong = 0;
        for (ChiTietBaoCao chiTiet : chiTietBaoCao) {
            tong += chiTiet.getDoanhThuChuyen();
        }
        this.tongDoanhThu = tong;
    }

    public void sapXepDoanhThuGiamDan() {
        chiTietBaoCao.sort(Comparator.comparingInt(ChiTietBaoCao::getDoanhThuChuyen).reversed());
    }

    public void luuKetQuaThongKe(String maBaoCao, List<ChiTietBaoCao> dsKetQua) {
        this.maBaoCao = maBaoCao;
        this.chiTietBaoCao = new ArrayList<>(dsKetQua);
    }

    public String getMaBaoCao() {
        return maBaoCao;
    }

    public LocalDate getNgayLapBaoCao() {
        return ngayLapBaoCao;
    }

    public int getTongDoanhThu() {
        return tongDoanhThu;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public List<ChiTietBaoCao> getChiTietBaoCao() {
        return new ArrayList<>(chiTietBaoCao);
    }
}
