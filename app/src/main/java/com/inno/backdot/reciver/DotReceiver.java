package com.inno.backdot.reciver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DotReceiver extends DeviceAdminReceiver {
    public DotReceiver() {
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i("log", "onDisabled");
        super.onDisabled(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i("log", "onEnabled");
        super.onEnabled(context, intent);
    }
}
