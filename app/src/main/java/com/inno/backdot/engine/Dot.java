package com.inno.backdot.engine;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.inno.backdot.R;

/**
 * Created by didik on 2016/5/12.
 */
public class Dot implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener{

    private static Dot mDot;
    private Context mContext;

    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private static ImageView imageView;
    private GestureDetector mGesture;

    private WindowManager windowManager;
    private Handler mHandler;
    private static AccessibilityService acs;
    private static DevicePolicyManager dpm;

    private Dot(Context mContext){
        this.mContext=mContext;
        this.windowManager= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mHandler=new Handler();
        this.dpm= (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mGesture=new GestureDetector(mContext,this);
        mGesture.setOnDoubleTapListener(this);
        initWindow();
    }

    public static Dot getInstance(Context context){
        if (mDot==null){
            return  new Dot(context);
        }else {
            return mDot;
        }
    }

    public void removeView(){
        if (windowManager!=null && imageView!=null){
            windowManager.removeView(imageView);
        }
    }

    public void setAccessbilityServier(AccessibilityService acs){
        this.acs=acs;
//        Log.e("@@@","被调用");
    }

    private void actionBack(){
        if (acs!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                acs.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"你的手机版本低于 4.03 ,请升级后使用!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else {
            Toast.makeText(mContext,"null",Toast.LENGTH_SHORT).show();
        }
    }
    private void actionLock(){
        if (dpm!=null){
            dpm.lockNow();
        }
    }

    private void actionRecent(){
        if (acs!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                acs.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"你的手机版本低于 4.03 ,请升级后使用!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void initWindow() {
        // 注意，悬浮窗只有一个，而当打开应用的时候才会产生悬浮窗，所以要判断悬浮窗是否已经存在，
        if (imageView != null) {
            windowManager.removeView(imageView);
        }
        // 使用Application context
        // 创建UI控件，避免Activity销毁导致上下文出现问题,因为现在的悬浮窗是系统级别的，不依赖与Activity存在
        imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.btn_normal_pressed);

//        params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;


        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        //TYPE_SYSTEM_ALERT  系统提示,它总是出现在应用程序窗口之上
        //TYPE_SYSTEM_OVERLAY   系统顶层窗口。显示在其他一切内容之上。此窗口不能获得输入焦点，否则影响锁屏
        // FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按,不设置这个flag的话，home页的划屏会有问题
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        params.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;  //显示在屏幕左中部

        //显示位置与指定位置的相对位置差
        params.x = 0;
        params.y = 0;
        //悬浮窗的宽高
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置透明
        params.format = PixelFormat.TRANSPARENT;

        //添加到window中
        windowManager.addView(imageView,params);

        initImageView();
    }

    private void initImageView() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float lastX; //上一次位置的X.Y坐标
            private float lastY;
            private float nowX;  //当前移动位置的X.Y坐标
            private float nowY;
            private float tranX; //悬浮窗移动位置的相对值
            private float tranY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = true;
                ret = mGesture.onTouchEvent(event);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // 获取按下时的X，Y坐标
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动时的X，Y坐标
                        nowX = event.getRawX();
                        nowY = event.getRawY();
                        // 计算XY坐标偏移量
                        tranX = nowX - lastX;
                        tranY = nowY - lastY;
                        // 移动悬浮窗
                        params.x -= tranX;//左对齐是+,右对齐是-
                        params.y += tranY;
                        //更新悬浮窗位置
                        windowManager.updateViewLayout(imageView,params);
                        //记录当前坐标作为下一次计算的上一次移动的位置坐标
                        lastX = nowX;
                        lastY = nowY;
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return ret;
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
//        Toast.makeText(mContext, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
        //返回上一级
        actionBack();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
//        Toast.makeText(mContext, "onDoubleTap", Toast.LENGTH_SHORT).show();
        //锁屏
        actionLock();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    //-------------------------------------------------------------
    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Toast.makeText(mContext, "onLongPress", Toast.LENGTH_SHORT).show();
        //历史应用(相当于长按菜单键)
        actionRecent();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }


}
