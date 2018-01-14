package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Promotion;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

public class MakeOrder extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private int customerID;
    private SearchView searchBar;
    private ListView branchesListView;
    private ListView carsListView;
    private DB_manager db_manager;
    private MyListAdapter branchesAdapter;
    private MyListAdapter carsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        customerID = getArguments().getInt(CarRentConst.CustomerConst.CUSTOMER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_order, container, false);

        searchBar = (SearchView) view.findViewById(R.id.searchBar);
        branchesListView = (ListView) view.findViewById(R.id.branchesListView);
        carsListView = (ListView) view.findViewById(R.id.freeCarListView);

        branchesAdapter = new MyListAdapter(getActivity(), db_manager.getBranches()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null)
                    convertView = View.inflate(getActivity(), R.layout.branch_list_view, null);

                TextView nameAnIdTextView = (TextView) convertView.findViewById(R.id.nameAndIdEditText);
                TextView addressTextView = (TextView) convertView.findViewById(R.id.addressEditText);

                Branch branch = (Branch) branchesListView.getItemAtPosition(position);
                nameAnIdTextView.setText(((Integer) branch.getBranchID()).toString() + " " + branch.getBranchName());
                addressTextView.setText(branch.getAddress());

                return convertView;
            }
        };
        branchesListView.setAdapter(branchesAdapter);

        searchBar.setOnQueryTextListener(this);
        branchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Branch branch = (Branch) branchesListView.getItemAtPosition(position);
                ArrayList<Car> freeCars = db_manager.getFreeCars(branch.getBranchID());
                carsAdapter = new MyListAdapter<Car>(getActivity(), freeCars) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        if (convertView == null)
                            convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                        TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                        TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                        TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                        Car car = (Car) carsListView.getItemAtPosition(position);
                        String branchName = "", modelName = "", companyName = "";
                        for (Branch branch : db_manager.getBranches())
                            if (branch.getBranchID() == car.getBranchID())
                                branchName = branch.getBranchName();
                        for (CarModel carModel : db_manager.getCarModels())
                            if (carModel.getModelCode() == car.getModelCode()) {
                                modelName = carModel.getModelName();
                                companyName = carModel.getCompany().name();
                            }

                        carIdEditText.setText(((Integer) car.getCarID()).toString());
                        modelNameAndCompanyEditText.setText(modelName + ", " + companyName);
                        branchNameEditText.setText(branchName);

                        return convertView;
                    }
                };
                carsListView.setAdapter(carsAdapter);
            }
        });
        carsListView.setOnItemClickListener(this);

        branchesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putInt(CarRentConst.BranchConst.BRANCH_ID, ((Branch) branchesListView.getItemAtPosition(position)).getBranchID());
                BranchDetails myDialogFragment = new BranchDetails();
                myDialogFragment.setArguments(args);
                myDialogFragment.show(getActivity().getFragmentManager(), "Branch Details");

                return false;
            }
        });

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        branchesAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.textDialogTitle));
        builder.setMessage(getString(R.string.textDialogMessage));
        builder.setPositiveButton(getString(R.string.buttonYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Car car = (Car) carsListView.getItemAtPosition(position);
                Reservation reservation = new Reservation();
                reservation.setCustomerID(customerID);
                reservation.setCarID(((Car) carsListView.getItemAtPosition(position)).getCarID());
                reservation.setBeginMileage(car.getMileage());
                reservation.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

                int reservationID = db_manager.addReservation(CarRentConst.reservationToContentValues(reservation));
                if (reservationID > 0)
                    Snackbar.make(getView(), getString(R.string.textSuccessAddReservationMessage) + reservationID, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(getView(), getString(R.string.textFiledCreateMessage), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                ArrayList<Car> freeCars = db_manager.getFreeCars(car.getBranchID());
                carsAdapter = new MyListAdapter<Car>(getActivity(), freeCars) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        if (convertView == null)
                            convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                        TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                        TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                        TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                        Car car = (Car) carsListView.getItemAtPosition(position);
                        String branchName = "", modelName = "", companyName = "";
                        for (Branch branch : db_manager.getBranches())
                            if (branch.getBranchID() == car.getBranchID())
                                branchName = branch.getBranchName();
                        for (CarModel carModel : db_manager.getCarModels())
                            if (carModel.getModelCode() == car.getModelCode()) {
                                modelName = carModel.getModelName();
                                companyName = carModel.getCompany().name();
                            }

                        carIdEditText.setText(((Integer) car.getCarID()).toString());
                        modelNameAndCompanyEditText.setText(modelName + ", " + companyName);
                        branchNameEditText.setText(branchName);

                        return convertView;
                    }
                };
                carsListView.setAdapter(carsAdapter);
            }
        });
        builder.setNegativeButton(getString(R.string.buttonNo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

}
