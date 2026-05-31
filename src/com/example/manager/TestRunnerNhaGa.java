package com.example.manager;

import com.example.manager.view.DangNhapFrm;
import com.example.manager.view.QuanLyChiTietGaFrm;
import com.example.manager.view.QuanLyChungFrm;
import com.example.manager.view.QuanLyGaFrm;
import com.example.manager.view.ThemGaFrm;
import com.example.manager.entity.NhaGa;
import java.util.List;

/**
 * Manual test runner for module "Quản lý nhà ga". Follows the same assertion
 * style as TestRunner.java in this codebase. Covers every opt-block in the
 * sequence diagram.
 */
public class TestRunnerNhaGa {

    public static void main(String[] args) {
        testDangNhapHopLe();
        testDangNhapSaiThongTin();
        testMoQuanLyGaFrm();
        testTaiDanhSachGa();
        testTimKiemGaTheoTen();
        testTimKiemGaKhongCo();
        testThemGaMoi();
        testThemGaThieuThongTin();
        testSuaThongTinGa();
        testXoaGa();
        System.out.println("\n=== Tat ca test PASSED ===");
    }

    // -------------------------------------------------------------------------
    // 1. Đăng nhập
    // -------------------------------------------------------------------------
    private static void testDangNhapHopLe() {
        DangNhapFrm frm = new DangNhapFrm();
        frm.setTxtTDN("admin");
        frm.setTxtMK("1234");

        QuanLyChungFrm chungFrm = (QuanLyChungFrm) frm.dangNhap();

        expect(frm.isDangNhapThanhCong(), "Dang nhap hop le phai thanh cong");
        expect(chungFrm != null, "Phai mo QuanLyChungFrm sau khi dang nhap");
        pass("testDangNhapHopLe");
    }

    private static void testDangNhapSaiThongTin() {
        DangNhapFrm frm = new DangNhapFrm();
        frm.setTxtTDN("");
        frm.setTxtMK("");

        QuanLyChungFrm chungFrm = (QuanLyChungFrm) frm.dangNhap();

        expect(!frm.isDangNhapThanhCong(), "Dang nhap thieu thong tin phai that bai");
        expect(chungFrm == null, "Khong duoc mo QuanLyChungFrm khi dang nhap sai");
        pass("testDangNhapSaiThongTin");
    }

    // -------------------------------------------------------------------------
    // 2. Mở QuanLyGaFrm — tải danh sách
    // -------------------------------------------------------------------------
    private static void testMoQuanLyGaFrm() {
        QuanLyChungFrm chungFrm = new QuanLyChungFrm();
        QuanLyGaFrm gaFrm = chungFrm.moManHinhQuanLyGa();

        expect(gaFrm != null, "QuanLyGaFrm phai duoc tao");
        expect(chungFrm.getQuanLyGaFrm() == gaFrm, "Phai luu tham chieu QuanLyGaFrm");
        pass("testMoQuanLyGaFrm");
    }

    private static void testTaiDanhSachGa() {
        QuanLyGaFrm frm = new QuanLyGaFrm();
        List<NhaGa> ds = frm.getTblDSGa();

        expect(ds != null, "Danh sach ga khong duoc null");
        expect(!ds.isEmpty(), "Phai co it nhat 1 nha ga sau khi tai");
        System.out.println("  [INFO] So ga tai duoc: " + ds.size());
        for (NhaGa ga : ds) {
            System.out.printf("         [%s] %s — %s — %s%n",
                    ga.getMaGa(), ga.getTenNhaGa(), ga.getDiaChi(), ga.getSoDienThoai());
        }
        pass("testTaiDanhSachGa");
    }

    // -------------------------------------------------------------------------
    // 3. Tìm kiếm
    // -------------------------------------------------------------------------
    private static void testTimKiemGaTheoTen() {
        QuanLyGaFrm frm = new QuanLyGaFrm();
        frm.setTxtTuKhoa("Hà Nội");
        frm.timKiemGa();

        List<NhaGa> ketQua = frm.getTblDSGa();
        expect(!ketQua.isEmpty(), "Tim kiem 'Ha Noi' phai co ket qua");
        expect(ketQua.get(0).getTenNhaGa().contains("Hà Nội"), "Ket qua phai chua 'Ha Noi'");
        pass("testTimKiemGaTheoTen");
    }

    private static void testTimKiemGaKhongCo() {
        QuanLyGaFrm frm = new QuanLyGaFrm();
        frm.setTxtTuKhoa("XYZ_KHONG_TON_TAI");
        frm.timKiemGa();

        expect(frm.getTblDSGa().isEmpty(), "Tim kiem tu khoa khong ton tai phai tra ve rong");
        pass("testTimKiemGaKhongCo");
    }

    // -------------------------------------------------------------------------
    // 4. Thêm nhà ga mới
    // -------------------------------------------------------------------------
    private static void testThemGaMoi() {
        QuanLyGaFrm gaFrm = new QuanLyGaFrm();
        int soBanDau = gaFrm.getTblDSGa().size();

        ThemGaFrm themFrm = gaFrm.moThemGaFrm();
        themFrm.setTxtTenNhaGa("Ga Vinh");
        themFrm.setTxtDiaChi("Nghe An");
        themFrm.setTxtSoDienThoai("0238-111222");
        NhaGa gaVuaThem = themFrm.luu();

        expect(gaVuaThem != null, "Them ga moi phai tra ve NhaGa");
        expect("Ga Vinh".equals(gaVuaThem.getTenNhaGa()), "Ten nha ga phai khop");
        expect("Nghe An".equals(gaVuaThem.getDiaChi()), "Dia chi phai khop");
        expect(themFrm.getThongBao().contains("thành công"), "Phai co thong bao thanh cong");

        gaFrm.taiDanhSachGa();
        expect(gaFrm.getTblDSGa().size() == soBanDau + 1, "Danh sach phai tang them 1");
        pass("testThemGaMoi");
    }

    private static void testThemGaThieuThongTin() {
        QuanLyGaFrm gaFrm = new QuanLyGaFrm();
        ThemGaFrm themFrm = gaFrm.moThemGaFrm();
        themFrm.setTxtTenNhaGa("");
        themFrm.setTxtDiaChi("");
        themFrm.setTxtSoDienThoai("");
        NhaGa ketQua = themFrm.luu();

        expect(ketQua == null, "Them ga thieu thong tin phai that bai");
        expect(themFrm.getThongBao() != null, "Phai co thong bao loi");
        pass("testThemGaThieuThongTin");
    }

    // -------------------------------------------------------------------------
    // 5. Sửa thông tin nhà ga
    // -------------------------------------------------------------------------
    private static void testSuaThongTinGa() {
        QuanLyGaFrm gaFrm = new QuanLyGaFrm();
        QuanLyChiTietGaFrm chiTietFrm = gaFrm.xemChiTietGa(0);

        expect(chiTietFrm != null, "Phai mo QuanLyChiTietGaFrm");

        chiTietFrm.setTxtTenNhaGa("Ga Ha Noi (Cap Nhat)");
        chiTietFrm.setTxtDiaChi("120 Le Duan, Ha Noi");
        chiTietFrm.setTxtSoDienThoai("024-99999999");
        boolean ok = chiTietFrm.luu();

        expect(ok, "Cap nhat ga phai thanh cong");
        expect(chiTietFrm.isDaCapNhat(), "isDaCapNhat phai la true");
        expect(chiTietFrm.getThongBao().contains("thành công"), "Phai co thong bao cap nhat thanh cong");

        // Verify update reflected in parent list
        NhaGa gaCapNhat = gaFrm.getTblDSGa().get(0);
        expect("Ga Ha Noi (Cap Nhat)".equals(gaCapNhat.getTenNhaGa()), "Ten ga phai duoc cap nhat");
        pass("testSuaThongTinGa");
    }

    // -------------------------------------------------------------------------
    // 6. Xóa nhà ga
    // -------------------------------------------------------------------------
    private static void testXoaGa() {
        QuanLyGaFrm gaFrm = new QuanLyGaFrm();
        int soBanDau = gaFrm.getTblDSGa().size();

        QuanLyChiTietGaFrm chiTietFrm = gaFrm.xemChiTietGa(0);
        boolean ok = chiTietFrm.xoaGa();

        expect(ok, "Xoa ga phai thanh cong");
        expect(chiTietFrm.isDaXoa(), "isDaXoa phai la true");
        expect(chiTietFrm.getThongBao().contains("thành công"), "Phai co thong bao xoa thanh cong");
        expect(gaFrm.getTblDSGa().size() == soBanDau - 1, "Danh sach phai giam di 1");
        pass("testXoaGa");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static void expect(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAIL: " + message);
        }
    }

    private static void pass(String testName) {
        System.out.println("PASS: " + testName);
    }
}
