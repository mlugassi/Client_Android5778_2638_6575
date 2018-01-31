package lugassi.wallach.client_android5778_2638_6575.controller.dialogs;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarDetails extends DialogFragment {


    DB_manager db_manager;
    Car car;
    private String errorMassage = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_car_details, container, false);


        // get car details
        new AsyncTask<Integer, Object, Car>() {
            @Override
            protected void onPostExecute(Car result) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                    return;
                }
                car = result;

                // get branch details
                new AsyncTask<Object, Object, String>() {
                    @Override
                    protected void onPostExecute(String branchName) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        if (branchName == null) return;
                        ((TextView) view.findViewById(R.id.branchNameTextView)).setText(branchName);
                        ((TextView) view.findViewById(R.id.branchNameTextView)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle args = new Bundle();
                                args.putInt(CarRentConst.BranchConst.BRANCH_ID, car.getBranchID());
                                BranchDetails myDialogFragment = new BranchDetails();
                                myDialogFragment.setArguments(args);
                                myDialogFragment.show(getActivity().getFragmentManager(), "Branch Details");
                            }
                        });
                    }

                    @Override
                    protected String doInBackground(Object... params) {
                        try {
                            return db_manager.getBranch(car.getBranchID()).getBranchName();
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return null;
                        }
                    }
                }.execute();

                // get model details
                new AsyncTask<Object, Object, String>() {
                    @Override
                    protected void onPostExecute(String modelName) {
                        if (errorMassage != null) {
                            Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                            errorMassage = null;
                        }
                        if (modelName != null)
                            ((TextView) view.findViewById(R.id.modelNameTextView)).setText(modelName);
                    }

                    @Override
                    protected String doInBackground(Object... params) {
                        try {
                            return db_manager.getCarModel(car.getModelCode()).getModelName();
                        } catch (Exception e) {
                            errorMassage = e.getMessage();
                            return null;
                        }
                    }
                }.execute();


                /// upload car details
                ((TextView) view.findViewById(R.id.carIdTextView)).setText(((Integer) car.getCarID()).toString());
                ((TextView) view.findViewById(R.id.mileageTextView)).setText(((Long) car.getMileage()).toString());
                ((TextView) view.findViewById(R.id.reservationsTextView)).setText(((Integer) car.getReservations()).toString());
            }

            @Override
            protected Car doInBackground(Integer... params) {
                try {
                    return db_manager.getCar(params[0]);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute(getArguments().getInt(CarRentConst.CarConst.CAR_ID));

        return view;
    }

}
