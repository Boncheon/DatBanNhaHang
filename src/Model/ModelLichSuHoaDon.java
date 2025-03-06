package Model;

public class ModelLichSuHoaDon {
    private String ID_HoaDon;
    private String MaHoaDon;
    private int Id_DatBan;
    private int Id_KhachHang;
    private int Id_NhanVien;
    private int ChietKhau;
    private int PhuPhi;
    private float TongSauGiam;
    private boolean TrangThai;
    private String NgayTao;
    
    public ModelLichSuHoaDon() {
    }

    public ModelLichSuHoaDon(String ID_HoaDon, String MaHoaDon, int Id_DatBan, int Id_KhachHang, int Id_NhanVien, int ChietKhau, int PhuPhi, float TongSauGiam, boolean TrangThai, String NgayTao) {
        this.ID_HoaDon = ID_HoaDon;
        this.MaHoaDon = MaHoaDon;
        this.Id_DatBan = Id_DatBan;
        this.Id_KhachHang = Id_KhachHang;
        this.Id_NhanVien = Id_NhanVien;
        this.ChietKhau = ChietKhau;
        this.PhuPhi = PhuPhi;
        this.TongSauGiam = TongSauGiam;
        this.TrangThai = TrangThai;
        this.NgayTao = NgayTao;
    }

    // Getters and Setters
    public String getID_HoaDon() {
        return ID_HoaDon;
    }

    public void setID_HoaDon(String ID_HoaDon) {
        this.ID_HoaDon = ID_HoaDon;
    }

    public String getMaHoaDon() {
        return MaHoaDon;
    }

    public void setMaHoaDon(String MaHoaDon) {
        this.MaHoaDon = MaHoaDon;
    }

    public int getId_DatBan() {
        return Id_DatBan;
    }

    public void setId_DatBan(int Id_DatBan) {
        this.Id_DatBan = Id_DatBan;
    }

    public int getId_KhachHang() {
        return Id_KhachHang;
    }

    public void setId_KhachHang(int Id_KhachHang) {
        this.Id_KhachHang = Id_KhachHang;
    }

    public int getId_NhanVien() {
        return Id_NhanVien;
    }

    public void setId_NhanVien(int Id_NhanVien) {
        this.Id_NhanVien = Id_NhanVien;
    }

    public int getChietKhau() {
        return ChietKhau;
    }

    public void setChietKhau(int ChietKhau) {
        this.ChietKhau = ChietKhau;
    }

    public int getPhuPhi() {
        return PhuPhi;
    }

    public void setPhuPhi(int PhuPhi) {
        this.PhuPhi = PhuPhi;
    }

    public float getTongSauGiam() {
        return TongSauGiam;
    }

    public void setTongSauGiam(float TongSauGiam) {
        this.TongSauGiam = TongSauGiam;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String NgayTao) {
        this.NgayTao = NgayTao;
    }
}
