package com.example.manager.entity;

import com.example.manager.enums.TrangThaiLichTrinh;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LichTrinh {
    // =========================================================================
    // CÁC THUỘC TÍNH GỐC TỪ GITHUB (Giữ nguyên vẹn 100%)
    // =========================================================================
    private String maLichTrinh;
    private LocalDateTime ngayKhoiHanh;
    private TrangThaiLichTrinh trangThai;
    private List<ChiTietLichTrinh> chiTietLichTrinh;
    private List<VeTau> veTau;
    private HanhTrinh hanhTrinh;
    private DoanTau doanTau;

    // === BỔ SUNG LẠI: Các trường String cũ phục vụ cho luồng Mua Vé của ông ===
    private String maHanhTrinh;
    private String maTau;
    private String ngayKhoiHanhStr;
    private String gioDi;
    private String gioDen;

    // === Constructor mặc định từ GitHub ===
    public LichTrinh() {
        this.chiTietLichTrinh = new ArrayList<>();
        this.veTau = new ArrayList<>();
    }

    // === Constructor đầy đủ tham số từ GitHub ===
    public LichTrinh(String maLichTrinh, LocalDateTime ngayKhoiHanh, TrangThaiLichTrinh trangThai,
                     HanhTrinh hanhTrinh, DoanTau doanTau) {
        this();
        this.maLichTrinh = maLichTrinh;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.trangThai = trangThai;
        this.hanhTrinh = hanhTrinh;
        this.doanTau = doanTau;
    }

    // === BỔ SUNG LẠI: Constructor 4 tham số cũ của ông (Cực kỳ quan trọng để LichTrinhDAO bốc DB không bị gãy) ===
    public LichTrinh(String maLichTrinh, String maTau, String gioDi, String gioDen) {
        this();
        this.maLichTrinh = maLichTrinh;
        this.maTau = maTau;
        this.gioDi = gioDi;
        this.gioDen = gioDen;
    }

    // === Thêm các Getter/Setter cho các thuộc tính cũ ông bổ sung ===
    public String getMaHanhTrinh() { return maHanhTrinh; }
    public void setMaHanhTrinh(String maHanhTrinh) { this.maHanhTrinh = maHanhTrinh; }
    public String getMaTau() { return maTau != null ? maTau : (doanTau != null ? doanTau.getMaTau() : ""); }
    public void setMaTau(String maTau) { this.maTau = maTau; }
    public String getNgayKhoiHanhStr() { return ngayKhoiHanhStr; }
    public void setNgayKhoiHanhStr(String ngayKhoiHanhStr) { this.ngayKhoiHanhStr = ngayKhoiHanhStr; }
    public String getGioDi() { return gioDi; }
    public void setGioDi(String gioDi) { this.gioDi = gioDi; }
    public String getGioDen() { return gioDen; }
    public void setGioDen(String gioDen) { this.gioDen = gioDen; }

    // =========================================================================
    // TOÀN BỘ LOGIC GỐC TỪ GITHUB (Giữ nguyên vẹn 100% để né Conflict)
    // =========================================================================
    public static List<LichTrinh> layDanhSachLichTrinhTrongKy(String maTau, LocalDate ngayBD, LocalDate ngayKT) {
        return new ArrayList<>();
    }

    public static LichTrinh layThongTinLichTrinh(String maTau) {
        return new LichTrinh();
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
}