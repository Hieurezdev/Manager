package com.example.manager;

import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.entity.ToaTau;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.LoaiToa;
import com.example.manager.enums.TrangThaiLichTrinh;
import com.example.manager.enums.TrangThaiTau;
import com.example.manager.enums.TrangThaiVe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        testBaoCaoTinhDoanhThuVaSapXep();
        testHanhTrinhGaDauGaCuoi();
        testHanhTrinhMaGaDauGaCuoi();
        System.out.println("All tests passed.");
    }

    private static void testBaoCaoTinhDoanhThuVaSapXep() {
        LocalDate ngayBD = LocalDate.of(2026, 5, 1);
        LocalDate ngayKT = LocalDate.of(2026, 5, 31);

        LichTrinh lt1 = taoLichTrinh("LT-1", 200, 1000000);
        LichTrinh lt2 = taoLichTrinh("LT-2", 100, 500000);

        List<ChiTietBaoCao> chiTietList = new ArrayList<>();
        chiTietList.add(new ChiTietBaoCao("CT1", 200, 200000000, 50.0, lt1));
        chiTietList.add(new ChiTietBaoCao("CT2", 100, 50000000, 25.0, lt2));

        BaoCao baoCao = BaoCao.taoMoiBaoCao(ngayBD, ngayKT);
        baoCao.luuKetQuaThongKe("BC-TEST", chiTietList);
        baoCao.tinhDoanhThu();
        baoCao.sapXepDoanhThuGiamDan();

        expect(baoCao.getTongDoanhThu() == 250000000, "Tong doanh thu khong dung");
        expect(baoCao.getChiTietBaoCao().get(0).getLichTrinh().getMaLichTrinh().equals("LT-1"),
                "Sap xep doanh thu giam dan bi sai");
    }

    private static void testHanhTrinhGaDauGaCuoi() {
        NhaGa ga1 = new NhaGa("GA-1", "Ga 1", null, "000");
        NhaGa ga2 = new NhaGa("GA-2", "Ga 2", null, "000");
        NhaGa ga3 = new NhaGa("GA-3", "Ga 3", null, "000");

        List<ChiTietHanhTrinh> ct = new ArrayList<>();
        ct.add(new ChiTietHanhTrinh("CT-1", 2, ga2));
        ct.add(new ChiTietHanhTrinh("CT-2", 1, ga1));
        ct.add(new ChiTietHanhTrinh("CT-3", 3, ga3));

        HanhTrinh hanhTrinh = new HanhTrinh("HT-1", "HT", 10.0, ct);
        expect("Ga 1".equals(hanhTrinh.getGaDau().getTenNhaGa()), "Ga dau khong dung");
        expect("Ga 3".equals(hanhTrinh.getGaCuoi().getTenNhaGa()), "Ga cuoi khong dung");
    }

    private static void testHanhTrinhMaGaDauGaCuoi() {
        NhaGa ga1 = new NhaGa("GA-START", "Ga start", null, "000");
        NhaGa ga2 = new NhaGa("GA-MID", "Ga mid", null, "000");
        NhaGa ga3 = new NhaGa("GA-END", "Ga end", null, "000");

        List<ChiTietHanhTrinh> ct = new ArrayList<>();
        ct.add(new ChiTietHanhTrinh("CT-1", 3, ga3));
        ct.add(new ChiTietHanhTrinh("CT-2", 1, ga1));
        ct.add(new ChiTietHanhTrinh("CT-3", 2, ga2));

        HanhTrinh hanhTrinh = new HanhTrinh("HT-2", "HT 2", 20.0, ct);

        expect("GA-START".equals(hanhTrinh.getGaDau().getMaGa()), "Ma ga dau khong dung");
        expect("GA-END".equals(hanhTrinh.getGaCuoi().getMaGa()), "Ma ga cuoi khong dung");
    }

    private static LichTrinh taoLichTrinh(String ma, int soVe, int giaVe) {
        NhaGa ga1 = new NhaGa("GA-1", "Ga 1", null, "000");
        NhaGa ga2 = new NhaGa("GA-2", "Ga 2", null, "000");
        List<ChiTietHanhTrinh> ct = new ArrayList<>();
        ct.add(new ChiTietHanhTrinh("CT-1", 1, ga1));
        ct.add(new ChiTietHanhTrinh("CT-2", 2, ga2));

        HanhTrinh hanhTrinh = new HanhTrinh("HT-1", "HT", 10.0, ct);

        DoanTau tau = new DoanTau("T1", "Tau 1", LoaiTau.THONG_NHAT, TrangThaiTau.HOAT_DONG, null);
        tau.addToaTau(new ToaTau("TOA-1", "Toa 1", 1, LoaiToa.NGOI_MEM, 400, ""));

        LichTrinh lichTrinh = new LichTrinh(ma, LocalDateTime.of(2026, 5, 10, 8, 0),
                TrangThaiLichTrinh.HOAN_THANH, hanhTrinh, tau);

        for (int i = 0; i < soVe; i++) {
            lichTrinh.addVeTau(new VeTau("VE-" + i, LoaiDoiTuong.NGUOI_LON, giaVe,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, null));
        }
        return lichTrinh;
    }

    private static void expect(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
