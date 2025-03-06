package Repository;

import Model.HoaDon;
import Model.DatBan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;

public class HoaDonRepository {

    private Connection connection;

    public HoaDonRepository() {
        this.connection = DBConnect.getConnection();
    }
    public boolean saveTamTinh(int idDatBan, int idNhanVien, Date ngayTao, double tongTien, boolean trangThai) {
        String query = "INSERT INTO HoaDon (ID_DatBan, ID_NhanVien, NgayTao, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idDatBan);
            pstmt.setInt(2, idNhanVien);
            pstmt.setDate(3, new java.sql.Date(ngayTao.getTime()));
            pstmt.setDouble(4, tongTien);
            pstmt.setBoolean(5, trangThai);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean saveHoaDon(HoaDon hoaDon) {
        String query = "INSERT INTO HoaDon (ID_DatBan, ID_NhanVien, NgayTao, TongTien, PhuPhi, ChietKhau, TongSauGiam, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, hoaDon.getIdDatBan());
            pstmt.setInt(2, hoaDon.getIdNhanVien());
            pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setDouble(4, hoaDon.getTongTien());
            pstmt.setDouble(5, hoaDon.getPhuPhi());
            pstmt.setDouble(6, hoaDon.getChietKhau());
            pstmt.setDouble(7, hoaDon.getTongSauGiam());
            pstmt.setBoolean(8, hoaDon.isTrangThai());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 public String getTenKhachHang(int idKhachHang) throws SQLException {
        String query = "SELECT TenKhachHang FROM KhachHang WHERE ID_KhachHang = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idKhachHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenKhachHang");
                }
            }
        }
        return null;
    }

    public String getSoDienThoai(int idKhachHang) throws SQLException {
        String query = "SELECT SoDienThoai FROM KhachHang WHERE ID_KhachHang = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idKhachHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("SoDienThoai");
                }
            }
        }
        return null;
    }

    public String getTenBan(int idDatBan) throws SQLException {
        String query = "SELECT TenBan FROM DatBan WHERE ID_DatBan = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idDatBan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenBan");
                }
            }
        }
        return null;
    }

    public String getTenNhanVien(int idNhanVien) throws SQLException {
        String query = "SELECT TenNhanVien FROM NhanVien WHERE ID_NhanVien = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenNhanVien");
                }
            }
        }
        return null;
    }
}
