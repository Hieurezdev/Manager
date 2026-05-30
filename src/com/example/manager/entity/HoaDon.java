package com.example.manager.entity;

import java.time.LocalDateTime;

public class HoaDon {
    // =========================================================================
    // CÁC THUỘC TÍNH GỐC TỪ GITHUB (Giữ nguyên vẹn kiểu dữ liệu)
    // =========================================================================
    private String maHoaDon;
    private PhieuTraVe phieuTraVe;
    private int tongTien; // Bản GitHub dùng int
    private String loaiHoaDon;
    private LocalDateTime ngayTao;

    // === BỔ SUNG LẠI: Các trường cũ phục vụ luồng Mua Vé của ông ===
    private String maNhanVienLap;
    private String maKhachHang;
    private String phuongThucThanhToan;
    private String trangThai;
    private String loaiDon; // Hỗ trợ hàm setLoaiDon() trong KhachHangDAO lúc nãy

    // === Constructor mặc định từ GitHub ===
    public HoaDon() {
    }

    // === Constructor đầy đủ tham số từ GitHub ===
    public HoaDon(String maHoaDon, PhieuTraVe phieuTraVe, int tongTien, String loaiHoaDon, LocalDateTime ngayTao) {
        this.maHoaDon = maHoaDon;
        this.phieuTraVe = phieuTraVe;
        this.tongTien = tongTien;
        this.loaiHoaDon = loaiHoaDon;
        this.ngayTao = ngayTao;
    }

    // === Thêm các Getter/Setter cho các thuộc tính ông bổ sung (Né lỗi compile luồng cũ) ===
    public String getMaNhanVienLap() { return maNhanVienLap; }
    public void setMaNhanVienLap(String m) { this.maNhanVienLap = m; }
    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String m) { this.maKhachHang = m; }
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String p) { this.phuongThucThanhToan = p; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String t) { this.trangThai = t; }
    public String getLoaiDon() { return loaiDon; }
    public void setLoaiDon(String loaiDon) { this.loaiDon = loaiDon; }

    // Để tương thích với code cũ giao diện nếu ông lỡ gọi getNgayGioLap dạng String
    public String getNgayGioLap() { return ngayTao != null ? ngayTao.toString() : ""; }
    public void setNgayGioLap(String n) { this.ngayTao = LocalDateTime.parse(n); }

    // =========================================================================
    // TOÀN BỘ GETTER/SETTER GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để né Conflict)
    // =========================================================================
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