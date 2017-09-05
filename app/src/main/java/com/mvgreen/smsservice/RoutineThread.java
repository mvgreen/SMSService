package com.mvgreen.smsservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

public class RoutineThread extends Thread {

    private static final String LOG_TAG = "Routine";
    private boolean active;
    private static RoutineThread instance;

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
        /*long startTime = System.currentTimeMillis();

        SharedPreferences prefs = MainActivity.getInstance().getPreferences(Context.MODE_PRIVATE);
        //final int expectedID = prefs.getInt(key, 1);

        final ArrayList<Record> fromServer = Data.load(prefs.getInt(key, 1));

        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.getInstance(), "Новых сообщений: " + fromServer.size(), Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<Record> records = DataReplacer.getInstance().load();

        if (records.isEmpty() && fromServer.isEmpty())
            return (System.currentTimeMillis() - startTime);

        int i;
        for (i = 0; i < records.size(); i++) {
            SMSMessenger.send(records.get(i));
        }

        // TODO продолжать работу, ориентируясь на данные сохраненные в оперативке.
        // TODO зависит от id последней записи, а не id последнего отправленного
        if (!fromServer.isEmpty() && !prefs.edit().putInt(key, fromServer.get(fromServer.size() - 1).id + 1).commit())
            Status.e("Routine", "Не удалось сохранить ID");

        if (!prefs.edit().putInt("SMS Count", prefs.getInt("SMS Count", 0) + i).commit())
            Status.e("Routine", "Не удалось сохранить статистику");

        MainActivity.getInstance().updateStatistics();

        return (System.currentTimeMillis() - startTime);
        */
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