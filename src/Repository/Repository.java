/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Form_Staff.KhachHang_Form;

import Model.ModelKH;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ListModel;
import java.util.Date;
import java.sql.PreparedStatement;
import main.config.DBConnect;


public class Repository {
    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql;

    public ArrayList<ModelKH> getAll() {
        ArrayList<ModelKH> list = new ArrayList<>();
        String sql = """
                     SELECT 
                           [MaKhachHang]
                           ,[Ten]
                           ,[SoDienThoai]
                           ,[Email]
                           ,[GioiTinh]
                           ,[DiaChi]
                           ,[NgayThamGia]
                       FROM [dbo].[KhachHang]
                     """;

        try (Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String makhachhang = rs.getString(1);
                String ten = rs.getString(2);
                String sodt = rs.getString(3);
                String email = rs.getString(4);
                boolean gioitinh = rs.getBoolean(5);
                String diachi = rs.getString(6);
                String ngaythamgia = rs.getString(7);
                ModelKH m1 = new ModelKH(makhachhang, ten, sodt, email, gioitinh, diachi, ngaythamgia);
                list.add(m1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean isCustomerExist(String phone) {
        String sql = "SELECT COUNT(*) FROM [dbo].[KhachHang] WHERE [SoDienThoai] = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addCustomer(ModelKH customer) {
        String sql = """
                     INSERT INTO [dbo].[KhachHang]
                           ([Ten], [SoDienThoai], [Email], [GioiTinh], [DiaChi], [NgayThamGia])
                     VALUES (?, ?, ?, ?, ?, ?)
                     """;
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customer.getTen());
            ps.setString(2, customer.getSodt());
            ps.setString(3, customer.getEmail());
            ps.setBoolean(4, customer.isGioitinh());
            ps.setString(5, customer.getDiachi());
            ps.setString(6, customer.getNgaythamgia());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println(new Repository().getAll());
    }
}

   
        


