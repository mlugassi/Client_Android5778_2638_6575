package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.MyListAdapter;
import lugassi.wallach.client_android5778_2638_6575.model.MyReceiver;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.EngineCapacity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeCars extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    private DB_manager db_manager;
    private ListView freeCarsListView;
    private AutoCompleteTextView carModelsAutoCompleteTextView;
    private Integer modelCode;
    private MyListAdapter<Car> carsAdapter;
    private ArrayList<CarModel> favoriteModels;
    private String errorMassage = null;

    private MyReceiver carChangedReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!carModelsAutoCompleteTextView.getText().toString().equals(""))
                // updte cars list and refresh listview
                new AsyncTask<Integer, Object, ArrayList<Car>>() {
                    @Override
                    protected void onPostExecute(ArrayList<Car> cars) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        carsAdapter = getCarsAdapter(cars);
                        freeCarsListView.setAdapter(carsAdapter);
                    }

                    @Override
                    protected ArrayList<Car> doInBackground(Integer... params) {
                        try {
                            return db_manager.getFreeCars(params[0]);
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return new ArrayList<Car>();
                        }
                    }
                }.execute(modelCode);
            else // if filter is empty get all free cars
                new AsyncTask<Integer, Object, ArrayList<Car>>() {
                    @Override
                    protected void onPostExecute(ArrayList<Car> result) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        carsAdapter = getCarsAdapter(result);
                        freeCarsListView.setAdapter(carsAdapter);
                    }

                    @Override
                    protected ArrayList<Car> doInBackground(Integer... params) {
                        try {
                            return db_manager.getFreeCars();
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return new ArrayList<Car>();
                        }
                    }
                }.execute();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        modelCode = -1;
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_free_cars_fragment));
        getActivity().registerReceiver(carChangedReceiver, new IntentFilter(CarRentConst.MyIntentFilter.CARS_CHANGED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_free_cars, container, false);
        freeCarsListView = (ListView) view.findViewById(R.id.freeCarListView);
        carModelsAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.carModelsAutoCompleteTextView);
        getFavoriteCarModels();
        /// download car models list and create model list for filtering
        new AsyncTask<Object, Object, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                try {
                    ArrayList<String> carModelsStrings = new ArrayList<String>();
                    ArrayList<CarModel> carModels = db_manager.getCarModels();
                    for (CarModel carModel : carModels)
                        carModelsStrings.add(carModel.getCompany().name() + ", " + carModel.getModelName() + "\n" + getString(R.string.textModelCode) + ": " + carModel.getModelCode());
                    return carModelsStrings;

                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return new ArrayList<String>();
                }
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                carModelsAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, strings));
                carModelsAutoCompleteTextView.setThreshold(1);
            }
        }.execute();

        // get free cars
        new AsyncTask<Integer, Object, ArrayList<Car>>() {
            @Override
            protected void onPostExecute(ArrayList<Car> result) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                carsAdapter = getCarsAdapter(result);
                freeCarsListView.setAdapter(carsAdapter);
            }

            @Override
            protected ArrayList<Car> doInBackground(Integer... params) {
                try {
                    return db_manager.getFreeCars();
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return new ArrayList<Car>();

                }
            }
        }.execute();

        carModelsAutoCompleteTextView.setOnClickListener(this);
        freeCarsListView.setOnItemLongClickListener(this);
        carModelsAutoCompleteTextView.setOnItemClickListener(this);
        carModelsAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /// returns to all free cars
                if (carModelsAutoCompleteTextView.getText().toString().equals("")) {
                    modelCode = -1;
                    // get all free cars
                    new AsyncTask<Integer, Object, ArrayList<Car>>() {
                        @Override
                        protected void onPostExecute(ArrayList<Car> result) {
                            carsAdapter = getCarsAdapter(result);
                            freeCarsListView.setAdapter(carsAdapter);
                        }

                        @Override
                        protected ArrayList<Car> doInBackground(Integer... params) {
                            try {
                                return db_manager.getFreeCars();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                return new ArrayList<Car>();

                            }
                        }
                    }.execute();

                }
            }
        });

        return view;
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
    void addModelToFavorite(final int modelCode) {
        try {
            /// download car model details and add to content provider
            new AsyncTask<Integer, Object, CarModel>() {
                @Override
                protected void onPostExecute(CarModel carModel) {
                    if (errorMassage != null) {
                        Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                        errorMassage = null;
                    }
                    if (carModel == null) return;
                    Uri uri = getActivity().getContentResolver().insert(CarRentConst.ContentProviderConstants.CONTENT_URI, CarRentConst.carModelToContentValues(carModel));
                    favoriteModels.add(carModel);
                    carsAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), getString(R.string.textAddModelFavorite) + " " + modelCode, Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                protected CarModel doInBackground(Integer... params) {
                    try {
                        return db_manager.getCarModel(params[0]);
                    } catch (Exception e) {
                        errorMassage = e.getMessage();
                        return null;

                    }
                }
            }.execute(modelCode);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    // remove model from favorite and update view
    void removeModelFromFavorite(final int modelCode) {
        try {

            /// downlaod car model details
            new AsyncTask<Integer, Object, CarModel>() {
                @Override
                protected void onPostExecute(CarModel carModel) {
                    if (errorMassage != null) {
                        Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                        errorMassage = null;
                    }
                    if (carModel == null) return;
                    int uri = getActivity().getContentResolver().delete(CarRentConst.ContentProviderConstants.CONTENT_URI,
                            CarRentConst.DataBaseConstants.MODEL_CODE + "=?", new String[]{new String(((Integer) carModel.getModelCode()).toString())});
                    if (uri < 0) return;
                    for (CarModel temp : favoriteModels)
                        if (carModel.getModelCode() == temp.getModelCode()) {
                            favoriteModels.remove(temp);
                            break;
                        }
                    carsAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), getString(R.string.textRemoveModelFavorite) + " " + modelCode, Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                protected CarModel doInBackground(Integer... params) {
                    try {
                        return db_manager.getCarModel(params[0]);
                    } catch (Exception e) {
                        errorMassage = e.getMessage();
                        return null;
                    }
                }
            }.execute(modelCode);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    // returns model code from line of the auto complete
    Integer getModelCode(String line) {
        return Integer.parseInt(line.substring(line.indexOf(":") + 2));
    }

    /// create cars adapter
    MyListAdapter<Car> getCarsAdapter(ArrayList<Car> cars) {
        return new MyListAdapter<Car>(getActivity(), cars) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null)
                    convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);
                final ImageButton favoriteImageButton = (ImageButton) convertView.findViewById(R.id.favoriteButton);


                final Car car = (Car) freeCarsListView.getItemAtPosition(position);
                carIdEditText.setText(((Integer) car.getCarID()).toString());

//                 get branch of car details
                new AsyncTask<Integer, Object, String>() {
                    @Override
                    protected void onPostExecute(String branchName) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        if (branchName != null)
                            branchNameEditText.setText(branchName);
                    }

                    @Override
                    protected String doInBackground(Integer... params) {
                        try {
                            return db_manager.getBranch(params[0]).getBranchName();
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return null;
                        }
                    }
                }.execute(car.getBranchID());
//                 get model of car details
                new AsyncTask<Integer, Object, String>() {
                    @Override
                    protected void onPostExecute(String modelNameAndCompany) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        if (modelNameAndCompany != null)
                            modelNameAndCompanyEditText.setText(modelNameAndCompany);
                    }

                    @Override
                    protected String doInBackground(Integer... params) {

                        CarModel carModel = null;
                        try {
                            carModel = db_manager.getCarModel(params[0]);
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return null;
                        }
                        return carModel.getModelName() + ", " + carModel.getCompany().name();
                    }
                }.execute(car.getModelCode());

                if (isModelFavorite(car.getModelCode()))
                    favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
                else
                    favoriteImageButton.setBackgroundResource(R.drawable.favorite_empty);

                favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isModelFavorite(car.getModelCode())) {
                            removeModelFromFavorite(car.getModelCode());
                            favoriteImageButton.setBackgroundResource(R.drawable.favorite_empty);
                        } else {
                            addModelToFavorite(car.getModelCode());
                            favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
                        }

                    }
                });
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String[] modelDetails = carModelsAutoCompleteTextView.getText().toString().split("\n");
        carModelsAutoCompleteTextView.setText(modelDetails[0]);
        modelCode = getModelCode(modelDetails[1]);

        // filter  cars list by model code
        new AsyncTask<Integer, Object, ArrayList<Car>>() {
            @Override
            protected void onPostExecute(ArrayList<Car> result) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                carsAdapter = getCarsAdapter(result);
                freeCarsListView.setAdapter(carsAdapter);
            }

            @Override
            protected ArrayList<Car> doInBackground(Integer... params) {
                try {
                    return db_manager.getFreeCars(params[0]);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return new ArrayList<Car>();
                }
            }
        }.execute(modelCode);
    }

    @Override
    public void onClick(View v) {

        /// open all models in first tap
        if (v == carModelsAutoCompleteTextView) {
            if (!carModelsAutoCompleteTextView.isPopupShowing())
                carModelsAutoCompleteTextView.showDropDown();
            else
                carModelsAutoCompleteTextView.dismissDropDown();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        /// open car details dialog
        Bundle args = new Bundle();
        args.putInt(CarRentConst.CarConst.CAR_ID, ((Car) freeCarsListView.getItemAtPosition(position)).getCarID());
        CarDetails myDialogFragment = new CarDetails();
        myDialogFragment.setArguments(args);
        myDialogFragment.show(getActivity().getFragmentManager(), "Car Details");
        return false;
    }
}
/*
 //    /// return the model name for the car row
//    String getModelName(int modelCode) {
//        for (CarModel carModel : carModels)
//            if (carModel.getModelCode() == modelCode)
//                return carModel.getModelName() + ", " + carModel.getCompany().name();
//        return "No Details";
//    }
//
//    /// return the branch name for the car row
//    String getBranchName(int branchID) {
//        for (Branch branch : branches)
//            if (branch.getBranchID() == branchID)
//                return branch.getBranchName();
//        return "No Details";
//    }
//
    /// gets the models list for the auto complete filter
//    ArrayList<String> getModelsStrings() {
//        ArrayList<String> carModelsStrings = new ArrayList<String>();
//        for (CarModel carModel : this.carModels)
//            carModelsStrings.add(carModel.getCompany().name() + ", " + carModel.getModelName() + "\n" + getString(R.string.textModelCode) + ": " + carModel.getModelCode());
//        return carModelsStrings;
//    }*/