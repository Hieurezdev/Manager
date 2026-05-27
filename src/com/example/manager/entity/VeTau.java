package com.example.manager.entity;

import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.TrangThaiVe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VeTau {
    private String maVe;
    private LoaiDoiTuong loaiDoiTuong;
    private int giaVe;
    private TrangThaiVe trangThai;
    private LocalDateTime thoiDiemBanVe;
    private GheNgoi gheNgoi;
    private KhachHang khachHang;

    public VeTau() {
    }

    public VeTau(String maVe, LoaiDoiTuong loaiDoiTuong, int giaVe, TrangThaiVe trangThai,
                 LocalDateTime thoiDiemBanVe, GheNgoi gheNgoi, KhachHang khachHang) {
        this.maVe = maVe;
        this.loaiDoiTuong = loaiDoiTuong;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
        this.thoiDiemBanVe = thoiDiemBanVe;
        this.gheNgoi = gheNgoi;
        this.khachHang = khachHang;
    }

    public static List<VeTau> layDanhSachVeTheoLichTrinh(String maLichTrinh) {
        return new ArrayList<>();
    }

    public String getMaVe() {
        return maVe;
    }

    public LoaiDoiTuong getLoaiDoiTuong() {
        return loaiDoiTuong;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public TrangThaiVe getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getThoiDiemBanVe() {
        return thoiDiemBanVe;
    }

    public GheNgoi getGheNgoi() {
        return gheNgoi;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }
}
