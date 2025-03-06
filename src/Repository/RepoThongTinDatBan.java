/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;


import Model.ModelThongTinDatBan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;

/**
 *
 * @author TriKien
 */
public class RepoThongTinDatBan {
      private Connection conn;

    public RepoThongTinDatBan() {

        conn = DBConnect.getConnection();

    }


  public List<ModelThongTinDatBan> getDatBanById(int idDatBan) throws SQLException {
        List<ModelThongTinDatBan> list = new ArrayList<>();

        String sql = "SELECT "
                + "db.MaDatBan, "
                + "kh.Ten AS TenKhachHang, "
                + "db.NgayDat, "
                + "db.GioDat AS GioDatBan, "
                + "db.SoNguoi, "
                + "b.TenBan "
                + "FROM DatBan db "
                + "JOIN KhachHang kh ON db.ID_KhachHang = kh.ID_KhachHang "
                + "JOIN Ban b ON db.ID_Ban = b.ID_Ban "
                + "WHERE db.ID_DatBan = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idDatBan);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelThongTinDatBan db = new ModelThongTinDatBan();
            db.setMaDatBan(rs.getString("MaDatBan"));
            db.setTenKhachHang(rs.getString("TenKhachHang"));
            db.setNgayDat(rs.getString("NgayDat"));
            db.setGioDatBan(rs.getString("GioDatBan"));
            db.setSoNguoi(rs.getInt("SoNguoi"));
            db.setTenBan(rs.getString("TenBan"));
            list.add(db);
        }

        return list;
    }
}
