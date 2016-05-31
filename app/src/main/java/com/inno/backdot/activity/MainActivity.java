package com.inno.backdot.activity;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.inno.backdot.R;
import com.inno.backdot.config.AppConfig;
import com.inno.backdot.config.AppHolder;
import com.inno.backdot.engine.Dot;
import com.inno.backdot.engine.Utils;
import com.inno.backdot.reciver.DotReceiver;
import com.inno.backdot.service.DotAccessibilityService;
import com.inno.backdot.service.WindowService;
import com.inno.backdot.utils.SPUtil;
import com.inno.backdot.view.PopWindow;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch mSw_Lock;
    private Switch mSw_FloatView;
    private ComponentName who;
    private Button mBt_me;
    private LinearLayout ll_theme;
    private LinearLayout ll_mode;
    private LinearLayout ll_help;
    private LinearLayout ll_me;

    private RelativeLayout mRe_1;
    private RelativeLayout mRe_2;
    private RelativeLayout mRe_3;
    private RelativeLayout mRe_4;
    private RelativeLayout mRe_5;
    //文字说明
    private TextView mLock_info;
    private TextView mFloatView_info;
    private TextView tv_info_1;
    private TextView tv_info_2;
    private TextView tv_info_3;
    private TextView tv_info_4;
    private TextView tv_info_5;

    public static final String[] mList=new String[]{"返回键","锁屏","回到桌面","显示下拉通知","显示最近应用"};
    private View rootView;
    private View v_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    private void init() {
        initView();
        initFirstTime();
        initInfos();
        initAdmin();
        who = new ComponentName(MainActivity.this,DotReceiver.class);
    }

    private void initInfos() {
        int info1 = SPUtil.getInt(this, AppConfig.ACTION_BACK, 1);
        int info2 = SPUtil.getInt(this,AppConfig.ACTION_LOCK,2);
        int info3 = SPUtil.getInt(this,AppConfig.ACTION_LONG_CLICK,3);
        int info4 = SPUtil.getInt(this,AppConfig.ACTION_PULL_DOWN,4);
        int info5 = SPUtil.getInt(this,AppConfig.ACTION_PULL_UP,5);
        setText(tv_info_1,info1);
        AppHolder.actions[0]=info1;
        setText(tv_info_2,info2);
        AppHolder.actions[1]=info2;
        setText(tv_info_3,info3);
        AppHolder.actions[2]=info3;
        setText(tv_info_4,info4);
        AppHolder.actions[3]=info4;
        setText(tv_info_5,info5);
        AppHolder.actions[4]=info5;
    }
    private void setText(TextView textView,int which){
        textView.setText("( "+mList[which-1]+" )");
    }

    private void initFirstTime() {
        int count = SPUtil.getInt(this, AppConfig.USER_COUNT, 0);
        if (count==0){
            SPUtil.putInt(this,AppConfig.ACTION_BACK,1);
            SPUtil.putInt(this,AppConfig.ACTION_LOCK,2);
            SPUtil.putInt(this,AppConfig.ACTION_LONG_CLICK,3);
            SPUtil.putInt(this,AppConfig.ACTION_PULL_DOWN,4);
            SPUtil.putInt(this,AppConfig.ACTION_PULL_UP,5);
            count++;
            SPUtil.putInt(this,AppConfig.USER_COUNT,count);
        }else {
            count++;
            SPUtil.putInt(this,AppConfig.USER_COUNT,count);
        }
    }

    private void initAdmin() {
        AppConfig.isActive= AppHolder.mDevicePolicyManager.isAdminActive(who);
        if (AppConfig.isActive){
            mLock_info.setText("( 已开启 )");
            mSw_Lock.setChecked(true);
        }else {
            mLock_info.setText("( 已关闭 )");
            mSw_Lock.setChecked(false);
        }

    }

    private void initView() {
        mSw_Lock = (Switch) findViewById(R.id.sw_lock);
        mSw_FloatView=((Switch) findViewById(R.id.sw_dot));

        ll_theme = (LinearLayout) findViewById(R.id.ll_theme);
        ll_mode = (LinearLayout) findViewById(R.id.ll_mode);

        ll_help= (LinearLayout) findViewById(R.id.ll_help);
        ll_me= (LinearLayout) findViewById(R.id.ll_me);

        /**五种操作的设置监听**/
        mRe_1 = ((RelativeLayout) findViewById(R.id.re_1));
        mRe_2 = ((RelativeLayout) findViewById(R.id.re_2));
        mRe_3 = ((RelativeLayout) findViewById(R.id.re_3));
        mRe_4 = ((RelativeLayout) findViewById(R.id.re_4));
        mRe_5 = ((RelativeLayout) findViewById(R.id.re_5));

        /**文字描述**/
        mLock_info = ((TextView) findViewById(R.id.tv_lock_text));
        mFloatView_info = ((TextView) findViewById(R.id.tv_dot_text));
        tv_info_1 = ((TextView) findViewById(R.id.tv2_1_info));
        tv_info_2 = ((TextView) findViewById(R.id.tv2_2_info));
        tv_info_3 = ((TextView) findViewById(R.id.tv2_3_info));
        tv_info_4 = ((TextView) findViewById(R.id.tv2_4_info));
        tv_info_5 = ((TextView) findViewById(R.id.tv2_5_info));

//        rootView = findViewById(R.id.rootView);
        v_line = ((View) findViewById(R.id.v_line));

        ll_theme.setOnClickListener(this);
        ll_mode.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        mRe_1.setOnClickListener(this);
        mRe_2.setOnClickListener(this);
        mRe_3.setOnClickListener(this);
        mRe_4.setOnClickListener(this);
        mRe_5.setOnClickListener(this);

        //获取或取消 锁屏权限
        mSw_Lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean adminActive = AppHolder.mDevicePolicyManager.isAdminActive(who);
                if (isChecked ){
                    if (!adminActive){
                        Intent intent = new Intent();
                        // 指定动作名称
                        intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        // 指定给哪个组件授权
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,who);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"说明文档");
                        MainActivity.this.startActivityForResult(intent,0);
                    }
                    mLock_info.setText("( 已开启 )");
                }else {
                    if(adminActive){
                        AppHolder.mDevicePolicyManager.removeActiveAdmin(who);
                        Toast.makeText(MainActivity.this, "已经移除,你可以重新激活,或者执行卸载操作.", Toast.LENGTH_LONG).show();
                    }
                    mLock_info.setText("( 已关闭 )");
                }
            }
        });

        mSw_FloatView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean serviceActive = Utils.isServiceActive(MainActivity.this, "com.inno.backdot.service.WindowService");
                if (isChecked){
                    if (!serviceActive){
                        boolean asbServer = Utils.isServiceActive(MainActivity.this, "com" +
                                ".inno.backdot.service.DotAccessibilityService");

                        if (asbServer){
                            Log.e("@@@","居然为真");
                            if (AppHolder.abs!=null) {
                                Dot.getInstance(MainActivity.this).setAccessbilityServier(AppHolder.abs);
                            }
                            startService(new Intent(MainActivity.this, WindowService.class));
                            return;
                        }else {
                            Log.e("@@@","居然为假");
                        }
                        checkSDK23();
                    }
                    mFloatView_info.setText("( 已开启 )");
                }else {
                    if (serviceActive){
                        stopService(new Intent(MainActivity.this, WindowService.class));
                        stopService(new Intent(MainActivity.this, DotAccessibilityService.class));
                    }
                    mFloatView_info.setText("( 已关闭 )");
                }
            }
        });
    }
    private void checkSDK23() {
        if (Build.VERSION.SDK_INT>=23){
            checkDrawOverlayPermission();
        }else {
            Intent intent=new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            startActivity(intent);
        }
    }


    @SuppressLint("NewApi")
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, 1234);
        }else {
            Intent intent=new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            startActivity(intent);
        }
    }
    public void loadAdmin(){
        // 判断该组件是否有系统管理员的权限
//        dpm = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

        boolean adminActive = AppHolder.mDevicePolicyManager.isAdminActive(who);
        if (adminActive){
            Toast.makeText(MainActivity.this, "the admin is already exit! do not do it!", Toast.LENGTH_SHORT).show();
            AppConfig.isActive=true;
            return;
        }

        // policyManager.lockNow(); // 锁屏
//            policyManager.resetPassword("123", 0); // 设置锁屏密码
        // devicePolicyManager.wipeData(0); 恢复出厂设置 (建议大家不要在真机上测试)
        // 模拟器不支持该操作
        Intent intent = new Intent();
        // 指定动作名称
        intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 指定给哪个组件授权
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,who);
        MainActivity.this.startActivityForResult(intent,0);

    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1234){
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                Intent intent=new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                startActivity(intent);
            }
            return;
        }
        AppConfig.isActive = AppHolder.mDevicePolicyManager.isAdminActive(who);
        if (AppConfig.isActive){
            Log.e("@@@","result+已激活");
        }else {
            Log.e("@@@","result+未激活");
        }

    }

    @Override
    protected void onResume() {
        initAdmin();
        boolean serviceActive = Utils.isServiceActive(MainActivity.this, "com.inno.backdot.service.WindowService");
        if (serviceActive){
            mFloatView_info.setText("( 已开启 )");
            mSw_FloatView.setChecked(true);
        }else {
            mFloatView_info.setText("( 已关闭 )");
            mSw_FloatView.setChecked(false);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.re_1:
                click_RX(v,0);
                break;
            case R.id.re_2:
                click_RX(v,1);
                break;
            case R.id.re_3:
                click_RX(v,2);
                break;
            case R.id.re_4:
                click_RX(v,3);
                break;
            case R.id.re_5:
                click_RX(v,4);
                break;
            case R.id.ll_mode:

                break;
            case  R.id.ll_theme:
                Toast.makeText(MainActivity.this,"计划之中!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_help:
                intent=new Intent(MainActivity.this,HelpActivity.class);
                intent.putExtra("type","help");
                startActivity(intent);
                break;
            case R.id.ll_me:
                intent=new Intent(MainActivity.this,HelpActivity.class);
                intent.putExtra("type","me");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void click_RX(View v, final int index) {
        PopWindow popWindow =null;
        switch (index){
            case 0:
                popWindow = new PopWindow(MainActivity.this, tv_info_1, v_line, AppHolder.actions[0]-1, v);
                break;
            case 1:
                popWindow = new PopWindow(MainActivity.this, tv_info_2, v_line, AppHolder.actions[1]-1, v);
                break;
            case 2:
                popWindow = new PopWindow(MainActivity.this, tv_info_3, v_line, AppHolder.actions[2]-1, v);
                break;
            case 3:
                popWindow = new PopWindow(MainActivity.this, tv_info_4, v_line,  AppHolder.actions[3]-1, v);
                break;
            case 4:
                popWindow = new PopWindow(MainActivity.this, tv_info_5, v_line,  AppHolder.actions[4]-1, v);
                break;
            default:
                break;
        }
        if (popWindow==null){
            Toast.makeText(MainActivity.this,"窗口为null!",Toast.LENGTH_SHORT).show();
            return;
        }
        popWindow.setonItemCheckedLister(new PopWindow.onItemCheckedLister() {
            @Override
            public void checked(int position) {
                switch (index){
                    case 0:
                        SPUtil.putInt(MainActivity.this,AppConfig.ACTION_BACK,position+1);
                        AppHolder.actions[0]=position+1;
                        break;
                    case 1:
                        SPUtil.putInt(MainActivity.this,AppConfig.ACTION_LOCK,position+1);
                        AppHolder.actions[1]=position+1;
                        break;
                    case 2:
                        SPUtil.putInt(MainActivity.this,AppConfig.ACTION_LONG_CLICK,position+1);
                        AppHolder.actions[2]=position+1;
                        break;
                    case 3:
                        SPUtil.putInt(MainActivity.this,AppConfig.ACTION_PULL_DOWN,position+1);
                        AppHolder.actions[3]=position+1;
                        break;
                    case 4:
                        SPUtil.putInt(MainActivity.this,AppConfig.ACTION_PULL_UP,position+1);
                        AppHolder.actions[4]=position+1;
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
