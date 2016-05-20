package com.inno.backdot.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.inno.backdot.engine.Dot;

public class DotAccessibilityService extends AccessibilityService {
    public DotAccessibilityService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //通过这个函数可以接收系统发送来的AccessibilityEvent，接收来的
        // AccessibilityEvent是经过过滤的，过滤是在配置工作时设置的。


    }

    @Override
    public void onInterrupt() {
        //这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。
   }

    @Override
    protected boolean onGesture(int gestureId) {
        return super.onGesture(gestureId);
    }

    @Override
    protected void onServiceConnected() {
        Log.e("@@@", "onServiceConnected() called ");
        Dot.getInstance(getApplicationContext()).setAccessbilityServier(this);
        startService(new Intent(this,WindowService.class));
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }


}
