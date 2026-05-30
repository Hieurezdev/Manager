package com.example.manager;

import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.KhachHang;
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

public class Demo {
    public static void main(String[] args) {
        LocalDate ngayBD = LocalDate.of(2026, 5, 1);
        LocalDate ngayKT = LocalDate.of(2026, 5, 31);

        List<LichTrinh> lichTrinhList = taoMockLichTrinh();
        BaoCao baoCao = BaoCao.taoMoiBaoCao(ngayBD, ngayKT);

        List<ChiTietBaoCao> chiTietList = new ArrayList<>();
        int stt = 1;

        for (LichTrinh lichTrinh : lichTrinhList) {
            if (!isTrongKy(lichTrinh, ngayBD, ngayKT)) {
                continue;
            }
            if (lichTrinh.getTrangThai() != TrangThaiLichTrinh.HOAN_THANH) {
                continue;
            }

            int soVeBan = 0;
            int doanhThu = 0;
            for (VeTau ve : lichTrinh.getVeTau()) {
                if (ve.getTrangThai() == TrangThaiVe.DA_BAN) {
                    soVeBan++;
                    doanhThu += ve.getGiaVe();
                }
            }

            int sucChua = tinhSucChua(lichTrinh.getDoanTau());
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

        baoCao.luuKetQuaThongKe("BC-001", chiTietList);
        baoCao.tinhDoanhThu();
        baoCao.sapXepDoanhThuGiamDan();

        inBaoCao(baoCao);
    }

    private static boolean isTrongKy(LichTrinh lichTrinh, LocalDate ngayBD, LocalDate ngayKT) {
        LocalDate ngayKhoiHanh = lichTrinh.getNgayKhoiHanh().toLocalDate();
        return !ngayKhoiHanh.isBefore(ngayBD) && !ngayKhoiHanh.isAfter(ngayKT);
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
                    lt.getNgayKhoiHanh().toLocalDate(),
                    ct.getSoVeBan(),
                    ct.getTiLeLapDay(),
                    ct.getDoanhThuChuyen()
            );
        }
    }

    private static List<LichTrinh> taoMockLichTrinh() {
        NhaGa haNoi = new NhaGa("GA-HN", "Ha Noi", null, "024-000000");
        NhaGa saiGon = new NhaGa("GA-SG", "Sai Gon", null, "028-000000");
        NhaGa vinh = new NhaGa("GA-VI", "Vinh", null, "0238-000000");

        List<ChiTietHanhTrinh> ctHt1 = new ArrayList<>();
        ctHt1.add(new ChiTietHanhTrinh("CTHT-1", 1, haNoi));
        ctHt1.add(new ChiTietHanhTrinh("CTHT-2", 2, saiGon));
        HanhTrinh ht1 = new HanhTrinh("HT-01", "Ha Noi - Sai Gon", 1726.0, ctHt1);

        List<ChiTietHanhTrinh> ctHt2 = new ArrayList<>();
        ctHt2.add(new ChiTietHanhTrinh("CTHT-3", 1, haNoi));
        ctHt2.add(new ChiTietHanhTrinh("CTHT-4", 2, vinh));
        HanhTrinh ht2 = new HanhTrinh("HT-02", "Ha Noi - Vinh", 319.0, ctHt2);

        DoanTau tau1 = new DoanTau("SE1", "Thong Nhat 1", LoaiTau.THONG_NHAT, TrangThaiTau.HOAT_DONG, null);
        tau1.addToaTau(new ToaTau("T1", "Toa 1", 1, LoaiToa.NGOI_MEM, 200, ""));
        tau1.addToaTau(new ToaTau("T2", "Toa 2", 2, LoaiToa.NGOI_MEM, 200, ""));

        DoanTau tau2 = new DoanTau("NA1", "Tau Vinh", LoaiTau.DIA_PHUONG, TrangThaiTau.HOAT_DONG, null);
        tau2.addToaTau(new ToaTau("T3", "Toa 3", 1, LoaiToa.NGOI_CUNG, 160, ""));
        tau2.addToaTau(new ToaTau("T4", "Toa 4", 2, LoaiToa.NGOI_CUNG, 160, ""));

        LichTrinh lt1 = new LichTrinh("LT-SE1-01", LocalDateTime.of(2026, 5, 20, 8, 0),
                TrangThaiLichTrinh.HOAN_THANH, ht1, tau1);
        LichTrinh lt2 = new LichTrinh("LT-SE1-02", LocalDateTime.of(2026, 5, 21, 9, 0),
                TrangThaiLichTrinh.HOAN_THANH, ht1, tau1);
        LichTrinh lt3 = new LichTrinh("LT-NA1-01", LocalDateTime.of(2026, 5, 22, 7, 30),
                TrangThaiLichTrinh.HOAN_THANH, ht2, tau2);

        KhachHang kh1 = new KhachHang("KH01", "Nguyen A", "0123456789", "0900000001", "a@example.com");
        KhachHang kh2 = new KhachHang("KH02", "Tran B", "0123456790", "0900000002", "b@example.com");

        for (int i = 0; i < 220; i++) {
            lt1.addVeTau(new VeTau("VE-SE1-" + i, LoaiDoiTuong.NGUOI_LON, 1200000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh1));
        }
        for (int i = 0; i < 180; i++) {
            lt2.addVeTau(new VeTau("VE-SE2-" + i, LoaiDoiTuong.NGUOI_LON, 1100000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh2));
        }
        for (int i = 0; i < 140; i++) {
            lt3.addVeTau(new VeTau("VE-NA1-" + i, LoaiDoiTuong.NGUOI_LON, 400000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh1));
        }

        List<LichTrinh> list = new ArrayList<>();
        list.add(lt1);
        list.add(lt2);
        list.add(lt3);
        return list;
    }
}
