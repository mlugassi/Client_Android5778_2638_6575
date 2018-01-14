package lugassi.wallach.client_android5778_2638_6575.model.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Customer {
    private String firstName;
    private String familyName;
    private int customerID;
    private Integer phone;
    private String email;
    private Long creditCard;
    private Gender gender;
    private int numAccidents;
    private Calendar birthDay;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Long creditCard) {
        this.creditCard = creditCard;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getNumAccidents() {
        return numAccidents;
    }

    public void setNumAccidents(int numAccidents) {
        this.numAccidents = numAccidents;
    }

    public Calendar getBirthDay() {
        return birthDay;
    }

    public String getBirthDayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(birthDay.getTime());
    }


    public void setBirthDay(String birthDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.birthDay = Calendar.getInstance();
            this.birthDay.setTime(sdf.parse(birthDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
