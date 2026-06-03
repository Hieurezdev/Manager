package com.example.manager.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * QuanLyChungFrm — Trang chủ chính dành cho Cán bộ Quản lý.
 */
public class QuanLyChungFrm extends JFrame implements ActionListener {

    // --- CÁC THÀNH PHẦN GIAO DIỆN THỰC TẾ (SWING COMPONENTS) ---
    private JButton btnLenLichReal;
    private JButton btnQuanLyGaReal;
    private JButton btnThongKeReal;
    private JButton btnLogoutReal;

    private String btnQuanLyBCTKL; // Báo cáo thống kê lịch trình
    private String btnQuanLyTTNG;  // Thông tin nhà ga
    private String btnQuanLyLTCT;  // Lịch trình chuyến tàu

    private QuanLyGaFrm quanLyGaFrm;

    // --- CONSTRUCTOR ---
    public QuanLyChungFrm() {
        // Cấu hình cửa sổ giao diện Swing
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

        // Gắn ActionCommand trùng khớp với logic xử lý
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
        btnQuanLyGaReal.addActionListener(this); // Gọi về hàm actionPerformed()

        // Các nút khác gọi trực tiếp điều hướng bằng lambda cho nhanh gọn
        btnLenLichReal.addActionListener(e -> {
            try {
                // Thử gọi màn hình Lên Lịch
                ManHinhLenLichChayView lenLichFrm = new ManHinhLenLichChayView(this);
                lenLichFrm.setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Chức năng Lên Lịch đang được phát triển.");
            }
        });

        btnThongKeReal.addActionListener(e -> {
            try {
                // Thử gọi màn hình Thống Kê
                ManHinhThongKeFrm thongKeFrm = new ManHinhThongKeFrm(this);
                thongKeFrm.setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Chức năng Thống Kê đang được phát triển.");
            }
        });

        btnLogoutReal.addActionListener(e -> {
            try {
                // Quay lại màn hình đăng nhập
                new DangNhapFrm().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                System.exit(0); // Nếu chưa có form login thì đóng ứng dụng luôn
            }
        });
    }

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
     * Mở màn hình QuanLyGaFrm.
     */
    public QuanLyGaFrm moManHinhQuanLyGa() {
        // Khởi tạo đối tượng giao diện chạy thật và truyền 'this' để làm form cha điều hướng ngược lại
        quanLyGaFrm = new QuanLyGaFrm(this);

        // Hiển thị trực tiếp form quản lý ga lên màn hình và ẩn trang chủ chung đi
        quanLyGaFrm.setVisible(true);
        this.setVisible(false);

        return quanLyGaFrm;              // Hiển thị quay lại test script
    }

    public QuanLyGaFrm getQuanLyGaFrm() {
        return quanLyGaFrm;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyChungFrm().setVisible(true);
        });
    }
}
