package lugassi.wallach.client_android5778_2638_6575.controller.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lugassi.wallach.client_android5778_2638_6575.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment implements View.OnClickListener {
    TextView phoneTextView;
    TextView mailTextView;
    TextView webTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_about_us_fragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        mailTextView = (TextView) view.findViewById(R.id.mailTextView);
        webTextView = (TextView) view.findViewById(R.id.webTextView);

        // phoneTextView.setClickable(true);
        phoneTextView.setOnClickListener(this);
        mailTextView.setOnClickListener(this);
        webTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == phoneTextView) {

            /// open dailer
            String phone = phoneTextView.getText().toString();
            String uri = "tel:" + phone.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } else if (v == mailTextView) {

            /// open mail
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTextView.getText().toString()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Your App");
            startActivity(Intent.createChooser(intent, ""));
        } else {

            // open browser
            String url = "http://" + webTextView.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
