package com.example.networkingtest;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class DownloadJsonTask extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> textViewWeakReference;

    DownloadJsonTask(WeakReference<TextView> textViewWeakReference) {
        this.textViewWeakReference = textViewWeakReference;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

                JSONObject g = jsonObject.getJSONObject("Valute");

                JSONArray c = g.names();


                StringBuilder ff = new StringBuilder();

                if (c != null) {
                    for (int i = 0; i < c.length(); ++i) {
                        JSONObject mm = g.getJSONObject(c.getString(i));

                        ff.append(mm).append(System.lineSeparator());
                    }
                }



                return ff.toString();
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
