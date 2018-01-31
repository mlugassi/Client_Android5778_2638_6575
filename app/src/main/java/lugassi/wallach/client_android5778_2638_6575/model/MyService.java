package lugassi.wallach.client_android5778_2638_6575.model;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import lugassi.wallach.client_android5778_2638_6575.R;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;

public class MyService extends Service {

    private DB_manager db_manager;
    private int interval = 10000;
    boolean isRun;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db_manager = DBManagerFactory.getManager();
        isRun = false;
    }

    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        isRun = true;

        Thread reservationsChanges = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(interval);
                        if (db_manager.detectCarsChanges()) { // detect if car resevations change in last 10 sec
                            Intent intent = new Intent();
                            intent.setAction(CarRentConst.MyIntentFilter.RESERVATIONS_CHANGED);
                            sendBroadcast(intent);
                        }
                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        Thread carsChanges = new Thread() {
            @Override
            public void run() {
                int lastSize = 0;
                try {
                    // get last count of cars
                    lastSize = db_manager.getFreeCars().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isRun) {
                    try {
                        Thread.sleep(interval);
                        // get current count of cars
                        int currentSize = db_manager.getFreeCars().size();
                        if (lastSize != currentSize) { // if there is change send intent
                            lastSize = currentSize; // update
                            Intent intent = new Intent();
                            intent.setAction(CarRentConst.MyIntentFilter.CARS_CHANGED);
                            sendBroadcast(intent);
                        }
                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        Thread branchesChanges = new Thread() {
            @Override
            public void run() {
                int lastSize = 0;
                try {
                    // get last count of branches
                    lastSize = db_manager.getBranches().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isRun) {
                    try {
                        Thread.sleep(interval);
                        // get current count of branches
                        int currentSize = db_manager.getBranches().size();
                        if (lastSize != currentSize) { // if there is change send intent
                            lastSize = currentSize; // update
                            Intent intent = new Intent();
                            intent.setAction(CarRentConst.MyIntentFilter.BRANCHES_CHANGED);
                            sendBroadcast(intent);
                        }
                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        branchesChanges.start();
        carsChanges.start();
        reservationsChanges.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        /// make notifications when new branch added
        Thread reservationsChange = new Thread() {
            @Override
            public void run() {
                isRun = true;
                int lastSize = 0;
                try {
                    lastSize = db_manager.getBranches().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isRun) {
                    try {
                        Thread.sleep(interval);
                        int currentSize = db_manager.getBranches().size();
                        if (lastSize != currentSize) {
                            lastSize = currentSize;
                            Notification.Builder nBuilder = new Notification.Builder(getBaseContext());
                            nBuilder.setSmallIcon(R.drawable.icon);
                            nBuilder.setContentTitle(getString(R.string.notification_newBranch));
                            nBuilder.setContentText(getString(R.string.notification_newBranchContent));
                            Notification notification = nBuilder.build();

                            startForeground(CarRentConst.MyService.NEW_BRANCH_ADDED, notification);
                        }
                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        reservationsChange.start();
        return null;
    }
}