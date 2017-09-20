package com.shanlin.sxf.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Sxf on 2017/9/20.
 * 做异步请求的Service--IntentService
 */

public class MyIntentService extends IntentService {
    public static String tag="com.shanlin.sxf.service.MyIntentService";
    public static Handler mHandler;


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String tag = intent.getStringExtra("tag");
        Log.e("aa","进入耗时操作");
        if(!TextUtils.isEmpty(tag)&&tag.equals(tag)){
            try {
                Thread.sleep(5000);
                //更新Ui
                if(mHandler!=null){
                    mHandler.sendEmptyMessage(1);
                    Log.e("aa","发送Handler");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void startIntentService(Context context, Handler handler){
        mHandler=handler;
        Intent intent=new Intent(context,MyIntentService.class);
        intent.putExtra("tag",tag);
        context.startService(intent);
    }
}
