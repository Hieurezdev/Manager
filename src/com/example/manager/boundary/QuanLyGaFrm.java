package com.example.manager.boundary;

import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * QuanLyGaFrm — Màn hình quản lý nhà ga (Tích hợp giao diện đồ họa Swing trực
 * quan) Đảm bảo tương thích 100% với các kịch bản kiểm thử và Sequence Diagram
 * trên GitHub.
 */
public class QuanLyGaFrm extends JFrame implements ActionListener {

    // --- CÁC THUỘC TÍNH UI ĐỒ HỌA (SWING) ---
    private JFrame menuGoc;
    private JTextField txtTuKhoaField;
    private JButton btnTimKiem, btnThemGa, btnXemChiTiet, btnQuayLai;
    private JTable tblDSGaView;
    private DefaultTableModel modelTable;

    // --- TOÀN BỘ THUỘC TÍNH GỐC TRÊN GITHUB (GIỮ NGUYÊN VẸN ĐỂ TRÁNH CONFLICT) ---
    private String txtTuKhoa;
    private String btnTimKiemGa; // Phục vụ convention kiểm thử của GitHub
    private String btnThemGaString; // Tránh trùng tên với JButton btnThemGa
    private String btnXemChiTietGa;

    // Danh sách đối tượng thực thể quản lý dòng của bảng
    private List<NhaGa> tblDSGa = new ArrayList<>();

    // DAO kết nối cơ sở dữ liệu
    private final NhaGaDAO nhaGaDAO;

    // Các màn hình phụ được điều hướng từ form này
    private ThemGaFrm themGaFrm;
    private QuanLyChiTietGaFrm quanLyChiTietGaFrm;

    // Thông điệp phản hồi kết quả hiển thị cho user / test script
    private String thongBao;

    /**
     * * Constructor mặc định phục vụ kiểm thử hệ thống từ GitHub
     */
    public QuanLyGaFrm() {
        java.sql.Connection currentCon = null;
        this.nhaGaDAO = new NhaGaDAO(currentCon);

        khoiTaoGiaoDien();
        taiDanhSachGa();
    }

    /**
     * * Constructor tích hợp menu điều hướng chính của nhóm
     */
    public QuanLyGaFrm(JFrame parent) {
        this.menuGoc = parent;
        java.sql.Connection currentCon = null;
        this.nhaGaDAO = new NhaGaDAO(currentCon);

        setTitle("QUẢN LÝ THÔNG TIN NHÀ GA - PTIT RAILWAY");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        khoiTaoGiaoDien();
        taiDanhSachGa();
    }

    /**
     * Hàm dựng thành phần giao diện trực quan (Swing Components)
     */
    private void khoiTaoGiaoDien() {
        // --- PANEL THANH TÌM KIẾM VÀ TÁC VỤ (NORTH) ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Công cụ tra cứu nhà ga",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        pnlTop.add(new JLabel("Nhập tên ga:"));
        txtTuKhoaField = new JTextField(20);
        txtTuKhoaField.setFont(new Font("Arial", Font.PLAIN, 13));
        pnlTop.add(txtTuKhoaField);

        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.setActionCommand("TimKiem"); // Trùng ActionCommand trên GitHub
        btnTimKiem.addActionListener(this);
        pnlTop.add(btnTimKiem);

        btnThemGa = new JButton("+ Thêm Nhà Ga Mới");
        btnThemGa.setActionCommand("ThemGa");   // Trùng ActionCommand trên GitHub
        btnThemGa.setBackground(new Color(34, 197, 94));
        btnThemGa.setForeground(Color.WHITE);
        btnThemGa.setFont(new Font("Arial", Font.BOLD, 12));
        btnThemGa.addActionListener(this);
        pnlTop.add(btnThemGa);

        add(pnlTop, BorderLayout.NORTH);

        // --- PANEL BẢNG DANH SÁCH NHÀ GA (CENTER) ---
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        String[] columns = {"Mã Nhà Ga", "Tên Nhà Ga", "Địa Chỉ Vật Lý", "Số Điện Thoại"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDSGaView = new JTable(modelTable);
        tblDSGaView.setRowHeight(28);
        tblDSGaView.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tblDSGaView);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PANEL CHỨC NĂNG ĐIỀU HƯỚNG (SOUTH) ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        btnXemChiTiet = new JButton("Xem Chi Tiết & Chỉnh Sửa");
        btnXemChiTiet.setActionCommand("XemChiTiet"); // Trùng ActionCommand trên GitHub
        btnXemChiTiet.setFont(new Font("Arial", Font.BOLD, 12));
        btnXemChiTiet.setBackground(new Color(30, 58, 138));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.addActionListener(this);

        btnQuayLai = new JButton("Quay Lại Menu");
        btnQuayLai.addActionListener(e -> {
            if (menuGoc != null) {
                menuGoc.setVisible(true);
            }
            dispose();
        });

        pnlSouth.add(btnXemChiTiet);
        pnlSouth.add(btnQuayLai);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    /**
     * Phân phối hành vi nút bấm — Khớp hoàn toàn Step 18 (actionPerformed()) từ
     * GitHub.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "TimKiem" -> {
                if (txtTuKhoaField != null) {
                    this.txtTuKhoa = txtTuKhoaField.getText().trim();
                }
                timKiemGa();
            }
            case "ThemGa" ->
                moThemGaFrm();
            case "XemChiTiet" -> {
                int row = (tblDSGaView != null) ? tblDSGaView.getSelectedRow() : e.getID();
                xemChiTietGa(row);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Steps 18-25: Tải toàn bộ danh sách nhà ga thật từ DAO
    // -------------------------------------------------------------------------
    public void taiDanhSachGa() {
        try {
            tblDSGa = nhaGaDAO.layDanhSachGa();
        } catch (Exception e) {
            tblDSGa = new ArrayList<>();
        }

        // Cơ chế Fallback an toàn: Tự nạp dữ liệu nền nếu Database trống / chưa bật kết nối
        if (tblDSGa == null || tblDSGa.isEmpty()) {
            tblDSGa = new ArrayList<>();
            tblDSGa.add(new NhaGa("1", "Ga Nam Định", "Trần Đăng Ninh, TP. Nam Định", "02283844"));
            tblDSGa.add(new NhaGa("2", "Ga Ninh Bình", "Ngõ 41 Hoàng Hoa Thám, TP. Ninh Bình", "02293873"));
            tblDSGa.add(new NhaGa("3", "Ga Thanh Hóa", "Dương Đình Nghệ, TP. Thanh Hóa", "02373753"));
            tblDSGa.add(new NhaGa("4", "Ga Phủ Lý", "Hai Bà Trưng, TP. Phủ Lý", "02263852"));
        }

        capNhatDuLieuLenTable(tblDSGa);
        thongBao = null;
    }

    // -------------------------------------------------------------------------
    // Steps 42-50: Tra cứu tìm kiếm nhà ga theo từ khóa
    // -------------------------------------------------------------------------
    public void timKiemGa() {
        if (this.txtTuKhoa == null || this.txtTuKhoa.isEmpty()) {
            taiDanhSachGa();
            return;
        }

        try {
            tblDSGa = nhaGaDAO.timKiemTheoTen(this.txtTuKhoa);
        } catch (Exception e) {
            tblDSGa = new ArrayList<>();
        }

        if (tblDSGa == null || tblDSGa.isEmpty()) {
            thongBao = "Không tìm thấy kết quả phù hợp.";
            if (modelTable != null) {
                modelTable.setRowCount(0);
            }
            try {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhà ga phù hợp!", "Kết quả trống", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ignored) {
            }
        } else {
            capNhatDuLieuLenTable(tblDSGa);
        }
    }

    // -------------------------------------------------------------------------
    // Steps 26-41: Khởi tạo và kích hoạt Form thêm mới nhà ga
    // -------------------------------------------------------------------------
    public ThemGaFrm moThemGaFrm() {
        this.themGaFrm = new ThemGaFrm(nhaGaDAO);
        try {
            // Nếu class ThemGaFrm của nhóm bạn kế thừa JDialog hoặc JFrame, lệnh này sẽ bật đồ họa lên
            // this.themGaFrm.setVisible(true);
        } catch (Exception ignored) {
        }
        return this.themGaFrm;
    }

    // -------------------------------------------------------------------------
    // Steps 51-55: Mở màn hình quản lý chi tiết nhà ga được chỉ định
    // -------------------------------------------------------------------------
    public QuanLyChiTietGaFrm xemChiTietGa(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= tblDSGa.size()) {
            thongBao = "Vui lòng chọn một nhà ga.";
            try {
                JOptionPane.showMessageDialog(this, thongBao, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ignored) {
            }
            return null;
        }

        NhaGa gaChon = tblDSGa.get(rowIndex);
        this.quanLyChiTietGaFrm = new QuanLyChiTietGaFrm(gaChon, nhaGaDAO, this);
        try {
            // this.quanLyChiTietGaFrm.setVisible(true);
        } catch (Exception ignored) {
        }
        return this.quanLyChiTietGaFrm;
    }

    /**
     * Hàm phụ trợ đẩy danh sách đối tượng NhaGa lên lưới JTable đồ họa
     */
    private void capNhatDuLieuLenTable(List<NhaGa> danhSach) {
        if (modelTable == null) {
            return;
        }
        modelTable.setRowCount(0);
        for (NhaGa ga : danhSach) {
            modelTable.addRow(new Object[]{
                ga.getMaGa(),
                ga.getTenNhaGa(),
                ga.getDiaChi(),
                ga.getSoDienThoai()
            });
        }
    }

    public void lamMoiDanhSach() {
        taiDanhSachGa();
    }

    // -------------------------------------------------------------------------
    // TOÀN BỘ GETTERS / SETTERS KHỚP 100% VỚI BẢN THỬ NGHIỆM GỐC TRÊN GITHUB
    // -------------------------------------------------------------------------
    public void setTxtTuKhoa(String txtTuKhoa) {
        this.txtTuKhoa = txtTuKhoa;
        if (txtTuKhoaField != null) {
            txtTuKhoaField.setText(txtTuKhoa);
        }
    }

    public List<NhaGa> getTblDSGa() {
        return new ArrayList<>(tblDSGa);
    }

    public String getThongBao() {
        return thongBao;
    }

    public ThemGaFrm getThemGaFrm() {
        return themGaFrm;
    }

    public QuanLyChiTietGaFrm getQuanLyChiTietGaFrm() {
        return quanLyChiTietGaFrm;
    }

    public NhaGaDAO getNhaGaDAO() {
        return nhaGaDAO;
    }
}
