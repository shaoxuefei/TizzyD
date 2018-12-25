package com.shanlin.sxf.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.shanlin.sxf.utils.FloatMessageWindow;

/**
 * @author : SXF
 * @ date   : 2018/12/18
 * Description :音频常驻Service
 */
public class MyVideoPlayService extends Service {
    MyBinder myBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        doInitVideo();
        myBinder = new MyBinder();
        return myBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);

    }

    //如果有可能需要给Service进行交互，可以用bind方式进行绑定ConnectedListener
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doInitVideo();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doInitVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                //若未授权则请求权限
                Intent intent14 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent14.setData(Uri.parse("package:" + getPackageName()));
                intent14.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent14);
            } else {
                FloatMessageWindow.getInstance(this).showWindows();
            }
        }
    }

    public class MyBinder extends Binder {

    }
}
