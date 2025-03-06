package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.config.DBConnect;
import model.Reservation;


public class ReservationRepository {

    public List<Object[]> getAllReservations() {
        List<Object[]> reservations = new ArrayList<>();
        String query = "SELECT DatBan.MaDatBan, KhachHang.Ten, KhachHang.SoDienThoai, DatBan.NgayDat, DatBan.GioDat, DatBan.SoNguoi, Ban.TenBan, DatBan.GhiChu, NhanVien.Ten AS TenNhanVien, DatBan.TrangThai " +
                       "FROM DatBan " +
                       "JOIN KhachHang ON DatBan.ID_KhachHang = KhachHang.ID_KhachHang " +
                       "JOIN Ban ON DatBan.ID_Ban = Ban.ID_Ban " +
                       "JOIN NhanVien ON DatBan.ID_NhanVien = NhanVien.ID_NhanVien";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                reservations.add(new Object[]{
                        rs.getString("MaDatBan"),
                        rs.getString("Ten"),
                        rs.getString("SoDienThoai"),
                        rs.getDate("NgayDat"),
                        rs.getTime("GioDat"),
                        rs.getInt("SoNguoi"),
                        rs.getString("TenBan"),
                        rs.getString("GhiChu"),
                        rs.getString("TenNhanVien"),
                        rs.getString("TrangThai")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<String> getAllBanNames() {
        List<String> banNames = new ArrayList<>();
        String query = "SELECT TenBan FROM Ban";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                banNames.add(rs.getString("TenBan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banNames;
    }

    public List<String> getAllNhanVienNames() {
        List<String> nhanVienNames = new ArrayList<>();
        String query = "SELECT Ten FROM NhanVien";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                nhanVienNames.add(rs.getString("Ten"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVienNames;
    }

    public List<String[]> getAllKhachHangDetails() {
        List<String[]> khachHangDetails = new ArrayList<>();
        String query = "SELECT Ten, SoDienThoai FROM KhachHang";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                khachHangDetails.add(new String[]{rs.getString("Ten"), rs.getString("SoDienThoai")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khachHangDetails;
    }

    public void addReservation(String tenKH, String soDienThoai, java.util.Date ngayDat, java.sql.Time gioDat, int soNguoi, String tenBan, String ghiChu, String tenNhanVien, String trangThai) {
        String query = "INSERT INTO DatBan (ID_KhachHang, ID_Ban, NgayDat, GioDat, SoNguoi, GhiChu, ID_NhanVien, TrangThai) " +
                       "VALUES ((SELECT ID_KhachHang FROM KhachHang WHERE Ten = ? AND SoDienThoai = ?), " +
                       "(SELECT ID_Ban FROM Ban WHERE TenBan = ?), ?, ?, ?, ?, " +
                       "(SELECT ID_NhanVien FROM NhanVien WHERE Ten = ?), ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, tenKH);
            pstmt.setString(2, soDienThoai);
            pstmt.setString(3, tenBan);
            pstmt.setDate(4, new java.sql.Date(ngayDat.getTime()));
            pstmt.setTime(5, gioDat);
            pstmt.setInt(6, soNguoi);
            pstmt.setString(7, ghiChu);
            pstmt.setString(8, tenNhanVien);
            pstmt.setString(9, trangThai);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservation(String maDatBan, String tenKH, String soDienThoai, java.util.Date ngayDat, java.sql.Time gioDat, int soNguoi, String tenBan, String ghiChu, String tenNhanVien, String trangThai) {
        String query = "UPDATE DatBan SET ID_KhachHang = (SELECT ID_KhachHang FROM KhachHang WHERE Ten = ? AND SoDienThoai = ?), " +
                       "ID_Ban = (SELECT ID_Ban FROM Ban WHERE TenBan = ?), NgayDat = ?, GioDat = ?, SoNguoi = ?, GhiChu = ?, " +
                       "ID_NhanVien = (SELECT ID_NhanVien FROM NhanVien WHERE Ten = ?), TrangThai = ? " +
                       "WHERE MaDatBan = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, tenKH);
            pstmt.setString(2, soDienThoai);
            pstmt.setString(3, tenBan);
            pstmt.setDate(4, new java.sql.Date(ngayDat.getTime()));
            pstmt.setTime(5, gioDat);
            pstmt.setInt(6, soNguoi);
            pstmt.setString(7, ghiChu);
            pstmt.setString(8, tenNhanVien);
            pstmt.setString(9, trangThai);
            pstmt.setString(10, maDatBan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservationStatus(String maDatBan, String trangThai) {
        String query = "UPDATE DatBan SET TrangThai = ? WHERE MaDatBan = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trangThai);
            pstmt.setString(2, maDatBan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getFilteredReservations(String trangThai, java.util.Date ngayDat, String search) {
        List<Object[]> reservations = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT DatBan.MaDatBan, KhachHang.Ten, KhachHang.SoDienThoai, DatBan.NgayDat, DatBan.GioDat, DatBan.SoNguoi, Ban.TenBan, DatBan.GhiChu, NhanVien.Ten AS TenNhanVien, DatBan.TrangThai " +
            "FROM DatBan " +
            "JOIN KhachHang ON DatBan.ID_KhachHang = KhachHang.ID_KhachHang " +
            "JOIN Ban ON DatBan.ID_Ban = Ban.ID_Ban " +
            "JOIN NhanVien ON DatBan.ID_NhanVien = NhanVien.ID_NhanVien " +
            "WHERE 1=1 "
        );

        List<Object> parameters = new ArrayList<>();

        if (trangThai != null && !trangThai.isEmpty()) {
            query.append("AND DatBan.TrangThai = ? ");
            parameters.add(trangThai);
        }

        if (ngayDat != null) {
            query.append("AND DatBan.NgayDat = ? ");
            parameters.add(new java.sql.Date(ngayDat.getTime()));
        }

        if (search != null && !search.isEmpty()) {
            query.append("AND (KhachHang.Ten LIKE ? OR KhachHang.SoDienThoai LIKE ? OR DatBan.GhiChu LIKE ?) ");
            parameters.add("%" + search + "%");
            parameters.add("%" + search + "%");
            parameters.add("%" + search + "%");
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Object[]{
                        rs.getString("MaDatBan"),
                        rs.getString("Ten"),
                        rs.getString("SoDienThoai"),
                        rs.getDate("NgayDat"),
                        rs.getTime("GioDat"),
                        rs.getInt("SoNguoi"),
                        rs.getString("TenBan"),
                        rs.getString("GhiChu"),
                        rs.getString("TenNhanVien"),
                        rs.getString("TrangThai")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<String> getAvailableBanNames(java.util.Date ngayDat) {
        List<String> banNames = new ArrayList<>();
        String query = "SELECT TenBan FROM Ban WHERE ID_Ban NOT IN (" +
                       "SELECT ID_Ban FROM DatBan WHERE NgayDat = ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(ngayDat.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    banNames.add(rs.getString("TenBan"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banNames;
    }
    public List<String> getUnCheckedInReservationsByDate(java.util.Date date) {
        List<String> reservationCodes = new ArrayList<>();
        String query = "SELECT MaDatBan FROM DatBan WHERE TrangThai = N'Chưa check-in' AND NgayDat = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservationCodes.add(rs.getString("MaDatBan"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationCodes;
    }
    
    public List<String> getAllMaDatBan() {
        List<String> maDatBanList = new ArrayList<>();
        String query = "SELECT MaDatBan FROM DatBan WHERE TrangThai = 'Chưa check-in'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                maDatBanList.add(rs.getString("MaDatBan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maDatBanList;
    }

    public ArrayList<Object[]> getDatMonByMaDatBan(String maDatBan) {
        ArrayList<Object[]> datMonList = new ArrayList<>();
        String query = """
            SELECT MonAn.TenMonAn, MonAn.Gia, DatMon.SoLuong, DatMon.GhiChu
            FROM DatMon
            JOIN MonAn ON DatMon.ID_MonAn = MonAn.ID_MonAn
            JOIN DatBan ON DatMon.ID_DatBan = DatBan.ID_DatBan
            WHERE DatBan.MaDatBan = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, maDatBan);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] datMon = {
                    rs.getString("TenMonAn"),
                    rs.getDouble("Gia"),
                    rs.getInt("SoLuong"),
                    rs.getString("GhiChu")
                };
                datMonList.add(datMon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datMonList;
    }
    public List<String> getMaDatBanByDate(java.util.Date date) {
        List<String> reservationCodes = new ArrayList<>();
        String query = "SELECT MaDatBan FROM DatBan WHERE TrangThai = N'Chưa check-in' AND NgayDat = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservationCodes.add(rs.getString("MaDatBan"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationCodes;
    }
    public List<Object[]> getDatBanByMaDatBan(String maDatBan) {
        List<Object[]> datBanList = new ArrayList<>();
        String query = "SELECT KhachHang.Ten, DatBan.NgayDat, DatBan.GioDat, DatBan.SoNguoi, Ban.TenBan " +
                       "FROM DatBan " +
                       "JOIN KhachHang ON DatBan.ID_KhachHang = KhachHang.ID_KhachHang " +
                       "JOIN Ban ON DatBan.ID_Ban = Ban.ID_Ban " +
                       "WHERE DatBan.MaDatBan = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maDatBan);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                datBanList.add(new Object[]{
                    rs.getString("Ten"),        // Tên khách hàng
                    rs.getDate("NgayDat"),      // Ngày đặt
                    rs.getTime("GioDat"),       // Giờ đặt
                    rs.getInt("SoNguoi"),       // Số người
                    rs.getString("TenBan")      // Tên bàn
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datBanList;
    }
    public List<String> getAvailableTablesByShiftAndDate(String shift, Date date) {
        List<String> availableTables = new ArrayList<>();
        String query = """
            SELECT Ban.TenBan 
            FROM Ban 
            WHERE Ban.ID_Ban NOT IN (
                SELECT DatBan.ID_Ban 
                FROM DatBan 
                WHERE DatBan.NgayDat = ? 
                AND (
                    (DatBan.GioDat < '14:00:00' AND ? = 'Ca sáng') OR 
                    (DatBan.GioDat >= '16:00:00' AND DatBan.GioDat <= '22:00:00' AND ? = 'Ca tối')
                )
            )
        """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            pstmt.setString(2, shift);
            pstmt.setString(3, shift);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    availableTables.add(rs.getString("TenBan"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableTables;
    }



     public List<Reservation> getReservationsByDate(Date date) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT KhachHang.Ten AS tenKhachHang, KhachHang.SoDienThoai, DatBan.NgayDat, DatBan.GioDat, DatBan.SoNguoi, Ban.TenBan, DatBan.GhiChu, DatBan.TrangThai " +
                       "FROM DatBan " +
                       "JOIN KhachHang ON DatBan.ID_KhachHang = KhachHang.ID_KhachHang " +
                       "JOIN Ban ON DatBan.ID_Ban = Ban.ID_Ban " +
                       "WHERE DatBan.NgayDat = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setTenKhachHang(rs.getString("tenKhachHang"));
                reservation.setSoDienThoai(rs.getString("SoDienThoai")); // Set phone number
                reservation.setNgayDat(rs.getDate("NgayDat"));
                reservation.setGioDat(rs.getTime("GioDat"));
                reservation.setSoNguoi(rs.getInt("SoNguoi"));
                reservation.setTenBan(rs.getString("TenBan"));
                reservation.setGhiChu(rs.getString("GhiChu")); // Set notes
                reservation.setTrangThai(rs.getString("TrangThai"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    public int addReservations(String tenKH, String soDienThoai, Date ngayDat, Time gioDat, int soNguoi, String tenBan, String ghiChu, int idNhanVien, String trangThai) {
        String query = "INSERT INTO DatBan (ID_KhachHang, ID_Ban, NgayDat, GioDat, SoNguoi, GhiChu, ID_NhanVien, TrangThai) "
                + "OUTPUT INSERTED.ID_DatBan "
                + // Lấy ID_DatBan vừa được tạo
                "VALUES ((SELECT ID_KhachHang FROM KhachHang WHERE Ten = ? AND SoDienThoai = ?), "
                + "(SELECT ID_Ban FROM Ban WHERE TenBan = ?), ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, tenKH);
            pstmt.setString(2, soDienThoai);
            pstmt.setString(3, tenBan);
            pstmt.setDate(4, new java.sql.Date(ngayDat.getTime()));
            pstmt.setTime(5, gioDat);
            pstmt.setInt(6, soNguoi);
            pstmt.setString(7, ghiChu);
            pstmt.setInt(8, idNhanVien);
            pstmt.setString(9, trangThai);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Trả về ID_DatBan vừa được tạo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi
    }
     public String getCustomerIDByPhoneNumber(String phoneNumber) {
        String query = "SELECT ID_KhachHang FROM KhachHang WHERE SoDienThoai = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ID_KhachHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
