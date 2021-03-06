package lugassi.wallach.client_android5778_2638_6575.controller.dialogs;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFinishOrder extends DialogFragment implements View.OnClickListener {

    DB_manager db_manager;
    Reservation reservation;
    private EditText mileageEditText;
    private CheckBox gasFilledCheckBox;
    private EditText gasEditText;
    private Button cancelButton;
    private Button okButton;
    private String errorMassage = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        errorMassage = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_finish_order, container, false);


        /// get reservation
        new AsyncTask<Integer, Object, Reservation>() {
            @Override
            protected void onPostExecute(Reservation result) {

                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                }
                reservation = result;
            }

            @Override
            protected Reservation doInBackground(Integer... params) {
                try {
                    return db_manager.getReservation(params[0]);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute(getArguments().getInt(CarRentConst.ReservationConst.RESERVATION_ID));


        mileageEditText = (EditText) view.findViewById(R.id.mileageEditText);
        gasFilledCheckBox = (CheckBox) view.findViewById(R.id.gasFilledCheckBox);
        gasEditText = (EditText) view.findViewById(R.id.gasEditText);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        okButton = (Button) view.findViewById(R.id.okButton);

        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        /// check box of the gas filled
        gasFilledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (gasFilledCheckBox.isChecked())
                    gasEditText.setVisibility(View.VISIBLE);
                else
                    gasEditText.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private boolean checkValues() {
        if (TextUtils.isEmpty(mileageEditText.getText().toString())) {
            mileageEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        if (gasFilledCheckBox.isChecked() && TextUtils.isEmpty(gasEditText.getText().toString())) {
            gasEditText.setError(getString(R.string.exceptionEmptyFileds));
            return false;
        }
        return true;
    }

    boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /// dialog action, finish or cancel
    @Override
    public void onClick(View v) {
        if (v == cancelButton) {
            dismiss();
        } else if (v == okButton && checkValues()) {

            int mileage = Integer.parseInt(mileageEditText.getText().toString());
            int gasCost = 0;
            if (gasFilledCheckBox.isChecked())
                gasCost = Integer.parseInt(gasEditText.getText().toString());


            /// closing reservation
            new AsyncTask<Integer, Object, String>() {
                @Override
                protected void onPostExecute(String result) {
                    if (tryParseDouble(result) && Double.parseDouble(result) >= 0)
                        Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.textCloseReservation) + " " + result, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    else
                        Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.exceptionFailedRequest) + "\n" + result, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    dismiss();
                }


                @Override
                protected String doInBackground(Integer... params) {

                    /// update reservation details and close it
                    reservation.setEndDate(Calendar.getInstance());
                    reservation.setOpen(false);
                    reservation.setFinishMileage(reservation.getBeginMileage() + params[0]);
                    reservation.setGasFilled(params[1]);
                    reservation.setGasFull(params[1] > 0);
                    return db_manager.closeReservation(CarRentConst.reservationToContentValues(reservation));
                }
            }.execute(mileage, gasCost);
        }
    }

}
