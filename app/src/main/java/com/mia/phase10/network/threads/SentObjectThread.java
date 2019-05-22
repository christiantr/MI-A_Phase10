package com.mia.phase10.network.threads;

import android.util.Log;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SentObjectThread implements Runnable {
    private ObjectOutputStream out;
    private Serializable objectSent;
    private static final String TAG = "SENT_OBJECT_THREAD";


    public SentObjectThread(ObjectOutputStream out, Serializable objectSent) {
        this.out = out;
        this.objectSent = objectSent;
    }

    @Override
    public void run() {
        try {
            out.writeObject(this.objectSent);
            out.flush();


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Object sent.");


    }

}
