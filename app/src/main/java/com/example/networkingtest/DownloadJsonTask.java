package com.example.networkingtest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadJsonTask extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> textViewWeakReference;

    DownloadJsonTask(WeakReference<TextView> textViewWeakReference) {
        this.textViewWeakReference = textViewWeakReference;
    }

    @Override
    protected String doInBackground(String... params) {
        String textUrl = params[0];

        InputStream in = null;
        BufferedReader br = null;

        try {
            URL url = new URL(textUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            int resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {

                in = httpConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }

                JSONObject jsonObject = new JSONObject(sb.toString());

                return String.valueOf(jsonObject.getJSONObject("rates"));
            } else {

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        TextView textView = textViewWeakReference.get();

        if (result != null) {
            textView.setText(result);
        } else {
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }
}
