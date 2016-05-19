package com.inno.backdot.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.inno.backdot.R;
import com.inno.backdot.config.AppHelper;
import com.inno.backdot.engine.Utils;
import com.inno.backdot.reciver.DotReceiver;

public class MainActivity extends AppCompatActivity {

    private Button mBt_Lock;
    private Button mBt_FloatView;
    private DevicePolicyManager dpm;
    private ComponentName who;
    private WebView mWeb;
    private Button mBt_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
//        initAdmin();
        who = new ComponentName(MainActivity.this,DotReceiver.class);
        dpm = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    private void initAdmin() {
        AppHelper.isActive=dpm.isAdminActive(who);
        if (AppHelper.isActive){
            mBt_Lock.setText("已开启");
            mBt_Lock.setEnabled(false);
        }

    }

    private void initView() {
        mBt_Lock = ((Button) findViewById(R.id.bt_lock));
        mBt_Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 指定动作名称
                intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                // 指定给哪个组件授权
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,who);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"说明文档");
                MainActivity.this.startActivityForResult(intent,0);
//                MainActivity.this.startActivity(intent);
            }
        });
        mBt_FloatView = ((Button) findViewById(R.id.bt_floatView));
        mBt_FloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "00000", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                startActivity(intent);

            }
        });

        mWeb = (WebView) findViewById(R.id.webView);
        mWeb.loadUrl("file:///android_asset/html/readme.html");
        mBt_me = ((Button) findViewById(R.id.bt_me));
        mBt_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "by didikee", Toast.LENGTH_LONG).show();
            }
        });

        mBt_me.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean adminActive = dpm.isAdminActive(who);
                if (adminActive){
                    dpm.removeActiveAdmin(who);
                    Toast.makeText(MainActivity.this, "已经移除,你可以重新激活,或者执行卸载操作.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "未激活,请先执行开启锁屏,执行激活操作.", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }
    public void loadAdmin(){
        // 判断该组件是否有系统管理员的权限
//        dpm = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

        boolean adminActive = dpm.isAdminActive(who);
        if (adminActive){
            Toast.makeText(MainActivity.this, "the admin is already exit! do not do it!", Toast.LENGTH_SHORT).show();
            AppHelper.isActive=true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppHelper.isActive = dpm.isAdminActive(who);
        if (AppHelper.isActive){
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
            mBt_FloatView.setText("已开启");
        }else {
            mBt_FloatView.setText("未开启");
        }
        super.onResume();
    }
}
