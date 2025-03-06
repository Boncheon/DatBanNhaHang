/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class Voucher {
    
    private String ID_Voucher;
    private String maVoucher;
    private String loaiVoucher;
    private String trangThai;
    private String ngayBatDau;
    private String ngayKetThuc;
    private float phanTram;
    private float soTienGiam;
    private float soLanDaDung;
    private int gioiHanLuotDung;
    private float tienGiamToiDa;
    private float tienGiamToiThieu;
    private String moTa;

    public Voucher() {
    }

    public Voucher(String ID_Voucher, String maVoucher, String loaiVoucher, String trangThai, String ngayBatDau, String ngayKetThuc, float phanTram, float soTienGiam, float soLanDaDung, int gioiHanLuotDung, float tienGiamToiDa, float tienGiamToiThieu, String moTa) {
        this.ID_Voucher = ID_Voucher;
        this.maVoucher = maVoucher;
        this.loaiVoucher = loaiVoucher;
        this.trangThai = trangThai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.phanTram = phanTram;
        this.soTienGiam = soTienGiam;
        this.soLanDaDung = soLanDaDung;
        this.gioiHanLuotDung = gioiHanLuotDung;
        this.tienGiamToiDa = tienGiamToiDa;
        this.tienGiamToiThieu = tienGiamToiThieu;
        this.moTa = moTa;
    }

    public String getID_Voucher() {
        return ID_Voucher;
    }

    public void setID_Voucher(String ID_Voucher) {
        this.ID_Voucher = ID_Voucher;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public float getPhanTram() {
        return phanTram;
    }

    public void setPhanTram(float phanTram) {
        this.phanTram = phanTram;
    }

    public float getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(float soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public float getSoLanDaDung() {
        return soLanDaDung;
    }

    public void setSoLanDaDung(float soLanDaDung) {
        this.soLanDaDung = soLanDaDung;
    }

    public int getGioiHanLuotDung() {
        return gioiHanLuotDung;
    }

    public void setGioiHanLuotDung(int gioiHanLuotDung) {
        this.gioiHanLuotDung = gioiHanLuotDung;
    }

    public float getTienGiamToiDa() {
        return tienGiamToiDa;
    }

    public void setTienGiamToiDa(float tienGiamToiDa) {
        this.tienGiamToiDa = tienGiamToiDa;
    }

    public float getTienGiamToiThieu() {
        return tienGiamToiThieu;
    }

    public void setTienGiamToiThieu(float tienGiamToiThieu) {
        this.tienGiamToiThieu = tienGiamToiThieu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

   
    
    
}
