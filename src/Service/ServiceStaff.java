package Service;


import Model.DatBan;
import Model.ModelBan;
import Model.ModelDatBan;
import Model.ModelNhanVien;

import Model.ModelNhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import main.config.DBConnect;

public class ServiceStaff {

    private final Connection con;

    //Connect tới DataBase       
    public ServiceStaff() {
        con = DBConnect.getConnection();
    }


    public ArrayList<ModelBan> MenuTableState( String state) throws SQLException {
        ArrayList<ModelBan> list = new ArrayList<>();
        String sql = "SELECT MaBan,TenBan,Trangthai FROM Ban";
        switch (state) {
            case "Tất cả" ->
                sql = "SELECT MaBan,TenBan,Trangthai FROM Ban ";
            case "Còn trống" ->
                sql = "SELECT MaBan,TenBan,Trangthai FROM Ban AND Trangthai='Con trong'";
            case "Đã đặt trước" ->
                sql = "SELECT MaBan,TenBan,Trangthai FROM Ban AND Trangthai='Da dat truoc'";
            case "Đang dùng bữa" ->
                sql = "SELECT MaBan,TenBan,Trangthai FROM Ban AND Trangthai='Dang dung bua'";
        }
        PreparedStatement p = con.prepareStatement(sql);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            String id = r.getString("MaBan");
            String name = r.getString("TenBan");
            String status = r.getString("Trangthai");
            ModelBan data = new ModelBan(id, name, status);
            list.add(data);
        }
        r.close();
        p.close();
        return list;
    }

    //Điều chỉnh trạng thái bàn thành Đã đặt trước sau khi nhân viên xác nhận
    public void setTableReserve(String MaBan) throws SQLException {
        String sql = "UPDATE BAN SET TrangThai = 'Da dat truoc' WHERE MaBan=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, MaBan);
        p.execute();
        p.close();
    }

    //Hủy trạng thái bàn đã Đặt trước trước thành Còn trống
    public void CancelTableReserve(String MaBan) throws SQLException {
        String sql = "UPDATE BAN SET TrangThai = 'Con trong' WHERE MaBan=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, MaBan);
        p.execute();
        p.close();
    }

    public ArrayList<ModelBan> MenuTable(String floor) throws SQLException {
        ArrayList<ModelBan> list = new ArrayList<>();
        String sql = "SELECT MaBan,TenBan,Trangthai FROM Ban WHERE Vitri=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, floor);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            String id = r.getString("MaBan");
            String name = r.getString("TenBan");
            String status = r.getString("Trangthai");
            ModelBan data = new ModelBan(id, name, status);
            list.add(data);
        }
        r.close();
        p.close();
        return list;
    }

    //Cập nhật trạng thái Hóa đơn thành Đã thanh toán khi Nhân viên xác nhận thanh toán
    public void UpdateHoaDonStatus(int idHD) throws SQLException {
        String sql = "UPDATE HoaDon SET TrangThai = 'Da thanh toan' WHERE ID_HoaDon=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setInt(1, idHD);
        p.execute();
        p.close();
    }

    //Lấy tên khách hàng từ Mã KH
    public String getTenKH(int idKH) throws SQLException {
        String name = "";
        String sql = "SELECT TenKH From KhachHang WHERE ID_KH=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setInt(1, idKH);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            name = r.getString(1);
        }
        p.close();
        r.close();
        return name;
    }
     public ArrayList<ModelBan> MenuTable() throws SQLException {
        ArrayList<ModelBan> list = new ArrayList<>();
        String sql = "SELECT MaBan,TenBan,Trangthai FROM Ban ";
        PreparedStatement p = con.prepareStatement(sql);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            String id = r.getString("MaBan");
            String name = r.getString("TenBan");
            String status = r.getString("Trangthai");
            ModelBan data = new ModelBan(id, name, status);
            list.add(data);
        }
        r.close();
        p.close();
        return list;
    }
public String getTenKhachHang(int idKhachHang) throws SQLException {
        String query = "SELECT TenKhachHang FROM KhachHang WHERE ID_KhachHang = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
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
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
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
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
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
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenNhanVien");
                }
            }
        }
        return null;
    }
    
   public ArrayList<ModelDatBan> getDatBanList() throws SQLException {
        ArrayList<ModelDatBan> list = new ArrayList<>();
        String sql = "SELECT * FROM DatBan";
        PreparedStatement p = con.prepareStatement(sql);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            int ID_DatBan = r.getInt("ID_DatBan");
            int ID_NhanVien = r.getInt("ID_NhanVien");
            int ID_KhachHang = r.getInt("ID_KhachHang");
            String ID_Ban = r.getString("ID_Ban");
            java.sql.Date NgayDat = r.getDate("NgayDat");
            String GioDat = r.getString("GioDat");
            int SoNguoi = r.getInt("SoNguoi");
            String TrangThai = r.getString("TrangThai");
            String GhiChu = r.getString("GhiChu");
            ModelDatBan data = new ModelDatBan(ID_DatBan, ID_NhanVien, ID_KhachHang, ID_Ban, NgayDat, GioDat, SoNguoi, TrangThai, GhiChu);
            list.add(data);
        }
        r.close();
        p.close();
        return list;
    }

    public String getTenBanById(String ID_Ban) throws SQLException {
        String tenBan = null;
        String sql = "SELECT TenBan FROM Ban WHERE MaBan = ?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, ID_Ban);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            tenBan = r.getString("TenBan");
        }
        r.close();
        p.close();
        return tenBan;
    }
}
