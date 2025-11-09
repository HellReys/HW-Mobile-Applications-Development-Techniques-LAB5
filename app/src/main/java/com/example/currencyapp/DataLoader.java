package com.example.currencyapp;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataLoader extends AsyncTask<String, Void, List<String>> {

    private final MainActivity activity;

    public DataLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<String> doInBackground(String... urls) {
        if (urls == null || urls.length == 0) return null;
        String urlString = urls[0];
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            int code = connection.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? connection.getInputStream() : connection.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return Parser.parseXML(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(List<String> data) {
        if (data != null) {
            activity.updateData(data, "USD");
        } else {
            ToastHelper.showToast(activity, "Failed to load XML currency data.");
        }
    }
}
