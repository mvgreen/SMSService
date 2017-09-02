package com.mvgreen.smsservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service{

    private static MainService instance;
    private boolean isActive;

    private MainService() {
        isActive = false;
    }

    public static boolean isOnline() {
        return getInstance().isActive;
    }

    public static MainService getInstance() {
        if (instance == null)
            instance = new MainService();
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void stop() {

    }

    public static void start() {

    }
}
