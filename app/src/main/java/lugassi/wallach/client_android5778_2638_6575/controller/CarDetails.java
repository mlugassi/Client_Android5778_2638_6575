package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Dialog;
import android.app.DialogFragment;
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
        car = db_manager.getCar(getArguments().getInt(CarRentConst.CarConst.CAR_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_car_details, container, false);

        Branch branch = db_manager.getBranch(car.getBranchID());
        ((TextView) view.findViewById(R.id.carIdTextView)).setText(((Integer) car.getCarID()).toString());
        ((TextView) view.findViewById(R.id.branchNameTextView)).setText(branch.getBranchName());
        ((TextView) view.findViewById(R.id.modelNameTextView)).setText(db_manager.getCarModel(car.getModelCode()).getModelName());
        ((TextView) view.findViewById(R.id.mileageTextView)).setText(((Long) car.getMileage()).toString());
        ((TextView) view.findViewById(R.id.reservationsTextView)).setText(((Integer) car.getReservations()).toString());

        ArrayList<String> branchDetails = new ArrayList<String>();
        branchDetails.add(((Integer) branch.getBranchID()).toString());
        branchDetails.add(branch.getBranchName());
        branchDetails.add(branch.getCity());
        branchDetails.add(branch.getAddress());
        branchDetails.add(((Integer) branch.getMaxParkingSpace()).toString());
        branchDetails.add(((Integer) branch.getActualParkingSpace()).toString());
        return view;
    }

}
