package Model;

public class DatMon {
    private int idDatBan;
    private int idMonAn;
    private int soLuong;
    private float thanhTien;
    private String ghiChu;

    public DatMon() {}

    public DatMon(int idDatBan, int idMonAn, int soLuong, float thanhTien, String ghiChu) {
        this.idDatBan = idDatBan;
        this.idMonAn = idMonAn;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
        this.ghiChu = ghiChu;
    }

    public int getIdDatBan() {
        return idDatBan;
    }

    public void setIdDatBan(int idDatBan) {
        this.idDatBan = idDatBan;
    }

    public int getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(int idMonAn) {
        this.idMonAn = idMonAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(float thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
