package com.example.manager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.manager.enums.TrangThaiLichTrinh;

public class LichTrinh {

    private String maLichTrinh;
    private LocalDateTime ngayKhoiHanh;
    private TrangThaiLichTrinh trangThai;
    private List<ChiTietLichTrinh> chiTietLichTrinh;
    private List<VeTau> veTau;
    private HanhTrinh hanhTrinh;
    private DoanTau doanTau;

    private String maHanhTrinh;
    private String maTau;
    private String ngayKhoiHanhStr;
    private String gioDi;
    private String gioDen;

    // === Constructor mặc định ===
    public LichTrinh() {
        this.chiTietLichTrinh = new ArrayList<>();
        this.veTau = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số ===
    public LichTrinh(String maLichTrinh, LocalDateTime ngayKhoiHanh, TrangThaiLichTrinh trangThai,
            HanhTrinh hanhTrinh, DoanTau doanTau) {
        this();
        this.maLichTrinh = maLichTrinh;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.trangThai = trangThai;
        this.hanhTrinh = hanhTrinh;
        this.doanTau = doanTau;
    }


    public LichTrinh(String maLichTrinh, String maTau, String gioDi, String gioDen) {
        this();
        this.maLichTrinh = maLichTrinh;
        this.maTau = maTau;
        this.gioDi = gioDi;
        this.gioDen = gioDen;
    }


    public String getMaHanhTrinh() {
        return maHanhTrinh;
    }

    public void setMaHanhTrinh(String maHanhTrinh) {
        this.maHanhTrinh = maHanhTrinh;
    }

    public String getMaTau() {
        return maTau != null ? maTau : (doanTau != null ? doanTau.getMaTau() : "");
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getNgayKhoiHanhStr() {
        return ngayKhoiHanhStr;
    }

    public void setNgayKhoiHanhStr(String ngayKhoiHanhStr) {
        this.ngayKhoiHanhStr = ngayKhoiHanhStr;
    }

    public String getGioDi() {
        return gioDi;
    }

    public void setGioDi(String gioDi) {
        this.gioDi = gioDi;
    }

    public String getGioDen() {
        return gioDen;
    }

    public void setGioDen(String gioDen) {
        this.gioDen = gioDen;
    }

    
    public static List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        return new ArrayList<>();
    }

    public static LichTrinh layThongTinLichTrinh(String maTau) {
        return new LichTrinh();
    }

    public void call(HanhTrinh hanhTrinh, DoanTau doanTau, LocalDateTime ngayKhoiHanh, TrangThaiLichTrinh trangThai) {
        this.hanhTrinh = hanhTrinh;
        this.doanTau = doanTau;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.trangThai = trangThai;
        this.chiTietLichTrinh = new ArrayList<>();
    }

    public void addChiTietLichTrinh(LocalDateTime gioDen, LocalDateTime gioDi, NhaGa nhaGa) {
        ChiTietLichTrinh ct = new ChiTietLichTrinh(null, gioDen, gioDi, nhaGa);
        this.chiTietLichTrinh.add(ct);
    }

    public boolean luuLichTrinh() {
        com.example.manager.dao.LichTrinhDAO dao = new com.example.manager.dao.LichTrinhDAO();
        return dao.addLichTrinh(this);
    }

    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public LocalDateTime getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public TrangThaiLichTrinh getTrangThai() {
        return trangThai;
    }

    public List<ChiTietLichTrinh> getChiTietLichTrinh() {
        return new ArrayList<>(chiTietLichTrinh);
    }
    public List<VeTau> getVeTau() {
        return new ArrayList<>(veTau);
    }

    public void addVeTau(VeTau ve) {
        if (veTau == null) {
            veTau = new ArrayList<>();
        }
        veTau.add(ve);
    }

    public HanhTrinh getHanhTrinh() {
        return hanhTrinh;
    }

    public DoanTau getDoanTau() {
        return doanTau;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        this.maLichTrinh = maLichTrinh;
    }

    public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public void setTrangThai(TrangThaiLichTrinh trangThai) {
        this.trangThai = trangThai;
    }

    public void setHanhTrinh(HanhTrinh hanhTrinh) {
        this.hanhTrinh = hanhTrinh;
    }

    public void setDoanTau(DoanTau doanTau) {
        this.doanTau = doanTau;
    }

    public List<GheNgoi> xuLyQuetSoDoGheToaXe(java.sql.Connection con, String maLichTrinh, String tenToa) throws Exception {
        List<GheNgoi> list = new ArrayList<>();
        String sql = "SELECT g.maGhe, g.soGhe, g.viTri, g.trangThai, t.maToa "
                + "FROM GheNgoi g "
                + "JOIN ToaTau t ON g.toaTauId = t.id "
                + "JOIN DoanTau dt ON t.doanTauId = dt.id "
                + "JOIN LichTrinh lt ON lt.doanTauId = dt.id "
                + "WHERE lt.maLichTrinh = ? AND t.tenToa = ?";

        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            ps.setString(2, tenToa);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GheNgoi g = new GheNgoi();
                    g.setMaGhe(rs.getString("maGhe"));
                    g.setSoGhe(rs.getInt("soGhe")); // Ép kiểu số int đồng bộ chuẩn nhóm
                    g.setViTri(rs.getString("viTri"));

                    String status = rs.getString("trangThai");
                    if ("DA_DAT".equalsIgnoreCase(status)) {
                        g.setTrangThai(com.example.manager.enums.TrangThaiGhe.DA_DAT);
                    } else if ("TAM_GIU".equalsIgnoreCase(status)) {
                        g.setTrangThai(com.example.manager.enums.TrangThaiGhe.TAM_GIU);
                    } else {
                        g.setTrangThai(com.example.manager.enums.TrangThaiGhe.TRONG);
                    }

                    try {
                        g.setMaToa(rs.getString("maToa"));
                    } catch (Exception ignored) {
                    }
                    list.add(g);
                }
            }
        }
        return list;
    }
}
