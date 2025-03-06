/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class ModelNhanVien {
    private String stt;
    private String id_NV;
    private String tenNV;
    private String sdt;
    private String diachi;
    private String ngaySinh;
    private String ngayVL;
    private String email;
    private boolean gioiTinh;
    private String chucvu;
    private boolean trangThai;
    private String cccd;
    private String matKhau;
    
    public ModelNhanVien() {
    }



    public ModelNhanVien(String stt, String id_NV, String tenNV, String sdt, String diachi, String ngaySinh, String ngayVL, String email, boolean gioiTinh, String chucvu, boolean trangThai, String cccd, String matKhau) {
        this.stt = stt;
        this.id_NV = id_NV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ngaySinh = ngaySinh;
        this.ngayVL = ngayVL;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.chucvu = chucvu;
        this.trangThai = trangThai;
        this.cccd = cccd;
        this.matKhau = matKhau;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getId_NV() {
        return id_NV;
    }

    public void setId_NV(String id_NV) {
        this.id_NV = id_NV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getNgayVL() {
        return ngayVL;
    }

    public void setNgayVL(String ngayVL) {
        this.ngayVL = ngayVL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
 

   
   
}