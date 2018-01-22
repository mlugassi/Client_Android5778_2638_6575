package lugassi.wallach.client_android5778_2638_6575.controller.dialogs;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarModelDetails extends DialogFragment {

    private TextView modelCodeTextView;
    private TextView nameTextView;
    private TextView companyTextView;
    private TextView engineTextView;
    private TextView seatsTextView;
    private TextView typeTextView;
    private TextView gasTankTextView;
    DB_manager db_manager;
    private int modelCode;
    private String errorMassage = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        modelCode = getArguments().getInt(CarRentConst.CarModelConst.MODEL_CODE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_car_model_details, container, false);
        modelCodeTextView = (TextView) view.findViewById(R.id.modelCodeTextView);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        companyTextView = (TextView) view.findViewById(R.id.companyTextView);
        engineTextView = (TextView) view.findViewById(R.id.engineTextView);
        seatsTextView = (TextView) view.findViewById(R.id.seatsTextView);
        typeTextView = (TextView) view.findViewById(R.id.typeTextView);
        gasTankTextView = (TextView) view.findViewById(R.id.gasTankTextView);

        new AsyncTask<Object, Object, CarModel>() {
            @Override
            protected void onPostExecute(CarModel carModel) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                    return;
                }
                modelCodeTextView.setText(((Integer) carModel.getModelCode()).toString());
                nameTextView.setText(carModel.getModelName());
                companyTextView.setText(carModel.getCompany().name());
                engineTextView.setText(carModel.getEngineCapacity().name());
                seatsTextView.setText(((Integer) carModel.getSeats()).toString());
                typeTextView.setText(carModel.getCarType().name());
                gasTankTextView.setText(((Integer) carModel.getMaxGasTank()).toString());
            }

            @Override
            protected CarModel doInBackground(Object... params) {
                try {
                    return db_manager.getCarModel(modelCode);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();


        return view;
    }

}
