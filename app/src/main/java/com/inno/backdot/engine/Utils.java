package com.inno.backdot.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by didik on 2016/5/11.
 */
public class Utils {
//    ActivityManager myManager = (ActivityManager) LoginActivity.this
//            .getApplicationContext().getSystemService(
//                    Context.ACTIVITY_SERVICE);
//    ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
//            .getRunningServices(30);
//    for (int i = 0; i < runningService.size(); i++) {
//        if (runningService.get(i).service.getClassName().toString()
//                .equals(className)) {
//            return true;
//        }
//    }

    /**
     * 判断一个服务是否在运行
     * @return
     */
    public static boolean isServiceActive(Context context,String serviceName){
        ActivityManager mAM= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList
                <ActivityManager.RunningServiceInfo>) mAM.getRunningServices(50);
        for (int i=0;i<runningServices.size();i++){
            Log.e("@@@","+++"+runningServices.get(i).service.getClassName().toString());
            if (runningServices.get(i).service.getClassName().toString().equalsIgnoreCase(serviceName)){
                return  true;
            }
        }
        return  false;
    }


}
