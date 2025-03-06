/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Form_Staff.MenuManagement_Form;
import Model.ModelMonAn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import main.config.DBConnect;
import response.MonAn_response;

/**
 *
 * @author Admin
 */
public class monAn_rp1 {


    public ArrayList<MonAn_response> getAll() {
        ArrayList<MonAn_response> list = new ArrayList<>();
        // CODE
        //b1: tạo sql
        String sql = """
 SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai,dbo.MonAn.Anh
FROM     dbo.LoaiMonAn INNER JOIN
                  dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
                        """;
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                MonAn_response monAn = MonAn_response.builder()
                        .maMonan(rs.getString(1))
                        .tenMonan(rs.getString(2))
                        .donGia(rs.getDouble(3))
                        .TenLoaiMon(rs.getString(4))
                        .TrangThai(rs.getBoolean(5))
                        .Anh(rs.getBytes(6))
                        //                    .Id_loaiMon(rs.getInt(6))
                        //                    .MaMonAn(rs.getString(7))
                        .build();
                list.add(monAn);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
    public ArrayList<MonAn_response> getAllhoatDong() {
        ArrayList<MonAn_response> list = new ArrayList<>();
        // CODE
        //b1: tạo sql
        String sql = """
SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai
FROM     dbo.LoaiMonAn INNER JOIN
                  dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
				  where dbo.MonAn.TrangThai = 1 
                        """;
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                MonAn_response monAn = MonAn_response.builder()
                        .maMonan(rs.getString(1))
                        .tenMonan(rs.getString(2))
                        .donGia(rs.getDouble(3))
                        .TenLoaiMon(rs.getString(4))
                        .TrangThai(rs.getBoolean(5))
                        //                    .Id_loaiMon(rs.getInt(6))
                        //                    .MaMonAn(rs.getString(7))
                        .build();
                list.add(monAn);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
       public ArrayList<MonAn_response> getAllTamDung() {
        ArrayList<MonAn_response> list = new ArrayList<>();
        // CODE
        //b1: tạo sql
        String sql = """
SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai
FROM     dbo.LoaiMonAn INNER JOIN
                  dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
				  where dbo.MonAn.TrangThai = 0 
                        """;
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                MonAn_response monAn = MonAn_response.builder()
                        .maMonan(rs.getString(1))
                        .tenMonan(rs.getString(2))
                        .donGia(rs.getDouble(3))
                        .TenLoaiMon(rs.getString(4))
                        .TrangThai(rs.getBoolean(5))
                        //                    .Id_loaiMon(rs.getInt(6))
                        //                    .MaMonAn(rs.getString(7))
                        .build();
                list.add(monAn);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
    public ArrayList<MonAn_response> searh(String tenMonan) {

        // CODE
        //b1: tạo sql
        String sql = """
SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai,dbo.MonAn.Anh
FROM     dbo.LoaiMonAn INNER JOIN
                  dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
                         where dbo.MonAn.TenMonAn like ? OR  dbo.MonAn.MaMonAn like ?
                        """;
        ArrayList<MonAn_response> list = new ArrayList<>();
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ps.setObject(1, "%" + tenMonan + "%");
            ps.setObject(2, "%" + tenMonan + "%");
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                list.add(new MonAn_response(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getBytes(6)
                ));
            }
          
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return list;
    }
    public ArrayList<MonAn_response> searhKhoang(Double giaMax ,Double giaMin) {

        // CODE
        //b1: tạo sql
        String sql = """
SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai
FROM     dbo.LoaiMonAn INNER JOIN
                  dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
				  where dbo.MonAn.Gia >=? and dbo.MonAn.Gia <=?
                        """;
        ArrayList<MonAn_response> list = new ArrayList<>();
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ps.setObject(1, giaMax);
            ps.setObject(2, giaMin);
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                list.add(new MonAn_response(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getBytes(6)
                ));
            }
          
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return list;
    }

    public boolean add(ModelMonAn MoAn) {

        int check = 0;

        String sql = """
INSERT INTO [dbo].[MonAn]
           ([TenMonAn]
           ,[Gia]
           ,[ID_LoaiMonAn]
           ,[TrangThai]
           ,[Anh])
     VALUES
           (?,?,?,?,?)
                     
                    """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, MoAn.getTenMonan());
            ps.setDouble(2, MoAn.getDonGia());
            ps.setInt(3, MoAn.getId_loaiMon());
            ps.setBoolean(4, MoAn.getTrangThai());
            ps.setBytes(5,MenuManagement_Form.SharedData.getPersonImage());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }

 public boolean update(String maMonAn, ModelMonAn newmonAn) {
    int check = 0;
    String sql = """
UPDATE [dbo].[MonAn]
   SET [TenMonAn] = ?
      ,[Gia] = ?
      [ID_LoaiMonAn] = ?
 WHERE MaMonAn = ?
                 """;
    try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, newmonAn.getTenMonan());
        ps.setDouble(2, newmonAn.getDonGia());
        ps.setInt(3, newmonAn.getId_loaiMon());
//        ps.setBoolean(4, newmonAn.getTrangThai());
//
//        byte[] imageBytes = MenuManagement_Form.SharedData.getPersonImage();
//        if (imageBytes != null) {
//            ps.setBytes(5, imageBytes);
//        } else {
//            ps.setNull(5, java.sql.Types.VARBINARY);
//        }

        ps.setString(4, maMonAn);
        check = ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace(System.out);
    }
    return check > 0;
}
 public boolean updateAnh(String maMonAn, ModelMonAn newmonAn) {
    int check = 0;
    String sql = """
UPDATE [dbo].[MonAn]
   SET 
      [Anh] = ?
 WHERE MaMonAn = ?
                 """;
    try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

        byte[] imageBytes = MenuManagement_Form.SharedData.getPersonImage();
        if (imageBytes != null) {
            ps.setBytes(1, imageBytes);
        } else {
            ps.setNull(1, java.sql.Types.VARBINARY);
        }
        ps.setString(2, maMonAn);
        check = ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace(System.out);
    }
    return check > 0;
}

    public boolean updateTrangThai(String maMonAn, Boolean newTrangThai) {
        int check = 0;
        String sql = """
UPDATE [dbo].[MonAn]
   SET 
      [TrangThai] = ?
 WHERE MaMonAn = ?
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, newTrangThai);
            ps.setObject(2, maMonAn);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return check > 0;
    }
    public static void main(String[] args) {
        System.out.println(new monAn_rp1().getAll());
    }

}
