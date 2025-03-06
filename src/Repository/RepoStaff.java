/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;


import Model.ModelNhanVien;
import Model.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import main.config.DBConnect;

/**
 *
 * @author TriKien
 */
public class RepoStaff {

    private Connection conn;

    public RepoStaff() {

        conn = DBConnect.getConnection();

    }

    public ArrayList<ModelNhanVien> getAll() throws SQLException {

        ArrayList<ModelNhanVien> list = new ArrayList();

        String sql = "Select ID_NhanVien, MaNhanVien, Ten, SoDienThoai, DiaChi, NgaySinh, NgayVaoLam, Email,MatKhau, GioiTinh, ChucVu, TrangThai, SoCCCD from NhanVien ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        list.clear();

        while (rs.next()) {

            ModelNhanVien nv = new ModelNhanVien();
            nv.setStt(rs.getString(1));
            nv.setId_NV(rs.getString(2));
            nv.setTenNV(rs.getString(3));
            nv.setSdt(rs.getString(4));
            nv.setDiachi(rs.getString(5));
            nv.setNgaySinh(rs.getString(6));
            nv.setNgayVL(rs.getString(7));
            nv.setEmail(rs.getString(8));
            nv.setMatKhau(rs.getString(9));
            nv.setGioiTinh(rs.getBoolean(10));
            nv.setChucvu(rs.getString(11));
            nv.setTrangThai(rs.getBoolean(12));
            nv.setCccd(rs.getString(13));
            list.add(nv);

        }

        return list;
    }

    public void add(ModelNhanVien nv) throws SQLException {

        String sql = "INSERT INTO NhanVien(Ten,SoDienThoai,DiaChi,NgaySinh, NgayVaoLam,Email,MatKhau, GioiTinh,ChucVu,TrangThai, SoCCCD) values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nv.getTenNV());
        ps.setString(2, nv.getSdt());
        ps.setString(3, nv.getDiachi());
        ps.setString(4, nv.getNgaySinh());
        ps.setString(5, nv.getNgayVL());
        ps.setString(6, nv.getEmail());
        ps.setString(7, nv.getMatKhau());
        ps.setString(8, String.valueOf(nv.isGioiTinh()));
        ps.setString(9, nv.getChucvu());
        ps.setString(10, String.valueOf(nv.isTrangThai()));
        ps.setString(11, nv.getCccd());
        ps.execute();

    }

    public void update(ModelNhanVien nv, String Id_NV) throws SQLException {

        String sql = "Update NhanVien Set Ten = ?,SoDienThoai = ?,DiaChi = ?,NgaySinh = ?,NgayVaoLam = ?, Email = ?,MatKhau = ?, GioiTinh = ?,ChucVu = ?,TrangThai = ? where MaNhanVien = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nv.getTenNV());
        ps.setString(2, nv.getSdt());
        ps.setString(3, nv.getDiachi());
        ps.setString(4, nv.getNgaySinh());
        ps.setString(5, nv.getNgayVL());
        ps.setString(6, nv.getEmail());
        ps.setString(7, nv.getMatKhau());
        ps.setBoolean(8, nv.isGioiTinh());
        ps.setString(9, nv.getChucvu());
        ps.setBoolean(10, nv.isTrangThai());
        ps.setString(11, nv.getId_NV());

        ps.executeUpdate();

    }

    public void updateTrangThai(String Id_NV, boolean newTrangThai) throws SQLException {
        String sql = "UPDATE NhanVien SET TrangThai = ? WHERE MaNhanVien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newTrangThai);
            ps.setString(2, Id_NV);
            ps.executeUpdate();
        }
    }
    public static NhanVien getNhanVienById(int nhanVienId) {
        NhanVien nhanVien = null;
        String query = "SELECT * FROM NhanVien WHERE ID_NhanVien = ?";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, nhanVienId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                nhanVien = new NhanVien();
                nhanVien.setId(rs.getInt("ID_NhanVien"));
                nhanVien.setTen(rs.getString("Ten"));
                // Set other fields if necessary
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return nhanVien;
    }

}
