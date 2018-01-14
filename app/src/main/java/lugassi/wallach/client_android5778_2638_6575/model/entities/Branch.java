package lugassi.wallach.client_android5778_2638_6575.model.entities;

public class Branch {

    private static int branchIDSerializer = 1;
    private String address;
    // might be final, waiting for constructor
    private int maxParkingSpace;
    private int actualParkingSpace;
    private int branchID;
    // for filter
    private String city;
    // if more than one branch in town give the name of
    // the neighborhood, by default the city name
    private String branchName;

    public Branch(int branchID) {
        this.branchID = branchID;
    }

    public Branch() {
        this.branchID = branchIDSerializer++;
        this.actualParkingSpace = 0;
    }

    public String getAddress() {
        return address;
    }

    public static int getBranchIDSerializer() {
        return branchIDSerializer;
    }

    public static void setBranchIDSerializer(int branchIDSerializer) {
        Branch.branchIDSerializer = branchIDSerializer;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxParkingSpace() {
        return maxParkingSpace;
    }

    public void setMaxParkingSpace(int maxParkingSpace) {
        this.maxParkingSpace = maxParkingSpace;
    }

    public int getActualParkingSpace() {
        return actualParkingSpace;
    }

    public void setActualParkingSpace(int actualParkingSpace) {
        this.actualParkingSpace = actualParkingSpace;
    }

    public int getBranchID() {
        return branchID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
