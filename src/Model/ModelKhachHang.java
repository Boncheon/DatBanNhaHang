/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bouncycastle.asn1.dvcs.Data;

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
public class ModelKhachHang {

  

   

  
  private String makhachhang;
  private String ten;
  private String sodt;
  private String email;
  private boolean  gioitinh;
  private String diachi;
  private String ngaythamgia;

   
   
  
}

