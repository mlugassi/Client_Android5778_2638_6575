package lugassi.wallach.client_android5778_2638_6575.model.backend;

import android.content.ContentValues;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Customer;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Promotion;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;


/**
 * Created by Michael on 21/11/2017.
 */

public interface DB_manager {

    public String checkUser(String userName, String password) throws Exception;

    public boolean createUser(String userName, String password, int userID) throws Exception;

    public int addCustomer(ContentValues contentValues) throws Exception;

    public int addReservation(ContentValues contentValues) throws Exception;

    public boolean addPromotion(ContentValues contentValues) throws Exception;


    public boolean updateCar(ContentValues contentValues) throws Exception;

    public boolean updateCustomer(ContentValues contentValues) throws Exception;

    public boolean updatePromotion(ContentValues contentValues) throws Exception;

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


    public Float closeReservation(ContentValues contentValues);


    public boolean detectCarsChanges() throws Exception;
}
