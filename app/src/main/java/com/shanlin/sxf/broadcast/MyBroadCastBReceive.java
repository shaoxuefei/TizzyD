package com.shanlin.sxf.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sxf on 2017/9/20.
 */

public class MyBroadCastBReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"B接受到了通知",Toast.LENGTH_SHORT).show();
        Log.e("aa","B接受到了通知");
    }
}
