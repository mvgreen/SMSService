package com.mvgreen.smsservice;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

public class SMSModule {

    private static final String LOG_TAG = "SMSModule";

    public static void sendSafe(Record r) {
        Status.toast(LOG_TAG, "Вызван пустой метод");
    }
    public static void sendReal(Record r) {
        r.tel = "8" + r.tel;

        Status.toast(LOG_TAG, "Запись: id = " + r.id + ", " + r.tel + ": " + r.text);

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(MainActivity.getInstance(), 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(MainActivity.getInstance(), 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        MainActivity.getInstance().registerReceiver(new SendBroadcastReceiver(), new IntentFilter(SENT));

        //---when the SMS has been delivered---
        MainActivity.getInstance().registerReceiver(new DeliverBroadcastReceiver(), new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(r.tel, null, r.text, sentPI, deliveredPI);
    }
}
