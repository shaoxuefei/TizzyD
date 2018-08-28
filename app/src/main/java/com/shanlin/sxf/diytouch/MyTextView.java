package com.shanlin.sxf.diytouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : SXF
 * @ date   : 2018/3/23
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class MyTextView extends TextView {
    private GestureDetector gestureDetector;
    private Context context;


    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        gestureDetector = new GestureDetector(context, new MyGestureDetectorListener());
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
        Log.e("aa", ">>>>>>>>>>>>>>MyTextView----MotionEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         MyLinearLayout---onInterceptTouchEvent-----false
         MyTextView---onTouchEvent-----false
         MyLinearLayout---onTouchEvent-----false
         */
        Log.e("aa", ">>>>>>>>>>>>>>MyTextView----onTouchEvent");

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Toast.makeText(getContext(), "ACTION_POINTER_DOWN", Toast.LENGTH_SHORT).show();
                break;
        }

        //TODO 调用手势
        gestureDetector.onTouchEvent(event);

        //false表示不消耗  直接向上传----true表示消耗不会向上传onTouchEvent
        return true;
    }

    class MyGestureDetectorListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(context, "onSingleTapUp", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            int action = e.getAction();
            Toast.makeText(context, "LongPress", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
