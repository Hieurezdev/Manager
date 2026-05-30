package com.example.manager.boundary;

import com.example.manager.dao.*;
import com.example.manager.entity.HoaDon;
import com.example.manager.entity.PhieuTraVe;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.TrangThaiGhe;
import com.example.manager.enums.TrangThaiVe;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.time.LocalDateTime;

public class TraVeFrm {
    private VeTau veTau;
    private int tienPhat;
    private int tienHoanLai;

    // Output UI fields
    private String outMaVe;
    private String outGiaVeGoc;
    private String outTienPhat;
    private String outTienHoanLai;
    private String outMessage;
    private boolean btnXacNhanEnabled;

    public TraVeFrm(VeTau ve) {
        this.veTau = ve;
        if (ve == null) {
            this.outMessage = "Dữ liệu vé không hợp lệ.";
            this.btnXacNhanEnabled = false;
            return;
        }

        Connection con = DBConnection.getConnection();
        PhieuTraVeDAO phieuDAO = new PhieuTraVeDAO(con);
        this.tienPhat = phieuDAO.tinhTienPhat(ve);

        this.outMaVe = ve.getMaVe();
        this.outGiaVeGoc = String.valueOf(ve.getGiaVe());

        if (this.tienPhat == -1) {
            this.tienHoanLai = 0;
            this.outTienPhat = "Không thể trả";
            this.outTienHoanLai = "0";
            this.outMessage = "Vé đã quá sát giờ tàu chạy, không thể trả vé hoàn tiền theo đúng quy định.";
            this.btnXacNhanEnabled = false;
        } else {
            this.tienHoanLai = ve.getGiaVe() - this.tienPhat;
            this.outTienPhat = String.valueOf(this.tienPhat);
            this.outTienHoanLai = String.valueOf(this.tienHoanLai);
            this.outMessage = "Hóa đơn phạt sẵn sàng. Hãy bấm Xác nhận để hoàn thành.";
            this.btnXacNhanEnabled = true;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e == null) return;
        if ("XacNhan".equals(e.getActionCommand())) {
            if (btnXacNhanEnabled) {
                xacNhanTraVe();
            } else {
                this.outMessage = "Nghiệp vụ trả vé không khả dụng.";
            }
        }
    }

    public void xacNhanTraVe() {
        Connection con = DBConnection.getConnection();
        VeTauDAO veDAO = new VeTauDAO(con);
        GheNgoiDAO gheDAO = new GheNgoiDAO(con);
        PhieuTraVeDAO phieuDAO = new PhieuTraVeDAO(con);
        HoaDonDAO hdDAO = new HoaDonDAO(con);

        // 1. Cập nhật vé tàu thành "Đã trả" (Khớp 100% với check constraint 'DaTra' của bạn)
        if (veDAO.updateTrangThai(veTau.getMaVe(), TrangThaiVe.DA_TRA)) {
            veTau.setTrangThai(TrangThaiVe.DA_TRA);
        }

        // 2. Giải phóng ghế ngồi về "Trong"
        if (veTau.getGheNgoi() != null) {
            if (gheDAO.updateTrangThai(veTau.getGheNgoi().getMaGhe(), TrangThaiGhe.TRONG)) {
                veTau.getGheNgoi().setTrangThai(TrangThaiGhe.TRONG);
            }
        }

        // 3. Khởi tạo và lưu HoaDon phạt hoàn tiền trước
        HoaDon hd = new HoaDon();
        String maHoaDon = "HD-" + System.currentTimeMillis();
        hd.setMaHoaDon(maHoaDon);
        hd.setTongTien(tienHoanLai);
        hd.setLoaiHoaDon("PhatTraVe");
        hd.setNgayTao(LocalDateTime.now());
        hdDAO.createHoaDon(hd);

        // 4. Khởi tạo và lưu PhieuTraVe liên kết với HoaDon vừa tạo
        PhieuTraVe phieu = new PhieuTraVe();
        phieu.setMaPhieu("PTV-" + System.currentTimeMillis());
        phieu.setVeTau(veTau);
        phieu.setTienPhat(tienPhat);
        phieu.setTienHoanLai(tienHoanLai);
        phieu.setNgayTao(LocalDateTime.now());
        
        hd.setPhieuTraVe(phieu);
        phieuDAO.createPhieuTraVe(phieu, maHoaDon);

        this.outMessage = "Hoàn trả vé thành công. Vui lòng trả lại số tiền thừa " + tienHoanLai + " VNĐ cho khách.";
        this.btnXacNhanEnabled = false;
    }

    // Getters
    public String getOutMaVe() { return outMaVe; }
    public String getOutGiaVeGoc() { return outGiaVeGoc; }
    public String getOutTienPhat() { return outTienPhat; }
    public String getOutTienHoanLai() { return outTienHoanLai; }
    public String getOutMessage() { return outMessage; }
    public boolean isBtnXacNhanEnabled() { return btnXacNhanEnabled; }
    public VeTau getVeTau() { return veTau; }
    public int getTienPhat() { return tienPhat; }
    public int getTienHoanLai() { return tienHoanLai; }
}
