/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author TriKien
 */
public class ModelThongTinDatBan {
    private String MaDatBan;
    private String TenKhachHang;
    private String NgayDat;
    private String GioDatBan;
    private int SoNguoi;
    private String TenBan;

    public ModelThongTinDatBan() {
    }

    public ModelThongTinDatBan(String MaDatBan, String TenKhachHang, String NgayDat, String GioDatBan, int SoNguoi, String TenBan) {
        this.MaDatBan = MaDatBan;
        this.TenKhachHang = TenKhachHang;
        this.NgayDat = NgayDat;
        this.GioDatBan = GioDatBan;
        this.SoNguoi = SoNguoi;
        this.TenBan = TenBan;
    }

    public String getMaDatBan() {
        return MaDatBan;
    }

    public void setMaDatBan(String MaDatBan) {
        this.MaDatBan = MaDatBan;
    }

    public String getTenKhachHang() {
        return TenKhachHang;
    }

    public void setTenKhachHang(String TenKhachHang) {
        this.TenKhachHang = TenKhachHang;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String NgayDat) {
        this.NgayDat = NgayDat;
    }

    public String getGioDatBan() {
        return GioDatBan;
    }

    public void setGioDatBan(String GioDatBan) {
        this.GioDatBan = GioDatBan;
    }

    public int getSoNguoi() {
        return SoNguoi;
    }

    public void setSoNguoi(int SoNguoi) {
        this.SoNguoi = SoNguoi;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String TenBan) {
        this.TenBan = TenBan;
    }
    
    
}
