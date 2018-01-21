package lugassi.wallach.client_android5778_2638_6575.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;

public class MyService extends Service {

    private DB_manager db_manager;
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
    public int onStartCommand(final Intent intent, int flags, int startId) {

        isRun = true;

        Thread reservationsChange = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(10000);
                        if (db_manager.detectCarsChanges()) {
                            Intent intent = new Intent();
                            intent.setAction(CarRentConst.MyIntentFilter.CARS_CHANGED);
                            sendBroadcast(intent);
                        } else
                            isRun = true;

                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        Thread carChange = new Thread() {
            @Override
            public void run() {
                int lastSize = 0;
                try {
                    lastSize = db_manager.getFreeCars().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isRun) {
                    try {
                        Thread.sleep(10000);
                        int cuurentSize = db_manager.getFreeCars().size();
                        if (lastSize != cuurentSize) {
                            lastSize = cuurentSize;
                            Intent intent = new Intent();
                            intent.setAction(CarRentConst.MyIntentFilter.CARS_CHANGED);
                            sendBroadcast(intent);
                        } else
                            isRun = true;

                    } catch (Exception e) {
                        Log.w("MyService", e.getMessage());
                    }
                }
            }
        };
        carChange.start();
        reservationsChange.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
