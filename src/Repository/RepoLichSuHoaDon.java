package Repository;


import Model.ModelLichSuHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import main.config.DBConnect;

public class RepoLichSuHoaDon {

    private Connection conn;

    public RepoLichSuHoaDon() {
        conn = DBConnect.getConnection();
    }

 public ArrayList<ModelLichSuHoaDon> getAll() throws SQLException {
        ArrayList<ModelLichSuHoaDon> list = new ArrayList<>();

        String sql = "SELECT "
                + "hd.ID_HoaDon, "
                + "hd.MaHoaDon, "
                + "hd.ID_DatBan, "
                + "hd.ID_KhachHang, "
                + "hd.ID_NhanVien, "
                + "hd.NgayTao, "
                + "hd.ChietKhau, "
                + "hd.PhuPhi, "
                + "hd.TongSauGiam, "
                + "hd.TrangThai "
                + "FROM HoaDon hd";

        // Sử dụng try-with-resources để đảm bảo kết nối và các tài nguyên được đóng đúng cách
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ModelLichSuHoaDon hd = new ModelLichSuHoaDon();
                hd.setID_HoaDon(rs.getString("ID_HoaDon"));
                hd.setMaHoaDon(rs.getString("MaHoaDon"));
                hd.setId_DatBan(rs.getInt("ID_DatBan"));
                hd.setId_KhachHang(rs.getInt("ID_KhachHang"));
                hd.setId_NhanVien(rs.getInt("ID_NhanVien"));
                hd.setNgayTao(rs.getString("NgayTao"));
                hd.setChietKhau(rs.getInt("ChietKhau"));
                hd.setPhuPhi(rs.getInt("PhuPhi"));
                hd.setTongSauGiam(rs.getFloat("TongSauGiam"));
                hd.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(hd);
            }
        }
        return list;
    }
}