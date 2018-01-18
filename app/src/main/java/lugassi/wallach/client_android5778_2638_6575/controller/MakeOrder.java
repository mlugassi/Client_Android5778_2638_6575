package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
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
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

public class MakeOrder extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private int customerID;
    private TextView freeCarTextView;
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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_make_order_fragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_order, container, false);

        searchBar = (SearchView) view.findViewById(R.id.searchBar);
        branchesListView = (ListView) view.findViewById(R.id.branchesListView);
        carsListView = (ListView) view.findViewById(R.id.freeCarListView);
        freeCarTextView = (TextView) view.findViewById(R.id.freeCarsLabel);

        new AsyncTask<Object, Object, ArrayList<Branch>>() {
            @Override
            protected void onPostExecute(ArrayList<Branch> branches) {
                branchesAdapter = new MyListAdapter(getActivity(), branches) {
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
            }

            @Override
            protected ArrayList<Branch> doInBackground(Object... params) {
                return db_manager.getBranches();
            }
        }.execute();


        searchBar.setOnQueryTextListener(this);
        branchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (carsListView.getVisibility() == View.GONE) {
                    carsListView.setVisibility(View.VISIBLE);
                    freeCarTextView.setVisibility(View.VISIBLE);
                }
                final Branch branch = (Branch) branchesListView.getItemAtPosition(position);
                new AsyncTask<Object, Object, ArrayList<Car>>() {
                    @Override
                    protected ArrayList<Car> doInBackground(Object... params) {
                        return db_manager.getFreeCarsByBranchID(branch.getBranchID());
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Car> freeCars) {
                        if (freeCars != null)
                            carsAdapter = new MyListAdapter<Car>(getActivity(), freeCars) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {

                                    if (convertView == null)
                                        convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                                    TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                                    final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                                    final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                                    final Car car = (Car) carsListView.getItemAtPosition(position);
                                    carIdEditText.setText(((Integer) car.getCarID()).toString());
                                    new AsyncTask<Integer, Object, String>() {
                                        @Override
                                        protected void onPostExecute(String branchName) {
                                            branchNameEditText.setText(branchName);
                                        }

                                        @Override
                                        protected String doInBackground(Integer... params) {
                                            return db_manager.getBranch(params[0]).getBranchName();
                                        }
                                    }.execute(car.getBranchID());

                                    new AsyncTask<Integer, Object, String>() {
                                        @Override
                                        protected void onPostExecute(String modelNameAndCompany) {
                                            modelNameAndCompanyEditText.setText(modelNameAndCompany);
                                        }

                                        @Override
                                        protected String doInBackground(Integer... params) {

                                            CarModel carModel = db_manager.getCarModel(params[0]);
                                            return carModel.getModelName() + ", " + carModel.getCompany().name();
                                        }
                                    }.execute(car.getModelCode());

                                    return convertView;
                                }

                            };
                        carsListView.setAdapter(carsAdapter);
                    }
                }.execute();
            }
        });
        carsListView.setOnItemClickListener(this);

        branchesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

        {
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
                final Car car = (Car) carsListView.getItemAtPosition(position);
                final Reservation reservation = new Reservation();
                reservation.setCustomerID(customerID);
                reservation.setCarID(((Car) carsListView.getItemAtPosition(position)).getCarID());
                reservation.setBeginMileage(car.getMileage());
                reservation.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

                new AsyncTask<Object, Object, Integer>() {
                    @Override
                    protected Integer doInBackground(Object... params) {
                        return db_manager.addReservation(CarRentConst.reservationToContentValues(reservation));
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        if (integer > 0)
                            Snackbar.make(getView(), getString(R.string.textSuccessAddReservationMessage) + integer, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(getView(), getString(R.string.textFiledCreateMessage), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        new AsyncTask<Object, Object, ArrayList<Car>>() {
                            @Override
                            protected ArrayList<Car> doInBackground(Object... params) {
                                return db_manager.getFreeCarsByBranchID(car.getBranchID());
                            }

                            @Override
                            protected void onPostExecute(ArrayList<Car> freeCars) {
                                if (freeCars != null)
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
                        };
                    }
                }.execute();

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
