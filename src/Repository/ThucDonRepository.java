package Repository;


import java.beans.Statement;
import response.MonAn_response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;

public class ThucDonRepository {
     public ArrayList<MonAn_response> getAll() {
        ArrayList<MonAn_response> list = new ArrayList<>();
        String sql = """
            SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai
            FROM dbo.LoaiMonAn 
            INNER JOIN dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
            WHERE dbo.MonAn.TrangThai = 1
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MonAn_response monAn = MonAn_response.builder()
                    .maMonan(rs.getString(1))
                    .tenMonan(rs.getString(2))
                    .donGia(rs.getDouble(3))
                    .TenLoaiMon(rs.getString(4))
                    .TrangThai(rs.getBoolean(5))
                    .build();
                list.add(monAn);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return list;
    }

    public List<MonAn_response> getMonAnByLoaiMon(String loaiMon) {
        List<MonAn_response> list = new ArrayList<>();
        String sql = """
            SELECT dbo.MonAn.MaMonAn, dbo.MonAn.TenMonAn, dbo.MonAn.Gia, dbo.LoaiMonAn.TenLoaiMonAn, dbo.MonAn.TrangThai
            FROM dbo.LoaiMonAn 
            INNER JOIN dbo.MonAn ON dbo.LoaiMonAn.ID_LoaiMonAn = dbo.MonAn.ID_LoaiMonAn
            WHERE dbo.MonAn.TrangThai = 1 AND dbo.LoaiMonAn.TenLoaiMonAn = ?
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MonAn_response monAn = MonAn_response.builder()
                    .maMonan(rs.getString(1))
                    .tenMonan(rs.getString(2))
                    .donGia(rs.getDouble(3))
                    .TenLoaiMon(rs.getString(4))
                    .TrangThai(rs.getBoolean(5))
                    .build();
                list.add(monAn);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return list;
    }
}