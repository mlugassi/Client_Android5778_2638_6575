package lugassi.wallach.client_android5778_2638_6575.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import lugassi.wallach.client_android5778_2638_6575.model.entities.Branch;

/**
 * Created by Michael on 10/12/2017.
 */

public class MyListAdapter<T> extends BaseAdapter implements Filterable {

    private final Context mContext;
    private List<T> mData;
    private List<T> fData;

    public MyListAdapter(final Context context, final ArrayList<T> mData) {
        this.mData = mData;
        this.mContext = context;
        fData = mData;
    }

    public void setData(ArrayList<T> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return fData.size();
    }

    @Override
    public Object getItem(int position) {
        return fData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if (constraint == null || constraint.length() == 0) {
                    results.values = mData;
                    results.count = mData.size();
                } else {
                    ArrayList<T> filterResultsData = new ArrayList<T>();

                    for (T data : mData) {
                        Branch branch = (Branch) data;
                        if (branch.getAddress().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filteredData) {
                fData = (List<T>) filteredData.values;
                notifyDataSetChanged();
            }
        };
    }
}
