package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
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
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.EngineCapacity;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

public class MakeOrder extends Fragment implements SearchView.OnQueryTextListener, OnItemClickListener, AdapterView.OnItemLongClickListener {

    private int customerID;
    private TextView freeCarTextView;
    private SearchView searchBar;
    private ListView branchesListView;
    private ListView carsListView;
    private DB_manager db_manager;
    private MyListAdapter branchesAdapter;
    private MyListAdapter carsAdapter;
    private ArrayList<CarModel> favoriteModels;

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

        getFavoriteCarModels();

        // get branches list
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
                        nameAnIdTextView.setText(branch.getBranchName());
                        addressTextView.setText(branch.getAddress());

                        return convertView;
                    }
                };
                branchesListView.setAdapter(branchesAdapter);
            }

            @Override
            protected ArrayList<Branch> doInBackground(Object... params) {
                try {
                    return db_manager.getBranches();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return new ArrayList<Branch>();
                }
            }
        }.execute();


        branchesListView.setOnItemClickListener(this);
        searchBar.setOnQueryTextListener(this);
        carsListView.setOnItemClickListener(this);
        branchesListView.setOnItemLongClickListener(this);

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
        if (view.getParent() == carsListView) {
            // open do order dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.textDialogTitle));
            builder.setMessage(getString(R.string.textDialogMessage));
            builder.setPositiveButton(getString(R.string.buttonYes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // on ok press create new reservation
                    final Car car = (Car) carsListView.getItemAtPosition(position);
                    final Reservation reservation = new Reservation();
                    reservation.setCustomerID(customerID);
                    reservation.setCarID(((Car) carsListView.getItemAtPosition(position)).getCarID());
                    reservation.setBeginMileage(car.getMileage());
                    reservation.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

                    new AsyncTask<Object, Object, Integer>() {
                        @Override
                        protected Integer doInBackground(Object... params) {
                            try {

                                return db_manager.addReservation(CarRentConst.reservationToContentValues(reservation));
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                return -1;
                            }
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
                                    try {
                                        return db_manager.getFreeCarsByBranchID(car.getBranchID());
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        return new ArrayList<Car>();
                                    }
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
                                                try {
                                                    for (Branch branch : db_manager.getBranches())
                                                        if (branch.getBranchID() == car.getBranchID())
                                                            branchName = branch.getBranchName();
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                                try {
                                                    for (CarModel carModel : db_manager.getCarModels())
                                                        if (carModel.getModelCode() == car.getModelCode()) {
                                                            modelName = carModel.getModelName();
                                                            companyName = carModel.getCompany().name();
                                                        }
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        } else if (view.getParent() == branchesListView) {
            /// open cars of selected branch
            if (carsListView.getVisibility() == View.GONE) {
                carsListView.setVisibility(View.VISIBLE);
                freeCarTextView.setVisibility(View.VISIBLE);
            }
            final Branch branch = (Branch) branchesListView.getItemAtPosition(position);

            //get cars of the selected branch
            new AsyncTask<Object, Object, ArrayList<Car>>() {
                @Override
                protected ArrayList<Car> doInBackground(Object... params) {
                    try {
                        return db_manager.getFreeCarsByBranchID(branch.getBranchID());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return new ArrayList<Car>();

                    }
                }

                @Override
                protected void onPostExecute(ArrayList<Car> freeCars) {
                    carsAdapter = new MyListAdapter<Car>(getActivity(), freeCars) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            if (convertView == null)
                                convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                            TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                            final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                            final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);
                            final ImageButton favoriteImageButton = (ImageButton) convertView.findViewById(R.id.favoriteButton);


                            final Car car = (Car) carsListView.getItemAtPosition(position);
                            carIdEditText.setText(((Integer) car.getCarID()).toString());
                            new AsyncTask<Integer, Object, String>() {
                                @Override
                                protected void onPostExecute(String branchName) {
                                    if (branchName != null)
                                        branchNameEditText.setText(branchName);
                                }

                                @Override
                                protected String doInBackground(Integer... params) {
                                    try {
                                        return db_manager.getBranch(params[0]).getBranchName();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        return null;
                                    }
                                }
                            }.execute(car.getBranchID());

                            new AsyncTask<Integer, Object, CarModel>() {
                                @Override
                                protected void onPostExecute(final CarModel carModel) {
                                    if (carModel == null) return;
                                    modelNameAndCompanyEditText.setText(carModel.getModelName() + ", " + carModel.getCompany().name());
                                    favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isModelFavorite(carModel.getModelCode())) {
                                                removeModelFromFavorite(carModel);
                                                favoriteImageButton.setBackgroundResource(R.drawable.favorite_empty);
                                            } else {
                                                addModelToFavorite(carModel);
                                                favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
                                            }

                                        }
                                    });
                                }

                                @Override
                                protected CarModel doInBackground(Integer... params) {
                                    try {
                                        return db_manager.getCarModel(params[0]);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        return null;
                                    }
                                }
                            }.execute(car.getModelCode());
                            if (isModelFavorite(car.getModelCode()))
                                favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
                            else
                                favoriteImageButton.setBackgroundResource(R.drawable.favorite_empty);

                            return convertView;
                        }

                    };
                    carsListView.setAdapter(carsAdapter);
                }
            }.execute();
        }
    }

    /// get from content provider the favorite models
    void getFavoriteCarModels() {
        favoriteModels = new ArrayList<CarModel>();
        String[] projection = new String[]{CarRentConst.DataBaseConstants.MODEL_CODE, CarRentConst.DataBaseConstants.MODEL_NAME,
                CarRentConst.DataBaseConstants.COMPANY, CarRentConst.DataBaseConstants.ENGINE_CAPACITY,
                CarRentConst.DataBaseConstants.SEATS, CarRentConst.DataBaseConstants.CAR_TYPE,
                CarRentConst.DataBaseConstants.MAX_GAS_TANK};
        Cursor cursor = getActivity().getContentResolver().query(CarRentConst.ContentProviderConstants.CONTENT_URI,
                projection, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                CarModel carModel = new CarModel(cursor.getInt(cursor.getColumnIndex(CarRentConst.DataBaseConstants.MODEL_CODE)));
                carModel.setModelName(cursor.getString(cursor.getColumnIndex(CarRentConst.DataBaseConstants.MODEL_NAME)));
                carModel.setCompany(Company.valueOf(cursor.getString(cursor.getColumnIndex(CarRentConst.DataBaseConstants.COMPANY))));
                carModel.setEngineCapacity(EngineCapacity.valueOf(cursor.getString(cursor.getColumnIndex(CarRentConst.DataBaseConstants.ENGINE_CAPACITY))));
                carModel.setSeats(cursor.getInt(cursor.getColumnIndex(CarRentConst.DataBaseConstants.SEATS)));
                carModel.setCarType(CarType.valueOf(cursor.getString(cursor.getColumnIndex(CarRentConst.DataBaseConstants.CAR_TYPE))));
                carModel.setMaxGasTank(cursor.getInt(cursor.getColumnIndex(CarRentConst.DataBaseConstants.MAX_GAS_TANK)));
                favoriteModels.add(carModel);
            } while (cursor.moveToNext());
        }

    }

    // check if model is favorite
    boolean isModelFavorite(int modelCode) {

        for (CarModel carModel : favoriteModels)
            if (carModel.getModelCode() == modelCode)
                return true;
        return false;
    }

    // add model to favorite and update view
    void addModelToFavorite(CarModel carModel) {
        try {
            Uri uri = getActivity().getContentResolver().insert(CarRentConst.ContentProviderConstants.CONTENT_URI, CarRentConst.carModelToContentValues(carModel));
            favoriteModels.add(carModel);
            carsAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.textAddModelFavorite) + " " + carModel.getModelCode(), Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    // remove model from favorite and update view
    void removeModelFromFavorite(CarModel carModel) {
        try {
            int uri = getActivity().getContentResolver().delete(CarRentConst.ContentProviderConstants.CONTENT_URI,
                    CarRentConst.DataBaseConstants.MODEL_CODE + "=?", new String[]{new String(((Integer) carModel.getModelCode()).toString())});
            if (uri == 0) return;
            for (CarModel temp : favoriteModels)
                if (carModel.getModelCode() == temp.getModelCode()) {
                    favoriteModels.remove(temp);
                    break;
                }
            carsAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.textRemoveModelFavorite) + " " + carModel.getModelCode(), Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // open branch dialog details
        Bundle args = new Bundle();
        args.putInt(CarRentConst.BranchConst.BRANCH_ID, ((Branch) branchesListView.getItemAtPosition(position)).getBranchID());
        BranchDetails myDialogFragment = new BranchDetails();
        myDialogFragment.setArguments(args);
        myDialogFragment.show(getActivity().getFragmentManager(), "Branch Details");

        return false;
    }
}
