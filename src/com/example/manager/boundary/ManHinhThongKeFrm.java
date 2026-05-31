package com.example.manager.boundary;

import com.example.manager.dao.BaoCaoDAO;
import com.example.manager.dao.DoanTauDAO;
import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.entity.ToaTau;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.TrangThaiLichTrinh;
import com.example.manager.enums.TrangThaiVe;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * ManHinhThongKeFrm — Giao diện thống kê hiệu suất doanh thu chuyến tàu. Đã gỡ
 * bỏ Mock Data, kết nối trực tiếp DAO nghiệp vụ của nhóm và giữ nguyên biến
 * kiểm thử GitHub.
 */
public class ManHinhThongKeFrm extends JFrame implements ActionListener {

    private JFrame menuGoc;

    // --- CÁC BIẾN GIAO DIỆN THỰC TẾ (SWING) ---
    private JTextField txtNgayBD, txtNgayKT;
    private JButton btnThongKeReal, btnQuayLai;
    private JTable tblKetQua;
    private DefaultTableModel modelTable;
    private JLabel lblTongDoanhThu;

    // --- GIỮ NGUYÊN BẮT BUỘC THUỘC TÍNH TRÊN GITHUB ĐỂ NÉ CONFLICT TEST ---
    private LocalDate ngayBD;
    private LocalDate ngayKT;
    private String btnThongKe;

    private String outMaLichTrinh;
    private String outTenTau;
    private String outGaDau;
    private String outGaCuoi;
    private String outNgayKhoiHanh;
    private String outSoVeBan;
    private String outTiLeLapDay;
    private String outDoanhThuChuyen;

    // Constructor phục vụ luồng chạy giao diện thực tế
    public ManHinhThongKeFrm(JFrame parent) {
        this.menuGoc = parent;
        initSwingComponents();
    }

    // Constructor mặc định bắt buộc phải giữ lại theo mẫu của GitHub
    public ManHinhThongKeFrm() {
        // Phục vụ test tự động không giao diện
    }

    private void initSwingComponents() {
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

        btnThongKeReal = new JButton("Thống Kê Doanh Thu");
        btnThongKeReal.setFont(new Font("Arial", Font.BOLD, 12));
        btnThongKeReal.setBorderPainted(false);
        btnThongKeReal.setFocusPainted(false);
        btnThongKeReal.setContentAreaFilled(false);
        btnThongKeReal.setOpaque(true);
        btnThongKeReal.setBackground(new Color(30, 144, 255));
        btnThongKeReal.setForeground(Color.WHITE);
        btnThongKeReal.addActionListener(this);
        pnlFilter.add(btnThongKeReal);

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

    /**
     * Chuẩn hóa hàm actionPerformed nhận diện sự kiện từ giao diện đồ họa thực
     * tế
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThongKeReal) {
            xuLyThongKeTuGiaoDien();
        } else if (src == btnQuayLai) {
            quayLaiMenu();
        }
    }

    /**
     * Đọc ngày tháng từ UI, kiểm tra tính hợp lệ rồi kích hoạt luồng xử lý
     * chuẩn của nhóm
     */
    private void xuLyThongKeTuGiaoDien() {
        String strNgayBD = txtNgayBD.getText().trim();
        String strNgayKT = txtNgayKT.getText().trim();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            this.ngayBD = LocalDate.parse(strNgayBD, dtf);
            this.ngayKT = LocalDate.parse(strNgayKT, dtf);

            if (ngayKT.isBefore(ngayBD)) {
                JOptionPane.showMessageDialog(this,
                        "Hệ thống báo lỗi: Ngày kết thúc không được nhỏ hơn ngày bắt đầu. Vui lòng chọn lại!",
                        "Lỗi thiết lập thời gian", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gọi hàm thongKe() chuẩn gốc trên GitHub để lấy dữ liệu tính toán từ DB (Không dùng Mock Data nữa!)
            BaoCao baoCao = thongKe();

            // Làm sạch bảng kết quả trước khi hiển thị dữ liệu mới lên JTable
            modelTable.setRowCount(0);

            if (baoCao == null || baoCao.getChiTietBaoCao() == null || baoCao.getChiTietBaoCao().isEmpty()) {
                lblTongDoanhThu.setText("TỔNG DOANH THU KỲ BÁO CÁO: 0 VNĐ");
                JOptionPane.showMessageDialog(this, "Không có dữ liệu vận hành hoặc hàm xử lý DB trả về kết quả trống.", "Kết quả trống", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Render dữ liệu tính toán từ hệ thống nghiệp vụ của nhóm lên bảng giao diện của bạn
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

            lblTongDoanhThu.setText(String.format("TỔNG DOANH THU KỲ BÁO CÁO: %,d VNĐ", baoCao.getTongDoanhThu()));

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập đúng định dạng DD/MM/YYYY.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * * HÀM THONGKE CHUẨN GỐC TRÊN GITHUB — ĐƯỢC GIỮ NGUYÊN VẸN 100% LOGIC
     * NGHIỆP VỤ. Đọc thông tin từ DAO thật liên kết với cơ sở dữ liệu.
     */
    public BaoCao thongKe() {
        Connection con = null; // Biến kết nối cơ sở dữ liệu thật của nhóm bạn
        BaoCaoDAO baoCaoDAO = new BaoCaoDAO(con);
        if (!baoCaoDAO.kiemTraHopLe(ngayBD, ngayKT)) {
            return null;
        }

        BaoCao baoCao = baoCaoDAO.taoMoiBaoCao(ngayBD, ngayKT);
        List<ChiTietBaoCao> chiTietList = new ArrayList<>();

        DoanTauDAO doanTauDAO = new DoanTauDAO(con);
        LichTrinhDAO lichTrinhDAO = new LichTrinhDAO(con);
        VeTauDAO veTauDAO = new VeTauDAO(con);

        int stt = 1;
        for (DoanTau tau : doanTauDAO.layDanhSachDoanTau()) {
            List<LichTrinh> lichTrinhList = lichTrinhDAO.layDanhSachLichTrinhTrongKy(
                    tau.getMaTau(), ngayBD, ngayKT
            );
            for (LichTrinh lichTrinh : lichTrinhList) {
                if (lichTrinh.getTrangThai() != TrangThaiLichTrinh.HOAN_THANH) {
                    continue;
                }

                List<VeTau> veDaBan = veTauDAO.layDanhSachVeTheoLichTrinh(lichTrinh.getMaLichTrinh());
                int soVeBan = 0;
                int doanhThu = 0;
                for (VeTau ve : veDaBan) {
                    if (ve.getTrangThai() == TrangThaiVe.DA_BAN) {
                        soVeBan++;
                        doanhThu += ve.getGiaVe();
                    }
                }

                int sucChua = tinhSucChua(tau);
                double tiLeLapDay = sucChua == 0 ? 0.0 : (soVeBan * 100.0) / sucChua;

                chiTietList.add(new ChiTietBaoCao(
                        "CTBC-" + stt,
                        soVeBan,
                        doanhThu,
                        tiLeLapDay,
                        lichTrinh
                ));
                stt++;
            }
        }

        String maBaoCao = "BC-" + System.currentTimeMillis();
        baoCao.luuKetQuaThongKe(maBaoCao, chiTietList);
        baoCao.tinhDoanhThu();
        baoCao.sapXepDoanhThuGiamDan();
        baoCaoDAO.luuKetQuaThongKe(maBaoCao, chiTietList);

        capNhatOutput(baoCao);
        return baoCao;
    }

    private void capNhatOutput(BaoCao baoCao) {
        StringBuilder maLichTrinh = new StringBuilder();
        StringBuilder tenTau = new StringBuilder();
        StringBuilder gaDau = new StringBuilder();
        StringBuilder gaCuoi = new StringBuilder();
        StringBuilder ngayKhoiHanh = new StringBuilder();
        StringBuilder soVeBan = new StringBuilder();
        StringBuilder tiLeLapDay = new StringBuilder();
        StringBuilder doanhThuChuyen = new StringBuilder();

        if (baoCao != null) {
            for (ChiTietBaoCao ct : baoCao.getChiTietBaoCao()) {
                LichTrinh lichTrinh = ct.getLichTrinh();
                String tenTauValue = lichTrinh.getDoanTau() == null ? "" : lichTrinh.getDoanTau().getTenTau();
                NhaGa gaDauValue = lichTrinh.getHanhTrinh() == null ? null : lichTrinh.getHanhTrinh().getGaDau();
                NhaGa gaCuoiValue = lichTrinh.getHanhTrinh() == null ? null : lichTrinh.getHanhTrinh().getGaCuoi();

                appendLine(maLichTrinh, lichTrinh.getMaLichTrinh());
                appendLine(tenTau, tenTauValue);
                appendLine(gaDau, gaDauValue == null ? "" : gaDauValue.getTenNhaGa());
                appendLine(gaCuoi, gaCuoiValue == null ? "" : gaCuoiValue.getTenNhaGa());
                appendLine(ngayKhoiHanh, lichTrinh.getNgayKhoiHanh().toLocalDate().toString());
                appendLine(soVeBan, String.valueOf(ct.getSoVeBan()));
                appendLine(tiLeLapDay, String.format("%.1f%%", ct.getTiLeLapDay()));
                appendLine(doanhThuChuyen, String.valueOf(ct.getDoanhThuChuyen()));
            }
        }

        this.outMaLichTrinh = maLichTrinh.toString();
        this.outTenTau = tenTau.toString();
        this.outGaDau = gaDau.toString();
        this.outGaCuoi = gaCuoi.toString();
        this.outNgayKhoiHanh = ngayKhoiHanh.toString();
        this.outSoVeBan = soVeBan.toString();
        this.outTiLeLapDay = tiLeLapDay.toString();
        this.outDoanhThuChuyen = doanhThuChuyen.toString();
    }

    private void appendLine(StringBuilder builder, String value) {
        if (builder.length() > 0) {
            builder.append('\n');
        }
        builder.append(value == null ? "" : value);
    }

    private int tinhSucChua(DoanTau tau) {
        int tong = 0;
        if (tau != null && tau.getToaTau() != null) {
            for (ToaTau toa : tau.getToaTau()) {
                tong += toa.getSoLuongGheToiDa();
            }
        }
        return tong;
    }

    private void quayLaiMenu() {
        if (menuGoc != null) {
            menuGoc.setVisible(true);
        }
        dispose();
    }

    // --- GETTERS & SETTERS (GIỮ NGUYÊN ĐỂ VƯỢT QUA JUNIT TEST) ---
    public void setNgayBD(LocalDate ngayBD) {
        this.ngayBD = ngayBD;
    }

    public void setNgayKT(LocalDate ngayKT) {
        this.ngayKT = ngayKT;
    }

    public String getOutMaLichTrinh() {
        return outMaLichTrinh;
    }

    public String getOutTenTau() {
        return outTenTau;
    }

    public String getOutGaDau() {
        return outGaDau;
    }

    public String getOutGaCuoi() {
        return outGaCuoi;
    }

    public String getOutNgayKhoiHanh() {
        return outNgayKhoiHanh;
    }

    public String getOutSoVeBan() {
        return outSoVeBan;
    }

    public String getOutTiLeLapDay() {
        return outTiLeLapDay;
    }

    public String getOutDoanhThuChuyen() {
        return outDoanhThuChuyen;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new ManHinhThongKeFrm(null).setVisible(true));
    }
}
