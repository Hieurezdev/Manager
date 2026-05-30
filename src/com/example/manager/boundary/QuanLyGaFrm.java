package com.example.manager.boundary;

import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * QuanLyGaFrm — Station management screen (UML class diagram).
 *
 * Sequence diagram responsibilities:
 *   Steps 18-25 : Load and display station list on open.
 *   Steps 26-41 : Open ThemGaFrm to add a new station, then refresh list.
 *   Steps 42-50 : Search stations by keyword and display results.
 *   Steps 52-53 : Open QuanLyChiTietGaFrm for the selected station.
 */
public class QuanLyGaFrm {

    // UI fields (plain-string convention used throughout this codebase)
    private String txtTuKhoa;
    private String btnTimKiemGa;
    private String btnThemGa;
    private String btnXemChiTietGa;

    // Table model — rows are NhaGa objects; getters expose display state for tests
    private List<NhaGa> tblDSGa = new ArrayList<>();

    // Shared DAO (null connection — in-memory store)
    private final NhaGaDAO nhaGaDAO = new NhaGaDAO(null);

    // Sub-screens opened by this form
    private ThemGaFrm themGaFrm;
    private QuanLyChiTietGaFrm quanLyChiTietGaFrm;

    // Output message shown to the user
    private String thongBao;

    /** Constructor — step 15 (QuanLyGaFrm()) → steps 18-25 load + display list. */
    public QuanLyGaFrm() {
        taiDanhSachGa();
    }

    /**
     * Dispatches button actions — step 18 (actionPerformed()).
     * ActionCommand values: "TimKiem", "ThemGa", "XemChiTiet".
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "TimKiem"  -> timKiemGa();       // steps 42-50
            case "ThemGa"   -> moThemGaFrm();     // steps 26-41
            case "XemChiTiet" -> {                // steps 51-55
                int row = e.getID();               // row index passed as event ID
                xemChiTietGa(row);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Steps 18-25: load full station list
    // -------------------------------------------------------------------------
    /** Calls NhaGaDAO.layDanhSachGa() → NhaGa.get() → populate table. */
    public void taiDanhSachGa() {
        tblDSGa = nhaGaDAO.layDanhSachGa(); // steps 19-24
        thongBao = null;                     // step 25: giao diện chính
    }

    // -------------------------------------------------------------------------
    // Steps 42-50: search by keyword
    // -------------------------------------------------------------------------
    /** Calls NhaGaDAO.timKiemTheoTen() and refreshes table. */
    public void timKiemGa() {
        tblDSGa = nhaGaDAO.timKiemTheoTen(txtTuKhoa); // steps 44-49
        // step 50: cập nhật và hiển thị kết quả (UI layer updates table)
    }

    // -------------------------------------------------------------------------
    // Steps 26-41: add new station
    // -------------------------------------------------------------------------
    /**
     * Steps 27-28: opens ThemGaFrm.
     * After the user saves, the caller must invoke {@link #taiDanhSachGa()} to refresh
     * (steps 40-41).
     */
    public ThemGaFrm moThemGaFrm() {
        themGaFrm = new ThemGaFrm(nhaGaDAO); // step 29: ThemGaFrm()
        // step 30: hiển thị thông tin thêm ga (UI shows the sub-form)
        return themGaFrm;
    }

    // -------------------------------------------------------------------------
    // Steps 51-55: open detail / edit / delete screen
    // -------------------------------------------------------------------------
    /**
     * Step 52-54: opens QuanLyChiTietGaFrm for the selected station.
     *
     * @param rowIndex index of the selected row in tblDSGa
     */
    public QuanLyChiTietGaFrm xemChiTietGa(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= tblDSGa.size()) {
            thongBao = "Vui lòng chọn một nhà ga.";
            return null;
        }
        NhaGa gaChon = tblDSGa.get(rowIndex);
        quanLyChiTietGaFrm = new QuanLyChiTietGaFrm(gaChon, nhaGaDAO, this); // step 54
        // step 55: hiển thị thông tin chi tiết trên nhà ga đã chọn
        return quanLyChiTietGaFrm;
    }

    // -------------------------------------------------------------------------
    // Setters (populate UI fields before actions)
    // -------------------------------------------------------------------------
    public void setTxtTuKhoa(String txtTuKhoa) {
        this.txtTuKhoa = txtTuKhoa;
    }

    // -------------------------------------------------------------------------
    // Getters (expose state for callers / tests)
    // -------------------------------------------------------------------------
    public List<NhaGa> getTblDSGa() {
        return new ArrayList<>(tblDSGa);
    }

    public String getThongBao() {
        return thongBao;
    }

    public ThemGaFrm getThemGaFrm() {
        return themGaFrm;
    }

    public QuanLyChiTietGaFrm getQuanLyChiTietGaFrm() {
        return quanLyChiTietGaFrm;
    }

    public NhaGaDAO getNhaGaDAO() {
        return nhaGaDAO;
    }
}
