package Model;

import java.util.Date;

/**
 *
 * @author RAVEN
 */
public class ModelData {


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public ModelData() {
    }

    public ModelData(String month, double amount, double cost, double profit) {
        this.month = month;
        this.amount = amount;
        this.cost = cost;
        this.profit = profit;
    }


  private String month; // Tháng
    private double amount; // Số lượng hóa đơn
    private double cost; // Doanh thu
    private double profit; // Chiết khấu
}
