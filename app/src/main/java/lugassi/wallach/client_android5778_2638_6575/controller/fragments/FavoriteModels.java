package lugassi.wallach.client_android5778_2638_6575.controller.fragments;


import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.controller.dialogs.CarModelDetails;
import lugassi.wallach.client_android5778_2638_6575.model.MyListAdapter;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.CarModel;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.CarType;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.Company;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Enums.EngineCapacity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteModels extends Fragment implements AdapterView.OnItemLongClickListener {

    private ArrayList<CarModel> carModels;
    private MyListAdapter carModelsAdapter;
    private ListView favoriteCarModelListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carModels = new ArrayList<>();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_favorite_models_fragment));
        uploadCarModels();
        carModelsAdapter = new MyListAdapter(getActivity(), carModels) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null)
                    convertView = View.inflate(getActivity(), R.layout.car_model_list_view, null);

                TextView carModelTextView = (TextView) convertView.findViewById(R.id.modelCodeEditText);
                TextView nameAndCompanyEditText = (TextView) convertView.findViewById(R.id.nameAndCompanyEditText);
                final ImageButton favoriteImageButton = (ImageButton) convertView.findViewById(R.id.favoriteButton);

                final CarModel carModel = (CarModel) favoriteCarModelListView.getItemAtPosition(position);
                carModelTextView.setText(((Integer) carModel.getModelCode()).toString());
                nameAndCompanyEditText.setText(carModel.getModelName() + ", " + carModel.getCompany().name());
                if (isModelFavorite(carModel.getModelCode()))
                    favoriteImageButton.setBackgroundResource(R.drawable.favorite_full);
                else
                    favoriteImageButton.setBackgroundResource(R.drawable.favorite_empty);
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
                return convertView;
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_models, container, false);
        favoriteCarModelListView = (ListView) view.findViewById(R.id.favoriteCarModelListView);
        favoriteCarModelListView.setAdapter(carModelsAdapter);

        favoriteCarModelListView.setOnItemLongClickListener(this);
        return view;
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

    boolean isModelFavorite(int modelCode) {

        for (CarModel carModel : carModels)
            if (carModel.getModelCode() == modelCode)
                return true;
        return false;
    }

    void addModelToFavorite(CarModel carModel) {
        try {
            Uri uri = getActivity().getContentResolver().insert(CarRentConst.ContentProviderConstants.CONTENT_URI, CarRentConst.carModelToContentValues(carModel));
            carModels.add(carModel);
            carModelsAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.textAddModelFavorite) + " " + carModel.getModelCode(), Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    void removeModelFromFavorite(CarModel carModel) {
        try {
            int uri = getActivity().getContentResolver().delete(CarRentConst.ContentProviderConstants.CONTENT_URI,
                    CarRentConst.DataBaseConstants.MODEL_CODE + "=?", new String[]{new String(((Integer) carModel.getModelCode()).toString())});
            if (uri == 0) return;
            for (CarModel temp : carModels)
                if (carModel.getModelCode() == temp.getModelCode()) {
                    carModels.remove(temp);
                    break;
                }
            carModelsAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.textRemoveModelFavorite) + " " + carModel.getModelCode(), Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // open car model dialog details
        Bundle args = new Bundle();
        args.putInt(CarRentConst.CarModelConst.MODEL_CODE, ((CarModel) favoriteCarModelListView.getItemAtPosition(position)).getModelCode());
        CarModelDetails myDialogFragment = new CarModelDetails();
        myDialogFragment.setArguments(args);
        myDialogFragment.show(getActivity().getFragmentManager(), "Car Model Details");

        return false;
    }
}
