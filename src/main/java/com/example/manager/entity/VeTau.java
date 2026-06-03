package com.example.manager.entity;

import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.TrangThaiVe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VeTau {
    // =========================================================================
    // =========================================================================
    private String maVe;
    private LoaiDoiTuong loaiDoiTuong;
    private int giaVe; // Bản GitHub dùng int
    private TrangThaiVe trangThai;
    private LocalDateTime thoiDiemBanVe;
    private GheNgoi gheNgoi;
    private KhachHang khachHang;
    private LichTrinh lichTrinh;
    private NhanVien nhanVien;

    // === BỔ SUNG LẠI: Các trường String cũ phục vụ cho luồng lưu JDBC của ông ===
    private String maKH;
    private String maLichTrinh;
    private String maGhe;
    private String maNhanVienBanVe;
    private String maHoaDon;
    private String thoiDiemBanVeStr;

    // === Constructor mặc định từ GitHub ===
    public VeTau() {
    }

    // === Constructor đầy đủ tham số 1 từ GitHub ===
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

    // === Constructor đầy đủ tham số 2 từ GitHub ===
    public VeTau(String maVe, LoaiDoiTuong loaiDoiTuong, int giaVe, TrangThaiVe trangThai,
                 LocalDateTime thoiDiemBanVe, GheNgoi gheNgoi, KhachHang khachHang,
                 LichTrinh lichTrinh, NhanVien nhanVien) {
        this.maVe = maVe;
        this.loaiDoiTuong = loaiDoiTuong;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
        this.thoiDiemBanVe = thoiDiemBanVe;
        this.gheNgoi = gheNgoi;
        this.khachHang = khachHang;
        this.lichTrinh = lichTrinh;
        this.nhanVien = nhanVien;
    }

    // === BỔ SUNG LẠI: Các Getter/Setter cho thuộc tính String cũ của ông (Né lỗi compile tầng DAO) ===
    public String getMaKH() { return maKH != null ? maKH : (khachHang != null ? khachHang.getMaKH() : ""); }
    public void setMaKH(String m) { this.maKH = m; }
    public String getMaLichTrinh() { return maLichTrinh != null ? maLichTrinh : (lichTrinh != null ? lichTrinh.getMaLichTrinh() : ""); }
    public void setMaLichTrinh(String m) { this.maLichTrinh = m; }
    public String getMaGhe() { return maGhe != null ? maGhe : (gheNgoi != null ? gheNgoi.getMaGhe() : ""); }
    public void setMaGhe(String m) { this.maGhe = m; }
    public String getMaNhanVienBanVe() { return maNhanVienBanVe != null ? maNhanVienBanVe : (nhanVien != null ? nhanVien.getMaNhanVien() : ""); }
    public void setMaNhanVienBanVe(String m) { this.maNhanVienBanVe = m; }
    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String m) { this.maHoaDon = m; }
    public String getThoiDiemBanVeStr() { return thoiDiemBanVeStr; }
    public void setThoiDiemBanVeStr(String t) { this.thoiDiemBanVeStr = t; }

    // =========================================================================
    // =========================================================================
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

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public void setLoaiDoiTuong(LoaiDoiTuong loaiDoiTuong) {
        this.loaiDoiTuong = loaiDoiTuong;
    }

    public void setGiaVe(int giaVe) {
        this.giaVe = giaVe;
    }

    public void setTrangThai(TrangThaiVe trangThai) {
        this.trangThai = trangThai;
    }

    public void setThoiDiemBanVe(LocalDateTime thoiDiemBanVe) {
        this.thoiDiemBanVe = thoiDiemBanVe;
    }

    public void setGheNgoi(GheNgoi gheNgoi) {
        this.gheNgoi = gheNgoi;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }

    public void setLichTrinh(LichTrinh lichTrinh) {
        this.lichTrinh = lichTrinh;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
}