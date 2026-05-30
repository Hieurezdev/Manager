package com.example.manager;

import com.example.manager.dao.BaoCaoDAO;
import com.example.manager.dao.DBConnection;
import com.example.manager.dao.DoanTauDAO;
import com.example.manager.dao.GheNgoiDAO;
import com.example.manager.dao.HanhTrinhDAO;
import com.example.manager.dao.HoaDonDAO;
import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.dao.NhaGaDAO;
import com.example.manager.dao.PhieuTraVeDAO;
import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.VeTau;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        System.out.println("=== DAO SMOKE TEST ===");
        Connection con = DBConnection.getConnection();
        if (con == null) {
            System.err.println("[FAIL] DBConnection.getConnection() returned null");
            return;
        }

        HanhTrinhDAO hanhTrinhDAO = new HanhTrinhDAO(con);
        NhaGaDAO nhaGaDAO = new NhaGaDAO(con);
        DoanTauDAO doanTauDAO = new DoanTauDAO(con);
        LichTrinhDAO lichTrinhDAO = new LichTrinhDAO(con);
        VeTauDAO veTauDAO = new VeTauDAO(con);
        GheNgoiDAO gheNgoiDAO = new GheNgoiDAO(con);
        HoaDonDAO hoaDonDAO = new HoaDonDAO(con);
        PhieuTraVeDAO phieuTraVeDAO = new PhieuTraVeDAO(con);
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO(con);

        List<HanhTrinh> hanhTrinhList = hanhTrinhDAO.getAllHanhTrinh();
        System.out.println("HanhTrinh count: " + hanhTrinhList.size());
        if (!hanhTrinhList.isEmpty()) {
            String maHanhTrinh = hanhTrinhList.get(0).getMaHanhTrinh();
            HanhTrinh detail = hanhTrinhDAO.layThongTinHanhTrinh(maHanhTrinh);
            int ctSize = detail == null ? 0 : detail.getChiTietHanhTrinh().size();
            System.out.println("HanhTrinh detail: " + maHanhTrinh + " (chiTiet=" + ctSize + ")");
            System.out.println("GaTrungGian count: " + hanhTrinhDAO.getGaTrungGian(maHanhTrinh).size());
        }

        List<DoanTau> tauList = doanTauDAO.layDanhSachDoanTau();
        System.out.println("DoanTau count: " + tauList.size());

        List<LichTrinh> lichTrinhList = lichTrinhDAO.layDanhSachLichTrinhTrongKy(
            "SE1",
            LocalDate.of(2026, 6, 1),
            LocalDate.of(2026, 6, 2)
        );
        System.out.println("LichTrinh count: " + lichTrinhList.size());
        LichTrinh latest = lichTrinhDAO.layThongTinLichTrinh("SE1");
        System.out.println("LichTrinh latest: " + (latest == null ? "null" : latest.getMaLichTrinh()));

        List<VeTau> veList = veTauDAO.layDanhSachVeTheoLichTrinh("LT_SE1_20260601");
        System.out.println("VeTau count (LT_SE1_20260601): " + veList.size());
        VeTau ve = veTauDAO.searchVeTau("VE_0001");
        System.out.println("VeTau search VE_0001: " + (ve == null ? "null" : ve.getMaVe()));

        if (ve != null) {
            int phat = phieuTraVeDAO.tinhTienPhat(ve);
            System.out.println("PhieuTraVe tinhTienPhat: " + phat);
        }

        System.out.println("Skipped write tests for HoaDonDAO, GheNgoiDAO, VeTauDAO update, BaoCaoDAO to avoid DB mutations.");
        System.out.println("=== DONE ===");
    }
}
