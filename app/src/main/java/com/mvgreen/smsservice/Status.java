package com.mvgreen.smsservice;

import android.widget.EditText;
import android.widget.Toast;

class Status {

    static void toast(String logTag, String message) {
        final String s = logTag + ": " + message;
        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.getInstance(), s, Toast.LENGTH_SHORT).show();
                ((EditText) MainActivity.getInstance().findViewById(R.id.edit_log)).getText().append("\n" + s);
            }
        });
    }
}
