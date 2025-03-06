package Model;

public class ModelBan {

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelBan() {
    }
    
    public ModelBan(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }
    
    public ModelBan(String ID, String name, String status) {
        this.ID = ID;
        this.name = name;
        this.status = status;
    }
    
    private String ID; //Mã bàn
    private String name; //Tên bàn
    private String status; //Loại bàn
    
}
