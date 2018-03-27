package com.shanlin.sxf.diytouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author : SXF
 * @ date   : 2018/3/23
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 默认
     * MyLinearLayout---onInterceptTouchEvent-----false
     * MyTextView---onTouchEvent-----false
     * MyTextView---dispatchTouchEvent-----false
     * MyTextView---onTouchEvent-----false
     * MyLinearLayout---onTouchEvent-----false
     * MyLinearLayout---dispatchTouchEvent-----false
     * MyLinearLayout---onInterceptTouchEvent-----false
     * MyTextView---onTouchEvent-----false
     * MyTextView---dispatchTouchEvent-----false
     * MyTextView---onTouchEvent-----false
     * MyLinearLayout---onTouchEvent-----false
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.e("aa", "MyTextView---dispatchTouchEvent-----" + super.dispatchTouchEvent(event));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         *  MyLinearLayout---onInterceptTouchEvent-----false
         MyTextView---onTouchEvent-----false
         MyLinearLayout---onTouchEvent-----false
         */
        Log.e("aa", "MyTextView---onTouchEvent-----" + super.onTouchEvent(event));
        //false表示不消耗  直接向上传----true表示消耗不会向上传onTouchEvent
        return true;
    }
}
