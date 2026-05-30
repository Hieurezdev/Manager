package com.example.manager.boundary;

import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;

import java.awt.event.ActionEvent;

/**
 * QuanLyChiTietGaFrm — Station detail / edit / delete screen (UML class diagram).
 *
 * Sequence diagram responsibilities:
 *   Step  54 : QuanLyChiTietGaFrm(ng: NhaGa) — constructed with selected station.
 *   Step  55 : Hiển thị thông tin chi tiết.
 *   Steps 56-64 [Sửa thông tin nhà ga]:
 *              User edits fields and clicks btnLuu → actionPerformed()
 *              → NhaGa.set() (step 59) → NhaGaDAO.capNhatGa() (step 62)
 *              → step 64: thông báo cập nhật thành công.
 *   Steps 65-72 [Xóa nhà ga]:
 *              User clicks btnXoaGa → actionPerformed() (step 66)
 *              → QuanLyGaFrm.actionPerformed() (step 68) → NhaGaDAO.xoaGa() (step 70)
 *              → step 72: hiển thị thành công.
 */
public class QuanLyChiTietGaFrm {

    // The station being viewed / edited (UML attribute: ng: NhaGa)
    private final NhaGa ng;

    // UI fields — pre-populated from ng in constructor
    private String txtTenNhaGa;
    private String txtDiaChi;
    private String txtSoDienThoai;
    private String btnLuu;
    private String btnHuy;
    private String btnXoaGa;
    private String btnSuaGa;

    // Injected collaborators
    private final NhaGaDAO nhaGaDAO;
    private final QuanLyGaFrm quanLyGaFrm; // parent — used to trigger list refresh

    // Output state
    private String thongBao;
    private boolean daXoa;
    private boolean daCapNhat;

    /**
     * UML constructor: QuanLyChiTietGaFrm(ng: NhaGa).
     * Pre-fills text fields from the selected station (step 55).
     */
    public QuanLyChiTietGaFrm(NhaGa ng, NhaGaDAO nhaGaDAO, QuanLyGaFrm quanLyGaFrm) {
        this.ng = ng;
        this.nhaGaDAO = nhaGaDAO;
        this.quanLyGaFrm = quanLyGaFrm;
        hienThiThongTin(); // step 55
    }

    /**
     * Dispatches button actions (steps 57, 66).
     * ActionCommand values: "Luu", "Huy", "XoaGa", "SuaGa"
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Luu"   -> luu();    // steps 57-64: save edited data
            case "XoaGa" -> xoaGa(); // steps 66-72: delete station
            case "Huy"   -> huy();
        }
    }

    // -------------------------------------------------------------------------
    // Steps 57-64: update station
    // -------------------------------------------------------------------------
    /**
     * Applies edited values to NhaGa (step 59: set()) then persists via DAO (step 62).
     */
    public boolean luu() {
        if (!hopLe()) {
            thongBao = "Vui lòng nhập đầy đủ thông tin.";
            return false;
        }

        // step 58-60: NhaGa.set() — capNhat mutates the entity in place
        ng.capNhat(txtTenNhaGa.trim(), txtDiaChi.trim(), txtSoDienThoai.trim());

        // step 61: call → step 62: NhaGaDAO.capNhatGa() → step 63: return
        nhaGaDAO.capNhatGa(ng, txtTenNhaGa.trim(), txtDiaChi.trim(), txtSoDienThoai.trim());

        daCapNhat = true;
        thongBao = "Cập nhật nhà ga thành công!"; // step 64

        quanLyGaFrm.taiDanhSachGa(); // refresh parent list
        return true;
    }

    // -------------------------------------------------------------------------
    // Steps 66-72: delete station
    // -------------------------------------------------------------------------
    /**
     * Step 66: actionPerformed() on btnXoaGa.
     * Delegates to NhaGaDAO.xoaGa() (step 70), then refreshes parent (step 72).
     */
    public boolean xoaGa() {
        // step 67: call → step 68: QuanLyGaFrm.actionPerformed() (delegated here directly)
        // step 69: call → step 70: NhaGaDAO.xoaGa()
        boolean ketQua = nhaGaDAO.xoaGa(ng); // step 70
        // step 71: return

        if (ketQua) {
            daXoa = true;
            thongBao = "Xóa nhà ga thành công!"; // step 72
            quanLyGaFrm.taiDanhSachGa();          // refresh parent list
        } else {
            thongBao = "Không thể xóa nhà ga này.";
        }
        return ketQua;
    }

    private void huy() {
        thongBao = null;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    /** Populates editable fields from the wrapped NhaGa (step 55). */
    private void hienThiThongTin() {
        txtTenNhaGa  = ng.getTenNhaGa();
        txtDiaChi    = ng.getDiaChi();
        txtSoDienThoai = ng.getSoDienThoai();
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
    public NhaGa getNg() {
        return ng;
    }

    public String getThongBao() {
        return thongBao;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public boolean isDaCapNhat() {
        return daCapNhat;
    }
}
