package com.example.manager.dao;

import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaoCaoDAO extends DAO {
    public BaoCaoDAO(Connection con) {
        super(con);
    }

    public boolean kiemTraHopLe(LocalDate ngayBD, LocalDate ngayKT) {
        return new BaoCao().kiemTraHopLe(ngayBD, ngayKT);
    }

    public BaoCao taoMoiBaoCao(LocalDate ngayBD, LocalDate ngayKT) {
        return BaoCao.taoMoiBaoCao(ngayBD, ngayKT);
    }

    public void sapXepDoanhThuGiamDan(BaoCao baoCao) {
        if (baoCao != null) {
            baoCao.sapXepDoanhThuGiamDan();
        }
    }


    public void luuKetQuaThongKe(String maBaoCao, List<ChiTietBaoCao> dsKetQua) {
        if (con != null) {
            List<ChiTietBaoCao> list = dsKetQua == null ? new ArrayList<>() : dsKetQua;
            int tongDoanhThu = 0;
            LocalDate ngayBatDau = null;
            LocalDate ngayKetThuc = null;

            for (ChiTietBaoCao ct : list) {
                tongDoanhThu += ct.getDoanhThuChuyen();
                if (ct.getLichTrinh() != null && ct.getLichTrinh().getNgayKhoiHanh() != null) {
                    LocalDate ngay = ct.getLichTrinh().getNgayKhoiHanh().toLocalDate();
                    if (ngayBatDau == null || ngay.isBefore(ngayBatDau)) {
                        ngayBatDau = ngay;
                    }
                    if (ngayKetThuc == null || ngay.isAfter(ngayKetThuc)) {
                        ngayKetThuc = ngay;
                    }
                }
            }

            if (ngayBatDau == null) {
                ngayBatDau = LocalDate.now();
                ngayKetThuc = LocalDate.now();
            }

            String insertBaoCao = "INSERT INTO BaoCao (maBaoCao, ngayLapBaoCao, tongDoanhThu, ngayBatDau, ngayKetThuc) " +
                                  "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?) " +
                                  "ON DUPLICATE KEY UPDATE tongDoanhThu = VALUES(tongDoanhThu), " +
                                  "ngayBatDau = VALUES(ngayBatDau), ngayKetThuc = VALUES(ngayKetThuc)";
            try (PreparedStatement ps = con.prepareStatement(insertBaoCao)) {
                ps.setString(1, maBaoCao);
                ps.setInt(2, tongDoanhThu);
                ps.setDate(3, java.sql.Date.valueOf(ngayBatDau));
                ps.setDate(4, java.sql.Date.valueOf(ngayKetThuc));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String insertChiTiet = "INSERT INTO ChiTietBaoCao " +
                                   "(maCTBC, soVeBan, doanhThuChuyen, tiLeLapDay, lichTrinhId, baoCaoId) " +
                                   "VALUES (?, ?, ?, ?, (SELECT id FROM LichTrinh WHERE maLichTrinh = ?), " +
                                   "(SELECT id FROM BaoCao WHERE maBaoCao = ?)) " +
                                   "ON DUPLICATE KEY UPDATE soVeBan = VALUES(soVeBan), " +
                                   "doanhThuChuyen = VALUES(doanhThuChuyen), tiLeLapDay = VALUES(tiLeLapDay)";
            try (PreparedStatement ps = con.prepareStatement(insertChiTiet)) {
                for (ChiTietBaoCao ct : list) {
                    String maLichTrinh = ct.getLichTrinh() == null ? null : ct.getLichTrinh().getMaLichTrinh();
                    ps.setString(1, ct.getMaCTBC());
                    ps.setInt(2, ct.getSoVeBan());
                    ps.setInt(3, ct.getDoanhThuChuyen());
                    ps.setDouble(4, ct.getTiLeLapDay());
                    ps.setString(5, maLichTrinh);
                    ps.setString(6, maBaoCao);
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        BaoCao baoCao = new BaoCao();
        baoCao.luuKetQuaThongKe(maBaoCao, dsKetQua == null ? new ArrayList<>() : dsKetQua);
    }

}
