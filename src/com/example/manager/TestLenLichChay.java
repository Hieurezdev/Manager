package com.example.manager;

import com.example.manager.view.ManHinhLenLichChayView;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.TrangThaiTau;
import java.util.ArrayList;

public class TestLenLichChay {
    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU TEST MODULE LÊN LỊCH CHẠY ===");

        // 1. Khởi tạo màn hình (View)
        ManHinhLenLichChayView view = new ManHinhLenLichChayView();

        // Gọi hàm giả lập việc load dữ liệu ban đầu lên View
        view.call();
        System.out.println("[View] Đã load danh sách tàu ban đầu.");

        // 2. Tạo dữ liệu giả (Mock Data) để giả lập người dùng chọn
        DoanTau tauDuocChon = new DoanTau("SE1", "Tàu SE1", LoaiTau.THONG_NHAT, TrangThaiTau.HOAT_DONG, new ArrayList<>());
        
        NhaGa gaDi = new NhaGa("GA_HANOI", "Ga Hà Nội", "", "02439423697");
        NhaGa gaDen = new NhaGa("GA_SAIGON", "Ga Sài Gòn", "", "02838436528");
        HanhTrinh hanhTrinhDuocChon = new HanhTrinh("HT_HNSG", "Tuyến Bắc Nam - Hà Nội Sài Gòn", 1726.0, new ArrayList<>());

        // 3. Mô phỏng hành động người dùng tương tác trên View
        System.out.println("\n--- Người dùng chọn Tàu: " + tauDuocChon.getTenTau() + " ---");
        view.actionPerformed("ChonTau", tauDuocChon);

        System.out.println("--- Người dùng chọn Hành trình: " + hanhTrinhDuocChon.getMaHanhTrinh() + " ---");
        view.actionPerformed("ChonHanhTrinh", hanhTrinhDuocChon);

        // 4. Mô phỏng người dùng nhấn nút Xác nhận lưu
        System.out.println("\n--- Người dùng nhấn nút Xác nhận Lưu Lịch Trình ---");
        view.actionPerformed("XacNhan", null);

        System.out.println("\n=== KẾT THÚC TEST ===");
    }
}
