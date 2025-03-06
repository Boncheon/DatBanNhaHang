package model;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private String tenKhachHang;
    private String soDienThoai; // New field for phone number
    private Date ngayDat;
    private Time gioDat;
    private int soNguoi;
    private String tenBan;
    private String ghiChu; // New field for notes
    private String trangThai;

    // Getters and Setters
    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Time getGioDat() {
        return gioDat;
    }

    public void setGioDat(Time gioDat) {
        this.gioDat = gioDat;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public int getDuration() {
        return 180; // 3 tiếng = 180 phút
    }
}
