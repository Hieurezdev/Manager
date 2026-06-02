package com.example.manager.boundary;

import com.example.manager.dao.QuanLyDAO;
import com.example.manager.entity.QuanLy;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * DangNhapFrm — Màn hình đăng nhập phân quyền điều hướng. Trả về QuanLyChungFrm
 * (nếu là quản lý) hoặc NhanVienHomeFrm (nếu là nhân viên).
 */
public class DangNhapFrm extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private String txtTDN = "";
    private String txtMK = "";
    private boolean dangNhapThanhCong;
    private QuanLyChungFrm quanLyChungFrm;
    private NhanVienHomeFrm nhanVienHomeFrm;

    public DangNhapFrm() {
        setTitle("HỆ THỐNG BÁN VÉ TÀU NỘI BỘ - ĐĂNG NHẬP");
        setSize(450, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTitle = new JPanel();
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitle.add(lblTitle);
        add(panelTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        panelCenter.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField("manager");
        panelCenter.add(txtUsername);

        panelCenter.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField("123");
        panelCenter.add(txtPassword);
        add(panelCenter, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout());
        btnLogin = new JButton("ĐĂNG NHẬP NGAY");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(Color.CYAN);
        btnLogin.addActionListener(this);
        panelBottom.add(btnLogin, BorderLayout.NORTH);

        JLabel lblHint = new JLabel("<html><center style='color:blue;'>Gợi ý tài khoản test:<br>Quản lý: manager/123 | Nhân viên: staff/123</center></html>", JLabel.CENTER);
        lblHint.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelBottom.add(lblHint, BorderLayout.SOUTH);
        add(panelBottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (txtUsername != null && txtPassword != null) {
            this.txtTDN = txtUsername.getText().trim();
            this.txtMK = new String(txtPassword.getPassword()).trim();
        }
        dangNhap();
    }

    /**
     * * Hàm đăng nhập đã chuyển kiểu trả về thành Object để có thể linh hoạt
     * trả về bất kỳ Form trang chủ nào mà không dính lỗi ép kiểu (Compile
     * Error).
     */
    public Object dangNhap() {
        String user = (txtUsername != null) ? txtUsername.getText().trim() : this.txtTDN;
        String pass = (txtPassword != null) ? new String(txtPassword.getPassword()).trim() : this.txtMK;

        QuanLyDAO dao = new QuanLyDAO();
        String vaiTro = dao.layVaiTroDangNhap(user, pass);

        if ("QuanLy".equals(vaiTro)) {
            dangNhapThanhCong = true;
            quanLyChungFrm = new QuanLyChungFrm();
            if (txtUsername != null) {
                quanLyChungFrm.setVisible(true);
                this.dispose();
            }
            return quanLyChungFrm; // Trả về trang chủ Quản lý
        } else if ("NhanVien".equals(vaiTro)) {
            dangNhapThanhCong = true;
            nhanVienHomeFrm = new NhanVienHomeFrm();
            if (txtUsername != null) {
                nhanVienHomeFrm.setVisible(true);
                this.dispose();
            }
            return nhanVienHomeFrm; // Trả về trang chủ Nhân viên
        } else {
            dangNhapThanhCong = false;
            if (txtUsername != null) {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }
    }

    // --- GETTERS & SETTERS (GIỮ NGUYÊN ĐỂ KHÔNG LỖI LIÊN ĐỚI) ---
    public void setTxtTDN(String txtTDN) {
        this.txtTDN = txtTDN;
        if (txtUsername != null) {
            txtUsername.setText(txtTDN);
        }
    }

    public void setTxtMK(String txtMK) {
        this.txtMK = txtMK;
        if (txtPassword != null) {
            txtPassword.setText(txtMK);
        }
    }

    public boolean isDangNhapThanhCong() {
        return dangNhapThanhCong;
    }

    public QuanLyChungFrm getQuanLyChungFrm() {
        return quanLyChungFrm;
    }

    public NhanVienHomeFrm getNhanVienHomeFrm() {
        return nhanVienHomeFrm;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhapFrm().setVisible(true);
        });
    }
}
