package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public class FibService extends Service {

    public static final String CHANNEL = "FIB_SERVICE";

    public static final String PARAM = "PARAM";

    public static final String RESULT = "RESULT";
    private Handler h;

    class FibCalculate implements Runnable {
        private final int param;

        public FibCalculate(int param) {
            this.param = param;
        }

        @Override
        public void run() {
            int a = 1, b = 1, t;
            for (int i = 0; i < param; i++) {
                t = b;
                b = a + b;
                a = t;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Bundle ans = new Bundle();
            ans.putInt(RESULT, a);
            Message m = new Message();
            m.setData(ans);
            h.sendMessage(m);
        }
    }

    @Override
    public void onCreate() {
        h = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Intent i = new Intent(CHANNEL);
                i.putExtra(RESULT, msg.getData().getInt(RESULT));
                sendBroadcast(i);
            }
        };
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new FibCalculate(intent.getIntExtra(PARAM, 0))).start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}