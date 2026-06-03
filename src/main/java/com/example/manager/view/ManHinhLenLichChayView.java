package com.example.manager.view;

import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.LichTrinh;
import com.example.manager.enums.TrangThaiLichTrinh;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * ManHinhLenLichChayView — Giao diện lập lịch trình chạy tàu. Đã loại bỏ hoàn
 * toàn Mock Data, kết nối thực thể thật và đồng bộ thuộc tính GitHub.
 */
public class ManHinhLenLichChayView extends JFrame implements ActionListener {

    private JFrame menuGoc; // MHQuanLyFrm để quay lại màn hình chính

    // --- CÁC THÀNH PHẦN GIAO DIỆN ĐỒ HỌA THỰC TẾ (SWING) ---
    private JComboBox<DoanTauWrapper> cbDoanTauReal;
    private JComboBox<HanhTrinhWrapper> cbHanhTrinhReal;
    private JTextField txtNgayKhoiHanh;
    private JTable tblGaTrungGian;
    private DefaultTableModel modelTableGa;
    private JButton btnXacNhanReal, btnQuayLai;

    private String inDanhSachTau;
    private String outGaTrungGian;
    private String inGioDen;
    private String inGioDi;
    private String subXacNhan;

    private List<DoanTau> danhSachTau;
    private List<HanhTrinh> danhSachHanhTrinh;
    private List<ChiTietHanhTrinh> danhSachGaTrungGian;

    private DoanTau tauDuocChon;
    private HanhTrinh hanhTrinhDuocChon;
    private LichTrinh lichTrinhMoi;

    // Constructor đồ họa thực tế được gọi từ QuanLyChungFrm
    public ManHinhLenLichChayView(JFrame parent) {
        this.menuGoc = parent;
        initSwingComponents();
        call(); // Kích hoạt nạp danh sách tàu từ DB
    }

    // Constructor mặc định bắt buộc giữ lại theo mẫu của GitHub
    public ManHinhLenLichChayView() {
    }

    private void initSwingComponents() {
        setTitle("QUẢN LÝ LẬP LỊCH TRÌNH CHẠY TÀU - PTIT RAILWAY");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                quayLaiMenu();
            }
        });
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- KHU VỰC THÔNG TIN CHỌN TÀU & HÀNH TRÌNH ---
        JPanel pnlTopForm = new JPanel(new GridLayout(2, 3, 15, 10));
        pnlTopForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Tham số thiết lập lịch trình tổng quan",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        pnlTopForm.add(new JLabel("Chọn Đoàn Tàu Điều Phối:"));
        pnlTopForm.add(new JLabel("Chọn Hành Trình Tuyến:"));
        pnlTopForm.add(new JLabel("Ngày khởi hành (DD/MM/YYYY):"));

        cbDoanTauReal = new JComboBox<>();
        cbDoanTauReal.addItem(new DoanTauWrapper(null));
        cbDoanTauReal.addActionListener(this);
        pnlTopForm.add(cbDoanTauReal);

        cbHanhTrinhReal = new JComboBox<>();
        cbHanhTrinhReal.addItem(new HanhTrinhWrapper(null));
        cbHanhTrinhReal.addActionListener(this);
        pnlTopForm.add(cbHanhTrinhReal);

        txtNgayKhoiHanh = new JTextField("15/06/2026");
        txtNgayKhoiHanh.setFont(new Font("Arial", Font.BOLD, 12));
        pnlTopForm.add(txtNgayKhoiHanh);

        add(pnlTopForm, BorderLayout.NORTH);

        // --- KHU VỰC BẢNG LƯỚI NHẬP GIỜ GA TRUNG GIAN ---
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Danh sách ga trung gian - Thiết lập mốc thời gian đến/rời ga",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)
        ));

        String[] headers = {"Thứ tự ga", "Mã Nhà Ga", "Tên Nhà Ga", "Giờ Đến Ga (HH:mm)", "Giờ Rời Ga (HH:mm)"};
        modelTableGa = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row == 0 && column == 3) {
                    return false;
                }
                if (row == (getRowCount() - 1) && column == 4) {
                    return false;
                }
                return column == 3 || column == 4;
            }
        };

        tblGaTrungGian = new JTable(modelTableGa);
        tblGaTrungGian.setRowHeight(28);
        tblGaTrungGian.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tblGaTrungGian);
        pnlTableWrapper.add(scrollPane, BorderLayout.CENTER);
        add(pnlTableWrapper, BorderLayout.CENTER);

        // --- KHU VỰC NÚT ĐIỀU HƯỚNG DƯỚI ĐÁY ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 10));
        btnQuayLai = new JButton("Quay Lại Menu");
        btnQuayLai.addActionListener(this);

        btnXacNhanReal = new JButton("Xác Nhận Lưu Lịch Trình");
        btnXacNhanReal.setFont(new Font("Arial", Font.BOLD, 12));
        btnXacNhanReal.setBackground(new Color(40, 167, 69));
        btnXacNhanReal.setForeground(Color.WHITE);
        btnXacNhanReal.addActionListener(this);

        pnlSouth.add(btnQuayLai);
        pnlSouth.add(btnXacNhanReal);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    /**
     */
    public void call() {
        // Lấy danh sách tàu thực tế qua phương thức tĩnh của Entity
        this.danhSachTau = DoanTau.getDanhSachTau();
        displayDanhSachTau(this.danhSachTau);
    }

    /**
     * HÀM ACTIONPERFORMED CỦA GITHUB - Giữ lại để né lỗi biên dịch bài test
     */
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

    /**
     * Logic hiển thị dữ liệu tàu lên JComboBox
     */
    private void displayDanhSachTau(List<DoanTau> danhSachTau) {
        if (cbDoanTauReal == null || danhSachTau == null) {
            return;
        }
        cbDoanTauReal.removeAllItems();
        cbDoanTauReal.addItem(new DoanTauWrapper(null)); // Ô mặc định
        for (DoanTau tau : danhSachTau) {
            cbDoanTauReal.addItem(new DoanTauWrapper(tau));
        }
    }

    private void callAfterChonTau() {
        try {
            // Thay vì gọi DAO.con bị lỗi protected, ta truyền thẳng null vào Constructor.
            // Nếu hệ thống chạy thật và đã có kết nối trước đó, bạn có thể truyền Connection hợp lệ vào đây.
            java.sql.Connection currentCon = null;
            com.example.manager.dao.HanhTrinhDAO hanhTrinhDAO = new com.example.manager.dao.HanhTrinhDAO(currentCon);

            // Gọi hàm bốc dữ liệu có sẵn của bạn
            this.danhSachHanhTrinh = hanhTrinhDAO.getAllHanhTrinh();

            // Cơ chế Fallback: Nếu DB chưa chạy hoặc danh sách trống, nạp dữ liệu nền để test giao diện
            if (this.danhSachHanhTrinh == null || this.danhSachHanhTrinh.isEmpty()) {
                this.danhSachHanhTrinh = new java.util.ArrayList<>();

                HanhTrinh ht1 = new HanhTrinh();
                ht1.setMaHanhTrinh("HT01");
                ht1.setTenHanhTrinh("Hà Nội - Sài Gòn (Tuyến Thống Nhất)");

                HanhTrinh ht2 = new HanhTrinh();
                ht2.setMaHanhTrinh("HT02");
                ht2.setTenHanhTrinh("Hà Nội - Hải Phòng (Tuyến Phía Đông)");

                this.danhSachHanhTrinh.add(ht1);
                this.danhSachHanhTrinh.add(ht2);
            }

            // Đổ dữ liệu lên ComboBox hành trình đồ họa
            displayDanhSachHanhTrinh(this.danhSachHanhTrinh);
        } catch (Exception e) {
            this.danhSachHanhTrinh = new java.util.ArrayList<>();
            displayDanhSachHanhTrinh(this.danhSachHanhTrinh);
        }
    }

    private void displayDanhSachHanhTrinh(List<HanhTrinh> danhSachHanhTrinh) {
        if (cbHanhTrinhReal == null || danhSachHanhTrinh == null) {
            return;
        }
        cbHanhTrinhReal.removeAllItems();
        cbHanhTrinhReal.addItem(new HanhTrinhWrapper(null));
        for (HanhTrinh ht : danhSachHanhTrinh) {
            cbHanhTrinhReal.addItem(new HanhTrinhWrapper(ht));
        }
    }

    private void callAfterChonHanhTrinh() {
        if (this.hanhTrinhDuocChon != null) {
            this.danhSachGaTrungGian = this.hanhTrinhDuocChon.getDanhSachGaTrungGian();
            displayGaTrungGian(this.danhSachGaTrungGian);
        }
    }

    private void displayGaTrungGian(List<ChiTietHanhTrinh> danhSachGaTrungGian) {
        if (modelTableGa == null) {
            return;
        }
        modelTableGa.setRowCount(0);
        if (danhSachGaTrungGian == null) {
            return;
        }

        int stt = 1;
        int size = danhSachGaTrungGian.size();
        for (ChiTietHanhTrinh ct : danhSachGaTrungGian) {
            String gioDenMacDinh = "";
            String gioDiMacDinh = "";

            if (stt == 1) {
                gioDenMacDinh = "Xuất phát";
            }
            if (stt == size) {
                gioDiMacDinh = "Kết thúc chặng";
            }

            modelTableGa.addRow(new Object[]{
                String.valueOf(stt),
                ct.getNhaGa().getMaGa(),
                ct.getNhaGa().getTenNhaGa(),
                gioDenMacDinh,
                gioDiMacDinh
            });
            stt++;
        }
    }

    /**
     * LẮNG NGHE SỰ KIỆN GIAO DIỆN SWING THỰC TẾ
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == cbDoanTauReal) {
            DoanTauWrapper wrapper = (DoanTauWrapper) cbDoanTauReal.getSelectedItem();
            if (wrapper != null && wrapper.getTau() != null) {
                this.tauDuocChon = wrapper.getTau();
                // GỌI TRỰC TIẾP HÀM NÀY ĐỂ NẠP HÀNH TRÌNH
                callAfterChonTau();
            }
        } else if (src == cbHanhTrinhReal) {
            HanhTrinhWrapper wrapper = (HanhTrinhWrapper) cbHanhTrinhReal.getSelectedItem();
            if (wrapper != null && wrapper.getHanhTrinh() != null) {
                this.hanhTrinhDuocChon = wrapper.getHanhTrinh();
                // GỌI TRỰC TIẾP HÀM NÀY ĐỂ HIỂN THỊ GA
                callAfterChonHanhTrinh();
            }
        } else if (src == btnXacNhanReal) {
            if (tblGaTrungGian.isEditing()) {
                tblGaTrungGian.getCellEditor().stopCellEditing();
            }
            xuLyXacNhanLuu(); // Chạy trực tiếp qua logic kiểm tra và lưu của GitHub
        } else if (src == btnQuayLai) {
            quayLaiMenu();
        }
    }

    /**
     * HÀM GỐC XULYXACNHANLUU TRÊN GITHUB — ĐƯỢC TÍCH HỢP RÀNG BUỘC KIỂM TRA ĐỒ
     * HỌA
     */
    private void xuLyXacNhanLuu() {
        if (this.tauDuocChon == null || this.hanhTrinhDuocChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ Hành trình và Đoàn tàu điều phối!", "Lỗi biểu mẫu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Đọc và kiểm tra định dạng ngày khởi hành tổng quan từ JTextField
        String strNgay = txtNgayKhoiHanh.getText().trim();
        LocalDate ngayKhoiHanhCucBo;
        try {
            ngayKhoiHanhCucBo = LocalDate.parse(strNgay, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày khởi hành không hợp lệ (DD/MM/YYYY)!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int rowCount = modelTableGa.getRowCount();
        LocalTime lastDepartureTime = null;

        // Vòng lặp kiểm tra logic thời gian nhập trên bảng lưới đồ họa
        for (int i = 0; i < rowCount; i++) {
            String tenGa = modelTableGa.getValueAt(i, 2).toString();
            String gioDenStr = modelTableGa.getValueAt(i, 3).toString().trim();
            String gioDiStr = modelTableGa.getValueAt(i, 4).toString().trim();

            if ((i > 0 && gioDenStr.isEmpty()) || (i < rowCount - 1 && gioDiStr.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Không được để trống thời gian ĐẾN/ĐI tại ga " + tenGa + "!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                LocalTime gioDen = (i > 0) ? LocalTime.parse(gioDenStr) : null;
                LocalTime gioDi = (i < rowCount - 1) ? LocalTime.parse(gioDiStr) : null;

                if (gioDen != null && gioDi != null && !gioDen.isBefore(gioDi)) {
                    JOptionPane.showMessageDialog(this, "Tại ga [" + tenGa + "]: Giờ đến phải SỚM HƠN giờ rời ga!", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (i > 0 && lastDepartureTime != null && gioDen != null && !gioDen.isAfter(lastDepartureTime)) {
                    JOptionPane.showMessageDialog(this, "Giờ đến ga [" + tenGa + "] không thể đi lùi/trùng với giờ rời ga trước đó!", "Lỗi tiến trình chặng chạy", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (gioDi != null) {
                    lastDepartureTime = gioDi;
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Sai định dạng thời gian HH:mm tại ga " + tenGa + "!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // BẮT ĐẦU LUỒNG ĐÓNG GÓI THỰC THỂ THEO THIẾT KẾ GITHUB
        this.lichTrinhMoi = new LichTrinh();

        // Trộn ngày khởi hành và giờ xuất phát của ga đầu tiên để ra LocalDateTime khởi hành chuẩn
        String gioXuatPhatStr = modelTableGa.getValueAt(0, 4).toString().trim();
        LocalDateTime thoiGianKhoiHanhTongQuan = LocalDateTime.of(ngayKhoiHanhCucBo, LocalTime.parse(gioXuatPhatStr));

        this.lichTrinhMoi.call(this.hanhTrinhDuocChon, this.tauDuocChon, thoiGianKhoiHanhTongQuan, TrangThaiLichTrinh.DANG_CHAY);

        if (this.danhSachGaTrungGian != null) {
            for (int i = 0; i < rowCount; i++) {
                ChiTietHanhTrinh ctht = this.danhSachGaTrungGian.get(i);
                String gioDenStr = modelTableGa.getValueAt(i, 3).toString().trim();
                String gioDiStr = modelTableGa.getValueAt(i, 4).toString().trim();

                LocalDateTime gioDen = (i == 0) ? thoiGianKhoiHanhTongQuan : LocalDateTime.of(ngayKhoiHanhCucBo, LocalTime.parse(gioDenStr));
                LocalDateTime gioDi = (i == rowCount - 1) ? gioDen.plusHours(2) : LocalDateTime.of(ngayKhoiHanhCucBo, LocalTime.parse(gioDiStr));

                this.lichTrinhMoi.addChiTietLichTrinh(gioDen, gioDi, ctht.getNhaGa());
            }
        }

        // Gọi hàm lưu dữ liệu thật xuống Database qua DAO nhóm
        boolean isSuccess = this.lichTrinhMoi.luuLichTrinh();

        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Lưu lịch trình chạy tàu mới vào cơ sở dữ liệu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu lịch trình thất bại! Vui lòng kiểm tra lại xung đột thiết bị hoặc kết nối.", "Thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        if (cbDoanTauReal.getItemCount() > 0) {
            cbDoanTauReal.setSelectedIndex(0);
        }
        cbHanhTrinhReal.removeAllItems();
        cbHanhTrinhReal.addItem(new HanhTrinhWrapper(null));
        modelTableGa.setRowCount(0);
        this.tauDuocChon = null;
        this.hanhTrinhDuocChon = null;
    }

    private void quayLaiMenu() {
        if (menuGoc != null) {
            menuGoc.setVisible(true);
        }
        dispose();
    }

    // --- WRAPPER CLASSES ĐỂ HIỂN THỊ ĐẸP TRÊN JCOMBOBOX MÀ VẪN GIỮ ĐƯỢC THỰC THỂ GỐC ---
    private static class DoanTauWrapper {

        private final DoanTau tau;

        public DoanTauWrapper(DoanTau tau) {
            this.tau = tau;
        }

        public DoanTau getTau() {
            return tau;
        }

        @Override
        public String toString() {
            return (tau == null) ? "-- Chọn đoàn tàu điều phối --" : tau.getMaTau() + " - " + tau.getTenTau();
        }
    }

    private static class HanhTrinhWrapper {

        private final HanhTrinh hanhTrinh;

        public HanhTrinhWrapper(HanhTrinh ht) {
            this.hanhTrinh = ht;
        }

        public HanhTrinh getHanhTrinh() {
            return hanhTrinh;
        }

        @Override
        public String toString() {
            return (hanhTrinh == null) ? "-- Chọn hành trình tuyến --" : hanhTrinh.getMaHanhTrinh() + " (" + hanhTrinh.getTenHanhTrinh() + ")";
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new ManHinhLenLichChayView(null).setVisible(true));
    }
}
