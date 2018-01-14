package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Customer;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Gender;

public class AddCustomer extends Activity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_customer);
        db_manager = DBManagerFactory.getManager();
        findViews();
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
        if (TextUtils.isEmpty(birthDayEditText.getText().toString())) {
            birthDayEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (genderSpinner.getSelectedItem() == null)
            return false;
        return true;
    }

    private void updateCustomer() {
//        try {
//            if (!checkValues())
//                return;
//            int maxParkingSpace = Integer.parseInt(parkingSpaceEditText.getText().toString());
//
//            branch.setAddress(addressEditText.getText().toString());
//            branch.setMaxParkingSpace(maxParkingSpace);
//            branch.setCity(cityEditText.getText().toString());
//            branch.setBranchName(nameEditText.getText().toString());
//
//            new AsyncTask<Object, Object, Boolean>() {
//                @Override
//                protected void onPostExecute(Boolean idResult) {
//                    super.onPostExecute(idResult);
//                    if (idResult)
//                        Toast.makeText(getBaseContext(), getString(R.string.textSuccessUpdateBranchMessage), Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(getBaseContext(), getString(R.string.textFailedUpdateMessage), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                protected Boolean doInBackground(Object... params) {
//                    return db_manager.updateBranch(branch.getBranchID(), CarRentConst.branchToContentValues(branch));
//                }
//            }.execute();
//
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), getString(R.string.textFailedUpdateMessage) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    private void addCustomer() {
        try {
            if (!checkValues())
                return;
            int customerID = Integer.parseInt(customerIDEditText.getText().toString());
            int phone = Integer.parseInt(phoneEditText.getText().toString());
            long creditCard = Long.parseLong(creditCardEditText.getText().toString());

            final Customer customer = new Customer();
            customer.setFirstName(firstNameEditText.getText().toString());
            customer.setFamilyName(familyNameEditText.getText().toString());
            customer.setCustomerID(customerID);
            customer.setPhone(phone);
            customer.setEmail(emailEditText.getText().toString());
            customer.setCreditCard(creditCard);
            customer.setGender((Gender)genderSpinner.getSelectedItem());
            customer.setBirthDay(birthDayEditText.getText().toString());

            new AsyncTask<Object, Object, Integer>() {
                @Override
                protected void onPostExecute(Integer idResult) {
                    super.onPostExecute(idResult);
                    Toast.makeText(getBaseContext(), getString(R.string.textSuccessCreateCustomerMessage) + idResult, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCustomer.this , AddUser.class);
                    intent.putExtra(CarRentConst.UserConst.USER_ID , idResult);
                    finish();
                    startActivity(intent);
                }

                @Override
                protected Integer doInBackground(Object... params) {
                    int x = 3;
                    return db_manager.addCustomer(CarRentConst.customerToContentValues(customer));

                }
            }.execute();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getString(R.string.textFiledCreateMessage) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            addCustomer();
        }
    }


}
