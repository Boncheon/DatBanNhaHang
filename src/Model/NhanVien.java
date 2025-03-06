package Model;

public class NhanVien {
    private int id;
    private String ten;
    // Thêm các trường khác nếu cần

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NhanVien(int id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public NhanVien() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    // Thêm các Getters và Setters cho các trường khác nếu có
}
