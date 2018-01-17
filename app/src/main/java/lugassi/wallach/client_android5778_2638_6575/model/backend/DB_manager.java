package lugassi.wallach.client_android5778_2638_6575.model.backend;

import android.content.ContentValues;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.model.entities.*;


/**
 * Created by Michael on 21/11/2017.
 */

public interface DB_manager {

    public String checkUser(String userName, String password);

    public Boolean createUser(String userName, String password, int userID);

    public int addCustomer(ContentValues contentValues);

    public int addReservation(ContentValues contentValues);

    public boolean addPromotion(ContentValues contentValues);


    public boolean updateCar(ContentValues contentValues);

    public boolean updateCustomer(ContentValues contentValues);

    public boolean updatePromotion(ContentValues contentValues);

    public ArrayList<Branch> getBranches();

    public Branch getBranch(int branchID);

    public ArrayList<Branch> getBranches(int carModel);

    public Car getCar(int carID);

    public ArrayList<Car> getFreeCars();

    public ArrayList<Car> getFreeCars(int modelCode);

    public ArrayList<Car> getFreeCarsByBranchID(int branchID);

    public CarModel getCarModel(int modelCode);

    public ArrayList<CarModel> getCarModels();

    public ArrayList<Customer> getCustomers();

    public Promotion getPromotion(int customerID);

    public Reservation getReservation(int reservationID);

    public ArrayList<Reservation> getReservationsOnGoing(int customerID);


    public Float closeReservation(ContentValues contentValues);


    public boolean checkReservations();
}
