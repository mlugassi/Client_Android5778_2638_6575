package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarDetails extends DialogFragment {


    DB_manager db_manager;
    Car car;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        try {
        car=  new AsyncTask<Integer, Object, Car>() {
                @Override
                protected void onPostExecute(Car result) {
                    super.onPostExecute(result);
                }

                @Override
                protected Car doInBackground(Integer... params) {
                    return db_manager.getCar(params[0]);
                }
            }.execute(getArguments().getInt(CarRentConst.CarConst.CAR_ID)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_car_details, container, false);
        new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPostExecute(String branchName) {
                ((TextView) view.findViewById(R.id.branchNameTextView)).setText(branchName);
            }

            @Override
            protected String doInBackground(Object... params) {
                return db_manager.getBranch(car.getBranchID()).getBranchName();
            }
        }.execute();
        new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPostExecute(String modelName) {
                ((TextView) view.findViewById(R.id.modelNameTextView)).setText(modelName);
            }

            @Override
            protected String doInBackground(Object... params) {
                return db_manager.getCarModel(car.getModelCode()).getModelName();
            }
        }.execute();

        ((TextView) view.findViewById(R.id.carIdTextView)).setText(((Integer) car.getCarID()).toString());
        ((TextView) view.findViewById(R.id.mileageTextView)).setText(((Long) car.getMileage()).toString());
        ((TextView) view.findViewById(R.id.reservationsTextView)).setText(((Integer) car.getReservations()).toString());

        return view;
    }

}
