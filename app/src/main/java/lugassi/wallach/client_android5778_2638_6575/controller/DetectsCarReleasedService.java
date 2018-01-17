package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.lang.Thread;

import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;

public class DetectsCarReleasedService extends Service {

    private DB_manager db_manager;
    boolean isRun;

    public DetectsCarReleasedService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db_manager = DBManagerFactory.getManager();
        isRun = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRun = true;

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(10000);
                        if (db_manager.detectCarsChanges()) {
                            Intent intent = new Intent();
                            intent.setAction("lugassi.wallach.RESERVATIONS_CHANGED");
                            sendBroadcast(intent);
                        }
                        else
                            isRun = true;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
