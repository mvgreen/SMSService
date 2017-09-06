package com.mvgreen.smsservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.mvgreen.smsservice.MainActivity.*;

public class RoutineThread extends Thread {

    private static final String LOG_TAG = "Routine";
    private boolean active;
    private static RoutineThread instance;

    @Override
    public void run() {
        instance = this;
        long workTime = 0;
        while (true) {
            timeout(workTime);
            synchronized (this) {
                if (!active)
                    return;
            }
            workTime = routine();
            MainActivity.getInstance().updateInterface();
        }
    }

    private void timeout(long workTime) {
        Status.toast(LOG_TAG, "Время работы: " + (workTime / 1000) + " секунд");
        try {
            TimeUnit.SECONDS.sleep(5 - (workTime / 1000));
        } catch (InterruptedException e) {
            active = false;
            stopRoutine();
            e.printStackTrace();
        }
    }

    private long routine() {
        long time = System.currentTimeMillis();

        SharedPreferences prefs = MainActivity.getInstance().getPreferences(Context.MODE_PRIVATE);

         final ArrayList<Record> records = Data.load();

         if (records.isEmpty()) {
            Status.toast(LOG_TAG, "Новых сообщений нет");
            return System.currentTimeMillis() - time;
        }

        Status.toast(LOG_TAG,"Новых сообщений: " + records.size());

        for (Record r : records)
            SMSModule.sendSafe(r);

        int newExpected = records.get(records.size() - 1).id + 1;

        // TODO продолжать работу, ориентируясь на данные сохраненные в оперативке
        if (!prefs.edit().putString(EXPECTED_ID, String.valueOf(newExpected)).commit())
            Status.toast(LOG_TAG, "Новый ожидаемый ID не сохранен");
        else
            Status.toast(LOG_TAG, "Новый ожидаемый ID: " + newExpected);

        if (!prefs.edit().putInt("SMS Count", prefs.getInt("SMS Count", 0) + records.size()).commit())
            Status.toast(LOG_TAG, "Не удалось сохранить статистику");

        return System.currentTimeMillis() - time;
    }

    void stopRoutine() {
        synchronized (this) {
            active = false;
        }
        try {
            instance.join();
        } catch (InterruptedException e) {
            Status.toast(LOG_TAG, "Ошибка закрытия потока!");
        }
    }

    void startRoutine() {
        synchronized (this) {
            if (active) {
                Status.toast(LOG_TAG, "Поток уже активен!");
                return;
            }
            active = true;
        }
        this.start();
    }
}