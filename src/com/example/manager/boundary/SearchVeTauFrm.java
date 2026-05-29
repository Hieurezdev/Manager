package com.example.manager.boundary;

import com.example.manager.dao.DBConnection;
import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.TrangThaiVe;

import java.awt.event.ActionEvent;
import java.sql.Connection;

public class SearchVeTauFrm {
    private String txtMaVe;
    private String btnTimKiem;
    private String btnHuyVe;

    // Output fields for UI
    private String outMaVe;
    private String outHoTenKhach;
    private String outSoCCCD;
    private String outTenChuyenTau;
    private String outGaDi;
    private String outGaDen;
    private String outGioDi;
    private String outGioDen;
    private String outViTriGhe;
    private String outGiaVe;
    private String outTrangThaiVe;
    private String outMessage;

    private VeTau veTauTimThay;

    public SearchVeTauFrm() {
    }

    public void actionPerformed(ActionEvent e) {
        if (e == null) return;
        if ("TimKiem".equals(e.getActionCommand())) {
            timKiemVe();
        } else if ("HuyVe".equals(e.getActionCommand())) {
            if (veTauTimThay != null) {
                if (veTauTimThay.getTrangThai() == TrangThaiVe.DA_HUY || veTauTimThay.getTrangThai() == TrangThaiVe.DA_TRA) {
                    this.outMessage = "Vé đã được làm thủ tục trả/hủy từ trước, không thể thao tác tiếp.";
                } else {
                    moManHinhTraVe(veTauTimThay);
                }
            } else {
                this.outMessage = "Nút Hủy vé không khả dụng do chưa chọn vé.";
            }
        }
    }

    public void timKiemVe() {
        if (txtMaVe == null || txtMaVe.trim().isEmpty()) {
            this.outMessage = "Mã vé không được để trống.";
            return;
        }

        Connection con = DBConnection.getConnection();
        VeTauDAO dao = new VeTauDAO(con);
        VeTau ve = dao.searchVeTau(txtMaVe);

        if (ve != null) {
            this.veTauTimThay = ve;
            this.outMaVe = ve.getMaVe();
            this.outGiaVe = String.valueOf(ve.getGiaVe());
            this.outTrangThaiVe = ve.getTrangThai().name();

            if (ve.getKhachHang() != null) {
                this.outHoTenKhach = ve.getKhachHang().getHoTen();
                this.outSoCCCD = ve.getKhachHang().getSoCCCD();
            } else {
                this.outHoTenKhach = "";
                this.outSoCCCD = "";
            }

            if (ve.getGheNgoi() != null) {
                this.outViTriGhe = "Số " + ve.getGheNgoi().getSoGhe() + " - " + ve.getGheNgoi().getViTri();
            } else {
                this.outViTriGhe = "";
            }

            if (ve.getLichTrinh() != null) {
                this.outTenChuyenTau = ve.getLichTrinh().getDoanTau() != null ? ve.getLichTrinh().getDoanTau().getTenTau() : "";
                this.outGioDi = ve.getLichTrinh().getNgayKhoiHanh().toString();
                if (ve.getLichTrinh().getHanhTrinh() != null) {
                    this.outGaDi = ve.getLichTrinh().getHanhTrinh().getGaDau() != null ? ve.getLichTrinh().getHanhTrinh().getGaDau().getTenNhaGa() : "";
                    this.outGaDen = ve.getLichTrinh().getHanhTrinh().getGaCuoi() != null ? ve.getLichTrinh().getHanhTrinh().getGaCuoi().getTenNhaGa() : "";
                }
            } else {
                this.outTenChuyenTau = "";
                this.outGioDi = "";
                this.outGaDi = "";
                this.outGaDen = "";
            }

            if (ve.getTrangThai() == TrangThaiVe.DA_HUY || ve.getTrangThai() == TrangThaiVe.DA_TRA) {
                this.outMessage = "Vé đã được làm thủ tục trả/hủy từ trước, không thể thao tác tiếp.";
            } else {
                this.outMessage = "Tìm thấy vé thành công.";
            }
        } else {
            this.veTauTimThay = null;
            clearOutputs();
            this.outMessage = "Không tìm thấy vé phù hợp với mã " + txtMaVe;
        }
    }

    public TraVeFrm moManHinhTraVe(VeTau ve) {
        return new TraVeFrm(ve);
    }

    private void clearOutputs() {
        this.outMaVe = "";
        this.outHoTenKhach = "";
        this.outSoCCCD = "";
        this.outTenChuyenTau = "";
        this.outGaDi = "";
        this.outGaDen = "";
        this.outGioDi = "";
        this.outViTriGhe = "";
        this.outGiaVe = "";
        this.outTrangThaiVe = "";
    }

    // Getters and Setters
    public String getTxtMaVe() { return txtMaVe; }
    public void setTxtMaVe(String txtMaVe) { this.txtMaVe = txtMaVe; }
    public String getOutMaVe() { return outMaVe; }
    public String getOutHoTenKhach() { return outHoTenKhach; }
    public String getOutSoCCCD() { return outSoCCCD; }
    public String getOutTenChuyenTau() { return outTenChuyenTau; }
    public String getOutGaDi() { return outGaDi; }
    public String getOutGaDen() { return outGaDen; }
    public String getOutGioDi() { return outGioDi; }
    public String getOutViTriGhe() { return outViTriGhe; }
    public String getOutGiaVe() { return outGiaVe; }
    public String getOutTrangThaiVe() { return outTrangThaiVe; }
    public String getOutMessage() { return outMessage; }
    public VeTau getVeTauTimThay() { return veTauTimThay; }
}
