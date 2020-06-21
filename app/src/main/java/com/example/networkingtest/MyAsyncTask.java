package com.example.networkingtest;

import android.widget.TextView;

import java.lang.ref.WeakReference;

class MyAsyncTask {
    private WeakReference<TextView> myWeakContext;

    MyAsyncTask(TextView textView) {
        myWeakContext = new WeakReference<>(textView);
    }

    WeakReference<TextView> getMyWeakContext() {
        return myWeakContext;
    }
}
