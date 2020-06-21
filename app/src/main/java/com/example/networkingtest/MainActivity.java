package com.example.networkingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button buttonJson = findViewById(R.id.button);

        downloadAndShowJson(textView);

        textView.setMovementMethod(new ScrollingMovementMethod());

        buttonJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndShowJson(v);
            }
        });
    }

    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        assert connManager != null;
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }

    // When user click on the "Download Json".
    public void downloadAndShowJson(View view) {
        boolean networkOK = this.checkInternetConnection();
        if (!networkOK) {
            return;
        }
        String jsonUrl = "https://www.cbr-xml-daily.ru/daily_json.js";

        MyAsyncTask myAsyncTask = new MyAsyncTask(textView);

        // Create a task to download and display json content.
        DownloadJsonTask task = new DownloadJsonTask(myAsyncTask.getMyWeakContext());

        // Execute task (Pass jsonUrl).
        task.execute(jsonUrl);
    }
}
