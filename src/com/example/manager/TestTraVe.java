package com.example.manager;

import com.example.manager.boundary.SearchVeTauFrm;
import com.example.manager.boundary.TraVeFrm;
import com.example.manager.dao.DBConnection;
import com.example.manager.dao.PhieuTraVeDAO;
import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.*;
import com.example.manager.enums.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestTraVe {

    public static void main(String[] args) {
        System.out.println("=== BAT DAU KIEM THU MO-DUL TRA VE ===");

        Connection con = DBConnection.getConnection();
        if (con != null) {
            System.out.println("-> Phat hien CSDL MySQL active. Tu dong thiet lap du lieu test CSDL...");
            setupRealDatabase(con);
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("-> CSDL offline. Tu dong nap du lieu Mock trong bo nho...");
            initMockDatabase();
        }

        try {
            testTraVeThanhCongTren24h();
            testTraVeThanhCongDuoi24hTren4h();
            testNgoaiLeVeKhongTonTai();
            testNgoaiLeVeDaHuyTruocDo();
            testNgoaiLeVeQuaHanHoanTra();

            System.out.println("=== DAT: TAT CA CAC CA KIEM THU TRA VE DEU DA VUOT QUA (ALL TESTS PASSED) ===");
        } finally {
            Connection cleanCon = DBConnection.getConnection();
            if (cleanCon != null) {
                System.out.println("-> Don dep du lieu test tren CSDL MySQL...");
                cleanupRealDatabase(cleanCon);
                try {
                    cleanCon.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setupRealDatabase(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("SET SESSION sql_mode = 'ANSI_QUOTES'");

            // Xóa sạch dữ liệu test để tránh trùng lặp
            stmt.executeUpdate("DELETE FROM PhieuTraVe WHERE maPhieuTra IN ('PTV_TEST_10H', 'PTV_TEST_2H')");
            stmt.executeUpdate("DELETE FROM PhieuTraVe WHERE veTauId = (SELECT id FROM VeTau WHERE maVe = 'VE_0002')");
            stmt.executeUpdate("DELETE FROM HoaDon WHERE maHoaDon LIKE 'HD-%'");
            stmt.executeUpdate("DELETE FROM VeTau WHERE maVe IN ('VE_TEST_10H', 'VE_TEST_2H')");
            stmt.executeUpdate("DELETE FROM LichTrinh WHERE maLichTrinh IN ('LT_TEST_10H', 'LT_TEST_2H')");
            stmt.executeUpdate("DELETE FROM GheNgoi WHERE maGhe IN ('G_TEST_10', 'G_TEST_20')");

            // Reset vé VE_0002 và ghế số 5 (SE1_T2_G01) của dữ liệu mẫu
            stmt.executeUpdate("UPDATE VeTau SET trangThai = 'DaBan' WHERE maVe = 'VE_0002'");
            stmt.executeUpdate("UPDATE GheNgoi SET trangThai = 'DaDat' WHERE id = 5");

            // Chèn Ghế Ngồi mẫu
            stmt.executeUpdate("INSERT INTO GheNgoi (maGhe, soGhe, viTri, trangThai, toaTauId) VALUES "
                    + "('G_TEST_10', 10, 'CuaSo', 'DaDat', 1), "
                    + "('G_TEST_20', 20, 'CuaSo', 'DaDat', 1)");

            // Chèn Lịch trình mẫu (DATE_ADD relative to NOW())
            stmt.executeUpdate("INSERT INTO LichTrinh (maLichTrinh, ngayKhoiHanh, trangThai, doanTauId, hanhTrinhId, quanLyId) VALUES "
                    + "('LT_TEST_10H', DATE_ADD(NOW(), INTERVAL 10 HOUR), 'ChuaChay', 1, 1, 1), "
                    + "('LT_TEST_2H', DATE_ADD(NOW(), INTERVAL 2 HOUR), 'ChuaChay', 1, 1, 1)");

            // Chèn Vé Tàu mẫu
            stmt.executeUpdate("INSERT INTO VeTau (maVe, loaiDoiTuong, giaVe, trangThai, thoiDiemBanVe, lichTrinhId, nhanVienId, khachHangId, gheNgoiId, chinhSachGiaId, hoaDonId) VALUES "
                    + "('VE_TEST_10H', 'NguoiLon', 500000, 'DaBan', DATE_SUB(NOW(), INTERVAL 1 DAY), (SELECT id FROM LichTrinh WHERE maLichTrinh='LT_TEST_10H'), 2, 1, (SELECT id FROM GheNgoi WHERE maGhe='G_TEST_10'), 1, 1), "
                    + "('VE_TEST_2H', 'NguoiLon', 500000, 'DaBan', DATE_SUB(NOW(), INTERVAL 1 DAY), (SELECT id FROM LichTrinh WHERE maLichTrinh='LT_TEST_2H'), 2, 1, (SELECT id FROM GheNgoi WHERE maGhe='G_TEST_20'), 1, 1)");

        } catch (SQLException e) {
            System.err.println("Setup real database failed: " + e.getMessage());
        }
    }

    private static void cleanupRealDatabase(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("SET SESSION sql_mode = 'ANSI_QUOTES'");
            stmt.executeUpdate("DELETE FROM PhieuTraVe WHERE maPhieuTra IN ('PTV_TEST_10H', 'PTV_TEST_2H')");
            stmt.executeUpdate("DELETE FROM PhieuTraVe WHERE veTauId = (SELECT id FROM VeTau WHERE maVe = 'VE_0002')");
            stmt.executeUpdate("DELETE FROM HoaDon WHERE maHoaDon LIKE 'HD-%'");
            stmt.executeUpdate("DELETE FROM VeTau WHERE maVe IN ('VE_TEST_10H', 'VE_TEST_2H')");
            stmt.executeUpdate("DELETE FROM LichTrinh WHERE maLichTrinh IN ('LT_TEST_10H', 'LT_TEST_2H')");
            stmt.executeUpdate("DELETE FROM GheNgoi WHERE maGhe IN ('G_TEST_10', 'G_TEST_20')");

            // Reset vé VE_0002 và ghế số 5 về ban đầu
            stmt.executeUpdate("UPDATE VeTau SET trangThai = 'DaBan' WHERE maVe = 'VE_0002'");
            stmt.executeUpdate("UPDATE GheNgoi SET trangThai = 'DaDat' WHERE id = 5");
        } catch (SQLException e) {
            System.err.println("Cleanup real database failed: " + e.getMessage());
        }
    }

    private static void initMockDatabase() {
        VeTauDAO.mockVeTauList.clear();
        PhieuTraVeDAO.mockPhieuTraVeList.clear();
        //HoaDonDAO.mockHoaDonList.clear();

        NhaGa gaHN = new NhaGa("GA-HN", "Ga Ha Noi", "", "024");
        NhaGa gaSG = new NhaGa("GA-SG", "Ga Sai Gon", "", "028");
        List<ChiTietHanhTrinh> ct = new ArrayList<>();
        ct.add(new ChiTietHanhTrinh("CT-1", 1, gaHN));
        ct.add(new ChiTietHanhTrinh("CT-2", 2, gaSG));

        HanhTrinh hanhTrinh = new HanhTrinh("HT-01", "Ha Noi - Sai Gon", 1726.0, ct);
        DoanTau doanTau = new DoanTau("SE3", "Thong Nhat SE3", LoaiTau.THONG_NHAT, TrangThaiTau.HOAT_DONG, null);

        LichTrinh lt30H = new LichTrinh("LT-SE3-30H", LocalDateTime.now().plusHours(30), TrangThaiLichTrinh.HOAN_THANH, hanhTrinh, doanTau);
        LichTrinh lt10H = new LichTrinh("LT-SE3-10H", LocalDateTime.now().plusHours(10), TrangThaiLichTrinh.HOAN_THANH, hanhTrinh, doanTau);
        LichTrinh lt2H = new LichTrinh("LT-SE3-2H", LocalDateTime.now().plusHours(2), TrangThaiLichTrinh.HOAN_THANH, hanhTrinh, doanTau);

        KhachHang kh1 = new KhachHang("KH-001", "Nguyen Van A", "123456789", "0900000001", "a@example.com");
        KhachHang kh2 = new KhachHang("KH-002", "Tran Van B", "987654321", "0900000002", "b@example.com");

        GheNgoi g15 = new GheNgoi();
        g15.setMaGhe("G15");
        g15.setSoGhe(15);
        g15.setViTri("Lối đi");
        g15.setTrangThai(TrangThaiGhe.DA_DAT);

        GheNgoi g16 = new GheNgoi();
        g16.setMaGhe("G16");
        g16.setSoGhe(16);
        g16.setViTri("Cửa sổ");
        g16.setTrangThai(TrangThaiGhe.DA_DAT);

        GheNgoi g17 = new GheNgoi();
        g17.setMaGhe("G17");
        g17.setSoGhe(17);
        g17.setViTri("Lối đi");
        g17.setTrangThai(TrangThaiGhe.DA_DAT);

        GheNgoi g18 = new GheNgoi();
        g18.setMaGhe("G18");
        g18.setSoGhe(18);
        g18.setViTri("Cửa sổ");
        g18.setTrangThai(TrangThaiGhe.DA_DAT);

        VeTau ve30H = new VeTau("VE_0002", LoaiDoiTuong.NGUOI_LON, 800000, TrangThaiVe.DA_BAN, LocalDateTime.now(), g15, kh1, lt30H, null);
        VeTau ve10H = new VeTau("VE_TEST_10H", LoaiDoiTuong.NGUOI_LON, 500000, TrangThaiVe.DA_BAN, LocalDateTime.now(), g16, kh2, lt10H, null);
        VeTau ve2H = new VeTau("VE_TEST_2H", LoaiDoiTuong.NGUOI_LON, 500000, TrangThaiVe.DA_BAN, LocalDateTime.now(), g17, kh1, lt2H, null);
        VeTau veCancelled = new VeTau("VE_0001", LoaiDoiTuong.NGUOI_LON, 450000, TrangThaiVe.DA_TRA, LocalDateTime.now(), g18, kh2, lt30H, null);

        VeTauDAO.mockVeTauList.add(ve30H);
        VeTauDAO.mockVeTauList.add(ve10H);
        VeTauDAO.mockVeTauList.add(ve2H);
        VeTauDAO.mockVeTauList.add(veCancelled);
    }

    private static void testTraVeThanhCongTren24h() {
        System.out.println("Running testTraVeThanhCongTren24h...");

        SearchVeTauFrm searchFrm = new SearchVeTauFrm();
        searchFrm.setTxtMaVe("VE_0002");

        searchFrm.actionPerformed(new ActionEvent(searchFrm, 1001, "TimKiem"));

        expect(searchFrm.getVeTauTimThay() != null, "Lỗi: Không tìm thấy vé VE_0002.");
        expect(searchFrm.getOutHoTenKhach().equals("Phạm Minh Đức"), "Lỗi: Sai họ tên khách.");

        TraVeFrm traVeFrm = searchFrm.moManHinhTraVe(searchFrm.getVeTauTimThay());

        // Kiểm tra tính tiền phạt (phải phạt 10% của 800.000đ = 80.000đ)
        expect(traVeFrm.getTienPhat() == 80000, "Lỗi: Tiền phạt trên 24h phải là 10% (80.000 VNĐ) nhưng nhận được: " + traVeFrm.getTienPhat());
        expect(traVeFrm.getTienHoanLai() == 720000, "Lỗi: Tiền hoàn trả phải là 720.000 VNĐ.");
        expect(traVeFrm.isBtnXacNhanEnabled(), "Lỗi: Nút xác nhận trả vé phải khả dụng.");

        traVeFrm.actionPerformed(new ActionEvent(traVeFrm, 1002, "XacNhan"));

        expect(traVeFrm.getVeTau().getTrangThai() == TrangThaiVe.DA_TRA, "Lỗi: Trạng thái vé chưa cập nhật thành DA_TRA.");
        expect(traVeFrm.getVeTau().getGheNgoi().getTrangThai() == TrangThaiGhe.TRONG, "Lỗi: Trạng thái ghế chưa cập nhật thành TRONG.");

        System.out.println("-> testTraVeThanhCongTren24h PASSED.");
    }

    private static void testTraVeThanhCongDuoi24hTren4h() {
        System.out.println("Running testTraVeThanhCongDuoi24hTren4h...");

        SearchVeTauFrm searchFrm = new SearchVeTauFrm();
        searchFrm.setTxtMaVe("VE_TEST_10H");
        searchFrm.actionPerformed(new ActionEvent(searchFrm, 1001, "TimKiem"));

        expect(searchFrm.getVeTauTimThay() != null, "Lỗi: Không tìm thấy vé VE_TEST_10H.");

        TraVeFrm traVeFrm = searchFrm.moManHinhTraVe(searchFrm.getVeTauTimThay());

        // Kiểm tra tính tiền phạt (phải phạt 20% của 500.000 = 100.000)
        expect(traVeFrm.getTienPhat() == 100000, "Lỗi: Tiền phạt từ 4h-24h phải là 20% (100.000 VNĐ) nhưng nhận được: " + traVeFrm.getTienPhat());
        expect(traVeFrm.getTienHoanLai() == 400000, "Lỗi: Tiền hoàn trả phải là 400.000 VNĐ.");
        expect(traVeFrm.isBtnXacNhanEnabled(), "Lỗi: Nút xác nhận trả vé phải khả dụng.");

        traVeFrm.actionPerformed(new ActionEvent(traVeFrm, 1002, "XacNhan"));

        expect(traVeFrm.getVeTau().getTrangThai() == TrangThaiVe.DA_TRA, "Lỗi: Trạng thái vé chưa cập nhật thành DA_TRA.");
        expect(traVeFrm.getVeTau().getGheNgoi().getTrangThai() == TrangThaiGhe.TRONG, "Lỗi: Trạng thái ghế chưa cập nhật thành TRONG.");

        System.out.println("-> testTraVeThanhCongDuoi24hTren4h PASSED.");
    }

    private static void testNgoaiLeVeKhongTonTai() {
        System.out.println("Running testNgoaiLeVeKhongTonTai...");

        SearchVeTauFrm searchFrm = new SearchVeTauFrm();
        searchFrm.setTxtMaVe("VE-FAKE-CODE");
        searchFrm.actionPerformed(new ActionEvent(searchFrm, 1001, "TimKiem"));

        expect(searchFrm.getVeTauTimThay() == null, "Lỗi: Lẽ ra không tìm thấy vé ảo.");
        expect(searchFrm.getOutMessage().contains("Không tìm thấy vé phù hợp"), "Lỗi: Sai thông báo ngoại lệ không tìm thấy.");

        System.out.println("-> testNgoaiLeVeKhongTonTai PASSED.");
    }

    private static void testNgoaiLeVeDaHuyTruocDo() {
        System.out.println("Running testNgoaiLeVeDaHuyTruocDo...");

        SearchVeTauFrm searchFrm = new SearchVeTauFrm();
        searchFrm.setTxtMaVe("VE_0001");
        searchFrm.actionPerformed(new ActionEvent(searchFrm, 1001, "TimKiem"));

        expect(searchFrm.getVeTauTimThay() != null, "Lỗi: Phải tìm thấy vé VE_0001.");
        expect(searchFrm.getOutMessage().contains("đã được làm thủ tục trả/hủy từ trước"), "Lỗi: Sai thông báo khi vé đã hủy.");

        System.out.println("-> testNgoaiLeVeDaHuyTruocDo PASSED.");
    }

    private static void testNgoaiLeVeQuaHanHoanTra() {
        System.out.println("Running testNgoaiLeVeQuaHanHoanTra...");

        SearchVeTauFrm searchFrm = new SearchVeTauFrm();
        searchFrm.setTxtMaVe("VE_TEST_2H");
        searchFrm.actionPerformed(new ActionEvent(searchFrm, 1001, "TimKiem"));

        expect(searchFrm.getVeTauTimThay() != null, "Lỗi: Phải tìm thấy vé VE_TEST_2H.");

        TraVeFrm traVeFrm = searchFrm.moManHinhTraVe(searchFrm.getVeTauTimThay());

        expect(traVeFrm.getTienPhat() == -1, "Lỗi: Vé quá sát giờ (< 4 tiếng) tiền phạt trả về phải là -1.");
        expect(!traVeFrm.isBtnXacNhanEnabled(), "Lỗi: Nút xác nhận trả vé phải bị khóa.");
        expect(traVeFrm.getOutMessage().contains("Vé đã quá sát giờ tàu chạy, không thể trả vé"), "Lỗi: Sai thông báo chặn hoàn trả.");

        System.out.println("-> testNgoaiLeVeQuaHanHoanTra PASSED.");
    }

    private static void expect(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
