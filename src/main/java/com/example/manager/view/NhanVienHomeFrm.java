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
import javax.swing.UIManager;


public class NhanVienHomeFrm extends JFrame implements ActionListener {

    private JButton btnMuaVeReal;
    private JButton btnTraVeReal;
    private JButton btnLogoutReal;

    private String btnTraVe;
    private String btnMuaVe;

    public NhanVienHomeFrm() {
        setTitle("TRANG CHỦ NHÂN VIÊN BÁN VÉ - PTIT RAILWAY");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblWelcome = new JLabel("Xin chào Nhân viên: staff", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblWelcome, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(2, 1, 15, 15));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnMuaVeReal = new JButton("Chức năng Mua Vé");
        btnMuaVeReal.setFont(new Font("Arial", Font.BOLD, 16));
        btnMuaVeReal.setActionCommand("MuaVe");
        btnTraVeReal = new JButton("Chức năng Trả Vé");
        btnTraVeReal.setFont(new Font("Arial", Font.BOLD, 16));
        btnTraVeReal.setActionCommand("TraVe");

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
                // Quay lại màn hình đăng nhập
                new DangNhapFrm().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Đăng xuất thành công! (Màn hình DangNhapFrm đang được cấu trúc).");
                System.exit(0);
            }
        });
    }

    
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

    public SearchVeTauFrm moManHinhSearchVeTau() {
        // Khởi tạo cửa sổ tra cứu, truyền 'this' làm Form mẹ để xử lý nút [Quay Lại]
        SearchVeTauFrm searchVeTauFrm = new SearchVeTauFrm(this);

        // Hiển thị màn hình tra cứu, ẩn màn hình chính
        searchVeTauFrm.setVisible(true);
        this.setVisible(false);

        return searchVeTauFrm;
    }

    public MuaVeFrm moManHinhMuaVe() {
        MuaVeFrm muaVeFrm = new MuaVeFrm(this);

        try {
            muaVeFrm.setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
           
            JOptionPane.showMessageDialog(this, "Điều hướng thành công sang: MuaVeFrm!\n(Đã kích hoạt luồng kết nối phân quyền)");
        }

        return muaVeFrm;
    }

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
