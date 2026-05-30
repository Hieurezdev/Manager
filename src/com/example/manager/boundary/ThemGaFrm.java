package com.example.manager.boundary;

import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;

import java.awt.event.ActionEvent;

/**
 * ThemGaFrm — Add new station dialog (UML class diagram).
 *
 * Sequence diagram responsibilities:
 *   Step  29 : ThemGaFrm() — constructed by QuanLyGaFrm.
 *   Step  30 : Hiển thị thông tin thêm ga (fields visible to user).
 *   Steps 31-32 : User enters data and clicks btnLuu → actionPerformed().
 *   Steps 33-38 : Call NhaGaDAO.taoGaMoi() → NhaGa.set() → return.
 *   Step  39 : Hiển thị thông báo thành công.
 *   Steps 40-41 : Caller (QuanLyGaFrm) refreshes the station list.
 */
public class ThemGaFrm {

    // UI fields
    private String txtTenNhaGa;
    private String txtDiaChi;
    private String txtSoDienThoai;
    private String btnLuu;
    private String btnHuy;

    // Injected DAO — shared with parent QuanLyGaFrm
    private final NhaGaDAO nhaGaDAO;

    // Output state
    private NhaGa gaVuaThem;
    private String thongBao;
    private boolean huy;

    /**
     * UML constructor: ThemGaFrm(tenGa, diaChi, soDienThoai).
     * Pre-fills fields when the form is opened with existing data (optional).
     *
     * @param nhaGaDAO shared DAO from the parent screen
     */
    public ThemGaFrm(NhaGaDAO nhaGaDAO) {
        this.nhaGaDAO = nhaGaDAO;
    }

    /**
     * Convenience constructor matching the UML signature.
     * Pre-populates the input fields (useful when cloning an existing station).
     */
    public ThemGaFrm(String tenGa, String diaChi, String soDienThoai, NhaGaDAO nhaGaDAO) {
        this.txtTenNhaGa = tenGa;
        this.txtDiaChi = diaChi;
        this.txtSoDienThoai = soDienThoai;
        this.nhaGaDAO = nhaGaDAO;
    }

    /**
     * Sequence diagram step 32: actionPerformed() — btnLuu or btnHuy clicked.
     * ActionCommand: "Luu" | "Huy"
     */
    public void actionPerformed(ActionEvent e) {
        if ("Luu".equals(e.getActionCommand())) {
            luu();
        } else {
            huyThemGa();
        }
    }

    // -------------------------------------------------------------------------
    // Steps 33-39: validate → taoGaMoi → notify
    // -------------------------------------------------------------------------
    /** Validates input, delegates creation to NhaGaDAO, sets success message. */
    public NhaGa luu() {
        if (!hopLe()) {
            thongBao = "Vui lòng nhập đầy đủ thông tin nhà ga.";
            return null;
        }

        // step 33: call → step 34: NhaGaDAO.taoGaMoi() → steps 35-37: NhaGa.set() internally
        gaVuaThem = nhaGaDAO.taoGaMoi(txtTenNhaGa.trim(), txtDiaChi.trim(), txtSoDienThoai.trim());

        thongBao = "Thêm nhà ga thành công!"; // step 39
        return gaVuaThem;                      // step 38: return to QuanLyGaFrm
    }

    private void huyThemGa() {
        huy = true;
        thongBao = null;
    }

    private boolean hopLe() {
        return txtTenNhaGa != null && !txtTenNhaGa.isBlank()
                && txtDiaChi != null && !txtDiaChi.isBlank()
                && txtSoDienThoai != null && !txtSoDienThoai.isBlank();
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------
    public void setTxtTenNhaGa(String txtTenNhaGa) {
        this.txtTenNhaGa = txtTenNhaGa;
    }

    public void setTxtDiaChi(String txtDiaChi) {
        this.txtDiaChi = txtDiaChi;
    }

    public void setTxtSoDienThoai(String txtSoDienThoai) {
        this.txtSoDienThoai = txtSoDienThoai;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------
    public NhaGa getGaVuaThem() {
        return gaVuaThem;
    }

    public String getThongBao() {
        return thongBao;
    }

    public boolean isHuy() {
        return huy;
    }
}
