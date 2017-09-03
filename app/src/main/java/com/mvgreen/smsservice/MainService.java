package com.mvgreen.smsservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service{

    private static final String LOG_TAG = "Service";
    private static MainService instance;
    private boolean isActive;

    static boolean isOnline() {
        return instance != null && getInstance().isActive;
    }

    private static MainService getInstance() {
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isActive = true;
        Status.toast(LOG_TAG, getString(R.string.service_started));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isActive = false;
        super.onDestroy();
    }
}
