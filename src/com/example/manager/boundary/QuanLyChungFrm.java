package com.example.manager.boundary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * QuanLyChungFrm — Trang chủ chính dành cho Cán bộ Quản lý. Kết hợp hoàn hảo
 * giữa giao diện Swing thực tế và cấu trúc Mock trên GitHub của nhóm.
 */
public class QuanLyChungFrm extends JFrame implements ActionListener {

    // --- CÁC THÀNH PHẦN GIAO DIỆN THỰC TẾ (SWING COMPONENTS) ---
    private JButton btnLenLichReal;
    private JButton btnQuanLyGaReal;
    private JButton btnThongKeReal;
    private JButton btnLogoutReal;

    // --- GIỮ NGUYÊN BẮT BUỘC CÁC TRƯỜNG TRÊN GITHUB ĐỂ TRÁNH LỖI MOCK TEST ---
    private String btnQuanLyBCTKL; // Báo cáo thống kê lịch trình
    private String btnQuanLyTTNG;  // Thông tin nhà ga
    private String btnQuanLyLTCT;  // Lịch trình chuyến tàu

    private QuanLyGaFrm quanLyGaFrm;

    // --- CONSTRUCTOR ---
    public QuanLyChungFrm() {
        // Cấu hình cửa sổ giao diện Swing theo bản tự code
        setTitle("MÀN HÌNH CHÍNH CỦA CÁN BỘ QUẢN LÝ");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề phía trên
        JLabel lblTitle = new JLabel("HỆ THỐNG CHỨC NĂNG DÀNH CHO QUẢN LÝ", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Vùng trung tâm chứa các nút chức năng
        JPanel pCenter = new JPanel(new GridLayout(3, 1, 15, 15));
        pCenter.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        btnLenLichReal = new JButton("1. Lên Lịch Chạy Tàu");
        btnQuanLyGaReal = new JButton("2. Quản Lý Thông Tin Nhà Ga");
        btnThongKeReal = new JButton("3. Thống Kê Doanh Thu Chuyến Tàu");

        // Gắn ActionCommand trùng khớp với logic xử lý trên GitHub của nhóm bạn
        btnQuanLyGaReal.setActionCommand("QuanLyTTNG");

        pCenter.add(btnLenLichReal);
        pCenter.add(btnQuanLyGaReal);
        pCenter.add(btnThongKeReal);
        add(pCenter, BorderLayout.CENTER);

        // Vùng nút Đăng xuất phía dưới
        btnLogoutReal = new JButton("Đăng xuất");
        JPanel pSouth = new JPanel();
        pSouth.add(btnLogoutReal);
        add(pSouth, BorderLayout.SOUTH);

        // --- ĐĂNG KÝ SỰ KIỆN LẮNG NGHE CHUNG ---
        btnQuanLyGaReal.addActionListener(this); // Gọi về hàm actionPerformed() theo chuẩn GitHub

        // Các nút khác gọi trực tiếp điều hướng bằng lambda cho nhanh gọn giống bản bạn code
        btnLenLichReal.addActionListener(e -> {
            try {
                // Thử gọi màn hình Lên Lịch nếu nhóm bạn đã chuẩn hóa file này sang Swing
                ManHinhLenLichChayView lenLichFrm = new ManHinhLenLichChayView(this);
                lenLichFrm.setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Chức năng Lên Lịch đang được phát triển.");
            }
        });

        btnThongKeReal.addActionListener(e -> {
            try {
                // Thử gọi màn hình Thống Kê nếu nhóm bạn đã chuẩn hóa file này sang Swing
                ManHinhThongKeFrm thongKeFrm = new ManHinhThongKeFrm(this);
                thongKeFrm.setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Chức năng Thống Kê đang được phát triển.");
            }
        });

        btnLogoutReal.addActionListener(e -> {
            try {
                // Quay lại màn hình đăng nhập chuẩn của nhóm trên GitHub
                new DangNhapFrm().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                System.exit(0); // Nếu chưa có form login thì đóng ứng dụng luôn cho tiện test
            }
        });
    }

    /**
     * Sequence diagram step 13: actionPerformed() khi btnQuanLyTTNG được click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e == null) {
            return;
        }
        String source = e.getActionCommand();
        if ("QuanLyTTNG".equals(source)) {
            moManHinhQuanLyGa();
        }
    }

    /**
     * Sequence diagram step 14-15: Mở màn hình QuanLyGaFrm. ĐỒNG BỘ: Sử dụng
     * trực tiếp luồng đồ họa Swing sạch sẽ, không dùng Reflection nữa.
     */
    public QuanLyGaFrm moManHinhQuanLyGa() {
        // Khởi tạo đối tượng giao diện chạy thật và truyền 'this' để làm form cha điều hướng ngược lại
        quanLyGaFrm = new QuanLyGaFrm(this); // step 15: QuanLyGaFrm()

        // Hiển thị trực tiếp form quản lý ga lên màn hình và ẩn trang chủ chung đi
        quanLyGaFrm.setVisible(true);
        this.setVisible(false);

        return quanLyGaFrm;              // step 16: Hiển thị quay lại test script
    }

    // --- GIỮ NGUYÊN CÁC HÀM GETTER ĐỂ KHÔNG LỖI LIÊN ĐỚI TEST SCRIPT ---
    public QuanLyGaFrm getQuanLyGaFrm() {
        return quanLyGaFrm;
    }

    // Hàm main dùng để test nhanh độc lập toàn bộ luồng quản lý nhà ga vừa code
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyChungFrm().setVisible(true);
        });
    }
}
