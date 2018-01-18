package lugassi.wallach.client_android5778_2638_6575.controller;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.EngineCapacity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteModels extends Fragment {

    private ArrayList<CarModel> carModels;
    private MyListAdapter carModelsAdapter;
    private ListView favoriteCarModelListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carModels = new ArrayList<>();
        uploadCarModels();
        carModelsAdapter = new MyListAdapter(getActivity(), carModels) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null)
                    convertView = View.inflate(getActivity(), R.layout.car_model_list_view, null);

                TextView carModelTextView = (TextView) convertView.findViewById(R.id.modelCodeEditText);
                TextView nameAndCompanyEditText = (TextView) convertView.findViewById(R.id.nameAndCompanyEditText);

                CarModel carModel = (CarModel) favoriteCarModelListView.getItemAtPosition(position);
                carModelTextView.setText(((Integer) carModel.getModelCode()).toString());
                nameAndCompanyEditText.setText(carModel.getModelName() + ", " + carModel.getCompany().name());

                return convertView;
            }
        };

    }

    void uploadCarModels() {
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
                carModels.add(carModel);
            } while (cursor.moveToNext());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_models, container, false);
        favoriteCarModelListView = (ListView) view.findViewById(R.id.favoriteCarModelListView);
        favoriteCarModelListView.setAdapter(carModelsAdapter);
        return view;
    }

}
