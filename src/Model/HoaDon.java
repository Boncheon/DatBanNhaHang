package Model;

public class HoaDon {
    private int idDatBan;
    private int idKhachHang;
    private int idNhanVien;
    private double tongTien;
    private double phuPhi;
    private double chietKhau;
    private double tongSauGiam;
    private boolean trangThai;

    // Getters and Setters
    public int getIdDatBan() {
        return idDatBan;
    }

    public void setIdDatBan(int idDatBan) {
        this.idDatBan = idDatBan;
    }

    public int getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(int idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public int getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(int idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getPhuPhi() {
        return phuPhi;
    }

    public void setPhuPhi(double phuPhi) {
        this.phuPhi = phuPhi;
    }

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        this.chietKhau = chietKhau;
    }

    public double getTongSauGiam() {
        return tongSauGiam;
    }

    public void setTongSauGiam(double tongSauGiam) {
        this.tongSauGiam = tongSauGiam;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
