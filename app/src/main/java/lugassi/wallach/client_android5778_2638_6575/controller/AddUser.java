package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Promotion;

public class AddUser extends Activity implements View.OnClickListener {

    private DB_manager db_manager;


    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createButton;
    private int customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_user);
        db_manager = DBManagerFactory.getManager();
        customerID = getIntent().getIntExtra(CarRentConst.UserConst.USER_ID, -1);
        findViews();
    }

    private void findViews() {
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        createButton = (Button) findViewById(R.id.createButton);

        createButton.setOnClickListener(this);
    }

    private boolean checkValues() {
        if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
            userNameEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (passwordEditText.getText().toString().length() < 6) {
            passwordEditText.setError(getString(R.string.exceptionLongLessFileds));
            return false;
        }
        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            confirmPasswordEditText.setError(getString(R.string.exceptionPassword));
            return false;
        }
        if (userNameEditText.getText().toString().length() > 25) {
            userNameEditText.setError(getString(R.string.exceptionUserName));
            return false;
        }
        return true;
    }

    private void addUser() {
        try {
            if (!checkValues())
                return;
            final String userName = userNameEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            /// adding user
            new AsyncTask<Object, Object, String>() {
                @Override
                protected void onPostExecute(String idResult) {
                    if (tryParseInt(idResult) && Integer.parseInt(idResult) > 0) {
                        // if success open manage activity
                        Intent intent = new Intent(AddUser.this, MainNavigation.class);
                        finish();
                        AddUser.this.startActivity(intent);
                    } else
                        Toast.makeText(getBaseContext(), getString(R.string.textFiledAddMessage) + "\n" + idResult, Toast.LENGTH_SHORT).show();

                }

                @Override
                protected String doInBackground(Object... params) {
                    try {
                        db_manager.createUser(userName, password, customerID);
                        return db_manager.addPromotion(CarRentConst.promotionToContentValues(new Promotion(customerID)));
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            }.execute();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getString(R.string.textFiledAddMessage) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        if (v == createButton) {
            addUser();
        }
    }

    @Override
    public void onBackPressed() {
        /// if cancel remove the customer that created before
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return db_manager.removeCustomer(customerID);
            }
        }.execute();
        super.onBackPressed();

    }
}
