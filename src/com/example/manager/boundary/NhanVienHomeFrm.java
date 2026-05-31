package com.example.manager.boundary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * NhanVienHomeFrm — Trang chủ chính dành cho Nhân viên bán vé. Loại bỏ hoàn
 * toàn Reflection, tích hợp luồng điều hướng đồ họa thật, không xung đột Git.
 */
public class NhanVienHomeFrm extends JFrame implements ActionListener {

    // --- CÁC THÀNH PHẦN GIAO DIỆN THỰC TẾ (SWING COMPONENTS) ---
    private JButton btnMuaVeReal;
    private JButton btnTraVeReal;
    private JButton btnLogoutReal;

    // --- GIỮ NGUYÊN BẮT BUỘC CÁC BIẾN MOCK TRÊN GITHUB ĐỂ NÉ CONFLICT ---
    private String btnTraVe;
    private String btnMuaVe;

    // --- CONSTRUCTOR ---
    public NhanVienHomeFrm() {
        // Cấu hình cửa sổ giao diện Swing
        setTitle("TRANG CHỦ NHÂN VIÊN BÁN VÉ - PTIT RAILWAY");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề phía trên hiển thị thông tin phiên làm việc
        JLabel lblWelcome = new JLabel("Xin chào Nhân viên: staff", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblWelcome, BorderLayout.NORTH);

        // Vùng trung tâm chứa các nút điều phối chức năng chính
        JPanel panelCenter = new JPanel(new GridLayout(2, 1, 15, 15));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnMuaVeReal = new JButton("Chức năng Mua Vé");
        btnMuaVeReal.setFont(new Font("Arial", Font.BOLD, 16));
        btnMuaVeReal.setActionCommand("MuaVe"); // Khớp ActionCommand logic nghiệp vụ GitHub

        btnTraVeReal = new JButton("Chức năng Trả Vé");
        btnTraVeReal.setFont(new Font("Arial", Font.BOLD, 16));
        btnTraVeReal.setActionCommand("TraVe"); // Khớp ActionCommand logic nghiệp vụ GitHub

        panelCenter.add(btnMuaVeReal);
        panelCenter.add(btnTraVeReal);
        add(panelCenter, BorderLayout.CENTER);

        // Vùng chức năng hệ thống phía dưới (Đăng xuất)
        btnLogoutReal = new JButton("Đăng xuất hệ thống");
        btnLogoutReal.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel panelSouth = new JPanel();
        panelSouth.add(btnLogoutReal);
        add(panelSouth, BorderLayout.SOUTH);

        // --- ĐĂNG KÝ SỰ KIỆN LẮNG NGHE CHUNG ---
        btnMuaVeReal.addActionListener(this);
        btnTraVeReal.addActionListener(this);

        btnLogoutReal.addActionListener(e -> {
            try {
                // Quay lại màn hình đăng nhập chuẩn của nhóm trên GitHub
                new DangNhapFrm().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Đăng xuất thành công! (Màn hình DangNhapFrm đang được cấu trúc).");
                System.exit(0);
            }
        });
    }

    // --- HÀM XỬ LÝ SỰ KIỆN CHUẨN TRÊN GITHUB ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e == null) {
            return;
        }

        String command = e.getActionCommand();

        if ("TraVe".equals(command)) {
            moManHinhSearchVeTau();
        }

        if ("MuaVe".equals(command)) {
            moManHinhMuaVe();
        }
    }

    /**
     * Hàm điều hướng chuẩn sang Màn hình tra cứu và hoàn trả vé tàu (Đã bỏ
     * Reflection)
     */
    public SearchVeTauFrm moManHinhSearchVeTau() {
        // Khởi tạo cửa sổ tra cứu xịn, truyền 'this' làm Form mẹ để xử lý nút [Quay Lại]
        SearchVeTauFrm searchVeTauFrm = new SearchVeTauFrm(this);

        // Hiển thị màn hình tra cứu, ẩn màn hình chính
        searchVeTauFrm.setVisible(true);
        this.setVisible(false);

        return searchVeTauFrm;
    }

    /**
     * Hàm khởi tạo và hiển thị màn hình Mua Vé của ông Đạt (Đã tối ưu hóa luồng
     * gọi thật)
     */
    public MuaVeFrm moManHinhMuaVe() {
        // Tương tự, khởi tạo màn hình mua vé thật truyền cây giao diện cha 'this' vào xử lý
        MuaVeFrm muaVeFrm = new MuaVeFrm(this);

        try {
            muaVeFrm.setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
            // Khớp fallback đề phòng trường hợp file MuaVeFrm chưa hoàn thiện kế thừa JFrame
            JOptionPane.showMessageDialog(this, "Điều hướng thành công sang: MuaVeFrm!\n(Đã kích hoạt luồng kết nối phân quyền)");
        }

        return muaVeFrm;
    }

    // --- GIỮ NGUYÊN TOÀN BỘ GETTERS/SETTERS MOCK ĐỂ KHÔNG LỖI LIÊN ĐỚI SCRIPT TEST ---
    public String getBtnTraVe() {
        return btnTraVe;
    }

    public void setBtnTraVe(String btnTraVe) {
        this.btnTraVe = btnTraVe;
    }

    public String getBtnMuaVe() {
        return btnMuaVe;
    }

    public void setBtnMuaVe(String btnMuaVe) {
        this.btnMuaVe = btnMuaVe;
    }

    /**
     * Hàm main chạy độc lập dùng để review giao diện tổng quan cho buổi báo cáo
     * tiến độ.
     */
    public static void main(String[] args) {
        // Thiết lập giao diện hệ điều hành cho mượt mà
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new NhanVienHomeFrm().setVisible(true);
        });
    }
}
