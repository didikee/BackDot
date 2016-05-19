package com.inno.backdot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.inno.backdot.engine.Dot;

public class WindowService extends Service {


    private Dot mDot;

    public WindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDot = Dot.getInstance(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDot != null) {
            mDot.removeView();
        }
    }
}
