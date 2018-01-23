package lugassi.wallach.client_android5778_2638_6575.controller.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
    private TextView totalMileageTextView;
    private TextView oldReservationsTextView;
    private TextView totalRentDaysTextView;
    private TextView accidentsTextView;
    private Button button;

    DB_manager db_manager;
    int customerID;
    Customer customer;
    private String errorMassage = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        customerID = getArguments().getInt(CarRentConst.CustomerConst.CUSTOMER_ID);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_main_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        totalReservationsTextView = (TextView) view.findViewById(R.id.totalReservationsTextView);
        totalRentDaysTextView = (TextView) view.findViewById(R.id.totalRentDaysTextView);
        totalMileageTextView = (TextView) view.findViewById(R.id.totalMileageTextView);
        oldReservationsTextView = (TextView) view.findViewById(R.id.oldReservationsTextView);
        accidentsTextView = (TextView) view.findViewById(R.id.accidentsTextView);
        button = (Button) view.findViewById(R.id.button);

        // get customer details
        new AsyncTask<Object, Object, Customer>() {
            @Override
            protected void onPostExecute(Customer result) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                customer = result;
                nameTextView.setText(result.getFirstName());
                accidentsTextView.setText(((Integer) result.getNumAccidents()).toString());
            }

            @Override
            protected Customer doInBackground(Object... params) {
                try {
                    return db_manager.getCustomer(customerID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();

        //get Promotion details
        new AsyncTask<Object, Object, Promotion>() {
            @Override
            protected void onPostExecute(Promotion promotion) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                totalRentDaysTextView.setText(((Integer) promotion.getTotalRentDays()).toString());
            }

            @Override
            protected Promotion doInBackground(Object... params) {
                try {
                    return db_manager.getPromotion(customerID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();

        new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPostExecute(String integer) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                totalReservationsTextView.setText(integer);
            }

            @Override
            protected String doInBackground(Object... params) {
                try {
                    return db_manager.getCustomerTotalReservations(customerID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();

        new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPostExecute(String integer) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                totalMileageTextView.setText(integer);
            }

            @Override
            protected String doInBackground(Object... params) {
                try {
                    return db_manager.getCustomerTotalMileage(customerID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();


        oldReservationsTextView.setOnClickListener(this);
        button.setOnClickListener(this);
        return view;
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
        if (v == button) {
            new AsyncTask<Object, Object, String>() {
                @Override
                protected void onPostExecute(String result) {
                    if (errorMassage != null) {
                        Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                        errorMassage = null;
                    }
                    if (tryParseInt(result) && Integer.parseInt(result) > 0)
                        accidentsTextView.setText(((Integer) customer.getNumAccidents()).toString());
                }

                @Override
                protected void onPreExecute() {
                    customer.setNumAccidents(customer.getNumAccidents() + 1);
                }

                @Override
                protected String doInBackground(Object... params) {
                    try {
                        return db_manager.updateCustomer(CarRentConst.customerToContentValues(customer));
                    } catch (Exception e) {
                        errorMassage = e.getMessage();
                        return null;
                    }
                }
            }.execute();
        } else if (v == oldReservationsTextView) {
            OldReservations fragment = new OldReservations();
            Bundle args = new Bundle();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            args.putInt(CarRentConst.CustomerConst.CUSTOMER_ID, customerID);
            fragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

}
