package com.shanlin.sxf.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Chronometer;

/**
 * @author : SXF
 * @ date   : 2018/8/10
 * Description :
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    Chronometer chronometer;
    AudioRecord audioRecord;
    MediaRecorder mediaRecorder;

    public MySurfaceView(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    //surfaceView的生命周期
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //runnable方法
    @Override
    public void run() {

    }


    private void initView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        //获取画布、锁定画布
        Canvas canvas = holder.lockCanvas();

    }
}
