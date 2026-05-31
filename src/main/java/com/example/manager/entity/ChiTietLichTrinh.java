package com.example.manager.entity;

import java.time.LocalDateTime;

public class ChiTietLichTrinh {
    private String maCTLT;
    private LocalDateTime gioDen;
    private LocalDateTime gioDi;
    private NhaGa nhaGa;

    public ChiTietLichTrinh() {
    }

    public ChiTietLichTrinh(String maCTLT, LocalDateTime gioDen, LocalDateTime gioDi, NhaGa nhaGa) {
        this.maCTLT = maCTLT;
        this.gioDen = gioDen;
        this.gioDi = gioDi;
        this.nhaGa = nhaGa;
    }

    public String getMaCTLT() {
        return maCTLT;
    }

    public LocalDateTime getGioDen() {
        return gioDen;
    }

    public LocalDateTime getGioDi() {
        return gioDi;
    }

    public NhaGa getNhaGa() {
        return nhaGa;
    }
}
