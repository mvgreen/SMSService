package com.mvgreen.smsservice;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Data {

    private static final String LOG_TAG = "Data";
    static ArrayList<Record> load() {
        Status.toast(LOG_TAG, "Чтение данных...");
        JSONObject json = null;
        SharedPreferences prefs = MainActivity.getInstance().getPreferences(Context.MODE_PRIVATE);
        String ip = prefs.getString(MainActivity.SERVER_IP, "");
        String device = prefs.getString(MainActivity.DEVICE_ID, "");
        String expectedID = prefs.getString(MainActivity.EXPECTED_ID, "");
        try {
            json = new JSONObject(readUrl("http://" + ip + "/testmessages.php?min_id=" + expectedID + "&id=" + device));
        } catch (Exception e) {
            Status.toast(LOG_TAG, "Не удалось загрузить файл");
        }
        return parseJSON(json, expectedID);
    }

    // TODO написать свой парсер
    private static ArrayList<Record> parseJSON(JSONObject json, String expectedID) {
        ArrayList<Record> ret = new ArrayList<>();
        if (json == null) {
            Status.toast(LOG_TAG, "Записей нет");
            return ret;
        }
        try {
            int id = Integer.parseInt(expectedID);
            while (json.has(Integer.toString(id))) {
                JSONObject o = json.getJSONObject(Integer.toString(id));
                Record r = new Record();
                r.id = o.getInt("id");
                r.tel = o.getString("tel");
                r.text = o.getString("text");
                r.date_timestamp = o.getInt("date_timestamp");
                r.date = o.getString("date");
                ret.add(r);
                id++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
