package com.example.manager.entity;

public class ChinhSachGia {
    private String maChinhSach;
    private String tenLoaiDoiTuong;
    private double phanTramGiamGia;
    private String moTa;

    // Constructor mặc định
    public ChinhSachGia() {}

    // Constructor đầy đủ tham số (Thêm vào để tối ưu code, không lo conflict)
    public ChinhSachGia(String maChinhSach, String tenLoaiDoiTuong, double phanTramGiamGia, String moTa) {
        this.maChinhSach = maChinhSach;
        this.tenLoaiDoiTuong = tenLoaiDoiTuong;
        this.phanTramGiamGia = phanTramGiamGia;
        this.moTa = moTa;
    }

    // === Danh sách các hàm Getter và Setter giữ nguyên vẹn ===
    public String getMaChinhSach() { 
        return maChinhSach; 
    }
    
    public void setMaChinhSach(String maChinhSach) { 
        this.maChinhSach = maChinhSach; 
    }
    
    public String getTenLoaiDoiTuong() { 
        return tenLoaiDoiTuong; 
    }
    
    public void setTenLoaiDoiTuong(String tenLoaiDoiTuong) { 
        this.tenLoaiDoiTuong = tenLoaiDoiTuong; 
    }
    
    public double getPhanTramGiamGia() { 
        return phanTramGiamGia; 
    }
    
    public void setPhanTramGiamGia(double phanTramGiamGia) { 
        this.phanTramGiamGia = phanTramGiamGia; 
    }
    
    public String getMoTa() { 
        return moTa; 
    }
    
    public void setMoTa(String moTa) { 
        this.moTa = moTa; 
    }
}