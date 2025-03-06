/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author nguye
 */
public class ModelKH {
    
    
  
  private String makhachhang;

    public ModelKH(String ten, String sodt, String email, boolean gioitinh, String diachi, String ngaythamgia) {
        this.ten = ten;
        this.sodt = sodt;
        this.email = email;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.ngaythamgia = ngaythamgia;
    }
  private String ten;
  private String sodt;
  private String email;
  private boolean  gioitinh;
  private String diachi;
  private String ngaythamgia;

    public ModelKH() {
    }

    public ModelKH(String makhachhang, String ten, String sodt, String email, boolean gioitinh, String diachi, String ngaythamgia) {
        this.makhachhang = makhachhang;
        this.ten = ten;
        this.sodt = sodt;
        this.email = email;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.ngaythamgia = ngaythamgia;
    }

    public String getMakhachhang() {
        return makhachhang;
    }

    public void setMakhachhang(String makhachhang) {
        this.makhachhang = makhachhang;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSodt() {
        return sodt;
    }

    public void setSodt(String sodt) {
        this.sodt = sodt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(boolean gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaythamgia() {
        return ngaythamgia;
    }

    public void setNgaythamgia(String ngaythamgia) {
        this.ngaythamgia = ngaythamgia;
    }
    public Object[]toDaTaRow(){
        return new Object[]{
            this.getMakhachhang(), this.getTen(), this.getSodt(), this.getEmail()
                , this.isGioitinh() ?"nam":"nu", this.getDiachi(), this.getNgaythamgia()
        };
    }
}
