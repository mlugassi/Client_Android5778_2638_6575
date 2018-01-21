package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Customer;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Promotion;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    private TextView nameTextView;
    private TextView totalReservationsTextView;
    private TextView totalRentDaysTextView;
    private TextView accidentsTextView;
    private Button button;

    DB_manager db_manager;
    int customerID;
    Customer customer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        customerID = getArguments().getInt(CarRentConst.CustomerConst.CUSTOMER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        totalReservationsTextView = (TextView) view.findViewById(R.id.totalReservationsTextView);
        totalRentDaysTextView = (TextView) view.findViewById(R.id.totalRentDaysTextView);
        accidentsTextView = (TextView) view.findViewById(R.id.accidentsTextView);
        button = (Button) view.findViewById(R.id.button);

        // get customer details
        new AsyncTask<Object, Object, Customer>() {
            @Override
            protected void onPostExecute(Customer result) {
                customer = result;
                nameTextView.setText(result.getFirstName());
                accidentsTextView.setText(((Integer) result.getNumAccidents()).toString());
            }

            @Override
            protected Customer doInBackground(Object... params) {
                try {
                    return db_manager.getCustomer(customerID);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }.execute();

        //get Promotion details
        new AsyncTask<Object, Object, Promotion>() {
            @Override
            protected void onPostExecute(Promotion promotion) {
                totalRentDaysTextView.setText(((Integer) promotion.getTotalRentDays()).toString());
            }

            @Override
            protected Promotion doInBackground(Object... params) {
                try {
                    return db_manager.getPromotion(customerID);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }.execute();

        new AsyncTask<Object, Object, Integer>() {
            @Override
            protected void onPostExecute(Integer integer) {
                totalReservationsTextView.setText(integer.toString());
            }

            @Override
            protected Integer doInBackground(Object... params) {
                try {
                    return db_manager.getCustomerTotalReservations(customerID);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }.execute();


        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            new AsyncTask<Object, Object, Boolean>() {
                @Override
                protected void onPostExecute(Boolean result) {
                    if (result) {
                        accidentsTextView.setText(((Integer) customer.getNumAccidents()).toString());
                    }
                }

                @Override
                protected void onPreExecute() {
                    customer.setNumAccidents(customer.getNumAccidents() + 1);
                }

                @Override
                protected Boolean doInBackground(Object... params) {
                    try {
                        return db_manager.updateCustomer(CarRentConst.customerToContentValues(customer));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }.execute();
        }
    }

}
