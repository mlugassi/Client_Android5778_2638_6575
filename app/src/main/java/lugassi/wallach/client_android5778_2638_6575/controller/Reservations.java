package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.MyListAdapter;
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
public class Reservations extends Fragment {

    private ListView reservationsListView;
    MyListAdapter<Reservation> reservationAdapter;
    DB_manager db_manager;
    int customerID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        customerID = getArguments().getInt(CarRentConst.CustomerConst.CUSTOMER_ID);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_reservation_fragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        reservationsListView = (ListView) view.findViewById(R.id.reservationsListView);

        new AsyncTask<Object, Object, ArrayList<Reservation>>() {
            @Override
            protected void onPostExecute(ArrayList<Reservation> reservations) {
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

                        new AsyncTask<Integer, Object, String>() {
                            @Override
                            protected void onPostExecute(String s) {
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
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    return null;

                                }
                                return branch.getBranchName();
                            }
                        }.execute(reservation.getCarID());
                        new AsyncTask<Integer, Object, String>() {
                            @Override
                            protected void onPostExecute(String s) {
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
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    return null;
                                }
                                return carModel.getModelName() + ", " + carModel.getCompany().name();
                            }
                        }.execute(reservation.getCarID());

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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return new ArrayList<Reservation>();
                }
            }
        }.execute();

        return view;
    }

}
