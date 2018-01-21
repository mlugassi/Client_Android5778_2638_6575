package lugassi.wallach.client_android5778_2638_6575.controller;


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_finish_order, container, false);

        new AsyncTask<Integer, Object, Reservation>() {
            @Override
            protected void onPostExecute(Reservation result) {
                reservation = result;
            }

            @Override
            protected Reservation doInBackground(Integer... params) {
                try {
                    return db_manager.getReservation(params[0]);
                } catch (Exception e) {
                    Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    @Override
    public void onClick(View v) {
        if (v == cancelButton) {
            dismiss();
        } else if (v == okButton && checkValues()) {

            int mileage = Integer.parseInt(mileageEditText.getText().toString());
            int gasCost = 0;
            if (gasFilledCheckBox.isChecked())
                gasCost = Integer.parseInt(gasEditText.getText().toString());

            new AsyncTask<Integer, Object, Long>() {
                @Override
                protected void onPostExecute(Long o) {
                    if (o >= 0)
                        Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.textCloseReservation) + " " + o, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    else
                        Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.exceptionFailedRequest), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    dismiss();
                }


                @Override
                protected Long doInBackground(Integer... params) {
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
