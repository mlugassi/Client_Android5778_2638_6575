package lugassi.wallach.client_android5778_2638_6575.model.backend;

import android.content.ContentValues;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.model.entities.*;

/**
 * Created by Michael on 21/11/2017.
 */

public interface DB_manager {

    public String checkUser(String userName, String password);

    public String createUser(String userName, String password, int userID);

    public String addCustomer(ContentValues contentValues);

    String removeCustomer(int customerID);

    public String addReservation(ContentValues contentValues);

    public String addPromotion(ContentValues contentValues);


    public String updateCar(ContentValues contentValues);

    public String updateCustomer(ContentValues contentValues);

    public String updatePromotion(ContentValues contentValues);

    public ArrayList<Branch> getBranches() throws Exception;

    public Branch getBranch(int branchID) throws Exception;

    public ArrayList<Branch> getBranches(int carModel) throws Exception;

    public Car getCar(int carID) throws Exception;

    public ArrayList<Car> getFreeCars() throws Exception;

    public ArrayList<Car> getFreeCars(int modelCode) throws Exception;

    public ArrayList<Car> getFreeCarsByBranchID(int branchID) throws Exception;

    public CarModel getCarModel(int modelCode) throws Exception;

    public ArrayList<CarModel> getCarModels() throws Exception;

    public ArrayList<Customer> getCustomers() throws Exception;

    public Customer getCustomer(int customerID) throws Exception;

    public Promotion getPromotion(int customerID) throws Exception;

    public Reservation getReservation(int reservationID) throws Exception;

    public ArrayList<Reservation> getReservationsOnGoing(int customerID) throws Exception;

    public String getCustomerTotalReservations(int customerID);

    public String getCustomerTotalMileage(int customerID);


    public String closeReservation(ContentValues contentValues);

    public boolean detectCarsChanges() throws Exception;
}
