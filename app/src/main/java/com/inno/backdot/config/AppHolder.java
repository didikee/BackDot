package com.inno.backdot.config;

import android.accessibilityservice.AccessibilityService;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by didik on 2016/5/19.
 */
public class AppHolder extends Application{

    public static WindowManager mWindowManager;

    public static DevicePolicyManager mDevicePolicyManager;

    public static int[] actions=new int[]{1,2,3,4,5};

    private static AppHolder instance ;
    public static AppHolder getinstance(){
        return instance;
    }

    public static AccessibilityService abs=null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        this.mWindowManager= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.mDevicePolicyManager= (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

    }

    public void setASB(AccessibilityService abs){
        this.abs=abs;
    }
}
