/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.ModelDatMon;
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
public class RepoDatMon {

    private Connection conn;

    public RepoDatMon() {

        conn = DBConnect.getConnection();

    }

    public List<ModelDatMon> getDatMonByIdDatBan(int idDatBan) throws SQLException {
        List<ModelDatMon> list = new ArrayList<>();

        String sql = "SELECT "
                + "dm.ID_DatMon, "
                + "ma.TenMonAn, "
                + "ma.Gia, "
                + "dm.SoLuong, "
                + "dm.ThanhTien "
                + "FROM DatMon dm "
                + "JOIN MonAn ma ON dm.ID_MonAn = ma.ID_MonAn "
                + "WHERE dm.ID_DatBan = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idDatBan);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelDatMon datMon = new ModelDatMon();
            datMon.setId_DatMon(rs.getInt("ID_DatMon"));
            datMon.setTenMonAn(rs.getString("TenMonAn"));
            datMon.setGia(rs.getFloat("Gia"));
            datMon.setSoLuong(rs.getInt("SoLuong"));
            datMon.setThanhTien(rs.getFloat("ThanhTien"));
            list.add(datMon);
        }

        return list;
    }

}
