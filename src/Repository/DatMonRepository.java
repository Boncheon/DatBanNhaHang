package Repository;

import Model.DatMon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;

public class DatMonRepository {



   // Lưu danh sách đặt món

    public boolean saveOrUpdateDatMon(List<DatMon> datMonList) {
        String insertQuery = """
            INSERT INTO DatMon (ID_DatBan, ID_MonAn, SoLuong, ThanhTien, GhiChu)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                for (DatMon datMon : datMonList) {
                    insertStmt.setInt(1, datMon.getIdDatBan());
                    insertStmt.setInt(2, datMon.getIdMonAn());
                    insertStmt.setInt(3, datMon.getSoLuong());
                    insertStmt.setFloat(4, datMon.getThanhTien());
                    insertStmt.setString(5, datMon.getGhiChu());
                    insertStmt.addBatch();
                }

                insertStmt.executeBatch();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void addDatMon(int idDatBan, int idMonAn, int soLuong, String ghiChu, double thanhTien) {
        String query = "INSERT INTO DatMon (ID_DatBan, ID_MonAn, SoLuong, GhiChu, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idDatBan);
            pstmt.setInt(2, idMonAn);
            pstmt.setInt(3, soLuong);
            pstmt.setString(4, ghiChu);
            pstmt.setDouble(5, thanhTien);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getIdMonAnByName(String tenMonAn) {
        String query = "SELECT ID_MonAn FROM MonAn WHERE TenMonAn = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tenMonAn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
