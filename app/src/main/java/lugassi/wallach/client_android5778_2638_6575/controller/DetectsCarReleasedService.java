package lugassi.wallach.client_android5778_2638_6575.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import lugassi.wallach.client_android5778_2638_6575.model.backend.DBManagerFactory;
import lugassi.wallach.client_android5778_2638_6575.model.backend.DB_manager;
import lugassi.wallach.client_android5778_2638_6575.model.entities.Car;

public class DetectsCarReleasedService extends Service {

    private DB_manager db_manager;

    public DetectsCarReleasedService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db_manager = DBManagerFactory.getManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    if (db_manager.detectCarsChanges())
                        db_manager.getCarModels();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
