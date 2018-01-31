package lugassi.wallach.client_android5778_2638_6575.model.entities;

public class Car {
    private static int carIDSerializer = 1;
    private int reservations; // num of reservations of the car
    private int branchID;
    private int modelCode;
    private long mileage;
    private int carID;

    public Car() {
        this.carID = carIDSerializer++;
        this.reservations = 0;
        this.mileage = 0;
    }

    public Car(int carID) {
        this.carID = carID;
    }

    public static int getCarIDSerializer() {
        return carIDSerializer;
    }

    public static void setCarIDSerializer(int carIDSerializer) {
        Car.carIDSerializer = carIDSerializer;
    }

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getModelCode() {
        return modelCode;
    }

    public void setModelCode(int modelCode) {
        this.modelCode = modelCode;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public int getCarID() {
        return carID;
    }
}
