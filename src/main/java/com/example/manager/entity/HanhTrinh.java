package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

public class HanhTrinh {

    private String maHanhTrinh;
    private String tenHanhTrinh;
    private double quangDuong;
    private List<ChiTietHanhTrinh> chiTietHanhTrinh;

    // === BỔ SUNG: Thuộc tính phục vụ riêng cho module Mua Vé của ông Đạt ===
    private String maGaDi;
    private String maGaDen;

    // === Constructor mặc định gốc từ GitHub ===
    public HanhTrinh() {
        this.chiTietHanhTrinh = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số gốc từ GitHub ===
    public HanhTrinh(String maHanhTrinh, String tenHanhTrinh, double quangDuong,
            List<ChiTietHanhTrinh> chiTietHanhTrinh) {
        this.maHanhTrinh = maHanhTrinh;
        this.tenHanhTrinh = tenHanhTrinh;
        this.quangDuong = quangDuong;
        this.chiTietHanhTrinh = chiTietHanhTrinh == null ? new ArrayList<>() : new ArrayList<>(chiTietHanhTrinh);
    }

    // === Getter/Setter cho thuộc tính của ông Đạt (Đã sửa lỗi biên dịch) ===
    public String getMaGaDi() {
        return maGaDi;
    }

    public void setMaGaDi(String maGaDi) {
        this.maGaDi = maGaDi;
    }

    public String getMaGaDen() {
        return maGaDen;
    }

    public void setMaGaDen(String maGaDen) {
        this.maGaDen = maGaDen;
    }

    // =========================================================================
    // TOÀN BỘ LOGIC GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để diệt tận gốc Conflict)
    // =========================================================================
    public static HanhTrinh layThongTinHanhTrinh(String maHanhTrinh) {
        HanhTrinh hanhTrinh = new HanhTrinh();
        hanhTrinh.maHanhTrinh = maHanhTrinh;
        return hanhTrinh;
    }

    // ĐỒNG BỘ: Trả lại hàm bốc ga trung gian cho bạn ông làm Thống kê không bị gãy code
    public List<ChiTietHanhTrinh> getDanhSachGaTrungGian() {
        com.example.manager.dao.HanhTrinhDAO dao = new com.example.manager.dao.HanhTrinhDAO(null);
        return dao.getGaTrungGian(this.maHanhTrinh);
    }

    public String getMaHanhTrinh() {
        return maHanhTrinh;
    }

    public String getTenHanhTrinh() {
        return tenHanhTrinh;
    }

    public double getQuangDuong() {
        return quangDuong;
    }

    public List<ChiTietHanhTrinh> getChiTietHanhTrinh() {
        return new ArrayList<>(chiTietHanhTrinh);
    }

    public NhaGa getGaDau() {
        ChiTietHanhTrinh dau = null;
        for (ChiTietHanhTrinh ct : chiTietHanhTrinh) {
            if (dau == null || ct.getThuTuGa() < dau.getThuTuGa()) {
                dau = ct;
            }
        }
        return dau == null ? null : dau.getNhaGa();
    }

    public NhaGa getGaCuoi() {
        ChiTietHanhTrinh cuoi = null;
        for (ChiTietHanhTrinh ct : chiTietHanhTrinh) {
            if (cuoi == null || ct.getThuTuGa() > cuoi.getThuTuGa()) {
                cuoi = ct;
            }
        }
        return cuoi == null ? null : cuoi.getNhaGa();
    }

    public void setMaHanhTrinh(String maHanhTrinh) {
        this.maHanhTrinh = maHanhTrinh;
    }

    public void setTenHanhTrinh(String tenHanhTrinh) {
        this.tenHanhTrinh = tenHanhTrinh;
    }

    public void setQuangDuong(double quangDuong) {
        this.quangDuong = quangDuong;
    }

    public void setChiTietHanhTrinh(List<ChiTietHanhTrinh> chiTietHanhTrinh) {
        this.chiTietHanhTrinh = chiTietHanhTrinh;
    }
}