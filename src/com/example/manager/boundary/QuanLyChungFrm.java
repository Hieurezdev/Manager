package com.example.manager.boundary;

import java.awt.event.ActionEvent;

/**
 * QuanLyChungFrm — Main management hub (UML class diagram).
 * Sequence diagram steps 12-15:
 *   - Quản Lý chọn "Quản lý thông tin nhà ga" → actionPerformed() → opens QuanLyGaFrm.
 */
public class QuanLyChungFrm {
    // Navigation buttons
    private String btnQuanLyBCTKL; // Báo cáo thống kê lịch trình
    private String btnQuanLyTTNG;  // Thông tin nhà ga
    private String btnQuanLyLTCT;  // Lịch trình chuyến tàu

    // Output screen opened by this form
    private QuanLyGaFrm quanLyGaFrm;

    public QuanLyChungFrm() {
    }

    /** Sequence diagram step 13: actionPerformed() when btnQuanLyTTNG is clicked. */
    public void actionPerformed(ActionEvent e) {
        String source = e.getActionCommand();
        if ("QuanLyTTNG".equals(source)) {
            moManHinhQuanLyGa();
        }
    }

    /** Sequence diagram step 14-15: opens QuanLyGaFrm. */
    public QuanLyGaFrm moManHinhQuanLyGa() {
        quanLyGaFrm = new QuanLyGaFrm(); // step 15: QuanLyGaFrm()
        return quanLyGaFrm;              // step 16: Hiển thị
    }

    // --- Getter ---
    public QuanLyGaFrm getQuanLyGaFrm() {
        return quanLyGaFrm;
    }
}
