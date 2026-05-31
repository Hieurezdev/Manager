package com.example.manager.view;

import com.example.manager.dao.DBConnection;
import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.dao.KhachHangDAO;
import com.example.manager.dao.HoaDonDAO;
import com.example.manager.entity.HoaDon;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.TrangThaiGhe;

import java.sql.Connection;
import java.util.List;

public class TestMuaVe {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   BẮT ĐẦU KIỂM THỬ TỰ ĐỘNG MODULE MUA VÉ TÀU     ");
        System.out.println("==================================================");

        // 1. Kiểm thử kết nối Database cục bộ thông qua DBConnection của nhóm
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.err.println("[❌ TEST FAILED] Không thể kết nối Database. Vui lòng kiểm tra file .env hoặc cấu hình SQL Server/MySQL!");
            return;
        }
        System.out.println("[✅ TEST PASSED] Kết nối thành công tới Database cục bộ.");

        try {
            LichTrinhDAO lichTrinhDAO = new LichTrinhDAO(conn);
            KhachHangDAO khachHangDAO = new KhachHangDAO(conn);
            HoaDonDAO hoaDonDAO = new HoaDonDAO(conn);

            // 2. Kiểm thử hàm tìm kiếm chuyến tàu phù hợp
            System.out.println("\n--- [Test Case 1] Tra cứu hành trình chuyến tàu ---");
            // Thay đổi Ga đi, Ga đến, Ngày đi tương ứng với data sẵn có trong DB local của ông để test
            String gaDi = "Ha Noi";
            String gaDen = "Da Nang";
            String ngayDi = "2026-06-01";

            List<LichTrinh> dsChuyen = lichTrinhDAO.layDanhSachChuyenTauPhuHop(gaDi, gaDen, ngayDi);
            if (dsChuyen != null) {
                System.out.println("[✅ TEST PASSED] Hàm layDanhSachChuyenTauPhuHop chạy thành công.");
                System.out.println("=> Tìm thấy " + dsChuyen.size() + " chuyến tàu phù hợp trong Database.");
                for (LichTrinh lt : dsChuyen) {
                    System.out.println("   + Mã lịch trình: " + lt.getMaLichTrinh() + " | Mã tàu: " + lt.getMaTau() + " | Giờ đi: " + lt.getGioDi());
                }
            } else {
                System.out.println("[❌ TEST FAILED] Hàm layDanhSachChuyenTauPhuHop trả về dữ liệu rỗng (null).");
            }

            // 3. Kiểm thử logic tính chính sách giá vé dựa theo đối tượng
            System.out.println("\n--- [Test Case 2] Xử lý chính sách giá và tạo hóa đơn tạm ---");
            if (dsChuyen != null && !dsChuyen.isEmpty()) {
                LichTrinh chuyenChon = dsChuyen.get(0); // Lấy chuyến đầu tiên ra để test gán vé

                // Giả lập dữ liệu hành khách nhập vào quầy bán vé
                String hoTen = "Nguyen Van A";
                String cccd = "001096123456";
                String sdt = "0987654321";
                String email = "khachhang@gmail.com";
                String loaiDoiTuong = LoaiDoiTuong.SINH_VIEN.name(); // Test đối tượng Sinh viên xem có được giảm giá không
                String toaChon = "Toa 1";
                String labelGhe = "12";
                double giaGoc = 850000;

                HoaDon hdTam = khachHangDAO.xuLyChinhSachGiaVaTaoDonTam(hoTen, cccd, sdt, email, loaiDoiTuong, chuyenChon, toaChon, labelGhe, giaGoc);

                if (hdTam != null) {
                    System.out.println("[✅ TEST PASSED] Hàm xuLyChinhSachGiaVaTaoHoaDonTam chạy nuột nà.");
                    System.out.println("   + Mã hóa đơn tạm sinh ra: " + hdTam.getMaHoaDon());
                    System.out.println("   + Giá gốc: " + giaGoc + " VND");
                    System.out.println("   + Tổng tiền sau áp dụng chính sách ưu đãi: " + hdTam.getTongTien() + " VND");
                } else {
                    System.out.println("[❌ TEST FAILED] Hàm xử lý chính sách giá vé trả về null.");
                }

                // 4. Kiểm thử hàm cập nhật trạng thái tạm giữ ghế dưới DB
                System.out.println("\n--- [Test Case 3] Cập nhật giữ chỗ tạm thời cho ghế ---");
                String maGheChon = "T1-G12"; // Mã ghế giả định
                boolean checkGhe = hoaDonDAO.capNhatTrangThaiGhe(maGheChon, TrangThaiGhe.TAM_GIU.name());
                if (checkGhe) {
                    System.out.println("[✅ TEST PASSED] Ghế " + maGheChon + " đã được chuyển sang trạng thái TAM_GIU dưới DB thành công.");
                } else {
                    System.out.println("[⚠️ TEST WARNING] Không tìm thấy mã ghế " + maGheChon + " dưới DB để update trạng thái.");
                }
            } else {
                System.out.println("[⚠️ SKIP TEST] Do không tìm thấy chuyến tàu nào ở Test Case 1 nên bỏ qua luồng test Hóa đơn.");
            }

        } catch (Exception e) {
            System.err.println("[❌ TEST CRASHED] Có lỗi xảy ra trong quá trình chạy kiểm thử mã nguồn:");
            e.printStackTrace();
        } finally {
            System.out.println("\n==================================================");
            System.out.println("       KẾT THÚC TIẾN TRÌNH KIỂM THỬ MODULE        ");
            System.out.println("==================================================");
        }
    }
}
