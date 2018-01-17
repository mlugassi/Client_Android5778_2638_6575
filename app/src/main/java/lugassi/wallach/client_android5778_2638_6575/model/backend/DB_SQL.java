package lugassi.wallach.client_android5778_2638_6575.model.backend;

import android.content.ContentValues;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.BranchConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.CarConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.CarModelConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.CustomerConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.PromotionConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.ReservationConst;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.UserConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Customer;
import lugassi.wallach.client_android5778_2638_6575.model.entities.EngineCapacity;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Gender;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Promotion;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

/**
 * Created by Michael on 21/11/2017.
 */

public class DB_SQL implements DB_manager {

    private String url = "http://mlugassi.vlab.jct.ac.il/JAVA-Project/";

    public DB_SQL() {
        try {
            String reservationResult = new AsyncTask<Object, Object, String>() {
                @Override
                protected void onPostExecute(String reservationResult) {
                    super.onPostExecute(reservationResult);
                }

                @Override
                protected String doInBackground(Object... params) {

                    try {
                        return GET(url + "Reservation/GetSerialNumber.php");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "0";
                }
            }.execute().get();
            Reservation.setReservationCounter(Integer.parseInt(reservationResult.substring(0, reservationResult.length() - 1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    /// users
    @Override
    public String checkUser(final String userName, final String password) {

        try {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put(UserConst.USER_NAME, userName);
            map.put(UserConst.PASSWORD, password);

            String results = POST(url + "Login/CheckUser.php", map);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results);
            }
            return results;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @Override
    public Boolean createUser(String userName, String password, int userID) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(UserConst.USER_NAME, userName);
            params.put(UserConst.PASSWORD, password);
            params.put(UserConst.USER_ID, userID);

            String results = POST(url + "Login/CreateUser.php", params);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }

//    private String checkAdmin(String userName, String password) {
//        String results = "";
//        try {
//            Map<String, Object> map = new LinkedHashMap<>();
//
//            map.put(UserConst.USER_NAME, userName.toLowerCase());
//            map.put(UserConst.PASSWORD, password);
//
//            results = POST(url + "Login/CheckAdmin.php", map);
//            if (results.equals("")) {
//                throw new Exception("An error occurred on the server's side");
//            }
//            if (results.substring(0, 5).equalsIgnoreCase("error")) {
//                throw new Exception(results.substring(5));
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e.getMessage());
//        }
//        return results;
//    }

    /// promotions

    @Override
    public boolean addPromotion(ContentValues contentValues) {

        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(PromotionConst.CUSTOMER_ID, contentValues.getAsInteger(PromotionConst.CUSTOMER_ID));
            data.put(PromotionConst.TOTAL_RENT_DAYS, contentValues.getAsInteger(PromotionConst.TOTAL_RENT_DAYS));
            data.put(PromotionConst.IS_USED, contentValues.getAsBoolean(PromotionConst.IS_USED));

            String results = POST(url + "Promotion/AddPromotion.php", data);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Override
    public boolean updatePromotion(ContentValues contentValues) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(PromotionConst.CUSTOMER_ID, contentValues.getAsInteger(PromotionConst.CUSTOMER_ID));
            data.put(PromotionConst.TOTAL_RENT_DAYS, contentValues.getAsInteger(PromotionConst.TOTAL_RENT_DAYS));
            data.put(PromotionConst.IS_USED, contentValues.getAsBoolean(PromotionConst.IS_USED));

            String results = POST(url + "Promotion/UpdatePromotion.php", data);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Override
    public Promotion getPromotion(int customerID) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(PromotionConst.CUSTOMER_ID, customerID);

            JSONArray array = new JSONObject(POST(url + "Promotion/GetPromotion.php", data)).getJSONArray("Promotion");
            JSONObject jsonObject = array.getJSONObject(0);
            Promotion promotion = new Promotion();
            promotion.setCustomerID(jsonObject.getInt(PromotionConst.CUSTOMER_ID));
            promotion.setTotalRentDays(jsonObject.getInt(PromotionConst.TOTAL_RENT_DAYS));
            promotion.setUsed(jsonObject.getBoolean(PromotionConst.IS_USED));

            return promotion;
        } catch (Exception e) {
            return new Promotion();
        }


    }

    /// branches

    @Override
    public ArrayList<Branch> getBranches() {
        ArrayList<Branch> branches = new ArrayList<Branch>();

        try {

            JSONArray array = new JSONObject(GET(url + "Branch/GetBranches.php")).getJSONArray("branches");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Branch branch = new Branch(jsonObject.getInt(BranchConst.BRANCH_ID));
                branch.setAddress(jsonObject.getString(BranchConst.ADDRESS));
                branch.setMaxParkingSpace(jsonObject.getInt(BranchConst.MAX_PARKING_SPACE));
                branch.setCity(jsonObject.getString(BranchConst.CITY));
                branch.setBranchName(jsonObject.getString(BranchConst.BRANCH_NAME));
                branch.setActualParkingSpace(jsonObject.getInt(BranchConst.ACTUAL_PARKING_SPACE));

                branches.add(branch);
            }
            return branches;
        } catch (Exception e) {
            return branches;
        }
    }

    @Override
    public Branch getBranch(int branchID) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(BranchConst.BRANCH_ID, branchID);

            JSONArray array = new JSONObject(POST(url + "Branch/GetBranch.php", data)).getJSONArray("branch");
            JSONObject jsonObject = array.getJSONObject(0);

            Branch branch = new Branch(jsonObject.getInt(BranchConst.BRANCH_ID));
            branch.setAddress(jsonObject.getString(BranchConst.ADDRESS));
            branch.setMaxParkingSpace(jsonObject.getInt(BranchConst.MAX_PARKING_SPACE));
            branch.setCity(jsonObject.getString(BranchConst.CITY));
            branch.setBranchName(jsonObject.getString(BranchConst.BRANCH_NAME));
            branch.setActualParkingSpace(jsonObject.getInt(BranchConst.ACTUAL_PARKING_SPACE));
            return branch;
        } catch (Exception e) {
            return new Branch(branchID);
        }
    }

    @Override
    public ArrayList<Branch> getBranches(int carModel) {
        ArrayList<Branch> branches = new ArrayList<Branch>();
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(CarModelConst.MODEL_CODE, carModel);

            JSONArray array = new JSONObject(POST(url + "Branch/GetBranchesByCarModel.php", params)).getJSONArray("branches");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Branch branch = new Branch(jsonObject.getInt(BranchConst.BRANCH_ID));
                branch.setAddress(jsonObject.getString(BranchConst.ADDRESS));
                branch.setMaxParkingSpace(jsonObject.getInt(BranchConst.MAX_PARKING_SPACE));
                branch.setCity(jsonObject.getString(BranchConst.CITY));
                branch.setBranchName(jsonObject.getString(BranchConst.BRANCH_NAME));
                branch.setActualParkingSpace(jsonObject.getInt(BranchConst.ACTUAL_PARKING_SPACE));

                branches.add(branch);
            }
        } catch (Exception e) {
        }

        return branches;
    }

    /// car models

    public CarModel getCarModel(int modelCode) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(CarModelConst.MODEL_CODE, modelCode);

            JSONArray array = new JSONObject(POST(url + "CarModel/GetCarModel.php", data)).getJSONArray("carModel");
            JSONObject jsonObject = array.getJSONObject(0);

            CarModel carModel = new CarModel(jsonObject.getInt(CarModelConst.MODEL_CODE));
            carModel.setCompany(Company.valueOf(jsonObject.getString(CarModelConst.COMPANY)));
            carModel.setSeats(jsonObject.getInt(CarModelConst.SEATS));
            carModel.setCarType(CarType.valueOf(jsonObject.getString(CarModelConst.CAR_TYPE)));
            carModel.setEngineCapacity(EngineCapacity.valueOf(jsonObject.getString(CarModelConst.ENGINE_CAPACITY)));
            carModel.setMaxGasTank(jsonObject.getInt(CarModelConst.MAX_GAS_TANK));
            carModel.setModelName(jsonObject.getString(CarModelConst.MODEL_NAME));
            return carModel;
        } catch (Exception e) {
            return new CarModel(modelCode);
        }
    }

    @Override
    public ArrayList<CarModel> getCarModels() {
        ArrayList<CarModel> carModels = new ArrayList<CarModel>();

        try {

            JSONArray array = new JSONObject(GET(url + "CarModel/GetCarModels.php")).getJSONArray("carModels");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                CarModel carModel = new CarModel(jsonObject.getInt(CarModelConst.MODEL_CODE));
                carModel.setCompany(Company.valueOf(jsonObject.getString(CarModelConst.COMPANY)));
                carModel.setSeats(jsonObject.getInt(CarModelConst.SEATS));
                carModel.setCarType(CarType.valueOf(jsonObject.getString(CarModelConst.CAR_TYPE)));
                carModel.setEngineCapacity(EngineCapacity.valueOf(jsonObject.getString(CarModelConst.ENGINE_CAPACITY)));
                carModel.setMaxGasTank(jsonObject.getInt(CarModelConst.MAX_GAS_TANK));
                carModel.setModelName(jsonObject.getString(CarModelConst.MODEL_NAME));

                carModels.add(carModel);
            }
            return carModels;

        } catch (Exception e) {
            return carModels;
        }
    }


    /// cars

    @Override
    public boolean updateCar(ContentValues contentValues) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(CarConst.CAR_ID, contentValues.getAsInteger(CarConst.CAR_ID));
            params.put(CarConst.MODEL_CODE, contentValues.getAsInteger(CarConst.MODEL_CODE));
            params.put(CarConst.BRANCH_ID, contentValues.getAsInteger(CarConst.BRANCH_ID));
            params.put(CarConst.RESERVATIONS, contentValues.getAsInteger(CarConst.RESERVATIONS));
            params.put(CarConst.MILEAGE, contentValues.getAsInteger(CarConst.MILEAGE));

            String results = POST(url + "Car/UpdateCar.php", params);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Car getCar(int carID) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(CarConst.CAR_ID, carID);

            JSONArray array = new JSONObject(POST(url + "Car/GetCar.php", data)).getJSONArray("car");
            JSONObject jsonObject = array.getJSONObject(0);

            Car car = new Car(jsonObject.getInt(CarConst.CAR_ID));
            car.setModelCode(jsonObject.getInt(CarConst.MODEL_CODE));
            car.setBranchID(jsonObject.getInt(CarConst.BRANCH_ID));
            car.setReservations(jsonObject.getInt(CarConst.RESERVATIONS));
            car.setMileage(jsonObject.getInt(CarConst.MILEAGE));
            return car;
        } catch (Exception e) {
            return new Car(carID);
        }
    }

    @Override
    public ArrayList<Car> getFreeCars() {
        ArrayList<Car> cars = new ArrayList<Car>();

        try {

            JSONArray array = new JSONObject(GET(url + "Car/GetFreeCars.php")).getJSONArray("cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Car car = new Car(jsonObject.getInt(CarConst.CAR_ID));
                car.setModelCode(jsonObject.getInt(CarConst.MODEL_CODE));
                car.setBranchID(jsonObject.getInt(CarConst.BRANCH_ID));
                car.setReservations(jsonObject.getInt(CarConst.RESERVATIONS));
                car.setMileage(jsonObject.getInt(CarConst.MILEAGE));

                cars.add(car);
            }
            return cars;

        } catch (Exception e) {
            return cars;
        }
    }

    @Override
    public ArrayList<Car> getFreeCars(int modelCode) {
        ArrayList<Car> cars = new ArrayList<Car>();

        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(CarConst.MODEL_CODE, modelCode);

            JSONArray array = new JSONObject(POST(url + "Car/GetFreeCarsByModelCode.php", data)).getJSONArray("cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Car car = new Car(jsonObject.getInt(CarConst.CAR_ID));
                car.setModelCode(jsonObject.getInt(CarConst.MODEL_CODE));
                car.setBranchID(jsonObject.getInt(CarConst.BRANCH_ID));
                car.setReservations(jsonObject.getInt(CarConst.RESERVATIONS));
                car.setMileage(jsonObject.getInt(CarConst.MILEAGE));

                cars.add(car);
            }
            return cars;

        } catch (Exception e) {
            return cars;
        }
    }

    @Override
    public ArrayList<Car> getFreeCarsByBranchID(int branchID) {
        ArrayList<Car> cars = new ArrayList<Car>();

        try {

            Map<String, Object> data = new LinkedHashMap<>();

            data.put(CarConst.BRANCH_ID, branchID);
            JSONArray array = new JSONObject(POST(url + "Car/GetFreeCarsByBranchID.php", data)).getJSONArray("cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Car car = new Car(jsonObject.getInt(CarConst.CAR_ID));
                car.setModelCode(jsonObject.getInt(CarConst.MODEL_CODE));
                car.setBranchID(jsonObject.getInt(CarConst.BRANCH_ID));
                car.setReservations(jsonObject.getInt(CarConst.RESERVATIONS));
                car.setMileage(jsonObject.getInt(CarConst.MILEAGE));

                cars.add(car);
            }
            return cars;

        } catch (Exception e) {
            return cars;
        }
    }

/// customers


    @Override
    public int addCustomer(ContentValues contentValues) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(CustomerConst.FIRST_NAME, contentValues.getAsString(CustomerConst.FIRST_NAME));
            params.put(CustomerConst.FAMILY_NAME, contentValues.getAsString(CustomerConst.FAMILY_NAME));
            params.put(CustomerConst.CUSTOMER_ID, contentValues.getAsInteger(CustomerConst.CUSTOMER_ID));
            params.put(CustomerConst.PHONE, contentValues.getAsInteger(CustomerConst.PHONE));
            params.put(CustomerConst.EMAIL, contentValues.getAsString(CustomerConst.EMAIL));
            params.put(CustomerConst.CREDIT_CARD, contentValues.getAsLong(CustomerConst.CREDIT_CARD));
            params.put(CustomerConst.GENDER, contentValues.getAsString(CustomerConst.GENDER));
            params.put(CustomerConst.NUM_ACCIDENTS, 0);
            params.put(CustomerConst.BIRTH_DAY, contentValues.getAsString(CustomerConst.BIRTH_DAY));

            String results = POST(url + "Customer/AddCustomer.php", params);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return contentValues.getAsInteger(CustomerConst.CUSTOMER_ID);
    }

    @Override
    public boolean updateCustomer(ContentValues contentValues) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(CustomerConst.FIRST_NAME, contentValues.getAsString(CustomerConst.FIRST_NAME));
            params.put(CustomerConst.FAMILY_NAME, contentValues.getAsString(CustomerConst.FAMILY_NAME));
            params.put(CustomerConst.CUSTOMER_ID, contentValues.getAsInteger(CustomerConst.CUSTOMER_ID));
            params.put(CustomerConst.PHONE, contentValues.getAsInteger(CustomerConst.PHONE));
            params.put(CustomerConst.EMAIL, contentValues.getAsString(CustomerConst.EMAIL));
            params.put(CustomerConst.CREDIT_CARD, contentValues.getAsLong(CustomerConst.CREDIT_CARD));
            params.put(CustomerConst.GENDER, contentValues.getAsString(CustomerConst.GENDER));
            params.put(CustomerConst.NUM_ACCIDENTS, contentValues.getAsInteger(CustomerConst.NUM_ACCIDENTS));
            params.put(CustomerConst.BIRTH_DAY, contentValues.getAsString(CustomerConst.BIRTH_DAY));

            String results = POST(url + "Customer/UpdateCustomer.php", params);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }

    @Override
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        try {
            JSONArray array = new JSONObject(GET(url + "Customer/GetCustomers.php")).getJSONArray("customers");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Customer customer = new Customer();
                customer.setCustomerID(jsonObject.getInt(CustomerConst.CUSTOMER_ID));
                customer.setFirstName(jsonObject.getString(CustomerConst.FIRST_NAME));
                customer.setFamilyName(jsonObject.getString(CustomerConst.FAMILY_NAME));
                customer.setPhone(jsonObject.getInt(CustomerConst.PHONE));
                customer.setCreditCard(jsonObject.getLong(CustomerConst.CREDIT_CARD));
                customer.setEmail(jsonObject.getString(CustomerConst.EMAIL));
                customer.setBirthDay(jsonObject.getString(CustomerConst.BIRTH_DAY));
                customer.setGender(Gender.valueOf(jsonObject.getString(CustomerConst.GENDER)));
                customer.setNumAccidents(jsonObject.getInt(CustomerConst.NUM_ACCIDENTS));

                customers.add(customer);
            }
        } catch (Exception e) {
        }

        return customers;
    }

/// reservations

    @Override
    public int addReservation(ContentValues contentValues) {

        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(ReservationConst.RESERVATION_ID, contentValues.getAsInteger(ReservationConst.RESERVATION_ID));
            data.put(ReservationConst.CUSTOMER_ID, contentValues.getAsInteger(ReservationConst.CUSTOMER_ID));
            data.put(ReservationConst.CAR_ID, contentValues.getAsInteger(ReservationConst.CAR_ID));
            data.put(ReservationConst.IS_OPEN, 1);
            data.put(ReservationConst.START_DATE, contentValues.getAsString(ReservationConst.START_DATE));
            data.put(ReservationConst.BEGIN_MILEAGE, contentValues.getAsLong(ReservationConst.BEGIN_MILEAGE));

            String results = POST(url + "Reservation/AddReservation.php", data);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
            return contentValues.getAsInteger(ReservationConst.RESERVATION_ID);

        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Reservation getReservation(int reservationID) {
        Reservation reservation = new Reservation();

        try {
            Map<String, Object> data = new LinkedHashMap<>();

            data.put(ReservationConst.RESERVATION_ID, reservationID);

            JSONArray array = new JSONObject(POST(url + "Reservation/GetReservation.php", data)).getJSONArray("reservation");
            JSONObject jsonObject = array.getJSONObject(0);

            reservation.setReservationID(jsonObject.getInt(ReservationConst.RESERVATION_ID));
            reservation.setCustomerID(jsonObject.getInt(ReservationConst.CUSTOMER_ID));
            reservation.setCarID(jsonObject.getInt(ReservationConst.CAR_ID));
            reservation.setOpen(jsonObject.getInt(ReservationConst.IS_OPEN) != 0);
            reservation.setStartDate(jsonObject.getString(ReservationConst.START_DATE));
            reservation.setEndDate(jsonObject.getString(ReservationConst.END_DATE));
            reservation.setBeginMileage(jsonObject.getLong(ReservationConst.BEGIN_MILEAGE));
            reservation.setFinishMileage(jsonObject.getLong(ReservationConst.FINISH_MILEAGE));
            reservation.setGasFull(jsonObject.getInt(ReservationConst.IS_GAS_FULL) != 0);
            reservation.setGasFilled(jsonObject.getInt(ReservationConst.GAS_FILLED));
            reservation.setReservationCost(BigDecimal.valueOf(jsonObject.getDouble(ReservationConst.RESERVATION_COST)).floatValue());

            return reservation;
        } catch (Exception e) {
            return reservation;
        }
    }

    @Override
    public ArrayList<Reservation> getReservationsOnGoing(int customerID) {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();

        try {

            Map<String, Object> data = new LinkedHashMap<>();

            data.put(ReservationConst.CUSTOMER_ID, customerID);

            JSONArray array = new JSONObject(POST(url + "Reservation/GetOnGoingReservations.php", data)).getJSONArray("reservations");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                Reservation reservation = new Reservation();
                reservation.setReservationID(jsonObject.getInt(ReservationConst.RESERVATION_ID));
                reservation.setCustomerID(jsonObject.getInt(ReservationConst.CUSTOMER_ID));
                reservation.setCarID(jsonObject.getInt(ReservationConst.CAR_ID));
                reservation.setOpen(jsonObject.getInt(ReservationConst.IS_OPEN) != 0);
                reservation.setStartDate(jsonObject.getString(ReservationConst.START_DATE));
                reservation.setEndDate(jsonObject.getString(ReservationConst.END_DATE));
                reservation.setBeginMileage(jsonObject.getLong(ReservationConst.BEGIN_MILEAGE));
                reservation.setFinishMileage(jsonObject.getLong(ReservationConst.FINISH_MILEAGE));
                reservation.setGasFull(jsonObject.getInt(ReservationConst.IS_GAS_FULL) != 0);
                reservation.setGasFilled(jsonObject.getInt(ReservationConst.GAS_FILLED));
                reservation.setReservationCost(BigDecimal.valueOf(jsonObject.getDouble(ReservationConst.RESERVATION_COST)).floatValue());

                reservations.add(reservation);
            }
            return reservations;
        } catch (Exception e) {
            return reservations;
        }
    }

    @Override
    public Float closeReservation(ContentValues contentValues) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            Float cost = calculateReservationCost(contentValues.getAsString(ReservationConst.START_DATE),
                    contentValues.getAsString(ReservationConst.END_DATE), contentValues.getAsInteger(ReservationConst.GAS_FILLED),
                    contentValues.getAsLong(ReservationConst.FINISH_MILEAGE) - contentValues.getAsLong(ReservationConst.BEGIN_MILEAGE));
            data.put(ReservationConst.RESERVATION_ID, contentValues.getAsInteger(ReservationConst.RESERVATION_ID));
            data.put(ReservationConst.IS_OPEN, 0);
            data.put(ReservationConst.END_DATE, contentValues.getAsString(ReservationConst.END_DATE));
            data.put(ReservationConst.FINISH_MILEAGE, contentValues.getAsLong(ReservationConst.FINISH_MILEAGE));
            data.put(ReservationConst.IS_GAS_FULL, contentValues.getAsBoolean(ReservationConst.IS_GAS_FULL));
            data.put(ReservationConst.GAS_FILLED, contentValues.getAsInteger(ReservationConst.GAS_FILLED));
            data.put(ReservationConst.RESERVATION_COST, cost);

            String results = POST(url + "Reservation/CloseReservation.php", data);
            if (results.equals("")) {
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }

            return cost;
        } catch (Exception e) {
            return Float.valueOf(-1);
        }
    }

    @Override
    public boolean detectCarsChanges() {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(calendar.getTime());
            calendar.setTimeInMillis(calendar.getTimeInMillis() - 10000);
            String lastDate = sdf.format(calendar.getTime());

            Map<String, Object> data = new LinkedHashMap<>();

            data.put("lastDate", lastDate);
            data.put("currentDate", currentDate);

            String result = POST(url + "Reservation/DetectCarsChanges.php", data);
            if (result.startsWith("true"))
                return true;
            else if (result.startsWith("false"))
                return false;
            else
                throw new Exception("Error");
        } catch (Exception e) {
            return false;
        }
    }

    /// post and get
    private static String GET(String url) throws ExecutionException, InterruptedException {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                return response.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static String POST(String url, Map<String, Object> params) throws IOException {

        //Convert Map<String,Object> into key=value&key=value pairs.
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else return "";
    }

    // help function
    Float calculateReservationCost(String start, String end, int gasCost, long mileage) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);

        Float cost = Float.valueOf((float) ((((endDate.getTime() - startDate.getTime()) / 1000) * 0.003) - gasCost + (mileage * 100)));
        return cost;
    }

}
