package lugassi.wallach.client_android5778_2638_6575.model.entities;

public class Promotion {
    private int customerID;
    private int totalRentDays;
    private boolean isUsed;


    public Promotion() {

    }

    public Promotion(int customerID) {
        this.customerID = customerID;
        totalRentDays = 0;
        isUsed = false;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getTotalRentDays() {
        return totalRentDays;
    }

    public void setTotalRentDays(int totalRentDays) {
        this.totalRentDays = totalRentDays;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    // method for promotion (discount/free days)
}
