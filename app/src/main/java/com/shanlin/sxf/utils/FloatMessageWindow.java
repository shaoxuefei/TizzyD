package com.shanlin.sxf.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.Main2Activity;
import com.shanlin.sxf.R;
import com.shanlin.sxf.application.MyApplication;
import com.shanlin.sxf.diyview.DiyFloatView;

import java.lang.ref.WeakReference;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author : SXF
 * @ date   : 2018/12/18
 * Description : 悬浮window
 */
public class FloatMessageWindow implements DiyFloatView.OnItemClickListener {

    private static FloatMessageWindow floatMessageWindow;
    private static WeakReference<Context> weakReference;

    private FloatMessageWindow() {
    }

    public static FloatMessageWindow getInstance(Context context) {
        if (floatMessageWindow == null) {
            floatMessageWindow = new FloatMessageWindow();
        }
        weakReference = new WeakReference<>(context);
        return floatMessageWindow;
    }

    private WindowManager.LayoutParams layoutParams;
    private int screenWidth, screenHeight;
    private WindowManager windowManager;
    DiyFloatView inflate;
    private TextView tvPlay;
    MediaPlayUtils mediaPlayer;

    public void showWindows() {
        mediaPlayer = new MediaPlayUtils();
        Context context = weakReference.get();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;


        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;

        //窗口级别
        //这种是在每个应用的上方显示，需要SYSTEM_ALERT_WINDOW权限、这种适合用于App外部浮窗
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        }
        //不推荐
//        layoutParams.type=WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.x = 10;
        layoutParams.y = screenHeight / 2;
        layoutParams.gravity = Gravity.START | Gravity.TOP;

        inflate = new DiyFloatView(context);
        inflate.setOnTypeClick(this);
        inflate.setOnTouchListener(new FloatingTouchListener());
        windowManager.addView(inflate, layoutParams);
    }

    public void addFloatView() {
        if (windowManager != null && inflate != null && !isDelete) {
            windowManager.addView(inflate, layoutParams);
        }
    }

    public void removeFloatingView() {
        try {
            if (windowManager != null && inflate != null) {
                windowManager.removeView(inflate);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onPlay() {
//        String voicePath = Environment.getExternalStorageDirectory() + "/msc/tts.wav";
        isDelete = false;
        String voicePath = "http://file.go-goal.cn/ggfiles/zyztfm/7f6d83ea.早报：深交所称将推动放宽创业板上市条件.mp3";
        mediaPlayer.startPlay(voicePath);
        buildNotification();
    }

    @Override
    public void onPause() {
        mediaPlayer.onPause();
    }

    boolean isDelete;

    @Override
    public void onDelete() {
        mediaPlayer.onDestory();
        removeFloatingView();
        //TODO 解绑
        isDelete = true;

    }

    class FloatingTouchListener implements ViewGroup.OnTouchListener {
        float startX, startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
//                    Log.e("aa", "startX--->" + event.getX() + "---->RawX" + event.getRawX());
//                    Log.e("aa", "startY--->" + event.getY() + "----->RawY" + event.getRawY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    //x,y是相对于WindowLayoutManager的独有的特性
                    layoutParams.x = (int) event.getRawX();
                    layoutParams.y = (int) event.getRawY();
                    //相对于屏幕左边点的x\y值
                    startX = event.getRawX();
                    startY = event.getRawY();
                    windowManager.updateViewLayout(inflate, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (startX > (screenWidth * 1f) / 2) {
                        layoutParams.x = screenWidth;
                    } else {
                        layoutParams.x = 10;
                    }
                    layoutParams.y = (int) startY;
                    windowManager.updateViewLayout(inflate, layoutParams);
                    break;
            }
            return false;
        }
    }


    /**
     * 弹窗显示
     */
    int notityId = 1001;
    MyBroCastReceive myBroCastReceive;
    String clickAction = "onClickAction";
    NotificationManager notificationManager;

    public void buildNotification() {
        Context context = weakReference.get();
        DiyFloatView floatView = new DiyFloatView(context);
        myBroCastReceive = new MyBroCastReceive();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(clickAction);

        //动态注册广播接受者
        context.registerReceiver(myBroCastReceive, intentFilter);


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.float_window_view);

        Intent intentClick = new Intent(clickAction);
        intentClick.putExtra("id", "play");
        remoteViews.setOnClickPendingIntent(R.id.tv_play, PendingIntent.getBroadcast(context, 0x11, intentClick, 0));


        Intent intentPause = new Intent(clickAction);
        intentPause.putExtra("id", "pause");
        remoteViews.setOnClickPendingIntent(R.id.tv_pause, PendingIntent.getBroadcast(context, 0x12, intentPause, 0));


        Intent intentDelete = new Intent(clickAction);
        intentDelete.putExtra("id", "delete");
        remoteViews.setOnClickPendingIntent(R.id.tv_delete, PendingIntent.getBroadcast(context, 0x13, intentDelete, 0));

        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String channel_id = "float_window_channel_id";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id, "float_window_channel_name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        //
        Notification.MediaStyle mMediaStyle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mMediaStyle = new Notification.MediaStyle();
            mMediaStyle.setShowActionsInCompactView(0, 1, 2);
        }

        //TODO message的内容message.getContent()可能是个Json格式包含跳转类型等
        Notification notification = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle("语音播报")
                .setContentText("科大讯飞科大讯飞科大讯飞科大讯飞科大讯飞科大讯飞")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
//                .setCustomContentView(remoteViews)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setPriority(Notification.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle())
                .build();


        Intent intent = new Intent(context, Main2Activity.class);
        intent.setPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0x11, intent, 0);
        notification.contentIntent = pendingIntent;
        if (notificationManager != null) {
            notificationManager.notify(notityId, notification);
        }
    }


    class MyBroCastReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra("id");
            Log.e("aa", "id---->" + id);
            switch (id) {
                case "play":
//                    Toast.makeText(context, "play", Toast.LENGTH_SHORT).show();
                    onPlay();
                    break;
                case "pause":
//                    Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show();
                    onPause();
                    break;
                case "delete":
//                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                    onDelete();
                    notificationManager.cancel(notityId);
                    break;
            }

        }
    }

}
