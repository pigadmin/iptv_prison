package product.prison.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import product.prison.utils.Logs;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Logs.e(getClass().getSimpleName() + "onCreate");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Logs.e(getClass().getSimpleName() + "onDestroy");

    }

}
