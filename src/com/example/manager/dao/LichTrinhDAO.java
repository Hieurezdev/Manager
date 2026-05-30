package com.example.manager.dao;

import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.ChiTietHanhTrinh;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.NhaGa;
import com.example.manager.entity.GheNgoi;
import com.example.manager.enums.LoaiTau;
import com.example.manager.enums.TrangThaiLichTrinh;
import com.example.manager.enums.TrangThaiTau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDAO extends DAO {

    private double giaVeGocTieuChuan = 850000;

    public LichTrinhDAO() {
        super(com.example.manager.dao.DBConnection.getConnection());
    }

    public LichTrinhDAO(Connection con) {
        super(con);
    }

    public double getGiaVeGocTieuChuan() {
        return giaVeGocTieuChuan;
    }

    // =========================================================================
    // 1. TOÀN BỘ CODE GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để diệt tận gốc Conflict)
    // =========================================================================
    public List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        if (con != null) {
            List<LichTrinh> list = new ArrayList<>();
            String sql = "SELECT l.id, l.maLichTrinh, l.ngayKhoiHanh, l.trangThai, "
                    + "ht.id AS htId, ht.maHanhTrinh, ht.tenHanhTrinh, ht.quangDuong, "
                    + "dt.id AS dtId, dt.maTau, dt.tenTau, dt.loaiTau, dt.trangThai AS ttTau "
                    + "FROM LichTrinh l "
                    + "JOIN DoanTau dt ON l.doanTauId = dt.id "
                    + "JOIN HanhTrinh ht ON l.hanhTrinhId = ht.id "
                    + "WHERE dt.maTau = ? AND l.ngayKhoiHanh >= ? AND l.ngayKhoiHanh < ? "
                    + "ORDER BY l.ngayKhoiHanh";
            LocalDateTime start = ngayBD == null
                    ? LocalDate.of(1970, 1, 1).atStartOfDay()
                    : ngayBD.atStartOfDay();
            LocalDateTime end = ngayKT == null
                    ? LocalDate.of(3000, 1, 1).atStartOfDay()
                    : ngayKT.plusDays(1).atStartOfDay();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maTau);
                ps.setTimestamp(2, Timestamp.valueOf(start));
                ps.setTimestamp(3, Timestamp.valueOf(end));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LichTrinh lichTrinh = new LichTrinh();
                        lichTrinh.setMaLichTrinh(rs.getString("maLichTrinh"));
                        Timestamp ts = rs.getTimestamp("ngayKhoiHanh");
                        if (ts != null) {
                            lichTrinh.setNgayKhoiHanh(ts.toLocalDateTime());
                        }
                        lichTrinh.setTrangThai(mapTrangThaiLichTrinh(rs.getString("trangThai")));

                        HanhTrinh hanhTrinh = new HanhTrinh();
                        hanhTrinh.setMaHanhTrinh(rs.getString("maHanhTrinh"));
                        hanhTrinh.setTenHanhTrinh(rs.getString("tenHanhTrinh"));
                        hanhTrinh.setQuangDuong(rs.getDouble("quangDuong"));
                        hanhTrinh.setChiTietHanhTrinh(loadChiTietHanhTrinh(rs.getInt("htId")));
                        lichTrinh.setHanhTrinh(hanhTrinh);

                        DoanTau tau = new DoanTau();
                        tau.setMaTau(rs.getString("maTau"));
                        tau.setTenTau(rs.getString("tenTau"));
                        tau.setLoaiTau(mapLoaiTau(rs.getString("loaiTau")));
                        tau.setTrangThai(mapTrangThaiTau(rs.getString("ttTau")));
                        lichTrinh.setDoanTau(tau);

                        list.add(lichTrinh);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        return LichTrinh.layDanhSachLichTrinhTrongKy(maTau, ngayBD, ngayKT);
    }

    public LichTrinh layThongTinLichTrinh(String maTau) {
        if (con != null) {
            String sql = "SELECT l.id, l.maLichTrinh, l.ngayKhoiHanh, l.trangThai, "
                    + "ht.id AS htId, ht.maHanhTrinh, ht.tenHanhTrinh, ht.quangDuong, "
                    + "dt.id AS dtId, dt.maTau, dt.tenTau, dt.loaiTau, dt.trangThai AS ttTau "
                    + "FROM LichTrinh l "
                    + "JOIN DoanTau dt ON l.doanTauId = dt.id "
                    + "JOIN HanhTrinh ht ON l.hanhTrinhId = ht.id "
                    + "WHERE dt.maTau = ? "
                    + "ORDER BY l.ngayKhoiHanh DESC LIMIT 1";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maTau);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        LichTrinh lichTrinh = new LichTrinh();
                        lichTrinh.setMaLichTrinh(rs.getString("maLichTrinh"));
                        Timestamp ts = rs.getTimestamp("ngayKhoiHanh");
                        if (ts != null) {
                            lichTrinh.setNgayKhoiHanh(ts.toLocalDateTime());
                        }
                        lichTrinh.setTrangThai(mapTrangThaiLichTrinh(rs.getString("trangThai")));

                        HanhTrinh hanhTrinh = new HanhTrinh();
                        hanhTrinh.setMaHanhTrinh(rs.getString("maHanhTrinh"));
                        hanhTrinh.setTenHanhTrinh(rs.getString("tenHanhTrinh"));
                        hanhTrinh.setQuangDuong(rs.getDouble("quangDuong"));
                        hanhTrinh.setChiTietHanhTrinh(loadChiTietHanhTrinh(rs.getInt("htId")));
                        lichTrinh.setHanhTrinh(hanhTrinh);

                        DoanTau tau = new DoanTau();
                        tau.setMaTau(rs.getString("maTau"));
                        tau.setTenTau(rs.getString("tenTau"));
                        tau.setLoaiTau(mapLoaiTau(rs.getString("loaiTau")));
                        tau.setTrangThai(mapTrangThaiTau(rs.getString("ttTau")));
                        lichTrinh.setDoanTau(tau);

                        return lichTrinh;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        return LichTrinh.layThongTinLichTrinh(maTau);
    }

    private List<ChiTietHanhTrinh> loadChiTietHanhTrinh(int hanhTrinhId) {
        List<ChiTietHanhTrinh> list = new ArrayList<>();
        String sql = "SELECT ct.maCTHT, ct.thuTuGa, ng.maGa, ng.tenNhaGa, ng.diaChi, ng.soDienThoai "
                + "FROM ChiTietHanhTrinh ct "
                + "JOIN NhaGa ng ON ct.nhaGaId = ng.id "
                + "WHERE ct.hanhTrinhId = ? ORDER BY ct.thuTuGa";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, hanhTrinhId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhaGa ga = new NhaGa(
                            rs.getString("maGa"),
                            rs.getString("tenNhaGa"),
                            rs.getString("diaChi"),
                            rs.getString("soDienThoai")
                    );
                    list.add(new ChiTietHanhTrinh(
                            rs.getString("maCTHT"),
                            rs.getInt("thuTuGa"),
                            ga
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private TrangThaiLichTrinh mapTrangThaiLichTrinh(String dbVal) {
        if ("DaHoanThanh".equalsIgnoreCase(dbVal)) {
            return TrangThaiLichTrinh.HOAN_THANH;
        }
        if ("BiHuy".equalsIgnoreCase(dbVal)) {
            return TrangThaiLichTrinh.HUY;
        }
        return TrangThaiLichTrinh.DANG_CHAY;
    }

    private LoaiTau mapLoaiTau(String dbVal) {
        if ("TauNhanh".equalsIgnoreCase(dbVal)) {
            return LoaiTau.THONG_NHAT;
        }
        if ("TauThuong".equalsIgnoreCase(dbVal)) {
            return LoaiTau.DIA_PHUONG;
        }
        return LoaiTau.KHAC;
    }

    private TrangThaiTau mapTrangThaiTau(String dbVal) {
        if ("SanSang".equalsIgnoreCase(dbVal)) {
            return TrangThaiTau.HOAT_DONG;
        }
        if ("BaoTri".equalsIgnoreCase(dbVal)) {
            return TrangThaiTau.BAO_TRI;
        }
        return TrangThaiTau.NGUNG;
    }

    public boolean addLichTrinh(LichTrinh lichTrinh) {
        if (this.con == null) {
            System.err.println("Lỗi: Không có kết nối tới CSDL");
            return false;
        }

        String sql = "INSERT INTO LichTrinh (maLichTrinh, ngayKhoiHanh, trangThai, doanTauId, hanhTrinhId, quanLyId) "
                + "VALUES (?, ?, 'ChuaChay', "
                + "(SELECT id FROM DoanTau WHERE maTau = ?), "
                + "(SELECT id FROM HanhTrinh WHERE maHanhTrinh = ?), 1)";

        try (PreparedStatement stmt = this.con.prepareStatement(sql)) {
            String maLichTrinh = "LT_" + lichTrinh.getDoanTau().getMaTau() + "_" + (System.currentTimeMillis() % 1000000);

            stmt.setString(1, maLichTrinh);
            stmt.setTimestamp(2, Timestamp.valueOf(lichTrinh.getNgayKhoiHanh()));
            stmt.setString(3, lichTrinh.getDoanTau().getMaTau());
            stmt.setString(4, lichTrinh.getHanhTrinh().getMaHanhTrinh());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Đã thêm Lịch Trình thành công vào DB: " + maLichTrinh);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // 2. CODE MUA VÉ CỦA ÔNG ĐẠT - ĐỒNG BỘ 100% DATABASE THẬT VÀ ĐẶC TẢ TUẦN TỰ
    // =========================================================================
    public List<LichTrinh> layDanhSachChuyenTauPhuHop(String gaDi, String gaDen, String ngayDi) throws Exception {
        // 2.1. Tính toán giá vé dựa trên quãng đường trong DB thật
        String sqlQD = "SELECT quangDuong FROM HanhTrinh WHERE maGaDi = ? AND maGaDen = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlQD)) {
            ps.setString(1, gaDi);
            ps.setString(2, gaDen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.giaVeGocTieuChuan = rs.getDouble("quangDuong") * 1000;
                }
            }
        }

        // 2.2. Lấy danh sách chuyến tàu chạy trong ngày từ DB thật
        List<LichTrinh> list = new ArrayList<>();
        String sqlLT = "SELECT lt.maLichTrinh, dt.maTau, ctt.gioDi, ctt.gioDen "
                + "FROM LichTrinh lt "
                + "JOIN DoanTau dt ON lt.doanTauId = dt.id "
                + "JOIN ChiTietLichTrinh ctt ON lt.maLichTrinh = ctt.maLichTrinh "
                + "WHERE CAST(lt.ngayKhoiHanh AS DATE) = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlLT)) {
            ps.setString(1, ngayDi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LichTrinh(rs.getString("maLichTrinh"), rs.getString("maTau"), rs.getString("gioDi"), rs.getString("gioDen")));
                }
            }
        }
        return list;
    }

    /**
     * Hàm bốc sơ đồ ghế ngồi ủy quyền qua lớp thực thể LichTrinh đúng chuẩn
     * kịch bản tuần tự nhóm
     */
    public List<GheNgoi> layThongTinLichTrinh(String maLichTrinh, String tenToa) throws Exception {
        // BƯỚC 1: Tiếp nhận tham số, kích hoạt luồng bằng cách gọi lớp thực thể LichTrinh
        LichTrinh lichTrinhEntity = new LichTrinh();

        // BƯỚC 2: Gọi tầng thực thể để phối hợp DoanTau, ToaTau, GheNgoi quét DB
        List<GheNgoi> listGhe = lichTrinhEntity.xuLyQuetSoDoGheToaXe(this.con, maLichTrinh, tenToa);

        // BƯỚC 3: Trả gói dữ liệu cấu trúc về cho giao diện vẽ sơ đồ
        return listGhe;
    }
}
