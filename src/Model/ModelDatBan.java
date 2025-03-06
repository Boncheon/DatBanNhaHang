package Model;

import java.util.Date;

public class ModelDatBan {
    private int ID_DatBan;
    private int ID_NhanVien;
    private int ID_KhachHang;
    private String ID_Ban;
    private Date NgayDat;
    private String GioDat;
    private int SoNguoi;
    private String TrangThai;
    private String GhiChu;

    public ModelDatBan(int ID_DatBan, int ID_NhanVien, int ID_KhachHang, String ID_Ban, Date NgayDat, String GioDat, int SoNguoi, String TrangThai, String GhiChu) {
        this.ID_DatBan = ID_DatBan;
        this.ID_NhanVien = ID_NhanVien;
        this.ID_KhachHang = ID_KhachHang;
        this.ID_Ban = ID_Ban;
        this.NgayDat = NgayDat;
        this.GioDat = GioDat;
        this.SoNguoi = SoNguoi;
        this.TrangThai = TrangThai;
        this.GhiChu = GhiChu;
    }

    public int getID_DatBan() {
        return ID_DatBan;
    }

    public void setID_DatBan(int ID_DatBan) {
        this.ID_DatBan = ID_DatBan;
    }

    public int getID_NhanVien() {
        return ID_NhanVien;
    }

    public void setID_NhanVien(int ID_NhanVien) {
        this.ID_NhanVien = ID_NhanVien;
    }

    public int getID_KhachHang() {
        return ID_KhachHang;
    }

    public void setID_KhachHang(int ID_KhachHang) {
        this.ID_KhachHang = ID_KhachHang;
    }

    public String getID_Ban() {
        return ID_Ban;
    }

    public void setID_Ban(String ID_Ban) {
        this.ID_Ban = ID_Ban;
    }

    public Date getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(Date NgayDat) {
        this.NgayDat = NgayDat;
    }

    public String getGioDat() {
        return GioDat;
    }

    public void setGioDat(String GioDat) {
        this.GioDat = GioDat;
    }

    public int getSoNguoi() {
        return SoNguoi;
    }

    public void setSoNguoi(int SoNguoi) {
        this.SoNguoi = SoNguoi;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }
}
