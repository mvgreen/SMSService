package com.mvgreen.smsservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service {

    private static final String LOG_TAG = "Service";
    private static MainService instance;
    private boolean isActive;
    private RoutineThread routine;

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
        if (isActive) {
            Status.toast(LOG_TAG, getString(R.string.unexpected_start));
            stopSelf(startId);
            return super.onStartCommand(intent, flags, startId);
        }
        isActive = true;
        startForeground(1, new Notification());
        routine = new RoutineThread();
        routine.startRoutine();
        Status.toast(LOG_TAG, getString(R.string.service_started));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        routine.stopRoutine();
        isActive = false;
        Status.toast(LOG_TAG, "Поток успешно завершен");
        super.onDestroy();
    }
}
