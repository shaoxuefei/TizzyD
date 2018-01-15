package com.shanlin.sxf.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sxf on 2017/9/20.
 */

public class MyService extends Service {
    public static String tag = "com.shanlin.sxf.service.MyService";
    public static Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("aa", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "DemoService", Toast.LENGTH_SHORT).show();
        Log.e("aa", "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("aa", "newThreadStart");
                    Thread.sleep(5000);
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();//Thread  记得Start()启动一下 ！！！
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
//        startForeground(); //是设置在转换在前台进程，一般通过Notification来提醒设置
        return super.bindService(service, conn, flags);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public static void startMyService(Context context, Handler hander) {
        mHandler = hander;
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("tag", tag);
        context.startService(intent);
    }
}
