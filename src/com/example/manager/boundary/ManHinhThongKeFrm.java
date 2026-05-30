package com.example.manager.boundary;

import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.KhachHang;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.entity.ToaTau;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.LoaiDoiTuong;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.LoaiToa;
import com.example.manager.enums.TrangThaiLichTrinh;
import com.example.manager.enums.TrangThaiTau;
import com.example.manager.enums.TrangThaiVe;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ManHinhThongKeFrm extends JFrame implements ActionListener {

    private JFrame menuGoc;

    private JTextField txtNgayBD, txtNgayKT;
    private JButton btnThongKe, btnQuayLai;
    private JTable tblKetQua;
    private DefaultTableModel modelTable;
    private JLabel lblTongDoanhThu;

    public ManHinhThongKeFrm(JFrame parent) {
        this.menuGoc = parent;
        setTitle("THỐNG KÊ CHUYẾN TÀU THEO DOANH THU");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                quayLaiMenu();
            }
        });
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. THANH CONFIG THAM SỐ THỜI GIAN (NORTH) ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thiết lập tham số thời gian báo cáo",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        pnlFilter.add(new JLabel("Từ ngày (DD/MM/YYYY):"));
        txtNgayBD = new JTextField("01/05/2026", 10);
        txtNgayBD.setFont(new Font("Arial", Font.PLAIN, 13));
        pnlFilter.add(txtNgayBD);

        pnlFilter.add(new JLabel("Đến ngày (DD/MM/YYYY):"));
        txtNgayKT = new JTextField("31/05/2026", 10);
        txtNgayKT.setFont(new Font("Arial", Font.PLAIN, 13));
        pnlFilter.add(txtNgayKT);

        btnThongKe = new JButton("Thống Kê Doanh Thu");
        btnThongKe.setFont(new Font("Arial", Font.BOLD, 12));

        btnThongKe.setBorderPainted(false);
        btnThongKe.setFocusPainted(false);
        btnThongKe.setContentAreaFilled(false);
        btnThongKe.setOpaque(true);

        btnThongKe.setBackground(new Color(30, 144, 255));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.addActionListener(this);
        pnlFilter.add(btnThongKe);

        add(pnlFilter, BorderLayout.NORTH);

        // --- 2. BẢNG HIỂN THỊ KẾT QUẢ ĐẦU RA (CENTER) ---
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Bảng kết quả hiệu suất và doanh thu chuyến chạy (Sắp xếp giảm dần)",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        String[] columns = {
            "Mã Lịch Trình", "Tên Tàu", "Ga Đầu", "Ga Cuối",
            "Ngày Khởi Hành", "Số Vé Bán", "Tỉ Lệ Lấp Đầy", "Doanh Thu Chuyến (VNĐ)"
        };

        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKetQua = new JTable(modelTable);
        tblKetQua.setRowHeight(30);
        tblKetQua.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblKetQua.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        tblKetQua.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        tblKetQua.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane(tblKetQua);
        pnlTableWrapper.add(scrollPane, BorderLayout.CENTER);
        add(pnlTableWrapper, BorderLayout.CENTER);

        // --- 3. KHU VỰC TỔNG HỢP & ĐIỀU HƯỚNG (SOUTH) ---
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        lblTongDoanhThu = new JLabel("TỔNG DOANH THU KỲ BÁO CÁO: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setForeground(new Color(220, 53, 69));
        pnlSouth.add(lblTongDoanhThu, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnQuayLai = new JButton("Quay Lại Menu");
        btnQuayLai.addActionListener(this);
        pnlButtons.add(btnQuayLai);

        pnlSouth.add(pnlButtons, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThongKe) {
            xuLyThongKeDoanhThu();
        } else if (src == btnQuayLai) {
            quayLaiMenu();
        }
    }

    private void xuLyThongKeDoanhThu() {
        String strNgayBD = txtNgayBD.getText().trim();
        String strNgayKT = txtNgayKT.getText().trim();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate ngayBD = LocalDate.parse(strNgayBD, dtf);
            LocalDate ngayKT = LocalDate.parse(strNgayKT, dtf);

            if (ngayKT.isBefore(ngayBD)) {
                JOptionPane.showMessageDialog(this,
                        "Hệ thống báo lỗi: Ngày kết thúc không được nhỏ hơn ngày bắt đầu. Vui lòng chọn lại!",
                        "Lỗi thiết lập thời gian", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Bắt đầu chạy luồng xử lý nghiệp vụ của Hiếu sử dụng MockData
            List<LichTrinh> lichTrinhList = taoMockLichTrinh();
            BaoCao baoCao = BaoCao.taoMoiBaoCao(ngayBD, ngayKT);
            List<ChiTietBaoCao> chiTietList = new ArrayList<>();
            int stt = 1;

            for (LichTrinh lichTrinh : lichTrinhList) {
                // Kiểm tra xem lịch trình có nằm trong khoảng ngày người dùng nhập trên giao diện không
                LocalDate ngayKhoiHanh = lichTrinh.getNgayKhoiHanh().toLocalDate();
                if (ngayKhoiHanh.isBefore(ngayBD) || ngayKhoiHanh.isAfter(ngayKT)) {
                    continue;
                }

                if (lichTrinh.getTrangThai() != TrangThaiLichTrinh.HOAN_THANH) {
                    continue;
                }

                int soVeBan = 0;
                int doanhThu = 0;
                for (VeTau ve : lichTrinh.getVeTau()) {
                    if (ve.getTrangThai() == TrangThaiVe.DA_BAN) {
                        soVeBan++;
                        doanhThu += ve.getGiaVe();
                    }
                }

                int sucChua = tinhSucChua(lichTrinh.getDoanTau());
                double tiLeLapDay = sucChua == 0 ? 0.0 : (soVeBan * 100.0) / sucChua;

                ChiTietBaoCao chiTiet = new ChiTietBaoCao(
                        "CTBC-" + stt,
                        soVeBan,
                        doanhThu,
                        tiLeLapDay,
                        lichTrinh
                );
                chiTietList.add(chiTiet);
                stt++;
            }

            // Thực hiện tính toán tổng tiền và sắp xếp giảm dần theo thiết kế logic của Hiếu
            baoCao.luuKetQuaThongKe("BC-001", chiTietList);
            baoCao.tinhDoanhThu();
            baoCao.sapXepDoanhThuGiamDan();

            // Clear bảng cũ để chuẩn bị render data mới lên JTable
            modelTable.setRowCount(0);

            if (baoCao.getChiTietBaoCao() == null || baoCao.getChiTietBaoCao().isEmpty()) {
                lblTongDoanhThu.setText("TỔNG DOANH THU KỲ BÁO CÁO: 0 VNĐ");
                JOptionPane.showMessageDialog(this, "Không có dữ liệu vận hành trong khoảng thời gian đã chọn.", "Kết quả trống", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Đổ chuẩn dữ liệu tính toán từ MockData của Hiếu lên JTable giao diện của bạn
            for (ChiTietBaoCao ct : baoCao.getChiTietBaoCao()) {
                LichTrinh lt = ct.getLichTrinh();
                String tenTau = lt.getDoanTau() == null ? "" : lt.getDoanTau().getTenTau();
                NhaGa gaDau = lt.getHanhTrinh() == null ? null : lt.getHanhTrinh().getGaDau();
                NhaGa gaCuoi = lt.getHanhTrinh() == null ? null : lt.getHanhTrinh().getGaCuoi();

                String tenGaDau = gaDau == null ? "" : gaDau.getTenNhaGa();
                String tenGaCuoi = gaCuoi == null ? "" : gaCuoi.getTenNhaGa();

                modelTable.addRow(new Object[]{
                    lt.getMaLichTrinh(),
                    tenTau,
                    tenGaDau,
                    tenGaCuoi,
                    lt.getNgayKhoiHanh().toLocalDate().toString(),
                    String.format("%,d", ct.getSoVeBan()),
                    String.format("%.1f%%", ct.getTiLeLapDay()),
                    String.format("%,d", ct.getDoanhThuChuyen())
                });
            }

            // Hiển thị tổng doanh thu thật tính được từ MockData
            lblTongDoanhThu.setText(String.format("TỔNG DOANH THU KỲ BÁO CÁO: %,d VNĐ", baoCao.getTongDoanhThu()));

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập đúng định dạng DD/MM/YYYY.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int tinhSucChua(DoanTau doanTau) {
        int tong = 0;
        if (doanTau != null && doanTau.getToaTau() != null) {
            for (ToaTau toa : doanTau.getToaTau()) {
                tong += toa.getSoLuongGheToiDa();
            }
        }
        return tong;
    }

    // Bê nguyên hàm tạo dữ liệu ảo cực kỳ chuẩn chỉ của Hiếu sang
    private List<LichTrinh> taoMockLichTrinh() {
        NhaGa haNoi = new NhaGa("GA-HN", "Ha Noi", "024-000000");
        NhaGa saiGon = new NhaGa("GA-SG", "Sai Gon", "028-000000");
        NhaGa vinh = new NhaGa("GA-VI", "Vinh", "0238-000000");

        List<ChiTietHanhTrinh> ctHt1 = new ArrayList<>();
        ctHt1.add(new ChiTietHanhTrinh("CTHT-1", 1, haNoi));
        ctHt1.add(new ChiTietHanhTrinh("CTHT-2", 2, saiGon));
        HanhTrinh ht1 = new HanhTrinh("HT-01", "Ha Noi - Sai Gon", 1726.0, ctHt1);

        List<ChiTietHanhTrinh> ctHt2 = new ArrayList<>();
        ctHt2.add(new ChiTietHanhTrinh("CTHT-3", 1, haNoi));
        ctHt2.add(new ChiTietHanhTrinh("CTHT-4", 2, vinh));
        HanhTrinh ht2 = new HanhTrinh("HT-02", "Ha Noi - Vinh", 319.0, ctHt2);

        DoanTau tau1 = new DoanTau("SE1", "Thong Nhat 1", LoaiTau.THONG_NHAT, TrangThaiTau.HOAT_DONG, null);
        tau1.addToaTau(new ToaTau("T1", "Toa 1", 1, LoaiToa.NGOI_MEM, 200, ""));
        tau1.addToaTau(new ToaTau("T2", "Toa 2", 2, LoaiToa.NGOI_MEM, 200, ""));

        DoanTau tau2 = new DoanTau("NA1", "Tau Vinh", LoaiTau.DIA_PHUONG, TrangThaiTau.HOAT_DONG, null);
        tau2.addToaTau(new ToaTau("T3", "Toa 3", 1, LoaiToa.NGOI_CUNG, 160, ""));
        tau2.addToaTau(new ToaTau("T4", "Toa 4", 2, LoaiToa.NGOI_CUNG, 160, ""));

        LichTrinh lt1 = new LichTrinh("LT-SE1-01", LocalDateTime.of(2026, 5, 20, 8, 0),
                TrangThaiLichTrinh.HOAN_THANH, ht1, tau1);
        LichTrinh lt2 = new LichTrinh("LT-SE1-02", LocalDateTime.of(2026, 5, 21, 9, 0),
                TrangThaiLichTrinh.HOAN_THANH, ht1, tau1);
        LichTrinh lt3 = new LichTrinh("LT-NA1-01", LocalDateTime.of(2026, 5, 22, 7, 30),
                TrangThaiLichTrinh.HOAN_THANH, ht2, tau2);

        KhachHang kh1 = new KhachHang("KH01", "Nguyen A", "0123456789", "0900000001", "a@example.com");
        KhachHang kh2 = new KhachHang("KH02", "Tran B", "0123456790", "0900000002", "b@example.com");

        for (int i = 0; i < 220; i++) {
            lt1.addVeTau(new VeTau("VE-SE1-" + i, LoaiDoiTuong.NGUOI_LON, 1200000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh1));
        }
        for (int i = 0; i < 180; i++) {
            lt2.addVeTau(new VeTau("VE-SE2-" + i, LoaiDoiTuong.NGUOI_LON, 1100000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh2));
        }
        for (int i = 0; i < 140; i++) {
            lt3.addVeTau(new VeTau("VE-NA1-" + i, LoaiDoiTuong.NGUOI_LON, 400000,
                    TrangThaiVe.DA_BAN, LocalDateTime.now(), null, kh1));
        }

        List<LichTrinh> list = new ArrayList<>();
        list.add(lt1);
        list.add(lt2);
        list.add(lt3);
        return list;
    }

    private void quayLaiMenu() {
        if (menuGoc != null) {
            menuGoc.setVisible(true);
        }
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new ManHinhThongKeFrm(null).setVisible(true));
    }
}
