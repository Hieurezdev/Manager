package com.example.manager.boundary;

import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.TrangThaiLichTrinh;

import java.time.LocalDateTime;
import java.util.List;

public class ManHinhLenLichChayView {
    // Các thành phần giao diện theo thiết kế
    private String inDanhSachTau;
    private String outGaTrungGian;
    private String inGioDen;
    private String inGioDi;
    private String subXacNhan;

    // Dữ liệu tạm thời trong quá trình thao tác
    private List<DoanTau> danhSachTau;
    private List<HanhTrinh> danhSachHanhTrinh;
    private List<ChiTietHanhTrinh> danhSachGaTrungGian;
    
    private DoanTau tauDuocChon;
    private HanhTrinh hanhTrinhDuocChon;
    private LichTrinh lichTrinhMoi;

    public ManHinhLenLichChayView() {
    }

    public void call() {
        // Lấy danh sách tàu
        this.danhSachTau = DoanTau.getDanhSachTau();
        // Hiển thị danh sách tàu ra màn hình
        displayDanhSachTau(this.danhSachTau);
    }

    public void actionPerformed(String actionCommand, Object data) {
        if ("ChonTau".equals(actionCommand)) {
            this.tauDuocChon = (DoanTau) data;
            callAfterChonTau();
        } else if ("ChonHanhTrinh".equals(actionCommand)) {
            this.hanhTrinhDuocChon = (HanhTrinh) data;
            callAfterChonHanhTrinh();
        } else if ("XacNhan".equals(actionCommand)) {
            xuLyXacNhanLuu();
        }
    }

    private void callAfterChonTau() {
        // Lấy danh sách hành trình
        // Giả định phương thức HanhTrinh.getAllHanhTrinh() sẽ được tạo sau
        // this.danhSachHanhTrinh = HanhTrinh.getAllHanhTrinh();
        // displayDanhSachHanhTrinh(this.danhSachHanhTrinh);
    }

    private void callAfterChonHanhTrinh() {
        // Lấy danh sách ga trung gian dựa trên hành trình đã chọn
        if (this.hanhTrinhDuocChon != null) {
            this.danhSachGaTrungGian = this.hanhTrinhDuocChon.getDanhSachGaTrungGian();
            displayGaTrungGian(this.danhSachGaTrungGian);
        }
    }

    private void xuLyXacNhanLuu() {
        // Tạo đối tượng LichTrinh mới
        this.lichTrinhMoi = new LichTrinh();
        
        // Gọi call nội bộ để gom thuộc tính
        this.lichTrinhMoi.call(this.hanhTrinhDuocChon, this.tauDuocChon, LocalDateTime.now(), TrangThaiLichTrinh.DANG_CHAY);

        // Vòng lặp cho mỗi ga được chọn (mô phỏng)
        if (this.danhSachGaTrungGian != null) {
            for (ChiTietHanhTrinh ct : this.danhSachGaTrungGian) {
                // Giả lập lấy giờ đến, giờ đi từ giao diện
                LocalDateTime gioDen = LocalDateTime.now().plusHours(1);
                LocalDateTime gioDi = LocalDateTime.now().plusHours(2);
                
                // Gọi call truyền dữ liệu đóng gói ChiTietLichTrinh
                this.lichTrinhMoi.addChiTietLichTrinh(gioDen, gioDi, ct.getNhaGa());
            }
        }

        // Lưu lịch trình
        boolean isSuccess = this.lichTrinhMoi.luuLichTrinh();

        // Hiển thị thông báo
        if (isSuccess) {
            System.out.println("Lưu lịch trình thành công!");
        } else {
            System.out.println("Lưu lịch trình thất bại!");
        }
    }

    private void displayDanhSachTau(List<DoanTau> danhSachTau) {
        // Logic cập nhật UI cho inDanhSachTau
    }

    private void displayDanhSachHanhTrinh(List<HanhTrinh> danhSachHanhTrinh) {
        // Logic cập nhật UI
    }

    private void displayGaTrungGian(List<ChiTietHanhTrinh> danhSachGaTrungGian) {
        // Logic cập nhật UI cho outGaTrungGian
    }
}
