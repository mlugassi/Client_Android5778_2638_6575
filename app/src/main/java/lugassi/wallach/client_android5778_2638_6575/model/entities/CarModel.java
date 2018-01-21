package lugassi.wallach.client_android5778_2638_6575.model.entities;

import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.EngineCapacity;

public class CarModel {
    private static int modelCodeSerializer = 1;
    private int modelCode;
    private String modelName;
    private Company company;
    private EngineCapacity engineCapacity;
    private int seats;
    private CarType carType;
   // private Color color;
    private int maxGasTank;

    public CarModel() {
        this.modelCode = modelCodeSerializer++;
    }

    public static void setModelCodeSerializer(int modelCodeSerializer) {
        CarModel.modelCodeSerializer = modelCodeSerializer;
    }

    public CarModel(int modelCode) {
        this.modelCode = modelCode;
    }

    public int getModelCode() {
        return modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public EngineCapacity getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(EngineCapacity engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
//
//    public Color getColor() {
//        return color;
//    }
//
//    public void setColor(Color color) {
//        this.color = color;
//    }

    public int getMaxGasTank() {
        return maxGasTank;
    }

    public void setMaxGasTank(int maxGasTank) {
        this.maxGasTank = maxGasTank;
    }

}
