package com.shanlin.sxf;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by shaoxf on 2018/3/16.
 */

public class MyScrollViw extends ScrollView {

    public MyScrollViw(Context context) {
        super(context);
    }

    public MyScrollViw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        //false--表示没有拦截
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("aa", "down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("aa", "move");
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e("aa", "cancel");

                break;
        }
        //scrollView并不是一个单独的View--他是个ViewGroup---所以这里返回true  会消费掉事件down  move----返回false会向下传递  不过会走down事件不会走move事件
        boolean onTouchEvent = super.onTouchEvent(ev);
        Log.e("aa", "onTouchEvent-------------" + onTouchEvent);
//        return true;
        return super.onTouchEvent(ev);
    }
}
