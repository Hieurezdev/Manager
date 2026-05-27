package com.example.manager.dao;

import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;

import java.sql.Connection;
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
        BaoCao baoCao = new BaoCao();
        baoCao.luuKetQuaThongKe(maBaoCao, dsKetQua == null ? new ArrayList<>() : dsKetQua);
    }

}
