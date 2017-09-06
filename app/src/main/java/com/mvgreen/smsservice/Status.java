package com.mvgreen.smsservice;

import android.widget.EditText;

class Status {

    private static int stringsCount = 0;
    static void toast(String logTag, String message) {
        final String s = logTag + ": " + message;
        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.getInstance(), s, Toast.LENGTH_SHORT).show();
                optimiseLog();
                stringsCount++;
                ((EditText) MainActivity.getInstance().findViewById(R.id.edit_log)).getText().append(s + "\n");
            }
        });
    }

    private static void optimiseLog() {
        String t = ((EditText) MainActivity.getInstance().findViewById(R.id.edit_log)).getText().toString();
        while (stringsCount >= 100) {
            t = t.substring(t.indexOf('\n') + 1);
            stringsCount--;
        }
        ((EditText) MainActivity.getInstance().findViewById(R.id.edit_log)).setText(t);
    }
}
