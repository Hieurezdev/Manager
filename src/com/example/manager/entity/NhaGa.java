package com.example.manager.entity;

import java.util.ArrayList;
import java.util.List;

public class NhaGa {
    private String maGa;
    private String tenNhaGa;
    private String soDienThoai;

    public NhaGa() {
    }

    public NhaGa(String maGa, String tenNhaGa, String soDienThoai) {
        this.maGa = maGa;
        this.tenNhaGa = tenNhaGa;
        this.soDienThoai = soDienThoai;
    }

    public static List<NhaGa> layThongTinGaDauGaCuoi(String maGaDi, String maGaDen) {
        List<NhaGa> ketQua = new ArrayList<>();
        ketQua.add(new NhaGa(maGaDi, null, null));
        ketQua.add(new NhaGa(maGaDen, null, null));
        return ketQua;
    }

    public String getMaGa() {
        return maGa;
    }

    public String getTenNhaGa() {
        return tenNhaGa;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }
}
