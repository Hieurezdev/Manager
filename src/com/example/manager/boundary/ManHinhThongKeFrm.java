package com.example.manager.boundary;

import com.example.manager.dao.BaoCaoDAO;
import com.example.manager.dao.DoanTauDAO;
import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.entity.ToaTau;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.TrangThaiLichTrinh;
import com.example.manager.enums.TrangThaiVe;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManHinhThongKeFrm {
    private LocalDate ngayBD;
    private LocalDate ngayKT;
    private String btnThongKe;

    private String outMaLichTrinh;
    private String outTenTau;
    private String outGaDau;
    private String outGaCuoi;
    private String outNgayKhoiHanh;
    private String outSoVeBan;
    private String outTiLeLapDay;
    private String outDoanhThuChuyen;

    public ManHinhThongKeFrm() {
    }

    public void actionPerformed(ActionEvent e) {
        thongKe();
    }

    public BaoCao thongKe() {
        Connection con = null;
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO(con);
        if (!baoCaoDAO.kiemTraHopLe(ngayBD, ngayKT)) {
            return null;
        }

        BaoCao baoCao = baoCaoDAO.taoMoiBaoCao(ngayBD, ngayKT);
        List<ChiTietBaoCao> chiTietList = new ArrayList<>();

        DoanTauDAO doanTauDAO = new DoanTauDAO(con);
        LichTrinhDAO lichTrinhDAO = new LichTrinhDAO(con);
        VeTauDAO veTauDAO = new VeTauDAO(con);

        int stt = 1;
        for (DoanTau tau : doanTauDAO.layDanhSachDoanTau()) {
            List<LichTrinh> lichTrinhList = lichTrinhDAO.layDanhSachLichTrinhTrongKy(
                    tau.getMaTau(), ngayBD, ngayKT
            );
            for (LichTrinh lichTrinh : lichTrinhList) {
                if (lichTrinh.getTrangThai() != TrangThaiLichTrinh.HOAN_THANH) {
                    continue;
                }

                List<VeTau> veDaBan = veTauDAO.layDanhSachVeTheoLichTrinh(lichTrinh.getMaLichTrinh());
                int soVeBan = 0;
                int doanhThu = 0;
                for (VeTau ve : veDaBan) {
                    if (ve.getTrangThai() == TrangThaiVe.DA_BAN) {
                        soVeBan++;
                        doanhThu += ve.getGiaVe();
                    }
                }

                int sucChua = tinhSucChua(tau);
                double tiLeLapDay = sucChua == 0 ? 0.0 : (soVeBan * 100.0) / sucChua;

                chiTietList.add(new ChiTietBaoCao(
                        "CTBC-" + stt,
                        soVeBan,
                        doanhThu,
                        tiLeLapDay,
                        lichTrinh
                ));
                stt++;
            }
        }

        String maBaoCao = "BC-" + System.currentTimeMillis();
        baoCao.luuKetQuaThongKe(maBaoCao, chiTietList);
        baoCao.tinhDoanhThu();
        baoCao.sapXepDoanhThuGiamDan();
        baoCaoDAO.luuKetQuaThongKe(maBaoCao, chiTietList);

        capNhatOutput(baoCao);
        return baoCao;
    }

    private void capNhatOutput(BaoCao baoCao) {
        StringBuilder maLichTrinh = new StringBuilder();
        StringBuilder tenTau = new StringBuilder();
        StringBuilder gaDau = new StringBuilder();
        StringBuilder gaCuoi = new StringBuilder();
        StringBuilder ngayKhoiHanh = new StringBuilder();
        StringBuilder soVeBan = new StringBuilder();
        StringBuilder tiLeLapDay = new StringBuilder();
        StringBuilder doanhThuChuyen = new StringBuilder();

        if (baoCao != null) {
            for (ChiTietBaoCao ct : baoCao.getChiTietBaoCao()) {
                LichTrinh lichTrinh = ct.getLichTrinh();
                String tenTauValue = lichTrinh.getDoanTau() == null ? "" : lichTrinh.getDoanTau().getTenTau();
                NhaGa gaDauValue = lichTrinh.getHanhTrinh() == null ? null : lichTrinh.getHanhTrinh().getGaDau();
                NhaGa gaCuoiValue = lichTrinh.getHanhTrinh() == null ? null : lichTrinh.getHanhTrinh().getGaCuoi();

                appendLine(maLichTrinh, lichTrinh.getMaLichTrinh());
                appendLine(tenTau, tenTauValue);
                appendLine(gaDau, gaDauValue == null ? "" : gaDauValue.getTenNhaGa());
                appendLine(gaCuoi, gaCuoiValue == null ? "" : gaCuoiValue.getTenNhaGa());
                appendLine(ngayKhoiHanh, lichTrinh.getNgayKhoiHanh().toLocalDate().toString());
                appendLine(soVeBan, String.valueOf(ct.getSoVeBan()));
                appendLine(tiLeLapDay, String.format("%.1f%%", ct.getTiLeLapDay()));
                appendLine(doanhThuChuyen, String.valueOf(ct.getDoanhThuChuyen()));
            }
        }

        this.outMaLichTrinh = maLichTrinh.toString();
        this.outTenTau = tenTau.toString();
        this.outGaDau = gaDau.toString();
        this.outGaCuoi = gaCuoi.toString();
        this.outNgayKhoiHanh = ngayKhoiHanh.toString();
        this.outSoVeBan = soVeBan.toString();
        this.outTiLeLapDay = tiLeLapDay.toString();
        this.outDoanhThuChuyen = doanhThuChuyen.toString();
    }

    private void appendLine(StringBuilder builder, String value) {
        if (builder.length() > 0) {
            builder.append('\n');
        }
        builder.append(value == null ? "" : value);
    }

    private int tinhSucChua(DoanTau tau) {
        int tong = 0;
        for (ToaTau toa : tau.getToaTau()) {
            tong += toa.getSoLuongGheToiDa();
        }
        return tong;
    }

    public void setNgayBD(LocalDate ngayBD) {
        this.ngayBD = ngayBD;
    }

    public void setNgayKT(LocalDate ngayKT) {
        this.ngayKT = ngayKT;
    }

    public String getOutMaLichTrinh() {
        return outMaLichTrinh;
    }

    public String getOutTenTau() {
        return outTenTau;
    }

    public String getOutGaDau() {
        return outGaDau;
    }

    public String getOutGaCuoi() {
        return outGaCuoi;
    }

    public String getOutNgayKhoiHanh() {
        return outNgayKhoiHanh;
    }

    public String getOutSoVeBan() {
        return outSoVeBan;
    }

    public String getOutTiLeLapDay() {
        return outTiLeLapDay;
    }

    public String getOutDoanhThuChuyen() {
        return outDoanhThuChuyen;
    }
}
