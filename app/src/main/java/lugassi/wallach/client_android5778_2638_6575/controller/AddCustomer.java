package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Customer;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.Gender;

public class AddCustomer extends Activity implements View.OnClickListener {

    private int customerID;
    private Customer customer;
    private DB_manager db_manager;
    private EditText firstNameEditText;
    private EditText familyNameEditText;
    private EditText customerIDEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText creditCardEditText;
    private Spinner genderSpinner;
    private EditText birthDayEditText;
    private Button button;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_customer);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        db_manager = DBManagerFactory.getManager();
        findViews();
        setCustomerValues();
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthDayEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void findViews() {
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        familyNameEditText = (EditText) findViewById(R.id.familyNameEditText);
        customerIDEditText = (EditText) findViewById(R.id.customerIDEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        creditCardEditText = (EditText) findViewById(R.id.creditCardEditText);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        birthDayEditText = (EditText) findViewById(R.id.birthDayEditText);
        button = (Button) findViewById(R.id.button);

        genderSpinner.setAdapter(new ArrayAdapter<Gender>(this, android.R.layout.simple_spinner_item, Gender.values()));
        button.setOnClickListener(this);
        birthDayEditText.setOnClickListener(this);
    }

    // set customer details for option to be called to update specific branch
    void setCustomerValues() {
        customerID = getIntent().getIntExtra(CarRentConst.CustomerConst.CUSTOMER_ID, -1);
        if (customerID >= 0) {
            new AsyncTask<Integer, Object, Customer>() {
                @Override
                protected void onPostExecute(Customer o) {
                    if (o == null) return;
                    customer = o;
                    firstNameEditText.setText(customer.getFirstName());
                    familyNameEditText.setText(customer.getFamilyName());
                    customerIDEditText.setText(((Integer) customer.getCustomerID()).toString());
                    phoneEditText.setText(customer.getPhoneString());
                    emailEditText.setText(customer.getEmail());
                    creditCardEditText.setText(((Long) customer.getCreditCard()).toString());
                    birthDayEditText.setText(customer.getBirthDayString());
                    button.setText(getString(R.string.buttonUpdate));
                    customerIDEditText.setVisibility(View.GONE);
                }

                @Override
                protected Customer doInBackground(Integer... params) {
                    try {
                        return db_manager.getCustomer(customerID);
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return null;

                    }
                }
            }.execute(customerID);

        } else resetInput();
    }

    private void resetInput() {
        firstNameEditText.setText("");
        familyNameEditText.setText("");
        customerIDEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        creditCardEditText.setText("");
        birthDayEditText.setText("");
        customer = null;
    }

    private boolean checkValues() {
        if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {
            firstNameEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (TextUtils.isEmpty(familyNameEditText.getText().toString())) {
            familyNameEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (TextUtils.isEmpty(customerIDEditText.getText().toString())) {
            customerIDEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (!tryParseInt(customerIDEditText.getText().toString())) {
            customerIDEditText.setError(getString(R.string.exceptionNumberFileds));
            return false;
        }
        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            phoneEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (TextUtils.isEmpty(creditCardEditText.getText().toString())) {
            creditCardEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (!tryParseLong(customerIDEditText.getText().toString())) {
            customerIDEditText.setError(getString(R.string.exceptionNumberFileds));
            return false;
        }
        if (TextUtils.isEmpty(birthDayEditText.getText().toString())) {
            birthDayEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (genderSpinner.getSelectedItem() == null)
            return false;
        return true;
    }

    private void updateCustomer() {
        try {
            if (!checkValues())
                return;
            int customerID = Integer.parseInt(customerIDEditText.getText().toString());
            int phone = Integer.parseInt(phoneEditText.getText().toString());
            long creditCard = Long.parseLong(creditCardEditText.getText().toString());

            customer.setFirstName(firstNameEditText.getText().toString());
            customer.setFamilyName(familyNameEditText.getText().toString());
            customer.setCustomerID(customerID);
            customer.setPhone(phone);
            customer.setEmail(emailEditText.getText().toString());
            customer.setCreditCard(creditCard);
            customer.setGender((Gender) genderSpinner.getSelectedItem());
            customer.setBirthDay(birthDayEditText.getText().toString());

            new AsyncTask<Object, Object, String>() {
                @Override
                protected void onPostExecute(String idResult) {
                    if (tryParseInt(idResult) && Integer.parseInt(idResult) > 0)
                        Toast.makeText(getBaseContext(), getString(R.string.textSuccessUpdateCustomerMessage), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getBaseContext(), getString(R.string.textFailedUpdateMessage) + "\n" + idResult, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected String doInBackground(Object... params) {
                    try {
                        return db_manager.updateCustomer(CarRentConst.customerToContentValues(customer));
                    } catch (Exception e) {
                        return e.getMessage();

                    }
                }
            }.execute();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getString(R.string.textFailedUpdateMessage) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addCustomer() {
        try {
            if (!checkValues())
                return;
            final int customerID = Integer.parseInt(customerIDEditText.getText().toString());
            int phone = Integer.parseInt(phoneEditText.getText().toString());
            long creditCard = Long.parseLong(creditCardEditText.getText().toString());

            final Customer customer = new Customer();
            customer.setFirstName(firstNameEditText.getText().toString());
            customer.setFamilyName(familyNameEditText.getText().toString());
            customer.setCustomerID(customerID);
            customer.setPhone(phone);
            customer.setEmail(emailEditText.getText().toString());
            customer.setCreditCard(creditCard);
            customer.setGender((Gender) genderSpinner.getSelectedItem());
            customer.setBirthDay(birthDayEditText.getText().toString());

            new AsyncTask<Object, Object, String>() {
                @Override
                protected void onPostExecute(String idResult) {
                    if (tryParseInt(idResult) && Integer.parseInt(idResult) > 0) {
                        Toast.makeText(getBaseContext(), getString(R.string.textSuccessCreateCustomerMessage) + idResult, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddCustomer.this, AddUser.class);
                        intent.putExtra(CarRentConst.UserConst.USER_ID, idResult);
                        finish();
                        startActivity(intent);
                    } else
                        Toast.makeText(getBaseContext(), getString(R.string.textFiledCreateMessage) + "\n" + idResult, Toast.LENGTH_SHORT).show();

                }

                @Override
                protected String doInBackground(Object... params) {
                    try {
                        return db_manager.addCustomer(CarRentConst.customerToContentValues(customer));
                    } catch (Exception e) {
                        return e.getMessage();
                    }

                }
            }.execute();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getString(R.string.textFiledCreateMessage) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean tryParseLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            if (customerID == -1) addCustomer();
            else updateCustomer();
        } else if (v == birthDayEditText) {
            new DatePickerDialog(AddCustomer.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }
}
