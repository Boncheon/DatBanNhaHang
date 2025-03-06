/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import main.config.DBConnect;
import response.MonAn_response;
import response.ThongKeReponse;

/**
 *
 * @author Asus
 */
public class ThongkeReponsitory {

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private String sql = null;

    public Double doanhthungay(Date date) {
    String sql = """
        select SUM(TongSauGiam) as [tongDoanhThuNgay]
        from HoaDon
        where (TrangThai = 1) and CAST(NgayTao AS DATE) = CAST(? AS DATE);
    """;
    try {
        Connection con = DBConnect.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        ps.setString(1, formattedDate);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("tongDoanhThuNgay");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    public Double doanhThuThang(Date date) {
        String sql = """
            SELECT SUM(TongSauGiam) as [tongDoanhThuThang]
            FROM HoaDon
            WHERE TrangThai = 1
            AND NgayTao >= DATEADD(MONTH, DATEDIFF(MONTH, 0, ?), 0)
            AND NgayTao < DATEADD(MONTH, DATEDIFF(MONTH, 0, ?) + 1, 0)
        """;
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareCall(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            ps.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("tongDoanhThuThang");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
public Double tongDoanhThuTheoNam(int year) {
        String sql = """
            SELECT SUM(TongSauGiam) as [tongDoanhThu]
            FROM HoaDon
            WHERE TrangThai = 1
            AND YEAR(NgayTao) = ?
        """;
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, year);  // Đặt năm vào câu lệnh SQL
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("tongDoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   public Integer SoHoaDon(Date date) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String month = monthFormat.format(date);
        String year = yearFormat.format(date);

        String sql = """
            SELECT COUNT(MaHoaDon) as [SoHoaDon]
            FROM HoaDon
            WHERE TrangThai = 1
            AND MONTH(NgayTao) = ?
            AND YEAR(NgayTao) = ?
        """;

        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(month));  // Đặt tháng vào câu lệnh SQL
            ps.setInt(2, Integer.parseInt(year));   // Đặt năm vào câu lệnh SQL
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoHoaDon");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ThongKeReponse> getAll() {
        ArrayList<ThongKeReponse> list = new ArrayList<>();
        // CODE
        //b1: tạo sql
        String sql = """
SELECT 
    ma.MaMonAn,
    ma.TenMonAn,
    ma.Gia,
    COUNT(hd.ID_HoaDon) AS "Số Lượng",
    COALESCE(ma.Gia * COUNT(hd.ID_HoaDon), 0) AS "Thành tiền"
FROM 
    MonAn ma
LEFT JOIN 
    HoaDon hd ON hd.ID_DatBan = ma.ID_MonAn AND hd.TrangThai = 1  -- Lọc theo trạng thái của hóa đơn nếu cần
GROUP BY 
    ma.MaMonAn, 
    ma.TenMonAn, 
    ma.Gia
Order by "Số Lượng" Desc;
                        """;
        //B2: tạo bảng kết nối
        // try ..... with......resources
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // kết quả trả ra 1 bảng  ==> 1ResultSet
            ResultSet rs = ps.executeQuery();
            //Sql==> Select ==> execteQuery
            // doc lan luot
            while (rs.next()) {
                // B3: Tạo 1 đối tượng
                ThongKeReponse monAn = ThongKeReponse.builder()
                        .maMonAn(rs.getString(1))
                        .tenMonAn(rs.getString(2))
                        .gia(rs.getFloat(3))
                        .soLuong(rs.getInt(4))
                        .thanhTien(rs.getFloat(5))
                        //                    .Id_loaiMon(rs.getInt(6))
                        //                    .MaMonAn(rs.getString(7))
                        .build();
                list.add(monAn);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
