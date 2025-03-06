/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repo;


import Model.ModelHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import main.config.DBConnect;

/**
 *
 * @author nguye
 */
public class HoaDonRepository {

    public ArrayList<ModelHoaDon> getAll() {
        String sql = """
                   SELECT [ID_HoaDon]
                                 ,[MaHoaDon]
                                 ,[ID_NhanVien]
                                 ,[ID_DatBan]
                                 ,[ID_Voucher]
                                 ,[NgayTao]
                                 ,[TongTien]
                                 ,[TongSauGiam]
                                 ,[TrangThai]
                                 ,[chietkhau]
                                 ,[PhuPhi]
                             FROM [dbo].[HoaDon]
                     """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            ArrayList<ModelHoaDon> lists = new ArrayList<>();
            while (rs.next()) {
                ModelHoaDon response1
                        = ModelHoaDon.builder()
                                .ID_HoaDon(rs.getString(1))
                                .MaHoaDon(rs.getString(2))
                                .ID_datban(rs.getString(3))
                                .ID_NhanVien(rs.getString(4))
                                .ID_Voucher(rs.getString(5))
                                .NgayTao(rs.getString(6))
                                .TongTien(rs.getString(7))
                                
                                .PhuPhi(rs.getString(8))
                                .chietkhau(rs.getString(9))
                                .TongSauGiam(rs.getString(10))
                                .TrangThai(rs.getBoolean(11))
                                .build();
                lists.add(response1);
            }
            return lists;
        } catch (Exception e) {
            // loi => nhay vao catch
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new HoaDonRepository().getAll());;
    }

    public ArrayList<Object[]> getDatMonByMaHoaDon(String idkhachhang) {
        ArrayList<Object[]> hoadonlist = new ArrayList<>();
        String query = """
select * from KhachHang 
            join HoaDon on  KhachHang.ID_KhachHang = HoaDon.ID_KhachHang
            where HoaDon.MaHoaDon =?
        """;

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, idkhachhang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] hoadon = {
                    rs.getString("mahoadon"),
                    rs.getString("idkhachhang"),
                    rs.getString("idnhanvien"),
                    rs.getString("ngaytao"),
                    rs.getString("tongtien"),
                    rs.getString("tongsaugiam"),
                    rs.getBoolean("trangthai"),};
                hoadonlist.add(hoadon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoadonlist;
    }
}
