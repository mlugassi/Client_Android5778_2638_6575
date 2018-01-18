package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeCars extends Fragment implements AdapterView.OnItemClickListener {

    private DB_manager db_manager;
    private ListView freeCarsListView;
    private AutoCompleteTextView carModelsAutoCompleteTextView;
    private Integer modelCode;
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

                        Car car = (Car) freeCarsListView.getItemAtPosition(position);
                        new AsyncTask<Integer, Object, CarModel>() {
                            @Override
                            protected void onPostExecute(CarModel carModel) {
                                Uri uri = getActivity().getContentResolver().insert(CarRentConst.ContentProviderConstants.CONTENT_URI, CarRentConst.carModelToContentValues(carModel));
                                Toast.makeText(getActivity(), "New record inserted", Toast.LENGTH_LONG)
                                        .show();
                            }

                            @Override
                            protected CarModel doInBackground(Integer... params) {
                                return db_manager.getCarModel(params[0]);
                            }
                        }.execute(car.getModelCode());
//                        Bundle args = new Bundle();
//                        args.putInt(CarRentConst.CarConst.CAR_ID, ((Car) freeCarsListView.getItemAtPosition(position)).getCarID());
//                        CarDetails myDialogFragment = new CarDetails();
//                        myDialogFragment.setArguments(args);
//                        myDialogFragment.show(getActivity().getFragmentManager(), "Car Details");
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
    }

}
