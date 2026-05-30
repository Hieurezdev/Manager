package com.example.manager.entity;

import java.time.LocalDateTime;

public class HoaDon {

    // =========================================================================
    // CÁC THUỘC TÍNH GỐC TỪ GITHUB (Giữ nguyên vẹn kiểu dữ liệu)
    // =========================================================================
    private String maHoaDon;
    private PhieuTraVe phieuTraVe;
    private int tongTien;
    private String loaiHoaDon;
    private LocalDateTime ngayTao;

    // === BỔ SUNG: Các trường phục vụ luồng nghiệp vụ Mua Vé của ông Đạt ===
    private String maNhanVienLap;
    private String maKhachHang;
    private String phuongThucThanhToan;
    private String trangThai;
    private String loaiDon;

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

    // === Thêm các Getter/Setter cho các thuộc tính ông bổ sung ===
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

    // ĐỒNG BỘ: Để tương thích với code cũ giao diện nếu ông lỡ gọi getNgayGioLap dạng String
    public String getNgayGioLap() {
        return ngayTao != null ? ngayTao.toString().replace("T", " ") : "";
    }

    // ĐỒNG BỘ NÂNG CẤP: Bọc an toàn để tránh crash app khi ép chuỗi ngày tháng lệch định dạng
    public void setNgayGioLap(String n) {
        if (n == null || n.trim().isEmpty()) {
            this.ngayTao = LocalDateTime.now();
            return;
        }
        try {
            // Thay thế khoảng trắng bằng chữ T để hợp thức hóa định dạng ISO nếu cần
            String formatted = n.trim().replace(" ", "T");
            if (formatted.length() > 19) {
                formatted = formatted.substring(0, 19); // Cắt bỏ phần nano giây dư thừa từ SQL
            }
            this.ngayTao = LocalDateTime.parse(formatted);
        } catch (Exception e) {
            // Nếu có lỗi parse thì gán mặc định thời gian hiện tại chứ không cho sập app
            this.ngayTao = LocalDateTime.now();
        }
    }

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
