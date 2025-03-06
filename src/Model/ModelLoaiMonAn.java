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
 * @author Admin
 */
@AllArgsConstructor // contrutor full tham so 
@NoArgsConstructor // contrutor k tham so 
@Getter
@Setter 
@ToString 
@Builder
public class ModelLoaiMonAn {
    private Integer Id_Loaimon;
    private String maLoaimon;
    private String tenLoaimon;
  }
