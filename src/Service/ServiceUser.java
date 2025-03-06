
package Service;


import Model.ModelLogin;
import Model.ModelNguoiDung;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import main.config.DBConnect;

// Controller Đăng ký/Đăng nhập vào hệ thống
public class ServiceUser {
    
    private final Connection con;
    
    //Connect tới DataBase       
    public ServiceUser() {
        con=DBConnect.getConnection();
    }
    
    /*
        Kiểm tra thông tin đăng nhập
        Trả về : null <- Nếu Thông tin đăng nhập sai
                 ModelNguoiDung <- Nếu thông tin đăng nhập đúng
    */
    public ModelNguoiDung login(ModelLogin login) throws SQLException {
        ModelNguoiDung user = null;
        String sql = "SELECT ID_NhanVien, Email, MatKhau, ChucVu  FROM NhanVien WHERE Email = ? AND MatKhau = ? AND MatKhau IS NOT NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login.getEmail());
            ps.setString(2, login.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("ID_NhanVien");
                    String email = rs.getString("Email");
                    String password = rs.getString("MatKhau");
                    String role = rs.getString("ChucVu");

                    user = new ModelNguoiDung(userID, email, password, role);
                }
            }
        }

        return user;
    }
}
