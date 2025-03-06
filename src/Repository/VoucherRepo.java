/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import Model.Voucher;
import java.util.ArrayList;
import main.config.DBConnect;

/**
 *
 * @author Admin
 */
public class VoucherRepo {
    
    private Connection conn;
    
    public VoucherRepo(){
        
        conn = DBConnect.getConnection();
        
    }
    
    public ArrayList<Voucher> getAll() throws SQLException{
        
        ArrayList<Voucher> list = new ArrayList<>();
        
         String sql = "select ID_Voucher,MaVoucher,LoaiVoucher,\n" +
            "TrangThai,NgayGioBatDau,NgayGioKetThuc,PhanTramGiam,\n" +
            "SoTienGiam,SoLanDaDung,GioiHanLuotDung,TienGiamToiDa,\n" +
            "TienGiamToiThieu,MoTa from Voucher";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            list.clear();
        
        while(rs.next()){
            
            Voucher vc = new Voucher();
            
            vc.setID_Voucher(rs.getString(1));
            vc.setMaVoucher(rs.getString(2));
            vc.setLoaiVoucher(rs.getString(3));
            vc.setTrangThai(rs.getString(4));
            vc.setNgayBatDau(rs.getString(5));
            vc.setNgayKetThuc(rs.getString(6));
            vc.setPhanTram(rs.getFloat(7));
            vc.setSoTienGiam(rs.getFloat(8));
            vc.setSoLanDaDung(rs.getFloat(9));
            vc.setGioiHanLuotDung(rs.getInt(10));
            vc.setTienGiamToiDa(rs.getFloat(11));
            vc.setTienGiamToiThieu(rs.getFloat(12));
            vc.setMoTa(rs.getString(13));
            
            list.add(vc);
            
        }
        return list;
    }
    
    public void add(Voucher vc) throws SQLException{
        
        String sql = "INSERT INTO Voucher (LoaiVoucher,"
                + "TrangThai,NgayGioBatDau,NgayGioKetThuc,"
                + "PhanTramGiam,SoTienGiam,GioiHanLuotDung,"
                + "TienGiamToiDa,TienGiamToiThieu,MoTa) VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        
        ps.setString(1,vc.getLoaiVoucher());
        ps.setString(2, vc.getTrangThai());
        ps.setString(3,vc.getNgayBatDau());
        ps.setString(4,vc.getNgayKetThuc());
        ps.setFloat(5, vc.getPhanTram());
        ps.setFloat(6,vc.getSoTienGiam());
        ps.setInt(7,vc.getGioiHanLuotDung());
        ps.setFloat(8,vc.getTienGiamToiDa());
        ps.setFloat(9,vc.getTienGiamToiThieu());
        ps.setString(10,vc.getMoTa());
        
        
        ps.execute();
        
    }
    
    
    
    public void update(Voucher vc,String Id_Voucher) throws SQLException{
        
        String sql = "UPDATE Voucher SET LoaiVoucher = ?,TrangThai = ?,NgayGioBatDau = ?,\n" +
                     "NgayGioKetThuc = ?,PhanTramGiam = ?,SoTienGiam = ?,GioiHanLuotDung = ?,\n" +
                     "TienGiamToiDa = ?,TienGiamToiThieu = ?,MoTa = ? WHERE MaVoucher = ?  ";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, vc.getLoaiVoucher());
        ps.setString(2, vc.getTrangThai());
        ps.setString(3,vc.getNgayBatDau());
        ps.setString(4,vc.getNgayKetThuc());
        ps.setFloat(5,vc.getPhanTram());
        ps.setFloat(6,vc.getSoTienGiam());
        ps.setInt(7,vc.getGioiHanLuotDung());
        ps.setFloat(8,vc.getTienGiamToiDa());
        ps.setFloat(9,vc.getTienGiamToiThieu());
        ps.setString(10,vc.getMoTa());
        ps.setString(11,vc.getID_Voucher());
        
        
        ps.executeUpdate();
        
    }
    
    
    public void setStatusEXP(String ma,String status) throws SQLException{
        
       
        
        String sql = "UPDATE Voucher SET TrangThai = ? WHERE MaVoucher = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setObject(1, status);
        ps.setObject(2, ma);
        
        ps.executeUpdate();
        
        
        
    }
    
    
    
    
}
