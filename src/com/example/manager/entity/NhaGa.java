package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

public class NhaGa {
    private String maGa;
    private String tenNhaGa;
    private String diaChi;
    private String soDienThoai;

    public NhaGa() {
    }

    public NhaGa(String maGa, String tenNhaGa, String diaChi, String soDienThoai) {
        this.maGa = maGa;
        this.tenNhaGa = tenNhaGa;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }

    // --- Factory method (sequence diagram step 34) ---
    public static NhaGa taoGaMoi(String tenNhaGa, String diaChi, String soDienThoai) {
        String maGa = "GA-" + System.currentTimeMillis();
        return new NhaGa(maGa, tenNhaGa, diaChi, soDienThoai);
    }

    // --- Business methods (sequence diagram steps 45, 62, 70) ---
    public static List<NhaGa> layDanhSach(List<NhaGa> danhSach) {
        return new ArrayList<>(danhSach);
    }

    public static List<NhaGa> timKiemTheoTen(List<NhaGa> danhSach, String tuKhoa) {
        List<NhaGa> ketQua = new ArrayList<>();
        String keyword = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        for (NhaGa ga : danhSach) {
            if (ga.tenNhaGa != null && ga.tenNhaGa.toLowerCase().contains(keyword)) {
                ketQua.add(ga);
            }
        }
        return ketQua;
    }

    public void capNhat(String tenNhaGa, String diaChi, String soDienThoai) {
        this.tenNhaGa = tenNhaGa;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }

    // --- Legacy helper kept for HanhTrinh compatibility ---
    public static List<NhaGa> layThongTinGaDauGaCuoi(String maGaDi, String maGaDen) {
        List<NhaGa> ketQua = new ArrayList<>();
        ketQua.add(new NhaGa(maGaDi, null, null, null));
        ketQua.add(new NhaGa(maGaDen, null, null, null));
        return ketQua;
    }

    // --- Getters ---
    public String getMaGa() {
        return maGa;
    }

    public String getTenNhaGa() {
        return tenNhaGa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }
}
