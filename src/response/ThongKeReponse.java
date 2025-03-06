/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package response;

import lombok.Builder;

/**
 *
 * @author Admin
 */
@Builder
public class ThongKeReponse {
        private String maMonAn;
    private String tenMonAn;
    private float gia;
    private float thanhTien;
    private int soLuong;

    public ThongKeReponse() {
    }

    public ThongKeReponse(String maMonAn, String tenMonAn, float gia, float thanhTien, int soLuong) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
        this.thanhTien = thanhTien;
        this.soLuong = soLuong;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public float getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(float thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
}
