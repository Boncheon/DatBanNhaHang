/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Form_Staff.MenuManagement_Form;
import Model.ModelLoaiMonAn;
import Model.ModelMonAn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;
import response.MonAn_response;

/**
 *
 * @author Admin
 */
public class LoaiMon_rp {
         public ArrayList<ModelLoaiMonAn> getAll(){
            ArrayList<ModelLoaiMonAn> list = new ArrayList<>();
            // CODE
            //b1: tạo sql
            String sql ="""
SELECT [ID_LoaiMonAn]
      ,[MaLoaiMonAn]
      ,[TenLoaiMonAn]
  FROM [QuanLyNhaHang].[dbo].[LoaiMonAn]
  ORDER BY [MaLoaiMonAn] DESC;
                        """;
            //B2: tạo bảng kết nối
            // try ..... with......resources
            try (Connection con = DBConnect.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)){
                // kết quả trả ra 1 bảng  ==> 1ResultSet
                ResultSet rs = ps.executeQuery();
                //Sql==> Select ==> execteQuery
                // doc lan luot
                while (rs.next()) {                    
                    // B3: Tạo 1 đối tượng
                    ModelLoaiMonAn  loaimon = ModelLoaiMonAn.builder()
                    .maLoaimon(rs.getString(2))
                    .Id_Loaimon(rs.getInt(1))
                    .tenLoaimon(rs.getString(3))
                    .build();
                    list.add(loaimon);
                }
                return list;
            } catch (Exception e) {
            e.printStackTrace(System.out);
            }
            return null;
        }
             public ModelLoaiMonAn getLoaiMaByMa(String TenMon){
        String query = """
SELECT [ID_LoaiMonAn]
                            ,[MaLoaiMonAn]
                            ,[TenLoaiMonAn]
                        FROM [dbo].[LoaiMonAn]
                        Where[TenLoaiMonAn] =?
                      """;
        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            // Set gia tri cho dau hoi cham 
            ps.setObject(1, TenMon);
            ResultSet rs = ps.executeQuery(); // Lay ket qua

            while (rs.next()) {
                ModelLoaiMonAn lm = new ModelLoaiMonAn(rs.getInt(1), rs.getString(2), rs.getString(3));
                return lm;
            }
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }
             
      public boolean add(ModelLoaiMonAn MoAn) {

        int check = 0;

        String sql = """
INSERT INTO [dbo].[LoaiMonAn]
           ([TenLoaiMonAn])
     VALUES
           (?)         
                    """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, MoAn.getTenLoaimon());
            
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }         
     public List<String> getAllLoaiMon() {
        List<String> loaiMonList = new ArrayList<>();
        String sql = "SELECT DISTINCT TenLoaiMonAn FROM LoaiMonAn";
        
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                loaiMonList.add(rs.getString("TenLoaiMonAn"));
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return loaiMonList;
    }
}
