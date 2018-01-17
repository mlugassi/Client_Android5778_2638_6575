package lugassi.wallach.client_android5778_2638_6575.controller;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_manager = DBManagerFactory.getManager();
        try {
            branch = new AsyncTask<Object, Object, Branch>() {
                @Override
                protected void onPostExecute(Branch branch) {
                    super.onPostExecute(branch);
                }

                @Override
                protected Branch doInBackground(Object... params) {
                    return branch = db_manager.getBranch(getArguments().getInt(CarRentConst.BranchConst.BRANCH_ID));
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_branch_details, container, false);
        ((TextView) view.findViewById(R.id.branchIdTextView)).setText(((Integer) branch.getBranchID()).toString());
        ((TextView) view.findViewById(R.id.nameTextView)).setText(branch.getBranchName());
        ((TextView) view.findViewById(R.id.cityTextView)).setText(branch.getCity());
        ((TextView) view.findViewById(R.id.addressTextView)).setText(branch.getAddress());
        ((TextView) view.findViewById(R.id.parkingSpaceTextView)).setText(((Integer) branch.getMaxParkingSpace()).toString());
        ((TextView) view.findViewById(R.id.actualParkingSpaceTextView)).setText(((Integer) branch.getActualParkingSpace()).toString());


        return view;
    }


}
