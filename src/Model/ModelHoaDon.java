/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author nguye
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ModelHoaDon {

    private String ID_HoaDon;
    private String MaHoaDon;

    private String ID_NhanVien;
    private String ID_datban;
    private String ID_Voucher;
    private String NgayTao;
    private String TongTien;

    private String PhuPhi;

    private String TongSauGiam;
    private boolean TrangThai;
    private String chietkhau;
}
