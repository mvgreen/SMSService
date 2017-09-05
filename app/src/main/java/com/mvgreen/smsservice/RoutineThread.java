package com.mvgreen.smsservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

public class RoutineThread extends Thread {

    private static final String LOG_TAG = "Routine";
    private boolean active;
    private static RoutineThread instance;
    private final String EXPECTED_ID = "expected_id";

    @Override
    public void run() {
        instance = this;
        while (true) {
            synchronized (this) {
                if (!active)
                    return;
            }

            routine();
        }
    }

    private void routine() {
        SharedPreferences prefs = MainActivity.getInstance().getPreferences(Context.MODE_PRIVATE);

        final ArrayList<Record> records = Data.load(prefs.getInt(EXPECTED_ID, 1));

        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Status.toast(LOG_TAG,"Новых сообщений: " + records.size());
            }
        });

        if (records.isEmpty())
            return;

        for (Record r : records)
            SMSModule.send(r);

        int newExpected = records.isEmpty() ? prefs.getInt(EXPECTED_ID, 1) : (records.get(records.size() - 1).id + 1);

        // TODO продолжать работу, ориентируясь на данные сохраненные в оперативке
        if (records.isEmpty())
            Status.toast(LOG_TAG, "Новых сообщений нет");
        else if (!prefs.edit().putInt(EXPECTED_ID, records.get(records.size() - 1).id + 1).commit())
            Status.toast(LOG_TAG, "Новый ожидаемый ID не сохранен");
        else
            Status.toast(LOG_TAG, "Новый ожидаемый ID: " + newExpected);

        if (!prefs.edit().putInt("SMS Count", prefs.getInt("SMS Count", 0) + records.size()).commit())
            Status.toast(LOG_TAG, "Не удалось сохранить статистику");

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