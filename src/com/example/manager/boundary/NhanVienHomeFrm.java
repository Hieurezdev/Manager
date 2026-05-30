package com.example.manager.boundary;

import java.awt.event.ActionEvent;

public class NhanVienHomeFrm {
    private String btnTraVe;
    // === BỔ SUNG: Biến nút Mua Vé của ông ===
    private String btnMuaVe;

    public NhanVienHomeFrm() {
    }

    // === CODE CỦA BẠN ÔNG (Giữ nguyên vẹn 100% để né Conflict) ===
    public SearchVeTauFrm moManHinhSearchVeTau() {
        return new SearchVeTauFrm();
    }

    public void actionPerformed(ActionEvent e) {
        if (e != null && "TraVe".equals(e.getActionCommand())) {
            moManHinhSearchVeTau();
        }
        
        // =========================================================================
        // BỔ SUNG LOGIC: Khi nhân viên click vào nút Mua Vé trên giao diện trang chủ
        // =========================================================================
        if (e != null && "MuaVe".equals(e.getActionCommand())) {
            moManHinhMuaVe();
        }
    }

    public String getBtnTraVe() {
        return btnTraVe;
    }

    public void setBtnTraVe(String btnTraVe) {
        this.btnTraVe = btnTraVe;
    }

    // =========================================================================
    // CÁC HÀM BỔ SUNG CỦA ÔNG (Viết dưới đáy file để Git Auto-Merge mượt mà)
    // =========================================================================
    
    /**
     * Hàm khởi tạo và hiển thị màn hình Mua Vé của ông Đạt
     */
    public MuaVeFrm moManHinhMuaVe() {
        MuaVeFrm muaVeFrm = new MuaVeFrm();
        muaVeFrm.setVisible(true); // Hiển thị màn hình mua vé lên
        return muaVeFrm;
    }

    public String getBtnMuaVe() {
        return btnMuaVe;
    }

    public void setBtnMuaVe(String btnMuaVe) {
        this.btnMuaVe = btnMuaVe;
    }
}