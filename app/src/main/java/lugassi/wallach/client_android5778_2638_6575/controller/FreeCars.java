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
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.EngineCapacity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeCars extends Fragment implements AdapterView.OnItemClickListener {

    private DB_manager db_manager;
    private ListView freeCarsListView;
    private AutoCompleteTextView carModelsAutoCompleteTextView;
    private Integer modelCode;
    private MyListAdapter<Car> carsAdapter;
    private ArrayList<CarModel> favoriteModels;
    private MyReceiver carChangedReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!carModelsAutoCompleteTextView.getText().toString().equals(""))
                new AsyncTask<Integer, Object, ArrayList<Car>>() {
                    @Override
                    protected void onPostExecute(ArrayList<Car> cars) {
                        ArrayAdapter<Car> carsAdapter = new ArrayAdapter<Car>(getActivity(), R.layout.car_list_view, cars) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                if (convertView == null)
                                    convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                                TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                                final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                                final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                                final Car car = (Car) freeCarsListView.getItemAtPosition(position);
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
                        freeCarsListView.setAdapter(carsAdapter);
                    }

                    @Override
                    protected ArrayList<Car> doInBackground(Integer... params) {
                        return db_manager.getFreeCars(params[0]);
                    }
                }.execute(modelCode);
            else
                new AsyncTask<Integer, Object, ArrayList<Car>>() {
                    @Override
                    protected void onPostExecute(ArrayList<Car> cars) {
                        ArrayAdapter<Car> carsAdapter = new ArrayAdapter<Car>(getActivity(), R.layout.car_list_view, cars) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                if (convertView == null)
                                    convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                                TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                                final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                                final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                                final Car car = (Car) freeCarsListView.getItemAtPosition(position);
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
                        freeCarsListView.setAdapter(carsAdapter);
                        freeCarsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle args = new Bundle();
                                args.putInt(CarRentConst.CarConst.CAR_ID, ((Car) freeCarsListView.getItemAtPosition(position)).getCarID());
                                CarDetails myDialogFragment = new CarDetails();
                                myDialogFragment.setArguments(args);
                                myDialogFragment.show(getActivity().getFragmentManager(), "Car Details");
                                return false;
                            }
                        });
                    }

                    @Override
                    protected ArrayList<Car> doInBackground(Integer... params) {
                        return db_manager.getFreeCars();
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
        uploadCarModels();
        getActivity().registerReceiver(carChangedReceiver, new IntentFilter(CarRentConst.MyIntentFilter.RESERVATIONS_CHANGED));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_free_cars, container, false);
        freeCarsListView = (ListView) view.findViewById(R.id.freeCarListView);
        carModelsAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.carModelsAutoCompleteTextView);

        new AsyncTask<Integer, Object, ArrayList<String>>() {
            @Override
            protected void onPostExecute(ArrayList<String> carModels) {
                ArrayAdapter<String> carModelsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, carModels);
                carModelsAutoCompleteTextView.setAdapter(carModelsAdapter);
                carModelsAutoCompleteTextView.setThreshold(1);
                carModelsAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!carModelsAutoCompleteTextView.isPopupShowing())
                            carModelsAutoCompleteTextView.showDropDown();
                        else
                            carModelsAutoCompleteTextView.dismissDropDown();
                    }
                });
            }

            @Override
            protected ArrayList<String> doInBackground(Integer... params) {
                ArrayList<String> carModels = new ArrayList<String>();
                for (CarModel carModel : db_manager.getCarModels())
                    carModels.add(carModel.getCompany().name() + ", " + carModel.getModelName() + "\n" + getString(R.string.textModelCode) + ": " + carModel.getModelCode());
                return carModels;
            }
        }.execute();

        new AsyncTask<Integer, Object, ArrayList<Car>>() {
            @Override
            protected void onPostExecute(ArrayList<Car> cars) {
                carsAdapter = new MyListAdapter<Car>(getActivity(), cars) {
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

                        if (isModelFavorite(car.getModelCode()))
                            favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
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
                freeCarsListView.setAdapter(carsAdapter);
                freeCarsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        Bundle args = new Bundle();
                        args.putInt(CarRentConst.CarConst.CAR_ID, ((Car) freeCarsListView.getItemAtPosition(position)).getCarID());
                        CarDetails myDialogFragment = new CarDetails();
                        myDialogFragment.setArguments(args);
                        myDialogFragment.show(getActivity().getFragmentManager(), "Car Details");
                        return false;
                    }
                });
            }

            @Override
            protected ArrayList<Car> doInBackground(Integer... params) {
                return db_manager.getFreeCars();
            }
        }.execute();

        carModelsAutoCompleteTextView.setOnItemClickListener(this);
        carModelsAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modelCode = -1;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    void uploadCarModels() {
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

    boolean isModelFavorite(int modelCode) {

        for (CarModel carModel : favoriteModels)
            if (carModel.getModelCode() == modelCode)
                return true;
        return false;
    }

    void addModelToFavorite(final int modelCode) {
        try {
            new AsyncTask<Integer, Object, CarModel>() {
                @Override
                protected void onPostExecute(CarModel carModel) {
                    Uri uri = getActivity().getContentResolver().insert(CarRentConst.ContentProviderConstants.CONTENT_URI, CarRentConst.carModelToContentValues(carModel));
                    favoriteModels.add(carModel);
                    carsAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), getString(R.string.textAddModelFavorite) + " " + modelCode, Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                protected CarModel doInBackground(Integer... params) {
                    return db_manager.getCarModel(params[0]);
                }
            }.execute(modelCode);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    void removeModelFromFavorite(final int modelCode) {
        try {
            new AsyncTask<Integer, Object, CarModel>() {
                @Override
                protected void onPostExecute(CarModel carModel) {
                    int uri = getActivity().getContentResolver().delete(CarRentConst.ContentProviderConstants.CONTENT_URI,
                            CarRentConst.DataBaseConstants.MODEL_CODE + "=?", new String[]{new String(((Integer) carModel.getModelCode()).toString())});
                    if (uri == 0) return;
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
                    return db_manager.getCarModel(params[0]);
                }
            }.execute(modelCode);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    Integer getModelCode(String line) {
        return Integer.parseInt(line.substring(line.indexOf(":") + 2));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String[] modelDetails = carModelsAutoCompleteTextView.getText().toString().split("\n");
        carModelsAutoCompleteTextView.setText(modelDetails[0]);
        Integer modelCode = getModelCode(modelDetails[1]);
        new AsyncTask<Integer, Object, ArrayList<Car>>() {
            @Override
            protected void onPostExecute(ArrayList<Car> cars) {
                carsAdapter = new MyListAdapter<Car>(getActivity(), cars) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        if (convertView == null)
                            convertView = View.inflate(getActivity(), R.layout.car_list_view, null);

                        TextView carIdEditText = (TextView) convertView.findViewById(R.id.carIdEditText);
                        final TextView modelNameAndCompanyEditText = (TextView) convertView.findViewById(R.id.modelNameAndCompanyEditText);
                        final TextView branchNameEditText = (TextView) convertView.findViewById(R.id.branchNameEditText);


                        final Car car = (Car) freeCarsListView.getItemAtPosition(position);
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
                freeCarsListView.setAdapter(carsAdapter);
            }

            @Override
            protected ArrayList<Car> doInBackground(Integer... params) {
                return db_manager.getFreeCars(params[0]);
            }
        }.execute(modelCode);
    }

}
