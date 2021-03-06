package lugassi.wallach.client_android5778_2638_6575.controller.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.controller.dialogs.DialogFinishOrder;
import lugassi.wallach.client_android5778_2638_6575.controller.dialogs.ReservationDetails;
import lugassi.wallach.client_android5778_2638_6575.model.MyListAdapter;
import lugassi.wallach.client_android5778_2638_6575.model.MyReceiver;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reservations extends Fragment implements AdapterView.OnItemLongClickListener {

    private ListView reservationsListView;
    MyListAdapter<Reservation> reservationAdapter;
    DB_manager db_manager;
    int customerID;
    private String errorMassage = null;

    /// detect reservations changes
    private MyReceiver reservationsChangedReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            /// update reservation list
            new AsyncTask<Integer, Object, ArrayList<Reservation>>() {
                @Override
                protected void onPostExecute(ArrayList<Reservation> result) {
                    if (errorMassage != null) {
                        Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                        errorMassage = null;
                    }
                    reservationAdapter.setData(result);
                    reservationAdapter.notifyDataSetChanged();
                }

                @Override
                protected ArrayList<Reservation> doInBackground(Integer... params) {
                    try {
                        return db_manager.getReservationsOnGoing(customerID);
                    } catch (Exception e) {
                        errorMassage = e.getMessage();
                        return new ArrayList<Reservation>();
                    }
                }
            }.execute();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        customerID = getArguments().getInt(CarRentConst.CustomerConst.CUSTOMER_ID);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_reservation_fragment));
        getActivity().registerReceiver(reservationsChangedReceiver, new IntentFilter(CarRentConst.MyIntentFilter.RESERVATIONS_CHANGED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        reservationsListView = (ListView) view.findViewById(R.id.reservationsListView);


        /// get open reservations
        new AsyncTask<Object, Object, ArrayList<Reservation>>() {
            @Override
            protected void onPostExecute(ArrayList<Reservation> reservations) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                reservationAdapter = new MyListAdapter<Reservation>(getActivity(), reservations) {
                    public View getView(int position, View convertView, ViewGroup parent) {

                        if (convertView == null)
                            convertView = View.inflate(getActivity(), R.layout.reservastion_list_view, null);

                        TextView startDateEditText = (TextView) convertView.findViewById(R.id.startDateEditText);
                        TextView reservationIDEditText = (TextView) convertView.findViewById(R.id.reservationIDTextView);
                        TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                        final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                        final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);
                        ImageButton finishOrderButton = (ImageButton) convertView.findViewById(R.id.finishOrderButton);

                        Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                        reservationIDEditText.setText(((Integer) reservation.getReservationID()).toString());
                        carIdEditText.setText(((Integer) reservation.getCarID()).toString());
                        startDateEditText.setText(reservation.getStartDateString());


                        /// get branch details
                        new AsyncTask<Integer, Object, String>() {
                            @Override
                            protected void onPostExecute(String s) {
                                if (errorMassage != null) {
                                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                                    errorMassage = null;
                                }
                                if (s != null)
                                    branchNameEditText.setText(s);
                            }

                            @Override
                            protected String doInBackground(Integer... params) {
                                Branch branch = null;
                                try {
                                    Car car = db_manager.getCar(params[0]);
                                    branch = db_manager.getBranch(car.getBranchID());

                                } catch (Exception e) {
                                    errorMassage = e.getMessage();
                                    return null;

                                }
                                return branch.getBranchName();
                            }
                        }.execute(reservation.getCarID());


                        /// get model details
                        new AsyncTask<Integer, Object, String>() {
                            @Override
                            protected void onPostExecute(String s) {
                                if (errorMassage != null) {
                                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                                    errorMassage = null;
                                }
                                if (s != null)
                                    modelNameAndCompanyEditText.setText(s);
                            }

                            @Override
                            protected String doInBackground(Integer... params) {
                                CarModel carModel = null;
                                try {
                                    Car car = db_manager.getCar(params[0]);
                                    carModel = db_manager.getCarModel(car.getModelCode());
                                } catch (Exception e) {
                                    errorMassage = e.getMessage();
                                    return null;
                                }
                                return carModel.getModelName() + ", " + carModel.getCompany().name();
                            }
                        }.execute(reservation.getCarID());

                        /// close reservation listener
                        finishOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View view = (View) v.getParent();

                                Bundle args = new Bundle();
                                args.putInt(CarRentConst.ReservationConst.RESERVATION_ID, Integer.parseInt(((TextView) view.findViewById(R.id.reservationIDTextView)).getText().toString()));
                                DialogFinishOrder myDialogFragment = new DialogFinishOrder();
                                myDialogFragment.setArguments(args);
                                myDialogFragment.show(getActivity().getFragmentManager(), "Finish Order");
                            }
                        });

                        return convertView;
                    }

                };
                reservationsListView.setAdapter(reservationAdapter);
            }

            @Override
            protected ArrayList<Reservation> doInBackground(Object... params) {
                try {
                    return db_manager.getReservationsOnGoing(customerID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return new ArrayList<Reservation>();
                }
            }
        }.execute();

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // open reservation dialog details
        Bundle args = new Bundle();
        args.putInt(CarRentConst.ReservationConst.RESERVATION_ID, ((Reservation) reservationsListView.getItemAtPosition(position)).getReservationID());
        ReservationDetails myDialogFragment = new ReservationDetails();
        myDialogFragment.setArguments(args);
        myDialogFragment.show(getActivity().getFragmentManager(), "Reservation Details");

        return false;
    }

}
