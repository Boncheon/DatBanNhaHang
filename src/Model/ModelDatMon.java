
package Model;

public class ModelDatMon {
    private int id_DatMon; 
    private String TenMonAn;
    private float gia;
    private int SoLuong;
    private float ThanhTien;

    public ModelDatMon() {
    }

    public ModelDatMon(int id_DatMon, String TenMonAn, float gia, int SoLuong, float ThanhTien) {
        this.id_DatMon = id_DatMon;
        this.TenMonAn = TenMonAn;
        this.gia = gia;
        this.SoLuong = SoLuong;
        this.ThanhTien = ThanhTien;
    }

    public int getId_DatMon() {
        return id_DatMon;
    }

    public void setId_DatMon(int id_DatMon) {
        this.id_DatMon = id_DatMon;
    }

    public String getTenMonAn() {
        return TenMonAn;
    }

    public void setTenMonAn(String TenMonAn) {
        this.TenMonAn = TenMonAn;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public float getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(float ThanhTien) {
        this.ThanhTien = ThanhTien;
    }
}
