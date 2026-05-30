package com.example.manager;

import com.example.manager.dao.DBConnection;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        LocalDate ngayBD = LocalDate.of(2026, 6, 1);
        LocalDate ngayKT = LocalDate.of(2026, 6, 2);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.err.println("Khong the ket noi CSDL. Kiem tra DBConnection va JDBC driver.");
                return;
            }

            BaoCao baoCao = BaoCao.taoMoiBaoCao(ngayBD, ngayKT);
            List<ChiTietBaoCao> chiTietList = thongKeDoanhThu(con, ngayBD, ngayKT);

            baoCao.luuKetQuaThongKe("BC-001", chiTietList);
            baoCao.tinhDoanhThu();
            baoCao.sapXepDoanhThuGiamDan();

            inBaoCao(baoCao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<ChiTietBaoCao> thongKeDoanhThu(Connection con, LocalDate ngayBD, LocalDate ngayKT) {
        DoanTauDAO doanTauDAO = new DoanTauDAO(con);
        LichTrinhDAO lichTrinhDAO = new LichTrinhDAO(con);
        VeTauDAO veTauDAO = new VeTauDAO(con);

        List<ChiTietBaoCao> chiTietList = new ArrayList<>();
        int stt = 1;

        for (DoanTau tau : doanTauDAO.layDanhSachDoanTau()) {
            List<LichTrinh> lichTrinhList = lichTrinhDAO.layDanhSachLichTrinhTrongKy(
                tau.getMaTau(), ngayBD, ngayKT
            );
            for (LichTrinh lichTrinh : lichTrinhList) {
                if (lichTrinh.getTrangThai() == TrangThaiLichTrinh.HUY) {
                    continue;
                }

                int soVeBan = 0;
                int doanhThu = 0;
                List<VeTau> veDaBan = veTauDAO.layDanhSachVeTheoLichTrinh(lichTrinh.getMaLichTrinh());
                for (VeTau ve : veDaBan) {
                    if (ve.getTrangThai() == TrangThaiVe.DA_BAN) {
                        soVeBan++;
                        doanhThu += ve.getGiaVe();
                    }
                }

                int sucChua = tinhSucChua(tau);
                double tiLeLapDay = sucChua == 0 ? 0.0 : (soVeBan * 100.0) / sucChua;

                ChiTietBaoCao chiTiet = new ChiTietBaoCao(
                    "CTBC-" + stt,
                    soVeBan,
                    doanhThu,
                    tiLeLapDay,
                    lichTrinh
                );
                chiTietList.add(chiTiet);
                stt++;
            }
        }

        return chiTietList;
    }

    private static int tinhSucChua(DoanTau doanTau) {
        int tong = 0;
        for (ToaTau toa : doanTau.getToaTau()) {
            tong += toa.getSoLuongGheToiDa();
        }
        return tong;
    }

    private static void inBaoCao(BaoCao baoCao) {
        System.out.println("=== THONG KE CHUYEN TAU THEO DOANH THU ===");
        System.out.println("Tong doanh thu: " + baoCao.getTongDoanhThu());
        System.out.println("Ma LT | Ten tau | Ga dau | Ga cuoi | Ngay khoi hanh | So ve | Ti le | Doanh thu");

        for (ChiTietBaoCao ct : baoCao.getChiTietBaoCao()) {
            LichTrinh lt = ct.getLichTrinh();
            String tenTau = lt.getDoanTau() == null ? "" : lt.getDoanTau().getTenTau();
            NhaGa gaDau = lt.getHanhTrinh() == null ? null : lt.getHanhTrinh().getGaDau();
            NhaGa gaCuoi = lt.getHanhTrinh() == null ? null : lt.getHanhTrinh().getGaCuoi();

            String tenGaDau = gaDau == null ? "" : gaDau.getTenNhaGa();
            String tenGaCuoi = gaCuoi == null ? "" : gaCuoi.getTenNhaGa();

            System.out.printf("%s | %s | %s | %s | %s | %d | %.1f%% | %d%n",
                lt.getMaLichTrinh(),
                tenTau,
                tenGaDau,
                tenGaCuoi,
                lt.getNgayKhoiHanh() == null ? "" : lt.getNgayKhoiHanh().toLocalDate(),
                ct.getSoVeBan(),
                ct.getTiLeLapDay(),
                ct.getDoanhThuChuyen()
            );
        }
    }
}
