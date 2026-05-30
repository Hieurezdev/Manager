package com.example.manager.boundary;

public class ManHinhQuanLyFrm {
    private String subThongKe;
    private String subLenLichChay; // Tùy chọn lên lịch chạy

    public ManHinhQuanLyFrm() {
    }

    public void actionPerformed(String actionCommand) {
        if ("LenLichChay".equals(actionCommand)) {
            call();
        }
    }

    private void call() {
        // Xử lý logic chuyển màn hình
        ManHinhLenLichChayView lenLichChayView = new ManHinhLenLichChayView();
        // Gọi call() của giao diện mới để chuẩn bị dữ liệu
        lenLichChayView.call();
    }

    public ManHinhThongKeFrm moManHinhThongKe() {
        return new ManHinhThongKeFrm();
    }

    public String getSubThongKe() {
        return subThongKe;
    }

    public String getSubLenLichChay() {
        return subLenLichChay;
    }
}
