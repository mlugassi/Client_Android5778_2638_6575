package lugassi.wallach.client_android5778_2638_6575.controller.dialogs;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Reservation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationDetails extends DialogFragment {

    private String errorMassage = null;
    private DB_manager db_manager;
    private int reservationID;

    private TextView reservationIDTextView;
    private TextView carIDTextView;
    private TextView startDayTextView;
    private LinearLayout endLinearLayout;
    private TextView endDayTextView;
    private TextView beginMileageTextView;
    private LinearLayout finishMileageLinearLayout;
    private TextView finishMileageTextView;
    private LinearLayout gasLinearLayout;
    private TextView gasFilledTextView;
    private LinearLayout costLinearLayout;
    private TextView reservationCostTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        reservationID = getArguments().getInt(CarRentConst.ReservationConst.RESERVATION_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reservation_details, container, false);

        reservationIDTextView = (TextView) view.findViewById(R.id.reservationIDTextView);
        carIDTextView = (TextView) view.findViewById(R.id.carIDTextView);
        startDayTextView = (TextView) view.findViewById(R.id.startDayTextView);
        endDayTextView = (TextView) view.findViewById(R.id.endDayTextView);
        beginMileageTextView = (TextView) view.findViewById(R.id.beginMileageTextView);
        finishMileageTextView = (TextView) view.findViewById(R.id.finishMileageTextView);
        gasFilledTextView = (TextView) view.findViewById(R.id.gasFilledTextView);
        reservationCostTextView = (TextView) view.findViewById(R.id.reservationCostTextView);
        endLinearLayout = (LinearLayout)view.findViewById( R.id.endLinearLayout );
        finishMileageLinearLayout = (LinearLayout)view.findViewById( R.id.finishMileageLinearLayout );
        gasLinearLayout = (LinearLayout)view.findViewById( R.id.gasLinearLayout );
        costLinearLayout = (LinearLayout)view.findViewById( R.id.costLinearLayout );

        new AsyncTask<Object, Object, Reservation>() {
            @Override
            protected void onPostExecute(Reservation reservation) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                    dismiss();
                }
                reservationIDTextView.setText(((Integer) reservation.getReservationID()).toString());
                carIDTextView.setText(((Integer) reservation.getCarID()).toString());
                startDayTextView.setText(reservation.getStartDateString());
                beginMileageTextView.setText(((Long) reservation.getBeginMileage()).toString());
                if (!reservation.isOpen()) {
                    endDayTextView.setText(reservation.getEndDateString());
                    finishMileageTextView.setText(((Long) reservation.getFinishMileage()).toString());
                    gasFilledTextView.setText(((Integer) reservation.getGasFilled()).toString());
                    reservationCostTextView.setText(((Double) reservation.getReservationCost()).toString());

                    endLinearLayout.setVisibility(View.VISIBLE);
                    finishMileageLinearLayout.setVisibility(View.VISIBLE);
                    gasLinearLayout.setVisibility(View.VISIBLE);
                    costLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Reservation doInBackground(Object... params) {
                try {
                    return db_manager.getReservation(reservationID);
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();


        return view;
    }

}
