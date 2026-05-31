package com.example.manager.boundary;

import com.example.manager.dao.DBConnection;
import com.example.manager.dao.HoaDonDAO;
import com.example.manager.dao.KhachHangDAO;
import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.entity.GheNgoi;
import com.example.manager.entity.HoaDon;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.TrangThaiGhe;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MuaVeFrm extends JFrame implements ActionListener {

    private Connection globalConnection;
    private LichTrinhDAO lichTrinhDAO;
    private KhachHangDAO khachHangDAO;
    private HoaDonDAO hoaDonDAO;

    private JTextField txtGaDi, txtGaDen, txtNgayDi;
    private JButton btnTimKiem;
    private JTable tblChuyenTau;
    private DefaultTableModel tableModel;
    private JPanel pnlSoDoGheWrapper;
    private JPanel pnlToaContainer;
    private JPanel pnlGheGrid;
    private JButton[] btnToas = new JButton[4];

    private List<LichTrinh> dsChuyenTauHienTai;
    private List<GheNgoi> dsGheHienTai;
    private LichTrinh chuyenChonTam = null;
    private String toaChon = "";
    private int rowsCurrentSelected = -1;
    private String maGheChonTam = null;
    private String maNhanVienVanhHanh = "NV001";

    private String tamTen, tamCccd, tamSdt;

    // --- BIẾN ĐIỀU HƯỚNG QUAY LẠI FORM CHA ---
    private JFrame khungMenuCha;

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    /**
     * CONSTRUCTOR NẠP CHỒNG (MỚI THÊM): Tiếp nhận form cha để phục vụ nút quay
     * lại đồ họa mượt mà
     */
    public MuaVeFrm(JFrame parent) {
        this(); // Gọi lại toàn bộ logic dựng UI của constructor mặc định bên dưới
        this.khungMenuCha = parent;

        // Thay đổi hành vi đóng cửa sổ: Chỉ tắt chính nó và hiển thị lại trang chủ nhân viên
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                quayLaiMànHinhChinh();
            }
        });
    }

    /**
     * CONSTRUCTOR MẶC ĐỊNH GỐC: Giữ nguyên vẹn 100% kịch bản kiểm thử tự động
     * của nhóm
     */
    public MuaVeFrm() {
        this.globalConnection = DBConnection.getConnection();
        if (this.globalConnection != null) {
            this.lichTrinhDAO = new LichTrinhDAO(globalConnection);
            this.khachHangDAO = new KhachHangDAO(globalConnection);
            this.hoaDonDAO = new HoaDonDAO(globalConnection);
        }

        setTitle("Hệ thống quản lý bán vé tàu hỏa - PTIT Railway");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel pnlNorthWrapper = new JPanel();
        pnlNorthWrapper.setLayout(new BoxLayout(pnlNorthWrapper, BoxLayout.Y_AXIS));

        // Thêm thanh công cụ chứa nút điều hướng quay lại trực quan cho nhân viên
        JPanel pnlStaffHeader = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("Quay lại Trang Chủ");
        btnBack.setFont(new Font("Arial", Font.BOLD, 11));
        btnBack.addActionListener(e -> quayLaiMànHinhChinh());
        pnlStaffHeader.add(btnBack, BorderLayout.WEST);

        pnlNorthWrapper.add(pnlStaffHeader);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tra cứu hành trình"));
        pnlSearch.add(new JLabel("Ga đi:"));
        txtGaDi = new JTextField("", 8);
        pnlSearch.add(txtGaDi);
        pnlSearch.add(new JLabel("Ga đến:"));
        txtGaDen = new JTextField("", 8);
        pnlSearch.add(txtGaDen);
        pnlSearch.add(new JLabel("Ngày đi:"));
        txtNgayDi = new JTextField("", 8);
        pnlSearch.add(txtNgayDi);
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.addActionListener(this);
        pnlSearch.add(btnTimKiem);
        pnlNorthWrapper.add(pnlSearch);
        add(pnlNorthWrapper, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"STT", "Mã tàu", "Giờ khởi hành", "Giờ đến", "Giá cơ bản", "Chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 5 && (rowsCurrentSelected == -1 || rowsCurrentSelected == r);
            }
        };
        tblChuyenTau = new JTable(tableModel);
        tblChuyenTau.setRowHeight(32);
        JScrollPane scrollTable = new JScrollPane(tblChuyenTau);
        scrollTable.setPreferredSize(new Dimension(940, 150));
        add(scrollTable, BorderLayout.CENTER);

        pnlSoDoGheWrapper = new JPanel(new BorderLayout(10, 10));
        pnlSoDoGheWrapper.setVisible(false);
        pnlToaContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        pnlToaContainer.add(new JLabel("Chọn toa:"));
        for (int i = 0; i < 4; i++) {
            btnToas[i] = new JButton("Toa " + (i + 1));
            final String nameToa = "Toa " + (i + 1);
            btnToas[i].addActionListener(evt -> clickChonToa(nameToa));
            pnlToaContainer.add(btnToas[i]);
        }
        pnlSoDoGheWrapper.add(pnlToaContainer, BorderLayout.NORTH);

        pnlGheGrid = new JPanel(new GridLayout(5, 11, 8, 8));
        JScrollPane scrollGhe = new JScrollPane(pnlGheGrid);
        scrollGhe.setPreferredSize(new Dimension(940, 220));
        pnlSoDoGheWrapper.add(scrollGhe, BorderLayout.CENTER);

        JPanel pnlColorGuide = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        pnlColorGuide.add(tạoOChuThichMau("Ghế Trống", Color.WHITE));
        pnlColorGuide.add(tạoOChuThichMau("Đã Có Người Đặt Trước", new Color(30, 144, 255)));
        pnlColorGuide.add(tạoOChuThichMau("Đang Chọn Giữ Chỗ", Color.YELLOW));
        pnlSoDoGheWrapper.add(pnlColorGuide, BorderLayout.SOUTH);
        add(pnlSoDoGheWrapper, BorderLayout.SOUTH);
    }

    private void quayLaiMànHinhChinh() {
        if (khungMenuCha != null) {
            khungMenuCha.setVisible(true);
            this.dispose();
        } else {
            // Fallback quay lại form chủ nhân viên mặc định nếu chạy độc lập file
            new NhanVienHomeFrm().setVisible(true);
            this.dispose();
        }
    }

    private JPanel tạoOChuThichMau(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblBox = new JLabel("   ");
        lblBox.setOpaque(true);
        lblBox.setBackground(color);
        lblBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.add(lblBox);
        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(lblText);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTimKiem) {
            hienThiDanhSachChuyenTauDB();
        }
    }

    private void hienThiDanhSachChuyenTauDB() {
        try {
            dsChuyenTauHienTai = lichTrinhDAO.layDanhSachChuyenTauPhuHop(txtGaDi.getText().trim(), txtGaDen.getText().trim(), txtNgayDi.getText().trim());
            tableModel.setRowCount(0);
            rowsCurrentSelected = -1;
            pnlSoDoGheWrapper.setVisible(false);
            if (dsChuyenTauHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hệ thống tìm kiếm không có chuyến tàu nào phù hợp.");
                return;
            }
            int stt = 1;
            for (LichTrinh lt : dsChuyenTauHienTai) {
                tableModel.addRow(new Object[]{stt++, lt.getMaTau(), lt.getGioDi(), lt.getGioDen(), String.format("%,.0f", lichTrinhDAO.getGiaVeGocTieuChuan()), "Chọn"});
            }
            tblChuyenTau.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            tblChuyenTau.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void xửLyNutChonChuyenTrongTable(int row) {
        try {
            if (rowsCurrentSelected == row) {
                if (maGheChonTam != null) {
                    hoaDonDAO.capNhatTrangThaiGhe(maGheChonTam, TrangThaiGhe.TRONG.name());
                }
                maGheChonTam = null;
                rowsCurrentSelected = -1;
                chuyenChonTam = null;
                pnlSoDoGheWrapper.setVisible(false);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt("Chọn", i, 5);
                }
            } else if (rowsCurrentSelected == -1) {
                rowsCurrentSelected = row;
                chuyenChonTam = dsChuyenTauHienTai.get(row);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(i == row ? "Đang chọn" : "-", i, 5);
                }

                pnlSoDoGheWrapper.setVisible(true);
                pnlGheGrid.removeAll();
                pnlGheGrid.revalidate();
                pnlGheGrid.repaint();
                for (int i = 0; i < 4; i++) {
                    btnToas[i].setBackground(null);
                }
                toaChon = "";
            }
            this.pack();
            this.setSize(1000, 750);
            this.setLocationRelativeTo(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clickChonToa(String nameToa) {
        this.toaChon = nameToa;
        for (int i = 0; i < 4; i++) {
            btnToas[i].setBackground(nameToa.endsWith(String.valueOf(i + 1)) ? Color.GREEN : null);
            btnToas[i].setOpaque(nameToa.endsWith(String.valueOf(i + 1)));
        }
        lamMoiSoDoGheTuDB();
    }

    private void lamMoiSoDoGheTuDB() {
        try {
            dsGheHienTai = lichTrinhDAO.layThongTinLichTrinh(chuyenChonTam.getMaLichTrinh(), toaChon);
            pnlGheGrid.removeAll();
            int[][] maTranGhe = new int[5][11];
            for (int col = 0; col < 11; col++) {
                maTranGhe[0][col] = 1 + col * 4;
                maTranGhe[1][col] = 2 + col * 4;
                maTranGhe[2][col] = -1;
                maTranGhe[3][col] = 3 + col * 4;
                maTranGhe[4][col] = 4 + col * 4;
            }
            for (int row = 0; row < 5; row++) {
                if (row == 2) {
                    for (int col = 0; col < 11; col++) {
                        pnlGheGrid.add(col == 5 ? new JLabel("LỐI ĐI", SwingConstants.CENTER) : new JLabel(""));
                    }
                    continue;
                }
                for (int col = 0; col < 11; col++) {
                    final String labelGhe = String.valueOf(maTranGhe[row][col]);
                    GheNgoi gheMatch = null;
                    if (dsGheHienTai != null) {
                        for (GheNgoi g : dsGheHienTai) {
                            if (String.valueOf(g.getSoGhe()).equals(labelGhe)) {
                                gheMatch = g;
                                break;
                            }
                        }
                    }

                    JButton btnGhe = new JButton(labelGhe);
                    btnGhe.setOpaque(true);
                    btnGhe.setBorderPainted(true);

                    if (gheMatch != null && gheMatch.getTrangThai() == TrangThaiGhe.DA_DAT) {
                        btnGhe.setBackground(new Color(30, 144, 255));
                        btnGhe.setEnabled(false);
                        btnGhe.setBorderPainted(false);
                    } else if (gheMatch != null && gheMatch.getTrangThai() == TrangThaiGhe.TAM_GIU) {
                        btnGhe.setBackground(Color.YELLOW);
                        btnGhe.setBorderPainted(false);
                    } else {
                        btnGhe.setBackground(Color.WHITE);
                    }

                    String soToaHienTai = toaChon.replaceAll("[^0-9]", "");
                    if (soToaHienTai.isEmpty()) {
                        soToaHienTai = "3";
                    }

                    final String maGheDB = (gheMatch != null) ? gheMatch.getMaGhe() : "T" + soToaHienTai + "-G" + labelGhe;

                    btnGhe.addActionListener(evt -> moPopupTamGiuGheDB(maGheDB, labelGhe));

                    pnlGheGrid.add(btnGhe);
                }
            }
            pnlGheGrid.revalidate();
            pnlGheGrid.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void moPopupTamGiuGheDB(String maGheDB, String labelGhe) {
        try {
            this.maGheChonTam = maGheDB;

            if (hoaDonDAO != null) {
                hoaDonDAO.capNhatTrangThaiGhe(maGheDB, TrangThaiGhe.TAM_GIU.name());
            }

            for (Component c : pnlGheGrid.getComponents()) {
                if (c instanceof JButton && ((JButton) c).getText().equals(labelGhe)) {
                    c.setBackground(Color.YELLOW);
                    break;
                }
            }

            JDialog dlg = new JDialog(this, "Thông tin hành khách", true);
            dlg.setSize(450, 320);
            dlg.setLocationRelativeTo(this);
            dlg.setLayout(new BorderLayout(10, 10));

            JPanel pnlTop = new JPanel(new GridLayout(3, 1));
            pnlTop.add(new JLabel("  Sơ đồ ghế: " + toaChon + " - Ghế " + labelGhe));
            pnlTop.add(new JLabel("  Trạng thái: Điền dữ liệu khách hàng thực tế"));
            JLabel lblTimer = new JLabel("  Thời gian giữ chỗ còn lại: 10:00");
            lblTimer.setForeground(Color.RED);
            pnlTop.add(lblTimer);
            dlg.add(pnlTop, BorderLayout.NORTH);

            JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));
            pnlForm.add(new JLabel("  Họ tên:"));
            JTextField txtTen = new JTextField("");
            pnlForm.add(txtTen);
            pnlForm.add(new JLabel("  Đối tượng:"));
            JComboBox<LoaiDoiTuong> cbo = new JComboBox<>(LoaiDoiTuong.values());
            pnlForm.add(cbo);
            pnlForm.add(new JLabel("  Sđt:"));
            JTextField txtSDT = new JTextField("");
            pnlForm.add(txtSDT);
            pnlForm.add(new JLabel("  Cccd:"));
            JTextField txtCC = new JTextField("");
            pnlForm.add(txtCC);
            dlg.add(pnlForm, BorderLayout.CENTER);

            final int[] time = {600};
            Timer timer = new Timer(1000, e -> {
                time[0]--;
                if (time[0] >= 0) {
                    lblTimer.setText(String.format("  Thời gian giữ chỗ còn lại: %02d:%02d", time[0] / 60, time[0] % 60));
                } else {
                    ((Timer) e.getSource()).stop();
                    dlg.dispose();
                    try {
                        if (hoaDonDAO != null) {
                            hoaDonDAO.capNhatTrangThaiGhe(maGheDB, TrangThaiGhe.TRONG.name());
                        }
                    } catch (Exception ignored) {
                    }
                    lamMoiSoDoGheTuDB();
                }
            });

            JButton btnXacMinh = new JButton("Xác minh & Xuất hóa đơn");
            btnXacMinh.addActionListener(evt -> {
                // 1. Lấy dữ liệu tạm từ text field
                String ten = txtTen.getText().trim();
                String cccd = txtCC.getText().trim();
                String sdt = txtSDT.getText().trim();

                // 2. Kiểm tra nếu thông tin trống thì chặn lại
                if (ten.isEmpty() || cccd.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đầy đủ thông tin khách hàng!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return; // DỪNG LẠI, không đóng dialog và không chạy tiếp code bên dưới
                }

                // 3. Nếu dữ liệu hợp lệ, mới thực hiện các bước tiếp theo
                try {
                    timer.stop();
                    dlg.dispose(); // Đóng popup nhập thông tin

                    this.tamTen = ten;
                    this.tamCccd = cccd;
                    this.tamSdt = sdt;

                    String loaiDTStr = ((LoaiDoiTuong) cbo.getSelectedItem()).name();
                    HoaDon hoadonTam = khachHangDAO.xuLyChinhSachGiaVaTaoDonTam(
                            tamTen, tamCccd, tamSdt, "khachhang@gmail.com",
                            loaiDTStr, chuyenChonTam, toaChon, labelGhe, 850000
                    );

                    // Hiển thị hóa đơn
                    moPopupHoaDonDB(hoadonTam, labelGhe);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dlg, "Có lỗi xảy ra: " + ex.getMessage());
                }
            });
            dlg.add(btnXacMinh, BorderLayout.SOUTH);

            timer.start();
            dlg.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void moPopupHoaDonDB(HoaDon b, String labelGhe) {
        JDialog dlg = new JDialog(this, "Hóa đơn thanh toán chi tiết", true);
        dlg.setSize(500, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(15, 15));

        JPanel pnl = new JPanel(new GridLayout(6, 2, 2, 2));
        pnl.setBackground(Color.LIGHT_GRAY);

        String gaDiThat = (chuyenChonTam != null) ? txtGaDi.getText().trim() : "";
        String gaDenThat = (chuyenChonTam != null) ? txtGaDen.getText().trim() : "";
        String gioDiThat = (chuyenChonTam != null) ? chuyenChonTam.getGioDi() : "";
        String gioDenThat = (chuyenChonTam != null) ? chuyenChonTam.getGioDen() : "";
        String maTauThat = (chuyenChonTam != null) ? chuyenChonTam.getMaTau() : "";

        String[][] data = {
            {" Họ tên: " + this.tamTen, " Cccd: " + this.tamCccd},
            {" Chuyến tàu: " + maTauThat, " Tuyến: " + gaDiThat + " -> " + gaDenThat},
            {" Khởi hành: " + gioDiThat, " Đến: " + gioDenThat},
            {" Vị trí: " + toaChon + " - Ghe " + labelGhe, " Loại: Vé hệ thống thực tế"},
            {" Chính sách: Áp dụng đối tượng", " Giá gốc: " + String.format("%,.0f", 850000.0) + " VND"},
            {" TỔNG TIỀN THANH TOÁN:", String.format("%,d", b.getTongTien()) + " VND"}
        };

        for (String[] r : data) {
            for (String c : r) {
                JLabel l = new JLabel(c);
                l.setOpaque(true);
                l.setBackground(Color.WHITE);
                pnl.add(l);
            }
        }
        dlg.add(pnl, BorderLayout.CENTER);

        JPanel pnlBtns = new JPanel(new FlowLayout());
        JButton btnOk = new JButton("Xác nhận thanh toán thành công");

        btnOk.addActionListener(evt -> {
            try {
                dlg.dispose();
                if (hoaDonDAO != null && khachHangDAO != null && chuyenChonTam != null) {
                    String maKH = khachHangDAO.layMaKhachHangTheoCCCD(this.tamCccd);
                    hoaDonDAO.luuGiaoDichThanhToanThat(maNhanVienVanhHanh, b, chuyenChonTam.getMaLichTrinh(), maGheChonTam, maKH);
                }

                lamMoiSoDoGheTuDB();
                JOptionPane.showMessageDialog(this, "Giao dịch thành công! Ghế đã được chuyển sang trạng thái ĐÃ BÁN.");
                maGheChonTam = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton btnCancel = new JButton("Hủy bỏ giao dịch");
        btnCancel.addActionListener(evt -> {
            try {
                dlg.dispose();
                if (hoaDonDAO != null) {
                    hoaDonDAO.capNhatTrangThaiGhe(maGheChonTam, TrangThaiGhe.TRONG.name());
                }
                lamMoiSoDoGheTuDB();
            } catch (Exception ignored) {
            }
        });

        pnlBtns.add(btnOk);
        pnlBtns.add(btnCancel);
        dlg.add(pnlBtns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            setText((v == null) ? "" : v.toString());
            setBackground(v != null && v.toString().equals("Đang chọn") ? Color.YELLOW : UIManager.getColor("Button.background"));
            setEnabled(v == null || !v.toString().equals("-"));
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;
        private int targetRow;

        public ButtonEditor(JCheckBox cb) {
            super(cb);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            targetRow = r;
            label = (v == null) ? "" : v.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> xửLyNutChonChuyenTrongTable(targetRow));
            }
            isPushed = false;
            return label;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new MuaVeFrm().setVisible(true));
    }
}
