package com.example.manager.entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soCCCD;
    private String soDienThoai;
    private String email;

    // === Constructor mặc định từ GitHub ===
    public KhachHang() {
    }

    // === Constructor đầy đủ tham số từ GitHub ===
    public KhachHang(String maKH, String hoTen, String soCCCD, String soDienThoai, String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soCCCD = soCCCD;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // === TOÀN BỘ GETTER GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để né Conflict) ===
    public String getMaKH() {
        return maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    // =========================================================================
    // CÁC HÀM SETTER BỔ SUNG CỦA ÔNG (Phục vụ luồng Mua Vé, không sợ conflict)
    // =========================================================================
    public void setMaKH(String maKH) { 
        this.maKH = maKH; 
    }

    public void setHoTen(String hoTen) { 
        this.hoTen = hoTen; 
    }

    public void setSoCCCD(String soCCCD) { 
        this.soCCCD = soCCCD; 
    }

    public void setSoDienThoai(String soDienThoai) { 
        this.soDienThoai = soDienThoai; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }
}