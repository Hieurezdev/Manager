package com.example.manager.boundary;

public class ManHinhQuanLyFrm {
    private String subThongKe;

    public ManHinhQuanLyFrm() {
    }

    public ManHinhThongKeFrm moManHinhThongKe() {
        return new ManHinhThongKeFrm();
    }

    public String getSubThongKe() {
        return subThongKe;
    }
}
