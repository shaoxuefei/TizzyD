package com.shanlin.sxf.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sxf on 2017/9/20.
 * 生命周期太短--在接收到广播的时候 开始创建，然后在onReceive()方法结束的时候 销毁，故不建议在onReceive()中做耗时操作或者是newThread();
 */

public class MyBroadCastReceive extends BroadcastReceiver{

    Handler handler;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"A接受到了通知,停2s",Toast.LENGTH_SHORT).show();
        Log.e("aa","A接受到了通知");
//        abortBroadcast();//终止广播
    }


}
