package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.MyService;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;

public class MainNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_main_title));
        setSupportActionBar(toolbar);
        startService(new Intent(this, MyService.class));
        customerID = getIntent().getIntExtra(CarRentConst.CustomerConst.CUSTOMER_ID, -1);
        String userName = getIntent().getStringExtra(CarRentConst.UserConst.USER_NAME);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View header = nav_view.getHeaderView(0);
        ((TextView) header.findViewById(R.id.userTextView)).setText(userName);
        setHomeFragment();

        bindService(new Intent(this, MyService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setHomeFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AddCustomer.class);
            intent.putExtra(CarRentConst.CustomerConst.CUSTOMER_ID, customerID);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args = new Bundle();
        Fragment fragment = new Fragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (id == R.id.nav_make_order) {
            fragment = new MakeOrder();

        } else if (id == R.id.nav_free_cars) {
            fragment = new FreeCars();

        } else if (id == R.id.nav_reservations) {
            fragment = new Reservations();

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, AddCustomer.class);
            intent.putExtra(CarRentConst.CustomerConst.CUSTOMER_ID, customerID);
            this.startActivity(intent);

        } else if (id == R.id.nav_contact) {
            fragment = new AboutUs();

        } else if (id == R.id.nav_favorite) {
            fragment = new FavoriteModels();

        } else if (id == R.id.nav_exit) {
            showEnsureDialog(R.id.nav_exit);
        } else if (id == R.id.nav_logout) {
            showEnsureDialog(R.id.nav_logout);
        }
        args.putInt(CarRentConst.CustomerConst.CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setHomeFragment() {
        Home fragment = new Home();
        Bundle args = new Bundle();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        args.putInt(CarRentConst.CustomerConst.CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    void showEnsureDialog(final int action) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (action == R.id.nav_logout)
            builder.setMessage(getString(R.string.textDialogMessageLogout));
        else if (action == R.id.nav_exit)
            builder.setMessage(getString(R.string.textDialogMessageExit));

        builder.setPositiveButton(getString(R.string.buttonYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action == R.id.nav_logout) {
                    Login.setDefaults(CarRentConst.UserConst.USER_NAME, "", MainNavigation.this);
                    Login.setDefaults(CarRentConst.UserConst.PASSWORD, "", MainNavigation.this);

                    Intent intent = new Intent(MainNavigation.this, Login.class);
                    startActivity(intent);
                    finish();
                } else if (action == R.id.nav_exit)
                    finishAffinity();
            }
        });
        builder.setNegativeButton(getString(R.string.buttonNo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
