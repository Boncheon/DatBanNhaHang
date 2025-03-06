/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repo;


import Model.ModelKhachHang;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ListModel;
import java.util.Date;
import java.sql.PreparedStatement;
import main.config.DBConnect;

/**
 *
 * @author nguye
 */
public class Repository {

    public ArrayList<ModelKhachHang> getAll() {
        String sql = """
                   SELECT [MaKhachHang] 
                             ,[Ten]
                             ,[SoDienThoai]
                             ,[Email]
                             ,[GioiTinh]
                             ,[DiaChi]
                             ,[NgayThamGia]
                         FROM [dbo].[KhachHang]
                     """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            ArrayList<ModelKhachHang> lists = new ArrayList<>();
            while (rs.next()) {
               ModelKhachHang response
                        = ModelKhachHang.builder()
                         
                        .makhachhang(rs.getString(1))
                        .ten(rs.getString(2))
                        .sodt(rs.getString(3))
                        .email(rs.getString(4))
                        .gioitinh(rs.getBoolean(5))
                        .diachi(rs.getString(6))
                        .ngaythamgia(rs.getString(7))
                        
                        .build();
                lists.add(response);
            }
            return lists;
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Repository().getAll());
    }

    public boolean add(ModelKhachHang kh) {

        int check = 0;

        String sql = """
                  INSERT INTO [dbo].[KhachHang]           
                            ( [Ten]
                             ,[SoDienThoai]
                             ,[Email]
                             ,[GioiTinh]
                             ,[DiaChi]
                             ,[NgayThamGia])
                       VALUES
                             (?,?,?,?,?,?)
                     
                    """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getTen());
            ps.setString(2, kh.getSodt());
            ps.setString(3, kh.getEmail());
            ps.setBoolean(4, kh.isGioitinh());
            ps.setString(5, kh.getDiachi());
            ps.setString(6, kh.getNgaythamgia());

            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }

    public boolean update(ModelKhachHang newKhachHang, String ma) {
        int check = 0;
        String sql = """
                    UPDATE [dbo].[KhachHang]
                        SET
                           [Ten] = ?
                           ,[SoDienThoai] =? 
                           ,[Email] = ?
                           ,[GioiTinh] =? 
                           ,[DiaChi] = ?
                           ,[NgayThamGia] =?
                      WHERE MaKhachHang = ?
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, newKhachHang.getTen());
            ps.setObject(2, newKhachHang.getSodt());
            ps.setObject(3, newKhachHang.getEmail());
            ps.setObject(4, newKhachHang.isGioitinh());
            ps.setObject(5, newKhachHang.getDiachi());
            ps.setObject(6, newKhachHang.getNgaythamgia());
            ps.setObject(7, ma);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }

   
}
