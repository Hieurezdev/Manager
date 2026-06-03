package com.example.manager.entity;

import java.time.LocalDateTime;

public class HoaDon {

    private String maHoaDon;
    private PhieuTraVe phieuTraVe;
    private int tongTien;
    private String loaiHoaDon;
    private LocalDateTime ngayTao;

    private String maNhanVienLap;
    private String maKhachHang;
    private String phuongThucThanhToan;
    private String trangThai;
    private String loaiDon;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, PhieuTraVe phieuTraVe, int tongTien, String loaiHoaDon, LocalDateTime ngayTao) {
        this.maHoaDon = maHoaDon;
        this.phieuTraVe = phieuTraVe;
        this.tongTien = tongTien;
        this.loaiHoaDon = loaiHoaDon;
        this.ngayTao = ngayTao;
    }

    public String getMaNhanVienLap() {
        return maNhanVienLap;
    }

    public void setMaNhanVienLap(String m) {
        this.maNhanVienLap = m;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String m) {
        this.maKhachHang = m;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String p) {
        this.phuongThucThanhToan = p;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String t) {
        this.trangThai = t;
    }

    public String getLoaiDon() {
        return loaiDon;
    }

    public void setLoaiDon(String loaiDon) {
        this.loaiDon = loaiDon;
    }

    public String getNgayGioLap() {
        return ngayTao != null ? ngayTao.toString().replace("T", " ") : "";
    }

    public void setNgayGioLap(String n) {
        if (n == null || n.trim().isEmpty()) {
            this.ngayTao = LocalDateTime.now();
            return;
        }
        try {
            // Thay thế khoảng trắng bằng chữ T để hợp thức hóa định dạng ISO nếu cần
            String formatted = n.trim().replace(" ", "T");
            if (formatted.length() > 19) {
                formatted = formatted.substring(0, 19);
            }
            this.ngayTao = LocalDateTime.parse(formatted);
        } catch (Exception e) {
            // Nếu có lỗi parse thì gán mặc định thời gian hiện tại chứ không cho sập app
            this.ngayTao = LocalDateTime.now();
        }
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
