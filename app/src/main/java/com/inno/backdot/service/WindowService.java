package com.inno.backdot.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

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
        registerListener();
        return super.onStartCommand(intent, flags, startId);
    }



    private void registerListener() {
        AccessibilityManager as= (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        as.addAccessibilityStateChangeListener(new AccessibilityManager.AccessibilityStateChangeListener() {


            @Override
            public void onAccessibilityStateChanged(boolean enabled) {
                Log.e("@@@ ","enable:"+enabled);
                if (!enabled){
                    stopSelf();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDot != null) {
            mDot.removeView();
            mDot.setInstanceNull();
        }
    }
}
