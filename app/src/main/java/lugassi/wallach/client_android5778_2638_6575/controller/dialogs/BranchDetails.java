package lugassi.wallach.client_android5778_2638_6575.controller.dialogs;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchDetails extends DialogFragment {

    DB_manager db_manager;
    Branch branch;
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
        final View view = inflater.inflate(R.layout.dialog_branch_details, container, false);

        /// get branch details
        new AsyncTask<Object, Object, Branch>() {
            @Override
            protected void onPostExecute(Branch result) {
                if (errorMassage != null) {
                    Toast.makeText(getActivity(), errorMassage, Toast.LENGTH_LONG).show();
                    errorMassage = null;
                    return;
                }
                branch = result;

                // upload the details
                ((TextView) view.findViewById(R.id.branchIdTextView)).setText(((Integer) branch.getBranchID()).toString());
                ((TextView) view.findViewById(R.id.nameTextView)).setText(branch.getBranchName());
                ((TextView) view.findViewById(R.id.cityTextView)).setText(branch.getCity());
                ((TextView) view.findViewById(R.id.addressTextView)).setText(branch.getAddress());

                /// navigate to the address
                ((TextView) view.findViewById(R.id.addressTextView)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=an+" + branch.getAddress() + "+" + branch.getCity()));
                        startActivity(intent);
                    }
                });
                TextView parkingSpaceTextView = (TextView) view.findViewById(R.id.parkingSpaceTextView);
                ProgressBar parkingSpaceProgressBar = (ProgressBar) view.findViewById(R.id.parkingSpaceProgressBar);
                parkingSpaceTextView.setText(((Integer) branch.getActualParkingSpace()).toString() + "/" + ((Integer) branch.getMaxParkingSpace()).toString());
                parkingSpaceProgressBar.setProgress(branch.getActualParkingSpace(), true);
            }

            @Override
            protected Branch doInBackground(Object... params) {
                try {
                    return db_manager.getBranch(getArguments().getInt(CarRentConst.BranchConst.BRANCH_ID));
                } catch (Exception e) {
                    errorMassage = e.getMessage();
                    return null;
                }
            }
        }.execute();

        return view;
    }


}
