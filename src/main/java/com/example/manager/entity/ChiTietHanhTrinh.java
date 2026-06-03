package com.example.manager.entity;

public class ChiTietHanhTrinh {
    private String maCTHT;
    private int thuTuGa;
    private NhaGa nhaGa;

    public ChiTietHanhTrinh() {
    }

    public ChiTietHanhTrinh(String maCTHT, int thuTuGa, NhaGa nhaGa) {
        this.maCTHT = maCTHT;
        this.thuTuGa = thuTuGa;
        this.nhaGa = nhaGa;
    }

    public String getMaCTHT() {
        return maCTHT;
    }

    public int getThuTuGa() {
        return thuTuGa;
    }

    public NhaGa getNhaGa() {
        return nhaGa;
    }
}
