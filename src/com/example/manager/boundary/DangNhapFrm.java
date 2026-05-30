package com.example.manager.boundary;

import com.example.manager.dao.QuanLyDAO;
import com.example.manager.entity.QuanLy;

import java.awt.event.ActionEvent;

/**
 * DangNhapFrm — Login screen (UML class diagram).
 * Sequence diagram steps 1-11:
 *   - Quản Lý enters credentials → actionPerformed() → QuanLyDAO.checkDangNhap()
 *   - On success, opens QuanLyChungFrm.
 */
public class DangNhapFrm {
    // UI fields (represented as plain strings per project convention)
    private String txtTDN;
    private String txtMK;
    private String btnDangNhap;

    // Output — set after a login attempt
    private boolean dangNhapThanhCong;
    private QuanLyChungFrm quanLyChungFrm;

    public DangNhapFrm() {
    }

    /** Sequence diagram step 2: actionPerformed() triggered by btnDangNhap click. */
    public void actionPerformed(ActionEvent e) {
        dangNhap();
    }

    /** Sequence diagram steps 3-10: build QuanLy, validate, open QuanLyChungFrm on success. */
    public QuanLyChungFrm dangNhap() {
        QuanLy quanLy = new QuanLy(); // step 4: QuanLy()
        quanLy.setTenDangNhap(txtTDN);
        quanLy.setMatKhau(txtMK);
        QuanLyDAO quanLyDAO = new QuanLyDAO(null);
        boolean hopLe = quanLyDAO.checkDangNhap(quanLy);        // step 7: checkDangNhap()

        dangNhapThanhCong = hopLe;
        if (!hopLe) {
            return null;
        }

        quanLyChungFrm = new QuanLyChungFrm(); // step 10: QuanLyChungFrm()
        return quanLyChungFrm;                 // step 11: Hiển thị
    }

    // --- Setters (used by tests / caller to fill UI fields) ---
    public void setTxtTDN(String txtTDN) {
        this.txtTDN = txtTDN;
    }

    public void setTxtMK(String txtMK) {
        this.txtMK = txtMK;
    }

    // --- Getters ---
    public boolean isDangNhapThanhCong() {
        return dangNhapThanhCong;
    }

    public QuanLyChungFrm getQuanLyChungFrm() {
        return quanLyChungFrm;
    }
}
