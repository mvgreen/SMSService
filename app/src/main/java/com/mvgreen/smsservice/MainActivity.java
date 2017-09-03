package com.mvgreen.smsservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";
    private static final String SERVER_IP = "SERVER_IP";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String EXPECTED_ID = "EXPECTED_ID";
    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO обновлять при повторном открытии
        instance = this;
        Status.toast(LOG_TAG, "В onCreate");
        initView();
    }

    @Override
    protected void onRestart() {
        //initView();
    }

    private void initView() {
        Status.toast(LOG_TAG, "В initView");
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        EditText temp = ((EditText) findViewById(R.id.edit_ip));
        temp.setText(prefs.getString(SERVER_IP, ""));

        temp = ((EditText) findViewById(R.id.edit_deviceid));
        temp.setText(prefs.getString(DEVICE_ID, "1"));

        temp = ((EditText) findViewById(R.id.edit_expected_id));
        temp.setText(prefs.getString(EXPECTED_ID, "1"));

        // TODO предусматривать открытие свернутого приложения с запущенным сервисом
        Button btn = (Button) findViewById(R.id.btn_controll_service);
        btn.setText(getString(R.string.btn_start_service));

        // TODO добавить возможность копировать лог
        final EditText edittext = (EditText) findViewById(R.id.edit_log);
        edittext.setInputType(InputType.TYPE_NULL);
        edittext.setSingleLine(false);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edittext.setSelection(edittext.getText().length());
            }
        });
    }

    public void onConfirmSettings(View view) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (!MainService.isOnline()) {
            EditText ip = (EditText) findViewById(R.id.edit_ip);
            EditText device = (EditText) findViewById(R.id.edit_deviceid);
            EditText expected = (EditText) findViewById(R.id.edit_expected_id);

            if (prefs.edit().putString(SERVER_IP, ip.getText().toString())
                    .putString(DEVICE_ID, device.getText().toString())
                    .putString(EXPECTED_ID, expected.getText().toString()).commit())
                Status.toast(LOG_TAG, getString(R.string.success));
            else
                Status.toast(LOG_TAG, getString(R.string.failed));
        } else
            Status.toast(LOG_TAG, getString(R.string.warning_stop_service_first));
    }

    public void onToggleService(View view) {
        if (MainService.isOnline()) {
            Status.toast(LOG_TAG, getString(R.string.attempt_to_stop));
            if (stopService(new Intent(this, MainService.class))) {
                Status.toast(LOG_TAG, getString(R.string.stop_success_later));
                ((Button) view).setText(getString(R.string.btn_start_service));
            }
            else
                Status.toast(LOG_TAG, getString(R.string.failed));
        }
        else {
            Status.toast(LOG_TAG, getString(R.string.attempt_to_start));
            startService(new Intent(this, MainService.class));
            ((Button) view).setText(getString(R.string.btn_stop_service));
        }
    }
}
