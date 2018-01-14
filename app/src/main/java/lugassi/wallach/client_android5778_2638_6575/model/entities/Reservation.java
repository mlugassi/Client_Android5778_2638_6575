package lugassi.wallach.client_android5778_2638_6575.model.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reservation {
    public static int reservationCounter;
    private int reservationID;
    private int customerID;
    private int carID;
    private boolean isOpen;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar returnDate;
    private long beginMileage;
    private long finishMileage;
    private boolean isGasFull;
    private int gasFilled;
    private float reservationCost;

    public Reservation() {
        reservationID = reservationCounter++;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public static void setReservationCounter(int reservationCounter) {
        Reservation.reservationCounter = reservationCounter;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        if (startDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(startDate.getTime());
    }

    public void setStartDate(String startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.startDate = Calendar.getInstance();
            this.startDate.setTime(sdf.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Calendar getEndDate() {
        return endDate;
    }

    public String getEndDateString() {
        if (endDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(endDate.getTime());
    }

    public void setEndDate(String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.endDate = Calendar.getInstance();
            this.endDate.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getReturnDate() {
        return returnDate;
    }

    public String getReturnDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(returnDate.getTime());
    }

    public void setReturnDate(String returnDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.returnDate = Calendar.getInstance();
            this.returnDate.setTime(sdf.parse(returnDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setReturnDate(Calendar returnDate) {
        this.returnDate = returnDate;
    }

    public long getBeginMileage() {
        return beginMileage;
    }

    public void setBeginMileage(long beginMileage) {
        this.beginMileage = beginMileage;
    }

    public long getFinishMileage() {
        return finishMileage;
    }

    public void setFinishMileage(long finishMileage) {
        this.finishMileage = finishMileage;
    }

    public boolean isGasFull() {
        return isGasFull;
    }

    public void setGasFull(boolean gasFull) {
        isGasFull = gasFull;
    }

    public int getGasFilled() {
        return gasFilled;
    }

    public void setGasFilled(int gasFilled) {
        this.gasFilled = gasFilled;
    }

    public float getReservationCost() {
        return reservationCost;
    }

    public void setReservationCost(float reservationCost) {
        this.reservationCost = reservationCost;
    }

}
